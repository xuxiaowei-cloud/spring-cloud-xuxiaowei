package cloud.xuxiaowei.gateway;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorTests {

	@LocalServerPort
	private int serverPort;

	@Test
	void actuator() {

		String url = String.format("http://127.0.0.1:%d/actuator", serverPort);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

		assertEquals(entity.getStatusCode(), HttpStatus.OK);

		Map map = entity.getBody();

		assertNotNull(map);
		assertNotNull(map.get("_links"));
	}

}
