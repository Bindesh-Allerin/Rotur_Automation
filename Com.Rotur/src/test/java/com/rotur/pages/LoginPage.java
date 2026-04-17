package com.rotur.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.rotur.base.BasePage;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver){
        super(driver);
    }

    // LOCATORS
    By username = By.xpath("(//input)[1]");
    By password = By.xpath("//input[@type='password']");
    By loginbtn = By.xpath("//button[contains(text(),'Login')]");
    By validLogin = By.xpath("(//h1)[2]");
    By emailError = By.xpath("(//input)[1]/following-sibling::div");
    By passError = By.xpath("//input[@type='password']/following-sibling::div");
    By toast = By.xpath("//li[contains(@data-y-position, 'top')]");

    // ACTIONS
    public void login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginbtn);
    }

    // VALIDATIONS
    public boolean isSuccessLogin() {
        return isPresent(validLogin);
    }

    public String getEmailErrMsg() {
        return isPresent(emailError) ? getText(emailError) : "";
    }

    public String getPasswordErrMsg() {
        return isPresent(passError) ? getText(passError) : "";
    }

    // TOAST
    public String getToastText() {
        return getText(toast);
    }

    public boolean getToastPosition() {
        try {
            WebElement element = find(toast);
            return element.getAttribute("data-x-position").equals("right")
                    && element.getAttribute("data-y-position").equals("top");
        } catch (Exception e) {
            return false;
        }
    }

    private String getTextColorSafe(By locator, String css) {
        try {
            return find(locator).getCssValue(css);
        } catch (Exception e) {
            return "";
        }
    }
    
    public String getToastTxtColor() {
        return getTextColorSafe(toast, "color");
    }

    public String getToastBGColor() {
        return getTextColorSafe(toast, "background-color");
    }
}

