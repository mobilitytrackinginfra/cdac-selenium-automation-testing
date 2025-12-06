package framework.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class WebExecutor {

    private WebDriver driver;

    public WebExecutor(WebDriver driver) {
        this.driver = driver;
    }

    private WebElement getElement(String locatorType, String locatorValue) {
        return switch(locatorType.toLowerCase()) {
            case "id" -> driver.findElement(By.id(locatorValue));
            case "name" -> driver.findElement(By.name(locatorValue));
            case "xpath" -> driver.findElement(By.xpath(locatorValue));
            case "css" -> driver.findElement(By.cssSelector(locatorValue));
            default -> throw new RuntimeException("Invalid locator type");
        };
    }

    public void openUrl(String url) { driver.get(url); }

    public void click(String locatorType, String locatorValue) {
        getElement(locatorType, locatorValue).click();
    }

    public void type(String locatorType, String locatorValue, String text) {
        getElement(locatorType, locatorValue).sendKeys(text);
    }

    public void selectByVisibleText(String locatorType, String locatorValue, String text) {
        Select s = new Select(getElement(locatorType, locatorValue));
        s.selectByVisibleText(text);
    }

    public void assertText(String locatorType, String locatorValue, String expected) {
        String actual = getElement(locatorType, locatorValue).getText();
        if (!actual.equals(expected))
            throw new AssertionError("Expected: " + expected + ", got: " + actual);
    }
}
