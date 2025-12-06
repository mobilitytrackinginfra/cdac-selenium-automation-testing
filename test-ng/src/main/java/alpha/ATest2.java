package alpha;

import org.testng.annotations.Test;

public class ATest2 {

	@Test(groups = {"alpha","reg"})
	public void m1() {
		System.out.println("ATest2 Method m1");
	}
	
	@Test(groups = {"alpha","fun"})
	public void m2() {
		System.out.println("ATest2 Method m2");
	}
}
