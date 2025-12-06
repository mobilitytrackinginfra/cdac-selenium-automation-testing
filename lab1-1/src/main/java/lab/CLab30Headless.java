package lab;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CLab30Headless {

    public static void main(String[] args) throws InterruptedException {

        // 1) Get system resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth  = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        System.out.println("System resolution: " + screenWidth + "x" + screenHeight);

        // 2) ChromeOptions for headless
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        
        WebDriver driver = new ChromeDriver(options);
        
        try {
            org.openqa.selenium.Dimension targetSize =
                    new org.openqa.selenium.Dimension(screenWidth, screenHeight);
            driver.manage().window().setSize(targetSize);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("http://localhost/crm");

            By locatorUsername = By.id("field-userName");
            WebElement txtUsername = driver.findElement(locatorUsername);
            txtUsername.sendKeys("admin");

            By locatorPassword = By.name("password");
            WebElement txtPassword = driver.findElement(locatorPassword);
            txtPassword.sendKeys("admin");

            By locatorLogin = By.tagName("button");
            WebElement btnLogin = driver.findElement(locatorLogin);
            btnLogin.click();

            try {
                driver.findElement(By.xpath("//span[text()='Accounts']")).click();
            } catch (Exception e) {
                System.out.println(((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64));
                e.printStackTrace();
            }
            
            System.out.println(driver.getCurrentUrl());
            Thread.sleep(Duration.ofSeconds(5));

        } finally {
            driver.quit();
        }
    }
}
