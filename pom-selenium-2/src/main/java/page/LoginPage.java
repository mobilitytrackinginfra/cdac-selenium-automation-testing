package page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@id='field-userName1']")
    private WebElement txtUsername;
    
    @FindBy(xpath = "//input[@id='field-password']")
    private WebElement txtpasswordField;

    @FindBy(xpath = "//button[@id='btn-login']")
    private WebElement btnloginButton;

    @FindBys({
    	@FindBy(css = ".navbar-logo-container"),
    	@FindBy(css = ".logo"),
    })
    private WebElement imgLogo;

    @FindAll({
    	@FindBy(tagName = "a"),
    	@FindBy(xpath = "//a[@href]"),
    })
    private List<WebElement> links;
    
    public void login(String username, String password) {
    	System.out.println(txtUsername.getAttribute("value"));
    	type(txtUsername, username);
    	type(txtpasswordField, password);
    	click(btnloginButton);
    	click(imgLogo);
    	for(WebElement link : links) {
    		System.out.println(link.getText()+":"+link.getAttribute("href"));
    	}
    }
    
}
