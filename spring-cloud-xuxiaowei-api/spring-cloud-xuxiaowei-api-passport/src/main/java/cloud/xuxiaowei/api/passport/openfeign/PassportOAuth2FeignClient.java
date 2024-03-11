package cloud.xuxiaowei.api.passport.openfeign;

import cloud.xuxiaowei.utils.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 登录授权服务 {@link FeignClient}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@FeignClient(value = ServiceConstant.PASSPORT_SERVICE)
public interface PassportOAuth2FeignClient {

	@GetMapping("/.well-known/oauth-authorization-server")
	Map<String, Object> oauthAuthorizationServer();

}
