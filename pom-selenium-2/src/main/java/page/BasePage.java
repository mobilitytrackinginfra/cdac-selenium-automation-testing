package page;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    protected void click(WebElement element) {
    	getElement(element).click();
    }

    protected void type(WebElement element, String text) {
    	getElement(element).sendKeys(text);
    }

    protected String getText(WebElement element) {
        return getElement(element).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        return getElement(element).isDisplayed();
    }
    
    protected WebElement getElement(WebElement element) {
    	return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}
