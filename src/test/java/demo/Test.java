package demo;

import java.text.SimpleDateFormat;

public class Test {

	@org.junit.Test
	public void testName() throws Exception {
		String accTime="2008-08-08 11:11:11";
		String cardId="0808";
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat imagePath = new SimpleDateFormat("yyyyMMdd/HH/yyyyMMddHHmmss");
		String imgPath = imagePath.format(yyyyMMddHHmmss.parse(accTime))+ cardId + ".jpg";
		System.out.println(imgPath);
	}
}
