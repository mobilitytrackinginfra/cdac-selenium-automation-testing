package test;

import org.testng.annotations.Test;

public class ALab8_DependsOnMethods {

	@Test(dependsOnMethods = "m1")
	public void m2() throws InterruptedException {
		System.out.println("Method m2");
	}
	
	@Test
	public void m1() {
		System.out.println("Method m1");
	}
	
	@Test
	public void m3() {
		System.out.println("Method m3");
	}

	@Test
	public void m4() {
		System.out.println("Method m4");
	}
}
