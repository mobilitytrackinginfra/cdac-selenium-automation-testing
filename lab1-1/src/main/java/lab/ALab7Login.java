package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class ALab7Login {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(5));
		
		By locatorUsername = By.id("field-userName");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin123");
		Thread.sleep(Duration.ofSeconds(2));
		txtUsername.sendKeys("admin");
		Thread.sleep(Duration.ofSeconds(2));
		txtUsername.clear();
		Thread.sleep(Duration.ofSeconds(2));
		txtUsername.sendKeys("admin");
		System.out.println("Text Entered in txtUsername is "+txtUsername.getText());
		System.out.println("Text Entered in txtUsername is "+txtUsername.getAttribute("value"));

		By locatorPassword = By.name("password");
		WebElement txtPassword = driver.findElement(locatorPassword);
		txtPassword.sendKeys("admin@CRM");
		Thread.sleep(Duration.ofSeconds(2));

		driver.findElement(By.className("fa-eye")).click();
		Thread.sleep(Duration.ofSeconds(2));

		By locatorLogin = By.tagName("button");
		WebElement btnLogin = driver.findElement(locatorLogin);
		btnLogin.click();
		
		
		Thread.sleep(Duration.ofSeconds(10));
		driver.quit();

	}
}
