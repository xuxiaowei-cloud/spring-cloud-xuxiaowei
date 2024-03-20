package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.utils.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static cloud.xuxiaowei.file.SpringCloudXuxiaoweiFileApplicationTests.clientCredentialsAccessToken;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenfeignRestControllerTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void index() throws JsonProcessingException {
		String scope = "openid profile message.read message.write";
		String accessToken = clientCredentialsAccessToken(restTemplate, scope);

		String url = String.format("http://127.0.0.1:%s/openfeign", serverPort);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(accessToken);
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Response> entity = new RestTemplate().postForEntity(url, httpEntity, Response.class);

		assertEquals(entity.getStatusCodeValue(), 200);
		assertNotNull(entity.getBody());
		assertTrue(entity.getBody().isSuccess());
	}

	@Test
	void indexStatusCode403() throws JsonProcessingException {
		String scope = "openid profile message.write";
		String accessToken = clientCredentialsAccessToken(restTemplate, scope);

		String url = String.format("http://127.0.0.1:%s/openfeign?access_token=%s", serverPort, accessToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(accessToken);
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

		ResponseEntity<Response> entity = null;
		try {
			entity = new RestTemplate().postForEntity(url, httpEntity, Response.class);
		}
		catch (HttpClientErrorException.Forbidden e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
		}

		assertNull(entity);
	}

}
