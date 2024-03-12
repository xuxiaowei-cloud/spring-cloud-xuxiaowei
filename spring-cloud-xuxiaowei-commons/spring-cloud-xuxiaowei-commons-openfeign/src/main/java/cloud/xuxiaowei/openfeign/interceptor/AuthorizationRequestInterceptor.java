package cloud.xuxiaowei.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求 授权 拦截器
 * <p>
 * 用于 feign 调用时，传递 Authorization
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class AuthorizationRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();

		template.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
	}

}
