package page;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

	private String txtUsername = "//input[@id='field-userName']";
    private String txtpasswordField = "//input[@id='field-password']";
    private String btnloginButton = "//button[@id='btn-login']";
    private String menuAccounts = "//span[text()='Accounts']";
    private String menu = "//a[@id='nav-menu-dropdown']";
    private String menuLogout = "//a[@data-name='logout']";
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private void enterUsername(String username) {
        type(txtUsername, username);
    }
    
    private void clearUsername() {
        clearText(txtUsername);
    }

    private void enterPassword(String password) {
        type(txtpasswordField, password);
    }

    private void clearPassword() {
        clearText(txtpasswordField);
    }
    private void clickLogin() {
        click(btnloginButton);
    }
    
    private void gotoDashboard() {
        click(menuAccounts);
    }

    public boolean login(String username, String password) {
    	try {
    		clearUsername();
    		clearPassword();
            enterUsername(username);
            enterPassword(password);
            clickLogin();
            gotoDashboard();			
		} catch (Exception e) {
			System.out.println("Login Failed for "+username+", "+password);
			return false;
		}

        return true;
    }
    
    public void logout() {
    	click(menu);
    	click(menuLogout);
    	
    }
}
