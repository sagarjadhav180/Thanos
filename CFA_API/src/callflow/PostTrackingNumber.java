package callflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONString;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.base.TestDataPreparation;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.pojo.request.success.NumberPool;
import com.convirza.tests.pojo.request.success.OverflowNumbers;
import com.convirza.tests.pojo.request.success.PhoneNumber;
import com.convirza.utils.TNUtil;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class PostTrackingNumber extends BaseClass{

	String class_name = "PostTrackingNumber";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String voice_prompt_file;
	String whisper_message_file;
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException    {
		
//		Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
//		String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
//		String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
//		access_token_company_admin=response;
			
//		Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
//		String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
//		String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
//		access_token_location_admin=response1;
		
		//fetching audio files
		String[] types = {"prompt","whisper"};
		for(String type:types) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("filter","type%3d"+type));
			CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
			Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
			String line = "";    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String success = json.get("result").toString();
				   test.log(LogStatus.INFO, "Verifying if success message is displayed");
				   Assert.assertTrue(success.equals("success"),"api did not retun success");
				   test.log(LogStatus.INFO, "Verifying if err is null");
				   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
				   JSONArray data_Arr = (JSONArray) json.get("data");
				   JSONObject data_obj = (JSONObject) data_Arr.get(0);
				   if(type.equals("prompt"))
				      voice_prompt_file = data_obj.get("reference_id").toString();
				   else if(type.equals("whisper"))
					   whisper_message_file = data_obj.get("reference_id").toString();
			}	
		}
		
	}

	
	@Test(priority=1)
	public void PostCallFlowForSimpleRouteByAgencyAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForSimpleRouteByAgencyAdmin", "To validate whether agency level admin user is able to create callflow of route type Simple");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=2)
	public void PostCallFlowForIvrRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForIvrRoute", "To validate whether agency level admin user is able to create call flow of route type IVR");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr", 2, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) parser.parse(line);
			String result_data = jsonResponse.get("result").toString();
			test.log(LogStatus.INFO, "Verifying if success is returned in result");
			Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			test.log(LogStatus.INFO, "Verifying if err is null");
			Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}
	
	}
	
	
	@Test(priority=3)
	public void PostCallFlowForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {

		test = extent.startTest("PostCallFlowForIvrRoute", "To validate whether agency level admin user is able to create call flow of route Number pool");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool", 2, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) parser.parse(line);
			String result_data = jsonResponse.get("result").toString();
			test.log(LogStatus.INFO, "Verifying if success is returned in result");
			Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			test.log(LogStatus.INFO, "Verifying if err is null");
			Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}
	}
	
	
//	@Test(priority=4)
	public void PostCallFlowForPercent() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForIvrRoute", "To validate whether agency level admin user is able to create call flow of route Percent");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"percentage", 2, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) parser.parse(line);
			String result_data = jsonResponse.get("result").toString();
			test.log(LogStatus.INFO, "Verifying if success is returned in result");
			Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			test.log(LogStatus.INFO, "Verifying if err is null");
			Assert.assertNull(jsonResponse.get("err"),"API did not return null error");
		}
	}
	
	
//	@Test(priority=5)
	public void PostCallFlowForGeo() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"geo", 2, "unique", "post");
		CloseableHttpResponse response;

		    response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) parser.parse(line);
				String result_data = jsonResponse.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
