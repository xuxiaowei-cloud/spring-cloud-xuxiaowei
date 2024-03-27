package cloud.xuxiaowei.utils;

import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件工具类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
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

		if (path.contains("..")) {
			CloudRuntimeException throwable = new CloudRuntimeException("路径异常");
			throwable.setReferences(references);
			throw throwable;
		}
	}

	/**
	 * 读取文件
	 * @param filePath 文件路径
	 * @return 返回 文件内容
	 * @throws IOException 读取文件异常
	 */
	public static String readText(String filePath) throws IOException {
		return readText(new File(filePath));
	}

	/**
	 * 读取文件
	 * @param file 文件
	 * @return 返回 文件内容
	 * @throws IOException 读取文件异常
	 */
	public static String readText(File file) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		// 创建一个新FileReader，给定从中读取文件 。
		try (FileReader fileReader = new FileReader(file);
				// 创建一个使用默认大小输入缓冲器的缓冲字符输入流。
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {

			String readLine;
			// 读取文本行。 的线被认为是由一个进料线中的任何一个被终止（“\n”），回车（“\r”），或回车立即由换行遵循。
			while ((readLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(System.lineSeparator()).append(readLine);
			}

		}
		catch (IOException e) {
			log.error("读取文件异常：", e);
			throw e;
		}
		return stringBuilder.toString();
	}

	/**
	 * 读取文件
	 * @param filePath 文件路径
	 * @return 返回 文件内容
	 * @throws IOException 读取文件异常
	 */
	public static List<String> readList(String filePath) throws IOException {
		return readList(new File(filePath));
	}

	/**
	 * 读取文件
	 * @param file 文件
	 * @return 返回 文件内容
	 * @throws IOException 读取文件异常
	 */
	public static List<String> readList(File file) throws IOException {
		List<String> list = new ArrayList<>();
		// 创建一个新FileReader，给定从中读取文件 。
		try (FileReader fileReader = new FileReader(file);

				// 创建一个使用默认大小输入缓冲器的缓冲字符输入流。
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {

			String readLine;
			// 读取文本行。 的线被认为是由一个进料线中的任何一个被终止（“\n”），回车（“\r”），或回车立即由换行遵循。
			while ((readLine = bufferedReader.readLine()) != null) {
				list.add(readLine);
			}

		}
		catch (IOException e) {
			log.error("读取文件异常：", e);
			throw e;
		}
		return list;
	}

}
