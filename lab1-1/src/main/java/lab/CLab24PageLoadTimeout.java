package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CLab24PageLoadTimeout {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		System.out.println("Before Set: "+driver.manage().timeouts().getPageLoadTimeout().getSeconds());
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		System.out.println("After Set: "+driver.manage().timeouts().getPageLoadTimeout().getSeconds());
		driver.get("http://localhost/samples/timeout-test.php?mode=slow");
		
		Thread.sleep(Duration.ofSeconds(3));
		driver.quit();

	}
}
