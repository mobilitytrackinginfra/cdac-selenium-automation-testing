package test;

import org.testng.annotations.Test;

public class ALab3_Enabled {

	@Test
	public void m2() {
		System.out.println("Method m2");
	}
	
	@Test
	public void m1() {
		System.out.println("Method m1");
	}
	
	@Test(enabled = false)
	public void m3() {
		System.out.println("Method m3");
	}
	
}
