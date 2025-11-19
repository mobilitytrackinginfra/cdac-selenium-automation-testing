package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab9WebElementDomProperty {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(5));
		
		By locatorUsername = By.id("field-userName");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin");

		By locatorPassword = By.name("password");
		WebElement txtPassword = driver.findElement(locatorPassword);
		txtPassword.sendKeys("admin@CRM");
		Thread.sleep(Duration.ofSeconds(2));

		By locatorLogin = By.tagName("button");
		WebElement btnLogin = driver.findElement(locatorLogin);
		btnLogin.click();
		
		
		Thread.sleep(Duration.ofSeconds(5));

		driver.get("http://localhost/crm/#Account");

		Thread.sleep(Duration.ofSeconds(3));

		System.out.println("Initial State");
		WebElement el = driver.findElement(By.className("select-all"));
		System.out.println("GetAttribute: "+el.getAttribute("checked"));
		System.out.println("GetDOMProperty: "+el.getDomProperty("checked"));

		el.click();
		
		System.out.println("After Click");
		System.out.println("GetAttribute: "+el.getAttribute("checked"));
		System.out.println("GetDOMProperty: "+el.getDomProperty("checked"));

		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
