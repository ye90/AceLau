package singleton;

/**
 * @author ye
 * 简单的方式，在并发执行的时候可能存在多个实例
 *
 */
public class SingletonDemo1 {
	
	private static SingletonDemo1 INSTANCE;

	public SingletonDemo1() {}
	
	public static SingletonDemo1 getInstance(){
		if(INSTANCE==null){
			INSTANCE = new SingletonDemo1();
		}
		return INSTANCE;
	}
	
}
