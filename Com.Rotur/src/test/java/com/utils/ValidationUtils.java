package com.utils;

import org.testng.Assert;

public class ValidationUtils {

	public static void validateText(String actual, String expected, String field) {
		Assert.assertEquals(actual, expected, field + " mismatch | Actual: " + actual + " Expected: " + expected);
	}

	public static void validateToast(String actualMsg, String expectedMsg, String actualColor, String actualBG,
			boolean isPositionCorrect, String type) {

		Assert.assertEquals(actualMsg, expectedMsg, "Toast message mismatch");

        // Position Validation
		Assert.assertTrue(isPositionCorrect, "Toast position incorrect");

        // Color Validation
		if (type.equals("error")) {
			Assert.assertEquals(actualColor, "rgba(153, 27, 27, 1)");
			Assert.assertEquals(actualBG, "rgba(254, 242, 242, 1)");
		} else if (type.equals("success")) {
			Assert.assertEquals(actualColor, "rgba(22, 101, 52, 1)");
			Assert.assertEquals(actualBG, "rgba(240, 253, 244, 1)");
		}
	}

}
