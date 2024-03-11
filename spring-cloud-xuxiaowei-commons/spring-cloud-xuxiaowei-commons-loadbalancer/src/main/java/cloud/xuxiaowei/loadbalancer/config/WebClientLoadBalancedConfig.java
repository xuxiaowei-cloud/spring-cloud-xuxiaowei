package cloud.xuxiaowei.loadbalancer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@ConditionalOnClass(WebClient.class)
public class WebClientLoadBalancedConfig {

	@Bean
	@LoadBalanced
	public WebClient webClient(ReactorLoadBalancerExchangeFilterFunction exchangeFilterFunction) {
		return WebClient.builder().filter(exchangeFilterFunction).build();
	}

}
