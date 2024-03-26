package cloud.xuxiaowei.file.config;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.core.utils.SecurityUtils;
import cloud.xuxiaowei.oauth2.point.ResourceServerAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class ResourceServerConfig {

	private SecurityProperties securityProperties;

	@Autowired
	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
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

		http.oauth2ResourceServer(oauth2ResourceServerCustomizer -> {
			oauth2ResourceServerCustomizer.authenticationEntryPoint(new ResourceServerAuthenticationEntryPoint());
		});

		return http.build();
	}

}
