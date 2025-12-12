package com.example.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for Google Search functionality.
 */
public class OdooHomePageTest extends BaseTest {
    
    @Test(description = "Verify Odoo homepage loads successfully")
    public void testOdooHomePageLoads() {
        System.out.println("Starting test: testOdooHomePageLoads");
        
        // Navigate to Odoo
        driver.get("https://www.odoo.com/app/crm");
        
        // Verify the page title contains "Free"
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        
        Assert.assertTrue(title.toLowerCase().contains("crm"), 
                "Page title should contain 'crm'. Actual title: " + title);
        
        System.out.println("Test passed: Odoo homepage loaded successfully");
    }
    
    @Test(description = "Verify Odoo pricing works correctly")
    public void testOdooPricing() {
        System.out.println("Starting test: testOdooPricing");
        
        // Navigate to Google
        driver.get("https://www.odoo.com/app/crm");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()=\"Start now - It's free\"]"))).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Pricing']"))).click();

        String pricing = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h3[text()='Standard']/../div/span[2]"))).getText();
      
        System.out.println("Standard Package Pricing: " + pricing);
        
        Assert.assertEquals(pricing, "580");
        
        System.out.println("Test passed: Verify Odoo pricing works correctly");
    }
}

