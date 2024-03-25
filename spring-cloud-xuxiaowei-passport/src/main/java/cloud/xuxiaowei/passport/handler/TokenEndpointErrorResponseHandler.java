package cloud.xuxiaowei.passport.handler;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * OAuth 2.1 异常处理
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class TokenEndpointErrorResponseHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		log.error("身份验证失败处理程序：", exception);

		Response<?> error = Response.error();

		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2AuthenticationException e = (OAuth2AuthenticationException) exception;
			OAuth2Error oauth2Error = e.getError();
			String description = oauth2Error.getDescription();
			String errorCode = oauth2Error.getErrorCode();
			String uri = oauth2Error.getUri();

			error.setCode(errorCode);
			error.setMessage(description);
			error.setUrl(uri);
		}
		else {
			String message = "此异常暂未分类，请联系管理员";
			error.setMessage(message);
			log.error(message, exception);
		}

		ResponseUtils.response(response, error);
	}

}
