package test;

import java.time.Duration;

import org.testng.annotations.Test;

public class ALab7_InvocationTimeout {

	@Test(invocationTimeOut = 5000, invocationCount = 3)
	public void m2() throws InterruptedException {
		System.out.println("Method m2");
		Thread.sleep(Duration.ofSeconds(2));
	}
	
	@Test
	public void m1() {
		System.out.println("Method m1");
	}
	
	@Test()
	public void m3() {
		System.out.println("Method m3");
	}
	
}
