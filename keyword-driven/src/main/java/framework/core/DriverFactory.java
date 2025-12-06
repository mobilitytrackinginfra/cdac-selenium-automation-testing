package framework.core;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

    public static WebDriver createDriver(String mode) {
        try {
            if (mode.equalsIgnoreCase("grid")) {
                ChromeOptions options = new ChromeOptions();
                return new RemoteWebDriver(new URI("http://192.168.1.44:4444").toURL(), options);
            } else {
                WebDriver driver = new ChromeDriver();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driver.manage().window().maximize();
                return driver;
            }
        } catch (Exception e) {
            throw new RuntimeException("Driver init failed: " + e.getMessage());
        }
    }
}
