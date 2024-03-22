package cloud.xuxiaowei.utils;

import cloud.xuxiaowei.utils.constant.LogConstants;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.UUID;

/**
 * 响应数据父类
 * <p>
 * 支持链式调用
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @param <T> 响应数据泛型
 */
@Data
@Accessors(chain = true)
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 错误代码
	 */
	private String code;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 请求 ID
	 */
	@Setter(AccessLevel.PRIVATE)
	private String requestId;

	/**
	 * 响应数据
	 */
	private T data;

	public Response() {
		this.requestId = requestId();
	}

	public Response(T data) {
		this.requestId = requestId();
		this.data = data;
	}

	public static <T> Response<T> ok() {
		return new Response<T>().setSuccess(true).setRequestId(requestId());
	}

	public static <T> Response<T> ok(String message) {
		return new Response<T>().setSuccess(true).setMessage(message).setRequestId(requestId());
	}

	public static <T> Response<T> error() {
		return new Response<T>().setCode("500").setMessage("系统异常").setRequestId(requestId());
	}

	public static <T> Response<T> error(String message) {
		return new Response<T>().setCode("500").setMessage(message).setRequestId(requestId());
	}

	public static <T> Response<T> error(String code, String message) {
		return new Response<T>().setCode(code).setMessage(message).setRequestId(requestId());
	}

	private static String requestId() {
		String id = MDC.get(LogConstants.C_REQUEST_ID);
		if (id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}

}
