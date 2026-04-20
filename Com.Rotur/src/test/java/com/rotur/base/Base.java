
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

	public WebDriver driver;
	protected static ExtentReports extent;

	// Report Setup (ONCE)
	@BeforeSuite
	public void setupReport() {
		extent = ExtentManager.getInstance();
	}

	// Browser + Test Setup
	@BeforeMethod
	public void setup(Method method) {

		String description = method.getAnnotation(Test.class).description();

		String browser = ConfigReader.getProperty("browser");
		driver = BrowserFactory.getDriver(browser);

		driver.manage().window().maximize();

		int time = Integer.parseInt(ConfigReader.getProperty("timeout"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));

		driver.get(ConfigReader.getProperty("url"));

		System.out.println("Browser Launched");

		// Extent Test Creation
		ExtentTest test = extent.createTest(method.getName(), description);
		test.assignCategory(method.getName() + "UI Tests");
		ExtentTestManager.setTest(test);
		ExtentTestManager.getTest().info("Test Started");
	}

	// Tear Down + Reporting
	@AfterMethod
	public void tearDown(ITestResult result) {

		ExtentTest test = ExtentTestManager.getTest();

		try {
			VisualAIUtil.validate(driver, result.getName());
			test.pass("Visual Validation Passed");
		} catch (Exception e) {
			test.warning("Visual Mismatch Detected");
		}

		if (result.getStatus() == ITestResult.FAILURE) {

			if (result.getMethod().getCurrentInvocationCount() <= Retry.maxRetry) {
				ExtentTestManager.getTest().warning("Retrying Exceution....");
			} else {
				test.fail(result.getThrowable());
			}

			// Screenshot
			String path = ScreenshotUtil.capture(driver, result.getName());
			try {
				test.addScreenCaptureFromPath(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Test Passed");
		}

		else if (result.getStatus() == ITestResult.SKIP) {
			test.skip("Test Skipped");
		}

		driver.quit();
	}

	// Flush Report (END)
	@AfterSuite
	public void flushReport() throws IOException {
		
		extent.flush();
		// Create a File object using the dynamic path saved earlier
		File reportFile = new File(ExtentManager.reportPath);

		// Automatically open the EXACT report just generated
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(reportFile.toURI());
		}
		
		//Call Email Reporting
		EmailUtil.sendReport();

	}
}
