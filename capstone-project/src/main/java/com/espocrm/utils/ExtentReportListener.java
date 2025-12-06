package com.espocrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.*;

public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;

    @Override
    public void onStart(ISuite suite) {
        extent = ExtentManager.getExtentReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        String testName = (description == null || description.isEmpty())
                ? methodName
                : methodName + " - " + description;

        ExtentTest test = extent.createTest(testName);
        ExtentTestManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed");
        }
        ExtentTestManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            String base64 = ScreenshotUtils.captureBase64();
            if (base64 != null) {
                try {
                    test.fail("Final failure screenshot",
                            MediaEntityBuilder
                                    .createScreenCaptureFromBase64String(base64, "Failure")
                                    .build());
                } catch (Exception e) {
                    test.warning("Failed to attach failure screenshot: " + e.getMessage());
                }
            }
        }
        ExtentTestManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getSkipCausedBy());
        }
        ExtentTestManager.unload();
    }

//    @Override public void onStart(ITestContext context) {}
//    @Override public void onFinish(ITestContext context) {}
}
