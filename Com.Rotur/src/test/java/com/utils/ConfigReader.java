package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	static Properties prop;

	// Use a static block instead of a constructor
	static {
		try {
			FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
			// Important: handle the case where the file is missing
			throw new RuntimeException("Could not load config.properties file at src/test/resources/config.properties");
		}
	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
}