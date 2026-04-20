package com.rotur.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.utils.WaitUtils;

public class BasePage {

    protected WebDriver driver;
    protected WaitUtils wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected WebElement find(By locator) {
        return wait.WaitForVisibility(locator);
    }

    protected void enter(By locator, String text) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void click(By locator) {
        wait.WaitForClickable(locator).click();
    }

    protected String getText(By locator) {
        try {
            return find(locator).getText();
        } catch (Exception e) {
            return "";
        }
    }

    protected boolean isPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }
}
