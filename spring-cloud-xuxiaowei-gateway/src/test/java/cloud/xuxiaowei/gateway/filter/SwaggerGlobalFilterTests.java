package cloud.xuxiaowei.gateway.filter;

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

import java.util.List;
import java.util.Map;

/**
 * 测试网关 Swagger 过滤器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerGlobalFilterTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void filter() throws JsonProcessingException {
		String service = "passport";
		String url = String.format("http://127.0.0.1:%s/%s/v3/api-docs", serverPort, service);
		String expectResult = String.format("http://127.0.0.1:%s/%s", serverPort, service);

		ResponseEntity<Map> entity = new RestTemplate().getForEntity(url, Map.class);

		Assert.isTrue(entity.getStatusCodeValue() == 200, String.format("%s HTTP 状态码不正常", url));

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		Map body = entity.getBody();
		log.info("url: \n{}", objectWriter.writeValueAsString(body));

		assert body != null;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> servers = (List<Map<String, Object>>) body.get("servers");

		for (Map<String, Object> server : servers) {
			Object urlObj = server.get("url");
			Assert.isTrue(expectResult.equals(urlObj), "网关修改 Swagger 接口响应结果失败");
		}
	}

}
