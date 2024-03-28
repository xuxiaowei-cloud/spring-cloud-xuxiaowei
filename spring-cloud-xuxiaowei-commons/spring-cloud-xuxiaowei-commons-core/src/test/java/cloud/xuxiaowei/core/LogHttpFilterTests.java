package cloud.xuxiaowei.core;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.utils.FileUtils;
import cloud.xuxiaowei.utils.RSAUtils;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyPair;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest(classes = { SpringCloudXuxiaoweiCommonsCoreApplication.class },
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogHttpFilterTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	private SecurityProperties securityProperties;

	@Test
	void doFilter() throws IOException {
		String loggingFileName = environment.getProperty("logging.file.name");

		assertNotNull(loggingFileName);

		List<String> list = FileUtils.readList(loggingFileName);
		int startLine = list.size();

		String ip = "127.0.0.1";
		String uri = "/doFilter";

		String url = String.format("http://%s:%s%s", ip, serverPort, uri);

		RestTemplate restTemplate = new RestTemplate();
		Response<?> response = restTemplate.getForObject(url, Response.class);
		assertNotNull(response);
		assertTrue(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNull(response.getUrl());
		assertNotNull(response.getRequestId());
		assertNull(response.getData());

		list = FileUtils.readList(loggingFileName);
		int endLine = list.size();

		String requestId = response.getRequestId();

		String contains1 = String.format(": %s :", requestId);
		String contains2 = String.format(": %s :", ip);
		String contains3 = String.format("URI: %s", uri);

		boolean contains = false;
		boolean containsPart = false;

		for (int i = startLine; i < endLine; i++) {
			String line = list.get(i);

			// 访问 URL 日志
			if (line.contains(contains1) && line.contains(contains2) && line.contains(contains3)) {
				contains = true;
			}

			// 普通日志
			if (line.contains(contains1) && line.contains(contains2) && !line.contains(contains3)) {
				containsPart = true;
			}
		}

		assertTrue(contains);
		assertTrue(containsPart);
	}

	@Test
	void properties() {
		assertNotNull(securityProperties.publicKey());
		assertNotNull(securityProperties.privateKey());
		assertNotNull(securityProperties.rsaPublicKey());

		KeyPair keyPair = RSAUtils.create(securityProperties.getPublicKey(), securityProperties.getPrivateKey());
		assertNotNull(keyPair);
	}

}
