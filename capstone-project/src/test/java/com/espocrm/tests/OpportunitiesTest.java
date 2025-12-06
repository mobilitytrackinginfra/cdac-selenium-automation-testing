package com.espocrm.tests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.espocrm.pages.OpportunitiesPage;
import com.espocrm.utils.ExcelUtils;

/**
 * CRUD tests for the Opportunities module.  Data is sourced from
 * the Opportunities sheet of the Excel workbook.  Each
 * opportunity record is created, verified, updated and deleted.
 */
public class OpportunitiesTest extends BaseTest {

    @DataProvider(name = "opportunityData")
    public Object[] opportunityData() {
        ExcelUtils excel = new ExcelUtils(DATA_FILE);
        return excel.getDataAsArray(OPPORTUNITIES_SHEET);
    }

    @Test(dataProvider = "opportunityData")
    public void opportunitiesCrudTest(Map<String, String> data) {
    	homePage.gotoOpportunitiesPage();
    	OpportunitiesPage opportunitiesPage = new OpportunitiesPage();
    	
    	opportunitiesPage.gotoCreateOpportunity();
    	
    	String originalName = data.getOrDefault("name", "Name") + System.currentTimeMillis();
    	data.put("name", originalName);

    	opportunitiesPage.fillForm(data);
    	opportunitiesPage.verifyOpportunity(data);
    	opportunitiesPage.deleteOpportunity();
    }
}