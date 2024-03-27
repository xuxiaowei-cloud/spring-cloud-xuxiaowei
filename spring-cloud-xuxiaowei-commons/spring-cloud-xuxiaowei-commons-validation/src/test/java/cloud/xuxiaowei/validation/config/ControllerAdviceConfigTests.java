package cloud.xuxiaowei.validation.config;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.validation.SpringCloudXuxiaoweiCommonsValidationApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = SpringCloudXuxiaoweiCommonsValidationApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerAdviceConfigTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void cloudRuntimeException() {
		String url = String.format("http://127.0.0.1:%s/cloud-runtime-exception", serverPort);

		RestTemplate restTemplate = new RestTemplate();
		Response<?> response = restTemplate.getForObject(url, Response.class);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertEquals("测试 CloudRuntimeException 异常", response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void cloudException() {
		String url = String.format("http://127.0.0.1:%s/cloud-exception", serverPort);

		RestTemplate restTemplate = new RestTemplate();
		Response<?> response = restTemplate.getForObject(url, Response.class);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertEquals("测试 CloudException 异常", response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void methodArgumentNotValidException() {
		String url = String.format("http://127.0.0.1:%s/method-argument-not-valid-exception", serverPort);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> requestBody = new HashMap<>();
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
		Response<?> response = restTemplate.postForObject(url, httpEntity, Response.class);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertNotNull(response.getMessage());
		assertTrue(response.getMessage().contains("用户名不能为空"));
		assertTrue(response.getMessage().contains("性别不能为空"));
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());

		requestBody.put("username", "徐晓伟");
		response = restTemplate.postForObject(url, httpEntity, Response.class);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertNotNull(response.getMessage());
		assertEquals("性别不能为空", response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());
	}

	@Test
	void missingServletRequestPartException() throws Exception {

		String name = "file2";
		String originalFilename = "test.txt";
		String fileContext = "Hello, World!";
		String url = "/missing-servlet-request-part-exception";

		MockMultipartFile file = new MockMultipartFile(name, originalFilename, MediaType.TEXT_PLAIN_VALUE,
				fileContext.getBytes());

		MvcResult mvcResult = mockMvc
			.perform(MockMvcRequestBuilders.multipart(url).file(file).contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		response.setCharacterEncoding("UTF-8");

		String content = response.getContentAsString();
		log.info(content);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		@SuppressWarnings("unchecked")
		Response<String> result = converter.getObjectMapper().readValue(content, Response.class);
		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals("500", result.getCode());
		assertEquals("缺少参数: file", result.getMessage());
		assertNull(result.getUrl());
		assertNotNull(result.getRequestId());
		assertNull(result.getData());
	}

}
