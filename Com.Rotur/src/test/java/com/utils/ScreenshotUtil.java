package com.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.*;

public class ScreenshotUtil {

	public static String capture(WebDriver driver, String name) {

		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String path = System.getProperty("user.dir") + "/test-output/screenshots/failures/" + Thread.currentThread().threadId() + ".png";
		try {
			FileUtils.copyFile(src, new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
}
