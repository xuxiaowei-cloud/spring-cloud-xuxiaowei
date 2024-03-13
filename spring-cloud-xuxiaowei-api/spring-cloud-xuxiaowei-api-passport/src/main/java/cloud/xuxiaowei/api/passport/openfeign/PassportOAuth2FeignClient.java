package cloud.xuxiaowei.api.passport.openfeign;

import cloud.xuxiaowei.openfeign.interceptor.AuthorizationRequestInterceptor;
import cloud.xuxiaowei.openfeign.interceptor.RequestIdRequestInterceptor;
import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.constant.ServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 登录授权服务 {@link FeignClient}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@FeignClient(value = ServiceConstants.PASSPORT_SERVICE,
		configuration = { RequestIdRequestInterceptor.class, AuthorizationRequestInterceptor.class })
public interface PassportOAuth2FeignClient {

	@GetMapping("/.well-known/oauth-authorization-server")
	Map<String, Object> oauthAuthorizationServer();

	@GetMapping("/")
	Response<?> index();

}
