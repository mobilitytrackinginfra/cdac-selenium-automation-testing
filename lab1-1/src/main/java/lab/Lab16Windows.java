package lab;

import java.time.Duration;
import java.util.Set;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lab16Windows {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));

		String originalWindow = driver.getWindowHandle();
		Set<String> before = driver.getWindowHandles();
		System.out.println("All Window IDs Before New Window "+before);
		driver.findElement(By.linkText("EspoCRM, Inc.")).click();
		Thread.sleep(Duration.ofSeconds(2));

		//Switching Logic
		Set<String> after = driver.getWindowHandles();
		System.out.println("All Window IDs After New Window "+after);
		after.removeAll(before);
		String newHandle = after.iterator().next();
		System.out.println("Latest Opened Window ID: "+newHandle);
		
		//Switching Logic End
		driver.switchTo().window(newHandle);
		Thread.sleep(Duration.ofSeconds(4));
		System.out.println("New Window Title: "+driver.getTitle());
		driver.findElement(By.xpath("//a[text()='DOWNLOAD']")).click();
		Thread.sleep(Duration.ofSeconds(3));
		driver.close();
		
		Thread.sleep(Duration.ofSeconds(2));
		driver.switchTo().window(originalWindow);
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(3));
		driver.quit();

	}
}
