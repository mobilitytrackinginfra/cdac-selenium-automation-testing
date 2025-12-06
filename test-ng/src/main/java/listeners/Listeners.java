package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listeners implements ITestListener {

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("On Test Case Finish");
	}
	
	@Override
	public void onStart(ITestContext context) {
		System.out.println("On Test Case Start");
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("OnTestMethodSuccess:"+result.getName());
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("OnTestMethodFailure");
	}
	
	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("OnTestMethodStart:"+result.getName());
	}
	
}
