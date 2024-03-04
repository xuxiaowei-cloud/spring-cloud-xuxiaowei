package cloud.xuxiaowei.file.service.impl;

import cloud.xuxiaowei.file.service.FileService;
import cn.com.xuxiaowei.utils.unit.DataSize;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 文件接口：本地储存实现
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Service
public class LocalFileService implements FileService {

	/**
	 * 上传文件到本地储存
	 * @param request 请求
	 * @param response 响应
	 * @param file 文件
	 * @param domainName 上传文件后 域名
	 * @param urlPrefix 上传文件后返回的 URL 前缀，可为空（为空时不处理）
	 * @param filePrefix 本地储存：上传文件后 本地储存 前缀，不可为空
	 * @return 返回上传结果的 URL
	 */
	@Override
	public String upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file,

			String domainName, String urlPrefix, @NonNull String filePrefix) {

		if (file == null) {
			throw new RuntimeException("上传文件不能为空");
		}

		String contentType = file.getContentType();
		String originalFileName = file.getOriginalFilename();

		if (originalFileName == null) {
			throw new RuntimeException("文件后缀名不能为空");
		}

		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

		long size = file.getSize();
		DataSize dataSize = DataSize.ofBytes(size);
		String longUnit = dataSize.toStringLongUnit();

		log.info("文件类型: {}, 文件名: {}, 文件后缀名: {}, 大小: {}({})", contentType, originalFileName, fileExtension, size,
				longUnit);

		String now = LocalDate.now().toString();

		String parent = filePrefix + now;
		String fileName = UUID.randomUUID() + fileExtension;
		String filePath = parent + File.separator + fileName;

		log.info("上传文件路径: {}", filePath);

		File folder = new File(parent);

		if (!folder.exists()) {
			boolean mkdirs = folder.mkdirs();
			log.info("文件夹: {} 创建结果: {}", parent, mkdirs);
		}

		Path target = Paths.get(parent, fileName);
		try {
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			throw new RuntimeException(String.format("本地储存文件: %s 异常：", filePath), e);
		}

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		if (domainName == null) {
			builder.scheme(request.getScheme())
				.host(request.getServerName())
				.port(request.getServerPort())
				.path(request.getContextPath());
		}
		else {
			try {
				builder = UriComponentsBuilder.newInstance().uri(new URI(domainName));
			}
			catch (URISyntaxException e) {
				throw new RuntimeException("上传文件配置的域名不合法", e);
			}
		}

		String uri = builder.path("/").path(urlPrefix).path("/").path(now).path("/").path(fileName).toUriString();

		log.info("文件上传的结果: {}", uri);

		return uri;
	}

}
