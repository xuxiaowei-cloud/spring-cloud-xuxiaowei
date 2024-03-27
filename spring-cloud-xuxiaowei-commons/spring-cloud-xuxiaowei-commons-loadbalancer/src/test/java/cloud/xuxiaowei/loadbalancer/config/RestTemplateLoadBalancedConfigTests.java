package cloud.xuxiaowei.loadbalancer.config;

import cloud.xuxiaowei.loadbalancer.SpringCloudXuxiaoweiCommonsLoadbalancerApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest(classes = SpringCloudXuxiaoweiCommonsLoadbalancerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestTemplateLoadBalancedConfigTests {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void getInterceptors() {
		assertNotNull(new RestTemplate().getInterceptors());
		assertEquals(0, new RestTemplate().getInterceptors().size());

		assertNotNull(restTemplate.getInterceptors());
		assertEquals(1, restTemplate.getInterceptors().size());

		assertInstanceOf(LoadBalancerInterceptor.class, restTemplate.getInterceptors().get(0));
	}

	@Test
	void remotePassport() throws JsonProcessingException {
		String url = "http://xuxiaowei-passport/.well-known/oauth-authorization-server";

		ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

		assertEquals(entity.getStatusCodeValue(), 200);

		Map<?, ?> body = entity.getBody();

		assertNotNull(body);

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));

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
	}

}
