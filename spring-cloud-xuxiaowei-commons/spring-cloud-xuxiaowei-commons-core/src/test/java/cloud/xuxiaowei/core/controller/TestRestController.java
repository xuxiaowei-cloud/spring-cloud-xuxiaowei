package cloud.xuxiaowei.core.controller;

import cloud.xuxiaowei.core.annotation.ControllerAnnotation;
import cloud.xuxiaowei.utils.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class TestRestController {

	@ControllerAnnotation("测试 doFilter")
	@GetMapping("/doFilter")
	public Response<?> doFilter() {
		return Response.ok();
	}

}
