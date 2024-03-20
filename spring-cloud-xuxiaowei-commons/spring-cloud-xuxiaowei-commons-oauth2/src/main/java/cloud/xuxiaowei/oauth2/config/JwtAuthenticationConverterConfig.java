package cloud.xuxiaowei.oauth2.config;

import cloud.xuxiaowei.oauth2.constant.OAuth2Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Security 注解鉴权配置，获取权限数据配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@EnableMethodSecurity
public class JwtAuthenticationConverterConfig {

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

}
