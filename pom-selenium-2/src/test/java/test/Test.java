package test;

import org.testng.annotations.BeforeSuite;

import drivermanager.DriverFactory;
import testcase.LoginTestCase;

public class Test {

	@BeforeSuite
	public void initPage() {
		DriverFactory.initDriver("chrome");
	}
	
	@org.testng.annotations.Test
	public void loginTest() {
		LoginTestCase tc1 = new LoginTestCase(DriverFactory.getDriver());
		tc1.validLogin();
	}
}
