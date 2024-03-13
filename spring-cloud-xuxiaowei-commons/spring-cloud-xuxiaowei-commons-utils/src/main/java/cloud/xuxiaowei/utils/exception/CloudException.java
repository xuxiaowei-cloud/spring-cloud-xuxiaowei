package cloud.xuxiaowei.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 微服务父异常
 *
 * @see CloudRuntimeException 微服务运行时父异常
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudException extends Exception {

	/**
	 * 错误代码
	 */
	private String code;

	/**
	 * 参考
	 */
	private List<String> references;

	public CloudException(String message) {
		super(message);
		this.code = "500";
	}

	public CloudException(String message, Throwable cause) {
		super(message, cause);
		this.code = "500";
	}

	public CloudException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
