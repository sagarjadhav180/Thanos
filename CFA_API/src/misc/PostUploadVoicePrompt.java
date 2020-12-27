package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class PostUploadVoicePrompt extends BaseClass {
	
	String class_name = "PostUploadVoicePrompt";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String filePath;
	
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException    {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;
			
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;
	
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority=1)
	public void uploadVoicePromptWithValidAccessToken() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptWithValidAccessToken", "Verify success message displayed if user pass valid access token");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=2)
	public void uploadVoicePromptWithInValidAccessToken() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptWithInValidAccessToken", "Verify status code 401 if user pass invalid access token");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		test_data = HelperClass.readTestData(class_name, "uploadVoicePromptWithInValidAccessToken");
		String token = test_data.get(1);
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", token, json);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 401 , "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "status code 401 is returned");
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=3)
	public void uploadVoicePromptWithInExpiredAccessToken() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptWithInExpiredAccessToken", "Verify status code 401 if user pass expired access token");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		test_data = HelperClass.readTestData(class_name, "uploadVoicePromptWithInExpiredAccessToken");
		String token = test_data.get(1);
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", token, json);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 401 , "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "status code 401 is returned");
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=4)
	public void uploadVoicePromptForAgencyLevel() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForAgencyLevel", "Verify is user is able to upload whisper message at agency level");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=5)
	public void uploadVoicePromptForCompanyLevel() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForCompanyLevel", "Verify is user is able to upload whisper message at company level");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token_company_admin, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=6)
	public void uploadVoicePromptForLocationLevel() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForLocationLevel", "Verify is user is able to upload whisper message at location level");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token_location_admin, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=7)
	public void uploadVoicePromptForMP3FomratFile() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForMP3FomratFile", "Verify is user is able to upload whisper message file of mp3 format");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=8)
	public void uploadVoicePromptForWAVFomratFile() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForWAVFomratFile", "Verify is user is able to upload whisper message file of wav format");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getWAVFile("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=9)
	public void uploadVoicePromptForMP4FomratFile() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptForMP4FomratFile", "Verify is appropriate error message is dispalyed when user try to upload whisper message file of mp4 format");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP4File();
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if err is displayed in result");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";			   
			   Assert.assertEquals(result, exp_result,"Result did not return error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed in data field");
			   String error_message = jsonobj.get("data").toString();
			   String exp_error_message = "Incorrect File Format. It Should Be mp3/wav";
			   Assert.assertEquals(error_message, exp_error_message,"Result did not return appropriate error message");
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=10)
	public void uploadVoicePromptFor15Files() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptFor15Files", "Verify is user is able to upload 15 whisper message file at a time");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getBDXFile();
		for(int i=0;i<15;i++) {
			json.put("file_"+i, file);		
		}
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
		}	
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=11)
	public void uploadVoicePromptFor16Files() throws Exception  {
		
		test = extent.startTest("uploadVoicePromptFor16Files", "Verify is user is getting error if try to upload more than 15 whisper message file at a time");
		test.assignCategory("CFA POST /misc/upload/prompt API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getBDXFile();
		for(int i=0;i<16;i++) {
			json.put("file_"+i, file);		
		}
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/prompt", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   test.log(LogStatus.INFO, "Verifying if error is displayed in result");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result = "error";			   
			   Assert.assertEquals(result, exp_result,"Result did not return error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed in data");	   
			   String err = jsonobj.get("data").toString();
			   String exp_err = "This method only support 15 files at a Time";			   
			   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message. Error displayed is "+err);		   
		}	
	}

}
