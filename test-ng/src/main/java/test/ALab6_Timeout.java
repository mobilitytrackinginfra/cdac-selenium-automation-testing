package test;

import java.time.Duration;

import org.testng.annotations.Test;

public class ALab6_Timeout {

	@Test(timeOut = 1000)
	public void m2() throws InterruptedException {
		System.out.println("Method m2");
		Thread.sleep(Duration.ofSeconds(5));
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
