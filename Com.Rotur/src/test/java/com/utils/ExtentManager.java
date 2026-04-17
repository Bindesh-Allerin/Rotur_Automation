package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;
    public static String reportPath;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String baseDir = System.getProperty("user.dir") + "/test-output/reports/";
            String timestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
            
            // 1. Dynamic path for history
            reportPath = baseDir + "ExtentReport_" + timestamp + ".html";
            // 2. Fixed path for Jenkins
            String latestReport = baseDir + "ExtentReport.html";

            ExtentSparkReporter dynamicSpark = new ExtentSparkReporter(reportPath);
            ExtentSparkReporter jenkinsSpark = new ExtentSparkReporter(latestReport);

            // Create a method or apply config to both to keep them identical
            configureReporter(dynamicSpark);
            configureReporter(jenkinsSpark);

            extent = new ExtentReports();
            // Attach both: Jenkins will always overwrite the 'latest', Dynamic will create a new file
            extent.attachReporter(dynamicSpark, jenkinsSpark);
            
            // System Info (Applied to the 'extent' object, so it goes to both reporters)
            extent.setSystemInfo("Project", "Rotur - Release Management");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Author", System.getProperty("user.name"));
            extent.setSystemInfo("Browser Version", ConfigReader.getProperty("browser"));
            extent.setSystemInfo("Environment", "Production: " + ConfigReader.getProperty("url"));
        }
        return extent;
    }

    // Helper method to ensure both reports look exactly the same
    private static void configureReporter(ExtentSparkReporter spark) {
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setReportName("Rotur Test Automation Report");
        spark.config().setDocumentTitle("Test Execution Report");
        spark.config().setCss("div.col-md-3 img { border: 2px solid #777 !important; box-shadow: 2px 2px 8px #aaa; margin: 10px 0; } .featherlight-content img { border: 2px solid #777 !important; }");
    }
}
