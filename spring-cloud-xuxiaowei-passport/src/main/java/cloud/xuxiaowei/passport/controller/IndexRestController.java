package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.properties.PassportProperties;
import cloud.xuxiaowei.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public Response<String> index(HttpServletRequest request, HttpServletResponse response) {

		String title = passportProperties.getTitle();

		Response<String> resp = Response.ok();
		resp.setData(title);

		return resp;
	}

}
