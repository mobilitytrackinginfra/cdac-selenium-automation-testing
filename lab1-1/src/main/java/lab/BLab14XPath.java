package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab14XPath {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/form/div/input")).sendKeys("admin");
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//input")).clear();
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("admin@CRM");
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//input[@class='form-control' and @tabindex='1']")).sendKeys("admin");
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//input[starts-with(@class,'form-')]")).clear();
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//input[contains(@name,'pass')]")).clear();
		Thread.sleep(Duration.ofSeconds(1));
		driver.findElement(By.xpath("//button[text()='Log in']")).click();
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.findElement(By.id("field-userName")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin@CRM");
		driver.findElement(By.tagName("button")).click();
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.get("http://localhost/crm/#Account/create");
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.xpath("//button[@data-action='addEmailAddress']/../div/div/input")).sendKeys("test");
		Thread.sleep(Duration.ofSeconds(5));
		
		driver.quit();

	}
}
