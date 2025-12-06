package test;

import org.testng.annotations.Test;

public class ALab9_IgnoreMissingDependencies {

	@Test(dependsOnMethods = "mm", ignoreMissingDependencies = true)
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
	
}
