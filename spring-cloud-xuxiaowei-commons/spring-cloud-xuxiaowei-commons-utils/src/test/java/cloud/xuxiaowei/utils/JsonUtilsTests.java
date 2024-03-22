package cloud.xuxiaowei.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
class JsonUtilsTests {

	@Test
	void getValue() throws JsonProcessingException {
		String str = "{\"a\":\"1\",\"b\":\"2\",\"c\":{\"d\":\"13\",\"e\":{\"f\":\"14\"}}}";

		String a = JsonUtils.getValue(str, "a");
		assertEquals(a, "1");

		String b = JsonUtils.getValue(str, "b");
		assertEquals(b, "2");

		String d = JsonUtils.getValue(str, "c.d");
		assertEquals(d, "13");

		String f = JsonUtils.getValue(str, "c.e.f");
		assertEquals(f, "14");

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = objectMapper.readTree(str);
		String tmp1 = JsonUtils.getValue(jsonNode, "c.e.f");
		assertEquals(tmp1, "14");

		jsonNode = null;
		@SuppressWarnings("all")
		String tmp2 = JsonUtils.getValue(jsonNode, "c.e.f");
		assertNull(tmp2);
	}

}
