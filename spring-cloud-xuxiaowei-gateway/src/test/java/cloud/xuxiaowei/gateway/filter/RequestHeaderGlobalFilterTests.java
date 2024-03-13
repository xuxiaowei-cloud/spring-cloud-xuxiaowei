package cloud.xuxiaowei.gateway.filter;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.constant.LogConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static cloud.xuxiaowei.gateway.SpringCloudXuxiaoweiGatewayApplicationTests.clientCredentialsAccessToken;

/**
 * 测试网关 请求头 过滤器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RequestHeaderGlobalFilterTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void filter() throws JsonProcessingException {

		String accessToken = clientCredentialsAccessToken(restTemplate);

		String url = String.format("http://127.0.0.1:%s/file/openfeign", serverPort);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(accessToken);
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Response> entity = new RestTemplate().postForEntity(url, httpEntity, Response.class);

		Assert.isTrue(entity.getStatusCodeValue() == 200, String.format("%s HTTP 状态码不正常", url));

		Response<?> body = entity.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("\n{}", objectWriter.writeValueAsString(body));

		HttpHeaders headers = entity.getHeaders();

		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			log.info("{}: {}", entry.getKey(), entry.getValue());
		}

		assert body != null;
		String requestId = body.getRequestId();
		assert requestId != null;

		String header = headers.getFirst(LogConstants.C_REQUEST_ID);
		assert header != null;

		Assert.isTrue(header.equals(requestId), "请求 ID 传递异常");

	}

}
