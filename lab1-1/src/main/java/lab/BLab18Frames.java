package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab18Frames {

	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("http://localhost/samples/frames.php");
		Thread.sleep(Duration.ofSeconds(3));

		// ===== Default content (top page) =====
		driver.findElement(By.id("mainCounterBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("mainText")).sendKeys("Main Window");
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("mainEchoBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));


		// ===== switchTo().frame(int index) -> frameA (index 0) =====
		driver.switchTo().frame(0); // frameA
		driver.findElement(By.id("aCounterBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("aText")).sendKeys("Frame A Text");
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("aEchoBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));

		// Back to MAIN
		driver.switchTo().defaultContent();

		// ===== switchTo().frame(String idOrName) -> frameB =====
		driver.switchTo().frame("frameB");
		driver.findElement(By.id("bText")).sendKeys("Frame B Text");
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("bEchoBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));


		// ===== Nested: still inside frameB, switch to child by name -> childB1 =====
		driver.switchTo().frame("childB1");
		driver.findElement(By.id("b1Text")).sendKeys("Fram A->Frame B");
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("b1EchoBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.findElement(By.id("b1AlertBtn")).click();
		Thread.sleep(Duration.ofSeconds(2));
		driver.switchTo().alert().accept();


		// ===== parentFrame(): childB1 -> frameB =====
		driver.switchTo().parentFrame();
		driver.findElement(By.id("bCounterBtn")).click();
		driver.findElement(By.id("bText")).sendKeys("BackToB");
		Thread.sleep(Duration.ofSeconds(2));


		// ===== defaultContent(): frameB -> MAIN =====
		driver.switchTo().defaultContent();
		driver.findElement(By.id("mainText")).sendKeys("BackToMain");
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}

}
