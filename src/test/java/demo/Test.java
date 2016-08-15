package demo;

import static org.junit.Assert.*;

public class Test {

	@org.junit.Test
	public void testName() throws Exception {
		String[] array={
				"CARDMESS","CARENTER","CHARGE","CARENTER","CHARGE",
				"CARENTER","FAVTYPE","EVENTS","CARDRENEWAL","",
				"CHARGE","FAVTYPE","CPRMORECARNO","FAVTYPE","AREARECORD","PREPAYCARDFEE","DEPOSITMONEYRECORD"};
		System.out.println(array[0]);
		System.out.println(array[17]);
	}
}
