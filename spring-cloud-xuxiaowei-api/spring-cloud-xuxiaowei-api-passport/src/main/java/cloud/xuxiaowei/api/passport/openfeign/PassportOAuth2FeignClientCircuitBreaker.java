package cloud.xuxiaowei.api.passport.openfeign;

import cloud.xuxiaowei.utils.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录授权服务 {@link FeignClient} 断路器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Service
public class PassportOAuth2FeignClientCircuitBreaker {

	private PassportOAuth2FeignClient passportOAuth2FeignClient;

	@Autowired
	public void setPassportOAuth2FeignClient(PassportOAuth2FeignClient passportOAuth2FeignClient) {
		this.passportOAuth2FeignClient = passportOAuth2FeignClient;
	}

	@CircuitBreaker(name = "#root.methodName", fallbackMethod = "oauthAuthorizationServerFallbackMethod")
	public Map<String, Object> oauthAuthorizationServer() {
		return passportOAuth2FeignClient.oauthAuthorizationServer();
	}

	@CircuitBreaker(name = "#root.methodName", fallbackMethod = "indexFallbackMethod")
	public Response<?> index() {
		return passportOAuth2FeignClient.index();
	}

	public Map<String, Object> oauthAuthorizationServerFallbackMethod(Throwable throwable) {
		log.error("请求异常：", throwable);
		Map<String, Object> map = new HashMap<>();
		map.put("msg", String.format("请求异常：%s", throwable));
		return map;
	}

	public Response<?> indexFallbackMethod(Throwable throwable) {
		log.error("请求异常：", throwable);
		return Response.error(String.format("请求异常：%s", throwable));
	}

}
