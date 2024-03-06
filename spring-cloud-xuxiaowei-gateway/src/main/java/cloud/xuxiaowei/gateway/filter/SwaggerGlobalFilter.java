package cloud.xuxiaowei.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改 Swagger 接口响应结果，使用网关地址调用
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Setter
@Slf4j
@Component
public class SwaggerGlobalFilter implements GlobalFilter, Ordered {

	public static final int ORDERED = Ordered.HIGHEST_PRECEDENCE + 10000;

	private static final Pattern REGEX_PATTERN = Pattern.compile("^/([^/]+)/v3/api-docs$");

	private int order = ORDERED;

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		URI uri = request.getURI();
		String path = uri.getPath();

		Matcher matcher = REGEX_PATTERN.matcher(path);

		if (!matcher.find()) {
			return chain.filter(exchange);
		}

		String service = matcher.group(1);

		String uriString = UriComponentsBuilder.newInstance()
			.scheme(uri.getScheme())
			.host(uri.getHost())
			.port(uri.getPort())
			.path(service)
			.toUriString();

		ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response) {
			@NonNull
			@Override
			public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {

				Flux<? extends DataBuffer> fluxDataBuffer = (Flux<? extends DataBuffer>) body;

				return response.writeWith(fluxDataBuffer.buffer().map(dataBuffer -> {

					DataBuffer join = response.bufferFactory().join(dataBuffer);

					byte[] bytes = new byte[join.readableByteCount()];
					join.read(bytes);
					DataBufferUtils.release(join);

					String originalText = new String(bytes);
					String result;

					ObjectMapper objectMapper = new ObjectMapper();
					try {

						@SuppressWarnings("unchecked")
						Map<String, Object> map = objectMapper.readValue(originalText, Map.class);
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> servers = (List<Map<String, Object>>) map.get("servers");

						for (Map<String, Object> server : servers) {
							Object urlObj = server.get("url");
							if (urlObj != null) {
								server.put("url", uriString);
							}
						}

						result = objectMapper.writeValueAsString(map);
					}
					catch (JsonProcessingException e) {
						log.error("解析并修改 Swagger 接口响应异常，使用原始返回结果：", e);
						result = originalText;
					}

					response.getHeaders().setContentLength(result.getBytes().length);
					return response.bufferFactory().wrap(result.getBytes());
				}));
			}
		};

		return chain.filter(exchange.mutate().response(decorator).build());
	}

}
