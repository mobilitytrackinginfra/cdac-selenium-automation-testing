package test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestPage {

	private static ExtentReports extent;
	
	public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }
	
	public static ExtentReports createInstance() {

        ExtentSparkReporter spark =
                new ExtentSparkReporter("reports/AutomationReport.html");

        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Selenium Execution - Extent Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "Swapnil");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("OS", System.getProperty("os.name"));

        return extent;
    }
	
	public static void main(String[] args) {
		ExtentReports report = getInstance();
		ExtentTest test = report.createTest("Test1");
		test.info("Test Started");
		test.fail("Network is Slow");
		test.pass("Done");
		extent.flush();
		
		test = report.createTest("Test2");
		test.info("Test Started");
		test.info("Network is Slow");
		test.pass("Done");
		
		extent.flush();
	}
}
