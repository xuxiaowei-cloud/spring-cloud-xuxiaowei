package cloud.xuxiaowei.loadbalancer.config;

import cloud.xuxiaowei.loadbalancer.SpringCloudXuxiaoweiCommonsLoadbalancerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest(classes = SpringCloudXuxiaoweiCommonsLoadbalancerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestTemplateLoadBalancedConfigTests {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void getInterceptors() {
		assertNotNull(new RestTemplate().getInterceptors());
		assertEquals(0, new RestTemplate().getInterceptors().size());

		assertNotNull(restTemplate.getInterceptors());
		assertEquals(1, restTemplate.getInterceptors().size());

		assertInstanceOf(LoadBalancerInterceptor.class, restTemplate.getInterceptors().get(0));
	}

}
