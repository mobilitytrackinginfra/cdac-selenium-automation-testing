package lab;

import java.time.Duration;
import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Sequence;

public class CLab20ContextClick {

	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("http://localhost/samples/contextclick.php");
		Thread.sleep(Duration.ofSeconds(3));

		Actions actions = new Actions(driver).moveToElement(driver.findElement(By.className("box"))).contextClick()
				.pause(1000).click(driver.findElement(By.xpath("//button[@data-action='copy']")))
				.pause(1000);
		Action act = actions.build();
		
		Collection<Sequence> seq = actions.getSequences();
		for(Sequence s : seq) {
			System.out.println(s.toJson());
		}
		
		act.perform();
		driver.switchTo().alert().accept();
		Thread.sleep(Duration.ofSeconds(3));
		act.perform();
		driver.switchTo().alert().accept();
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}

}
