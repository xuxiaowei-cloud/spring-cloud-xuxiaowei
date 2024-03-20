package cloud.xuxiaowei.passport;

import cloud.xuxiaowei.oauth2.annotation.EnableOAuth2TokenCheck;
import cn.com.xuxiaowei.boot.oauth2.annotation.EnableOAuth2Redis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动入口
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@EnableOAuth2Redis
@EnableOAuth2TokenCheck
@SpringBootApplication
public class SpringCloudXuxiaoweiPassportApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudXuxiaoweiPassportApplication.class, args);
	}

}
