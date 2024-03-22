package cloud.xuxiaowei.utils;

import lombok.SneakyThrows;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
public class RSAUtils {

	private static final String RSA = "RSA";

	public static KeyPair generate() {
		return generate(2048);
	}

	@SneakyThrows
	public static KeyPair generate(int keysize) {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
		keyPairGenerator.initialize(keysize);
		return keyPairGenerator.generateKeyPair();
	}

	public static KeyPair create(String publicKey, String privateKey) {
		return new KeyPair(publicKey(publicKey), privateKey(privateKey));
	}

	public static String publicKey(PublicKey publicKey) {
		return Base64.getEncoder().encodeToString(publicKey.getEncoded());
	}

	@SneakyThrows
	public static PublicKey publicKey(String publicKey) {
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return keyFactory.generatePublic(keySpec);
	}

	public static String privateKey(PrivateKey privateKey) {
		return Base64.getEncoder().encodeToString(privateKey.getEncoded());
	}

	@SneakyThrows
	public static PrivateKey privateKey(String privateKey) {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return keyFactory.generatePrivate(keySpec);
	}

}
