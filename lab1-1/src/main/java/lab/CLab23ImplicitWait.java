package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CLab23ImplicitWait {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		
		System.out.println("Before Set: "+driver.manage().timeouts().getImplicitWaitTimeout().getSeconds());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		Duration timeout = driver.manage().timeouts().getImplicitWaitTimeout();
		System.out.println("After Set: "+timeout.getSeconds());
		
		By locatorUsername = By.id("field-userName1");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin");
		By locatorPassword = By.name("password");
		WebElement txtPassword = driver.findElement(locatorPassword);
		txtPassword.sendKeys("admin@CRM");
		By locatorLogin = By.tagName("button");
		WebElement btnLogin = driver.findElement(locatorLogin);
		btnLogin.click();
		
		Thread.sleep(Duration.ofSeconds(3));
		driver.quit();

	}
}
