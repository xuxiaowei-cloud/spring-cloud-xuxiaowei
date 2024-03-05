package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.properties.PassportProperties;
import cloud.xuxiaowei.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class IndexRestController {

	private PassportProperties passportProperties;

	@Autowired
	public void setPassportProperties(PassportProperties passportProperties) {
		this.passportProperties = passportProperties;
	}

	@RequestMapping
	public Response<String> index(HttpServletRequest request, HttpServletResponse response) {

		String title = passportProperties.getTitle();

		Response<String> resp = Response.ok();
		resp.setData(title);

		return resp;
	}

}
