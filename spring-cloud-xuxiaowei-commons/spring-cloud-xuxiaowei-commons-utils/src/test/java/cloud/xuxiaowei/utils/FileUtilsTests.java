package cloud.xuxiaowei.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
class FileUtilsTests {

	@Test
	void checkFileNameNull() {
		Exception tmp = null;
		try {
			FileUtils.checkFileName(null);
		}
		catch (Exception e) {
			tmp = e;
		}
		assertNotNull(tmp);
	}

	@Test
	void checkFileName() {
		List<String> fileNames = Arrays.asList("..", "/", "\\");
		for (String fileName : fileNames) {
			Exception tmp = null;
			try {
				FileUtils.checkFileName(fileName);
			}
			catch (Exception e) {
				tmp = e;
			}
			assertNotNull(tmp);
		}
	}

	@Test
	void checkPathPathNull() {
		Path path = null;
		Exception tmp = null;
		try {
			FileUtils.checkPath(path);
		}
		catch (Exception e) {
			tmp = e;
		}
		assertNotNull(tmp);
	}

	@Test
	void checkPathPath() {
		Path path = Paths.get("/home/../");
		Exception tmp = null;
		try {
			FileUtils.checkPath(path);
		}
		catch (Exception e) {
			tmp = e;
		}
		assertNotNull(tmp);
	}

	@Test
	void checkPathStringNull() {
		String path = null;
		Exception tmp = null;
		try {
			FileUtils.checkPath(path);
		}
		catch (Exception e) {
			tmp = e;
		}
		assertNotNull(tmp);
	}

	@Test
	void checkPath() {
		Exception tmp = null;
		try {
			FileUtils.checkPath("..");
		}
		catch (Exception e) {
			tmp = e;
		}
		assertNotNull(tmp);
	}

}
