package com.espocrm.pages;

import org.openqa.selenium.By;

/**
 * Represents the landing page after a successful login.  From
 * here users can access different modules such as Accounts,
 * Contacts, Leads and Opportunities via the navigation menu.
 */
public class HomePage extends BasePage {

    public HomePage() {
        super();
    }
    
    public void gotoAccountPage() {
        By moduleLink = By.linkText("Accounts");
        click(moduleLink);
    }
    
    public void gotoContactsPage() {
        By moduleLink = By.linkText("Contacts");
        click(moduleLink);
    }
    
    public void gotoLeadsPage() {
        By moduleLink = By.linkText("Leads");
        click(moduleLink);
    }
    
    public void gotoOpportunitiesPage() {
        By moduleLink = By.linkText("Opportunities");
        click(moduleLink);
    }
    
}