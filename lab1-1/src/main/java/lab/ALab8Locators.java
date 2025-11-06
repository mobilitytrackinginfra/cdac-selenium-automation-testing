package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab8Locators {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));
		
		By locatorPoweredBy = By.linkText("EspoCRM, Inc.");
		WebElement lnkPoweredBy = driver.findElement(locatorPoweredBy);
		lnkPoweredBy.click();

		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

		driver = new ChromeDriver();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));

		By locatorChargedBy = By.partialLinkText("Inc.");
		WebElement lnkChargedBy = driver.findElement(locatorChargedBy);
		lnkChargedBy.click();
		
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
