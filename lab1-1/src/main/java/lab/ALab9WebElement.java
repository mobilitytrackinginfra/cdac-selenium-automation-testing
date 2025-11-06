package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab9WebElement {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(2));
		
		By locatorUsername = By.id("field-userName");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin");

		Point point = txtUsername.getLocation();
		System.out.println("Point X:"+point.getX()+", Y:"+point.getY());
		
		System.out.println("Accessible Name:"+txtUsername.getAccessibleName());
		System.out.println("Aria Role:"+txtUsername.getAriaRole());
		System.out.println("Attribute spellcheck:"+txtUsername.getAttribute("spellcheck"));
		System.out.println("CSS Value color:"+txtUsername.getCssValue("color"));
		System.out.println("Dom Attribute:"+txtUsername.getDomAttribute("value"));
		System.out.println("Dom Property:"+txtUsername.getDomProperty("value"));
		Rectangle rect = txtUsername.getRect();
		System.out.println("Rectangle - X:"+rect.getX()+", Y:"+rect.getY()+", Height"+rect.getHeight()+", Width:"+rect.getWidth());
		System.out.println("Shadow Root:"+txtUsername.getShadowRoot());
		System.out.println("Get Size:"+txtUsername.getSize());
		System.out.println("Tag Name:"+txtUsername.getTagName());
		System.out.println("Displayed:"+txtUsername.isDisplayed());
		System.out.println("Enabled:"+txtUsername.isEnabled());
		System.out.println("Selected:"+txtUsername.isSelected());

		txtUsername.submit();
		
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
