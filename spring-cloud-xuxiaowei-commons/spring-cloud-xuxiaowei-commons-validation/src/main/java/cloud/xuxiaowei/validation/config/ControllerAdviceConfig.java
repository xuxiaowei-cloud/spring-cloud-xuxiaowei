package cloud.xuxiaowei.validation.config;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.exception.CloudException;
import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import com.google.common.base.Joiner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link org.springframework.stereotype.Controller}、{@link org.springframework.web.bind.annotation.RestController}
 * 异常处理
 * <p>
 * 1. {@link ControllerAdvice} 不支持通配符
 * <p>
 * 2. 直接扫描
 * {@link org.springframework.stereotype.Controller}、{@link org.springframework.web.bind.annotation.RestController}
 * 父包
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@ControllerAdvice({ "cloud.xuxiaowei" })
public class ControllerAdviceConfig {

	/**
	 * 微服务运行时父异常 处理
	 * @param exception 微服务运行时父异常
	 * @param request 请求
	 * @param response 响应
	 * @return 返回 异常消息
	 */
	@ResponseBody
	@ExceptionHandler(CloudRuntimeException.class)
	public Response<?> cloudRuntimeException(CloudRuntimeException exception, HttpServletRequest request,
			HttpServletResponse response) {

		String code = exception.getCode();
		String message = exception.getMessage();

		log.error(String.format("%s：%s", code, message), exception);

		return Response.error(code, message);
	}

	/**
	 * 微服务父异常 处理
	 * @param exception 微服务父异常
	 * @param request 请求
	 * @param response 响应
	 * @return 返回 异常消息
	 */
	@ResponseBody
	@ExceptionHandler(CloudException.class)
	public Response<?> cloudException(CloudException exception, HttpServletRequest request,
			HttpServletResponse response) {

		String code = exception.getCode();
		String message = exception.getMessage();

		log.error(String.format("%s：%s", code, message), exception);

		return Response.error(code, message);
	}

	/**
	 * 方法参数验证异常 处理
	 * @param exception 方法参数验证异常
	 * @param request 请求
	 * @param response 相应
	 * @return 返回 异常消息
	 */
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response<?> methodArgumentNotValidException(MethodArgumentNotValidException exception,
			HttpServletRequest request, HttpServletResponse response) {

		String objectName = exception.getObjectName();

		FieldError fieldError = exception.getFieldError();
		List<ObjectError> allErrors = exception.getAllErrors();

		int errorCount = exception.getErrorCount();
		Map<String, Object> model = exception.getModel();

		BindingResult bindingResult = exception.getBindingResult();
		MethodParameter parameter = exception.getParameter();

		int fieldErrorCount = exception.getFieldErrorCount();

		String message = exception.getMessage();

		if (fieldErrorCount == 1) {
			if (fieldError == null) {
				return Response.error(message);
			}
			else {
				String defaultMessage = fieldError.getDefaultMessage();
				return Response.error(defaultMessage);
			}
		}
		else {
			List<String> list = new ArrayList<>();
			for (ObjectError objectError : allErrors) {
				String defaultMessage = objectError.getDefaultMessage();
				list.add(defaultMessage);
			}
			String result = Joiner.on(",").join(list);
			return Response.error(result);
		}

	}

	/**
	 * multipart/form-data 请求 方法参数验证异常 处理
	 * @param exception multipart/form-data 请求 方法参数验证异常
	 * @param request 请求
	 * @param response 相应
	 * @return 返回 异常消息
	 */
	@ResponseBody
	@ExceptionHandler(MissingServletRequestPartException.class)
	public Response<?> missingServletRequestPartException(MissingServletRequestPartException exception,
			HttpServletRequest request, HttpServletResponse response) {
		String requestPartName = exception.getRequestPartName();
		return Response.error(String.format("缺少参数: %s", requestPartName));
	}

}
