package cloud.xuxiaowei.utils;

import java.util.Base64;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
public class Base64Utils {

	public static byte[] decode(String src) {
		return Base64.getDecoder().decode(src);
	}

	public static String decodeStr(String src) {
		return new String(decode(src));
	}

}
