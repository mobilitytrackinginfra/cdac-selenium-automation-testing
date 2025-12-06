package com.espocrm.utils;

import com.aventstack.extentreports.ExtentTest;

/**
 * Holds ExtentTest in a ThreadLocal so that parallel tests
 * each get their own ExtentTest instance.
 */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> tlTest = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static ExtentTest getTest() {
        return tlTest.get();
    }

    public static void setTest(ExtentTest test) {
        tlTest.set(test);
    }

    public static void unload() {
        tlTest.remove();
    }
}
