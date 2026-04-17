package com.utils;

import java.io.File;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;

public class VisualAIUtil {

	public static void validate(WebDriver driver, String testName) {

		String base = System.getProperty("user.dir");
		String baseline = base + "/test-output/screenshots/baseline/" + testName + ".png";
		String Current = base + "/test-output/screenshots/current/" + testName + ".png";
		String diff = base + "/test-output/screenshots/diff/" + testName + ".png";

		ScreenshotUtil.capture(driver, "current/" + testName);
		File baseFile = new File(baseline);

		if (!baseFile.exists()) {
			ScreenshotUtil.capture(driver, "baseline/" + testName);
		} else {
			double diffPercent = ImageComparatorUtil.compare(baseline, Current, diff);
			if (diffPercent > 2.0) {
				// throw new AssertionError("UI Mismatch:" +diffPercent +"%");
				// 1. Create the HTML table for side-by-side view with borders
				String comparisonTable = "<table style='width:100%; border:1px solid #ddd; border-collapse: collapse;'>"
						+ "<tr>"
						+ "<th style='border:1px solid #ddd; padding:8px; background-color: #f2f2f2;'>Expected (Baseline)</th>"
						+ "<th style='border:1px solid #ddd; padding:8px; background-color: #f2f2f2;'>Actual (Current)</th>"
						+ "</tr>" + "<tr>" + "<td style='border:1px solid #ddd; padding:10px; text-align:center;'>"
						+ "<img src='../../baseline/" + testName
						+ ".png' style='width:350px; border:2px solid #777;' />" + "</td>"
						+ "<td style='border:1px solid #ddd; padding:10px; text-align:center;'>"
						+ "<img src='../../current/" + testName + ".png' style='width:350px; border:2px solid red;' />"
						+ "</td>" + "</tr>" + "</table>" + "<p style='color:red;'><b>Difference Percentage: "
						+ diffPercent + "%</b></p>";

				// 2. Log the comparison to the report
				ExtentTestManager.getTest().log(Status.FAIL, "Visual Validation Failed for: " + testName);
				ExtentTestManager.getTest().info(comparisonTable);

				// 3. Fail the test
				throw new AssertionError("UI Mismatch: " + diffPercent + "%");
			} else {
				ExtentTestManager.getTest()
						.pass("Visual Validation Passed for: " + testName + " (Diff: " + diffPercent + "%)");
			}
		}
	}
}
