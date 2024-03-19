package cloud.xuxiaowei.core.utils;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.List;

/**
 * Security 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class SecurityUtils {

	/**
	 * 配置路径权限
	 * @param requestMatchers 请求路径匹配，用于权限控制
	 * @param authorizeRequests 授权请求
	 */
	public static void authorizeRequests(List<SecurityProperties.RequestMatcher> requestMatchers,
			ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {

		for (SecurityProperties.RequestMatcher requestMatcher : requestMatchers) {
			String[] patterns = requestMatcher.getPatterns();
			SecurityProperties.Access[] accesses = requestMatcher.getAccesses();
			String ipAddress = requestMatcher.getIpAddress();
			HttpMethod method = requestMatcher.getMethod();
			if (method == null) {
				for (SecurityProperties.Access access : accesses) {
					authorizeRequests.mvcMatchers(patterns).access(access.toString());
				}

				if (ipAddress != null) {
					authorizeRequests.mvcMatchers(patterns).hasIpAddress(ipAddress);
				}
			}
			else {
				for (SecurityProperties.Access access : accesses) {
					authorizeRequests.mvcMatchers(method, patterns).access(access.toString());
				}

				if (ipAddress != null) {
					authorizeRequests.mvcMatchers(method, patterns).hasIpAddress(ipAddress);
				}
			}
		}
	}

}
