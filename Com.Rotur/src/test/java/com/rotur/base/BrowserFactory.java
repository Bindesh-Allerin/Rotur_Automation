package com.rotur.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserFactory {

	// ThreadLocal ensures each thread has its own independent driver instance
	private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	// VERSION 1: Used in @BeforeMethod to INITIALIZE the driver
	public static WebDriver getDriver(String browser) {
		if (tlDriver.get() == null) {
			WebDriver driver;
			switch (browser.toLowerCase()) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			default:
				throw new RuntimeException("Invalid Browser: " + browser);
			}
			tlDriver.set(driver); // Correct way to store in ThreadLocal
		}
		return tlDriver.get();
	}

	// Use this in your tests or @AfterMethod to FETCH the existing driver
	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	// Properly closes the browser and removes the ThreadLocal reference
	public static void quitDriver() {
		if (tlDriver.get() != null) {
			tlDriver.get().quit();
			tlDriver.remove(); // Prevents memory leaks in parallel runs
		}
	}
}
