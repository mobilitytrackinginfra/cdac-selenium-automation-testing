package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput.ScrollOrigin;

public class CLab21Actions {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.findElement(By.id("field-userName")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin@CRM");
		driver.findElement(By.tagName("button")).click();
		Thread.sleep(Duration.ofSeconds(5));
		
		Actions act = new Actions(driver)
				.moveToElement(driver.findElement(By.xpath("//span[text()='Stream']")))
				.clickAndHold()
				.moveByOffset(100, 0)
				.pause(Duration.ofSeconds(3))
				.release();
		act.perform();

		Thread.sleep(Duration.ofSeconds(3));
		
		act = new Actions(driver)
				.clickAndHold(driver.findElement(By.xpath("//span[text()='Stream']")))
				.pause(Duration.ofSeconds(3))
				.release(driver.findElement(By.xpath("//span[text()='My Activities']")));
		act.perform();
		
		Thread.sleep(Duration.ofSeconds(3));
		
		act = new Actions(driver).dragAndDrop(driver.findElement(By.xpath("//span[text()='Stream']")), driver.findElement(By.xpath("//span[text()='My Activities']")));
		act.perform();
		
		Thread.sleep(Duration.ofSeconds(5));
		
		driver.get("https://en.wikipedia.org/wiki/India");
		Thread.sleep(Duration.ofSeconds(3));
		
		act = new Actions(driver).scrollToElement(driver.findElement(By.xpath("//h2[@id='Etymology']"))).pause(Duration.ofSeconds(3)).scrollByAmount(0, 200);
		act.perform();
		Thread.sleep(Duration.ofSeconds(3));
		
		new Actions(driver).scrollFromOrigin(ScrollOrigin.fromElement(driver.findElement(By.xpath("//span[text()='India']"))), 0, 200).perform();
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
