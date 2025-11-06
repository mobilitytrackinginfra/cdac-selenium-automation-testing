package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lab7Login {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/suitecrm/public/");
		Thread.sleep(Duration.ofSeconds(5));
		
		By locatorUsername = By.name("username");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin");

		By locatorPassword = By.name("password");
		WebElement txtPassword = driver.findElement(locatorPassword);
		txtPassword.sendKeys("admin");

		By locatorLogin = By.id("login-button");
		WebElement btnLogin = driver.findElement(locatorLogin);
		btnLogin.click();

		
		
		
		Thread.sleep(Duration.ofSeconds(30));
		driver.quit();

	}
}
