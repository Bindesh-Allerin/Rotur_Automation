package com.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private int count = 0;
    public static final int maxRetry =  Integer.parseInt(ConfigReader.getProperty("retryCount"));

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxRetry) {
            count++;
            System.out.println("Retrying test execution: "+ result.getName()+ " | Attempt: " + count );
            return true;
        }
        return false;
    }
}
