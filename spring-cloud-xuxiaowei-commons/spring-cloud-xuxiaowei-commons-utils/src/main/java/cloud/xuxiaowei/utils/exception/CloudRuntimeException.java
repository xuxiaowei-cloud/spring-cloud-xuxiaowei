package cloud.xuxiaowei.utils.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微服务运行时父异常
 *
 * @see CloudException 微服务父异常
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudRuntimeException extends RuntimeException {

	/**
	 * 错误代码
	 */
	private String code;

	public CloudRuntimeException(String message) {
		super(message);
		this.code = "500";
	}

	public CloudRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.code = "500";
	}

	public CloudRuntimeException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

}
