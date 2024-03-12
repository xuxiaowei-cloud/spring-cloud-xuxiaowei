package cloud.xuxiaowei.gateway.filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

import static cloud.xuxiaowei.utils.constant.LogConstant.*;

/**
 * 日志 过滤器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Setter
@Component
public class LogWebFilter implements WebFilter, Ordered {

	public static final int ORDERED = Ordered.HIGHEST_PRECEDENCE;

	private int order = ORDERED;

	@Override
	public int getOrder() {
		return order;
	}

	@NonNull
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		HttpHeaders headers = response.getHeaders();

		String id = request.getId();
		URI uri = request.getURI();
		InetSocketAddress remoteAddress = request.getRemoteAddress();
		InetAddress address = remoteAddress.getAddress();
		String hostName = address.getHostName();
		String hostAddress = address.getHostAddress();

		MDC.put(C_REQUEST_ID, id);
		MDC.put(C_IP, hostName);

		log.debug("URI: {}, {}: {}, {}: {}", uri, C_HOST_NAME, hostName, C_HOST_ADDRESS, hostAddress);

		headers.set(C_REQUEST_ID, id);

		return chain.filter(exchange);
	}

}
