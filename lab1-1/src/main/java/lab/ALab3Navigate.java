package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab3Navigate {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(5));

		driver.navigate().to("http://localhost/samples/alerts.php");
		Thread.sleep(Duration.ofSeconds(5));

		driver.navigate().back();
		Thread.sleep(Duration.ofSeconds(3));
		
		driver.navigate().forward();
		Thread.sleep(Duration.ofSeconds(3));

		driver.navigate().refresh();

		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