//				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
	}

	
	@Test(priority=6)
	public void PostCallFlowForSimpleRouteForMandatoryParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForSimpleRouteForMandatoryParams", "To validate whether agency level admin user is able to create callflow of route type Simple with mandatory paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForSimpleRouteForMandatoryParams");
		String[] fields = test_data.get(2).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=7)
	public void PostCallFlowForSimpleRouteForOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForSimpleRouteForOptionalParams", "To validate whether agency level admin user is not able to create callflow of route type Simple with optional paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForSimpleRouteForOptionalParams");
		String[] fields = test_data.get(1).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String message = jsonResponse.get("message").toString();
		   String exp_message = "Validation errors";
		   test.log(LogStatus.INFO, "Verifying if validation error is returned in result");
		   Assert.assertEquals(message, exp_message, "API is returning validation error while creating callflow. API response: " + line);
		   
		}	
	}

	
	@Test(priority=8)
	public void PostCallFlowForSimpleRouteForMandatoryAndOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForSimpleRouteForMandatoryParams", "To validate whether agency level admin user is able to create callflow of route type Simple with mandatory and optional paramters");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=9)
	public void PostCallFlowForNumberPoolWithMandatoryParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForNumberPoolWithMandatoryParams", "To validate whether agency level admin user is able to create callflow of route type Number pool with mandatory paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForNumberPoolWithMandatoryParams");
		String[] fields = test_data.get(2).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}

	
	@Test(priority=10)	
	public void PostCallFlowForNumberPoolForOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForNumberPoolForOptionalParams", "To validate whether agency level admin user is not able to create callflow of route type Number Pool with optional paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForNumberPoolForOptionalParams");
		String[] fields = test_data.get(1).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String message = jsonResponse.get("message").toString();
		   String exp_message = "Validation errors";
		   test.log(LogStatus.INFO, "Verifying if validation error is returned in result");
		   Assert.assertEquals(message, exp_message, "API is returning validation error while creating callflow. API response: " + line);
		   
		}	
	}

	
	@Test(priority=11)
	public void PostCallFlowForNumberPoolForMandatoryAndOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForNumberPoolForMandatoryAndOptionalParams", "To validate whether agency level admin user is able to create callflow of route type Number pool with mandatory and optional paramters");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=12)
	public void PostCallFlowForIVRWithMandatoryParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForIVRWithMandatoryParams", "To validate whether agency level admin user is able to create callflow of route type IVR with mandatory paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForIVRWithMandatoryParams");
		String[] fields = test_data.get(2).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=13)	
	public void PostCallFlowForIVRWithOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForIVRWithOptionalParams", "To validate whether agency level admin user is not able to create callflow of route type IVR with optional paramters only");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowForIVRWithOptionalParams");
		String[] fields = test_data.get(1).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String message = jsonResponse.get("message").toString();
		   String exp_message = "Validation errors";
		   test.log(LogStatus.INFO, "Verifying if validation error is returned in result");
		   Assert.assertEquals(message, exp_message, "API is returning validation error while creating callflow. API response: " + line);		   
		}	
	}
	
	
	@Test(priority=14)
	public void PostCallFlowForIVRWithMandatoryAndOptionalParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowForIVRWithMandatoryAndOptionalParams", "To validate whether agency level admin user is able to create callflow of route type IVR with mandatory and optional paramters");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=15)	
	public void PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForSimpleRoute", "To validate whether agency level admin user is not able to create callflow of route type Simple with isSimultaneousTrue And overflow_numbers Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForSimpleRoute");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
	@Test(priority=16)	
	public void PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForNumberPool", "To validate whether agency level admin user is not able to create callflow of route type Simple with isSimultaneousTrue And overflow_numbers Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithSimultaneousTrueAndOverflowObjectMissingForNumberPool");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
//	@Test(priority=17)	issue logged -- need to change exp_err_message
	public void PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForSimpleRoute", "To validate whether agency level admin user is not able to create callflow of route type Simple with postcallIvrEnabled true And postcallIvrId Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForSimpleRoute");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
//	@Test(priority=18)	issue logged --need to change exp_err_message
	public void PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForIvrRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForIvrRoute", "To validate whether agency level admin user is not able to create callflow of route type IVR with postcallIvrEnabled true And postcallIvrId Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForIvrRoute");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
//	@Test(priority=19)	issue logged --  need to change exp_err_message
	public void PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForNumberPool", "To validate whether agency level admin user is not able to create callflow of route type number pool with postcallIvrEnabled true And postcallIvrId Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithPostCallIvrTrueAndPostCallIvrIdMissingForNumberPool");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
//	@Test(priority=20)	-- getting 500
	public void PostCallFlowWithMessageParameerMissingForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithMessageParameerMissingForSimpleRoute", "To validate whether agency level admin user is not able to create callflow of route type simple with message_enabled true And message Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithMessageParameerMissingForSimpleRoute");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}	
	
	
//	@Test(priority=21)	-- getting 500
	public void PostCallFlowWithMessageParameerMissingForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithMessageParameerMissingForNumberPool", "To validate whether agency level admin user is not able to create callflow of route type number pool with message_enabled true And message Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithMessageParameerMissingForNumberPool");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}

	
//	@Test(priority=22) -- getting 500
	public void PostCallFlowWithWhisperMessageParameerMissingForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithWhisperMessageParameerMissingForSimpleRoute", "To validate whether agency level admin user is not able to create callflow of route type simple with whisper_enabled true And whisper_message Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithWhisperMessageParameerMissingForSimpleRoute");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}

	
