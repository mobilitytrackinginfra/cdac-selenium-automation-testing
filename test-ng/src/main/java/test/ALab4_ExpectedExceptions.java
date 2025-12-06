package test;

import org.testng.annotations.Test;

public class ALab4_ExpectedExceptions {

	@Test
	public void m2() {
		System.out.println("Method m2");
	}
	
	@Test(expectedExceptions = {NullPointerException.class, ArrayIndexOutOfBoundsException.class})
	public void m1() {
		System.out.println("Method m1");
		String str = null;
		str.length();
	}
	
	@Test
	public void m3() {
		System.out.println("Method m3");
	}
	
}
