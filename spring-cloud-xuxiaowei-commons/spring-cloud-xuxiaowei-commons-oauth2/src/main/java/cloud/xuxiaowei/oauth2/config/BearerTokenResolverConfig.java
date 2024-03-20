package cloud.xuxiaowei.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * Bearer 令牌解析 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class BearerTokenResolverConfig {

	/**
	 * 允许从 URL 参数中获取 Token，参数名 access_token
	 */
	@Bean
	public BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
		bearerTokenResolver.setAllowUriQueryParameter(true);
		return bearerTokenResolver;
	}

}
