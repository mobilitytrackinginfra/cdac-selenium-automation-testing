package lab;

import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab4Window {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(5));

		driver.manage().window().fullscreen();
		Thread.sleep(Duration.ofSeconds(5));

		driver.manage().window().maximize();
		Thread.sleep(Duration.ofSeconds(5));

		driver.manage().window().minimize();
		Thread.sleep(Duration.ofSeconds(3));

		driver.manage().window().maximize();
		Thread.sleep(Duration.ofSeconds(1));

		Point point = driver.manage().window().getPosition();
		System.out.println("Position X:"+point.getX()+", Y:"+point.getY());

		Dimension dimension = driver.manage().window().getSize();
		System.out.println("Dimension Height:"+dimension.getHeight()+", Width:"+dimension.getWidth());

		driver.manage().window().setSize(new Dimension(300, 400));
		Thread.sleep(Duration.ofSeconds(2));
		driver.manage().window().setPosition(new Point(100, 150));

		point = driver.manage().window().getPosition();
		System.out.println("New Position X:"+point.getX()+", Y:"+point.getY());

		dimension = driver.manage().window().getSize();
		System.out.println("New Dimension Height:"+dimension.getHeight()+", Width:"+dimension.getWidth());

		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
