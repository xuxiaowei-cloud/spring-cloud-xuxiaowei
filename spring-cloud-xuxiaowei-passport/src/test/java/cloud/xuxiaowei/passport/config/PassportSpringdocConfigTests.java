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
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

		Assert.isTrue(entity.getStatusCodeValue() == 200, String.format("%s HTTP 状态码不正常", url));

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		Map body = entity.getBody();
		String value = objectWriter.writeValueAsString(body);
		log.info("url: \n{}", value);

		assert body != null;
		String openapi = body.get("openapi").toString();
		Assert.isTrue("3.0.1".equals(openapi), String.format("%s openapi 版本号不正确", url));

		String tokenUrl = JsonUtils.getValue(value,
				"components.securitySchemes.oauth2_clientCredentials.flows.clientCredentials.tokenUrl");
		Assert.isTrue(tokenUrl != null, String.format("%s swagger securitySchemes 配置不正确", url));
	}

	@Test
	void apiDocsSwaggerConfig() throws JsonProcessingException {
		String url = String.format("http://127.0.0.1:%d/v3/api-docs/swagger-config", serverPort);

		ResponseEntity<Map> entity = new RestTemplate().getForEntity(url, Map.class);

		Assert.isTrue(entity.getStatusCodeValue() == 200, String.format("%s HTTP 状态码不正常", url));

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		Map body = entity.getBody();
		log.info("url: \n{}", objectWriter.writeValueAsString(body));

		assert body != null;
		String configUrl = body.get("configUrl").toString();
		Assert.isTrue("/v3/api-docs/swagger-config".equals(configUrl), String.format("%s swagger 配置地址不正确", url));
	}

}
