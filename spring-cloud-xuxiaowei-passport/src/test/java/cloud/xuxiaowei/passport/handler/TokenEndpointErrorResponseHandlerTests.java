package cloud.xuxiaowei.passport.handler;

import cloud.xuxiaowei.passport.SpringCloudXuxiaoweiPassportApplicationTests;
import cloud.xuxiaowei.utils.Response;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OAuth 2.1 Token 异常处理 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenEndpointErrorResponseHandlerTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private WebClient webClient;

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
		String scope = UUID.randomUUID().toString();

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
		requestBody.put(OAuth2ParameterNames.SCOPE, Collections.singletonList(scope));
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

	@Test
	void codeMissingError() {

		String tokenUrl = String.format("http://127.0.0.1:%d/oauth2/token", serverPort);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(tokenUrl, httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_request", response.getCode());
		assertEquals("OAuth 2.0 Parameter: code", response.getMessage());
		assertEquals("https://datatracker.ietf.org/doc/html/rfc6749#section-5.2", response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void redirectUriError() throws IOException {

		String username = "user1";
		String password = "password";

		String redirectUri = "https://home.baidu.com/home/index/contact_us";
		String scope = "openid profile message.read message.write";

		String tokenUrl = String.format("http://127.0.0.1:%d/oauth2/token", serverPort);

		String state = UUID.randomUUID().toString();

		HtmlPage loginPage = webClient.getPage("/login");

		HtmlInput usernameInput = loginPage.querySelector("input[name=\"username\"]");
		HtmlInput passwordInput = loginPage.querySelector("input[name=\"password\"]");
		usernameInput.type(username);
		passwordInput.type(password);

		HtmlButton signInButton = loginPage.querySelector("button");
		Page signInPage = signInButton.click();
		log.info("signIn Page URL: {}", signInPage.getUrl());

		HtmlPage authorize = webClient.getPage(
				String.format("/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s",
						SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID, redirectUri, scope, state));

		String authorizeUrl = authorize.getUrl().toString();
		log.info("authorize URL: {}", authorize.getUrl());

		String url;
		if (authorizeUrl.startsWith(redirectUri)) {
			url = authorizeUrl;
		}
		else {
			HtmlCheckBoxInput profile = authorize.querySelector("input[id=\"profile\"]");
			HtmlCheckBoxInput messageRead = authorize.querySelector("input[id=\"message.read\"]");
			HtmlCheckBoxInput messageWrite = authorize.querySelector("input[id=\"message.write\"]");
			HtmlButton submitButton = authorize.querySelector("button");

			profile.setChecked(true);
			messageRead.setChecked(true);
			messageWrite.setChecked(true);

			Page authorized = submitButton.click();
			url = authorized.getUrl().toString();
			log.info("authorized URL: {}", url);
		}

		UriTemplate uriTemplate = new UriTemplate(String.format("%s?code={code}&state={state}", redirectUri));
		Map<String, String> match = uriTemplate.match(url);
		String code = match.get("code");

		String redirectUriError = redirectUri + UUID.randomUUID();

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
				SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.CODE, Collections.singletonList(code));
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		requestBody.put(OAuth2ParameterNames.REDIRECT_URI, Collections.singletonList(redirectUriError));

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Response<?> response = restTemplate.postForObject(tokenUrl, httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_grant", response.getCode());
		assertNull(response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

}
