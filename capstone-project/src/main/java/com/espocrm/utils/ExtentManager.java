package com.espocrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Singleton-style manager for ExtentReports.
 */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            // Report output path
            String reportPath = "test-output/EspoCRM-ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("EspoCRM Automation Report");
            spark.config().setReportName("UI Regression Suite");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // You can add system info here if you want
            extent.setSystemInfo("Project", "EspoCRM");
            extent.setSystemInfo("Tester", "Swapnil");
        }
        return extent;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
