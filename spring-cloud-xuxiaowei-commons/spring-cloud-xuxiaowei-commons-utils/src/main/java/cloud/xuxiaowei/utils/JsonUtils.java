package cloud.xuxiaowei.utils;

import cloud.xuxiaowei.utils.exception.CloudRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
public class JsonUtils {

	/**
	 * 递归获取JSON中的特定值，支持点号分隔的键路径
	 * @param str
	 * @param keyPath
	 * @return
	 */
	public static String getValue(String str, String keyPath) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(str);
		return getValue(node, keyPath);
	}

	/**
	 * 递归获取JSON中的特定值，支持点号分隔的键路径
	 * @param node
	 * @param keyPath
	 * @return
	 */
	public static String getValue(JsonNode node, String keyPath) {
		if (node == null || keyPath == null || keyPath.isEmpty()) {
			return null;
		}
		String[] keys = keyPath.split("\\.");
		JsonNode current = node;
		for (String key : keys) {
			current = current.get(key);
			if (current == null) {
				throw new CloudRuntimeException("未找到: " + keyPath);
			}
		}
		return current.asText();
	}

}
