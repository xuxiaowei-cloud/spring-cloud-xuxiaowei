package cloud.xuxiaowei.utils;

import cloud.xuxiaowei.utils.exception.CloudRuntimeException;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * 文件工具类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class FileUtils {

	/**
	 * 检查文件名是否合法
	 * @param fileName 文件名
	 */
	public static void checkFileName(String fileName) {
		if (fileName == null) {
			throw new CloudRuntimeException("路径为空");
		}
		// @formatter:off
		List<String> references = Arrays.asList(
				"https://cwe.mitre.org/data/definitions/22.html",
				"https://cwe.mitre.org/data/definitions/23.html",
				"https://cwe.mitre.org/data/definitions/36.html",
				"https://cwe.mitre.org/data/definitions/73.html");
		// @formatter:on

		if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
			CloudRuntimeException throwable = new CloudRuntimeException("路径异常");
			throwable.setReferences(references);
			throw throwable;
		}
	}

	/**
	 * 检查路径是否合法
	 * @param path 路径
	 */
	public static void checkPath(Path path) {
		if (path == null) {
			throw new CloudRuntimeException("路径为空");
		}
		checkPath(path.toString());
	}

	/**
	 * 检查路径是否合法
	 * @param path 路径
	 */
	public static void checkPath(String path) {
		if (path == null) {
			throw new CloudRuntimeException("路径为空");
		}
		// @formatter:off
		List<String> references = Arrays.asList(
				"https://cwe.mitre.org/data/definitions/22.html",
				"https://cwe.mitre.org/data/definitions/23.html",
				"https://cwe.mitre.org/data/definitions/36.html",
				"https://cwe.mitre.org/data/definitions/73.html");
		// @formatter:on

		if (path.toString().contains("..")) {
			CloudRuntimeException throwable = new CloudRuntimeException("路径异常");
			throwable.setReferences(references);
			throw throwable;
		}
	}

}
