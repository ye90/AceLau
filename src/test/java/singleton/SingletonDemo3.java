package singleton;

/**
 * @author ye
 * 工厂化方式，可以在实例过程中添加逻辑
 */
public class SingletonDemo3 {

	public static final SingletonDemo3 INSTANCE =new SingletonDemo3();

	public SingletonDemo3() {}
	
	public static SingletonDemo3 getInstance(){
		return INSTANCE;
	}
	
}
