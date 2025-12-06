package beta;

import org.testng.annotations.Test;

public class BTest2 {

	@Test(groups = {"beta","smoke"})
	public void m1() {
		System.out.println("BTest2 Method m1");
	}
	
	@Test(groups = {"beta","acceptance"})
	public void m2() {
		System.out.println("BTest2 Method m2");
	}
}
