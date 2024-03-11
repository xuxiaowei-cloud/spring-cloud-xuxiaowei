package cloud.xuxiaowei.file.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public interface FileService {

	/**
	 * 上传文件
	 * @param request 请求
	 * @param response 响应
	 * @param file 文件
	 * @param domainName 上传文件后 域名
	 * @param urlPrefix 上传文件后返回的 URL 前缀，可为空（为空时不处理）
	 * @param filePrefix 本地储存：上传文件后 前缀，不可为空
	 * @return 返回上传结果的 URL
	 */
	String upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String domainName,
			String urlPrefix, String filePrefix);

}
