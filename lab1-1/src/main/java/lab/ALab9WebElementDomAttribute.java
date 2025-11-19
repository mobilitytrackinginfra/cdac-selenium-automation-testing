package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab9WebElementDomAttribute {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/samples/dom.php");
		Thread.sleep(Duration.ofSeconds(5));
		
		WebElement txtUsername = driver.findElement(By.id("username"));
		System.out.println("Before Type");
		System.out.println("GetAttribute: "+txtUsername.getAttribute("value"));
		System.out.println("GetDOMAttribute: "+txtUsername.getDomAttribute("value"));
		txtUsername.sendKeys("admin");
		System.out.println("After Type");
		System.out.println("GetAttribute: "+txtUsername.getAttribute("value"));
		System.out.println("GetDOMAttribute: "+txtUsername.getDomAttribute("value"));


		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
