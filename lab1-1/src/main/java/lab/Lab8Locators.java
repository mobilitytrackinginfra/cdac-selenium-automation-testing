package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lab8Locators {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://localhost/suitecrm/public/");
		Thread.sleep(Duration.ofSeconds(5));
		
		By locatorPoweredBy = By.linkText("© Powered By SugarCRM");
		WebElement lnkPoweredBy = driver.findElement(locatorPoweredBy);
		lnkPoweredBy.click();

		driver.navigate().refresh();

		By locatorChargedBy = By.partialLinkText("Supercharged by");
		WebElement lnkChargedBy = driver.findElement(locatorChargedBy);
		lnkChargedBy.click();
		
		
		Thread.sleep(Duration.ofSeconds(30));
		driver.quit();

	}
}
