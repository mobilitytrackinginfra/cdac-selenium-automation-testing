package alpha;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class ATest1 {

	@Test(groups = {"alpha","reg"})
	public void m1() {
		System.out.println("ATest1 Method m1");
	}
	
	@Test(groups = {"alpha","fun"})
	public void m2() {
		System.out.println("ATest1 Method m2");
	}
	
	@BeforeSuite(groups = {"alpha"})
	public void bs1() {
		System.out.println("ATest1 Method BS1");
	}
	
}
