package webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.convirza.tests.helper.CreateComponents;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.convirza.tests.helper.CreateComponents;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class GetWebhook extends BaseClass{
	String class_name = "GetWebhook";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
  @Test(priority=1)
	public void get_webhook_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		// Execute webhook api method without access_token
		test = extent.startTest("get_webhook_without_access_token", "To validate whether user is able to get webhook through webhook api without access_token");
		test.assignCategory("CFA GET /webhook API");
		test.log(LogStatus.INFO, "Execute webhook api method without access_token");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/webhook", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "No access token is passed, 401 status error found");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when Authorization token is not passed");
		test.log(LogStatus.PASS, "Check status message when Authorization token is not passed");
	}
	

  @Test(priority=2)
	public void get_webhook_with_blank_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_webhook_with_blank_access_token", "To validate whether user is able to get webhook through webhook api with blank access_token");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_blank_access_token");
		String blank_access_token = ""; 
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId",test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", blank_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 400, "No access token is passed, 400 status error is not found");
		test.log(LogStatus.PASS, "Check status code when blank access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Bad Request", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when blank access_token is passed");
	}
	
  @Test(priority=3)
	public void get_webhook_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_webhook_with_invalid_token", "To validate whether user is able to get webhook through webhook api with invalid access_token");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_invalid_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId",test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	

  @Test(priority=4)
	public void get_webhook_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_webhook_with_expired_token", "To validate whether user is able to get webhook through webhook api with expired access_token");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_expired_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId",test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
    

  @Test(priority=5)
	public void get_webhook_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with valid access_token
		test = extent.startTest("get_webhook_with_valid_access_token", "To validate whether user is able to get webhooks through webhook api with valid token");
		test.assignCategory("CFA GET /webhook API");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId",groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with valid access_token");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when valid access_token is passed.");
		   JSONArray webhook_data = (JSONArray)json.get("data");
		   for(int i=0; i<webhook_data.size(); i++){
			   JSONObject webhook = (JSONObject)webhook_data.get(i);
			   Assert.assertTrue(webhook.containsKey("webhook_id"), "webhook_id field is not present in response.");
			   Assert.assertTrue(webhook.containsKey("webhook_name"), "webhook_name field is not present in response.");
			   Assert.assertTrue(webhook.containsKey("target_url"), "target_url field is not present in response.");
			   
			   HelperClass.multiple_assertnotEquals(webhook.get("webhook_id"), "webhook_id");
			   HelperClass.multiple_assertnotEquals(webhook.get("webhook_name"), "webhook_name");
			   HelperClass.multiple_assertnotEquals(webhook.get("target_url"), "target_url");
			   
			   Assert.assertEquals(webhook.get("webhook_id").getClass().getName(), "java.lang.Long");
			   HelperClass.multiple_assertnotEquals(webhook.get("webhook_name").getClass().getName(), "java.lang.String");
			   HelperClass.multiple_assertnotEquals(webhook.get("target_url").getClass().getName(), "java.lang.String");
		   }
		   
		   test.log(LogStatus.PASS, "To check whether webhook_id is present in response.");
		   test.log(LogStatus.PASS, "To check whether webhook_name is present in response.");
		   test.log(LogStatus.PASS, "To check whether target_url is present in response.");
		}
	}
	

  @Test(priority=6)
	public void get_webhook_without_passing_ouid_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api without passing id parameter
		test = extent.startTest("get_webhook_without_passing_ouid_parameter", "To validate whether user is able to get webhooks through webhook api without passing id parameter.");
		test.assignCategory("CFA GET /webhook API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method without passing ouId parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when ouId parameter is not passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when ouId parameter is not passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when ouId parameter is not passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (ouId): Value is required but was not provided", "Invalid message value is returned in response when ouId parameter is not passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "ouId", "Invalid name value is returned in response when ouId parameter is not passed");
			JSONArray expected_path_values = new JSONArray();
			expected_path_values.add("paths");
			expected_path_values.add("/webhook");
			expected_path_values.add("get");
			expected_path_values.add("parameters");
			expected_path_values.add("0");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertEquals(error_path, expected_path_values, "path is invalid when ouId parameter is not passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "REQUIRED", "Invalid code value is returned in response when ouId parameter is not passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value is required but was not provided", "Invalid message value is returned in response when ouId parameter is not passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path, expected_path_values,"path is invalid when ouId parameter is not passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when ouId parameter is not passed.");
		}
	}	
    
   //@Test(priority=7)
	public void get_webhook_with_blank_ouid_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with blank id parameter
		test = extent.startTest("get_webhook_with_blank_ouid_parameter", "To validate whether user is able to get webhooks through webhook api when blank id is passed.");
		test.assignCategory("CFA GET /webhook API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method when blank ouId parameter is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank ouId parameter is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank ouId parameter is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank ouId parameter is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (ouId): Expected type integer but found type string", "Invalid message value is returned in response when blank ouId parameter is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "ouId", "Invalid name value is returned in response when blank ouId parameter is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank ouId parameter is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank ouId parameter is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank ouId parameter is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(),"path is not blank when blank ouId parameter is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank ouId parameter is passed.");
		}
	}
    

  @Test(priority=8)
	public void get_webhook_with_invalid_ouid_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with invalid id parameter
		test = extent.startTest("get_webhook_with_invalid_ouid_parameter", "To validate whether user is able to get webhooks through webhook api when invalid id is passed.");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_invalid_ouid_parameter");
		String[] webhook_ids = test_data.get(1).split(",");
		for(String webhook_id:webhook_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", webhook_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute webhook api method when invalid("+webhook_id+") id parameter is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+webhook_id+") id parameter is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+webhook_id+") id parameter is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+webhook_id+") id parameter is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (ouId): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+webhook_id+") id parameter is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "ouId", "Invalid name value is returned in response when invalid("+webhook_id+") id parameter is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid("+webhook_id+") id parameter is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+webhook_id+") id parameter is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+webhook_id+") id parameter is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(),"path is not blank when invalid("+webhook_id+") id parameter is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+webhook_id+") id parameter is passed.");
			}
		}
	}
    

  //@Test(priority=9)
	public void get_webhook_with_negative_ouid_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with negative id parameter
		test = extent.startTest("get_webhook_with_negative_ouid_parameter", "To validate whether user is able to get webhooks through webhook api when negative id is passed.");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_negative_ouid_parameter");
		int webhook_id = Integer.parseInt(test_data.get(1));
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", Integer.toString(webhook_id)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method when negative("+webhook_id+") id parameter is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when negative id parameter is passed.");
			test.log(LogStatus.PASS, "Check API returns error when negative id parameter is passed.");
			String error_data = json.get("err").toString();
			Assert.assertEquals(error_data, "You don't have access to this org_unit_Id.", "Invalid validation is displayed when negative id parameter is passed.");
			test.log(LogStatus.PASS, "Check API returns proper validation when negative id parameter is passed.");
		}
	}    
    

  //@Test(priority=10)
	public void get_webhook_with_valid_ouid_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with valid id parameter
		test = extent.startTest("get_webhook_with_valid_ouid_parameter", "To validate whether user is able to get webhooks through webhook api with valid id parameter.");
		test.assignCategory("CFA GET /webhook API");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		int ouid = Integer.parseInt(groupId);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", Integer.toString(ouid)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with valid ouid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when valid access_token is passed.");
		   JSONArray webhook_data = (JSONArray)json.get("data");
		   Assert.assertTrue(webhook_data.size()>0, "Data field is empty");
		}
	}
    

