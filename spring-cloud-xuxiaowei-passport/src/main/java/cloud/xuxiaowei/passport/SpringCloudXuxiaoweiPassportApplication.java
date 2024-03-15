package cloud.xuxiaowei.passport;

import cloud.xuxiaowei.passport.annotation.EnableOAuth2JdbcAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动入口
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@EnableOAuth2JdbcAnnotation
@SpringBootApplication
public class SpringCloudXuxiaoweiPassportApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudXuxiaoweiPassportApplication.class, args);
	}

}
