package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ALab1Browsers {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
		
		Thread.sleep(Duration.ofSeconds(3));

//		driver = new FirefoxDriver();
//		Thread.sleep(Duration.ofSeconds(5));
//		driver.quit();
//		
//		Thread.sleep(Duration.ofSeconds(3));

		driver = new EdgeDriver();
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}
}

