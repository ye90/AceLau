package excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/*
 * 使用步骤:
 * 1.新建一个类,例如StudentVO.
 * 2.设置哪些属性需要导出,哪些需要设置提示.
 * 3.设置实体数据
 * 4.调用exportExcel方法.
 * 本例向您提供以下问题的解决方案:
 * 1.实体对象存放的值需要转换为其他文字的情况,例如:实例中有0,1表示男,女;而导入导出的excel中是中文的"男","女".
 * 2.实体对象的时间类型处理.
 * 
 */
public class ExportTest {
	public static void main(String[] args) {
		// 初始化数据
		List<Student> list = new ArrayList<Student>();

		Student stu = new Student();
		stu.setId(1);
		stu.setName("李坤");
		stu.setSex(0);
		stu.setClazz(5);
		stu.setCompany("天融信");
		stu.setBirthday(new Date());
		list.add(stu);

		Student stu2 = new Student();
		stu2.setId(2);
		stu2.setName("曹贵生");
		stu2.setSex(0);
		stu2.setClazz(5);
		stu2.setCompany("中银");
		list.add(stu2);

		Student stu3 = new Student();
		stu3.setId(3);
		stu3.setName("李学宇");
		stu3.setSex(1);
		stu3.setClazz(6);
		list.add(stu3);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream("d:\\AceLau.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ExcelUtil.exportExcel(list, "学生信息", 2222, out);
		System.out.println("----执行完毕----------");
	}

	@Test
	public void testName(HttpServletResponse res) throws Exception {
		// 初始化数据
		List<Student> list = new ArrayList<Student>();

		Student stu = new Student();
		stu.setId(1);
		stu.setName("李坤");
		stu.setSex(0);
		stu.setClazz(5);
		stu.setCompany("天融信");
		stu.setBirthday(new Date());
		list.add(stu);

		Student stu2 = new Student();
		stu2.setId(2);
		stu2.setName("曹贵生");
		stu2.setSex(0);
		stu2.setClazz(5);
		stu2.setCompany("中银");
		list.add(stu2);

		Student stu3 = new Student();
		stu3.setId(3);
		stu3.setName("李学宇");
		stu3.setSex(1);
		stu3.setClazz(6);
		list.add(stu3);

		ExcelUtil.exportExcel(list, "学生信息", 2222, res.getOutputStream());
		System.out.println("----执行完毕----------");
	}

}
