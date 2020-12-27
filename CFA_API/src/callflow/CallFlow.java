package callflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.Constants.GroupHierarchy;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.helper.CreateCallFlow;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class CallFlow extends BaseClass {

	TestDataYamlReader yamlReader = new TestDataYamlReader();
  @Test(priority=1)
	public void callflow_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		// Execute callflow api method without access_token
		test = extent.startTest("callflow_without_access_token", "To validate whether user is able to get callflow through callflow api without access_token");
		test.assignCategory("CFA GET /callflow API");
		test.log(LogStatus.INFO, "Execute callflow api method without access_token");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/callflow", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "No access token is passed, 401 status error found");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when Authorization token is not passed");
		test.log(LogStatus.PASS, "Check status message when Authorization token is not passed");
	}
	
  @Test(priority=2)
	public void callflow_with_blank_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_with_blank_access_token", "To validate whether user is able to get callflow through callflow api with blank access_token");
		test.assignCategory("CFA GET /callflow API");
		String blank_access_token = ""; 
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id","5484"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", blank_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 400, "No access token is passed, 400 status error is not found");
		test.log(LogStatus.PASS, "Check status code when blank access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Bad Request", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when blank access_token is passed");
	}
	
  @Test(priority=3)
	public void callflow_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_with_invalid_token", "To validate whether user is able to get callflow through callflow api with invalid access_token");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id","5484"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	
  @Test(priority=4)
	public void callflow_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_with_expired_token", "To validate whether user is able to get callflow through callflow api with expired access_token");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id","5484"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
    
  @Test(priority=5)
	public void callflow_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with valid access_token
		test = extent.startTest("callflow_with_valid_access_token", "To validate whether user is able to get callflows through callflow api with valid token");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String callFlowId = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		list.add(new BasicNameValuePair("id",callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when valid access_token is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_id"),"callflow api does not contain call_flow_id field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_name"),"callflow api does not contain call_flow_name field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("group_id"),"callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_id"),"callflow api does not contain channel_id field.");
		   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_category"),"callflow api does not contain channel_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_sub_category"),"callflow api does not contain channel_sub_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_sub_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_status"),"callflow api does not contain call_flow_status field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("voicemail_enabled"),"callflow api does not contain voicemail_enabled field.");
		   test.log(LogStatus.PASS, "Check whether voicemail_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("ring_delay"),"callflow api does not contain ring_delay field.");
		   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("default_ringto"),"callflow api does not contain default_ringto field.");	   
		   test.log(LogStatus.PASS, "Check whether default_ringto field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_enabled"),"callflow api does not contain whisper_enabled field.");
		   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("play_disclaimer"),"callflow api does not contain play_disclaimer field.");
		   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("created_at"),"callflow api does not contain created_at field.");	   
		   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("updated_at"),"callflow api does not contain updated_at field.");
		   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_message"),"callflow api does not contain whisper_message field.");
		   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_created"),"callflow api does not contain call_flow_created field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("repeat_interval"),"callflow api does not contain repeat_interval field.");
		   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("routable_type"),"callflow api does not contain routable_type field.");
		   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("dnis"),"callflow api does not contain dnis field.");	   
		   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message_enabled"),"callflow api does not contain message_enabled field.");
		   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message"),"callflow api does not contain message field.");	   
		   test.log(LogStatus.PASS, "Check whether message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_value"),"callflow api does not contain call_value field.");
		   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("record_until"),"callflow api does not contain record_until field.");
		   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("email_to_notify"),"callflow api does not contain email_to_notify field.");	   
		   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_threshold"),"callflow api does not contain spam_threshold field.");
		   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_filter_enabled"),"callflow api does not contain spam_filter_enabled field.");
		   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("webhook_enabled"),"callflow api does not contain webhook_enabled field.");
		   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("number"),"callflow api does not contain number field.");
		   test.log(LogStatus.PASS, "Check whether number field is present in response");
		   
		   // Check data type of fields
		   Assert.assertTrue(call_flow_data.get("call_flow_id").getClass().getName().equals("java.lang.Long"),"");
		   Assert.assertTrue(call_flow_data.get("call_flow_name").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(call_flow_data.get("group_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(call_flow_data.get("channel_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(call_flow_data.get("channel").getClass().getName().equals("java.lang.String"));	   
		   Assert.assertTrue(call_flow_data.get("channel_category").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(call_flow_data.get("channel_sub_category").getClass().getName().equals("java.lang.String"));	   
		   Assert.assertTrue(call_flow_data.get("call_flow_status").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(call_flow_data.get("created_at").getClass().getName().equals("java.lang.String"));	   	   
		   Assert.assertTrue(call_flow_data.get("call_flow_created").getClass().getName().equals("java.lang.String"));   
		   Assert.assertTrue(call_flow_data.get("routable_type").getClass().getName().equals("java.lang.String"));
		   test.log(LogStatus.PASS, "Check the data type of all fields of callflow api response");
		   
		   // Check fields are not null
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("call_flow_id"), "call_flow_id");
		   test.log(LogStatus.PASS, "Check call_flow_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("call_flow_name"), "call_flow_name");
		   test.log(LogStatus.PASS, "Check call_flow_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("group_id"), "group_id");
		   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("channel_id"), "channel_id");
		   test.log(LogStatus.PASS, "Check channel_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("channel_category"), "channel_category");
		   test.log(LogStatus.PASS, "Check channel_category date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("channel_sub_category"), "channel_sub_category");
		   test.log(LogStatus.PASS, "Check channel_sub_category date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("call_flow_status"), "call_flow_status");
		   test.log(LogStatus.PASS, "Check call_flow_status date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("created_at"), "created_at");
		   test.log(LogStatus.PASS, "Check created_at date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(call_flow_data.get("number"), "number");
		   test.log(LogStatus.PASS, "Check number date is not null or blank in response.");
		}
	}
	
  @Test(priority=6)
	public void callflow_without_passing_id_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api without passing id parameter
		test = extent.startTest("callflow_without_passing_id_parameter", "To validate whether user is able to get callflows through callflow api without passing id parameter.");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method without passing id parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank id parameter is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank id parameter is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank id parameter is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Value is required but was not provided", "Invalid message value is returned in response when blank id parameter is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when blank id parameter is passed");
			JSONArray expected_path_values = new JSONArray();
			expected_path_values.add("paths");
			expected_path_values.add("/callflow");
			expected_path_values.add("get");
			expected_path_values.add("parameters");
			expected_path_values.add("0");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertEquals(error_path, expected_path_values, "path is invalid when blank id parameter is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "REQUIRED", "Invalid code value is returned in response when blank id parameter is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value is required but was not provided", "Invalid message value is returned in response when blank id parameter is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path, expected_path_values,"path is invalid when blank id parameter is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank id parameter is passed.");
		}
	}	
    
  @Test(priority=7)
	public void callflow_with_blank_id_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with blank id parameter
		test = extent.startTest("callflow_with_blank_id_parameter", "To validate whether user is able to get callflows through callflow api when blank id is passed.");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method when blank id parameter is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank id parameter is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank id parameter is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank id parameter is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when blank id parameter is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when blank id parameter is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank id parameter is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank id parameter is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank id parameter is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(),"path is not blank when blank id parameter is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank id parameter is passed.");
		}
	}
    
  @Test(priority=8)
	public void callflow_with_invalid_id_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with invalid id parameter
		test = extent.startTest("callflow_with_invalid_id_parameter", "To validate whether user is able to get callflows through callflow api when invalid id is passed.");
		test.assignCategory("CFA GET /callflow API");
		String[] call_flow_ids = {"abc","!@#","abc12","5484a"};
		for(String call_flow_id:call_flow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method when invalid("+call_flow_id+") id parameter is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+call_flow_id+") id parameter is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid("+call_flow_id+") id parameter is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+call_flow_id+") id parameter is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(),"path is not blank when invalid("+call_flow_id+") id parameter is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+call_flow_id+") id parameter is passed.");
			}
		}
	}
    
  @Test(priority=9)
	public void callflow_with_negative_id_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with negative id parameter
		test = extent.startTest("callflow_with_negative_id_parameter", "To validate whether user is able to get callflows through callflow api when negative id is passed.");
		test.assignCategory("CFA GET /callflow API");
		int call_flow_id = -5484;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", Integer.toString(call_flow_id)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method when negative("+call_flow_id+") id parameter is passed.");
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
			Assert.assertEquals(error_data, "No record found", "Invalid validation is displayed when negative id parameter is passed.");
			test.log(LogStatus.PASS, "Check API returns proper validation when negative id parameter is passed.");
		}
	}    
    
  @Test(priority=10)
	public void callflow_with_valid_id_parameter() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with valid id parameter
		test = extent.startTest("callflow_with_valid_id_parameter", "To validate whether user is able to get callflows through callflow api with valid id parameter.");
		test.assignCategory("CFA GET /callflow API");
		
		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String callFlowId = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when valid access_token is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, String.valueOf(callFlowId), "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
		}
	}
    
  @Test(priority=11)
	public void callflow_with_deleted_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException, SQLException{
		// Execute callflow api with deleted id parameter
		test = extent.startTest("callflow_with_deleted_call_flow_id", "To validate whether user is able to get callflow through callflow api with deleted id parameter.");
		test.assignCategory("CFA GET /callflow API");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String call_flow_id = DBCallFlowsUtils.getCallflowByStatusInGroup(groupId, "deleted");
		
		if(call_flow_id.equals("no data found in db")) {
			test.log(LogStatus.INFO, "Test not executed since no data found in db");
		}else {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method with deleted call flow id parameter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API retuns error when deleted call flow id parameter.");
			   Assert.assertNull(json.get("err"), "err data is returned when deleted call flow id parameter.");
			}
		}
		

		
	}    
    
  @Test(priority=12)
	public void callflow_with_tracking_num_based_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with tracking number based id parameter
		test = extent.startTest("callflow_with_tracking_num_based_id", "To validate whether user is able to get callflows through callflow api with tracking number based callflow id");
		test.assignCategory("CFA GET /callflow API");
		
		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String callFlowId = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with tracking number based callflow id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when tracking number based callflow id is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when tracking number based callflow id is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, callFlowId, "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
		   
		   String callflow_number = call_flow_data.get("dnis").toString();
		   
		   JSONArray number_data = (JSONArray) call_flow_data.get("number");
		   JSONObject number = (JSONObject) number_data.get(0);
		   Assert.assertTrue(number.containsKey("rate_center"), "rate_center field is not present in response when tracking number based callflow id is passed.");
		   test.log(LogStatus.PASS, "Check rate_center field is present in response when tracking number based callflow id is passed.");
		   Assert.assertTrue(number.containsKey("npa"), "npa field is not present in response when tracking number based callflow id is passed.");
		   test.log(LogStatus.PASS, "Check npa field is present in response when tracking number based callflow id is passed.");
		   Assert.assertTrue(number.containsKey("nxx"), "nxx field is not present in response when tracking number based callflow id is passed.");
		   test.log(LogStatus.PASS, "Check nxx field is present in response when tracking number based callflow id is passed.");
		   Assert.assertTrue(number.containsKey("ocn"), "ocn field is not present in response when tracking number based callflow id is passed.");
		   test.log(LogStatus.PASS, "Check ocn field is present in response when tracking number based callflow id is passed.");
		   
		   String expected_npa_value = callflow_number.substring(0, 3);
		   String expected_nxx_value = callflow_number.substring(3, 6);
		   String expected_ocn_value = callflow_number.substring(6, 10);
		   if(expected_ocn_value.startsWith("0"))
			   expected_ocn_value = callflow_number.substring(7, 10);
		   
		   if(expected_nxx_value.startsWith("0"))
			   expected_nxx_value = callflow_number.substring(4, 6);
		   
		   Assert.assertEquals(number.get("npa").toString(), expected_npa_value, "Invalid npa value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid npa value in response.");
		   Assert.assertEquals(number.get("nxx").toString(), expected_nxx_value, "Invalid nxx value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid nxx value in response.");
		   Assert.assertEquals(number.get("ocn").toString(), expected_ocn_value, "Invalid ocn value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid ocn value in response.");
		}
	}
    
  @Test(priority=13)
	public void callflow_with_pool_based_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with number pool based id parameter
		test = extent.startTest("callflow_with_pool_based_id", "To validate whether user is able to get callflows through callflow api with pool based callflow id");
		test.assignCategory("CFA GET /callflow API");
		CreateCallFlow createCallflow = new CreateCallFlow();
		String callFlowId = createCallflow.createPoolBasedCallflow(GroupHierarchy.AGENCY);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with number pool based callflow id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when number pool based callflow id is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when number pool based callflow id is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, String.valueOf(callFlowId), "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");		   
		}
	}   
	
  @Test(priority=14)
	public void callflow_with_simple_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with simple/hangup callflow id parameter
		test = extent.startTest("callflow_with_simple_callflow_based_id", "To validate whether user is able to get callflows through callflow api with simple/hangup route based callflow id");
		test.assignCategory("CFA GET /callflow API");
		
		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String simple_route = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
