package cloud.xuxiaowei.passport.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerMetadataClaimNames;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizationServerSettingsTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void local() throws JsonProcessingException {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> entity = restTemplate.getForEntity(
				String.format("http://127.0.0.1:%d/.well-known/oauth-authorization-server", serverPort), Map.class);

		assertEquals(entity.getStatusCodeValue(), 200);

		Map body = entity.getBody();

		assertNotNull(body);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));

		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.ISSUER));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT));
		assertNotNull(
				body.get(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED));
	}

	@Test
	void remote() throws JsonProcessingException {

		String url = "http://xuxiaowei-passport/.well-known/oauth-authorization-server";

		ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

		assertEquals(entity.getStatusCodeValue(), 200);

		Map body = entity.getBody();

		assertNotNull(body);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));

		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.ISSUER));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT));
		assertNotNull(
				body.get(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED));
		assertNotNull(body.get(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED));
	}

}
