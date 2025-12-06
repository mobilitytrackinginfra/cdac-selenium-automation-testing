package com.espocrm.tests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.espocrm.pages.ContactPage;
import com.espocrm.utils.ExcelUtils;

/**
 * Exercises CRUD operations for the Contacts module.  Uses
 * data from the Contacts sheet of the Excel workbook.  Each
 * contact is created with a unique last name to avoid
 * collisions, then edited and deleted.
 */
public class ContactsTest extends BaseTest {

    @DataProvider(name = "contactData")
    public Object[] contactData() {
        ExcelUtils excel = new ExcelUtils(DATA_FILE);
        return excel.getDataAsArray(CONTACTS_SHEET);
    }

    @Test(dataProvider = "contactData")
    public void contactsCrudTest(Map<String, String> data) {
    	
    	homePage.gotoContactsPage();
    	ContactPage contactPage = new ContactPage();
    	
    	contactPage.gotoCreateContact();
    	
    	String originalName = data.getOrDefault("firstname", "FirstName") + System.currentTimeMillis();
    	data.put("firstname", originalName);

    	String originalEmail = data.getOrDefault("email", "email");
    	String email = originalEmail.substring(0, originalEmail.indexOf("@"));
    	
    	data.put("email", email+"-"+System.currentTimeMillis()+originalEmail.substring(originalEmail.indexOf("@")));

    	contactPage.fillForm(data);
    	contactPage.verifyContact(data);
    	contactPage.deleteContact();
    }
}