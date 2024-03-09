package cloud.xuxiaowei.gateway.oauth2;

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
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
	void localPassport() throws JsonProcessingException {

		// @formatter:off
		List<String> list = Arrays.asList(
				String.format("http://127.0.0.1:%d/xuxiaowei-passport/.well-known/oauth-authorization-server", serverPort),
				String.format("http://127.0.0.1:%d/passport/.well-known/oauth-authorization-server", serverPort));
		// @formatter:on

		RestTemplate restTemplate = new RestTemplate();

		for (String url : list) {

			ResponseEntity<Map> entity = restTemplate.getForEntity(String.format(url), Map.class);

			Assert.isTrue(entity.getStatusCodeValue() == 200, "HTTP 状态码不正常");

			Map body = entity.getBody();

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

			log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));
		}
	}

	@Test
	void remotePassport() throws JsonProcessingException {

		// @formatter:off
		List<String> list = Arrays.asList(
				"http://xuxiaowei-passport/.well-known/oauth-authorization-server",
				"http://xuxiaowei-gateway/passport/.well-known/oauth-authorization-server");
		// @formatter:on

		for (String url : list) {
			ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

			Assert.isTrue(entity.getStatusCodeValue() == 200, "HTTP 状态码不正常");

			Map body = entity.getBody();

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

			log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));
		}
	}

}
