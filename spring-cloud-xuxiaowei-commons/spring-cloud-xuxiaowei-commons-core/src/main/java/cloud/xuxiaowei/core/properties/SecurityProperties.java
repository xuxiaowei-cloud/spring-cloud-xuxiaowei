package cloud.xuxiaowei.core.properties;

import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@ConfigurationProperties("xuxiaowei.cloud.security")
public class SecurityProperties {

	private String publicKey;

	private String privateKey;

	public PublicKey publicKey() {
		return new RSA(null, this.publicKey).getPublicKey();
	}

	public RSAPublicKey rsaPublicKey() {
		return (RSAPublicKey) publicKey();
	}

	public PrivateKey privateKey() {
		return new RSA(this.privateKey, null).getPrivateKey();
	}

}
