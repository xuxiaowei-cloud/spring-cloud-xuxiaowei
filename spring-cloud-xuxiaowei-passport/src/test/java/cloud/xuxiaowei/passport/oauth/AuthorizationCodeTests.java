package cloud.xuxiaowei.passport.oauth;

import cloud.xuxiaowei.utils.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * OAuth 2.1 授权码模式 自动化 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizationCodeTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private WebClient webClient;

	// @formatter:off
	/**
	 *
	 * 前提
	 * <p>
	 * 1. Redis 数据为空
	 * <p>
	 * 2. oauth2_authorization、oauth2_authorization_consent 表为空
	 * <p>
	 * 测试 10 次 授权码模式
	 * <p>
	 * 1. 使用 JDBC 实现：查询 oauth2_authorization 表 根据主键 31（更新数据前查询一次，故：存在重复查询）次，根据授权码 20 次，oauth2_registered_client 表 92 次，oauth2_authorization_consent 表 12 次
	 * <p>
	 * 2. 使用 Redis 实现：查询 oauth2_authorization 表 根据主键 31（更新数据前查询一次，故：存在重复查询）次，根据授权码 0 次，oauth2_registered_client 表 1 次，oauth2_authorization_consent 表 3 次
	 */
	// @formatter:on
	@Test
	void start() throws IOException {

		String username = "user1";
		String password = "password";

		String redirectUri = "https://home.baidu.com/home/index/contact_us";
		String scope = "openid profile message.read message.write";

		String clientId = "messaging-client";
		String clientSecret = "secret";

		for (int i = 0; i < 10; i++) {
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
							clientId, redirectUri, scope, state));

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

			String tokenUrl = String.format("http://127.0.0.1:%d/oauth2/token", serverPort);

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

			Map<?, ?> token = getToken(clientId, clientSecret, code, redirectUri, tokenUrl);
			log.info("token:\n{}", objectWriter.writeValueAsString(token));

			assertNotNull(token.get("access_token"));
			assertNotNull(token.get("refresh_token"));
			assertNotNull(token.get("scope"));
			assertNotNull(token.get("id_token"));
			assertNotNull(token.get("token_type"));
			assertNotNull(token.get("expires_in"));

			String accessToken = token.get("access_token").toString();

			RestTemplate restTemplate = new RestTemplate();
			@SuppressWarnings("all")
			ResponseEntity<Response> entity = restTemplate.getForEntity(
					String.format("http://127.0.0.1:%d?access_token=%s", serverPort, accessToken), Response.class);

			assertEquals(entity.getStatusCodeValue(), 200);

			@SuppressWarnings("unchecked")
			Response<String> response = entity.getBody();

			assertNotNull(response);

			log.info("\n{}", objectWriter.writeValueAsString(response));

			String title = response.getData();

			assertEquals("徐晓伟微服务", title);

			String refreshToken = token.get("refresh_token").toString();

			Map<?, ?> refresh = refreshToken(clientId, clientSecret, refreshToken, tokenUrl);

			assertNotNull(refresh);

			log.info("refresh:\n{}", objectWriter.writeValueAsString(refresh));

			assertNotNull(refresh.get("access_token"));
			assertNotNull(refresh.get("refresh_token"));
			assertNotNull(refresh.get("scope"));
			assertNotNull(refresh.get("id_token"));
			assertNotNull(refresh.get("token_type"));
			assertNotNull(refresh.get("expires_in"));
		}
	}

	private Map<?, ?> getToken(String clientId, String clientSecret, String code, String redirectUri, String tokenUrl) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put("code", Collections.singletonList(code));
		requestBody.put("grant_type", Collections.singletonList("authorization_code"));
		requestBody.put("redirect_uri", Collections.singletonList(redirectUri));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		return restTemplate.postForObject(tokenUrl, httpEntity, Map.class);
	}

	private Map<?, ?> refreshToken(String clientId, String clientSecret, String refreshToken, String tokenUrl) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put("refresh_token", Collections.singletonList(refreshToken));
		requestBody.put("grant_type", Collections.singletonList("refresh_token"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		return restTemplate.postForObject(tokenUrl, httpEntity, Map.class);
	}

}