//		CreateCallFlow createCallflowObj = new CreateCallFlow();
//		String hangup_route = createCallflowObj.createHangupCallflow(Constants.GroupHierarchy.AGENCY);

		String[] call_flow_ids = {simple_route};
		for(String call_flow_id:call_flow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method with "+(call_flow_id==simple_route?"simple":"hangup")+" callflow based callflow id.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API retuns success when "+(call_flow_id==simple_route?"simple":"hangup")+" callflow based callflow id is passed.");
			   Assert.assertNull(json.get("err"), "err data is not null when "+(call_flow_id==simple_route?"simple":"hangup")+" callflow based callflow id is passed.");
			   JSONObject call_flow_data = (JSONObject)json.get("data");
			   // Check response contains the fields
			   String id_in_response = call_flow_data.get("call_flow_id").toString();
			   Assert.assertEquals(id_in_response, String.valueOf(call_flow_id), "API does not return callflow based on passed callflow id.");
			   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
			   
			   Assert.assertTrue(call_flow_data.containsKey("call_flow_id"),"callflow api does not contain call_flow_id field.");
			   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("call_flow_name"),"callflow api does not contain call_flow_name field.");
			   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("group_id"),"callflow api does not contain group_id field.");
			   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("channel_id"),"callflow api does not contain channel_id field.");
			   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
			   test.log(LogStatus.PASS, "Check whether channel field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("channel_category"),"callflow api does not contain channel_category field.");
			   test.log(LogStatus.PASS, "Check whether channel_category field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("channel_sub_category"),"callflow api does not contain channel_sub_category field.");
			   test.log(LogStatus.PASS, "Check whether channel_sub_category field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("call_flow_status"),"callflow api does not contain call_flow_status field.");	   
			   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("voicemail_enabled"),"callflow api does not contain voicemail_enabled field.");
			   test.log(LogStatus.PASS, "Check whether voicemail_enabled field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("ring_delay"),"callflow api does not contain ring_delay field.");
			   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("default_ringto"),"callflow api does not contain default_ringto field.");	   
			   test.log(LogStatus.PASS, "Check whether default_ringto field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("whisper_enabled"),"callflow api does not contain whisper_enabled field.");
			   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
			   test.log(LogStatus.PASS, "Check whether channel field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("play_disclaimer"),"callflow api does not contain play_disclaimer field.");
			   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("created_at"),"callflow api does not contain created_at field.");	   
			   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("updated_at"),"callflow api does not contain updated_at field.");
			   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("whisper_message"),"callflow api does not contain whisper_message field.");
			   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("call_flow_created"),"callflow api does not contain call_flow_created field.");	   
			   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("repeat_interval"),"callflow api does not contain repeat_interval field.");
			   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("routable_type"),"callflow api does not contain routable_type field.");
			   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
			   Assert.assertEquals(call_flow_data.get("routable_type").toString(), "SimpleRoute", "route type is not retuned simple route in response when "+(call_flow_id==simple_route?"simple":"hangup")+" callflow based callflow id is passed.");
			   test.log(LogStatus.PASS, "Check route_type is simple route in response.");
			   Assert.assertTrue(call_flow_data.containsKey("dnis"),"callflow api does not contain dnis field.");	   
			   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("message_enabled"),"callflow api does not contain message_enabled field.");
			   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("message"),"callflow api does not contain message field.");	   
			   test.log(LogStatus.PASS, "Check whether message field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("call_value"),"callflow api does not contain call_value field.");
			   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("record_until"),"callflow api does not contain record_until field.");
			   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("email_to_notify"),"callflow api does not contain email_to_notify field.");	   
			   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("spam_threshold"),"callflow api does not contain spam_threshold field.");
			   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("spam_filter_enabled"),"callflow api does not contain spam_filter_enabled field.");
			   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
			   Assert.assertTrue(call_flow_data.containsKey("webhook_enabled"),"callflow api does not contain webhook_enabled field.");
			   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
			   
			   String callflow_number = call_flow_data.get("dnis").toString();
			   
			   JSONArray number_data = (JSONArray) call_flow_data.get("number");
			   JSONObject number = (JSONObject) number_data.get(0);
			   Assert.assertTrue(number.containsKey("rate_center"), "rate_center field is not present in response.");
			   test.log(LogStatus.PASS, "Check rate_center field is present in response.");
			   Assert.assertTrue(number.containsKey("npa"), "npa field is not present in response.");
			   test.log(LogStatus.PASS, "Check npa field is present in response.");
			   Assert.assertTrue(number.containsKey("nxx"), "nxx field is not present in response.");
			   test.log(LogStatus.PASS, "Check nxx field is present in response.");
			   Assert.assertTrue(number.containsKey("ocn"), "ocn field is not present in response.");
			   test.log(LogStatus.PASS, "Check ocn field is present in response.");
			   
			   String expected_npa_value = callflow_number.substring(0, 3);
			   String expected_nxx_value = callflow_number.substring(3, 6);
			   
			   Assert.assertEquals(number.get("npa").toString(), expected_npa_value, "Invalid npa value is returned in response.");
			   test.log(LogStatus.PASS, "Check API return valid npa value in response.");
			   Assert.assertEquals(number.get("nxx").toString(), expected_nxx_value, "Invalid nxx value is returned in response.");
			   test.log(LogStatus.PASS, "Check API return valid nxx value in response.");
			}
		}
	}
	
