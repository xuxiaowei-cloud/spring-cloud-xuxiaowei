package cloud.xuxiaowei.utils;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	void readText() throws IOException {
		String filePath = Paths.get("LICENSE").toAbsolutePath().toString();
		String text = FileUtils.readText(filePath);
		int length = text.length();
		// Linux：11357
		// Windows：11558
		assertTrue(Arrays.asList(11558, 11357).contains(length));
	}

	@Test
	void readTextError() {
		String filePath = Paths.get(UUID.randomUUID().toString()).toAbsolutePath().toString();
		String text = null;
		try {
			text = FileUtils.readText(filePath);
		}
		catch (Exception e) {
			assertInstanceOf(FileNotFoundException.class, e);
		}
		assertNull(text);
	}

	@Test
	void readList() throws IOException {
		String filePath = Paths.get("LICENSE").toAbsolutePath().toString();
		List<String> list = FileUtils.readList(filePath);
		int size = list.size();
		assertEquals(201, size);
	}

	@Test
	void readListError() {
		String filePath = Paths.get(UUID.randomUUID().toString()).toAbsolutePath().toString();
		List<String> list = null;
		try {
			list = FileUtils.readList(filePath);
		}
		catch (Exception e) {
			assertInstanceOf(FileNotFoundException.class, e);
		}
		assertNull(list);
	}

}
