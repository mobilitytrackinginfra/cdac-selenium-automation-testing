package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab9WebElementAssignments {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.google.com");
		Thread.sleep(Duration.ofSeconds(10));
		
		WebElement el = driver.findElement(By.className("CgwTDb"));
		System.out.println(el.isDisplayed());
		Thread.sleep(Duration.ofSeconds(7));
		System.out.println(el.isDisplayed());
		
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
