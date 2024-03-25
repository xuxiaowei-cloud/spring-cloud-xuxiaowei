package cloud.xuxiaowei.passport.handler;

import cloud.xuxiaowei.passport.SpringCloudXuxiaoweiPassportApplicationTests;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OAuth 2.1 认证 异常处理
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientAuthenticationResponseHandlerTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void clientError() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET + "test");
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
		requestBody.put(OAuth2ParameterNames.SCOPE,
				Collections.singletonList("openid profile message.read message.write"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", serverPort),
				httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_client", response.getCode());
		assertEquals("Client authentication failed: client_secret", response.getMessage());
		assertEquals("https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1", response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void codeError() {
		String code = UUID.randomUUID().toString();

		String tokenUrl = String.format("http://127.0.0.1:%d/oauth2/token", serverPort);
		String redirectUri = "https://home.baidu.com/home/index/contact_us";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.CODE, Collections.singletonList(code));
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		requestBody.put(OAuth2ParameterNames.REDIRECT_URI, Collections.singletonList(redirectUri));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(tokenUrl, httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_grant", response.getCode());
		assertEquals("Client authentication failed: code", response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

}
