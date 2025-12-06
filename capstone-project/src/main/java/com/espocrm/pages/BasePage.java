package com.espocrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.espocrm.utils.DriverManager;
import com.espocrm.utils.ExtentTestManager;
import com.espocrm.utils.ScreenshotUtils;

import java.time.Duration;

/**
 * Abstract base class for all page objects in the framework.
 * Provides common convenience methods for interacting with
 * elements and waiting for conditions.  Subclasses inherit
 * the driver and wait instance created here.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructs a new page instance bound to the given WebDriver.
     *
     * @param driver the WebDriver instance
     */
    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Waits for an element to be clickable and clicks it.
     *
     * @param locator the element locator
     */
    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
        logStepWithScreenshot("Clicked on element: " + locator.toString());
    }

    /**
     * Sends the given text to the element located by the provided
     * {@link By}. The call waits until the element is visible.
     *
     * @param locator element locator
     * @param text    value to input
     */
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
        logStepWithScreenshot("Typed '" + text + "' into element: " + locator.toString());
    }

    /**
     * Retrieves text content from the element located by the
     * provided {@link By}. The call waits until the element
     * is visible.
     *
     * @param locator element locator
     * @return trimmed text of the element
     */
    protected String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String text = element.getText().trim();
        logStepWithScreenshot("Read text '" + text + "' from element: " + locator.toString());
        return text;
    }

    
    protected void textMatches(By locator, String text) {
        wait.until(ExpectedConditions.textToBe(locator, text));
    }

    /**
     * Waits until the element located by the provided {@link By}
     * becomes visible and returns the {@link WebElement}.
     *
     * @param locator element locator
     * @return the located WebElement
     */
    protected WebElement getElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    
    /**
     * Logs a step to ExtentReports with a screenshot.
     *
     * @param message step description
     */
    private void logStepWithScreenshot(String message) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) {
            return; // running without listener or report
        }

        String base64 = ScreenshotUtils.captureBase64();
        if (base64 == null) {
            test.info(message);
            return;
        }

        try {
            test.info(message,
                    MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64, message)
                            .build());
        } catch (Exception e) {
            test.info(message + " (screenshot failed: " + e.getMessage() + ")");
        }
    }

}