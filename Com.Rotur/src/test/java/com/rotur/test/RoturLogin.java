package com.rotur.test;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.rotur.base.Base;
import com.rotur.base.BrowserFactory;
import com.rotur.pages.LoginPage;
import com.testData.LoginData;
import com.utils.JsonUtils;
import com.utils.ValidationUtils;
import com.utils.Retry;
import com.utils.ExtentTestManager;


public class RoturLogin extends Base {

    @DataProvider(name = "loginData", parallel = true)
    public Object[] getData() {
        return JsonUtils
                .getLoginData("src/test/resources/testdata/login.json")
                .toArray();
    }

 
    
    @Test(description = "Verify Login Functionality", dataProvider = "loginData", retryAnalyzer = Retry.class)
    public void RoturLoginPage(LoginData data) {

        //System.out.println("Executing Test → " + data.toString());
        System.out.println("Thread [" + Thread.currentThread().threadId() + "] Executing → " + data.toString());

        // Use BrowserFactory.getDriver() inside the test to get the correct thread's driver
        WebDriver driver = BrowserFactory.getDriver();

        LoginPage lp = new LoginPage(driver);
        
        ExtentTestManager.getTest().info("Entering credentials....");
        ExtentTestManager.getTest().info("Executing: " + data.toString());
        lp.login(data.getEmail(), data.getPassword());

        switch (data.getType()) {

            case "success":
                ValidationUtils.validateText(
                        String.valueOf(lp.isSuccessLogin()),
                        "true",
                        "Login Success");
                break;

            case "validation":
                ExtentTestManager.getTest().info("Validating field errors");
                if (data.getErrors() != null) {

                    if (data.getErrors().containsKey("email")) {
                        ValidationUtils.validateText(
                                lp.getEmailErrMsg(),
                                data.getErrors().get("email"),
                                "Email Field");
                    }

                    if (data.getErrors().containsKey("password")) {
                        ValidationUtils.validateText(
                                lp.getPasswordErrMsg(),
                                data.getErrors().get("password"),
                                "Password Field");
                    }
                }
                break;

            case "error":
                ExtentTestManager.getTest().info("Validating error toast");
            	ValidationUtils.validateToast(
            		    lp.getToastText(),
            		    data.getToast(),
            		    lp.getToastTxtColor(),
            		    lp.getToastBGColor(),
            		    lp.getToastPosition(),
            		    "Error Toast Check"
            		);

                break;

            default:
                throw new RuntimeException("Invalid test type: " + data.getType());
        }
        
    }
}