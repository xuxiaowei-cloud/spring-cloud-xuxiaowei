package cloud.xuxiaowei.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuxiaowei
 * @since 0.0.1
 */
class RSAUtilsTests {

	@Test
	void generate() {
		KeyPair keyPair = RSAUtils.generate();
		keyPair(keyPair);
	}

	@Test
	void keysize() {
		List<Integer> keysizes = Arrays.asList(512, 1024, 2048, 3072, 4096);
		for (Integer keysize : keysizes) {
			KeyPair keyPair = RSAUtils.generate(keysize);
			keyPair(keyPair);
		}
	}

	@Test
	void create() {
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvUEZbnpoYCSVveN8/h3ouADkX1l5/qpS/vaVuWMOMpVbWqiVysrL7x8NV0i0NKBf+Ufa0aJBMYtsRGTsKOGv4ulwoUCMDizZ47xCUVMR6JCbm3qVejeK5GWPatrRFPWXwIL5G4nk4ZkpEhFUn0qeJwEPG70QNxZDNJVIqbDK0CROZifd/7REy9SQjIsQbVjUmC2J09IXE4FK3YDIltUOJLf7ASnkIb5al/IelKLIkoYaiI4Jjw6/zK2QXwNaO74FSOIbxDM/yixrkuArtsrqLbxjw/BMlW5pLguTfbeXobbmS+t1MgIfCmWye5GXCsuuS+K/RElLmvBtT+o1xZNBowIDAQAB";
		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9QRluemhgJJW943z+Hei4AORfWXn+qlL+9pW5Yw4ylVtaqJXKysvvHw1XSLQ0oF/5R9rRokExi2xEZOwo4a/i6XChQIwOLNnjvEJRUxHokJubepV6N4rkZY9q2tEU9ZfAgvkbieThmSkSEVSfSp4nAQ8bvRA3FkM0lUipsMrQJE5mJ93/tETL1JCMixBtWNSYLYnT0hcTgUrdgMiW1Q4kt/sBKeQhvlqX8h6UosiShhqIjgmPDr/MrZBfA1o7vgVI4hvEMz/KLGuS4Cu2yuotvGPD8EyVbmkuC5N9t5ehtuZL63UyAh8KZbJ7kZcKy65L4r9ESUua8G1P6jXFk0GjAgMBAAECggEACYT7Qv/cPS3J7G3DbULS4GKHGja1eLZXi2Sa3mLZpXi2yiYdtsAhpusAcEoDanxAp5jSLGZFcjkJnp0W9e14O+kzFaFaCiJ+aUKgcVfE2FZhxV3So/2kOl3Ta3QLPUsnMXNFFXoHrcNW+gYuI/8MWnjmqiYF6Udw5PW3XHW8leCPSTL+XREhXvr42WbUdVJx5D0T1Kzxby3RhyM+sOfgnj0ItXeqcAnci7x/KyO7uN3lBYmn7uVluYdh08oB1sJ1/+FZy6CAmW3gR11HhpdSgJcdxmcK53Viv0LAM6zCR3o6YJePD/BB7vDS79OXNJwOX78iQ88demZLQWl8c7KOmQKBgQC//OuOnc9RjRaYqsrr2zPqDNpgw6lS3zSpao/oc1Q0asTH/QGFY/8E9ZYakMotultk2SzwaDUDk0MBxQlIMJUsz1pQHG1JvjESQ33wFyjUW1EGHp9d5EgdQ+QFB3Pnr1M+rSOT8lkMjkvkNsy1TADUSRpDXTQHtmVD91ecvxsR+wKBgQD8WtjdLnJBAtdMTlduh0eS7n8l+dmfz94lWs6QX0GEuHZyExPqHyVyyYje4kQ/8BOThVBJmzwAkbSJnjjY3dKf/aN3jagzUwp53nbcE0cj7Hbokx6WC49aHV7GOe1VxHQ1DLX3uxytICIzy4D3oRtOg2xXfEYNEKHdTXzTQS2meQKBgF9c2QChIH6Ij0XL6DmZS0MhmUNhTPTuC/FvDgZPau8SbaZM5PmF/wFOn32Yl+R44bbJLUpI6LRa7Hivj3m9MAvu/xQ12o87mpJQ1fhw+IXqyec5RL3Vi4CF3XgmCIt3iejjBX4SbesPr2CvW6VeGDdaDGKTpCmEju5gqLMiNecLAoGBAIBJHp2yEtsBurlLyh6iHryPCYwgq4UWgvOyaffytLfnni73i9Qk6UClOzMzC0cFoK4Jw3rns2UGrSSYjoWQnpY+tm78UFlnCZhSg1H1auDUQM6Sm6XjUoiZDajvobtMF1cwkwDQ2g8YmsqB1ZoNCHitTtJalrHllqON1LsAE1jhAoGAUNxiVF4GUu4DrW7Dpbdpocme1b5oj9jDaYXnkRBFYnlrX9vNGy2ctYEWHEIW7LFy374aGVgXFikkRjVxehCuoO+qt2wN/fQScFnQvMOjdRAnpInvCjoi5NfIBtKAU/Co7TgR6ogAFGh2NvTZYUmj9JoQ1Pd6v+1Qrm9Zsqz+vRE=";
		KeyPair keyPair = RSAUtils.create(publicKey, privateKey);
		keyPair(keyPair);
	}

	void keyPair(KeyPair keyPair) {
		PublicKey getPublic = keyPair.getPublic();
		PrivateKey getPrivate = keyPair.getPrivate();

		String publicKeyStr = RSAUtils.publicKey(getPublic);
		String privateKeyStr = RSAUtils.privateKey(getPrivate);

		PublicKey publicKey = RSAUtils.publicKey(publicKeyStr);
		PrivateKey privateKey = RSAUtils.privateKey(privateKeyStr);

		assertEquals(new String(getPublic.getEncoded()), new String(publicKey.getEncoded()));
		assertEquals(new String(getPrivate.getEncoded()), new String(privateKey.getEncoded()));
	}

}
