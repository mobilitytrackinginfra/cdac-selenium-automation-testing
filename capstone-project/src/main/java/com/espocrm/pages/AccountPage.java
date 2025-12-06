package com.espocrm.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;

public class AccountPage extends BasePage {

	private final By txtName = By.xpath("//input[@data-name='name']");
	private final By txtWebsite = By.xpath("//input[@data-name='website']");
	private final By selectPhoneCountry = By.xpath("//div[@aria-label='Telephone country code']");
	private final By txtMobile = By.xpath("//input[@type='text' and contains(@class,'phone-number')]");
	private final By txtEmail = By.xpath("//input[@type='email']");
	private final By btnSave = By.xpath("//button[@data-name='save']");
	
	private final By lblName = By.xpath("//span[text()='Name']/../../div/span");
	private final By lblWebsite = By.xpath("//span[text()='Website']/../../div/a");
	private final By lblEmail = By.xpath("//span[text()='Email']/../../div/div/a");
	
	private final By menuAccount = By.xpath("//button[text()='Edit']/../button[2]");
	private final By menuRemove = By.xpath("//a[@data-action='delete']");
	private final By menuRemoveConfirm = By.xpath("//button[text()=' Remove ']");
	
	private final By removeSuccessMsg = By.xpath("//div[@class='message' and text()='Removed']");
	
	private final By btnCreateAccount = By.xpath("//span[text()='Create Account']");
	
	public void gotoCreateAccount() {
		click(btnCreateAccount);
	}
	
	public void fillForm(Map<String, String> formData) {
		type(txtName, formData.get("name"));
		type(txtWebsite, formData.get("website"));
		click(selectPhoneCountry);
		click(By.xpath("//span[text()='"+formData.get("countrycode")+"']"));
		type(txtMobile, formData.get("phone"));
		type(txtEmail, formData.get("email"));
		click(btnSave);
    }
	
	public void verifyAccount(Map<String, String> formData) {
		Assert.assertEquals(getText(lblName), formData.get("name"));
		Assert.assertEquals(getText(lblWebsite), formData.get("website").replace("https://", "").replace("http://", ""));
		Assert.assertEquals(getText(lblEmail), formData.get("email"));
		getElement(By.xpath("//a[@href=\"tel:"+formData.get("countrycode")+formData.get("phone")+"\"]"));
    }
	
	public void deleteAccount() {
		click(menuAccount);
		click(menuRemove);
		click(menuRemoveConfirm);
		click(removeSuccessMsg);
	}
}
