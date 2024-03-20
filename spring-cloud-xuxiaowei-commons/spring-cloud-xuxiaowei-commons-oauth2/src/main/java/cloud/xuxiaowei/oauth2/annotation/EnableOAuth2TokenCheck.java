package cloud.xuxiaowei.oauth2.annotation;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.oauth2.config.JwtAuthenticationConverterConfig;
import cloud.xuxiaowei.oauth2.constant.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.lang.annotation.*;

/**
 * 此注解用于开启 JWT Token 检查
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see JwtAuthenticationConverter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ EnableOAuth2TokenCheck.AuthenticationTokenConverterConfig.class })
public @interface EnableOAuth2TokenCheck {

	class AuthenticationTokenConverterConfig {

		private StringRedisTemplate stringRedisTemplate;

		private SecurityProperties securityProperties;

		@Autowired
		public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
			this.stringRedisTemplate = stringRedisTemplate;
		}

		@Autowired
		public void setSecurityProperties(SecurityProperties securityProperties) {
			this.securityProperties = securityProperties;
		}

		@Bean
		public JwtAuthenticationConverter jwtAuthenticationConverter() {

			JwtAuthenticationConverter jwtAuthenticationConverter = JwtAuthenticationConverterConfig.jwtConverter();

			JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			jwtGrantedAuthoritiesConverter.setAuthorityPrefix(OAuth2Constants.AUTHORITY_PREFIX);
			jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(OAuth2Constants.AUTHORITIES);

			jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

				String tokenValue = jwt.getTokenValue();

				String key = securityProperties.getTokenCheckPrefix() + tokenValue;

				Boolean hassedKey = stringRedisTemplate.hasKey(key);

				if (hassedKey == null || !hassedKey) {
					throw new InvalidBearerTokenException("JWT 无效");
				}

				return jwtGrantedAuthoritiesConverter.convert(jwt);
			});

			return jwtAuthenticationConverter;
		}

	}

}
