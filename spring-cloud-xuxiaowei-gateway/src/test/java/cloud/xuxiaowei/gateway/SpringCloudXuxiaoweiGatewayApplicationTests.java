package cloud.xuxiaowei.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Slf4j
@SpringBootTest
public class SpringCloudXuxiaoweiGatewayApplicationTests {

	static final String CLIENT_ID = "messaging-client";

	static final String CLIENT_SECRET = "secret";

	public static String clientCredentialsAccessToken(RestTemplate restTemplate) throws JsonProcessingException {
		return clientCredentialsMap(restTemplate).get("access_token").toString();
	}

	public static Map clientCredentialsMap(RestTemplate restTemplate) throws JsonProcessingException {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.put("grant_type", Collections.singletonList("client_credentials"));
		requestBody.put("scope", Collections.singletonList("openid profile message.read message.write"));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		@SuppressWarnings("all")
		Map map = restTemplate.postForObject("http://xuxiaowei-passport/oauth2/token", httpEntity, Map.class);

		log.info("token:\n{}", objectWriter.writeValueAsString(map));

		return map;
	}

}
