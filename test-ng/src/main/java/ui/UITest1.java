package ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UITest1 {

	@Test
	public void m1() {
		System.out.println("UITest1 Method m1");
	}
	
	@Test
	public void m2() {
		System.out.println("UITest1 Method m2");
	}

	@BeforeSuite
	public void bs1() {
		System.out.println("UITest1 BS1");
	}
	
	@BeforeTest
	public void bt1() {
		System.out.println("UITest1 BT1");
	}
	
	@BeforeClass
	public void bc1() {
		System.out.println("UITest1 BC1");
	}
	
	@BeforeMethod
	public void bm1() {
		System.out.println("UITest1 BM1");
	}
	
	@AfterMethod
	public void am1() {
		System.out.println("UITest1 AM1");
	}
	
	@AfterClass
	public void ac1() {
		System.out.println("UITest1 AC1");
	}
	
	@AfterTest
	public void at1() {
		System.out.println("UITest1 AT1");
	}
	
	@AfterSuite
	public void as1() {
		System.out.println("UITest1 AS1");
	}

}
