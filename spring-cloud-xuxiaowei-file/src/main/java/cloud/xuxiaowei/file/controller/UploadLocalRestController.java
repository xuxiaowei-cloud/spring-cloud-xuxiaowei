package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.core.annotation.ControllerAnnotation;
import cloud.xuxiaowei.file.properties.FileProperties;
import cloud.xuxiaowei.file.service.FileService;
import cloud.xuxiaowei.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上传文件：本地储存
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Tag(name = "上传文件：本地储存", description = "主要功能：使用本机硬盘储存文件")
@SecurityRequirement(name = "oauth2_clientCredentials")
@RestController
@RequestMapping("/upload/local")
public class UploadLocalRestController {

	private FileService fileService;

	private FileProperties fileProperties;

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@Autowired
	public void setFileProperties(FileProperties fileProperties) {
		this.fileProperties = fileProperties;
	}

	/**
	 * 上传文件：本地储存
	 * @param request 请求
	 * @param response 响应
	 * @param file 文件
	 * @return 返回上传结果
	 */
	@Operation(summary = "上传文件(本地储存)", description = "将文件上传至服务器，服务器使用本机硬盘保存文件，并通过网络映射提供访问功能")
	@PostMapping
	@ControllerAnnotation("上传文件(本地储存)")
	public Response<String> post(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {

		String localDomainName = fileProperties.getLocalDomainName();
		String localUrlPrefix = fileProperties.getLocalUrlPrefix();
		String localFilePrefix = fileProperties.getLocalFilePrefix();

		String url = fileService.upload(request, response, file, localDomainName, localUrlPrefix, localFilePrefix);

		Response<String> resp = Response.ok();
		resp.setData(url);

		return resp;
	}

}
