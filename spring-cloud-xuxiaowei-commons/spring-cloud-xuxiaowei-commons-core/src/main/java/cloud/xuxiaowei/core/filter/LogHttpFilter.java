package cloud.xuxiaowei.core.filter;

import cloud.xuxiaowei.utils.constant.LogConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志 过滤器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Setter
@Component
public class LogHttpFilter extends HttpFilter implements Ordered {

	public static final int ORDERED = Ordered.HIGHEST_PRECEDENCE;

	private int order = ORDERED;

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String uri = request.getRequestURI();
		String remoteHost = request.getRemoteHost();
		String remoteAddr = request.getRemoteAddr();

		String id = request.getHeader(LogConstants.C_REQUEST_ID);
		if (!StringUtils.hasText(id)) {
			id = RandomStringUtils.randomAlphanumeric(8);
		}

		MDC.put(LogConstants.C_REQUEST_ID, id);
		MDC.put(LogConstants.C_IP, remoteHost);

		log.debug("URI: {}, {}: {}, {}: {}", uri, LogConstants.C_REMOTE_HOST, remoteHost, LogConstants.C_REMOTE_ADDR,
				remoteAddr);

		super.doFilter(request, response, chain);
	}

}
