package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CLab19FileUpload2 {
    public static void main(String[] args) throws Exception {
    	WebDriver driver = new ChromeDriver();
    	driver.get("https://www.ilovepdf.com/pdf_to_excel");
    	Thread.sleep(Duration.ofSeconds(3));
    	WebElement fileUpload = driver.findElement(By.xpath("//input[@type='file']"));
    	//invalid argument: File not found : C:\Users\swapn\Downloads\Session414.pdf
    	fileUpload.sendKeys("C:\\Users\\swapn\\Downloads\\Session4.pdf\nC:\\Users\\swapn\\Downloads\\Session2.pdf");
    	Thread.sleep(Duration.ofSeconds(3));
    	
    	Thread.sleep(Duration.ofSeconds(10));
    	driver.quit();
    }
}
