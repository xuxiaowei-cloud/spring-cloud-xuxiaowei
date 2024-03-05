package cloud.xuxiaowei.passport.controller;

import cloud.xuxiaowei.passport.dto.UserInfoDTO;
import cloud.xuxiaowei.passport.vo.UserInfoVO;
import cloud.xuxiaowei.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
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
			Authentication authentication, @Valid @RequestBody UserInfoDTO userInfo) {

		UserInfoVO userInfoVO = new UserInfoVO();
		BeanUtils.copyProperties(userInfo, userInfoVO);

		Response<UserInfoVO> ok = Response.ok();
		ok.setData(userInfoVO);

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication contextAuthentication = context.getAuthentication();

		String authenticationName = authentication.getName();
		String contextAuthenticationName = contextAuthentication.getName();

		userInfoVO.setName(authenticationName);

		log.info("name: {}", authenticationName);
		log.info("name equals: {}", authenticationName.equals(contextAuthenticationName));

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		log.info(String.valueOf(authorities));

		if (authentication instanceof JwtAuthenticationToken) {
			JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

			Jwt token = jwtAuthenticationToken.getToken();
			Instant issuedAt = token.getIssuedAt();
			Instant expiresAt = token.getExpiresAt();
			Map<String, Object> claims = token.getClaims();

			log.info("Token 创建时间：{}", issuedAt);
			log.info("Token 过期时间：{}", expiresAt);
			log.info("Token 有效负荷：{}", claims);

		}

		String remoteHost = request.getRemoteHost();
		log.info("remoteHost: {}", remoteHost);

		return ok;
	}

}
