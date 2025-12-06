package parameters;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestClass {

	@Test
	@Parameters("name")
	public void m1(String name) {
		System.out.println("ATest1 Method m1 "+name);
	}
	
	@Test
	@Parameters({"name","department"})
	public void m2(String str1, String str2) {
		System.out.println("ATest1 Method m2:"+str1+", "+str2);
	}
	
	@Test
	@Parameters({"city"})
	public void m3(@Optional("Pune") String city) {
		System.out.println("ATest1 Method m2:"+city);
	}
}
