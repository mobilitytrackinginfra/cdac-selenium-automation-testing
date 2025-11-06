package lab;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

public class ALab5Logs {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://localhost/crm");

		Set<String> logTypes = driver.manage().logs().getAvailableLogTypes();
		System.out.println(logTypes);
		
		LogEntries logs = driver.manage().logs().get("browser");
		System.out.println("Browser Logs");
		
		for(LogEntry log:logs) {
			System.out.println(log.toJson());
		}
		
		logs = driver.manage().logs().get("client");
		System.out.println("Client Logs");
		
		for(LogEntry log:logs) {
			System.out.println(log.toJson());
		}
		
		logs = driver.manage().logs().get("driver");
		System.out.println("Driver Logs");
		
		for(LogEntry log:logs) {
			System.out.println(log.toJson());
		}
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
