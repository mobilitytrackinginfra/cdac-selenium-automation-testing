package page;

import java.time.Duration;

import org.openqa.selenium.By;
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

    protected void click(String locator) {
    	getElement(locator).click();
    }

    protected void type(String locator, String text) {
    	getElement(locator).sendKeys(text);
    }

    protected String getText(String locator) {
        return getElement(locator).getText();
    }
    
    protected void clearText(String locator) {
        getElement(locator).clear();
    }

    protected boolean isDisplayed(String locator) {
        return getElement(locator).isDisplayed();
    }
    
    protected WebElement getElement(String xpath) {
    	return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }
}
