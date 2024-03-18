package cloud.xuxiaowei.passport.oauth;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * OAuth 2.1 凭证式 自动化 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientCredentialsTests {

	@LocalServerPort
	private int serverPort;

	// @formatter:off
	/**
	 * 前提
	 * <p>
	 * 1. Redis 数据为空
	 * <p>
	 * 2. oauth2_authorization、oauth2_authorization_consent（此处使用 凭证式，与此表无关） 表为空
	 * <p>
	 * 测试 10 次 凭证式
	 * <p>
	 * 1. 使用 JDBC 实现：查询 oauth2_authorization 表 10 次（无重复查询）、oauth2_registered_client 表 10 次
	 * <p>
	 * 2. 使用 Redis 实现：查询 oauth2_authorization 表 10 次（无重复查询）、oauth2_registered_client 表 1 次
	 */
	// @formatter:on
	@Test
	void start() throws JsonProcessingException {

		String clientId = "messaging-client";
		String clientSecret = "secret";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put("grant_type", Collections.singletonList("client_credentials"));
		requestBody.put("scope", Collections.singletonList("openid profile message.read message.write"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		Map map = restTemplate.postForObject(String.format("http://127.0.0.1:%d/oauth2/token", serverPort), httpEntity,
				Map.class);

		assertNotNull(map);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("token:\n{}", objectWriter.writeValueAsString(map));

		assertNotNull(map.get("access_token"));
		assertNotNull(map.get("scope"));
		assertNotNull(map.get("token_type"));
		assertNotNull(map.get("expires_in"));
	}

}
