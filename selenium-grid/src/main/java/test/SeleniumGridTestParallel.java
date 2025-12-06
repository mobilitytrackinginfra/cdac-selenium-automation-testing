package test;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumGridTestParallel {

    public static void main(String[] args) throws Exception {
        URL gridUrl = new URI("http://localhost:4444").toURL();
        
        Runnable chromeTask = () -> {
        	ChromeOptions options = new ChromeOptions();
            options.setBrowserVersion("stable");
            options.setPlatformName("WINDOWS");

            WebDriver driver = new RemoteWebDriver(gridUrl, options);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();

            driver.get("https://www.google.com");
            System.out.println("Title From Chrome is: " + driver.getTitle());
            
            try {Thread.sleep(5000);} catch (InterruptedException e) {}
            driver.quit();
        };

        
        Runnable firefoxTask = () -> {
        	FirefoxOptions options = new FirefoxOptions();
            options.setBrowserVersion("stable");
            options.setPlatformName("WINDOWS");

            WebDriver driver = new RemoteWebDriver(gridUrl, options);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();

            driver.get("https://www.google.com");
            System.out.println("Title From Firefox is: " + driver.getTitle());
            
            try {Thread.sleep(5000);} catch (InterruptedException e) {}
            driver.quit();
        };
        
        Thread t1 = new Thread(chromeTask);
        Thread t2 = new Thread(firefoxTask);
        

        t1.start();
        t2.start();
        
    }
}
