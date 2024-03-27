package cloud.xuxiaowei.validation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
public class UserInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	@NotEmpty(message = "用户名不能为空")
	private String username;

	/**
	 * 性别
	 */
	@NotNull(message = "性别不能为空")
	private String sex;

}
