package demo;

import static org.junit.Assert.*;

public class Test {

	static{
		System.out.println("static first!");
	}
	
	static{
		System.out.println("static second");
	}
	
	public static void main(String[] args) {
		System.out.println("main");
	}
}
