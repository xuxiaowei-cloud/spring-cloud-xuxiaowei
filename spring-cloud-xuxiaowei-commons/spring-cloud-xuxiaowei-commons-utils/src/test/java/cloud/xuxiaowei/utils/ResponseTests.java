package cloud.xuxiaowei.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
class ResponseTests {

	@Test
	void start() {
		Response<?> response = new Response<>();
		assertFalse(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNotNull(response.getRequestId());

		Object data = response.getData();
		assertNull(data);

		String dataStr = "徐晓伟";

		response = new Response<>(dataStr);
		assertFalse(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNotNull(response.getRequestId());

		data = response.getData();
		assertEquals(dataStr, data);
	}

	@Test
	void string() {
		Response<String> response = new Response<>();
		assertFalse(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNotNull(response.getRequestId());

		String data = response.getData();
		assertNull(data);
	}

	@Test
	void ok() {
		Response<?> response = Response.ok();
		assertTrue(response.isSuccess());
		assertNull(response.getCode());
		assertNull(response.getMessage());
		assertNotNull(response.getRequestId());

		Object data = response.getData();
		assertNull(data);

		String message = "添加成功";

		response = Response.ok(message);
		assertTrue(response.isSuccess());
		assertNull(response.getCode());
		assertEquals(message, response.getMessage());
		assertNotNull(response.getRequestId());

		data = response.getData();
		assertNull(data);
	}

	@Test
	void error() {
		Response<?> response = Response.error();
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertEquals("系统异常", response.getMessage());
		assertNotNull(response.getRequestId());

		Object data = response.getData();
		assertNull(data);

		String message = "添加失败";

		response = Response.error(message);
		assertFalse(response.isSuccess());
		assertEquals("500", response.getCode());
		assertEquals(message, response.getMessage());
		assertNotNull(response.getRequestId());

		data = response.getData();
		assertNull(data);

		String code = "503";

		response = Response.error(code, message);
		assertFalse(response.isSuccess());
		assertEquals(code, response.getCode());
		assertEquals(message, response.getMessage());
		assertNotNull(response.getRequestId());

		data = response.getData();
		assertNull(data);
	}

}
