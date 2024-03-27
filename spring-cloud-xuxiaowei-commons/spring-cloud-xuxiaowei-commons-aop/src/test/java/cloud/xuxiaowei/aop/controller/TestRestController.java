package cloud.xuxiaowei.aop.controller;

import cloud.xuxiaowei.utils.Response;
import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class TestRestController {

	@GetMapping("/ok")
	public Response<?> ok() {
		return Response.ok();
	}

	@GetMapping("/cloud-runtime-exception")
	public Response<?> cloudRuntimeException() {
		throw new CloudRuntimeException("测试 CloudRuntimeException 异常");
	}

}
