package com.example.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

/**
 * Base test class that handles WebDriver setup and teardown.
 * Connects to Selenium Grid Hub.
 */
public class BaseTest {
    
    protected WebDriver driver;
    
    // Default Selenium Grid URL - can be overridden via system property
    private static final String DEFAULT_GRID_URL = "http://localhost:4444/wd/hub";
    
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        // Get Grid URL from system property or use default
        String gridUrl = System.getProperty("selenium.grid.url", DEFAULT_GRID_URL);
        
        System.out.println("Connecting to Selenium Grid at: " + gridUrl);
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        // Create Remote WebDriver connecting to Selenium Grid
        driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        System.out.println("WebDriver session created successfully");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("Closing WebDriver session");
            driver.quit();
        }
    }
}

