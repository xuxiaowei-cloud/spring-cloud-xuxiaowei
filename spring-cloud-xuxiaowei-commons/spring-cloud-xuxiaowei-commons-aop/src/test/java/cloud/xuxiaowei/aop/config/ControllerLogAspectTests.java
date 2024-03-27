package cloud.xuxiaowei.aop.config;

import cloud.xuxiaowei.aop.SpringCloudXuxiaoweiCommonsAopApplication;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest(classes = SpringCloudXuxiaoweiCommonsAopApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerLogAspectTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void aroundOk() {
		String url = String.format("http://127.0.0.1:%s/ok", serverPort);

		RestTemplate restTemplate = new RestTemplate();
		Response<?> response = restTemplate.getForObject(url, Response.class);
		assertNotNull(response);
		assertTrue(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void aroundCloudRuntimeException() {
		String url = String.format("http://127.0.0.1:%s/cloud-runtime-exception", serverPort);

		RestTemplate restTemplate = new RestTemplate();

		Response<?> response = null;
		try {
			response = restTemplate.getForObject(url, Response.class);
		}
		catch (HttpServerErrorException.InternalServerError e) {
			log.error("模拟服务器异常", e);
			int rawStatusCode = e.getRawStatusCode();
			assertEquals(500, rawStatusCode);
		}

		assertNull(response);
	}

}
