package listeners;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

public class CustomReporter implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		for(ISuite suite: suites) {
			Map<String, ISuiteResult> results = suite.getResults();
			for(Entry<String, ISuiteResult> res : results.entrySet()) {
				System.out.println(res.getKey());
				ITestContext context = res.getValue().getTestContext();
				System.out.println("Passed: "+context.getPassedTests().getAllMethods().size());
				System.out.println("Failed: "+context.getFailedTests().size());
				System.out.println("Skipped: "+context.getSkippedTests().size());
			}
		}
	}
}
