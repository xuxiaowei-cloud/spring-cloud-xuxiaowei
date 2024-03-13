package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

import static cloud.xuxiaowei.file.SpringCloudXuxiaoweiFileApplicationTests.clientCredentialsAccessToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadLocalRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@LocalServerPort
	private int serverPort;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private SecurityProperties securityProperties;

	@Test
	void uploadLocalMockMvc() throws Exception {

		String accessToken = clientCredentialsAccessToken(restTemplate);

		String originalFilename = "test.txt";
		String fileContext = "Hello, World!";
		String url = "/upload/local";
		List<String> list = Arrays.asList("file", "file2");

		RSAPublicKey rsaPublicKey = securityProperties.rsaPublicKey();
		NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
			.withPublicKey(rsaPublicKey);
		NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
		Jwt jwt = nimbusJwtDecoder.decode(accessToken);

		for (String name : list) {

			MockMultipartFile file = new MockMultipartFile(name, originalFilename, MediaType.TEXT_PLAIN_VALUE,
					fileContext.getBytes());

			MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.multipart(url)
					.file(file)
					.with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
					.contentType(MediaType.MULTIPART_FORM_DATA))
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
			boolean success = result.isSuccess();

			if ("file2".equals(name)) {
				assertFalse(success);
				break;
			}

			assertTrue(success);

			String fileUrl = result.getData();

			fileUrl = UriComponentsBuilder.newInstance()
				.uri(new URI(fileUrl))
				.port(serverPort)
				.queryParam("access_token", accessToken)
				.toUriString();

			ResponseEntity<String> entity = new RestTemplate().getForEntity(fileUrl, String.class);

			assertEquals(entity.getStatusCodeValue(), 200);

			String body = entity.getBody();
			log.info("网络文件：{} 内容：{}", fileUrl, body);
			log.info("本地文件内容：{}", fileContext);

			assertEquals(fileContext, body);
		}
	}

}
