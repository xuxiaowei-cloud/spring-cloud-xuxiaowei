package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.vo.UserInfoVO;
import cloud.xuxiaowei.utils.Response;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cloud.xuxiaowei.passport.SpringCloudXuxiaoweiPassportApplicationTests.clientCredentialsAccessToken;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void info() throws JsonProcessingException {

		String url = String.format("http://127.0.0.1:%d/user/info", serverPort);

		String accessToken = clientCredentialsAccessToken(restTemplate);

		List<String> list = Arrays.asList("徐晓伟", "");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setBearerAuth(accessToken);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		for (String username : list) {

			Map<String, String> requestBody = new HashMap<>();
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
			requestBody.put("username", username);

			if (StringUtils.hasText(username)) {
				requestBody.put("sex", "男");
			}

			@SuppressWarnings("unchecked")
			Response<UserInfoVO> response = new RestTemplate().postForObject(url, httpEntity, Response.class);

			assertNotNull(response);

			log.info("Response<UserInfoVO>:\n{}", objectWriter.writeValueAsString(response));

			boolean success = response.isSuccess();

			if ("".equals(username)) {
				assertFalse(success);
				break;
			}

			assertTrue(success);
		}

	}

}
