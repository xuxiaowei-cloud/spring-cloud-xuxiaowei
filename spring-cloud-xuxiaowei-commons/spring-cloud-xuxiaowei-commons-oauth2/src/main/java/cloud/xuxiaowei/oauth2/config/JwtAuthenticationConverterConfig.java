package cloud.xuxiaowei.oauth2.config;

import cloud.xuxiaowei.oauth2.constant.OAuth2Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Security 注解鉴权配置，获取权限数据配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtAuthenticationConverterConfig {

	/**
	 * 权限数据读取位置、设置为无权限前缀
	 * <p>
	 * 需要搭配 {@link EnableGlobalMethodSecurity#prePostEnabled} 设置为 {@link Boolean#TRUE} 使用
	 */
	@Bean
	@ConditionalOnMissingBean(JwtAuthenticationConverter.class)
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		return jwtConverter();
	}

	public static JwtAuthenticationConverter jwtConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		// 设置 Token 中获取权限数据的声明名称
		grantedAuthoritiesConverter.setAuthoritiesClaimName(OAuth2Constants.AUTHORITIES);

		// 设置成无前缀
		grantedAuthoritiesConverter.setAuthorityPrefix(OAuth2Constants.AUTHORITY_PREFIX);

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

}
