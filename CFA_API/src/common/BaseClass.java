package common;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.convirza.constants.TestCaseConstants;
import com.convirza.tests.base.EnvionmentHelper;
import com.convirza.tests.base.TestDataCleanup;
import com.convirza.tests.base.TestDataPreparation;
import com.convirza.tests.core.io.TestCaseResultWriter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseClass {

	public static String access_token = "", expired_access_token = "1718ac57-e54e-45b8-8455-6e31bdd34d3e", invalid_access_token = "dsfdsf"; 
	public static ExtentReports extent;
	public static ExtentTest test;
	
	
	@BeforeSuite
	public void setup() throws Exception {
		 // Create the extentReport
         FileUtils.createDir("//Result");

		 extent = new ExtentReports(System.getProperty("user.dir") + "/Result/cfa_api_report.html", true);
		 extent.addSystemInfo("User Name", "Convirza");
		 extent.addSystemInfo("OS", "Windows");
		 extent.addSystemInfo("Host Name", "convirza");
		 extent.loadConfig(new File(Directory.getCommonDir() + "extent_config.xml"));
		 
		 EnvionmentHelper envionmentHelper = new EnvionmentHelper();
		 envionmentHelper.updateEnvironmentConfigs();
		 envionmentHelper.updateTestDataWithConfig();
		 getAccessToken();
//		 TestDataPreparation testDataPrepare = new TestDataPreparation();
//		 testDataPrepare.createTestData();

	}
	
	public void getAccessToken() throws ClientProtocolException, URISyntaxException, IOException, ParseException{
		 test = extent.startTest("get_access_token", "Get the access_token from outh/token api");
		 // Get the access token
		 ArrayList<String> get_credential = new ArrayList<String>();
		 get_credential.add("agency_admin_email");
		 get_credential.add("agency_admin_password");
		 ArrayList<String> config = HelperClass.read_config(get_credential);
		 access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		 test.log(LogStatus.PASS, "Get the access token from access_token api");
		 test.log(LogStatus.INFO, "Fetched access_token: "+access_token);
	}
	
	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result) {
		try {
			if(result.getStatus() == ITestResult.FAILURE){
				//Code to add failure message in report	
				test.log(LogStatus.FAIL, result.getThrowable().getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Code to add test result in report
		extent.endTest(test);
		extent.flush();
	}
	
	@AfterSuite
	public void send_report() throws Exception {
		// Get count of passed, failed and skipped test case
		int [] result = Listener.count_of_test();
		TestCaseResultWriter testCaseResultWriter = new TestCaseResultWriter(TestCaseConstants.Status.PASS);
		testCaseResultWriter.addTestCasesToReport(Listener.passedtests,Listener.passedMethodAndRequestDetails);
		
		testCaseResultWriter = new TestCaseResultWriter(TestCaseConstants.Status.FAIL);
		testCaseResultWriter.addTestCasesToReport(Listener.failedtests,Listener.failedMethodAndRequestDetails);
		
		// HelperClass.writeExcel(result);
		try {
//			TestDataCleanup cleanUpObj = new TestDataCleanup();
//			cleanUpObj.cleanup();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SendEmail.execute("cfa_api_report.html", result);
		}

	}
}