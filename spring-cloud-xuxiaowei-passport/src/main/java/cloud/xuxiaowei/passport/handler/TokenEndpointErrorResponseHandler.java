package cloud.xuxiaowei.passport.handler;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth 2.1 Token 异常处理
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class TokenEndpointErrorResponseHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		log.error("OAuth 2.1 Token 异常处理：", exception);

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
