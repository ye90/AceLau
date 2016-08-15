package excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ImportTest {
	public static void main(String[] args) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("d:\\AceLau.xls");
			List<Student> list = ExcelUtil.importExcel(Student.class,"学生信息", fis);
			System.out.println(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