//  @Test(priority=15)
	public void callflow_with_geo_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with geo callflow id parameter
		test = extent.startTest("callflow_with_geo_callflow_id", "To validate whether user is able to get callflows through callflow api with geo based callflow id");
		test.assignCategory("CFA GET /callflow API");
		
		CreateCallFlow createCallflow = new CreateCallFlow();
		String call_flow_id = createCallflow.createGeoCallflow(Constants.GroupHierarchy.AGENCY);

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", call_flow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with geo based callflow id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when geo based callflow id is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when geo based callflow id is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, String.valueOf(call_flow_id), "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
		   
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_id"),"callflow api does not contain call_flow_id field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_name"),"callflow api does not contain call_flow_name field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("group_id"),"callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_id"),"callflow api does not contain channel_id field.");
		   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_category"),"callflow api does not contain channel_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_sub_category"),"callflow api does not contain channel_sub_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_sub_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_status"),"callflow api does not contain call_flow_status field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("voicemail_enabled"),"callflow api does not contain voicemail_enabled field.");
		   test.log(LogStatus.PASS, "Check whether voicemail_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("ring_delay"),"callflow api does not contain ring_delay field.");
		   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("default_ringto"),"callflow api does not contain default_ringto field.");	   
		   test.log(LogStatus.PASS, "Check whether default_ringto field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_enabled"),"callflow api does not contain whisper_enabled field.");
		   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("play_disclaimer"),"callflow api does not contain play_disclaimer field.");
		   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("created_at"),"callflow api does not contain created_at field.");	   
		   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("updated_at"),"callflow api does not contain updated_at field.");
		   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_message"),"callflow api does not contain whisper_message field.");
		   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_created"),"callflow api does not contain call_flow_created field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("repeat_interval"),"callflow api does not contain repeat_interval field.");
		   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("routable_type"),"callflow api does not contain routable_type field.");
		   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
		   Assert.assertEquals(call_flow_data.get("routable_type").toString(), "GeoRoute", "route type is not retuned geo in response");
		   test.log(LogStatus.PASS, "Check route_type is geo in response.");
		   Assert.assertTrue(call_flow_data.containsKey("dnis"),"callflow api does not contain dnis field.");	   
		   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message_enabled"),"callflow api does not contain message_enabled field.");
		   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message"),"callflow api does not contain message field.");	   
		   test.log(LogStatus.PASS, "Check whether message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_value"),"callflow api does not contain call_value field.");
		   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("record_until"),"callflow api does not contain record_until field.");
		   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("email_to_notify"),"callflow api does not contain email_to_notify field.");	   
		   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_threshold"),"callflow api does not contain spam_threshold field.");
		   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_filter_enabled"),"callflow api does not contain spam_filter_enabled field.");
		   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("webhook_enabled"),"callflow api does not contain webhook_enabled field.");
		   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
		   
		   JSONObject geo_route = (JSONObject)call_flow_data.get("geo_route");		
		   Assert.assertTrue(geo_route.containsKey("play_branding"), "play_branding is not response.");
		   Assert.assertTrue(geo_route.containsKey("radius"), "radius is not response.");
		   Assert.assertTrue(geo_route.containsKey("strategy"), "strategy is not response.");
		   Assert.assertTrue(geo_route.containsKey("play_branding"), "play_branding is not response.");
		   Assert.assertTrue(geo_route.containsKey("allow_manual_entry"), "allow_manual_entry is not response.");
		   Assert.assertTrue(geo_route.containsKey("options"), "options is not response.");
		   
		   JSONObject options = (JSONObject)geo_route.get("options");	
		   Assert.assertTrue(options.containsKey("geo_target_did"), "play_branding is not response.");
		   Assert.assertTrue(options.containsKey("group_id"), "group_id is not response.");
		   Assert.assertTrue(options.containsKey("latitude"), "latitude is not response.");
		   Assert.assertTrue(options.containsKey("address"), "address is not response.");
		   Assert.assertTrue(options.containsKey("city"), "city is not response.");
		   Assert.assertTrue(options.containsKey("geo_created_at"), "geo_created_at is not response.");
		   Assert.assertTrue(options.containsKey("geo_updated_at"), "geo_updated_at is not response.");
		   
		   JSONArray location_data = (JSONArray)call_flow_data.get("location");		
		   for(int j=0;j<location_data.size();j++){
			   JSONObject location = (JSONObject) location_data.get(j);
			   Assert.assertTrue(location.containsKey("location_id"), "location_id is not present in response.");
			   Assert.assertTrue(location.containsKey("org_unit_id"), "org_unit_id is not present in response.");
			   Assert.assertTrue(location.containsKey("location_name"), "location_name is not present in response.");
			   Assert.assertTrue(location.containsKey("location_created"), "location_created is not present in response.");
			   Assert.assertTrue(location.containsKey("location_modified"), "location_modified is not present in response.");
			   Assert.assertTrue(location.containsKey("location_active"), "location_active is not present in response.");
		   }
		   test.log(LogStatus.PASS, "Check location_details is present in response.");
		   String callflow_number = call_flow_data.get("dnis").toString();
		   
		   JSONArray number_data = (JSONArray) call_flow_data.get("number");
		   JSONObject number = (JSONObject) number_data.get(0);
		   Assert.assertTrue(number.containsKey("rate_center"), "rate_center field is not present in response.");
		   test.log(LogStatus.PASS, "Check rate_center field is present in response.");
		   Assert.assertTrue(number.containsKey("npa"), "npa field is not present in response.");
		   test.log(LogStatus.PASS, "Check npa field is present in response.");
		   Assert.assertTrue(number.containsKey("nxx"), "nxx field is not present in response.");
		   test.log(LogStatus.PASS, "Check nxx field is present in response.");
		   Assert.assertTrue(number.containsKey("ocn"), "ocn field is not present in response.");
		   test.log(LogStatus.PASS, "Check ocn field is present in response.");
		   
		   String expected_npa_value = callflow_number.substring(0, 3);
		   String expected_nxx_value = callflow_number.substring(3, 6);
		   int expected_ocn_value = Integer.parseInt(callflow_number.substring(6, 10));
		   
		   Assert.assertEquals(number.get("npa").toString(), expected_npa_value, "Invalid npa value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid npa value in response.");
		   Assert.assertEquals(number.get("nxx").toString(), expected_nxx_value, "Invalid nxx value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid nxx value in response.");
		   Assert.assertEquals(Integer.parseInt(number.get("ocn").toString()), expected_ocn_value, "Invalid ocn value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid ocn value in response.");
		}
	}
  
  @Test(priority=16)
	public void callflow_with_ivr_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with ivr callflow id parameter
		test = extent.startTest("callflow_with_ivr_callflow_id", "To validate whether user is able to get callflows through callflow api with ivr route based callflow id");
		test.assignCategory("CFA GET /callflow API");
		CreateCallFlow createCallflow = new CreateCallFlow();
		String call_flow_id = createCallflow.createIvrCallflow(Constants.GroupHierarchy.AGENCY);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", call_flow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with ivr callflow based callflow id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when ivr callflow based callflow id is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when ivr callflow based callflow id is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, String.valueOf(call_flow_id), "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
		   
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_id"),"callflow api does not contain call_flow_id field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_name"),"callflow api does not contain call_flow_name field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("group_id"),"callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_id"),"callflow api does not contain channel_id field.");
		   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_category"),"callflow api does not contain channel_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_sub_category"),"callflow api does not contain channel_sub_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_sub_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_status"),"callflow api does not contain call_flow_status field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("voicemail_enabled"),"callflow api does not contain voicemail_enabled field.");
		   test.log(LogStatus.PASS, "Check whether voicemail_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("ring_delay"),"callflow api does not contain ring_delay field.");
		   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_enabled"),"callflow api does not contain whisper_enabled field.");
		   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("play_disclaimer"),"callflow api does not contain play_disclaimer field.");
		   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("created_at"),"callflow api does not contain created_at field.");	   
		   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("updated_at"),"callflow api does not contain updated_at field.");
		   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_message"),"callflow api does not contain whisper_message field.");
		   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_created"),"callflow api does not contain call_flow_created field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("repeat_interval"),"callflow api does not contain repeat_interval field.");
		   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("routable_type"),"callflow api does not contain routable_type field.");
		   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
		   Assert.assertEquals(call_flow_data.get("routable_type").toString(), "IvrRoute2", "route type is not retuned ivr in response");
		   test.log(LogStatus.PASS, "Check route_type is ivr in response.");
		   Assert.assertTrue(call_flow_data.containsKey("dnis"),"callflow api does not contain dnis field.");	   
		   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message_enabled"),"callflow api does not contain message_enabled field.");
		   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message"),"callflow api does not contain message field.");	   
		   test.log(LogStatus.PASS, "Check whether message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_value"),"callflow api does not contain call_value field.");
		   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("record_until"),"callflow api does not contain record_until field.");
		   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("email_to_notify"),"callflow api does not contain email_to_notify field.");	   
		   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_threshold"),"callflow api does not contain spam_threshold field.");
		   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_filter_enabled"),"callflow api does not contain spam_filter_enabled field.");
		   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("webhook_enabled"),"callflow api does not contain webhook_enabled field.");
		   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
		   
		   String callflow_number = call_flow_data.get("dnis").toString();
		   
		   JSONArray number_data = (JSONArray) call_flow_data.get("number");
		   JSONObject number = (JSONObject) number_data.get(0);
		   Assert.assertTrue(number.containsKey("rate_center"), "rate_center field is not present in response.");
		   test.log(LogStatus.PASS, "Check rate_center field is present in response.");
		   Assert.assertTrue(number.containsKey("npa"), "npa field is not present in response.");
		   test.log(LogStatus.PASS, "Check npa field is present in response.");
		   Assert.assertTrue(number.containsKey("nxx"), "nxx field is not present in response.");
		   test.log(LogStatus.PASS, "Check nxx field is present in response.");
		   Assert.assertTrue(number.containsKey("ocn"), "ocn field is not present in response.");
		   test.log(LogStatus.PASS, "Check ocn field is present in response.");
		   
		   String expected_npa_value = callflow_number.substring(0, 3);
		   String expected_nxx_value = callflow_number.substring(3, 6);
		   String expected_ocn_value = callflow_number.substring(6, 10);
		   
		   Assert.assertEquals(number.get("npa").toString(), expected_npa_value, "Invalid npa value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid npa value in response.");
		   Assert.assertEquals(number.get("nxx").toString(), expected_nxx_value, "Invalid nxx value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid nxx value in response.");
		   Assert.assertEquals(Integer.parseInt(number.get("ocn").toString()), Integer.parseInt(expected_ocn_value), "Invalid ocn value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid ocn value in response.");
		}
	} 
  	
