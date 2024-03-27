package cloud.xuxiaowei.loadbalancer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebClientLoadBalancedConfigTests {

	@Autowired
	private WebClient webClient;

	@Test
	void remotePassport() {

		webClient.get()
			.uri("http://xuxiaowei-passport/.well-known/oauth-authorization-server")
			.exchange()
			.flatMap(response -> {
				if (response.statusCode().is2xxSuccessful()) {
					return response.bodyToMono(Map.class);
				}
				else {
					return Mono.error(new RuntimeException("无法提取数据。Status code: " + response.statusCode()));
				}
			})
			.subscribe(body -> {
				assertNotNull(body);

				ObjectMapper objectMapper = new ObjectMapper();
				ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

				try {
					log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));
				}
				catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}

				assertNotNull(body.get("issuer"));
				assertNotNull(body.get("authorization_endpoint"));
				assertNotNull(body.get("token_endpoint"));
				assertNotNull(body.get("token_endpoint_auth_methods_supported"));
				assertNotNull(body.get("jwks_uri"));
				assertNotNull(body.get("response_types_supported"));
				assertNotNull(body.get("grant_types_supported"));
				assertNotNull(body.get("revocation_endpoint"));
				assertNotNull(body.get("revocation_endpoint_auth_methods_supported"));
				assertNotNull(body.get("introspection_endpoint"));
				assertNotNull(body.get("introspection_endpoint_auth_methods_supported"));
				assertNotNull(body.get("code_challenge_methods_supported"));
			});
	}

}
