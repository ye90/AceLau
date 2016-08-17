package jdkdemo;

import static org.junit.Assert.*;

import java.lang.management.ManagementFactory;

import org.junit.Test;

public class SystemDemo {

	/**   
	* @Description :获取本程序PID
	* @Author :刘植业 
	* @CreationTime :2016年8月16日 下午1:49:40  
	* @throws Exception
	*/
	@Test
	public void testName() throws Exception {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println(name.split("@")[0]);
	}
}
