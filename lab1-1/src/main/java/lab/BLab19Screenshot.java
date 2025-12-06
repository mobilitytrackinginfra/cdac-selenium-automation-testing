package lab;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab19Screenshot {

	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		Thread.sleep(Duration.ofSeconds(3));

		TakesScreenshot ts = (TakesScreenshot)driver;
		
		File src = ts.getScreenshotAs(OutputType.FILE);
		System.out.println(src.getAbsolutePath());
	    File dst = new File("D:\\CDAC\\cdac-selenium-automation-testing\\screenshot-temp\\"+System.currentTimeMillis()+".png");
		System.out.println(dst.getAbsolutePath());
	    dst.getParentFile().mkdirs();
	    Files.copy(src.toPath(), dst.toPath());

	    // As BYTES
	    byte[] png = ts.getScreenshotAs(OutputType.BYTES);

	    // As BASE64
	    String b64 = ts.getScreenshotAs(OutputType.BASE64);
	    System.out.println(b64);
	    
	    src = driver.findElement(By.tagName("button")).getScreenshotAs(OutputType.FILE);
	    dst = new File("D:\\CDAC\\cdac-selenium-automation-testing\\screenshot-temp\\"+System.currentTimeMillis()+".png");
	    dst.getParentFile().mkdirs();
	    Files.copy(src.toPath(), dst.toPath());
	    
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();
	}

}
