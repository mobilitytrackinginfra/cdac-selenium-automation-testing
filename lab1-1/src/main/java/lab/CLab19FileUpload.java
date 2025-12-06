package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CLab19FileUpload {
    public static void main(String[] args) throws Exception {
    	WebDriver driver = new ChromeDriver();
    	driver.get("http://localhost/samples/elements.php");
    	Thread.sleep(Duration.ofSeconds(3));
    	WebElement fileUpload = driver.findElement(By.id("fileUpload"));
    	//invalid argument: File not found : C:\Users\swapn\Downloads\Session414.pdf
    	fileUpload.sendKeys("C:\\Users\\swapn\\Downloads\\Session4.pdf");
    	Thread.sleep(Duration.ofSeconds(3));
    	fileUpload.clear();
    	Thread.sleep(Duration.ofSeconds(5));
    	driver.quit();
    }
}
