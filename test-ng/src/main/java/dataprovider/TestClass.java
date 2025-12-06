package dataprovider;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestClass {

	@Test(dataProvider = "dp1")
	public void m1(Integer no) {
		if(no%2==0) {
			System.out.println(no+" is even");			
		} else {
			System.out.println(no+" is odd");			
		}
	}
	
	@DataProvider(name = "dp1")
	public Integer[] getData() {
		return new Integer[]{1,2,4,5,6};
	}
	
	
	
	@Test(dataProvider = "dp2", dataProviderClass = DPClass.class)
	public void m2(String str) {
		if(new StringBuilder(str).reverse().toString().equals(str)) {
			System.out.println(str+" is palindrome");			
		} else {
			System.out.println(str+" is not palindrome");			
		}
	}
}
