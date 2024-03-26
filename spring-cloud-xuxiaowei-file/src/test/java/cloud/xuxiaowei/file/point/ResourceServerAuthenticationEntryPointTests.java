package cloud.xuxiaowei.file.point;

import cloud.xuxiaowei.oauth2.point.ResourceServerAuthenticationEntryPoint;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 资源服务 身份验证入口点 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see ResourceServerAuthenticationEntryPoint
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceServerAuthenticationEntryPointTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void accessTokenUUID() {
		String accessToken = UUID.randomUUID().toString();

		String url = String.format("http://127.0.0.1:%s/openfeign", serverPort);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(accessToken);
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
		Response<?> response = new RestTemplate().postForObject(url, httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("invalid_token", response.getCode());
		assertEquals(
				"An error occurred while attempting to decode the Jwt: Invalid JWT serialization: Missing dot delimiter(s)",
				response.getMessage());
		assertEquals("https://tools.ietf.org/html/rfc6750#section-3.1", response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void accessNull() {
		String url = String.format("http://127.0.0.1:%s/openfeign", serverPort);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
		Response<?> response = new RestTemplate().postForObject(url, httpEntity, Response.class);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertEquals("Full authentication is required to access this resource", response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

}
