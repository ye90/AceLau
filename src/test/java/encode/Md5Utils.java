package encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public final class Md5Utils {

	public static String encoder(String input){
		MessageDigest instance = null;
		try {
			instance = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 加密
		byte[] digest = instance.digest(input.getBytes());
		// 十六进制加密
		char[] encodeHex = Hex.encodeHex(digest);
		return new String(encodeHex);
	}
}
