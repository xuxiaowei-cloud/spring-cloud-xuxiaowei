package cloud.xuxiaowei.core.properties;

import cloud.xuxiaowei.utils.RSAUtils;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * 微服务 安全配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@ConfigurationProperties("xuxiaowei.cloud.security")
public class SecurityProperties {

	/**
	 * RSA 公钥
	 */
	private String publicKey;

	/**
	 * RSA 私钥
	 */
	private String privateKey;

	/**
	 * 请求路径匹配，用于权限控制
	 */
	private List<RequestMatcher> requestMatchers;

	/**
	 * 检查 Token 时 Redis 的前缀
	 */
	private String tokenCheckPrefix = "spring-authorization-server:oauth2_authorization:access_token:";

	public PublicKey publicKey() {
		return RSAUtils.publicKey(this.publicKey);
	}

	public RSAPublicKey rsaPublicKey() {
		return (RSAPublicKey) publicKey();
	}

	public PrivateKey privateKey() {
		return RSAUtils.privateKey(this.privateKey);
	}

	/**
	 * 请求路径匹配，用于权限控制
	 *
	 * @since 0.0.1
	 * @author xuxiaowei
	 */
	@Data
	public static class RequestMatcher {

		/**
		 * 路径
		 */
		private String[] patterns = new String[0];

		/**
		 * 访问权限
		 */
		private Access[] accesses = new Access[0];

		/**
		 * 请求的方法
		 */
		private HttpMethod method;

		/**
		 * 允许的 IP 地址，一个路径表达式只能配置一个 CIDR，配置多个时，只有第一个 CIDR 有效
		 * <p>
		 * 常用内网 IP：
		 * <p>
		 * CIDR：192.168.0.0/16：192.168.0.0 到 192.168.255.255
		 * <p>
		 * CIDR：172.16.0.0/12：172.16.0.0 到 172.31.255.255
		 * <p>
		 * CIDR：10.0.0.0/8：10.0.0.0 到 10.255.255.255
		 */
		private String ipAddress;

	}

	/**
	 * 访问权限
	 */
	@Getter
	public enum Access {

		/**
		 * 允许任何人访问
		 */
		permitAll,

		/**
		 * 不允许任何人访问
		 */
		denyAll,

		/**
		 * 允许匿名用户访问
		 */
		anonymous,

		/**
		 * 允许经过身份验证的用户访问
		 */
		authenticated,

		/**
		 * 允许经过身份验证的用户访问，但不包含 remembered 类型的用户
		 */
		fullyAuthenticated,

		/**
		 * 允许 remembered 类型登录的用户访问
		 */
		rememberMe

	}

}
