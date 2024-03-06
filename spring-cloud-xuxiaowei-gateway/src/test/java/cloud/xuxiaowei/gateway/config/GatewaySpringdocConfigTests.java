package cloud.xuxiaowei.gateway.config;

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

import java.util.ArrayList;
import java.util.List;
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
class GatewaySpringdocConfigTests {

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
		log.info("url: \n{}", objectWriter.writeValueAsString(body));

		assert body != null;
		String openapi = body.get("openapi").toString();
		Assert.isTrue("3.0.1".equals(openapi), String.format("%s openapi 版本号不正确", url));
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

		@SuppressWarnings("all")
		List<Map<String, String>> urls = (List<Map<String, String>>) body.get("urls");

		List<Map<String, String>> errs = new ArrayList<>();

		for (Map<String, String> map : urls) {
			String u = map.get("url");
			String name = map.get("name");
			if ("passport".equals(name)) {
				Assert.isTrue("/passport/v3/api-docs".equals(u), "passport 配置的 springdoc 不正确");
			}
			else if ("file".equals(name)) {
				Assert.isTrue("/file/v3/api-docs".equals(u), "passport 配置的 springdoc 不正确");
			}
			else {
				errs.add(map);
			}
		}

		log.info("errs:\n{}", objectWriter.writeValueAsString(errs));

		Assert.isTrue(errs.isEmpty(), "存在未验证的 springdoc 配置");
	}

}
