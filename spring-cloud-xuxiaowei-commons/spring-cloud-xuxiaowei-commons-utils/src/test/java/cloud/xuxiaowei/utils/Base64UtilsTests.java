package cloud.xuxiaowei.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
class Base64UtilsTests {

	@Test
	void decodeStr() {
		String source = "eyJzdWIiOiJtZXNzYWdpbmctY2xpZW50IiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTcxMTA3NzkxMiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsIm1lc3NhZ2UucmVhZCIsIm1lc3NhZ2Uud3JpdGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo1Mzc4MiIsImV4cCI6MTcxMTA4MTUxMiwiaWF0IjoxNzExMDc3OTEyLCJhdXRob3JpdGllcyI6WyJvcGVuaWQiLCJwcm9maWxlIiwibWVzc2FnZS5yZWFkIiwibWVzc2FnZS53cml0ZSJdfQ";
		String target = "{\"sub\":\"messaging-client\",\"aud\":\"messaging-client\",\"nbf\":1711077912,\"scope\":[\"openid\",\"profile\",\"message.read\",\"message.write\"],\"iss\":\"http://127.0.0.1:53782\",\"exp\":1711081512,\"iat\":1711077912,\"authorities\":[\"openid\",\"profile\",\"message.read\",\"message.write\"]}";
		String decodeStr = Base64Utils.decodeStr(source);
		assertEquals(decodeStr, target);
	}

}
