package cloud.xuxiaowei.core.utils;

import cloud.xuxiaowei.core.properties.SecurityProperties;
import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import lombok.Getter;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Getter
public enum AuthorizationDecisionEnum {

	/**
	 * 允许任何人访问
	 */
	permitAll(SecurityProperties.Access.permitAll, (a, o) -> new AuthorizationDecision(true)),

	/**
	 * 不允许任何人访问
	 */
	denyAll(SecurityProperties.Access.denyAll, (a, o) -> new AuthorizationDecision(false)),

	/**
	 * 允许匿名用户访问
	 */
	anonymous(SecurityProperties.Access.anonymous, AuthenticatedAuthorizationManager.anonymous()),

	/**
	 * 允许经过身份验证的用户访问
	 */
	authenticated(SecurityProperties.Access.authenticated, AuthenticatedAuthorizationManager.authenticated()),

	/**
	 * 允许经过身份验证的用户访问，但不包含 remembered 类型的用户
	 */
	fullyAuthenticated(SecurityProperties.Access.fullyAuthenticated,
			AuthenticatedAuthorizationManager.fullyAuthenticated()),

	/**
	 * 允许 remembered 类型登录的用户访问
	 */
	rememberMe(SecurityProperties.Access.rememberMe, AuthenticatedAuthorizationManager.rememberMe());

	private final SecurityProperties.Access access;

	private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;

	AuthorizationDecisionEnum(SecurityProperties.Access access,
			AuthorizationManager<RequestAuthorizationContext> authorizationManager) {
		this.access = access;
		this.authorizationManager = authorizationManager;
	}

	static AuthorizationDecisionEnum access(SecurityProperties.Access access) {
		AuthorizationDecisionEnum[] values = values();
		for (AuthorizationDecisionEnum value : values) {
			if (value.access.equals(access)) {
				return value;
			}
		}
		throw new CloudRuntimeException("访问权限不合法");
	}

}
