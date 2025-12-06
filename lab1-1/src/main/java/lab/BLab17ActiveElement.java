package lab;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab17ActiveElement {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.google.com/?zx=1762437272877&no_sw_cr=1");
		Thread.sleep(Duration.ofSeconds(2));
		
		WebElement el = driver.switchTo().activeElement();
		el.sendKeys("India");
		System.out.println("Tag Name "+el.getTagName());
		System.out.println(el.getText());
		
		Thread.sleep(Duration.ofSeconds(3));
		driver.quit();

	}
}
