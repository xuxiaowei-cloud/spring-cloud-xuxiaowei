package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import static cloud.xuxiaowei.file.SpringCloudXuxiaoweiFileApplicationTests.clientCredentialsAccessToken;
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

		RSAPublicKey rsaPublicKey = securityProperties.rsaPublicKey();
		NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
			.withPublicKey(rsaPublicKey);
		NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
		Jwt jwt = nimbusJwtDecoder.decode(accessToken);

		MockMultipartFile file = new MockMultipartFile("file", originalFilename, MediaType.TEXT_PLAIN_VALUE,
				fileContext.getBytes());

		MvcResult mvcResult = mockMvc
			.perform(MockMvcRequestBuilders.multipart(url)
				.file(file)
				.with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andExpect(status().isOk())
			.andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		log.info(content);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		Map result = converter.getObjectMapper().readValue(content, Map.class);
		String fileUrl = result.get("url").toString();

		fileUrl = UriComponentsBuilder.newInstance()
			.uri(new URI(fileUrl))
			.port(serverPort)
			.queryParam("access_token", accessToken)
			.toUriString();

		ResponseEntity<String> entity = new RestTemplate().getForEntity(fileUrl, String.class);

		Assert.isTrue(entity.getStatusCodeValue() == 200, String.format("文件: %s HTTP 状态码不正常", fileUrl));

		String body = entity.getBody();
		log.info("网络文件：{} 内容：{}", fileUrl, body);
		log.info("本地文件内容：{}", fileContext);
		Assert.isTrue(fileContext.equals(body), String.format("网络文件: %s 与本地文件 内容不同", fileUrl));
	}

}
