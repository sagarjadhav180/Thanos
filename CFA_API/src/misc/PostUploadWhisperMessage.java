package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
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
public class PostUploadWhisperMessage extends BaseClass {

	String class_name = "PostUploadWhisperMessage";
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
			
//			uploadMessageFile();
	}
	
	@SuppressWarnings("unchecked")
	public void uploadMessageFile() throws ClientProtocolException, IOException, URISyntaxException, ParseException  {
		
		//Upload file
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/misc/upload/whisper", access_token, json);
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
		
		//get file path
		filePath = getMessageFilePath();
	}
	
	
	@SuppressWarnings("unchecked")
	public String getMessageFilePath() throws ClientProtocolException, IOException, URISyntaxException, ParseException  {
		
		String path = null;
		//Get file path
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String fileName = Constants.TEST_FILE_MP3.substring(0, Constants.TEST_FILE_MP3.indexOf('.'));
		nvps.add(new BasicNameValuePair("filter","file_name%3d"+fileName));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   JSONArray data_arr = (JSONArray) jsonobj.get("data");
			   JSONObject file_obj = (JSONObject) data_arr.get(0);
			   path = file_obj.get("reference_id").toString();
		}
		return path;
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=1)
	public void uploadWhisperMessageWithValidAccessToken() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageWithValidAccessToken", "Verify success message displayed if user pass valid access token");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageWithInValidAccessToken() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageWithInValidAccessToken", "Verify status code 401 if user pass invalid access token");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		test_data = HelperClass.readTestData(class_name, "uploadWhisperMessageWithInValidAccessToken");
		String token = test_data.get(1);
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", token, json);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 401 , "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "status code 401 is returned");
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=3)
	public void uploadWhisperMessageWithInExpiredAccessToken() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageWithInExpiredAccessToken", "Verify status code 401 if user pass expired access token");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		test_data = HelperClass.readTestData(class_name, "uploadWhisperMessageWithInExpiredAccessToken");
		String token = test_data.get(1);
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", token, json);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 401 , "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "status code 401 is returned");
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(priority=4)
	public void uploadWhisperMessageForAgencyLevel() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForAgencyLevel", "Verify is user is able to upload whisper message at agency level");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageForCompanyLevel() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForCompanyLevel", "Verify is user is able to upload whisper message at company level");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token_company_admin, json);
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
	public void uploadWhisperMessageForLocationLevel() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForLocationLevel", "Verify is user is able to upload whisper message at location level");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token_location_admin, json);
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
	public void uploadWhisperMessageForMP3FomratFile() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForMP3FomratFile", "Verify is user is able to upload whisper message file of mp3 format");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP3File("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageForWAVFomratFile() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForWAVFomratFile", "Verify is user is able to upload whisper message file of wav format");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getWAVFile("1mb");
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageForMP4FomratFile() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageForMP4FomratFile", "Verify is appropriate error message is dispalyed when user try to upload whisper message file of mp4 format");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getMP4File();
		json.put("file", file);
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageFor15Files() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageFor15Files", "Verify is user is able to upload 15 whisper message file at a time");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getBDXFile();
		for(int i=0;i<15;i++) {
			json.put("file_"+i, file);		
		}
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
	public void uploadWhisperMessageFor16Files() throws Exception  {
		
		test = extent.startTest("uploadWhisperMessageFor16Files", "Verify is user is getting error if try to upload more than 15 whisper message file at a time");
		test.assignCategory("CFA POST /misc/upload/whisper API");
		
		JSONObject json = new JSONObject();
		String file=FileConstants.getBDXFile();
		for(int i=0;i<16;i++) {
			json.put("file_"+i, file);		
		}
		
		CloseableHttpResponse response = HelperClass.make_post_request_message_upload("/v2/misc/upload/whisper", access_token, json);
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