//  @Test(priority=11) -- Uncomment when defect will be fixed
	public void get_webhook_with_deleted_ouid() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with deleted ouid parameter
		test = extent.startTest("get_webhook_with_deleted_ouid", "To validate whether user is able to get webhook through webhook api with deleted ouid parameter.");
		test.assignCategory("CFA GET /webhook API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		groupId = DBGroupUtils.getGroupIdByStatus(groupId, "deleted");
		int ouid = Integer.parseInt(groupId);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", Integer.toString(ouid)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with deleted ouid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API retuns success when deleted ouid parameter. <b style='color:red'>Defect Reported: CT-17996</b>");
		   Assert.assertNull(json.get("err"), "err data is not null when deleted ouid parameter.");
		}
	} 
    
  @Test(priority=12) // Need to add validation
	public void get_webhook_with_ouid_having_no_webhook() throws URISyntaxException, ClientProtocolException, IOException, ParseException{

		// Execute webhook api with ouid which have no webhook
		test = extent.startTest("get_webhook_with_ouid_having_no_webhook", "To validate whether user is able to get webhook through webhook api with ouid which have no webhook.");
		test.assignCategory("CFA GET /webhook API");
	  String groupId = CreateComponents.createGroup(Constants.GroupHierarchy.LOCATION);
	  System.out.println("==========================================================");
	  System.out.println("Group Id: " + groupId);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with ouid which have no webhook");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			 System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns error when such ouid is passed which have no webhook.");
		   test.log(LogStatus.PASS, "API retuns error when such ouid is passed which have no webhook.");
		   Assert.assertNull(json.get("err"), "err data is not null when such ouid is passed which have no webhook.");
		   JSONArray data = (JSONArray) json.get("data");
		   Assert.assertTrue(data.isEmpty(), "Check no records is displayed when ouid which have no webhook is passed");
		   test.log(LogStatus.PASS, "Check no records is displayed when ouid which have no webhook is passed.");
		}

	}     
	
	//@Test(priority=13) // Need to add validation
	public void get_webhook_with_nonexisting_ouid() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with non existing ouid
		test = extent.startTest("get_webhook_with_nonexisting_ouid", "To validate whether user is able to get webhook through webhook api with non existing ouid.");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_nonexisting_ouid");
		int ouid = Integer.parseInt(test_data.get(1));
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", Integer.toString(ouid)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with non existing ouid.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();		   
		   Assert.assertEquals(result_data, "error", "API returns success when non existing ouid is passed.");
		   test.log(LogStatus.PASS, "Check API returns error when non existing ouid is passed.");
		   String error_data = json.get("err").toString();
		   Assert.assertEquals(error_data, "You don't have access to this org_unit_Id.", "Invalid validation is displayed when non existing ouid is passed.");
		   test.log(LogStatus.PASS, "Check API returns proper validation when non existing ouid is passed.");
		}
	} 
	
	//@Test(priority=14) // Need to add validation
	public void get_webhook_with_other_billing_ouid() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with other billing ouid
		test = extent.startTest("get_webhook_with_other_billing_ouid", "To validate whether user is able to get webhook through webhook api with other billing ouid.");
		test.assignCategory("CFA GET /webhook API");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		int ouid = Integer.parseInt(other_billing_group);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", Integer.toString(ouid)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute webhook api method with other billing ouid.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();		   
		   Assert.assertEquals(result_data, "error", "API returns success when other billing ouid is passed.");
		   test.log(LogStatus.PASS, "Check API returns error when other billing ouid is passed.");
		   String error_data = json.get("err").toString();
		   Assert.assertEquals(error_data, "You don't have access to this org_unit_Id.", "Invalid validation is displayed when other billing ouid is passed.");
		   test.log(LogStatus.PASS, "Check API returns proper validation when other billing ouid is passed.");
		}
	}
	

  //@Test(priority=15)
	public void get_webhook_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with valid id parameter
		test = extent.startTest("get_webhook_with_agency_admin_access_token", "To validate whether user is able to get webhooks through webhook api with agency admin access_token.");
		test.assignCategory("CFA GET /webhook API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		for(String group: new String[]{agency_group,company_group,location_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", group));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute webhook api method with "+(group.equals(agency_group)?"agency":group.equals(company_group)?"company":"location")+" level ouid is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API retuns success when "+(group.equals(agency_group)?"agency":group.equals(company_group)?"company":"location")+" level ouid is passed using agency admin access_token.");
			   Assert.assertNull(json.get("err"), "err data is not null when "+(group.equals(agency_group)?"agency":group.equals(company_group)?"company":"location")+" level ouid is passed using agency admin access_token.");
			   JSONArray webhook_data = (JSONArray)json.get("data");
			   Assert.assertTrue(webhook_data.size()>0, "Data field is empty");
			}
		}
	}
    

  //@Test(priority=16)
	public void get_webhook_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with valid company admin access_token
		test = extent.startTest("get_webhook_with_company_admin_access_token", "To validate whether user is able to get webhooks through webhook api with company admin access_token.");
		test.assignCategory("CFA GET /webhook API");
		test_data = HelperClass.readTestData(class_name, "get_webhook_with_company_admin_access_token");
		
		// Get access_token of company admin
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		for(String group: new String[]{agency_group,company_group,location_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", group));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute webhook api method with "+(group.equals(agency_group)?"agency":group.equals(company_group)?"company":"location")+" level ouid is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(group.equals(company_group) || group.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API retuns error when "+(group.equals(company_group)?"company":"location")+" level ouid is passed using company admin access_token.");
				   Assert.assertNull(json.get("err"), "err data is not null when "+(group.equals(company_group)?"company":"location")+" level ouid is passed using company admin access_token.");
				   JSONArray webhook_data = (JSONArray)json.get("data");
				   Assert.assertTrue(webhook_data.size()>0, "Data field is empty");
			   }
			   else{
				   Assert.assertEquals(result_data, "error", "API retuns success when agency level ouid is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success");
				   String error_data = json.get("err").toString();
				   Assert.assertEquals(error_data, "You don't have access to this org_unit_Id.", "Invalid validation is displayed when agency level ouid is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns proper validation when agency level ouid is passed using company admin access_token.");
			   }
			}
		}
	} 
    

  //@Test(priority=17)
	public void get_webhook_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute webhook api with valid location admin access_token
		test = extent.startTest("get_webhook_with_company_admin_access_token", "To validate whether user is able to get webhooks through webhook api with location admin access_token.");
		test.assignCategory("CFA GET /webhook API");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		for(String group: new String[]{agency_group,company_group,location_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", group));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/webhook", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute webhook api method with "+(group.equals(agency_group)?"agency":group.equals(company_group)?"company":"location")+" level ouid is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(group.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API retuns error when "+(group.equals(company_group)?"company":"location")+" level ouid is passed using location admin access_token.");
				   Assert.assertNull(json.get("err"), "err data is not null when "+(group.equals(company_group)?"company":"location")+" level ouid is passed using location admin access_token.");
				   JSONArray webhook_data = (JSONArray)json.get("data");
				   Assert.assertTrue(webhook_data.size()>0, "Data field is empty");
			   }
			   else{
				   Assert.assertEquals(result_data, "error", "API retuns success when "+(group.equals(agency_group)?"agency":"company")+" level ouid is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success");
				   String error_data = json.get("err").toString();
				   Assert.assertEquals(error_data, "You don't have access to this org_unit_Id.", "Invalid validation is displayed when "+(group.equals(agency_group)?"agency":"company")+" level ouid is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns proper validation when "+(group.equals(agency_group)?"agency":"company")+" level ouid is passed using location admin access_token.");
			   }
			}
		}
	}  
}
