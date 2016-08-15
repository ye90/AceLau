package jdkdemo;

import org.junit.Test;


/**
 * TODO 实现多线程（模版设计模式）
 * @author AceLau
 * 2016年7月21日
 */
public class ThreadDemo implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			// 模版设Code
			System.out.println(Thread.currentThread().getName() + "--" + i);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new ThreadDemo());
		thread.start();
//		thread.join();//执行完一个线程后，再执行下一个
		new Thread(new ThreadDemo()).start();;
	}
	
	/**
	 * TODO junit测不了效果
	 */
	@Test
	public void testName() throws Exception {
		Thread thread = new Thread(new ThreadDemo());
		thread.start();
//		thread.join();//执行完一个线程后，再执行下一个
		new Thread(new ThreadDemo()).start();;
	}
}
