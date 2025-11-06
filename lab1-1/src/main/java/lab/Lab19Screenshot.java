package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lab19Screenshot {

	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(3));

		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}

}
