package cloud.xuxiaowei.file.config;

import cloud.xuxiaowei.file.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

/**
 * {@link WebMvcConfigurationSupport} 优先级比 {@link WebMvcConfigurer} 高
 * <p>
 * 使用了 {@link WebMvcConfigurationSupport} 之后，{@link WebMvcConfigurer} 会失效
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class WebMvcConfigurationSupportConfig extends WebMvcConfigurationSupport {

	private FileProperties fileProperties;

	@Autowired
	public void setFileProperties(FileProperties fileProperties) {
		this.fileProperties = fileProperties;
	}

	@Override
	protected void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

		List<FileProperties.ResourceHandler> resourceHandlers = fileProperties.getResourceHandlers();

		if (resourceHandlers == null || resourceHandlers.isEmpty()) {
			log.warn("本地静态资源配置为空");
		}
		else {
			for (FileProperties.ResourceHandler resourceHandler : resourceHandlers) {
				String addResourceHandler = resourceHandler.getAddResourceHandler();
				String addResourceLocations = resourceHandler.getAddResourceLocations();

				log.info("网路路径: {} 映射到本地路径: {}", addResourceHandler, addResourceLocations);

				File directory = new File(addResourceLocations.replaceFirst("file:", ""));
				if (directory.exists()) {
					log.info("文件夹: {} 已存在", addResourceLocations);
				}
				else {
					boolean mkdirs = directory.mkdirs();
					log.info("文件夹: {} 创建结果: {}", addResourceLocations, mkdirs);
				}

				registry.addResourceHandler(addResourceHandler).addResourceLocations(addResourceLocations);
			}

		}

	}

}
