package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.properties.PassportProperties;
import cloud.xuxiaowei.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Tag(name = "首页", description = "未完成，仅测试")
@SecurityRequirement(name = "oauth2_clientCredentials")
@RestController
public class IndexRestController {

	private PassportProperties passportProperties;

	@Autowired
	public void setPassportProperties(PassportProperties passportProperties) {
		this.passportProperties = passportProperties;
	}

	@Operation(summary = "首页", description = "未完成，仅测试")
	@GetMapping
	public Response<String> indexGet(HttpServletRequest request, HttpServletResponse response) {

		String title = passportProperties.getTitle();

		Response<String> resp = Response.ok();
		resp.setData(title);

		return resp;
	}

	@Operation(summary = "首页", description = "未完成，仅测试")
	@PostMapping
	@PreAuthorize("hasAnyAuthority('message.read')")
	public Response<String> index(HttpServletRequest request, HttpServletResponse response) {

		String title = passportProperties.getTitle();

		Response<String> resp = Response.ok();
		resp.setData(title);

		return resp;
	}

}
