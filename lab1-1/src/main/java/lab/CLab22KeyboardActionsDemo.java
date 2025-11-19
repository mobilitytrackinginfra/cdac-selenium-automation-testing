package lab;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class CLab22KeyboardActionsDemo {
    public static void main(String[] args) throws InterruptedException {
        // 1) Setup driver
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            driver.get("http://localhost/samples/keyboardevents.php");
            Thread.sleep(Duration.ofSeconds(5));
            
            WebElement arena = driver.findElement(By.id("arena"));
            new Actions(driver).moveToElement(arena).click().perform();
            Actions actions = new Actions(driver);

            // Move Right for ~1 second (hold ArrowRight down)
            actions.keyDown(Keys.ARROW_RIGHT).perform();
            Thread.sleep(1000);
            actions.keyUp(Keys.ARROW_RIGHT).perform();


            // Diagonal: Up + Right together for ~900ms
            actions.keyDown(Keys.ARROW_UP).keyDown(Keys.ARROW_RIGHT).perform();
            Thread.sleep(900);
            actions.keyUp(Keys.ARROW_RIGHT).keyUp(Keys.ARROW_UP).perform();

            // Pause (Space toggles pause in the demo)
            actions.keyDown(Keys.SPACE).keyUp(Keys.SPACE).perform();
            Thread.sleep(700);

            // Resume
            actions.keyDown(Keys.SPACE).keyUp(Keys.SPACE).perform();

            // Reset with 'R'
            actions.keyDown("r").keyUp("r").perform();

            // Small scripted path example: a rectangle loop using holds
            hold(driver, Keys.ARROW_RIGHT, 700);
            hold(driver, Keys.ARROW_DOWN, 700);
            hold(driver, Keys.ARROW_LEFT, 700);
            hold(driver, Keys.ARROW_UP, 700);

            // Bonus: WASD also works (mapped in your HTML). Move with 'D' then 'W'.
            hold(driver, "d", 600);
            hold(driver, "w", 600);

        } finally {
            Thread.sleep(1200);
            driver.quit();
        }
    }

    private static void hold(WebDriver driver, Keys key, long millis) throws InterruptedException {
        Actions a = new Actions(driver);
        a.keyDown(key).perform();
        Thread.sleep(millis);
        a.keyUp(key).perform();
    }

    private static void hold(WebDriver driver, String keyChar, long millis) throws InterruptedException {
        if (keyChar == null || keyChar.length() != 1) {
            throw new IllegalArgumentException("Use a single character like 'w', 'a', 's', 'd'.");
        }
        Actions a = new Actions(driver);
        a.keyDown(keyChar).perform();
        Thread.sleep(millis);
        a.keyUp(keyChar).perform();
    }
}
