package cloud.xuxiaowei.oauth2.point;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源服务 身份验证入口点
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class ResourceServerAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		log.error("资源服务 身份验证入口点：", authException);

		Response<?> error = Response.error();

		if (authException instanceof InvalidBearerTokenException) {
			InvalidBearerTokenException e = (InvalidBearerTokenException) authException;
			OAuth2Error oauth2Error = e.getError();
			String description = oauth2Error.getDescription();
			String errorCode = oauth2Error.getErrorCode();
			String uri = oauth2Error.getUri();

			error.setCode(errorCode);
			error.setMessage(description);
			error.setUrl(uri);
		}
		else if (authException instanceof InsufficientAuthenticationException) {
			InsufficientAuthenticationException e = (InsufficientAuthenticationException) authException;
			error.setMessage(e.getMessage());
		}
		else {
			String message = "此异常暂未分类，请联系管理员";
			error.setMessage(message);
			log.error(message, authException);
		}

		ResponseUtils.response(response, error);

	}

}
