package cloud.xuxiaowei.file.exchange;

import cloud.xuxiaowei.api.passport.exchange.PassportOAuth2HttpExchange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 登录授权服务 {@link FeignClient} 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class PassportOAuth2HttpExchangeTests {

	@Autowired
	private PassportOAuth2HttpExchange passportOAuth2HttpExchange;

	@Test
	void oauthAuthorizationServer() throws JsonProcessingException {
		Map<String, Object> map = passportOAuth2HttpExchange.oauthAuthorizationServer();

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		log.info("登录授权服务 .well-known/oauth-authorization-server 接口 测试结果:\n{}", objectWriter.writeValueAsString(map));

		assertNotNull(map.get("issuer"));
		assertNotNull(map.get("authorization_endpoint"));
		assertNotNull(map.get("token_endpoint"));
		assertNotNull(map.get("token_endpoint_auth_methods_supported"));
		assertNotNull(map.get("jwks_uri"));
		assertNotNull(map.get("response_types_supported"));
		assertNotNull(map.get("grant_types_supported"));
		assertNotNull(map.get("revocation_endpoint"));
		assertNotNull(map.get("revocation_endpoint_auth_methods_supported"));
		assertNotNull(map.get("introspection_endpoint"));
		assertNotNull(map.get("introspection_endpoint_auth_methods_supported"));
		assertNotNull(map.get("code_challenge_methods_supported"));
	}

}
