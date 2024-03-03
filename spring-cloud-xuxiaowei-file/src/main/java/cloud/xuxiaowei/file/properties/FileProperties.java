package cloud.xuxiaowei.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("xuxiaowei.cloud.file")
public class FileProperties {

	private List<ResourceHandler> resourceHandlers;

	/**
	 * @author xuxiaowei
	 * @since 0.0.1
	 */
	@Data
	public static class ResourceHandler {

		private String addResourceHandler;

		private String addResourceLocations;

	}

}
