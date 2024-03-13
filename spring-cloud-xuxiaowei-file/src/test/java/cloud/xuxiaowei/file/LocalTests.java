package cloud.xuxiaowei.file;

import cloud.xuxiaowei.file.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static cloud.xuxiaowei.file.SpringCloudXuxiaoweiFileApplicationTests.clientCredentialsAccessToken;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocalTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private FileProperties fileProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void restTemplate() throws IOException {

		String accessToken = clientCredentialsAccessToken(restTemplate);

		List<FileProperties.ResourceHandler> resourceHandlers = fileProperties.getResourceHandlers();

		for (FileProperties.ResourceHandler resourceHandler : resourceHandlers) {

			String addResourceHandler = resourceHandler.getAddResourceHandler();
			String addResourceLocations = resourceHandler.getAddResourceLocations();

			String path = addResourceLocations.replaceFirst("file:", "");

			String fileName = UUID.randomUUID() + ".txt";
			String filePath = UriComponentsBuilder.newInstance().path(path).path("/").path(fileName).toUriString();
			String fileUrl = String.format("http://127.0.0.1:%d%s%s?access_token=%s", serverPort,
					addResourceHandler.replace("**", ""), fileName, accessToken);
			String fileContext = fileName + "-123";

			File file = new File(filePath);

			assertFalse(file.exists());

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileContext);
			writer.close();
			log.info("文件已创建: {}", filePath);

			ResponseEntity<String> entity = new RestTemplate().getForEntity(fileUrl, String.class);

			assertEquals(entity.getStatusCodeValue(), 200);

			String body = entity.getBody();
			log.info("网络文件：{} 内容：{}", fileUrl, body);
			log.info("本地文件：{} 内容：{}", filePath, fileContext);

			assertEquals(fileContext, body);
		}

	}

}
