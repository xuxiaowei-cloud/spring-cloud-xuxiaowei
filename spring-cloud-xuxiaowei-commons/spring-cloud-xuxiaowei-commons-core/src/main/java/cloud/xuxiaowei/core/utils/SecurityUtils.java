package cloud.xuxiaowei.core.utils;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

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
	 * @param authorizeRequestsCustomizer 授权请求
	 */
	public static void authorizeRequests(List<SecurityProperties.RequestMatcher> requestMatchers,
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequestsCustomizer) {

		for (SecurityProperties.RequestMatcher requestMatcher : requestMatchers) {
			String[] patterns = requestMatcher.getPatterns();
			SecurityProperties.Access[] accesses = requestMatcher.getAccesses();
			String ipAddress = requestMatcher.getIpAddress();
			HttpMethod method = requestMatcher.getMethod();
			if (method == null) {
				for (SecurityProperties.Access access : accesses) {
					authorizeRequestsCustomizer.requestMatchers(patterns)
						.access(AuthorizationDecisionEnum.access(access).getAuthorizationManager());
				}

				if (ipAddress != null) {
					IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
					for (String pattern : patterns) {
						authorizeRequestsCustomizer.requestMatchers(request -> {
							boolean matchesIp = ipAddressMatcher.matches(request);
							AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(pattern);
							boolean matchesPattern = antPathRequestMatcher.matches(request);
							return matchesIp && matchesPattern;
						}).permitAll();
					}
				}
			}
			else {
				for (SecurityProperties.Access access : accesses) {
					authorizeRequestsCustomizer.requestMatchers(method, patterns)
						.access(AuthorizationDecisionEnum.access(access).getAuthorizationManager());
				}

				if (ipAddress != null) {
					IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
					for (String pattern : patterns) {
						authorizeRequestsCustomizer.requestMatchers(request -> {
							boolean matchesIp = ipAddressMatcher.matches(request);
							AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(pattern);
							boolean matchesPattern = antPathRequestMatcher.matches(request);
							return matchesIp && matchesPattern && method.matches(request.getMethod());
						}).permitAll();
					}
				}
			}
		}
	}

}