//	@Test(priority=23) -- getting 500
	public void PostCallFlowWithWhisperMessageParameerMissingForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithWhisperMessageParameerMissingForNumberPool", "To validate whether agency level admin user is not able to create callflow of route type number pool with whisper_enabled true And whisper_message Object missing");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		test_data = HelperClass.readTestData(class_name, "PostCallFlowWithWhisperMessageParameerMissingForNumberPool");
		String[] fields = test_data.get(3).split(",");
		callflowReq = TNUtil.removeParams(callflowReq, fields);
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "overflowNumbers field is required. Make sure you typed it as 'overflowNumbers'";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}
	
	
	@Test(priority=24) 
	public void PostCallFlowWithDuplicateOverflowNumbersForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithDuplicateOverflowNumbersForSimpleRoute", "To validate whether agency level admin user is not able to create callflow of route type simple route with isSimultaneous true And duplicate overflow numbers");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "duplicate", "post");;
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "Overflow Numbers cannot contain duplicate numbers with Simultaneous hunt.";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}

	
	@Test(priority=25) 
	public void PostCallFlowWithDuplicateOverflowNumbersForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithDuplicateOverflowNumbersForNumberPool", "To validate whether agency level admin user is not able to create callflow of route type number pool route with isSimultaneous true And duplicate overflow numbers");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "duplicate", "post");
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result = jsonResponse.get("result").toString();
		   String exp_result = "error";
		   test.log(LogStatus.INFO, "Verifying if error is returned in result");
		   Assert.assertEquals(result, exp_result, "API is not returning error while creating callflow. API response: " + line);
		   String error = jsonResponse.get("err").toString();
		   String exp_error = "Overflow Numbers cannot contain duplicate numbers with Simultaneous hunt.";
		   test.log(LogStatus.INFO, "Verifying if appropriate error is returned");
		   Assert.assertEquals(error, exp_error, "API is not returning appropriate error while creating callflow. API response: " + line);		   
		}	
	}

	
	@Test(priority=26) 
	public void PostCallFlowWithDuplicateOverflowNumbersSimultaneousFalseForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithDuplicateOverflowNumbersSimultaneousFalseForSimpleRoute", "To validate whether agency level admin user is able to create callflow of route type simple route with isSimultaneous false And duplicate overflow numbers");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "duplicate", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("isSimultaneous",false);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=27) 
	public void PostCallFlowWithDuplicateOverflowNumbersSimultaneousFalseForNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithDuplicateOverflowNumbersSimultaneousFalseForNumberPool", "To validate whether agency level admin user is able to create callflow of route type number pool route with isSimultaneous false And duplicate overflow numbers");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "duplicate", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("isSimultaneous",false);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}

	
	@Test(priority=28) 
	public void PostCallFlowWithExternalAudioFileVPSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileVPSimpleRoute", "To validate whether agency level admin user is able to create callflow of route type simple route with external audio file in voice prompt and text in whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("message",voice_prompt_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=29) 
	public void PostCallFlowWithExternalAudioFileWhisperSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileWhisperSimpleRoute", "To validate whether agency level admin user is able to create callflow of route type simple route with text in voice prompt and audio file in whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("whisper_message",whisper_message_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}


	@Test(priority=30) 
	public void PostCallFlowWithExternalAudioFileWhisperAndVPSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileWhisperAndVPSimpleRoute", "To validate whether agency level admin user is able to create callflow of route type simple route with audio file in voice prompt and whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("whisper_message",whisper_message_file);}});
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("message",voice_prompt_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}

	
	@Test(priority=31) 
	public void PostCallFlowWithExternalAudioFileVPNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileVPNumberPool", "To validate whether agency level admin user is able to create callflow of route type number pool route with external audio file in voice prompt and text in whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("message",voice_prompt_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=32) 
	public void PostCallFlowWithExternalAudioFileWhisperNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileWhisperNumberPool", "To validate whether agency level admin user is able to create callflow of route type number pool route with text in voice prompt and audio file in whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("whisper_message",whisper_message_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}

	
	@Test(priority=33) 
	public void PostCallFlowWithExternalAudioFileWhisperAndVPNumberPool() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileWhisperAndVPNumberPool", "To validate whether agency level admin user is able to create callflow of route type number pool route with audio file in voice prompt and whisper message");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("whisper_message",whisper_message_file);}});
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("message",voice_prompt_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
	@Test(priority=34) 
	public void PostCallFlowWithExternalAudioFileVpIVR() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostCallFlowWithExternalAudioFileVpIVR", "To validate whether agency level admin user is able to create callflow of route type IVR route with audio file in voice prompt");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "post");
		callflowReq = TNUtil.overRideTemplate(callflowReq, new HashMap<String,Object>(){{put("message",voice_prompt_file);}});
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}
	
	
}
