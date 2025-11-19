package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab14CSSSelector {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/samples/xpath-axes.php");
		Thread.sleep(Duration.ofSeconds(2));
		
		System.out.println(driver.findElement(By.cssSelector("a")).getText());
		System.out.println(driver.findElement(By.cssSelector("#item1")).getText());
		System.out.println(driver.findElement(By.cssSelector(".nav-item")).getText());
		System.out.println(driver.findElement(By.cssSelector("a#homeLink")).getText());
		System.out.println(driver.findElement(By.cssSelector("li[data-id='2']")).getText());
		System.out.println(driver.findElements(By.cssSelector("li[id*='item']")).size());
		System.out.println(driver.findElements(By.cssSelector("li[id^='item']")).size());
		System.out.println(driver.findElements(By.cssSelector("li[id$='2']")).size());
		System.out.println(driver.findElements(By.cssSelector("ul#itemList li")).size());
		System.out.println(driver.findElement(By.cssSelector("#section2 > h2")).getText());
		System.out.println(driver.findElement(By.cssSelector("#item1 + li")).getText());
		System.out.println(driver.findElements(By.cssSelector("#item1 ~ li")).size());
		System.out.println(driver.findElement(By.cssSelector("#nav li:last-child")).getText());
		System.out.println(driver.findElement(By.cssSelector("#nav li:first-child")).getText());
		System.out.println(driver.findElement(By.cssSelector("#nav li:nth-child(2)")).getText());
		System.out.println(driver.findElements(By.cssSelector("a:not(#homeLink)")).size());
		System.out.println(driver.findElements(By.cssSelector("#item1, #item3")).size());
		
		
		Thread.sleep(Duration.ofSeconds(5));
		
		driver.quit();

	}
}
