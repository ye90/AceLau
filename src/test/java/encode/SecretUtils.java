package encode;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class SecretUtils {

	/**
	 * 不推荐用这方法，因为密码不严谨可以为9位
	 * @throws Exception
	 * DES加密、解密（64位破解需要4小时）
	 */
	@Test
	public void DESDemo() throws Exception {
		String input = "AceLau";
		String key = "12345678";// 8个字符的倍数（前面7个字符拿来加密，最后一个拿来校验）8位以后无效，但是可以用

		// 实例化秘钥工厂
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
		// 秘钥规范
		KeySpec keySpec = new DESKeySpec(key.getBytes());
		// 生成秘钥
		SecretKey secretKey = factory.generateSecret(keySpec);

		// 实例化密码器
		Cipher cipher = Cipher.getInstance("DES");

		// 加密：初始化操作模式
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		// 执行操作
		byte[] result = cipher.doFinal(input.getBytes());
		System.out.println("DES加密后的数据：" + new String(result));
		// 上面是危险操作，会出现乱码，导致再转换字节数组时数据会发生变化
		System.out.println("Base64加密后的数据: " + Base64.encodeBase64String(result));

		// 解密：初始化操作模式
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] deCrypt = cipher.doFinal(result);
		System.out.println("DES解密后的数据" + new String(deCrypt));
		System.out.println("Base64解密后的数据: " + Base64.encodeBase64String(deCrypt));

	}
	
	/**
	 * 推荐用：这个方法可以随意切换加密模式
	 * @throws Exception
	 * AES加密解密（128位破解需要10亿年）
	 */
	@Test
	public void AESDemo() throws Exception {
		String input = "AceLau";
		String key = "1234567812345678";// 16个字符的倍数（中文占三个字符）
		
		String algorithm = "AES";//ctrl+1抽取全局变量
		SecretKeySpec spec=new SecretKeySpec(key.getBytes(), algorithm);

		// 实例化密码器
		Cipher cipher = Cipher.getInstance(algorithm);

		// 加密：初始化操作模式
		cipher.init(Cipher.ENCRYPT_MODE, spec);
		// 执行操作
		byte[] result = cipher.doFinal(input.getBytes());
		System.out.println("AES加密后的数据：" + new String(result));
		// 上面是危险操作，会出现乱码，导致再转换字节数组时数据会发生变化
		String encodeBase64String = Base64.encodeBase64String(result);
		System.out.println("Base64加密后的数据: " + encodeBase64String);

		// 解密：初始化操作模式
		cipher.init(Cipher.DECRYPT_MODE, spec);
		byte[] deCrypt = cipher.doFinal(result);
		System.out.println("AES解密后的数据" + new String(deCrypt));
		System.out.println("Base64解密后的数据: " + new String(Base64.decodeBase64(encodeBase64String)));

	}
}
