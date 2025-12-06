package testcase;

import org.openqa.selenium.WebDriver;

import page.LoginPage;

public class LoginTestCase {

	private LoginPage login;
	public LoginTestCase(WebDriver driver) {
		login = new LoginPage(driver);
	}
	
	public void validLogin() {
		login.login("admin", "admin");
	}
	
	public boolean loginWithCredentials(String username, String passwrod) {
		return login.login(username, passwrod);
	}
	
	public void logout() {
		login.logout();
	}
}
