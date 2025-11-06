package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab15Alerts {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/samples/alerts.php");
		Thread.sleep(Duration.ofSeconds(2));
		
		driver.findElement(By.xpath("//button[text()='Show Alert']")).click();
		Thread.sleep(Duration.ofSeconds(2));
		String alertText = driver.switchTo().alert().getText();
		System.out.println("Alert Text: "+alertText);
		driver.switchTo().alert().accept();
		Thread.sleep(Duration.ofSeconds(3));
		
		driver.findElement(By.xpath("//button[text()='Show Input Alert']")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.switchTo().alert().dismiss();
		Thread.sleep(Duration.ofSeconds(2));
		alertText = driver.switchTo().alert().getText();
		System.out.println("Alert Dismiss Message: "+alertText);
		driver.switchTo().alert().accept();
		Thread.sleep(Duration.ofSeconds(2));

		
		driver.findElement(By.xpath("//button[text()='Show Input Alert']")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.switchTo().alert().sendKeys("test");
		Thread.sleep(Duration.ofSeconds(2));
		driver.switchTo().alert().accept();
		Thread.sleep(Duration.ofSeconds(2));
		alertText = driver.switchTo().alert().getText();
		System.out.println("Entered Value: "+alertText);
		driver.switchTo().alert().accept();
		Thread.sleep(Duration.ofSeconds(3));

		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
