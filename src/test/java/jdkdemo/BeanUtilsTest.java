package jdkdemo;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import excel.Student;

public class BeanUtilsTest {

	private Student stu = new Student();
	private Student stu2 = new Student();

	@Before
	public void init() {
		stu.setId(1);
		stu.setName("ye");
		stu.setClazz(5);
		stu.setCompany("天融信");
		stu.setBirthday(new Date());
		stu2.setName("AceLau");
		stu2.setSex(0);
	}


	@Test
	public void testName() throws Exception {
		BeanUtils.copyProperties(stu, stu2);
	}

	@Test
	public void testName2() throws Exception {
		BeanUtils.copyPropertiesIgnoreNull(stu, stu2);
	}

	@After
	public void print() {
		BeanUtils.printInvoke(stu);
		BeanUtils.printInvoke(stu2);
	}
}
