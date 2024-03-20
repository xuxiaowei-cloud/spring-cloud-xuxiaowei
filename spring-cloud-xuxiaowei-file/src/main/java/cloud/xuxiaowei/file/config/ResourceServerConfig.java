package cloud.xuxiaowei.file.config;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.core.utils.SecurityUtils;
import cloud.xuxiaowei.utils.constant.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
@EnableMethodSecurity
public class ResourceServerConfig {

	private SecurityProperties securityProperties;

	@Autowired
	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	/**
	 * 允许从 URL 参数中获取 Token，参数名 access_token
	 */
	@Bean
	public BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
		bearerTokenResolver.setAllowUriQueryParameter(true);
		return bearerTokenResolver;
	}

	/**
	 * 权限数据读取位置、设置为无权限前缀
	 * <p>
	 * 需要搭配 {@link EnableMethodSecurity#prePostEnabled} 设置为 {@link Boolean#TRUE} 使用
	 */
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		// 设置 Token 中获取权限数据的声明名称
		grantedAuthoritiesConverter.setAuthoritiesClaimName(OAuth2Constants.AUTHORITIES);

		// 设置成无前缀
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		List<SecurityProperties.RequestMatcher> requestMatchers = securityProperties.getRequestMatchers();

		http.authorizeHttpRequests(authorizeRequestsCustomizer -> {

			SecurityUtils.authorizeRequests(requestMatchers, authorizeRequestsCustomizer);

			// 其他地址：需要授权访问，此配置要放在最后一行
			authorizeRequestsCustomizer.anyRequest().authenticated();

		});

		http.oauth2ResourceServer(oauth2ResourceServerCustomizer -> {
			oauth2ResourceServerCustomizer.jwt(oauth2ResourceServer -> {
				RSAPublicKey rsaPublicKey = securityProperties.rsaPublicKey();
				NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
					.withPublicKey(rsaPublicKey);
				NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
				oauth2ResourceServer.decoder(nimbusJwtDecoder);
			});
		});

		return http.build();
	}

}
