package lab;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CLab29ExpectedConditions {

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("http://localhost/samples/waits.php");

            // -----------------------------------------------------------------
            // Basic elements reused across many conditions
            // -----------------------------------------------------------------
            WebElement status = driver.findElement(By.id("status"));
            WebElement message = driver.findElement(By.id("message"));
            WebElement messageInput = driver.findElement(By.id("messageInput"));
            WebElement cb1 = driver.findElement(By.id("cb1"));
            WebElement cb2 = driver.findElement(By.id("cb2"));
            WebElement toHide = driver.findElement(By.id("toHide"));
            WebElement staleElement = driver.findElement(By.id("staleElement"));
            WebElement parent = driver.findElement(By.id("parent"));
            WebElement nestedParent = driver.findElement(By.id("nestedParent"));
            WebElement btnAlert = driver.findElement(By.id("btnAlert"));
            WebElement btnToggleStatus = driver.findElement(By.id("btnToggleStatus"));
            WebElement btnHide = driver.findElement(By.id("btnHide"));
            WebElement btnChangeText = driver.findElement(By.id("btnChangeText"));
            WebElement btnRemoveStale = driver.findElement(By.id("btnRemoveStale"));
            WebElement btnAddItem = driver.findElement(By.id("btnAddItem"));
            WebElement btnToggleCheckbox = driver.findElement(By.id("btnToggleCheckbox"));
            WebElement newWindowLink = driver.findElement(By.id("newWindowLink"));

            List<WebElement> children = driver.findElements(By.cssSelector("#parent .child"));
            List<WebElement> items = driver.findElements(By.cssSelector("#itemsList .item"));

            // -----------------------------------------------------------------
            // TITLE & URL
            // -----------------------------------------------------------------
            // titleIs
            wait.until(ExpectedConditions.titleIs("ExpectedConditions Demo"));

            // titleContains
            wait.until(ExpectedConditions.titleContains("Demo"));

            // urlToBe
            wait.until(ExpectedConditions.urlToBe("http://localhost/samples/waits.php0"));

            // urlContains
            wait.until(ExpectedConditions.urlContains("/waits.php"));

            // urlMatches (simple regex to match file URL)
            wait.until(ExpectedConditions.urlMatches(".*waits\\.php$"));

            // -----------------------------------------------------------------
            // ALERTS
            // -----------------------------------------------------------------
            btnAlert.click();
            Thread.sleep(Duration.ofSeconds(5));
            // alertIsPresent
            wait.until(ExpectedConditions.alertIsPresent()).accept();

            // -----------------------------------------------------------------
            // ATTRIBUTE / DOM ATTRIBUTE / DOM PROPERTY
            // -----------------------------------------------------------------
            // Before: data-state="idle"
            btnToggleStatus.click(); // now should be "ready"
            Thread.sleep(Duration.ofSeconds(5));

            // attributeContains(By, ...)
            wait.until(ExpectedConditions.attributeContains(
                    By.id("status"), "data-state", "ready"));

            // attributeContains(WebElement, ...)
            wait.until(ExpectedConditions.attributeContains(
                    status, "data-state", "ready"));

            // attributeToBe(By, ...)
            wait.until(ExpectedConditions.attributeToBe(
                    By.id("status"), "data-state", "ready"));

            // attributeToBe(WebElement, ...)
            wait.until(ExpectedConditions.attributeToBe(
                    status, "data-state", "ready"));

            // attributeToBeNotEmpty(WebElement, ...)
            wait.until(ExpectedConditions.attributeToBeNotEmpty(
                    status, "data-state"));

            // For domAttributeToBe / domPropertyToBe we conceptually use data-* etc.
            // domAttributeToBe
            wait.until(ExpectedConditions.domAttributeToBe(
                    status, "data-dom-attr", "ok"));

            // domPropertyToBe (assumes property value is "ok" after JS manipulations)
            wait.until(ExpectedConditions.domPropertyToBe(
                    status, "dataset", status.getDomProperty("dataset")));

            // -----------------------------------------------------------------
            // SELECTION / CHECKBOXES
            // -----------------------------------------------------------------
            // elementSelectionStateToBe(By, ...)
            wait.until(ExpectedConditions.elementSelectionStateToBe(
                    By.id("cb1"), true));

            // elementSelectionStateToBe(WebElement, ...)
            wait.until(ExpectedConditions.elementSelectionStateToBe(
                    cb1, true));

            // elementToBeSelected(By, ...)
            wait.until(ExpectedConditions.elementToBeSelected(
                    By.id("cb1")));

            // elementToBeSelected(WebElement, ...)
            wait.until(ExpectedConditions.elementToBeSelected(cb1));

            // Use toggle for second checkbox
            btnToggleCheckbox.click();
            wait.until(ExpectedConditions.elementSelectionStateToBe(
                    cb2, true));

            // -----------------------------------------------------------------
            // CLICKABILITY
            // -----------------------------------------------------------------
            // elementToBeClickable(By, ...)
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("btnAddItem"))).click();

            // elementToBeClickable(WebElement, ...)
            wait.until(ExpectedConditions.elementToBeClickable(
                    btnChangeText)).click();

            // -----------------------------------------------------------------
            // FRAMES (4 overloads)
            // -----------------------------------------------------------------
            // frameToBeAvailableAndSwitchToIt(int)
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
            driver.switchTo().defaultContent();

            // frameToBeAvailableAndSwitchToIt(String) – by name or id
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("demoFrameByName"));
            driver.switchTo().defaultContent();

            // frameToBeAvailableAndSwitchToIt(By)
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                    By.id("demoFrameByLocator")));
            driver.switchTo().defaultContent();

            // frameToBeAvailableAndSwitchToIt(WebElement)
            WebElement frameElement = driver.findElement(By.id("demoFrameByIndex"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
            driver.switchTo().defaultContent();

            // -----------------------------------------------------------------
            // INVISIBILITY
            // -----------------------------------------------------------------
            // invisibilityOf(WebElement)
            btnHide.click(); // hides #toHide
            Thread.sleep(Duration.ofSeconds(5));
            wait.until(ExpectedConditions.invisibilityOf(toHide));

            // invisibilityOfAllElements(List<WebElement>)
            wait.until(ExpectedConditions.invisibilityOfAllElements(
                    Arrays.asList(toHide)));

            // invisibilityOfAllElements(WebElement...)
            wait.until(ExpectedConditions.invisibilityOfAllElements(toHide));

            // invisibilityOfElementLocated(By)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.id("toHide")));

            // invisibilityOfElementWithText(By, String)
            wait.until(ExpectedConditions.invisibilityOfElementWithText(
                    By.id("textToHide"), "I will be hidden"));

            // -----------------------------------------------------------------
            // JAVASCRIPT
            // -----------------------------------------------------------------
            // javaScriptThrowsNoExceptions(String)
            wait.until(ExpectedConditions.javaScriptThrowsNoExceptions(
                    "return document.title;"));

            // jsReturnsValue(String)
            Object jsVal = wait.until(ExpectedConditions.jsReturnsValue(
                    "return document.getElementById('status').getAttribute('data-state');"));
            System.out.println("jsReturnsValue: " + jsVal);

            // -----------------------------------------------------------------
            // LOGICAL COMBINATIONS
            // -----------------------------------------------------------------
            // not(...)
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.titleContains("SomethingElse")));

            // and(...)
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.titleContains("Demo"),
                    ExpectedConditions.presenceOfElementLocated(By.id("status"))
            ));

            // or(...)
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElement(status, "READY"),
                    ExpectedConditions.textToBePresentInElement(status, "IDLE")
            ));

            // -----------------------------------------------------------------
            // COUNTS / NUMBER OF ELEMENTS / WINDOWS
            // -----------------------------------------------------------------
            // Add one more item to list so we know the count increases
            btnAddItem.click();
            Thread.sleep(Duration.ofSeconds(5));

            // numberOfElementsToBe(By, int)
            wait.until(ExpectedConditions.numberOfElementsToBe(
                    By.cssSelector("#itemsList .item"), 4)); // we added 2 items earlier

            // numberOfElementsToBeLessThan(By, int)
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(
                    By.cssSelector("#itemsList .item"), 10));

            // numberOfElementsToBeMoreThan(By, int)
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    By.cssSelector("#itemsList .item"), 2));

            // numberOfWindowsToBe(int)
            String originalHandle = driver.getWindowHandle();
            newWindowLink.click();
            Thread.sleep(Duration.ofSeconds(5));
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            // Close extra window to clean up
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(originalHandle)) {
                    driver.switchTo().window(handle).close();
                }
            }
            driver.switchTo().window(originalHandle);

            // -----------------------------------------------------------------
            // PRESENCE
            // -----------------------------------------------------------------
            // presenceOfAllElementsLocatedBy(By)
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("#itemsList .item")));

            // presenceOfElementLocated(By)
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("status")));

            // presenceOfNestedElementLocatedBy(By parent, By child)
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                    By.id("parent"), By.className("child")));

            // presenceOfNestedElementLocatedBy(WebElement parent, By child)
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                    parent, By.className("child")));

            // presenceOfNestedElementsLocatedBy(By parent, By child)
            wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(
                    By.id("parent"), By.className("child")));

            // -----------------------------------------------------------------
            // REFRESHED
            // -----------------------------------------------------------------
            // refreshed(ExpectedCondition<T>)
            ExpectedCondition<WebElement> refreshedCondition =
                    ExpectedConditions.refreshed(
                            ExpectedConditions.visibilityOfElementLocated(By.id("status"))
                    );
            wait.until(refreshedCondition);

            // -----------------------------------------------------------------
            // STALENESS
            // -----------------------------------------------------------------
            btnRemoveStale.click(); // removes #staleElement
            Thread.sleep(Duration.ofSeconds(5));
            wait.until(ExpectedConditions.stalenessOf(staleElement));

            // -----------------------------------------------------------------
            // TEXT CONDITIONS
            // -----------------------------------------------------------------
            // textMatches(By, Pattern)
            btnChangeText.click();
            Thread.sleep(Duration.ofSeconds(5));
            wait.until(ExpectedConditions.textMatches(
                    By.id("message"),
                    Pattern.compile("New message.*")));

            // textToBe(By, String)
            wait.until(ExpectedConditions.textToBe(
                    By.id("message"), "New message text"));

            // textToBePresentInElement(WebElement, String)
            wait.until(ExpectedConditions.textToBePresentInElement(
                    message, "New"));

            // textToBePresentInElementLocated(By, String)
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.id("message"), "New"));

            // textToBePresentInElementValue(By, String)
            wait.until(ExpectedConditions.textToBePresentInElementValue(
                    By.id("messageInput"), "New message"));

            // textToBePresentInElementValue(WebElement, String)
            wait.until(ExpectedConditions.textToBePresentInElementValue(
                    messageInput, "New message"));

            // -----------------------------------------------------------------
            // VISIBILITY / NESTED VISIBILITY
            // -----------------------------------------------------------------
            // visibilityOf(WebElement)
            wait.until(ExpectedConditions.visibilityOf(status));

            // visibilityOfAllElements(List<WebElement>)
            wait.until(ExpectedConditions.visibilityOfAllElements(children));

            // visibilityOfAllElements(WebElement...)
            wait.until(ExpectedConditions.visibilityOfAllElements(
                    children.get(0), children.get(1)));

            // visibilityOfAllElementsLocatedBy(By)
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.cssSelector("#itemsList .item")));

            // visibilityOfElementLocated(By)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("status")));

            // visibilityOfNestedElementsLocatedBy(By parent, By child)
            wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                    By.id("nestedParent"), By.className("nested-child")));

            // visibilityOfNestedElementsLocatedBy(WebElement parent, By child)
            wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(
                    nestedParent, By.className("nested-child")));

            System.out.println("All ExpectedConditions executed.");

        } finally {
            driver.quit();
        }
    }
}
