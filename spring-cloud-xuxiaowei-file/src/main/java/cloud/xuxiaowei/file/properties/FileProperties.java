package cloud.xuxiaowei.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("xuxiaowei.cloud.file")
public class FileProperties {

	/**
	 * 本地储存：静态资源映射
	 */
	private List<ResourceHandler> resourceHandlers;

	/**
	 * 本地储存：上传文件后 域名
	 */
	private String localDomainName;

	/**
	 * 本地储存：上传文件后 返回的URL 前缀
	 */
	private String localUrlPrefix;

	/**
	 * 本地储存：上传文件后 本地储存 前缀，不能为空
	 */
	private String localFilePrefix;

	/**
	 * 本地储存：静态资源映射
	 *
	 * @author xuxiaowei
	 * @since 0.0.1
	 */
	@Data
	public static class ResourceHandler {

		/**
		 * 网路路径
		 */
		private String addResourceHandler;

		/**
		 * 本地路径
		 */
		private String addResourceLocations;

	}

}
