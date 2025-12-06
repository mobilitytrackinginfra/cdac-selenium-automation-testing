package com.espocrm.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.espocrm.pages.HomePage;
import com.espocrm.pages.LoginPage;
import com.espocrm.utils.ConfigReader;
import com.espocrm.utils.DriverManager;
import com.espocrm.utils.WebDriverFactory;

/**
 * Abstract TestNG base class that sets up and tears down a
 * WebDriver instance for each test method.  It also performs
 * login to the EspoCRM application using credentials defined
 * in the configuration file.  Concrete test classes extend
 * this class to gain access to the home page instance.
 */
public abstract class BaseTest {

	protected static final String DATA_FILE  = "src/test/resources/testData.xlsx";
	protected static final String ACCOUNTS_SHEET = "Accounts";
	protected static final String CONTACTS_SHEET = "Contacts";
	protected static final String LEADS_SHEET = "Leads";
	protected static final String OPPORTUNITIES_SHEET = "Opportunities";
    
    protected HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    protected void setUp() {
    	WebDriver driver = WebDriverFactory.createInstance();
        DriverManager.setDriver(driver);
        DriverManager.getDriver().get(ConfigReader.getProperty("baseUrl"));
    }
    
    @BeforeMethod(dependsOnMethods = "setUp")
    public void login() {
        LoginPage loginPage = new LoginPage();
        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");
        loginPage.login(username, password);
        homePage = new HomePage();
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
    	DriverManager.getDriver().quit();
    	DriverManager.unload();
    }
}