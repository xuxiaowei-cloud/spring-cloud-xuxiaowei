/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloud.xuxiaowei.passport.config;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.core.utils.SecurityUtils;
import cloud.xuxiaowei.utils.constant.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Joe Grandja
 * @author xuxiaowei
 * @since 0.1.0
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
	 * 需要搭配 {@link EnableGlobalMethodSecurity#prePostEnabled} 设置为 {@link Boolean#TRUE} 使用
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

		http.authorizeRequests(authorizeRequests -> {

			SecurityUtils.authorizeRequests(requestMatchers, authorizeRequests);

			// 其他地址：需要授权访问，此配置要放在最后一行
			authorizeRequests.anyRequest().authenticated();

		}).formLogin(withDefaults());

		http.oauth2ResourceServer().jwt(oauth2ResourceServer -> {
			RSAPublicKey rsaPublicKey = securityProperties.rsaPublicKey();
			NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
				.withPublicKey(rsaPublicKey);
			NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
			oauth2ResourceServer.decoder(nimbusJwtDecoder);
		});

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

}
