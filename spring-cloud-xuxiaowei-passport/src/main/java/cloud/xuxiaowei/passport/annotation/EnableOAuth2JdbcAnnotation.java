package cloud.xuxiaowei.passport.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.lang.annotation.*;

/**
 * 开启 OAuth 2.1 JDBC 实现
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ EnableOAuth2JdbcAnnotation.OAuth2JdbcConfig.class })
public @interface EnableOAuth2JdbcAnnotation {

	class OAuth2JdbcConfig {

		@Bean
		public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
			return new JdbcRegisteredClientRepository(jdbcOperations);
		}

		@Bean
		public OAuth2AuthorizationService authorizationService(JdbcOperations jdbcOperations,
				RegisteredClientRepository registeredClientRepository) {
			return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
		}

		@Bean
		public OAuth2AuthorizationConsentService authorizationConsentService(JdbcOperations jdbcOperations,
				RegisteredClientRepository registeredClientRepository) {
			return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
		}

	}

}
