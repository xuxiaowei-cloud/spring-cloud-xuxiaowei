package cloud.xuxiaowei.passport.config;

import cloud.xuxiaowei.utils.constant.OAuth2Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义 JWT 编码器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class OAuth2TokenCustomizerConfig implements OAuth2TokenCustomizer<JwtEncodingContext> {

	@Override
	public void customize(JwtEncodingContext context) {

		// OAuth2 Token 类型
		OAuth2TokenType tokenType = context.getTokenType();

		// JWT 构建器
		JwtClaimsSet.Builder claims = context.getClaims();

		// 用户认证
		Authentication principal = context.getPrincipal();

		// 客户权限
		Set<String> authorizedScopes = context.getAuthorizedScopes();

		if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {

			Set<String> authorities = principal.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

			// 合并权限、范围
			authorities.addAll(authorizedScopes);

			// 将合并权限放入 JWT 中
			claims.claim(OAuth2Constants.AUTHORITIES, authorities);

		}
	}

}
