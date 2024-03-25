package cloud.xuxiaowei.passport.oauth;

import cloud.xuxiaowei.oauth2.constant.OAuth2Constants;
import cloud.xuxiaowei.passport.SpringCloudXuxiaoweiPassportApplicationTests;
import cloud.xuxiaowei.utils.Base64Utils;
import cloud.xuxiaowei.utils.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientCredentialsTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void start() throws JsonProcessingException {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
		requestBody.put(OAuth2ParameterNames.SCOPE,
				Collections.singletonList("openid profile message.read message.write"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Map map = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", serverPort), httpEntity,
				Map.class);

		assertNotNull(map);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("token:\n{}", objectWriter.writeValueAsString(map));

		assertNotNull(map.get(OAuth2ParameterNames.ACCESS_TOKEN));
		assertNotNull(map.get(OAuth2ParameterNames.SCOPE));
		assertNotNull(map.get(OAuth2ParameterNames.TOKEN_TYPE));
		assertNotNull(map.get(OAuth2ParameterNames.EXPIRES_IN));

		String accessToken = map.get(OAuth2ParameterNames.ACCESS_TOKEN).toString();

		String[] split = accessToken.split("\\.");
		assertEquals(split.length, 3);

		String payloadEncode = split[1];

		String payloadDecode = Base64Utils.decodeStr(payloadEncode);

		Map payload = objectMapper.readValue(payloadDecode, Map.class);

		log.info("payload:\n{}", objectWriter.writeValueAsString(payload));

		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.SUB));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.AUD));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.NBF));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.SCOPE));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.ISS));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.EXP));
		assertNotNull(payload.get(OAuth2TokenIntrospectionClaimNames.IAT));
		assertNotNull(payload.get(OAuth2Constants.AUTHORITIES));
		assertEquals(payload.get(OAuth2Constants.AUTHORITIES), payload.get(OAuth2TokenIntrospectionClaimNames.SCOPE));

		// 凭证式模式：
		// sub：代表用户名，由于凭证式是自己给自己授权，所以 sub 和 aud 相同，都是 客户ID
		// aud：代表客户ID
		assertEquals(payload.get(OAuth2TokenIntrospectionClaimNames.SUB),
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID);
		assertEquals(payload.get(OAuth2TokenIntrospectionClaimNames.AUD),
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID);

	}

	@Test
	void grantTypeMissingError() {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.SCOPE,
				Collections.singletonList("openid profile message.read message.write"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", serverPort),
				httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_request", response.getCode());
		assertEquals("OAuth 2.0 Parameter: grant_type", response.getMessage());
		assertEquals("https://datatracker.ietf.org/doc/html/rfc6749#section-5.2", response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void scopeInvalidError() {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
		requestBody.put(OAuth2ParameterNames.SCOPE, Collections.singletonList("a"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", serverPort),
				httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_scope", response.getCode());
		assertNull(response.getMessage());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

}
