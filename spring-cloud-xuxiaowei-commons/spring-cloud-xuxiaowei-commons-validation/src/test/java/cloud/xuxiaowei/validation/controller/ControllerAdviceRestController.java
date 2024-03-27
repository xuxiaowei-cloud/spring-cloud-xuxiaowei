package cloud.xuxiaowei.validation.controller;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.exception.CloudException;
import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import cloud.xuxiaowei.validation.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class ControllerAdviceRestController {

	@GetMapping("/cloud-runtime-exception")
	public Response<?> cloudRuntimeException() {
		throw new CloudRuntimeException("测试 CloudRuntimeException 异常");
	}

	@GetMapping("/cloud-exception")
	public Response<?> cloudException() throws CloudException {
		throw new CloudException("测试 CloudException 异常");
	}

	@PostMapping("/method-argument-not-valid-exception")
	public Response<?> methodArgumentNotValidException(@Valid @RequestBody UserInfoDTO userInfo) {
		return Response.ok().setData(userInfo);
	}

	@PostMapping("/missing-servlet-request-part-exception")
	public Response<?> missingServletRequestPartException(@RequestParam("file") MultipartFile file) {
		return Response.ok();
	}

}
