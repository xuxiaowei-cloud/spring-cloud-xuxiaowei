/*
 * Copyright 2020-2022 the original author or authors.
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
import cloud.xuxiaowei.passport.handler.ClientAuthenticationResponseHandler;
import cloud.xuxiaowei.passport.handler.TokenEndpointErrorResponseHandler;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @author Joe Grandja
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	private SecurityProperties securityProperties;

	@Autowired
	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
			.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

		// Enable OpenID Connect 1.0
		authorizationServerConfigurer.oidc(Customizer.withDefaults());

		authorizationServerConfigurer.tokenEndpoint(tokenEndpointCustomizer -> {
			tokenEndpointCustomizer.errorResponseHandler(new TokenEndpointErrorResponseHandler());
		});

		authorizationServerConfigurer.clientAuthentication(clientAuthenticationCustomizer -> {
			clientAuthenticationCustomizer.errorResponseHandler(new ClientAuthenticationResponseHandler());
		});

		http.exceptionHandling(
				exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

		return http.build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = new RSAKey.Builder(securityProperties.rsaPublicKey())
			.privateKey(securityProperties.privateKey())
			.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

}
