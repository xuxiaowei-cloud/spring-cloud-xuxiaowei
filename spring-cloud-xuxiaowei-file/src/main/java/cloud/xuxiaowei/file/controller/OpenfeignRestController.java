package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.api.passport.openfeign.PassportOAuth2FeignClientCircuitBreaker;
import cloud.xuxiaowei.core.annotation.ControllerAnnotation;
import cloud.xuxiaowei.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 openfeign
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Tag(name = "测试 openfeign")
@SecurityRequirement(name = "oauth2_clientCredentials")
@RestController
@RequestMapping("/openfeign")
public class OpenfeignRestController {

	private PassportOAuth2FeignClientCircuitBreaker passportOAuth2FeignClientCircuitBreaker;

	@Autowired
	public void setPassportOAuth2FeignClientCircuitBreaker(
			PassportOAuth2FeignClientCircuitBreaker passportOAuth2FeignClientCircuitBreaker) {
		this.passportOAuth2FeignClientCircuitBreaker = passportOAuth2FeignClientCircuitBreaker;
	}

	/**
	 * 测试 openfeign 调用 Passport
	 * @param request 请求
	 * @param response 响应
	 * @return 返回上传结果
	 */
	@PostMapping
	@Operation(summary = "测试 openfeign 调用 Passport")
	@ControllerAnnotation("测试 openfeign 调用 Passport")
	@PreAuthorize("hasAnyAuthority('message.read')")
	public Response<?> index(HttpServletRequest request, HttpServletResponse response) {
		return passportOAuth2FeignClientCircuitBreaker.index();
	}

}
