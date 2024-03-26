package cloud.xuxiaowei.passport.oauth;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.oauth2.constant.OAuth2Constants;
import cloud.xuxiaowei.passport.SpringCloudXuxiaoweiPassportApplicationTests;
import cloud.xuxiaowei.utils.Base64Utils;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
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

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SecurityProperties securityProperties;

	@Test
	void start() throws IOException {

		String username = "user1";
		String password = "password";

		String redirectUri = "https://home.baidu.com/home/index/contact_us";
		String scope = "openid profile message.read message.write";

		String tokenCheckPrefix = securityProperties.getTokenCheckPrefix();

		for (int i = 0; i < 3; i++) {

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

			String tokenUrl = String.format("http://127.0.0.1:%d/oauth2/token", serverPort);

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

			Map<?, ?> token = getToken(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
					SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET, code, redirectUri, tokenUrl);
			log.info("token:\n{}", objectWriter.writeValueAsString(token));

			assertNotNull(token.get(OAuth2ParameterNames.ACCESS_TOKEN));
			assertNotNull(token.get(OAuth2ParameterNames.REFRESH_TOKEN));
			assertNotNull(token.get(OAuth2ParameterNames.SCOPE));
			assertNotNull(token.get(OidcParameterNames.ID_TOKEN));
			assertNotNull(token.get(OAuth2ParameterNames.TOKEN_TYPE));
			assertNotNull(token.get(OAuth2ParameterNames.EXPIRES_IN));

			String accessToken = token.get(OAuth2ParameterNames.ACCESS_TOKEN).toString();

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
			assertNotEquals(payload.get(OAuth2Constants.AUTHORITIES),
					payload.get(OAuth2TokenIntrospectionClaimNames.SCOPE));

			// 授权码模式：
			// sub：代表用户名
			// aud：代表客户ID
			assertEquals(payload.get(OAuth2TokenIntrospectionClaimNames.SUB), username);
			assertEquals(payload.get(OAuth2TokenIntrospectionClaimNames.AUD),
					SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Response> entity;
			if (i == 1) {

				String key = tokenCheckPrefix + accessToken;

				stringRedisTemplate.delete(key);

				entity = restTemplate.getForEntity(
						String.format("http://127.0.0.1:%d?access_token=%s", serverPort, accessToken), Response.class);

				Response<?> response = entity.getBody();

				assertNotNull(response);
				assertFalse(response.isSuccess());
				assertEquals("invalid_token", response.getCode());
				assertEquals("Invalid token", response.getMessage());
				assertEquals("https://tools.ietf.org/html/rfc6750#section-3.1", response.getUrl());
				assertNotNull(response.getRequestId());
				assertNull(response.getData());
				continue;
			}
			else {
				entity = restTemplate.getForEntity(
						String.format("http://127.0.0.1:%d?access_token=%s", serverPort, accessToken), Response.class);
			}

			assertEquals(entity.getStatusCodeValue(), 200);

			@SuppressWarnings("unchecked")
			Response<String> response = entity.getBody();

			assertNotNull(response);

			log.info("\n{}", objectWriter.writeValueAsString(response));

			assertNotNull(response);
			assertTrue(response.isSuccess());
			assertNull(response.getCode());
			assertNull(response.getMessage());
			assertNull(response.getUrl());
			assertNotNull(response.getRequestId());
			assertEquals("徐晓伟微服务", response.getData());

			String refreshToken = token.get(OAuth2ParameterNames.REFRESH_TOKEN).toString();

			Map<?, ?> refresh = refreshToken(SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_ID,
					SpringCloudXuxiaoweiPassportApplicationTests.CLIENT_SECRET, refreshToken, tokenUrl);

			assertNotNull(refresh);

			log.info("refresh:\n{}", objectWriter.writeValueAsString(refresh));

			assertNotNull(refresh.get(OAuth2ParameterNames.ACCESS_TOKEN));
			assertNotNull(refresh.get(OAuth2ParameterNames.REFRESH_TOKEN));
			assertNotNull(refresh.get(OAuth2ParameterNames.SCOPE));
			assertNotNull(refresh.get(OidcParameterNames.ID_TOKEN));
			assertNotNull(refresh.get(OAuth2ParameterNames.TOKEN_TYPE));
			assertNotNull(refresh.get(OAuth2ParameterNames.EXPIRES_IN));
		}
	}

	private Map<?, ?> getToken(String clientId, String clientSecret, String code, String redirectUri, String tokenUrl) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.CODE, Collections.singletonList(code));
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE,
				Collections.singletonList(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));
		requestBody.put(OAuth2ParameterNames.REDIRECT_URI, Collections.singletonList(redirectUri));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		return restTemplate.postForObject(tokenUrl, httpEntity, Map.class);
	}

	private Map<?, ?> refreshToken(String clientId, String clientSecret, String refreshToken, String tokenUrl) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put(OAuth2ParameterNames.REFRESH_TOKEN, Collections.singletonList(refreshToken));
		requestBody.put(OAuth2ParameterNames.GRANT_TYPE, Collections.singletonList(OAuth2ParameterNames.REFRESH_TOKEN));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		return restTemplate.postForObject(tokenUrl, httpEntity, Map.class);
	}

}
