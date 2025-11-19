package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab10WebElementFindElement {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.findElement(By.id("field-userName")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin@CRM");
		driver.findElement(By.tagName("button")).click();
		Thread.sleep(Duration.ofSeconds(5));
		
		WebElement header = driver.findElement(By.className("navbar-header"));
		WebElement logo = header.findElement(By.className("navbar-logo-container"));
		WebElement link =  logo.findElement(By.tagName("a"));
		link.click();
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
