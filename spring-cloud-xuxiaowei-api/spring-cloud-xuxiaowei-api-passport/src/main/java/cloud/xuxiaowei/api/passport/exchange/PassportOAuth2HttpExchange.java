package cloud.xuxiaowei.api.passport.exchange;

import cloud.xuxiaowei.utils.constant.ServiceConstant;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

/**
 * 登录授权服务 {@link HttpExchange}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@HttpExchange("http://" + ServiceConstant.PASSPORT_SERVICE)
public interface PassportOAuth2HttpExchange {

	@GetExchange("/.well-known/oauth-authorization-server")
	Map<String, Object> oauthAuthorizationServer();

}
