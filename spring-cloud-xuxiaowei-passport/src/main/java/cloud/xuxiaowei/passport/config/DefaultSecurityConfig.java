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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Joe Grandja
 * @author xuxiaowei
 * @since 0.1.0
 */
@EnableWebSecurity
public class DefaultSecurityConfig {

	private SecurityProperties securityProperties;

	@Autowired
	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	@Bean
	public BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
		bearerTokenResolver.setAllowUriQueryParameter(true);
		return bearerTokenResolver;
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		// @formatter:off
		http.authorizeRequests(authorizeRequests -> authorizeRequests
			// 静态资源：允许所有人访问
			.regexMatchers("/favicon.ico").permitAll()
			// 端点：允许所有人访问
			.regexMatchers("^/actuator(/.*)?$").permitAll()
			// API 文档：允许所有人访问
			.regexMatchers("^/(swagger-ui|v3/api-docs)(/.*)?$").permitAll()
			// 其他地址：需要授权访问
			.anyRequest().authenticated()).formLogin(withDefaults());
		// @formatter:on

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
