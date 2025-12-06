package test;

import org.testng.annotations.Test;

public class BLab10_ExceptionMessage {

	@Test(expectedExceptions = {NullPointerException.class}, expectedExceptionsMessageRegExp = ".*String.length.*")
	public void m2() throws InterruptedException {
		System.out.println("Method m2");
		String str = null;
		str.length();
	}
	
	@Test
	public void m1() {
		System.out.println("Method m1");
	}
	
	@Test
	public void m3() {
		System.out.println("Method m3");
	}
	
}
