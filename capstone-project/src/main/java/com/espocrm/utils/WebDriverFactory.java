package com.espocrm.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Factory responsible for provisioning WebDriver instances
 * based on the desired browser specified in the configuration.
 * Each thread maintains its own driver instance via a
 * {@link ThreadLocal} to support parallel execution.
 */
public final class WebDriverFactory {

    /**
     * Creates (if necessary) and returns the WebDriver instance
     * bound to the current thread.  If a driver has already
     * been created for this thread it is reused.  The browser
     * type is derived from the config file with a default
     * fallback to Chrome.
     *
     * @return the thread‑bound WebDriver instance
     */
    public static WebDriver createInstance() {
    	String browser = ConfigReader.getProperty("browser");
        if (browser == null) {
            browser = "chrome";
        }
        WebDriver driver = null;
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.addArguments("--remote-allow-origins=*", "--start-maximized");
                driver = new FirefoxDriver(ffOptions);
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*", "--start-maximized");
                driver = new ChromeDriver(options);
                break;
        }
        return driver;
    }

}