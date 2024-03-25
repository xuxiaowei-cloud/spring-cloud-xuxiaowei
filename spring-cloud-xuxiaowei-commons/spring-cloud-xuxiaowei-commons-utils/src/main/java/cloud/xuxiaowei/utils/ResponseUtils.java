package cloud.xuxiaowei.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
public class ResponseUtils {

	/**
	 * 响应数据
	 * @param response 响应
	 * @param object Object 类型的数据
	 * @throws IOException IO 异常
	 */
	@SuppressWarnings({ "deprecation" })
	public static void response(HttpServletResponse response, Object object) throws IOException {
		response(response, object, MediaType.APPLICATION_JSON_UTF8, HttpStatus.OK);
	}

	/**
	 * 响应数据
	 * @param response 响应
	 * @param object Object 类型的数据
	 * @param httpStatus 响应状态码
	 * @throws IOException IO 异常
	 */
	@SuppressWarnings({ "deprecation" })
	public static void response(HttpServletResponse response, Object object, HttpStatus httpStatus) throws IOException {
		response(response, object, MediaType.APPLICATION_JSON_UTF8, httpStatus);
	}

	/**
	 * 响应数据
	 * @param response 响应
	 * @param object Object 类型的数据
	 * @param contentType 响应类型
	 * @throws IOException IO 异常
	 */
	public static void response(HttpServletResponse response, Object object, MediaType contentType) throws IOException {
		response(response, object, contentType, HttpStatus.OK);
	}

	/**
	 * 响应数据
	 * @param response 响应
	 * @param object Object 类型的数据
	 * @param contentType 响应类型
	 * @param httpStatus 响应状态码
	 * @throws IOException IO 异常
	 */
	public static void response(HttpServletResponse response, Object object, MediaType contentType,
			HttpStatus httpStatus) throws IOException {
		response.setContentType(contentType.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String json = objectMapper.writeValueAsString(object);
		response.getWriter().println(json);
		response.setStatus(httpStatus.value());
		response.flushBuffer();
	}

}
