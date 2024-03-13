package cloud.xuxiaowei.gateway.filter;

import cloud.xuxiaowei.utils.constant.LogConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * 请求头 过滤器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Setter
@Slf4j
@Component
public class RequestHeaderGlobalFilter implements GlobalFilter, Ordered {

	public static final int ORDERED = Ordered.HIGHEST_PRECEDENCE + 20000;

	private int order = ORDERED;

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		String id = request.getId();
		InetSocketAddress remoteAddress = request.getRemoteAddress();
		HttpHeaders headers = request.getHeaders();
		String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
		InetAddress address = remoteAddress.getAddress();
		String hostName = address.getHostName();
		String hostAddress = address.getHostAddress();

		Consumer<HttpHeaders> httpHeaders = httpHeader -> {
			httpHeader.set(LogConstants.C_REQUEST_ID, id);
			httpHeader.set(LogConstants.C_HOST_NAME, hostName);
			httpHeader.set(LogConstants.C_HOST_ADDRESS, hostAddress);
			httpHeader.set(HttpHeaders.AUTHORIZATION, authorization);
		};

		ServerHttpRequest build = request.mutate().headers(httpHeaders).build();

		return chain.filter(exchange.mutate().request(build).build());
	}

}
