package cloud.xuxiaowei.passport;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringCloudXuxiaoweiAdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudXuxiaoweiAdminServerApplication.class, args);
	}

}
