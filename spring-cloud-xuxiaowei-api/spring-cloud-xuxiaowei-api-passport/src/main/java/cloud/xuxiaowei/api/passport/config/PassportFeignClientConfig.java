package cloud.xuxiaowei.api.passport.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;

/**
 * 登录授权服务 {@link FeignClient} 接口 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
@EnableFeignClients("cloud.xuxiaowei.api.passport.openfeign")
public class PassportFeignClientConfig {

}
