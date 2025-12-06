package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab9ShadowRoot {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/samples/shadow.php");
		Thread.sleep(Duration.ofSeconds(2));

//		driver.findElement(By.id("shadow-btn")).click();
		
		WebElement shadowHost = driver.findElement(By.id("shadow-host"));
		SearchContext root = shadowHost.getShadowRoot();
		root.findElement(By.id("shadow-btn")).click();
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
