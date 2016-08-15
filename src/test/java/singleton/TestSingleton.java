package singleton;

import org.junit.Test;

/**
 * TODO 单例设计模式
 * @author AceLau
 * 2016年7月21日
 */
public class TestSingleton {
	
	@Test
	public void testName() throws Exception {
		System.out.println(SingletonDemo1.getInstance());
		System.out.println(SingletonDemo1.getInstance());
		System.out.println(SingletonDemo2.INSTANCE);
		System.out.println(SingletonDemo2.INSTANCE);
		System.out.println(SingletonDemo3.getInstance());
		System.out.println(SingletonDemo3.getInstance());
		System.out.println(SingletonDemo4.INSTANCE.getName() );
		System.out.println(SingletonDemo4.INSTANCE);
	}
}
