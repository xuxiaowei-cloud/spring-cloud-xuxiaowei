package cloud.xuxiaowei.passport.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * OAuth 2.1 客户服务 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class RegisteredClientRepositoryTests {

	@Autowired
	private RegisteredClientRepository registeredClientRepository;

	@Test
	void save() {

		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId("messaging-client-" + RandomStringUtils.randomNumeric(3))
			.clientSecret("{noop}secret-" + RandomStringUtils.randomNumeric(3))
			.clientSecretExpiresAt(Instant.now().plus(3650, ChronoUnit.DAYS))
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
			.redirectUri("http://127.0.0.1:8080/authorized")
			.redirectUri("https://home.baidu.com/home/index/contact_us")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.scope("message.read")
			.scope("message.write")
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.build();

		log.info("id: {}", registeredClient.getId());
		log.info("clientId: {}", registeredClient.getClientId());
		log.info("clientIdIssuedAt: {}", registeredClient.getClientIdIssuedAt());
		log.info("clientSecret: {}", registeredClient.getClientSecret());
		log.info("clientSecretExpiresAt: {}", registeredClient.getClientSecretExpiresAt());
		log.info("clientName: {}", registeredClient.getClientName());
		log.info("clientAuthenticationMethods: {}", registeredClient.getClientAuthenticationMethods());
		log.info("authorizationGrantTypes: {}", registeredClient.getAuthorizationGrantTypes());
		log.info("redirectUris: {}", registeredClient.getRedirectUris());
		log.info("scopes: {}", registeredClient.getScopes());
		log.info("clientSettings: {}", registeredClient.getClientSettings());
		log.info("tokenSettings: {}", registeredClient.getTokenSettings());

		registeredClientRepository.save(registeredClient);

		RegisteredClient byId = registeredClientRepository.findById(registeredClient.getId());
		assert byId != null;

		log.info("byId id: {}", byId.getId());
		log.info("byId clientId: {}", byId.getClientId());
		log.info("byId clientIdIssuedAt: {}", byId.getClientIdIssuedAt());
		log.info("byId clientSecret: {}", byId.getClientSecret());
		log.info("byId clientSecretExpiresAt: {}", byId.getClientSecretExpiresAt());
		log.info("byId clientName: {}", byId.getClientName());
		log.info("byId clientAuthenticationMethods: {}", byId.getClientAuthenticationMethods());
		log.info("byId authorizationGrantTypes: {}", byId.getAuthorizationGrantTypes());
		log.info("byId redirectUris: {}", byId.getRedirectUris());
		log.info("byId scopes: {}", byId.getScopes());
		log.info("byId clientSettings: {}", byId.getClientSettings());
		log.info("byId tokenSettings: {}", byId.getTokenSettings());

		RegisteredClient byClientId = registeredClientRepository.findByClientId(registeredClient.getClientId());
		assert byClientId != null;

		log.info("byClientId id: {}", byClientId.getId());
		log.info("byClientId clientId: {}", byClientId.getClientId());
		log.info("byClientId clientIdIssuedAt: {}", byClientId.getClientIdIssuedAt());
		log.info("byClientId clientSecret: {}", byClientId.getClientSecret());
		log.info("byClientId clientSecretExpiresAt: {}", byClientId.getClientSecretExpiresAt());
		log.info("byClientId clientName: {}", byClientId.getClientName());
		log.info("byClientId clientAuthenticationMethods: {}", byClientId.getClientAuthenticationMethods());
		log.info("byClientId authorizationGrantTypes: {}", byClientId.getAuthorizationGrantTypes());
		log.info("byClientId redirectUris: {}", byClientId.getRedirectUris());
		log.info("byClientId scopes: {}", byClientId.getScopes());
		log.info("byClientId clientSettings: {}", byClientId.getClientSettings());
		log.info("byClientId tokenSettings: {}", byClientId.getTokenSettings());
	}

}
