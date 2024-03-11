package cloud.xuxiaowei.file.config;

import cloud.xuxiaowei.file.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerResourceResolver;
import org.springdoc.webmvc.ui.SwaggerWebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springdoc.core.utils.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * {@link WebMvcConfigurationSupport} 优先级比 {@link WebMvcConfigurer} 高
 * <p>
 * 使用了 {@link WebMvcConfigurationSupport} 之后，{@link WebMvcConfigurer} 会失效
 *
 * @see SwaggerWebMvcConfigurer
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class WebMvcConfigurationSupportConfig extends WebMvcConfigurationSupport {

	private SwaggerUiConfigParameters swaggerUiConfigParameters;

	private SwaggerIndexTransformer swaggerIndexTransformer;

	private Optional<ActuatorProvider> actuatorProvider;

	private SwaggerResourceResolver swaggerResourceResolver;

	private FileProperties fileProperties;

	@Autowired
	public void setSwaggerUiConfigParameters(SwaggerUiConfigParameters swaggerUiConfigParameters) {
		this.swaggerUiConfigParameters = swaggerUiConfigParameters;
	}

	@Autowired
	public void setSwaggerIndexTransformer(SwaggerIndexTransformer swaggerIndexTransformer) {
		this.swaggerIndexTransformer = swaggerIndexTransformer;
	}

	@Autowired
	public void setActuatorProvider(Optional<ActuatorProvider> actuatorProvider) {
		this.actuatorProvider = actuatorProvider;
	}

	@Autowired
	public void setSwaggerResourceResolver(SwaggerResourceResolver swaggerResourceResolver) {
		this.swaggerResourceResolver = swaggerResourceResolver;
	}

	@Autowired
	public void setFileProperties(FileProperties fileProperties) {
		this.fileProperties = fileProperties;
	}

	@Override
	protected void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

		swaggerWebMvcConfigurer(registry);

		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

		List<FileProperties.ResourceHandler> resourceHandlers = fileProperties.getResourceHandlers();
		String localFilePrefix = fileProperties.getLocalFilePrefix();
		String localUrlPrefix = fileProperties.getLocalUrlPrefix();

		ArrayList<FileProperties.ResourceHandler> resourceHandlerList = new ArrayList<>(resourceHandlers);
		if (StringUtils.hasText(localFilePrefix) && StringUtils.hasText(localUrlPrefix)) {
			FileProperties.ResourceHandler resourceHandler = new FileProperties.ResourceHandler();
			resourceHandler.setAddResourceHandler(localUrlPrefix);
			resourceHandler.setAddResourceLocations(localFilePrefix);
			resourceHandlerList.add(resourceHandler);
		}

		if (resourceHandlers.isEmpty()) {
			log.warn("本地静态资源配置为空");
		}
		else {
			for (FileProperties.ResourceHandler resourceHandler : resourceHandlerList) {
				String addResourceHandler = resourceHandler.getAddResourceHandler();
				String addResourceLocations = resourceHandler.getAddResourceLocations();

				if (!addResourceHandler.endsWith("/**")) {
					addResourceHandler = UriComponentsBuilder.newInstance()
						.path(addResourceHandler)
						.path("/**")
						.toUriString();
				}

				if (!addResourceLocations.startsWith("file:")) {
					addResourceLocations = "file:" + addResourceLocations;
				}

				if (!addResourceLocations.endsWith("/")) {
					addResourceLocations += "/";
				}

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

	/**
	 * @see SwaggerWebMvcConfigurer
	 */
	private void swaggerWebMvcConfigurer(ResourceHandlerRegistry registry) {
		String swaggerPath = swaggerUiConfigParameters.getPath();

		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains(DEFAULT_PATH_SEPARATOR))
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		if (actuatorProvider.isPresent() && actuatorProvider.get().isUseManagementPort())
			uiRootPath.append(actuatorProvider.get().getBasePath());

		registry.addResourceHandler(uiRootPath + SWAGGER_UI_PREFIX + "*/*" + SWAGGER_INITIALIZER_JS)
			.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
			.setCachePeriod(0)
			.resourceChain(false)
			.addResolver(swaggerResourceResolver)
			.addTransformer(swaggerIndexTransformer);

		registry.addResourceHandler(uiRootPath + SWAGGER_UI_PREFIX + "*/**")
			.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
			.resourceChain(false)
			.addResolver(swaggerResourceResolver)
			.addTransformer(swaggerIndexTransformer);
	}

}