//  @Test(priority=17)
	public void callflow_with_percentage_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with percentage callflow id parameter
		test = extent.startTest("callflow_with_percentage_callflow_id", "To validate whether user is able to get callflows through callflow api with percentage route based callflow id");
		test.assignCategory("CFA GET /callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
	  CreateCallFlow createCallflowObj = new CreateCallFlow();
	  String percentage_route = createCallflowObj.createPercentageCallflow(Constants.GroupHierarchy.AGENCY);;
	  list.add(new BasicNameValuePair("id", percentage_route));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow api method with percentage callflow based callflow id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API retuns success when percentage callflow based callflow id is passed.");
		   Assert.assertNull(json.get("err"), "err data is not null when percentage callflow based callflow id is passed.");
		   JSONObject call_flow_data = (JSONObject)json.get("data");
		   // Check response contains the fields
		   String id_in_response = call_flow_data.get("call_flow_id").toString();
		   Assert.assertEquals(id_in_response, percentage_route, "API does not return callflow based on passed callflow id.");
		   test.log(LogStatus.PASS, "Check API returns callflow based on passed callflow id.");
		   
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_id"),"callflow api does not contain call_flow_id field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_name"),"callflow api does not contain call_flow_name field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("group_id"),"callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_id"),"callflow api does not contain channel_id field.");
		   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_category"),"callflow api does not contain channel_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel_sub_category"),"callflow api does not contain channel_sub_category field.");
		   test.log(LogStatus.PASS, "Check whether channel_sub_category field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_status"),"callflow api does not contain call_flow_status field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("voicemail_enabled"),"callflow api does not contain voicemail_enabled field.");
		   test.log(LogStatus.PASS, "Check whether voicemail_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("ring_delay"),"callflow api does not contain ring_delay field.");
		   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("default_ringto"),"callflow api does not contain default_ringto field.");	   
		   test.log(LogStatus.PASS, "Check whether default_ringto field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_enabled"),"callflow api does not contain whisper_enabled field.");
		   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("channel"),"callflow api does not contain channel field.");
		   test.log(LogStatus.PASS, "Check whether channel field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("play_disclaimer"),"callflow api does not contain play_disclaimer field.");
		   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("created_at"),"callflow api does not contain created_at field.");	   
		   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("updated_at"),"callflow api does not contain updated_at field.");
		   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("whisper_message"),"callflow api does not contain whisper_message field.");
		   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_flow_created"),"callflow api does not contain call_flow_created field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("repeat_interval"),"callflow api does not contain repeat_interval field.");
		   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("routable_type"),"callflow api does not contain routable_type field.");
		   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
		   Assert.assertEquals(call_flow_data.get("routable_type").toString(), "PercentageBasedRoute", "route type is not retuned percentage in response");
		   test.log(LogStatus.PASS, "Check route_type is percentage in response.");
		   Assert.assertTrue(call_flow_data.containsKey("dnis"),"callflow api does not contain dnis field.");	   
		   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message_enabled"),"callflow api does not contain message_enabled field.");
		   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("message"),"callflow api does not contain message field.");	   
		   test.log(LogStatus.PASS, "Check whether message field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("call_value"),"callflow api does not contain call_value field.");
		   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("record_until"),"callflow api does not contain record_until field.");
		   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("email_to_notify"),"callflow api does not contain email_to_notify field.");	   
		   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_threshold"),"callflow api does not contain spam_threshold field.");
		   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("spam_filter_enabled"),"callflow api does not contain spam_filter_enabled field.");
		   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
		   Assert.assertTrue(call_flow_data.containsKey("webhook_enabled"),"callflow api does not contain webhook_enabled field.");
		   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
		   
		   String callflow_number = call_flow_data.get("dnis").toString();
		   
		   JSONArray number_data = (JSONArray) call_flow_data.get("number");
		   JSONObject number = (JSONObject) number_data.get(0);
		   Assert.assertTrue(number.containsKey("rate_center"), "rate_center field is not present in response.");
		   test.log(LogStatus.PASS, "Check rate_center field is present in response.");
		   Assert.assertTrue(number.containsKey("npa"), "npa field is not present in response.");
		   test.log(LogStatus.PASS, "Check npa field is present in response.");
		   Assert.assertTrue(number.containsKey("nxx"), "nxx field is not present in response.");
		   test.log(LogStatus.PASS, "Check nxx field is present in response.");
		   Assert.assertTrue(number.containsKey("ocn"), "ocn field is not present in response.");
		   test.log(LogStatus.PASS, "Check ocn field is present in response.");
		   
		   String expected_npa_value = callflow_number.substring(0, 3);
		   String expected_nxx_value = callflow_number.substring(3, 6);
		   int expected_ocn_value = Integer.parseInt(callflow_number.substring(6, 10));
		   
		   Assert.assertEquals(number.get("npa").toString(), expected_npa_value, "Invalid npa value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid npa value in response.");
		   Assert.assertEquals(number.get("nxx").toString(), expected_nxx_value, "Invalid nxx value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid nxx value in response.");
		   Assert.assertEquals(Integer.parseInt(number.get("ocn").toString()), expected_ocn_value, "Invalid ocn value is returned in response.");
		   test.log(LogStatus.PASS, "Check API return valid ocn value in response.");
		}
	}  
  
  @Test(priority=19)
	public void callflow_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with agency admin access_token
		test = extent.startTest("callflow_with_agency_admin_access_token", "To validate whether user is able to get callflow.");
		test.assignCategory("CFA GET /callflow API");
		
		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String agency_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String company_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		String location_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		String[] callflow_ids = {agency_callflow,company_callflow,location_callflow};
		for(String call_flow_id:callflow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method with "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API retuns success when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with agency admin access_token.");
			   Assert.assertNull(json.get("err"), "err data is not null when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with agency admin access_token.");
			}
		}
    }
    
  @Test(priority=20)
	public void callflow_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with agency admin access_token
		test = extent.startTest("callflow_with_agency_admin_access_token", "To validate whether user is able to get callflow.");
		test.assignCategory("CFA GET /callflow API");
		
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String agency_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String company_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		String location_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		String[] callflow_ids = {agency_callflow,company_callflow,location_callflow};

		for(String call_flow_id:callflow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method with "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(call_flow_id == agency_callflow){
				   Assert.assertEquals(result_data, "error", "API retuns success when agency level call_flow_id with company admin access_token. Defect Reported: CT-17106");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API retuns error when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with company admin access_token.");
				   Assert.assertNull(json.get("err"), "err data is not null when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with company admin access_token.");
			   }
			}
		}
    }
    
  @Test(priority=21)
	public void callflow_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow api with location admin access_token
		test = extent.startTest("callflow_with_location_admin_access_token", "To validate whether user is able to get callflow using location admin access_token.");
		test.assignCategory("CFA GET /callflow API");

		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String, Object> confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String agency_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String company_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		confCallFlowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		String location_callflow = confCallFlowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		String[] callflow_ids = {agency_callflow,company_callflow,location_callflow};

		for(String call_flow_id:callflow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow api method with "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(call_flow_id == agency_callflow || call_flow_id == company_callflow){
				   Assert.assertEquals(result_data, "error", "API retuns success when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with location admin access_token. Defect Reported: CT-17106");
			   }
			   else {
				   Assert.assertEquals(result_data, "success", "API retuns error when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with location admin access_token.");
				   Assert.assertNull(json.get("err"), "err data is not null when "+(call_flow_id==agency_callflow?"agency":call_flow_id==company_callflow?"company":"location")+" level call_flow_id with location admin access_token.");
			   }
			}
		}
  }    

}	
