package com.rotur.base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.utils.ConfigReader;
import com.utils.EmailUtil;
import com.utils.ExtentManager;
import com.utils.ExtentTestManager;
import com.utils.Retry;
import com.utils.ScreenshotUtil;
import com.utils.VisualAIUtil;

public class Base {

	protected static ExtentReports extent;

	@BeforeSuite
	public void setupReport() {
		extent = ExtentManager.getInstance();
	}

	@Parameters("browser")
	@BeforeMethod
	public void setup(Method method, @Optional("chrome") String browserXML) {

		String description = method.getAnnotation(Test.class).description();

		// Priority: Uses XML parameter if available, else falls back to Config
		String browser = (browserXML != null) ? browserXML : ConfigReader.getProperty("browser");

		// Initialize the driver via the Factory (ThreadLocal)
		WebDriver driver = BrowserFactory.getDriver(browser);

		driver.manage().window().maximize();

		System.out.println("Execution starting on Thread ID: " + Thread.currentThread().threadId());

		int time = Integer.parseInt(ConfigReader.getProperty("timeout"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));

		driver.get(ConfigReader.getProperty("url"));

		// Extent Test Creation
		ExtentTest test = extent.createTest(method.getName() + " [" + browser + "]", description);
		test.assignCategory("UI Tests");
		ExtentTestManager.setTest(test);
		ExtentTestManager.getTest().info("Test Started on browser: " + browser);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		// Get the driver instance for the current thread
		WebDriver driver = BrowserFactory.getDriver();
		ExtentTest test = ExtentTestManager.getTest();

		// 1. Check Selenium Test Status First
		if (result.getStatus() == ITestResult.FAILURE) {
			handleFailure(result, test, driver);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			// 2. Only perform Visual AI if the functional test passed
			try {
				VisualAIUtil.validate(driver, result.getName());
				test.pass("Functional & Visual Validation Passed");
			} catch (Exception e) {
				test.fail("Visual Mismatch Detected: " + e.getMessage());
				result.setStatus(ITestResult.FAILURE); // Force TestNG to mark it failed
			}
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.skip("Test Skipped");
		}

		BrowserFactory.quitDriver();
		ExtentTestManager.unload();
	}

	private void handleFailure(ITestResult result, ExtentTest test, WebDriver driver) {
		if (result.getMethod().getCurrentInvocationCount() <= Retry.maxRetry) {
			test.warning("Test failed. Retrying Execution...");
		} else {
			test.fail(result.getThrowable());
		}

		String path = ScreenshotUtil.capture(driver, result.getName());
		try {
			test.addScreenCaptureFromPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	try
//
//	{
//		VisualAIUtil.validate(driver, result.getName());
//		test.pass("Visual Validation Passed");
//	}catch(
//	Exception e)
//	{
//		test.warning("Visual Mismatch Detected");
//	}
//
//	if(result.getStatus()==ITestResult.FAILURE)
//	{
//		if (result.getMethod().getCurrentInvocationCount() <= Retry.maxRetry) {
//			test.warning("Retrying Execution....");
//		} else {
//			test.fail(result.getThrowable());
//		}
//
//		// Capture Screenshot using the thread-specific driver
//		String path = ScreenshotUtil.capture(driver, result.getName());
//		try {
//			test.addScreenCaptureFromPath(path);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}else if(result.getStatus()==ITestResult.SUCCESS)
//	{
//		test.pass("Test Passed");
//	}

	// Quit and cleanup the ThreadLocal reference
//	BrowserFactory.quitDriver();
//	}

	@AfterSuite
	public void flushReport() throws IOException {
		extent.flush();
		File reportFile = new File(ExtentManager.reportPath);

		if (Desktop.isDesktopSupported() && reportFile.exists()) {
			Desktop.getDesktop().browse(reportFile.toURI());
		}

		EmailUtil.sendReport();
	}
}
