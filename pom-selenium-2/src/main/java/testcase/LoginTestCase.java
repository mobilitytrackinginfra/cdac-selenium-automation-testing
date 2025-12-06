package testcase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import page.LoginPage;

public class LoginTestCase {

	private LoginPage login;
	public LoginTestCase(WebDriver driver) {
//		login = new LoginPage(driver);
//		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), login);
//		PageFactory.initElements(driver, login);
		
		login = PageFactory.initElements(driver, LoginPage.class);
	}
	
	public void validLogin() {
		login.login("admin", "admin");
	}
}
