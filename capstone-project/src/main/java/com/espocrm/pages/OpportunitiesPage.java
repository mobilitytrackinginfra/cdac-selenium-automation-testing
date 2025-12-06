package com.espocrm.pages;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;

public class OpportunitiesPage extends BasePage {

	private final By txtName = By.xpath("//input[@data-name='name']");
	private final By selectStage = By.xpath("//div[@data-name='stage' and @class='field']");
	private final By txtAmount = By.xpath("//input[@data-name='amount']");
	private final By txtCloseDate = By.xpath("//input[@data-name='closeDate']");
	
	private final By btnSave = By.xpath("//button[@data-name='save']");
	
	private final By lblName = By.xpath("//span[text()='Name']/../../div");
	private final By lblStage = By.xpath("//span[text()='Stage']/../../div/span");
	private final By lblAmount = By.xpath("//div[@data-name='amount']/div/span");
	private final By lblCloseDate = By.xpath("//div[@data-name='closeDate']/div/span");
	
	private final By menuLead = By.xpath("//button[text()='Edit']/../button[2]");
	private final By menuRemove = By.xpath("//a[@data-action='delete']");
	private final By menuRemoveConfirm = By.xpath("//button[text()=' Remove ']");
	
	private final By removeSuccessMsg = By.xpath("//div[@class='message' and text()='Removed']");
	
	private final By btnCreateOpportunities = By.xpath("//span[text()='Create Opportunity']");
	
	public void gotoCreateOpportunity() {
		click(btnCreateOpportunities);
	}
	
	public void fillForm(Map<String, String> formData) {
		type(txtName, formData.get("name"));
		click(selectStage);
		click(By.xpath("//div[@data-value='"+formData.get("stage")+"']"));
		type(txtAmount, formData.get("amount"));
		type(txtCloseDate, formData.get("closedate"));
		click(btnSave);
    }
	
	public void verifyOpportunity(Map<String, String> formData) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
        String formattedAmount = df.format(Float.parseFloat(formData.get("amount")));

		DateTimeFormatter inputFormat  = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd MMM");
        LocalDate date = LocalDate.parse(formData.get("closedate"), inputFormat);
        String formattedCloseDate = date.format(outputFormat);

		textMatches(lblName, formData.get("name"));
		Assert.assertEquals(getText(lblName), formData.get("name"));
		Assert.assertEquals(getText(lblStage), formData.get("stage"));
		Assert.assertEquals(getText(lblAmount), formattedAmount);
		
        Assert.assertEquals(getText(lblCloseDate), formattedCloseDate);
    }
	
	public void deleteOpportunity() {
		click(menuLead);
		click(menuRemove);
		click(menuRemoveConfirm);
		click(removeSuccessMsg);
	}
}
