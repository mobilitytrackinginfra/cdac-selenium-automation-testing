package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class CLab20ContextClick {

	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("http://localhost/samples/contextclick.php");
		Thread.sleep(Duration.ofSeconds(3));

		Actions act = new Actions(driver).moveToElement(driver.findElement(By.className("box"))).contextClick();
		act.perform();
		driver.findElement(By.xpath("//button[@data-action='copy']")).click();
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}

}
