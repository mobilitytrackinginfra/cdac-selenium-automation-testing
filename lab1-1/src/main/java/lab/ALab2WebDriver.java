package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab2WebDriver {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/crm");

		String url = driver.getCurrentUrl();
		System.out.println("Current URL "+url);
		
		String title = driver.getTitle();
		System.out.println("Page Title "+title);
		
		String pageSource = driver.getPageSource();
		System.out.println("Page Source "+pageSource);
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
