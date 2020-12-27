package common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.LogStatus;
 

public class Listener extends BaseClass implements ITestListener {
	static List<ITestNGMethod> passedtests = new ArrayList<ITestNGMethod>();
	static List<ITestNGMethod> failedtests = new ArrayList<ITestNGMethod>();
	static List<ITestNGMethod> skippedtests = new ArrayList<ITestNGMethod>();
	static Map<String,List<String>> failedMethodAndRequestDetails =  new HashMap<String, List<String>>();
	static Map<String,List<String>> passedMethodAndRequestDetails =  new HashMap<String, List<String>>();
	
	String failed_test_case = "";
	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		// TODO Auto-generated method stub
		failedtests.add(arg0.getMethod());
		failed_test_case = arg0.getMethod().getMethodName();
		List<String> testUrlAndParams = new ArrayList<String>();
		testUrlAndParams.add(HelperClass.uri.toString());
		testUrlAndParams.add(HelperClass.params.toString());
		failedMethodAndRequestDetails.put(failed_test_case, testUrlAndParams);
		
		System.out.println("Failed:"+ arg0.getMethod());
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		skippedtests.add(arg0.getMethod());
		System.out.println("Skipped:"+ arg0.getMethod());
		test = extent.startTest(arg0.getMethod().getMethodName());
		LogStatus sta = test.getRunStatus();
		System.out.println("Skipped Status: "+sta);
		test.log(LogStatus.SKIP, "test skipped because of test method <span class='red label'>" + failed_test_case + "</span>" + " is failed.");
		extent.endTest(test);
		extent.flush();
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		passedtests.add(arg0.getMethod());
		String passedTestCase = arg0.getMethod().getMethodName();
		List<String> testUrlAndParams = new ArrayList<String>();
		testUrlAndParams.add(HelperClass.uri.toString());
		testUrlAndParams.add(HelperClass.params.toString());
		passedMethodAndRequestDetails.put(passedTestCase, testUrlAndParams);
	}
	 
	
	
	public static int[] count_of_test(){
		int [] result = {passedtests.size(),failedtests.size(),skippedtests.size()}; 
		return result;
	}
	
	public static List<ITestNGMethod> getFailedTestCase() {
		return failedtests;
	}
	// This belongs to ISuiteListener and will execute before the Suite start
}
