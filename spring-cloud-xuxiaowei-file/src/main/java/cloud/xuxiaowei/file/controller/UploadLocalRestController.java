package cloud.xuxiaowei.file.controller;

import cloud.xuxiaowei.file.properties.FileProperties;
import cloud.xuxiaowei.file.service.FileService;
import cloud.xuxiaowei.utils.Response;
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
	@PostMapping
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
