

import framework.core.*;
import framework.models.TestCase;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {

        WebDriver driver = DriverFactory.createDriver("local");
        WebExecutor web = new WebExecutor(driver);
        KeywordExecutor executor = new KeywordExecutor(web);

        List<TestCase> list = new TestCaseReader().loadTestCases("testcases.xlsx");

        for (TestCase tc : list) {
            System.out.println("Executing: " + tc.getTestCaseId());
            tc.getSteps().forEach(executor::executeStep);
        }

        driver.quit();
    }
}
