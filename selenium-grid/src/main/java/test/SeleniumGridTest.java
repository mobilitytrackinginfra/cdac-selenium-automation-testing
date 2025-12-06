package test;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumGridTest {

    public static void main(String[] args) throws Exception {
        URL gridUrl = new URI("http://localhost:4444").toURL();
//        ChromeOptions options = new ChromeOptions();
        FirefoxOptions options = new FirefoxOptions();
        options.setBrowserVersion("stable");
        options.setPlatformName("WINDOWS");

        WebDriver driver = new RemoteWebDriver(gridUrl, options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        driver.get("https://www.google.com");
        System.out.println("Title is: " + driver.getTitle());
        
        Thread.sleep(5000);
        driver.quit();
    }
}
