package com.espocrm.pages;

import org.openqa.selenium.By;

/**
 * Page object representing the EspoCRM login screen.  Provides
 * methods to populate login credentials and submit the form.  If
 * the login is successful the next page (home page) should be
 * loaded.  Locators are defined using relatively generic
 * selectors to accommodate minor UI changes.
 */
public class LoginPage extends BasePage {

    // Locators for login page elements.  Generic CSS selectors are used
    // because the exact HTML may vary between EspoCRM versions.
    private final By usernameInput = By.cssSelector("input[name='username']");
    private final By passwordInput = By.cssSelector("input[name='password']");
    private final By loginButton   = By.cssSelector("button[type='submit']");

    /**
     * Enters the supplied credentials into the login form and
     * submits it.  Upon completion the caller should navigate to
     * the home page and perform further actions.
     *
     * @param username EspoCRM username
     * @param password EspoCRM password
     * @return a new HomePage instance
     */
    public void login(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }
}