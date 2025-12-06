package com.espocrm.tests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.espocrm.pages.LeadsPage;
import com.espocrm.utils.ExcelUtils;

/**
 * Test class covering CRUD operations on the Leads module.  Test
 * data is sourced from the Leads sheet of the provided Excel
 * workbook.  Each lead is created, verified, updated and
 * deleted within a single test method.
 */
public class LeadsTest extends BaseTest {

    @DataProvider(name = "leadData")
    public Object[] leadData() {
        ExcelUtils excel = new ExcelUtils(DATA_FILE);
        return excel.getDataAsArray(LEADS_SHEET);
    }

    @Test(dataProvider = "leadData")
    public void leadsCrudTest(Map<String, String> data) {
    	homePage.gotoLeadsPage();
    	LeadsPage leadsPage = new LeadsPage();
    	
    	leadsPage.gotoCreateLead();
    	
    	String originalName = data.getOrDefault("firstname", "FirstName") + System.currentTimeMillis();
    	data.put("firstname", originalName);

    	String originalEmail = data.getOrDefault("email", "email");
    	String email = originalEmail.substring(0, originalEmail.indexOf("@"));
    	
    	data.put("email", email+"-"+System.currentTimeMillis()+originalEmail.substring(originalEmail.indexOf("@")));

    	leadsPage.fillForm(data);
    	leadsPage.verifyLead(data);
    	leadsPage.deleteLead();
    }
}