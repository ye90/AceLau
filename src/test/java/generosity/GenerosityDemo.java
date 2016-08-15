package generosity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GenerosityDemo {
	
	private Point<Integer, String> point=null;
	
	@Before
	public void init(){
		point = new Point<Integer, String>();
		point.setX(88);
		point.setY("AceLau");
	}

	/**
	 * TODO 实例化泛型类
	 */
	@Test
	public void GenerosityClass() throws Exception {
		System.out.println("GenerosityClass ：" + point.getX() + ", " + point.getY());
	}
	
	/**
	 * TODO 泛型方法
	 */
	@Test
	public void GenerosityMethod() throws Exception {
		point.printPoint(point.getX(), point.getY());
	}
	
	
	/**
	 * TODO 泛型接口
	 */
	@Test
	public void GenerositInterface() throws Exception {
		InfoImp<String> infoImp = new InfoImp<String>("AceLau");
		System.out.println("Length Of String:" + infoImp.getVar().length());
		
	}
	
}

// 定义泛型类
class Point<T1, T2> {
	T1 x;
	T2 y;

	public T1 getX() {
		return x;
	}

	public void setX(T1 x) {
		this.x = x;
	}

	public T2 getY() {
		return y;
	}

	public void setY(T2 y) {
		this.y = y;
	}

	// 定义泛型方法
	public <T3, T4> void printPoint(T3 x, T4 y) {
		T3 m = x;
		T4 n = y;
		System.out.println("Method ：" + m + ", " + n);
	}

}


//定义泛型接口
interface Info<T> {
  public T getVar();
}

//实现接口
class InfoImp<T> implements Info<T> {
  private T var;

  // 定义泛型构造方法
  public InfoImp(T var) {
      this.setVar(var);
  }

  public void setVar(T var) {
      this.var = var;
  }

  public T getVar() {
      return this.var;
  }
}

