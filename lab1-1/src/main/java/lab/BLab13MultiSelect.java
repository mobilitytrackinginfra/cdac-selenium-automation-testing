package lab;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class BLab13MultiSelect {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/samples/select.php");
		Thread.sleep(Duration.ofSeconds(2));
		
		WebElement el = driver.findElement(By.id("langs"));
		Select langs = new Select(el);
		langs.selectByIndex(0);
		Thread.sleep(Duration.ofSeconds(1));
		langs.selectByVisibleText("CSS");
		Thread.sleep(Duration.ofSeconds(1));
		langs.selectByContainsVisibleText("Type");
		Thread.sleep(Duration.ofSeconds(1));
		
		langs.deselectByIndex(3);
		Thread.sleep(Duration.ofSeconds(1));
		langs.deselectByVisibleText("CSS");
		Thread.sleep(Duration.ofSeconds(1));
		langs.deSelectByContainsVisibleText("HTM");
		Thread.sleep(Duration.ofSeconds(2));
		

		langs.selectByVisibleText("CSS");
		langs.selectByIndex(0);
		langs.selectByContainsVisibleText("Type");
		Thread.sleep(Duration.ofSeconds(2));

		List<WebElement> allLangs = langs.getOptions();
		System.out.println("###Get All Options");
		for(WebElement l : allLangs) {
			System.out.println(l.getText());
		}
		System.out.println("###Get First Selected Option");
		System.out.println(langs.getFirstSelectedOption().getText());
		
		List<WebElement> selectedLangs = langs.getAllSelectedOptions();
		System.out.println("###Get All Selected Options");
		for(WebElement l : selectedLangs) {
			System.out.println(l.getText());
		}
		
		System.out.println("###Multiple "+langs.isMultiple());
		langs.deselectAll();
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
