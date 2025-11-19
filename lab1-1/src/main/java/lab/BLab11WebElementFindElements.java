package lab;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab11WebElementFindElements {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.google.com");
		Thread.sleep(Duration.ofSeconds(2));
		
		List<WebElement> allDivs = driver.findElements(By.tagName("div"));
		System.out.println(allDivs.size());
		
		int i = 1;
		for(WebElement div : allDivs) {
			int total = div.findElements(By.tagName("a")).size();
			System.out.println("Div:"+i+", Links: "+total);
			i++;
		}
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
