package com.espocrm.tests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.espocrm.pages.AccountPage;
import com.espocrm.utils.ExcelUtils;

/**
 * Test class that exercises CRUD operations for the Accounts
 * module of EspoCRM.  Data for account creation is pulled
 * from the Accounts sheet of an Excel workbook.  Each test
 * iteration will create, verify, edit and delete one account.
 */
public class AccountsTest extends BaseTest {

    
    @DataProvider(name = "accountData")
    public Object[] accountData() {
        ExcelUtils excel = new ExcelUtils(DATA_FILE);
        return excel.getDataAsArray(ACCOUNTS_SHEET);
    }

    @Test(dataProvider = "accountData")
    public void accountsCrudTest(Map<String, String> data) {
        // Navigate to Accounts
    	homePage.gotoAccountPage();
    	AccountPage account = new AccountPage();
    	account.gotoCreateAccount();
    	String originalName = data.getOrDefault("name", "Account") + System.currentTimeMillis();
    	data.put("name", originalName);

    	String originalEmail = data.getOrDefault("email", "email");
    	String email = originalEmail.substring(0, originalEmail.indexOf("@"));
    	
    	data.put("email", email+"-"+System.currentTimeMillis()+originalEmail.substring(originalEmail.indexOf("@")));

    	account.fillForm(data);
        account.verifyAccount(data);
        account.deleteAccount();
    }
    
}