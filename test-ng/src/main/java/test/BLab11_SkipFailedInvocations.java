package test;

import org.testng.annotations.Test;

public class BLab11_SkipFailedInvocations {

	@Test(invocationCount = 10, skipFailedInvocations = true)
	public void m2() throws InterruptedException {
		System.out.println("Method m2 "+(10/0));
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
