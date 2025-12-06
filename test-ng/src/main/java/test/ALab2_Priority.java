package test;

import org.testng.annotations.Test;

public class ALab2_Priority {

	@Test(priority = 2)
	public void m2() {
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
