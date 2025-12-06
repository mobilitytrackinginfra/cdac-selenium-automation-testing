package test;

import org.testng.annotations.Test;
import org.testng.annotations.CustomAttribute;

public class BLab12_AlwaysRun {

	@Test(dependsOnMethods = "m1", alwaysRun = true)
	public void m2() throws InterruptedException {
		System.out.println("Method m2");
	}
	
	@Test(attributes = {
			 @CustomAttribute(name = "id", values = {"TC-101"}),
			 @CustomAttribute(name = "severity", values = {"critical"}),
	})
	public void m1() {
		System.out.println("Method m1"+(10/0));
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
