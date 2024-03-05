package cloud.xuxiaowei.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
	 * 响应数据
	 */
	private T data;

	public static <T> Response<T> ok() {
		return new Response<T>().setSuccess(true);
	}

	public static <T> Response<T> ok(String message) {
		return new Response<T>().setSuccess(true).setMessage(message);
	}

	public static <T> Response<T> error() {
		return new Response<T>().setMessage("系统异常");
	}

	public static <T> Response<T> error(String message) {
		return new Response<T>().setMessage(message);
	}

	public static <T> Response<T> error(String code, String message) {
		return new Response<T>().setCode(code).setMessage(message);
	}

}
