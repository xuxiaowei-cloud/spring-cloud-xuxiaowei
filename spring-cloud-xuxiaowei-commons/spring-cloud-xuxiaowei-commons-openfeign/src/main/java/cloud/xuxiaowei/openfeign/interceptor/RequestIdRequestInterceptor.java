package cloud.xuxiaowei.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static cloud.xuxiaowei.utils.constant.LogConstant.C_REQUEST_ID;

/**
 * 请求 ID 拦截器
 * <p>
 * 用于 feign 调用时，传递 请求 ID
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class RequestIdRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();

		template.header(C_REQUEST_ID, request.getHeader(C_REQUEST_ID));
	}

}
