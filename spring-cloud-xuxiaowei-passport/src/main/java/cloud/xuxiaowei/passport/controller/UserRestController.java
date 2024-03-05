package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.dto.UserInfoDTO;
import cloud.xuxiaowei.passport.vo.UserInfoVO;
import cloud.xuxiaowei.utils.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

	/**
	 * @param request 请求
	 * @param response 响应
	 * @param userInfo
	 * @return
	 */
	@PostMapping("/info")
	public Response<UserInfoVO> info(HttpServletRequest request, HttpServletResponse response,
			@Valid @RequestBody UserInfoDTO userInfo) {

		UserInfoVO userInfoVO = new UserInfoVO();
		BeanUtils.copyProperties(userInfo, userInfoVO);

		Response<UserInfoVO> ok = Response.ok();
		ok.setData(userInfoVO);

		return ok;
	}

}
