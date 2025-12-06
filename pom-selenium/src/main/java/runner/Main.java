package runner;

import java.util.List;

import org.openqa.selenium.WebDriver;

import drivermanager.DriverFactory;
import testcase.LoginTestCase;
import utils.CSVReader;
import utils.User;

public class Main {

	public static void main(String[] args) throws Exception {
		WebDriver driver = DriverFactory.initDriver("chrome");
		
		LoginTestCase tc1 = new LoginTestCase(driver);
//		tc1.validLogin();

		List<User> users = CSVReader.getLoginUsers();
		for(User usr : users) {
			boolean isLogin = tc1.loginWithCredentials(usr.getUsername(), usr.getPassword());
			if(isLogin)
				tc1.logout();
		}
		Thread.sleep(5000);
		driver.quit();
	}
}
