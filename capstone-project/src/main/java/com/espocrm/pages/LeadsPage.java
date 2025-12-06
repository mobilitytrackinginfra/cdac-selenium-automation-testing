package com.espocrm.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;

public class LeadsPage extends BasePage {

	private final By txtFirstName = By.xpath("//input[@data-name='firstName']");
	private final By txtLastName = By.xpath("//input[@data-name='lastName']");
	private final By selectPhoneCountry = By.xpath("//div[@aria-label='Telephone country code']");
	private final By txtMobile = By.xpath("//input[@type='text' and contains(@class,'phone-number')]");
	private final By txtEmail = By.xpath("//input[@type='email']");
	private final By btnSave = By.xpath("//button[@data-name='save']");
	
	private final By lblName = By.xpath("//span[text()='Name']/../../div");
	private final By lblEmail = By.xpath("//span[text()='Email']/../../div/div/a");
	
	private final By menuLead = By.xpath("//button[text()='Edit']/../button[2]");
	private final By menuRemove = By.xpath("//a[@data-action='delete']");
	private final By menuRemoveConfirm = By.xpath("//button[text()=' Remove ']");
	
	private final By removeSuccessMsg = By.xpath("//div[@class='message' and text()='Removed']");
	
	private final By btnCreateLead = By.xpath("//span[text()='Create Lead']");
	
	public void gotoCreateLead() {
		click(btnCreateLead);
	}
	
	public void fillForm(Map<String, String> formData) {
		type(txtFirstName, formData.get("firstname"));
		type(txtLastName, formData.get("lastname"));
		click(selectPhoneCountry);
		click(By.xpath("//span[text()='"+formData.get("countrycode")+"']"));
		type(txtMobile, formData.get("phone"));
		type(txtEmail, formData.get("email"));
		click(btnSave);
    }
	
	public void verifyLead(Map<String, String> formData) {
		textMatches(lblName, formData.get("firstname")+" "+formData.get("lastname"));
		Assert.assertEquals(getText(lblName), formData.get("firstname")+" "+formData.get("lastname"));
		Assert.assertEquals(getText(lblEmail), formData.get("email"));
		getElement(By.xpath("//a[@href=\"tel:"+formData.get("countrycode")+formData.get("phone")+"\"]"));
    }
	
	public void deleteLead() {
		click(menuLead);
		click(menuRemove);
		click(menuRemoveConfirm);
		click(removeSuccessMsg);
	}
}
