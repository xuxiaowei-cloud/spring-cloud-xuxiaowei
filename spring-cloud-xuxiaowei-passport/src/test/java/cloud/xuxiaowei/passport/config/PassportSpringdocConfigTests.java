package cloud.xuxiaowei.passport.config;

import cloud.xuxiaowei.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SpringDoc 配置 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PassportSpringdocConfigTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void apiDocs() throws JsonProcessingException {
		String url = String.format("http://127.0.0.1:%d/v3/api-docs", serverPort);

		ResponseEntity<Map> entity = new RestTemplate().getForEntity(url, Map.class);

		assertEquals(entity.getStatusCodeValue(), 200);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		Map body = entity.getBody();
		String value = objectWriter.writeValueAsString(body);
		log.info("url: \n{}", value);

		assertNotNull(body);

		Object openapi = body.get("openapi");

		assertEquals(openapi, "3.0.1");

		String tokenUrl = JsonUtils.getValue(value,
				"components.securitySchemes.oauth2_clientCredentials.flows.clientCredentials.tokenUrl");

		assertNotNull(tokenUrl);
	}

	@Test
	void apiDocsSwaggerConfig() throws JsonProcessingException {
		String url = String.format("http://127.0.0.1:%d/v3/api-docs/swagger-config", serverPort);

		ResponseEntity<Map> entity = new RestTemplate().getForEntity(url, Map.class);

		assertEquals(entity.getStatusCodeValue(), 200);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		Map body = entity.getBody();

		assertNotNull(body);

		log.info("url: \n{}", objectWriter.writeValueAsString(body));

		Object configUrl = body.get("configUrl");

		assertEquals(configUrl, "/v3/api-docs/swagger-config");
	}

}
