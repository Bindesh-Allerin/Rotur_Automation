package com.rotur.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.rotur.base.Base;
import com.rotur.pages.LoginPage;
import com.testData.LoginData;
import com.utils.JsonUtils;
import com.utils.ValidationUtils;
import com.utils.Retry;
import com.utils.ExtentTestManager;


public class RoturLogin extends Base {

    @DataProvider(name = "loginData")
    public Object[] getData() {
        return JsonUtils
                .getLoginData("src/test/resources/testdata/login.json")
                .toArray();
    }

    @Test(description = "Verify Login Functionality", dataProvider = "loginData", retryAnalyzer = Retry.class)
    public void RoturLoginPage(LoginData data) {

        System.out.println("Executing Test → " + data.toString());

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
                                "Email");
                    }

                    if (data.getErrors().containsKey("password")) {
                        ValidationUtils.validateText(
                                lp.getPasswordErrMsg(),
                                data.getErrors().get("password"),
                                "Password");
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
            		    "error"
            		);

                break;

            default:
                throw new RuntimeException("Invalid test type: " + data.getType());
        }
        
        ExtentTestManager.getTest().pass("Login Successful");
        ExtentTestManager.getTest().fail("Login Failed");
    }
}