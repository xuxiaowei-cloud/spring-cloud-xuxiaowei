package cloud.xuxiaowei.passport.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
public class UserInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	private String username;

}
