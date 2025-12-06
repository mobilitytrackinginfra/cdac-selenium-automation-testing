package com.espocrm.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Screenshot helper using base64 so it can directly
 * be embedded in ExtentReports.
 */
public final class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static String captureBase64() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return null;
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
