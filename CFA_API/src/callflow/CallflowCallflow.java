package callflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.convirza.constants.TestDataYamlConstants;	
import com.convirza.tests.core.io.TestDataYamlReader;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class CallflowCallflow extends BaseClass{

	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	SimpleDateFormat date_formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.ENGLISH);
   @Test(priority=1)
	public void callflow_callflow_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		// Execute callflow/callflow api method without access_token
		test = extent.startTest("callflow_callflow_without_access_token", "To validate whether user is able to get callflow through callflow/callflow api without access_token");
		test.assignCategory("CFA GET callflow/callflow API");
		test.log(LogStatus.INFO, "Execute callflow/callflow api method without access_token");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/callflow/callflow", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "No access token is passed, 401 status error found");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when Authorization token is not passed");
		test.log(LogStatus.PASS, "Check status message when Authorization token is not passed");
	}
	
  // @Test(priority=2)
	public void callflow_callflow_with_blank_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_callflow_with_blank_access_token", "To validate whether user is able to get callflow through callflow/callflow api with blank access_token");
		test.assignCategory("CFA GET callflow/callflow API");
		String blank_access_token = ""; List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", blank_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 400, "No access token is passed, 400 status error is not found");
		test.log(LogStatus.PASS, "Check status code when blank access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Bad Request", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when blank access_token is passed");
	}
	
  // @Test(priority=3)
	public void callflow_callflow_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_callflow_with_invalid_token", "To validate whether user is able to get callflow through callflow/callflow api with invalid access_token");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	
  // @Test(priority=4)
	public void callflow_callflow_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("callflow_callflow_with_expired_token", "To validate whether user is able to get callflow through callflow/callflow api with expired access_token");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "No access token is passed, 401 status error is not found");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
	
  // @Test(priority=5)
	public void callflow_callflow_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid access_token
		test = extent.startTest("callflow_callflow_with_valid_access_token", "To validate whether user is able to get callflows through callflow/callflow api with valid token");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether callflow/callflow returns 100 record by default
		   Assert.assertEquals(array.size(), 100, "callflow/callflow does not return 100 records");
		   test.log(LogStatus.PASS, "Check whether callflow/callflow returns 100 record by default");
		   // Get the first callflow from the callflow/callflow
		   JSONObject first_callflow = (JSONObject)array.get(5);
		   System.out.println(first_callflow);
		   // Check response contains the fields
		   Assert.assertTrue(first_callflow.containsKey("call_flow_id"),"callflow/callflow api does not contain call_flow_id field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_id field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("call_flow_name"),"callflow/callflow api does not contain call_flow_name field.");
		   test.log(LogStatus.PASS, "Check whether call_flow_name field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("group_id"),"callflow/callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("channel_id"),"callflow/callflow api does not contain channel_id field.");
		   test.log(LogStatus.PASS, "Check whether channel_id field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("call_flow_status"),"callflow/callflow api does not contain call_flow_status field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_status field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("vm_enabled"),"callflow/callflow api does not contain vm_enabled field.");	   
		   test.log(LogStatus.PASS, "Check whether vm_enabled field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("ring_delay"),"callflow/callflow api does not contain ring_delay field.");
		   test.log(LogStatus.PASS, "Check whether ring_delay field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("default_ringto"),"callflow/callflow api does not contain default_ringto field.");	   
		   test.log(LogStatus.PASS, "Check whether default_ringto field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("whisper_enabled"),"callflow/callflow api does not contain whisper_enabled field.");
		   test.log(LogStatus.PASS, "Check whether whisper_enabled field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("play_disclaimer"),"callflow/callflow api does not contain play_disclaimer field.");
		   test.log(LogStatus.PASS, "Check whether play_disclaimer field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("created_at"),"callflow/callflow api does not contain created_at field.");	   
		   test.log(LogStatus.PASS, "Check whether created_at field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("updated_at"),"callflow/callflow api does not contain updated_at field.");
		   test.log(LogStatus.PASS, "Check whether updated_at field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("whisper_message"),"callflow/callflow api does not contain whisper_message field.");
		   test.log(LogStatus.PASS, "Check whether whisper_message field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("call_flow_created"),"callflow/callflow api does not contain call_flow_created field.");	   
		   test.log(LogStatus.PASS, "Check whether call_flow_created field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("postcall_ivr_enabled"),"callflow/callflow api does not contain postcall_ivr_enabled field.");
		   test.log(LogStatus.PASS, "Check whether postcall_ivr_enabled field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("repeat_interval"),"callflow/callflow api does not contain repeat_interval field.");
		   test.log(LogStatus.PASS, "Check whether repeat_interval field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("routable_type"),"callflow/callflow api does not contain routable_type field.");
		   test.log(LogStatus.PASS, "Check whether routable_type field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("dnis"),"callflow/callflow api does not contain dnis field.");	   
		   test.log(LogStatus.PASS, "Check whether dnis field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("message_enabled"),"callflow/callflow api does not contain message_enabled field.");
		   test.log(LogStatus.PASS, "Check whether message_enabled field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("postcall_ivr_id"),"callflow/callflow api does not contain postcall_ivr_id field.");
		   test.log(LogStatus.PASS, "Check whether postcall_ivr_id field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("message"),"callflow/callflow api does not contain message field.");	   
		   test.log(LogStatus.PASS, "Check whether message field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("call_value"),"callflow/callflow api does not contain call_value field.");
		   test.log(LogStatus.PASS, "Check whether call_value field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("record_until"),"callflow/callflow api does not contain record_until field.");
		   test.log(LogStatus.PASS, "Check whether record_until field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("email_to_notify"),"callflow/callflow api does not contain email_to_notify field.");	   
		   test.log(LogStatus.PASS, "Check whether email_to_notify field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("spam_threshold"),"callflow/callflow api does not contain spam_threshold field.");
		   test.log(LogStatus.PASS, "Check whether spam_threshold field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("spam_filter_enabled"),"callflow/callflow api does not contain spam_filter_enabled field.");
		   test.log(LogStatus.PASS, "Check whether spam_filter_enabled field is present in response");
		   Assert.assertTrue(first_callflow.containsKey("webhook_enabled"),"callflow/callflow api does not contain webhook_enabled field.");
		   test.log(LogStatus.PASS, "Check whether webhook_enabled field is present in response");
		   
		   // Check data type of fields
		   Assert.assertTrue(first_callflow.get("call_flow_id").getClass().getName().equals("java.lang.Long"),"");
		   Assert.assertTrue(first_callflow.get("call_flow_name").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_callflow.get("group_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_callflow.get("channel_id").getClass().getName().equals("java.lang.Long"));	   
		   Assert.assertTrue(first_callflow.get("call_flow_status").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_callflow.get("created_at").getClass().getName().equals("java.lang.String"));	   	   
		   Assert.assertTrue(first_callflow.get("call_flow_created").getClass().getName().equals("java.lang.String"));   
		   Assert.assertTrue(first_callflow.get("routable_type").getClass().getName().equals("java.lang.String"));
		   test.log(LogStatus.PASS, "Check the data type of all fields of callflow/callflow api response");
		   
		   // Check fields are not null
		   HelperClass.multiple_assertnotEquals(first_callflow.get("call_flow_id"), "call_flow_id");
		   test.log(LogStatus.PASS, "Check call_flow_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow.get("call_flow_name"), "call_flow_name");
		   test.log(LogStatus.PASS, "Check call_flow_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow.get("group_id"), "group_id");
		   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow.get("channel_id"), "channel_id");
		   test.log(LogStatus.PASS, "Check channel_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow.get("call_flow_status"), "call_flow_status");
		   test.log(LogStatus.PASS, "Check call_flow_status date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow.get("created_at"), "created_at");
		   test.log(LogStatus.PASS, "Check created_at date is not null or blank in response.");
		}
	}	
	
  // @Test(priority=6)
	public void callflow_callflow_without_any_params() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api without any parameter
		test = extent.startTest("callflow_callflow_without_any_params", "To validate whether user is able to get callflows through callflow/callflow api without any parameters.");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method without any parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether callflow/callflow returns 100 record by default
		   Assert.assertEquals(array.size(), 100, "callflows callflow does not return 100 records");
		   test.log(LogStatus.PASS, "Check whether callflow/callflow returns 100 record by default");
		   // Get the callflow from the callflow/callflow
		   for(int i=0;i<array.size();i++){
			   JSONObject callflow = (JSONObject)array.get(i);
			   // Check response contains the fields
			   Assert.assertTrue(callflow.containsKey("call_flow_id"),"callflow/callflow api does not contain call_flow_id field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_name"),"callflow/callflow api does not contain call_flow_name field.");
			   Assert.assertTrue(callflow.containsKey("group_id"),"callflow/callflow api does not contain group_id field.");
			   Assert.assertTrue(callflow.containsKey("channel_id"),"callflow/callflow api does not contain channel_id field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_status"),"callflow/callflow api does not contain call_flow_status field.");	   
			   Assert.assertTrue(callflow.containsKey("ring_delay"),"callflow/callflow api does not contain ring_delay field.");
			   Assert.assertTrue(callflow.containsKey("default_ringto"),"callflow/callflow api does not contain default_ringto field.");	   
			   Assert.assertTrue(callflow.containsKey("whisper_enabled"),"callflow/callflow api does not contain whisper_enabled field.");
			   Assert.assertTrue(callflow.containsKey("play_disclaimer"),"callflow/callflow api does not contain play_disclaimer field.");
			   Assert.assertTrue(callflow.containsKey("created_at"),"callflow/callflow api does not contain created_at field.");	   
			   Assert.assertTrue(callflow.containsKey("updated_at"),"callflow/callflow api does not contain updated_at field.");
			   Assert.assertTrue(callflow.containsKey("whisper_message"),"callflow/callflow api does not contain whisper_message field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_created"),"callflow/callflow api does not contain call_flow_created field.");	   
			   Assert.assertTrue(callflow.containsKey("postcall_ivr_enabled"),"callflow/callflow api does not contain postcall_ivr_enabled field.");
			   Assert.assertTrue(callflow.containsKey("repeat_interval"),"callflow/callflow api does not contain repeat_interval field.");
			   Assert.assertTrue(callflow.containsKey("routable_type"),"callflow/callflow api does not contain routable_type field.");
			   Assert.assertTrue(callflow.containsKey("dnis"),"callflow/callflow api does not contain dnis field.");	   
			   Assert.assertTrue(callflow.containsKey("message_enabled"),"callflow/callflow api does not contain message_enabled field.");
			   Assert.assertTrue(callflow.containsKey("postcall_ivr_id"),"callflow/callflow api does not contain postcall_ivr_id field.");
			   Assert.assertTrue(callflow.containsKey("message"),"callflow/callflow api does not contain message field.");	   
			   Assert.assertTrue(callflow.containsKey("call_value"),"callflow/callflow api does not contain call_value field.");
			   Assert.assertTrue(callflow.containsKey("record_until"),"callflow/callflow api does not contain record_until field.");
			   Assert.assertTrue(callflow.containsKey("email_to_notify"),"callflow/callflow api does not contain email_to_notify field.");	   
			   Assert.assertTrue(callflow.containsKey("spam_threshold"),"callflow/callflow api does not contain spam_threshold field.");
			   Assert.assertTrue(callflow.containsKey("spam_filter_enabled"),"callflow/callflow api does not contain spam_filter_enabled field.");
			   Assert.assertTrue(callflow.containsKey("webhook_enabled"),"callflow/callflow api does not contain webhook_enabled field.");
			   
			   // Check fields are not null
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_name"), "call_flow_name");
			   	if(callflow.get("group_id")==null)	
			   		test.log(LogStatus.FAIL, "Some garbage data is present in db");
			   HelperClass.multiple_assertnotEquals(callflow.get("group_id"), "group_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("channel_id"), "channel_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(callflow.get("created_at"), "created_at");
			   
			   // Check data type of fields
			   Assert.assertTrue(callflow.get("call_flow_id").getClass().getName().equals("java.lang.Long"),"");
			   Assert.assertTrue(callflow.get("call_flow_name").getClass().getName().equals("java.lang.String"));
			   Assert.assertTrue(callflow.get("group_id").getClass().getName().equals("java.lang.Long"));
			   Assert.assertTrue(callflow.get("channel_id").getClass().getName().equals("java.lang.Long"));   
			   Assert.assertTrue(callflow.get("call_flow_status").getClass().getName().equals("java.lang.String"));
			   Assert.assertTrue(callflow.get("created_at").getClass().getName().equals("java.lang.String"));	   	   
			   Assert.assertTrue(callflow.get("call_flow_created").getClass().getName().equals("java.lang.String"));   
			   Assert.assertTrue(callflow.get("routable_type").getClass().getName().equals("java.lang.String"));
		   }
		}
	}
	
  // @Test(priority=7)
	public void callflow_callflow_with_blank_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with blank limit value
		test = extent.startTest("callflow_callflow_with_blank_limit", "To validate whether user is able to get callflows through callflow/callflow api with blank limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
			System.out.println(line);
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Expected type integer but found type string", "Invalid message value is returned in response when blank limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when blank limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank limit value is passed.");
		}
	}	
	
  // @Test(priority=8)
	public void callflow_callflow_with_invalid_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid limit value
		test = extent.startTest("callflow_callflow_with_invalid_limit", "To validate whether user is able to get callflows through callflow/callflow api with invalid limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] invalid_limits = {"abc","!@$#","123abc","abc123"};
		for(String limit:invalid_limits){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("limit", limit));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+limit+") limit value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+limit+") limit value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+limit+") limit value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+limit+") limit value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (limit): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+limit+") limit value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when invalid("+limit+") limit value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid("+limit+") limit value is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+limit+") limit value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+limit+") limit value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+limit+") limit value is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+limit+") limit value is passed.");
			}
		}
	}
	
  // @Test(priority=9)
	public void callflow_callflow_with_negative_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid limit value
		test = extent.startTest("callflow_callflow_with_invalid_limit", "To validate whether user is able to get callflows through callflow/callflow api with invalid limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int limit = -10;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit",Integer.toString(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with negative("+limit+") limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative("+limit+") limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative("+limit+") limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative("+limit+") limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when negative("+limit+") limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when negative("+limit+") limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> path_list = new ArrayList<String>();
			path_list.add("paths");
			path_list.add("/callflow/callflow");
			path_list.add("get");
			path_list.add("parameters");
			path_list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, path_list, "path is not valid when zero("+limit+") limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+limit+") limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit+" is less than minimum 1", "Invalid message value is returned in response when negative("+limit+") limit value is passed");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "Maximum number of records to return", "Invalid description value is returned in response when negative("+limit+") limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+limit+") limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+limit+") limit value is passed.");
		}
	}
	
  // @Test(priority=10)
	public void callflow_callflow_with_zero_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with zero limit value
		test = extent.startTest("callflow_callflow_with_zero_limit", "To validate whether user is able to get callflows through callflow/callflow api with zero limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int limit = 0;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit",Integer.toString(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with zero("+limit+") limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when zero("+limit+") limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when zero("+limit+") limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when zero("+limit+") limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when zero("+limit+") limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when zero("+limit+") limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> path_list = new ArrayList<String>();
			path_list.add("paths");
			path_list.add("/callflow/callflow");
			path_list.add("get");
			path_list.add("parameters");
			path_list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, path_list, "path is not valid when zero("+limit+") limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when zero("+limit+") limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit+" is less than minimum 1", "Invalid message value is returned in response when zero("+limit+") limit value is passed");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "Maximum number of records to return", "Invalid description value is returned in response when zero("+limit+") limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when zero("+limit+") limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when zero("+limit+") limit value is passed.");
		}
	}	
	
  // @Test(priority=11)
	public void callflow_callflow_with_valid_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid limit
		test = extent.startTest("callflow_callflow_with_valid_limit", "To validate whether user is able to get callflows through callflow/callflow api with valid limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int limit = 1;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit",Integer.toString(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid limit");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid limit value is passed.");
		   test.log(LogStatus.PASS, "Check callflow/callflow api returns success when valid limit value is passed.");
		   Assert.assertNull(json.get("err"), "API returns null in error when valid limit value is passed.");
		   test.log(LogStatus.PASS, "Check callflow/callflow api returns success when valid limit value is passed.");
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether callflow/callflow returns 100 record by default
		   Assert.assertEquals(array.size(), limit, "callflows callflow does not return records according to passed limit value");
		   test.log(LogStatus.PASS, "Check whether callflows callflow return records according to passed limit value");
		   // Get the callflow from the callflow/callflow
		   for(int i=0;i<array.size();i++){
			   JSONObject callflow = (JSONObject)array.get(i);
			   // Check response contains the fields
			   Assert.assertTrue(callflow.containsKey("call_flow_id"),"callflow/callflow api does not contain call_flow_id field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_name"),"callflow/callflow api does not contain call_flow_name field.");
			   Assert.assertTrue(callflow.containsKey("group_id"),"callflow/callflow api does not contain group_id field.");
			   Assert.assertTrue(callflow.containsKey("channel_id"),"callflow/callflow api does not contain channel_id field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_status"),"callflow/callflow api does not contain call_flow_status field.");	   
			   Assert.assertTrue(callflow.containsKey("ring_delay"),"callflow/callflow api does not contain ring_delay field.");
			   Assert.assertTrue(callflow.containsKey("default_ringto"),"callflow/callflow api does not contain default_ringto field.");	   
			   Assert.assertTrue(callflow.containsKey("whisper_enabled"),"callflow/callflow api does not contain whisper_enabled field.");
			   Assert.assertTrue(callflow.containsKey("play_disclaimer"),"callflow/callflow api does not contain play_disclaimer field.");
			   Assert.assertTrue(callflow.containsKey("created_at"),"callflow/callflow api does not contain created_at field.");	   
			   Assert.assertTrue(callflow.containsKey("updated_at"),"callflow/callflow api does not contain updated_at field.");
			   Assert.assertTrue(callflow.containsKey("whisper_message"),"callflow/callflow api does not contain whisper_message field.");
			   Assert.assertTrue(callflow.containsKey("call_flow_created"),"callflow/callflow api does not contain call_flow_created field.");	   
			   Assert.assertTrue(callflow.containsKey("postcall_ivr_enabled"),"callflow/callflow api does not contain postcall_ivr_enabled field.");
			   Assert.assertTrue(callflow.containsKey("repeat_interval"),"callflow/callflow api does not contain repeat_interval field.");
			   Assert.assertTrue(callflow.containsKey("routable_type"),"callflow/callflow api does not contain routable_type field.");
			   Assert.assertTrue(callflow.containsKey("dnis"),"callflow/callflow api does not contain dnis field.");	   
			   Assert.assertTrue(callflow.containsKey("message_enabled"),"callflow/callflow api does not contain message_enabled field.");
			   Assert.assertTrue(callflow.containsKey("postcall_ivr_id"),"callflow/callflow api does not contain postcall_ivr_id field.");
			   Assert.assertTrue(callflow.containsKey("message"),"callflow/callflow api does not contain message field.");	   
			   Assert.assertTrue(callflow.containsKey("call_value"),"callflow/callflow api does not contain call_value field.");
			   Assert.assertTrue(callflow.containsKey("record_until"),"callflow/callflow api does not contain record_until field.");
			   Assert.assertTrue(callflow.containsKey("email_to_notify"),"callflow/callflow api does not contain email_to_notify field.");	   
			   Assert.assertTrue(callflow.containsKey("spam_threshold"),"callflow/callflow api does not contain spam_threshold field.");
			   Assert.assertTrue(callflow.containsKey("spam_filter_enabled"),"callflow/callflow api does not contain spam_filter_enabled field.");
			   Assert.assertTrue(callflow.containsKey("webhook_enabled"),"callflow/callflow api does not contain webhook_enabled field.");
			   
			   // Check data type of fields
			   Assert.assertTrue(callflow.get("call_flow_id").getClass().getName().equals("java.lang.Long"),"");
			   Assert.assertTrue(callflow.get("call_flow_name").getClass().getName().equals("java.lang.String"));
			   Assert.assertTrue(callflow.get("group_id").getClass().getName().equals("java.lang.Long"));
			   Assert.assertTrue(callflow.get("channel_id").getClass().getName().equals("java.lang.Long"));
			   Assert.assertTrue(callflow.get("call_flow_status").getClass().getName().equals("java.lang.String"));
			   Assert.assertTrue(callflow.get("created_at").getClass().getName().equals("java.lang.String"));	   	   
			   Assert.assertTrue(callflow.get("call_flow_created").getClass().getName().equals("java.lang.String"));   
			   Assert.assertTrue(callflow.get("routable_type").getClass().getName().equals("java.lang.String"));
			   
			   // Check fields are not null
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(callflow.get("group_id"), "group_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("channel_id"), "channel_id");
			   HelperClass.multiple_assertnotEquals(callflow.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(callflow.get("created_at"), "created_at");
		   }
		}
	}
	
  // @Test(priority=12)
	public void callflow_callflow_with_1000_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with 1000 limit
		test = extent.startTest("callflow_callflow_with_1000_limit", "To validate whether user is able to get callflows through callflow/callflow api with 1000 limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int limit = 1000;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit",Integer.toString(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with 1000 limit");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when 1000 limit value is passed.");
		   test.log(LogStatus.PASS, "Check callflow/callflow api returns success when 1000 limit value is passed.");
		   Assert.assertNull(json.get("err"), "API returns null in error when 1000 limit value is passed.");
		   test.log(LogStatus.PASS, "Check callflow/callflow api returns success when 1000 limit value is passed.");
		   JSONArray array = (JSONArray)json.get("data");
		   
		   Assert.assertTrue(array.size()<=limit, "callflows callflow return records more than defined limit value when 1000 limit value is passed.");
		   test.log(LogStatus.PASS, "Check whether callflows callflow return records according to passed limit value");
		   // Get the callflow from the callflow/callflow
		   for(int i=0;i<array.size();i++){
			   JSONObject callflow = (JSONObject)array.get(i);
			   // Check response contains the fields
			   Assert.assertNotNull(callflow);
		   }
		}
	}	
	
  // @Test(priority=13)
	public void callflow_callflow_with_more_than_1000_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with more than 1000 limit
		test = extent.startTest("callflow_callflow_with_more_than_1000_limit", "To validate whether user is able to get callflows through callflow/callflow api with more than 1000 limit value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int limit = 1001;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit",Integer.toString(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with more than 1000 limit");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when more than 1000("+limit+") limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when more than 1000("+limit+") limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when more than 1000("+limit+") limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when more than 1000("+limit+") limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when more than 1000("+limit+") limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> path_list = new ArrayList<String>();
			path_list.add("paths");
			path_list.add("/callflow/callflow");
			path_list.add("get");
			path_list.add("parameters");
			path_list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, path_list, "path is not valid when more than 1000("+limit+") limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MAXIMUM", "Invalid code value is returned in response when more than 1000("+limit+") limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit+" is greater than maximum 1000", "Invalid message value is returned in response when more than 1000("+limit+") limit value is passed");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "Maximum number of records to return", "Invalid description value is returned in response when more than 1000("+limit+") limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when more than 1000("+limit+") limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when more than 1000("+limit+") limit value is passed.");
		}
	}	
	
  // @Test(priority=14)
	public void callflow_callflow_with_blank_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with blank offset value
		test = extent.startTest("callflow_callflow_with_blank_offset", "To validate whether user is able to get callflows through callflow/callflow api with blank offset value.");
		test.assignCategory("CFA GET callflow/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank offset value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank offset value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank offset value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when blank offset value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when blank offset value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank offset value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank offset value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank offset value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank offset value is passed.");
		}
	}	
	
  // @Test(priority=15)
	public void callflow_callflow_with_invalid_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid offset value
		test = extent.startTest("callflow_callflow_with_invalid_offset", "To validate whether user is able to get callflows through callflow/callflow api with invalid offset value.");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] invalid_offsets = {"abc","!@$#","123abc","abc123"};
		for(String offset:invalid_offsets){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("offset", offset));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+offset+") offset value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+offset+") offset value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+offset+") offset value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+offset+") offset value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset+") offset value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when invalid("+offset+") offset value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid("+offset+") offset value is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+offset+") offset value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset+") offset value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+offset+") offset value is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+offset+") offset value is passed.");
			}
		}
	}
	
  // @Test(priority=16)
	public void callflow_callflow_with_negative_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with negative offset value
		test = extent.startTest("callflow_callflow_with_invalid_offset", "To validate whether user is able to get callflows through callflow/callflow api with negative offset value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int offset = -10;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("offset",Integer.toString(offset)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with negative("+offset+") offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative("+offset+") offset value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative("+offset+") offset value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative("+offset+") offset value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (offset): Value failed JSON Schema validation", "Invalid message value is returned in response when negative("+offset+") offset value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when negative("+offset+") offset value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> path_list = new ArrayList<String>();
			path_list.add("paths");
			path_list.add("/callflow/callflow");
			path_list.add("get");
			path_list.add("parameters");
			path_list.add("1");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, path_list, "path is not valid when zero("+offset+") offset value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+offset+") offset value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+offset+" is less than minimum 0", "Invalid message value is returned in response when negative("+offset+") offset value is passed");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "Number of records from the beginning to start the data set from", "Invalid description value is returned in response when negative("+offset+") offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+offset+") offset value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+offset+") offset value is passed.");
		}
	}
	
  // @Test(priority=17)
	public void callflow_callflow_with_zero_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api without offset value
		test = extent.startTest("callflow_callflow_with_zero_limit", "To validate whether user is able to get callflows through callflow/callflow api with zero offset value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int offset = 0;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response_without_offset = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response_without_offset.getStatusLine().getStatusCode() == 500 || response_without_offset.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response_without_offset.getStatusLine().getStatusCode()+" "+response_without_offset.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method without passing offset parameter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response_without_offset.getEntity().getContent()));
		String line = ""; 
		JSONObject json_response_without_offset = new JSONObject(), json_response_with_offset=new JSONObject();
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   json_response_without_offset = (JSONObject) parser.parse(line);
		   String result_data = json_response_without_offset.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when offset is not passed.");
		}
		test.log(LogStatus.PASS, "API returns success when offset is not passed.");
		
		// Execute callflow/callflow api with zero offset value
		list.add(new BasicNameValuePair("offset",Integer.toString(0)));
		CloseableHttpResponse response_with_offset = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response_with_offset.getStatusLine().getStatusCode() == 500 || response_with_offset.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response_with_offset.getStatusLine().getStatusCode()+" "+response_with_offset.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with zero("+offset+") offset value");
		rd = new BufferedReader(new InputStreamReader(response_with_offset.getEntity().getContent()));
		line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   json_response_with_offset = (JSONObject) parser.parse(line);
		   String result_data = json_response_with_offset.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when 0 offset value is passed.");
		}
		test.log(LogStatus.PASS, "API returns success when 0 offset value is passed.");
		Assert.assertEquals(json_response_without_offset, json_response_with_offset, "offset 0 is skipped logs.");
		test.log(LogStatus.PASS, "Check 0 offset is not replacing any logs in callflow/callflow api.");
	}
	
  // @Test(priority=18)
	public void callflow_callflow_with_valid_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid offset value
		test = extent.startTest("callflow_callflow_with_valid_offset", "To validate whether user is able to get callflows through callflow/callflow api with valid offset value.");
		test.assignCategory("CFA GET callflow/callflow API");
		int offset = 5;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response_without_offset = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response_without_offset.getStatusLine().getStatusCode() == 500 || response_without_offset.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response_without_offset.getStatusLine().getStatusCode()+" "+response_without_offset.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method without passing offset parameter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response_without_offset.getEntity().getContent()));
		String line = ""; String[] callflow_id = new String[offset+1];
		JSONObject json_response_without_offset = new JSONObject(), json_response_with_offset=new JSONObject();
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   json_response_without_offset = (JSONObject) parser.parse(line);
		   String result_data = json_response_without_offset.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when offset is not passed.");
		   JSONArray array= (JSONArray)json_response_without_offset.get("data");
		   for(int i=0;i<offset+1;i++){
			   JSONObject nth_callflow_data =(JSONObject) array.get(i);
			   String nth_callflow_id = nth_callflow_data.get("call_flow_id").toString();
			   callflow_id[i] = nth_callflow_id;
		   }
		}
		test.log(LogStatus.PASS, "API returns success when offset is not passed.");
		
		// Execute callflow/callflow api with zero offset value
		list.add(new BasicNameValuePair("offset",Integer.toString(offset)));
		CloseableHttpResponse response_with_offset = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response_with_offset.getStatusLine().getStatusCode() == 500 || response_with_offset.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response_with_offset.getStatusLine().getStatusCode()+" "+response_with_offset.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+offset+") offset value");
		rd = new BufferedReader(new InputStreamReader(response_with_offset.getEntity().getContent()));
		line = "";
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   json_response_with_offset = (JSONObject) parser.parse(line);
		   String result_data = json_response_with_offset.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when offset is not passed.");
		   JSONArray array= (JSONArray) json_response_with_offset.get("data");
		   String[] callflow_id_temp = new String[offset];
		   callflow_id_temp = Arrays.copyOfRange(callflow_id,0,offset);
		   System.out.println("callflow_id_temp: "+callflow_id_temp);
		   boolean element_exist = false;
		   for(String callflow_id_val:callflow_id_temp){
			   for(int n = 0; n < array.size(); n++)
			   {
			       JSONObject object = (JSONObject)array.get(n);
			       if(object.get("call_flow_id").toString().equals(callflow_id[offset])){
			    	   element_exist = true;
			       }
	   			   Assert.assertNotEquals(object.get("call_flow_id").toString(), callflow_id_val, "callflows are not skipped when offset is used");
			   }
		   }
		   Assert.assertTrue(element_exist);
		}
		test.log(LogStatus.PASS, "API returns success when valid offset value is passed.");
		test.log(LogStatus.PASS, "Check offset is skipping defined number of logs.");
	}
	
  // @Test(priority=19)
	public void callflow_callflow_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("callflow_callflow_with_blank_filter", "To validate whether user is able to get callflows through callflow/callflow api with blank filter value");
		test.assignCategory("CFA GET callflow/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank filter value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertEquals(array.size(), 100);
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when blank filter value is passed.");
		   test.log(LogStatus.PASS, "API returns success when blank filter value is passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when blank filter value is passed.");
		   test.log(LogStatus.PASS, "err is null when blank filter value is passed.");
		}	
	}	
	
  // @Test(priority=20)	
	public void callflow_callflow_with_valid_filter_for_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for call_flow_id
		test = extent.startTest("callflow_callflow_with_valid_filter_for_call_flow_id", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for callflow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int agency_callflow = 5484, company_callflow = 5521, location_callflow = 5524;
		int[] callflow_ids = {agency_callflow,company_callflow,location_callflow};
		for(int callflow_id:callflow_ids){
			String[] operators = {"=","!="};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_id"+encoded_operator+callflow_id));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+(callflow_id==agency_callflow?"agency":callflow_id==company_callflow?"company":"location")+" level callflow) filter for call_flow_id with "+operator+" operator.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   // Get the callflow from the callflow/callflow
				   JSONObject callflow = (JSONObject)array.get(0);
				   // Check whether callflow/callflow returns 1 record valid call_flow_id is passed for filter
				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return record when valid("+(callflow_id==agency_callflow?"agency":callflow_id==company_callflow?"company":"location")+" level callflow) filter for call_flow_id with "+operator+" operator.");
				   if(operator.equals("="))
					   Assert.assertEquals(callflow.get("call_flow_id").toString(), String.valueOf(callflow_id), "callflow/callflow api does not return searched callflow when valid("+(callflow_id==agency_callflow?"agency":callflow_id==company_callflow?"company":"location")+" level callflow) filter for call_flow_id with "+operator+" operator.");
				   else
					   Assert.assertNotEquals(callflow.get("call_flow_id").toString(), String.valueOf(callflow_id), "callflow/callflow api does not return searched callflow when valid("+(callflow_id==agency_callflow?"agency":callflow_id==company_callflow?"company":"location")+" level callflow) filter for call_flow_id with "+operator+" operator.");
				}
			}	
		}
	}
	
	@Test(priority=21)	
	public void callflow_callflow_with_greater_or_less_filter_for_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with greater or less than filter for call_flow_id
		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_callflow_id", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for call_flow_id");
		test.assignCategory("CFA GET callflow/callflow API");

		Map<String, Object> confCampaignHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);	
		int call_flow_id = (int) confCampaignHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID);	
		
		String[] operators = {">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("limit", "500"));
			list.add(new BasicNameValuePair("filter", "call_flow_id"+encoded_operator+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for call_flow_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   
			   Boolean element_exist = false;
			   for(int n = 0; n < array.size(); n++)
			   {
			       JSONObject object = (JSONObject)array.get(n);
			       if(object.get("call_flow_id").toString().equals(String.valueOf(call_flow_id))){
			    	   element_exist = true;
			       }
			       int route_id = Integer.parseInt(object.get("call_flow_id").toString());
	   			   if(operator.equals(">="))
	   				   Assert.assertTrue(route_id>=call_flow_id,"callflow/callflow api return result which have call_flow_id greater than equal to entered call_flow_id. Found call_flow_id: "+call_flow_id); 
	   			   else if(operator.equals("<="))
	   				   Assert.assertTrue(route_id<=call_flow_id,"callflow/callflow api return result which have call_flow_id less than equal to entered call_flow_id. Found call_flow_id: "+call_flow_id); 
			   }
			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for call_flow_id");
			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for call_flow_id field to filter the result.");
			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for call_flow_id field to filter the result");
			}
		}	
	}	
	
  // @Test(priority=22)
	public void callflow_callflow_with_invalid_filter_operator_for_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for callflow_id
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_callflow_id", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for call_flow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int call_flow_id = 5484;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_id"+encoded_operator+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for call_flow_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_id"+operator+String.valueOf(call_flow_id));
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_id");
			}	   
		}
	}	
	
  // @Test(priority=23)	
	public void callflow_callflow_with_filter_for_nonexist_or_other_billing_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing or other billing call_flow_id
		test = extent.startTest("callflow_callflow_with_filter_for_nonexist_or_other_billing_callflow_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing or other billing call_flow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int non_existing_callflow = 100000, other_billing_callflow = 7800;
		int[] call_flow_ids = {non_existing_callflow, other_billing_callflow};
		for(int call_flow_id:call_flow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+(call_flow_id==non_existing_callflow?"non existing":"other billing")+" callflow id for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when non existing or other billing call_flow_id is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when "+(call_flow_id==non_existing_callflow?"non existing":"other billing")+" call_flow_id is entered for filter");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when non existing or other billing call_flow_id is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when "+(call_flow_id==non_existing_callflow?"non existing":"other billing")+" call_flow_id is entered for filter");
			}
		}
	}
	
  // @Test(priority=24)	// Not working
	public void callflow_callflow_with_filter_for_invalid_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for invalid call_flow_id
		test = extent.startTest("callflow_callflow_with_filter_for_invalid_callflow_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid call_flow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] callflow_ids = {"abc","!@#","abc12", "12ab"};
		for(String call_flow_id:callflow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid callflow id for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid call_flow_id is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when invalid call_flow_id is entered for filter. Defect Reported: CT-17119");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when invalid call_flow_id is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid call_flow_id is entered for filter");
			}	
		}
	}	
	
  // @Test(priority=25)
	public void callflow_callflow_with_filter_for_blank_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank call_flow_id
		test = extent.startTest("callflow_callflow_with_filter_for_blank_call_flow_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank call_flow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_id = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank call_flow_id is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank call_flow_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank call_flow_id is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank call_flow_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank call_flow_id is passed in filter.");
		}
	}			
	
  // @Test(priority=26)	
	public void callflow_callflow_with_valid_filter_for_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for call_flow_name
		test = extent.startTest("callflow_callflow_with_valid_filter_for_call_flow_name", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for callflow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String agency_callflow = "APICallFlow_Agency", company_callflow = "CompanyAPI", location_callflow = "LocationAPI(Dont Delete)";
		String[] callflows = {agency_callflow,company_callflow,location_callflow};
		for(String call_flow_name:callflows){
			String[] operators = {"=","!="};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_name"+encoded_operator+call_flow_name));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+(call_flow_name.equals(agency_callflow)?"agency":call_flow_name.equals(company_callflow)?"company":"location")+" level callflow) filter for company_callflow.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   for(int i=0; i<array.size(); i++){
					   // Get the callflow from the callflow/callflow
					   JSONObject callflow = (JSONObject)array.get(i);
					   // Check whether callflow/callflow returns 1 record valid call_flow_name is passed for filter
					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+(call_flow_name.equals(agency_callflow)?"agency":call_flow_name.equals(company_callflow)?"company":"location")+" level callflow) filter for call_flow_name with "+operator+" operator.");
					   if(operator.equals("="))
						   Assert.assertEquals(callflow.get("call_flow_name").toString(), call_flow_name, "callflow/callflow api does not return searched callflow when valid("+(call_flow_name.equals(agency_callflow)?"agency":call_flow_name.equals(company_callflow)?"company":"location")+" level callflow) filter for call_flow_name with "+operator+" operator.");
					   else
						   Assert.assertNotEquals(callflow.get("call_flow_name").toString(), call_flow_name, "callflow/callflow api does not return searched callflow when valid("+(call_flow_name.equals(agency_callflow)?"agency":call_flow_name.equals(company_callflow)?"company":"location")+" level callflow) filter for call_flow_name with "+operator+" operator.");
				   }
				   test.log(LogStatus.PASS, "Check callflow/callflow api returns searched callflow when valid("+(call_flow_name.equals(agency_callflow)?"agency":call_flow_name.equals(company_callflow)?"company":"location")+" level callflow) filter for call_flow_name with "+operator+" operator.");
				}
			}	
		}
	}	
	
  // @Test(priority=27)
	public void callflow_callflow_with_invalid_filter_operator_for_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for call_flow_name
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_call_flow_name", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for call_flow_name");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_name = "APICallFlow_Agency";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_name"+encoded_operator+call_flow_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for call_flow_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   if(operator.equals(">=")||operator.equals("<="))
				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : call_flow_name"+operator+call_flow_name);
			   else
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_name"+operator+call_flow_name);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_name");
			}	   
		}
	}	
	
  // @Test(priority=28)
	public void callflow_callflow_with_filter_for_nonexist_or_other_billing_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing call_flow_name
		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_call_flow_name", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing or other billing call_flow_name");
		test.assignCategory("CFA GET callflow/callflow API");
		String non_existing_callflow = "NonExistingCallFlow", other_billing_callflow = "OtherBillingCallFlow";
		String[] call_flow_names = {non_existing_callflow,other_billing_callflow};
		for(String call_flow_name:call_flow_names){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_name%3d"+call_flow_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+(call_flow_name.equals(non_existing_callflow)?"non existing":"other billing")+" call_flow_name for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when non existing/other billing call_flow_name is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when "+(call_flow_name.equals(non_existing_callflow)?"non existing":"other billing")+" call_flow_name is entered for filter");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when non existing/other billing call_flow_name is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when "+(call_flow_name.equals(non_existing_callflow)?"non existing":"other billing")+" call_flow_name is entered for filter");
			}
		}
	}
	
  // @Test(priority=29)
	public void callflow_callflow_with_filter_for_blank_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank call_flow_name
		test = extent.startTest("callflow_callflow_with_filter_for_blank_call_flow_name", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank call_flow_name");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_name = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_name%3d"+call_flow_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank call_flow_name is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank call_flow_name is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank call_flow_name is passed in filter.");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank call_flow_name is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank call_flow_name is passed in filter.");
		}
	}		
	
  // @Test(priority=30)	
	public void callflow_callflow_with_valid_filter_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for group_id
		test = extent.startTest("callflow_callflow_with_valid_filter_for_group_id", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for callflow_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int agency_group = 8, company_group = 114, location_group = 116;
		int[] group_ids = {agency_group,company_group,location_group};
		for(int group_id:group_ids){
			String[] operators = {"=","!="};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+group_id));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group) filter for group_id with "+operator+" operator.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   // Get the callflow from the callflow/callflow
				   JSONObject callflow = (JSONObject)array.get(0);
				   // Check whether callflow/callflow returns 1 record valid call_flow_id is passed for filter
				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return atleast 1 record when valid("+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group) filter for group_id with "+operator+" operator.");
				   if(operator.equals("="))
					   Assert.assertEquals(callflow.get("group_id").toString(), String.valueOf(group_id), "callflow/callflow api does not return searched callflow when valid("+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level callflow) filter for group_id with "+operator+" operator.");
				   else
					   Assert.assertNotEquals(callflow.get("group_id").toString(), String.valueOf(group_id), "callflow/callflow api does not return searched callflow when valid("+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level callflow) filter for group_id with "+operator+" operator.");
				}
			}	
		}
	}	
	
  // @Test(priority=31)	
	public void callflow_callflow_with_greater_or_less_filter_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with greater or less than filter for group_id
		test = extent.startTest("callflow_callflow_with_valid_filter_for_group_id", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int group_id = 114;
		String[] operators = {">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("limit", "500"));
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   
			   Boolean element_exist = false;
			   for(int n = 0; n < array.size(); n++)
			   {
			       JSONObject object = (JSONObject)array.get(n);
			       if(object.get("group_id").toString().equals(String.valueOf(group_id))){
			    	   element_exist = true;
			       }
			       int ouid = Integer.parseInt(object.get("group_id").toString());
	   			   if(operator.equals(">="))
	   				   Assert.assertTrue(ouid>=group_id,"callflow/callflow api return result which have group_id greater than equal to entered group_id. Found group_id: "+group_id); 
	   			   else if(operator.equals("<="))
	   				   Assert.assertTrue(ouid<=group_id,"callflow/callflow api return result which have group_id less than equal to entered group_id. Found group_id: "+group_id); 
			   }
			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for group_id");
			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for group_id field to filter the result.");
			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for group_id field to filter the result");
			}
		}	
	}	
	
  // @Test(priority=32)
	public void callflow_callflow_with_invalid_filter_operator_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for group_id
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_group_id", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int group_id = 114;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : group_id"+operator+String.valueOf(group_id));
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for group_id");
			}	   
		}
	}	
	
  // @Test(priority=33)	
	public void callflow_callflow_with_filter_for_nonexist_or_other_billing_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing or other billing group_id
		test = extent.startTest("callflow_callflow_with_filter_for_nonexist_or_other_billing_group_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing or other billing group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int non_existing_group_id = 100000, other_billing_group_id = 7800;
		int[] group_ids = {non_existing_group_id, other_billing_group_id};
		for(int group_id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id%3d"+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+(group_id==non_existing_group_id?"non existing":"other billing")+" group_id for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when non existing or other billing group_id is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when "+(group_id==non_existing_group_id?"non existing":"other billing")+" group_id is entered for filter");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when non existing or other billing group_id is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when "+(group_id==non_existing_group_id?"non existing":"other billing")+" group_id is entered for filter");
			}
		}
	}
	
  // @Test(priority=34)
	public void callflow_callflow_with_filter_for_invalid_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for invalid group_id
		test = extent.startTest("callflow_callflow_with_filter_for_invalid_group_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] group_ids = {"abc","!@#","abc12", "12ab"};
		for(String group_id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id%3d"+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid group_id for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid group_id is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when invalid group_id is entered for filter.  Defect Reported: CT-17119");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when invalid group_id is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid group_id is entered for filter");
			}	
		}
	}
	
  // @Test(priority=35)
	public void callflow_callflow_with_filter_for_blank_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank group_id
		test = extent.startTest("callflow_callflow_with_filter_for_blank_group_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String group_id = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "group_id%3d"+group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank group_id is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank group_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank group_id is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank group_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank group_id is passed in filter.");
		}
	}	
	
  // @Test(priority=36)	
	public void callflow_callflow_with_valid_filter_for_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for channel_id
		test = extent.startTest("callflow_callflow_with_valid_filter_for_channel_id", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for channel_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int channel_id = 25;
		String[] operators = {"=","!="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "channel_id"+encoded_operator+channel_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+channel_id+") channel_id is passed in filter with "+operator+" operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   JSONObject callflow = (JSONObject)array.get(0);
			   // Check whether callflow/callflow returns 1 record valid channel_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return atleast 1 record when valid("+channel_id+") channel_id is passed in filter with "+operator+" operator.");
			   for(int i=0;i<array.size();i++){
				   if(operator.equals("="))
					   Assert.assertEquals(callflow.get("channel_id").toString(), String.valueOf(channel_id), "callflow/callflow api does not return searched callflow when valid("+channel_id+") channel_id is passed in filter with "+operator+" operator.");
				   else
					   Assert.assertNotEquals(callflow.get("channel_id").toString(), String.valueOf(channel_id), "callflow/callflow api does not return searched callflow when valid("+channel_id+") channel_id is passed in filter with "+operator+" operator.");
			   }
			}
		}	
	}	
	
  // @Test(priority=37)	
	public void callflow_callflow_with_greater_or_less_filter_for_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with greater or less than filter for channel_id
		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_channel_id", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for channel_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int channel_id = 25;
		String[] operators = {">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("limit", "500"));
			list.add(new BasicNameValuePair("filter", "channel_id"+encoded_operator+channel_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for channel_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   
			   Boolean element_exist = false;
			   for(int n = 0; n < array.size(); n++)
			   {
			       JSONObject object = (JSONObject)array.get(n);
			       if(object.get("channel_id").toString().equals(String.valueOf(channel_id))){
			    	   element_exist = true;
			       }
			       int channel = Integer.parseInt(object.get("channel_id").toString());
	   			   if(operator.equals(">="))
	   				   Assert.assertTrue(channel>=channel_id,"callflow/callflow api return result which have group_id greater than or equal to entered channel_id. Found channel_id: "+channel_id); 
	   			   else if(operator.equals("<="))
	   				   Assert.assertTrue(channel<=channel_id,"callflow/callflow api return result which have group_id less than or equal to entered channel_id. Found channel_id: "+channel_id); 
			   }
			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for channel_id");
			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for channel_id field to filter the result.");
			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for channel_id field to filter the result");
			}
		}	
	}	
	
  // @Test(priority=38)
	public void callflow_callflow_with_invalid_filter_operator_for_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for channel_id
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_channel_id", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for channel_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int channel_id = 25;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "channel_id"+encoded_operator+channel_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for channel_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : channel_id"+operator+String.valueOf(channel_id));
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for channel_id");
			}	   
		}
	}	
	
  // @Test(priority=39)	
	public void callflow_callflow_with_filter_for_nonexisting_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing channel_id
		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_channel_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int non_existing_channel_id = 501;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "channel_id%3d"+non_existing_channel_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/list api method with non existing group_id for filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing group_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when non existing group_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when non existing group_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing group_id is entered for filter");
		}
	}
	
  // @Test(priority=40)
	public void callflow_callflow_with_filter_for_invalid_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for invalid channel_id
		test = extent.startTest("callflow_callflow_with_filter_for_invalid_channel_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid channel_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] channel_ids = {"abc","!@#","abc12", "12ab"};
		for(String channel_id:channel_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "channel_id%3d"+channel_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid channel_id for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid channel_id is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when invalid channel_id is entered for filter. Defect Reported: CT-17119");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when invalid channel_id is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid channel_id is entered for filter");
			}	
		}
	}
	
  // @Test(priority=41)
	public void callflow_callflow_with_filter_for_blank_channel_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank channel_id
		test = extent.startTest("callflow_callflow_with_filter_for_blank_channel_id", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank channel_id");
		test.assignCategory("CFA GET callflow/callflow API");
		String channel_id = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "channel_id%3d"+channel_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/list api method with blank channel_id is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank channel_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank channel_id is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank channel_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank channel_id is passed in filter.");
		}
	}	
	
  // @Test(priority=42)	
	public void callflow_callflow_with_valid_filter_for_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for call_flow_status
		test = extent.startTest("callflow_callflow_with_valid_filter_for_call_flow_status", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for call_flow_status");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] operators = {"=","!="};
		String[] callflow_status = {"active","inactive","referral"};
		for(String call_flow_status:callflow_status){
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_status"+encoded_operator+call_flow_status));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+call_flow_status+") call_flow_status is passed in filter with "+operator+" operator.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   // Get the callflow from the callflow/callflow
				   for(int i=0;i<array.size();i++){
					   JSONObject callflow = (JSONObject)array.get(i);
					   // Check whether callflow/callflow returns at least 1 record valid call_flow_status is passed for filter
					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+call_flow_status+") call_flow_status is passed in filter with "+operator+" operator..");
					   if(operator.equals("="))
						   Assert.assertEquals(callflow.get("call_flow_status").toString(), call_flow_status, "callflow/callflow api does not return searched callflow when valid("+call_flow_status+") call_flow_status is passed in filter with "+operator+" operator.");   
					   else
						   Assert.assertNotEquals(callflow.get("call_flow_status").toString(), call_flow_status, "callflow/callflow api does not return searched callflow when valid("+call_flow_status+") call_flow_status is passed in filter with "+operator+" operator.");   
				   }
				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for call_flow_status field.");
				}
			}	
		}
	}	
	
  // @Test(priority=43)
	public void callflow_callflow_with_invalid_filter_operator_for_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for call_flow_status
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_call_flow_status", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for call_flow_status");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_status = "Linkedin";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_status"+encoded_operator+call_flow_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for call_flow_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   if(operator.equals(">=")||operator.equals("<="))
				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : call_flow_status"+operator+call_flow_status);
			   else
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_status"+operator+call_flow_status);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_status");
			}	   
		}
	}	
	
  // @Test(priority=44)
	public void callflow_callflow_with_filter_for_nonexisting_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing call_flow_status
		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_call_flow_status", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing call_flow_status");
		test.assignCategory("CFA GET callflow/callflow API");
		String non_existing_call_flow_status = "NonExistingChannel";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_status%3d"+non_existing_call_flow_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with non existing call_flow_status is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing call_flow_status is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when non existing call_flow_status is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when non existing call_flow_status is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing call_flow_status is passed in filter.");
		}
	}
	
  // @Test(priority=45)
	public void callflow_callflow_with_filter_for_deleted_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for deleted call_flow_status
		test = extent.startTest("callflow_callflow_with_filter_for_deleted_call_flow_status", "To validate whether user is able to get callflow through callflow/callflow api with filter for deleted call_flow_status");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_status = "deleted";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_status%3d"+call_flow_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with deleted call_flow_status is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when deleted call_flow_status is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when deleted call_flow_status is passed in filter.");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when deleted call_flow_status is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when deleted call_flow_status is passed in filter.");
		}
	}	
	
  // @Test(priority=46)
	public void callflow_callflow_with_filter_for_blank_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank call_flow_status
		test = extent.startTest("callflow_callflow_with_filter_for_blank_call_flow_status", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank call_flow_status");
		test.assignCategory("CFA GET callflow/callflow API");
		String call_flow_status = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_status%3d"+call_flow_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank call_flow_status is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank call_flow_status is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank call_flow_status is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank call_flow_status is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank call_flow_status is passed in filter.");
		}
	}		
	
  // @Test(priority=47)	
	public void callflow_callflow_with_valid_filter_for_ring_delay() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for ring_delay
		test = extent.startTest("callflow_callflow_with_valid_filter_for_ring_delay", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for ring_delay");
		test.assignCategory("CFA GET callflow/callflow API");
		int ring_delay = 0;
		String[] operators = {"=","!="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ring_delay"+encoded_operator+ring_delay));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+ring_delay+") ring_delay is passed in filter with "+operator+" operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Get the callflow from the callflow/callflow
			   for(int i=0;i<array.size();i++){
				   JSONObject callflow = (JSONObject)array.get(i);
				   // Check whether callflow/callflow returns at least 1 record valid ring_delay is passed for filter
				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+ring_delay+") ring_delay is passed in filter with "+operator+" operator.");
				   if(operator.equals("="))
					   Assert.assertEquals(callflow.get("ring_delay").toString(), String.valueOf(ring_delay), "callflow/callflow api does not return searched callflow when valid("+ring_delay+") ring_delay is passed in filter with "+operator+" operator.");
				   else
					   Assert.assertNotEquals(callflow.get("ring_delay").toString(), String.valueOf(ring_delay), "callflow/callflow api does not return searched callflow when valid("+ring_delay+") ring_delay is passed in filter with "+operator+" operator.");
			   }
			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for ring_delay field.");
			}
		}
	}	
	
  // @Test(priority=48)
	public void callflow_callflow_with_invalid_filter_operator_for_ring_delay() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for ring_delay
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_ring_delay", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for ring_delay");
		test.assignCategory("CFA GET callflow/callflow API");
		int ring_delay = 0;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ring_delay"+encoded_operator+ring_delay));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for ring_delay");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : ring_delay"+operator+String.valueOf(ring_delay));
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for ring_delay");
			}	   
		}
	}	
	
  // @Test(priority=49)	
	public void callflow_callflow_with_filter_for_nonexisting_ring_delay() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing ring_delay
		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_ring_delay", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing group_id");
		test.assignCategory("CFA GET callflow/callflow API");
		int non_existing_ring_delay = 501;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "ring_delay%3d"+non_existing_ring_delay));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/list api method with non existing group_id for filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing group_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when non existing group_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when non existing group_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing group_id is entered for filter");
		}
	}
	
  // @Test(priority=50)
	public void callflow_callflow_with_filter_for_invalid_ring_delay() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for invalid ring_delay
		test = extent.startTest("callflow_callflow_with_filter_for_invalid_ring_delay", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid ring_delay");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] ring_delays = {"abc","!@#","abc12", "12ab"};
		for(String ring_delay:ring_delays){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ring_delay%3d"+ring_delay));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid ring_delay for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid ring_delay is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when invalid ring_delay is entered for filter. Defect Reported: CT-17119");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when invalid ring_delay is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid ring_delay is entered for filter");
			}	
		}
	}
	
  // @Test(priority=51)
	public void callflow_callflow_with_filter_for_blank_ring_delay() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank ring_delay
		test = extent.startTest("callflow_callflow_with_filter_for_blank_ring_delay", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank ring_delay");
		test.assignCategory("CFA GET callflow/callflow API");
		String ring_delay = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "ring_delay%3d"+ring_delay));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank ring_delay is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank ring_delay is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank ring_delay is passed in filter. Defect Reported: CT-17119");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when blank ring_delay is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank ring_delay is passed in filter.");
		}
	}	
	
  // @Test(priority=52)	
	public void callflow_callflow_with_valid_filter_for_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for default_ringto
		test = extent.startTest("callflow_callflow_with_valid_filter_for_default_ringto", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for default_ringto");
		test.assignCategory("CFA GET callflow/callflow API");
		String default_ringto = "8018786943";
		String[] operators = {"=","!="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "default_ringto"+encoded_operator+default_ringto));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+default_ringto+") default_ringto is passed in filter with "+operator+" operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Get the callflow from the callflow/callflow
			   for(int i=0;i<array.size();i++){
				   JSONObject callflow = (JSONObject)array.get(i);
				   // Check whether callflow/callflow returns at least 1 record valid default_ringto is passed for filter
				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+default_ringto+") default_ringto is passed in filter with "+operator+" operator.");
				   if(operator.equals("="))
					   Assert.assertEquals(callflow.get("default_ringto").toString(), String.valueOf(default_ringto), "callflow/callflow api does not return searched callflow when valid("+default_ringto+") default_ringto is passed in filter with "+operator+" operator.");
				   else
					   Assert.assertNotEquals(callflow.get("default_ringto").toString(), String.valueOf(default_ringto), "callflow/callflow api does not return searched callflow when valid("+default_ringto+") default_ringto is passed in filter with "+operator+" operator.");
			   }
			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for default_ringto field.");
			}
		}
	}	
	
  // @Test(priority=53)
	public void callflow_callflow_with_invalid_filter_operator_for_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid filter operator for default_ringto
		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_default_ringto", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for default_ringto");
		test.assignCategory("CFA GET callflow/callflow API");
		String default_ringto = "8018786943";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "default_ringto"+encoded_operator+default_ringto));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for default_ringto");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   if(operator.equals(">=")||operator.equals("<=")){
				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
				   JSONArray data_array = (JSONArray)json.get("data");
				   // Check data field is blank when invalid default_ringto is entered for filter
				   // Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid default_ringto is passed in filter.");
			   }
			   else{
				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : default_ringto"+operator+String.valueOf(default_ringto));
			   }
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for default_ringto");
			}	   
		}
	}	
	
  // @Test(priority=54)	
	public void callflow_callflow_with_filter_for_nonexisting_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for non existing default_ringto
		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_default_ringto", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing default_ringto");
		test.assignCategory("CFA GET callflow/callflow API");
		String non_existing_default_ringto = "5451451515";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "default_ringto%3d"+non_existing_default_ringto));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with non existing default_ringto for filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing default_ringto is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when non existing default_ringto is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when non existing default_ringto is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing default_ringto is entered for filter");
		}
	}
	
  // @Test(priority=55)	
	public void callflow_callflow_with_filter_for_non_10_digits_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for less than or greater than default_ringto
		test = extent.startTest("callflow_callflow_with_filter_for_non_10_digits_default_ringto", "To validate whether user is able to get callflow through callflow/callflow api with filter for less than or more than 10 digits default_ringto.");
		test.assignCategory("CFA GET callflow/callflow API");
		String less_than_10_default_ringto = "54514515", more_than_10_default_ringto = "801587869435";
		String[] ringto_numbers = {less_than_10_default_ringto, more_than_10_default_ringto};
		for(String default_ringto:ringto_numbers){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "default_ringto%3d"+default_ringto));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+(default_ringto.equals(less_than_10_default_ringto)?"less than":"greater than")+" 10 digits default_ringto is passed for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when less than or more than 10 digits default_ringto is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when "+(default_ringto.equals(less_than_10_default_ringto)?"less than":"greater than")+" 10 digits default_ringto is passed for filter");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when less than or more than 10 digits default_ringto is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when "+(default_ringto.equals(less_than_10_default_ringto)?"less than":"greater than")+" 10 digits default_ringto is passed for filter");
			}
		}
	}	
	
  // @Test(priority=56)
	public void callflow_callflow_with_filter_for_invalid_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for invalid default_ringto
		test = extent.startTest("callflow_callflow_with_filter_for_invalid_default_ringto", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid default_ringto");
		test.assignCategory("CFA GET callflow/callflow API");
		String[] default_ringtos = {"abc","!@#","abc12", "12ab"};
		for(String default_ringto:default_ringtos){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "default_ringto%3d"+default_ringto));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid default_ringto for filter.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid default_ringto is entered for filter
			   Assert.assertEquals(result_data, "success", "API is returning error when invalid default_ringto is entered for filter");
			   JSONArray data_array = (JSONArray)json.get("data");
			   // Check data field is blank when invalid default_ringto is entered for filter
			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid default_ringto is entered for filter");
			}	
		}
	}	
	
  // @Test(priority=57)
	public void callflow_callflow_with_filter_for_blank_default_ringto() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with filter for blank default_ringto
		test = extent.startTest("callflow_callflow_with_filter_for_blank_default_ringto", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank default_ringto");
		test.assignCategory("CFA GET callflow/callflow API");
		String default_ringto = "";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "default_ringto%3d"+default_ringto));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank default_ringto is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when blank default_ringto is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when blank default_ringto is passed in filter.");
		   JSONArray data_array = (JSONArray)json.get("data");
		   for(int i=0;i<data_array.size();i++){
			   JSONObject call_flow = (JSONObject)data_array.get(i);
			   String ringto_num = call_flow.get("default_ringto").toString();
			   Assert.assertEquals(ringto_num, default_ringto, "default_ringto is not blank in response when blank default_ringto is passed in filter.");
		   }
		   test.log(LogStatus.PASS, "Check default_ringto is blank in response when blank default_ringto is passed in filter.");
		}
	}	
    
  // @Test(priority=74)	
   	public void callflow_callflow_with_valid_filter_for_whisper_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for whisper_enabled
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_whisper_enabled", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for whisper_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] whisper_enabled_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String whisper_enabled:whisper_enabled_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "whisper_enabled"+encoded_operator+whisper_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+whisper_enabled+") whisper_enabled is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid whisper_enabled is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+whisper_enabled+") whisper_enabled is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   					   		Assert.assertEquals(callflow.get("whisper_enabled").toString(), whisper_enabled, "callflow/callflow api does not return searched callflow when valid("+whisper_enabled+") whisper_enabled is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("whisper_enabled").toString(), whisper_enabled, "callflow/callflow api does not return searched callflow when valid("+whisper_enabled+") whisper_enabled is passed in filter with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for whisper_enabled field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=75)
   	public void callflow_callflow_with_invalid_filter_operator_for_whisper_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for whisper_enabled
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_whisper_enabled", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for whisper_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_enabled = "true";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "whisper_enabled"+encoded_operator+whisper_enabled));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for whisper_enabled");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   System.out.println(line);
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid whisper_enabled is entered for filter
   				   // Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid whisper_enabled is passed in filter.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : whisper_enabled"+operator+whisper_enabled);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for whisper_enabled");
   			}	   
   		}
   	}	
   	
  // @Test(priority=76)
   	public void callflow_callflow_with_filter_for_invalid_whisper_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid whisper_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_whisper_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid whisper_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_enabled = "invalid_whisper_enabled";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "whisper_enabled%3d"+whisper_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid whisper_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid whisper_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid whisper_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid whisper_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid whisper_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=77)
   	public void callflow_callflow_with_filter_for_blank_whisper_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank whisper_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_whisper_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank whisper_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_enabled = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "whisper_enabled%3d"+whisper_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank whisper_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank whisper_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank whisper_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank whisper_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank whisper_enabled is passed in filter.");
   		}
   	}		
   	
  // @Test(priority=78)	
   	public void callflow_callflow_with_valid_filter_for_play_disclaimer() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for play_disclaimer
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_play_disclaimer", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for play_disclaimer");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] play_disclaimer_values = {"never","before","after"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String play_disclaimer:play_disclaimer_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "play_disclaimer"+encoded_operator+play_disclaimer));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+play_disclaimer+") play_disclaimer is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid play_disclaimer is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+play_disclaimer+") play_disclaimer is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   					   		Assert.assertEquals(callflow.get("play_disclaimer").toString(), play_disclaimer, "callflow/callflow api does not return searched callflow when valid("+play_disclaimer+") play_disclaimer is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("play_disclaimer").toString(), play_disclaimer, "callflow/callflow api does not return searched callflow when valid("+play_disclaimer+") play_disclaimer is passed in filter with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for play_disclaimer field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=79)
   	public void callflow_callflow_with_invalid_filter_operator_for_play_disclaimer() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for play_disclaimer
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_play_disclaimer", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for play_disclaimer");
		test.assignCategory("CFA GET callflow/callflow API");
   		String play_disclaimer = "never";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "play_disclaimer"+encoded_operator+play_disclaimer));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for play_disclaimer");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   if(operator.equals(">=")||operator.equals("<="))
   				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : play_disclaimer"+operator+play_disclaimer);
   			   else
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : play_disclaimer"+operator+play_disclaimer);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for play_disclaimer");
   			}	   
   		}
   	}	
   	
  // @Test(priority=80)
   	public void callflow_callflow_with_filter_for_invalid_play_disclaimer() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid play_disclaimer
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_play_disclaimer", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid play_disclaimer");
		test.assignCategory("CFA GET callflow/callflow API");
   		String play_disclaimer = "invalid_play_disclaimer";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "play_disclaimer%3d"+play_disclaimer));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid play_disclaimer is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid play_disclaimer is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid play_disclaimer is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid play_disclaimer is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid play_disclaimer is passed in filter.");
   		}
   	}
   	
  // @Test(priority=81)
   	public void callflow_callflow_with_filter_for_blank_play_disclaimer() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank play_disclaimer
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_play_disclaimer", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank play_disclaimer");
		test.assignCategory("CFA GET callflow/callflow API");
   		String play_disclaimer = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "play_disclaimer%3d"+play_disclaimer));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank play_disclaimer is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank play_disclaimer is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank play_disclaimer is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   
   		   for(int i=0; i<data_array.size(); i++){
   			  JSONObject data = (JSONObject)data_array.get(i);
   			  String data_disclaimer = data.get("play_disclaimer").toString();
   			  Assert.assertEquals(data_disclaimer, play_disclaimer, "Play disclaimer value is not returned according to applied filter.");
   		   }		 
   		   test.log(LogStatus.PASS, "play_disclaimer field in response is not empty when blank play_disclaimer is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=82)	
   	public void callflow_callflow_with_valid_filter_for_whisper_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for whisper_message
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_whisper_message", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for whisper_message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		String whisper_message = "test";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "whisper_message"+encoded_operator+whisper_message));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+whisper_message+") whisper_message is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid whisper_message is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+whisper_message+") whisper_message is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   				   		Assert.assertEquals(callflow.get("whisper_message").toString(), whisper_message, "callflow/callflow api does not return searched callflow when valid("+whisper_message+") whisper_message is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("whisper_message").toString(), whisper_message, "callflow/callflow api does not return searched callflow when valid("+whisper_message+") whisper_message is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for whisper_message field.");
   			}
   		}	
   	}	
   	
  // @Test(priority=83)
   	public void callflow_callflow_with_invalid_filter_operator_for_whisper_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for whisper_message
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_whisper_message", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for whisper_message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_message = "test";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "whisper_message"+encoded_operator+whisper_message));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for whisper_message");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   if(operator.equals(">=")||operator.equals("<="))
   				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : whisper_message"+operator+whisper_message);
   			   else
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : whisper_message"+operator+whisper_message);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for whisper_message");
   			}	   
   		}
   	}	
   	
  // @Test(priority=84)
   	public void callflow_callflow_with_filter_for_invalid_whisper_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid whisper_message
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_whisper_message", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid whisper_message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_message = "invalid_whisper_message";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "whisper_message%3d"+whisper_message));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid whisper_message is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid whisper_message is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid whisper_message is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid whisper_message is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid whisper_message is passed in filter.");
   		}
   	}
   	
  // @Test(priority=85)
   	public void callflow_callflow_with_filter_for_blank_whisper_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid whisper_message
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_whisper_message", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank whisper_message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String whisper_message = "";
   		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "whisper_message%3d"+whisper_message));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank whisper_message is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank whisper_message is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank whisper_message is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank whisper_message is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank whisper_message is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=86)	
   	public void callflow_callflow_with_valid_filter_for_postcall_ivr_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for postcall_ivr_enabled
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_postcall_ivr_enabled", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for postcall_ivr_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] postcall_ivr_enabled_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String postcall_ivr_enabled:postcall_ivr_enabled_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "postcall_ivr_enabled"+encoded_operator+postcall_ivr_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+postcall_ivr_enabled+") postcall_ivr_enabled is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid postcall_ivr_enabled is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+postcall_ivr_enabled+") postcall_ivr_enabled is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   					   		Assert.assertEquals(callflow.get("postcall_ivr_enabled").toString(), postcall_ivr_enabled, "callflow/callflow api does not return searched callflow when valid("+postcall_ivr_enabled+") postcall_ivr_enabled is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("postcall_ivr_enabled").toString(), postcall_ivr_enabled, "callflow/callflow api does not return searched callflow when valid("+postcall_ivr_enabled+") postcall_ivr_enabled is passed in filter with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for postcall_ivr_enabled field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=87)
   	public void callflow_callflow_with_invalid_filter_operator_for_postcall_ivr_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for postcall_ivr_enabled
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_postcall_ivr_enabled", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for postcall_ivr_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String postcall_ivr_enabled = "true";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "postcall_ivr_enabled"+encoded_operator+postcall_ivr_enabled));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for postcall_ivr_enabled");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid postcall_ivr_enabled is entered for filter
   				   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid postcall_ivr_enabled is passed in filter.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : postcall_ivr_enabled"+operator+postcall_ivr_enabled);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for postcall_ivr_enabled");
   			}	   
   		}
   	}	
   	
  // @Test(priority=88)
   	public void callflow_callflow_with_filter_for_invalid_postcall_ivr_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid postcall_ivr_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_postcall_ivr_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid postcall_ivr_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String postcall_ivr_enabled = "invalid_postcall_ivr_enabled";
   		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "postcall_ivr_enabled%3d"+postcall_ivr_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid postcall_ivr_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid postcall_ivr_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid postcall_ivr_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid postcall_ivr_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid postcall_ivr_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=89)
   	public void callflow_callflow_with_filter_for_blank_postcall_ivr_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank postcall_ivr_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_postcall_ivr_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank postcall_ivr_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String postcall_ivr_enabled = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "postcall_ivr_enabled%3d"+postcall_ivr_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank postcall_ivr_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank postcall_ivr_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank postcall_ivr_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank postcall_ivr_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank postcall_ivr_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=90)	
   	public void callflow_callflow_with_valid_filter_for_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for repeat_interval
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_repeat_interval", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for repeat_interval.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		int repeat_interval = 72;
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "repeat_interval"+encoded_operator+repeat_interval));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+repeat_interval+") repeat_interval is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid repeat_interval is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+repeat_interval+") repeat_interval is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   					   Assert.assertEquals(callflow.get("repeat_interval").toString(), String.valueOf(repeat_interval), "callflow/callflow api does not return searched callflow when valid("+repeat_interval+") repeat_interval is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("repeat_interval").toString(), String.valueOf(repeat_interval), "callflow/callflow api does not return searched callflow when valid("+repeat_interval+") repeat_interval is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for repeat_interval field.");
   			}
   		}
   	}	
   	
  // @Test(priority=91)	
   	public void callflow_callflow_with_greater_or_less_filter_for_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with greater or less than filter for repeat_interval
   		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_repeat_interval", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for repeat_interval");
		test.assignCategory("CFA GET callflow/callflow API");
   		int repeat_interval = 72;
   		String[] operators = {">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "repeat_interval"+encoded_operator+repeat_interval));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for repeat_interval");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   
   			   Boolean element_exist = false;
   			   for(int n = 0; n < array.size(); n++)
   			   {
   			       JSONObject object = (JSONObject)array.get(n);
   			       if(object.get("repeat_interval").toString().equals(String.valueOf(repeat_interval))){
   			    	   element_exist = true;
   			       }
   			       int route_id = Integer.parseInt(object.get("repeat_interval").toString());
   	   			   if(operator.equals(">="))
   	   				   Assert.assertTrue(route_id>=repeat_interval,"callflow/callflow api return result which have repeat_interval greater than equal to entered repeat_interval. Found repeat_interval: "+repeat_interval); 
   	   			   else if(operator.equals("<="))
   	   				   Assert.assertTrue(route_id<=repeat_interval,"callflow/callflow api return result which have repeat_interval less than equal to entered repeat_interval. Found repeat_interval: "+repeat_interval); 
   			   }
   			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for repeat_interval");
   			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for repeat_interval field to filter the result.");
   			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for repeat_interval field to filter the result");
   			}
   		}	
   	}
   	
  // @Test(priority=92)
   	public void callflow_callflow_with_invalid_filter_operator_for_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for repeat_interval
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_repeat_interval", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for repeat_interval");
		test.assignCategory("CFA GET callflow/callflow API");
   		int repeat_interval = 72;
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "repeat_interval"+encoded_operator+repeat_interval));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for repeat_interval");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : repeat_interval"+operator+repeat_interval);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for repeat_interval");
   			}	   
   		}
   	}	
   	
  // @Test(priority=93)
   	public void callflow_callflow_with_filter_for_invalid_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid repeat_interval
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_repeat_interval", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid repeat_interval");
		test.assignCategory("CFA GET callflow/callflow API");
   		String repeat_interval = "invalid_repeat_interval";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "repeat_interval%3d"+repeat_interval));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid repeat_interval is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid repeat_interval is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid repeat_interval is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid repeat_interval is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid repeat_interval is passed in filter.");
   		}
   	}
   	
  // @Test(priority=94)
   	public void callflow_callflow_with_filter_for_blank_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank repeat_interval
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_repeat_interval", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank repeat_interval");
		test.assignCategory("CFA GET callflow/callflow API");
   		String repeat_interval = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "repeat_interval%3d"+repeat_interval));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank repeat_interval is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank repeat_interval is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank repeat_interval is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank repeat_interval is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank repeat_interval is passed in filter.");
   		}
   	}
   	
  // @Test(priority=95)	
   	public void callflow_callflow_with_valid_filter_for_routable_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for routable_type
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_routable_type", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for routable_type.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="}, routabled_types = {"SimpleRoute","PercentageBasedRoute","IvrRoute2","GeoRoute"};
   		String encoded_operator = "";
   		for(String routable_type:routabled_types){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "routable_type"+encoded_operator+routable_type));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+routable_type+") routable_type is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid routable_type is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+routable_type+") routable_type is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   						   Assert.assertEquals(callflow.get("routable_type").toString(), String.valueOf(routable_type), "callflow/callflow api does not return searched callflow when valid("+routable_type+") routable_type is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("routable_type").toString(), String.valueOf(routable_type), "callflow/callflow api does not return searched callflow when valid("+routable_type+") routable_type is passed in filter with "+operator+" operator.");
   					   
   					   if(routable_type.equals("PercentageBasedRoute")&&operator.equals("=")){
   						   Assert.assertTrue(callflow.containsKey("percentage_route"), "percentage_route field is not present for percentage route.");
   						   JSONObject percentage_route = (JSONObject)callflow.get("percentage_route");   						   
   						   Assert.assertTrue(percentage_route.containsKey("percentage_options"), "percentage_options field is not present for percentage route.");
   						   JSONArray percentage_options = (JSONArray)percentage_route.get("percentage_options");  
   						   for(int j=0;j<percentage_options.size();j++){
   							   JSONObject percentage_option = (JSONObject) percentage_options.get(j);
   							   Assert.assertTrue(percentage_option.containsKey("percentage"), "percentage field is not present in response for percentage route.");
   							   Assert.assertTrue(percentage_option.containsKey("per_target_did"), "per_target_did field is not present in response for percentage route.");
   							   Assert.assertNotNull(percentage_option.get("percentage"), "percentage field is null in response for percentage route.");
							   Assert.assertNotNull(percentage_option.get("per_target_did"), "per_target_did field is null in response for percentage route.");
   						   }   						   
   					   }
   					   
   					   else if(routable_type.equals("IvrRoute2")&&operator.equals("=")){
   						   Assert.assertTrue(callflow.containsKey("ivr_route"), "ivr_route field is not present for percentage route.");
						   JSONObject ivr_route = (JSONObject)callflow.get("ivr_route");   						   
						   Assert.assertTrue(ivr_route.containsKey("ivr_options"), "percentage_options field is not present for percentage route.");
						   JSONArray ivr_options = (JSONArray)ivr_route.get("ivr_options");  
   						   for(int j=0;j<ivr_options.size();j++){
   							   JSONObject ivr_option = (JSONObject) ivr_options.get(j);
   							   Assert.assertTrue(ivr_option.containsKey("ivr_vm_enabled"), "ivr_vm_enabled field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("message_enabled"), "message_enabled field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_whisper_enabled"), "ivr_whisper_enabled field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("ivr_record_enabled"), "ivr_record_enabled field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("ivr_webhook_enabled"), "ivr_webhook_enabled field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_updated_at"), "ivr_updated_at field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_email_to_notify"), "ivr_email_to_notify field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("ivr_whisper_message"), "ivr_whisper_message field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("ivr_message"), "ivr_message field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_target_did"), "ivr_target_did field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_play_disclaimer"), "ivr_play_disclaimer field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("ivr_option_id"), "ivr_option_id field is not present in response for ivr route.");
							   Assert.assertTrue(ivr_option.containsKey("value"), "value field is not present in response for ivr route.");
   							   Assert.assertTrue(ivr_option.containsKey("ivr_created_at"), "ivr_created_at field is not present in response for ivr route.");
							   
   							   Assert.assertNotNull(ivr_option.get("ivr_vm_enabled"), "ivr_vm_enabled field is null in response for ivr route.");
   							   Assert.assertNotNull(ivr_option.get("message_enabled"), "message_enabled field is null in response for ivr route.");
   							   Assert.assertNotNull(ivr_option.get("ivr_whisper_enabled"), "ivr_whisper_enabled field is null in response for ivr route.");
							   Assert.assertNotNull(ivr_option.get("ivr_record_enabled"), "ivr_record_enabled field is null in response for ivr route.");
							   Assert.assertNotNull(ivr_option.get("ivr_webhook_enabled"), "ivr_webhook_enabled field is null in response for ivr route.");
   							   Assert.assertNotNull(ivr_option.get("ivr_target_did"), "ivr_target_did field is null in response for ivr route.");
   							   Assert.assertNotNull(ivr_option.get("ivr_play_disclaimer"), "ivr_play_disclaimer field is null in response for ivr route.");
							   Assert.assertNotNull(ivr_option.get("ivr_option_id"), "ivr_option_id field is null in response for ivr route.");
							   Assert.assertNotNull(ivr_option.get("value"), "value field is null in response for ivr route.");
   						   } 
					   }
   					   else if(routable_type.equals("GeoRoute")&&operator.equals("=")){
						   JSONObject geo_route = (JSONObject) callflow.get("geo_route");
						   Assert.assertNotNull(geo_route, "geo_route field is null in response.");
						   
						   Assert.assertTrue(geo_route.containsKey("play_branding"), "play_branding field is not present in response");
						   Assert.assertTrue(geo_route.containsKey("allow_manual_entry"), "allow_manual_entry field is not present in response");
						   Assert.assertTrue(geo_route.containsKey("radius"), "radius field is not present in response");
						   Assert.assertTrue(geo_route.containsKey("strategy"), "strategy field is not present in response");
						   Assert.assertTrue(geo_route.containsKey("geo_options"), "geo_options field is not present in response");
						   
						   Assert.assertNotNull(geo_route.get("play_branding"), "play_branding is null in response");
						   Assert.assertNotNull(geo_route.get("allow_manual_entry"), "allow_manual_entry is null in response");
						   Assert.assertNotNull(geo_route.get("strategy"), "strategy is null in response");
						   
						   JSONArray geo_options = (JSONArray)geo_route.get("geo_options");
						   if(!geo_options.isEmpty()){
							   for(int j=0;j<geo_options.size();j++){
								   JSONObject location = (JSONObject) geo_options.get(j);
								   Assert.assertTrue(location.containsKey("geo_option_id"), "geo_option_id is not present in geo location.");
								   Assert.assertTrue(location.containsKey("address"), "address is not present in geo location.");
								   Assert.assertTrue(location.containsKey("geo_updated_at"), "geo_updated_at is not present in geo location.");
								   Assert.assertTrue(location.containsKey("geo_created_at"), "geo_created_at is not present in geo location.");
								   Assert.assertTrue(location.containsKey("group_id"), "group_id is not present in geo location.");
								   Assert.assertTrue(location.containsKey("city"), "city is not present in geo location.");
								   Assert.assertTrue(location.containsKey("geo_target_did"), "geo_target_did is not present in geo location.");
								   Assert.assertTrue(location.containsKey("latitude"), "latitude is not present in geo location.");
								   Assert.assertTrue(location.containsKey("longitude"), "longitude is not present in geo location.");
							   }
						   }
					   }
   				   	}
   				   	test.log(LogStatus.PASS, "Check percentage callflow contains percentage details");
   				   	test.log(LogStatus.PASS, "Check ivr callflow contains ivr details");
	   				test.log(LogStatus.PASS, "Check geo callflow contains geo details");
	   				test.log(LogStatus.PASS, "Check API returns result based on filter applied for routable_type field.");
				}
   			}
   		}
   	}	
   	
  // @Test(priority=96)
   	public void callflow_callflow_with_invalid_filter_operator_for_routable_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for routable_type
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_routable_type", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for routable_type");
		test.assignCategory("CFA GET callflow/callflow API");
   		String routable_type = "SimpleRoute";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "routable_type"+encoded_operator+routable_type));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for routable_type");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   if(operator.equals(">=")||operator.equals("<="))
   				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : routable_type"+operator+routable_type);
   			   else
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : routable_type"+operator+routable_type);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for routable_type");
   			}	   
   		}
   	}	
   	
  // @Test(priority=97)
   	public void callflow_callflow_with_filter_for_invalid_routable_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid routable_type
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_routable_type", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid routable_type");
		test.assignCategory("CFA GET callflow/callflow API");
   		String routable_type = "invalid_routable_type";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "routable_type%3d"+routable_type));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid routable_type is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid routable_type is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid routable_type is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid routable_type is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid routable_type is passed in filter.");
   		}
   	}
   	
  // @Test(priority=98)
   	public void callflow_callflow_with_filter_for_blank_routable_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank routable_type
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_routable_type", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank routable_type");
		test.assignCategory("CFA GET callflow/callflow API");
   		String routable_type = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "routable_type%3d"+routable_type));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank routable_type is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank routable_type is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank routable_type is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank routable_type is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank routable_type is passed in filter.");
   		}
   	}
   	
  // @Test(priority=99)	
   	public void callflow_callflow_with_valid_filter_for_dnis() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for dnis
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_dnis", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for dnis.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		String dnis = "3852180113";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "dnis"+encoded_operator+dnis));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+dnis+") dnis is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at 1 record valid dnis is passed for filter
   				   if(operator.equals("=")){
   					   Assert.assertEquals(callflow.get("dnis").toString(), String.valueOf(dnis), "callflow/callflow api does not return searched callflow when valid("+dnis+") dnis is passed in filter with "+operator+" operator.");   
   				   	   Assert.assertTrue(array.size()==1, "callflow/callflow is returning more than 1 record when valid("+dnis+") dnis is passed in filter with "+operator+" operator.");	
   				   }else{
   					   Assert.assertNotEquals(callflow.get("dnis").toString(), String.valueOf(dnis), "callflow/callflow api does not return searched callflow when valid("+dnis+") dnis is passed in filter with "+operator+" operator.");
   				   	   Assert.assertTrue(array.size()>=1, "callflow/callflow is returning at least 1 record when valid("+dnis+") dnis is passed in filter with "+operator+" operator.");
   				   }		
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for dnis field.");
   			}
   		}
   	}	
   	
  // @Test(priority=100)
   	public void callflow_callflow_with_invalid_filter_operator_for_dnis() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for dnis
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_dnis", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for dnis");
		test.assignCategory("CFA GET callflow/callflow API");
   		String dnis = "3852180113";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "dnis"+encoded_operator+dnis));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for dnis");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse when "+ operator +" filter operator is used for dnis.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter when "+ operator +" filter operator is used for dnis.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : dnis"+operator+dnis);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for dnis");
   			}	   
   		}
   	}	
   	
  // @Test(priority=101)
   	public void callflow_callflow_with_filter_for_invalid_dnis() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid dnis
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_dnis", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid dnis");
		test.assignCategory("CFA GET callflow/callflow API");
   		String dnis = "invalid_dnis";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "dnis%3d"+dnis));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid dnis is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid dnis is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid dnis is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid dnis is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid dnis is passed in filter.");
   		}
   	}
   	
  // @Test(priority=102)
   	public void callflow_callflow_with_filter_for_blank_dnis() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank dnis
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_dnis", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank dnis");
		test.assignCategory("CFA GET callflow/callflow API");
   		String dnis = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "dnis%3d"+dnis));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank dnis is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank dnis is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank dnis is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank dnis is entered for filter
   		   for(int i=0;i<data_array.size();i++){
   			   JSONObject callflow = (JSONObject)data_array.get(i);
   			   // Check whether callflow/callflow returns at 1 record valid dnis is passed for filter
   			   Assert.assertEquals(callflow.get("dnis").toString(), String.valueOf(dnis), "callflow/callflow api does not return searched callflow when blank("+dnis+") dnis is passed in filter.");   
   		   	   Assert.assertTrue(data_array.size()>=1, "callflow/callflow is returning more than 1 record when blank("+dnis+") dnis is passed in filter.");		
   		   }
   		}
   	}	
   	
  // @Test(priority=103)	
   	public void callflow_callflow_with_valid_filter_for_message_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for message_enabled
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_message_enabled", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for message_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] message_enabled_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String message_enabled:message_enabled_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "message_enabled"+encoded_operator+message_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+message_enabled+") message_enabled is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid message_enabled is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+message_enabled+") message_enabled is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   					   		Assert.assertEquals(callflow.get("message_enabled").toString(), message_enabled, "callflow/callflow api does not return searched callflow when valid("+message_enabled+") message_enabled is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("message_enabled").toString(), message_enabled, "callflow/callflow api does not return searched callflow when valid("+message_enabled+") message_enabled is passed in filter with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for message_enabled field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=104)
   	public void callflow_callflow_with_invalid_filter_operator_for_message_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for message_enabled
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_message_enabled", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for message_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message_enabled = "true";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "message_enabled"+encoded_operator+message_enabled));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for message_enabled");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid message_enabled is entered for filter
   				   for(int i=0;i<data_array.size();i++){
   					   JSONObject call_flow = (JSONObject) data_array.get(i);
   					   Assert.assertEquals(call_flow.get("message_enabled").toString(), message_enabled, "API is not returning passed message_enabled when filter is applied with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API is returning passed message_enabled when filter is applied with "+operator+" operator.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : message_enabled"+operator+message_enabled);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for message_enabled");
   			}	   
   		}
   	}	
   	
  // @Test(priority=105)
   	public void callflow_callflow_with_filter_for_invalid_message_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid message_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_message_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid message_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message_enabled = "invalid_message_enabled";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "message_enabled%3d"+message_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid message_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid message_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid message_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid message_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid message_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=106)
   	public void callflow_callflow_with_filter_for_blank_message_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank message_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_message_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank message_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message_enabled = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "message_enabled%3d"+message_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank message_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank message_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank message_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank message_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank message_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=107)	
   	public void callflow_callflow_with_filter_for_postcall_ivr_id_with_null() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for postcall_ivr_id with null value
   		test = extent.startTest("callflow_callflow_with_filter_for_postcall_ivr_id_with_null", "To validate whether user is able to get callflow through callflow/callflow api with filter for postcall_ivr_id with null value.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "postcall_ivr_id"+encoded_operator+null));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with null postcall_ivr_id is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   				System.out.println(line);
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result = json.get("result").toString();
   			   Assert.assertEquals(result, "success", "API returns error when null is passed in postcall_ivr_id value.");
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record null postcall_ivr_id is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when null postcall_ivr_id is passed in filter with "+operator+" operator.");
   				   Assert.assertNull(callflow.get("postcall_ivr_id"), "null is not returned in postcall_ivr_id when null is passed in filter for postcall_ivr_id field.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for postcall_ivr_id field.");
   			}
   		}
   	}
   	
  // @Test(priority=108)	
   	public void callflow_callflow_with_valid_filter_for_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for message
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_message", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		String message = "test";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "message"+encoded_operator+message));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+message+") message is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid message is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+message+") message is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   				   		Assert.assertEquals(callflow.get("message").toString(), message, "callflow/callflow api does not return searched callflow when valid("+message+") message is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("message").toString(), message, "callflow/callflow api does not return searched callflow when valid("+message+") message is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for message field.");
   			}
   		}	
   	}	
   	
  // @Test(priority=109)
   	public void callflow_callflow_with_invalid_filter_operator_for_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for message
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_message", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message = "test";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "message"+encoded_operator+message));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for message");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   if(operator.equals(">=")||operator.equals("<="))
   				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : message"+operator+message);
   			   else
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : message"+operator+message);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for message");
   			}	   
   		}
   	}	
   	
  // @Test(priority=110)
   	public void callflow_callflow_with_filter_for_invalid_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid message
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_message", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message = "invalid_message";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "message%3d"+message));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid message is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid message is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid message is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid message is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid message is passed in filter.");
   		}
   	}
   	
  // @Test(priority=111)
   	public void callflow_callflow_with_filter_for_blank_message() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid message
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_message", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank message");
		test.assignCategory("CFA GET callflow/callflow API");
   		String message = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "message%3d"+message));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank message is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank message is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank message is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank message is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank message is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=112)	
   	public void callflow_callflow_with_valid_filter_for_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for call_value
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_call_value", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for call_value.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		int call_value = 10;
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "call_value"+encoded_operator+call_value));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+call_value+") call_value is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid call_value is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+call_value+") call_value is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   					   Assert.assertEquals(callflow.get("call_value").toString(), String.valueOf(call_value), "callflow/callflow api does not return searched callflow when valid("+call_value+") call_value is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("call_value").toString(), String.valueOf(call_value), "callflow/callflow api does not return searched callflow when valid("+call_value+") call_value is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for call_value field.");
   			}
   		}
   	}	
   	
  // @Test(priority=113)	
   	public void callflow_callflow_with_greater_or_less_filter_for_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with greater or less than filter for call_value
   		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_call_value", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for call_value");
		test.assignCategory("CFA GET callflow/callflow API");
   		int call_value = 10;
   		String[] operators = {">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "call_value"+encoded_operator+call_value));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for call_value");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   
   			   Boolean element_exist = false;
   			   for(int n = 0; n < array.size(); n++)
   			   {
   			       JSONObject object = (JSONObject)array.get(n);
   			       if(object.get("call_value").toString().equals(String.valueOf(call_value))){
   			    	   element_exist = true;
   			       }
   			       int route_id = Integer.parseInt(object.get("call_value").toString());
   	   			   if(operator.equals(">="))
   	   				   Assert.assertTrue(route_id>=call_value,"callflow/callflow api return result which have call_value greater than equal to entered call_value. Found call_value: "+call_value); 
   	   			   else if(operator.equals("<="))
   	   				   Assert.assertTrue(route_id<=call_value,"callflow/callflow api return result which have call_value less than equal to entered call_value. Found call_value: "+call_value); 
   			   }
   			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for call_value");
   			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for call_value field to filter the result.");
   			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for call_value field to filter the result");
   			}
   		}	
   	}
   	
  // @Test(priority=114)
   	public void callflow_callflow_with_invalid_filter_operator_for_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for call_value
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_call_value", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for call_value");
		test.assignCategory("CFA GET callflow/callflow API");
   		int call_value = 10;
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "call_value"+encoded_operator+call_value));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for call_value");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_value"+operator+call_value);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_value");
   			}	   
   		}
   	}	
   	
  // @Test(priority=115) // Query Pool Error
   	public void callflow_callflow_with_filter_for_invalid_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid call_value
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_call_value", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid call_value");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] call_values = {"abc","!@#$","abc12","12ab"};
   		for(String call_value : call_values){
   			ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+call_value+") call_value is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when invalid call_value is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+call_value+") call_value is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when invalid call_value is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+call_value+") call_value is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=116)
   	public void callflow_callflow_with_filter_for_negative_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for negative call_value
   		test = extent.startTest("callflow_callflow_with_filter_for_negative_call_value", "To validate whether user is able to get callflow through callflow/callflow api with filter for negative call_value");
		test.assignCategory("CFA GET callflow/callflow API");
   		int call_value = -10;
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with negative("+call_value+") call_value is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when negative call_value is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when negative("+call_value+") call_value is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when negative call_value is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when negative("+call_value+") call_value is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=117) // Query Pool Error
   	public void callflow_callflow_with_filter_for_blank_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank call_value
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_call_value", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank call_value");
		test.assignCategory("CFA GET callflow/callflow API");
   		String call_value = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank call_value is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank call_value is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank call_value is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank call_value is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank call_value is passed in filter.");
   		}
   	}
   	
  // @Test(priority=118)	
   	public void callflow_callflow_with_valid_filter_for_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with valid filter for record_until
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_record_until", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for record_until.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		Date date = new Date();
   		date = date_formatter.parse("2016-07-12 19:18:48");
   		String record_until = date_formatter.format(date);
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until"+encoded_operator+record_until));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+record_until+") record_until is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid record_until is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+record_until+") record_until is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   					   Assert.assertEquals(callflow.get("record_until").toString(), record_until, "callflow/callflow api does not return searched callflow when valid("+record_until+") record_until is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("record_until").toString(), record_until, "callflow/callflow api does not return searched callflow when valid("+record_until+") record_until is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for record_until field.");
   			}
   		}
   	}	
   	
  // @Test(priority=119)	//Query Pool Error
   	public void callflow_callflow_with_greater_or_less_filter_for_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with greater or less than filter for record_until
   		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_record_until", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2016-07-12 19:18:48");
   		String record_until = date_formatter.format(date);
   		String[] operators = {">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until"+encoded_operator+record_until));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/list", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for record_until.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");			   
   			   Assert.assertEquals(json.get("result").toString(), "success","API retuns error when "+(operator.equals(">=")?">=":"<=")+" operator is used for record_until field to filter the result. Defect reported: CT-17190");
   			   Boolean element_exist = false;
   			   for(int n = 0; n < array.size(); n++)
   			   {
   			       JSONObject object = (JSONObject)array.get(n);
   			       Date record_date = (Date)object.get("record_until");
   			       if(record_date.equals(date)){
   			    	   element_exist = true;
   			       }
   	   			   if(operator.equals(">="))
   	   				   Assert.assertTrue(record_date.after(date) || record_date.equals(date),"callflow/callflow api return result which have record_until less than equal to entered record_until. Found record_until: "+record_until); 
   	   			   else if(operator.equals("<="))
   	   				   Assert.assertTrue(record_date.before(date) || record_date.equals(date),"callflow/callflow api return result which have record_until greater than equal to entered record_until. Found record_until: "+record_until); 
   			   }
   			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for record_until");
   			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for record_until field to filter the result.");
   			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for record_until field to filter the result");
   			}
   		}	
   	}
   	
  // @Test(priority=120)
   	public void callflow_callflow_with_invalid_filter_operator_for_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with invalid filter operator for record_until
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_record_until", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2016-07-12 19:18:48");
   		String record_until = date_formatter.format(date);
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until"+encoded_operator+record_until));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for record_until");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : record_until"+operator+record_until);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for record_until");
   			}	   
   		}
   	}	
   	
  // @Test(priority=121) // Query Pool Error
   	public void callflow_callflow_with_filter_for_invalid_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] record_untils = {"abc","!@#$","abc12","12ab"};
   		for(String record_until : record_untils){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until%3d"+record_until));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+record_until+") record_until is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when invalid record_until is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+record_until+") record_until is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when invalid record_until is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+record_until+") record_until is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=122) // Query Pool Error
   	public void callflow_callflow_with_filter_for_blank_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String record_until = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "record_until%3d"+record_until));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank record_until is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank record_until is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank record_until is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank record_until is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank record_until is passed in filter.");
   		}
   	}
   	
  // @Test(priority=123) //Query Pool error
   	public void callflow_callflow_with_filter_for_unsupported_formatted_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for unsupported formatted record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_unsupported_formatted_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for unsupported formatted record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] unsupported_dates = {"Dec 27 19:18:48 IST 2016", "12-07-2016 19:18:48", "2016-07-12 07:18:48PM", "19:18:48 2016-07-12"}; 
   		for(String unsupported_date: unsupported_dates){
   			ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until%3d"+unsupported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/list api method with unsupported formatted("+unsupported_date+") record_until is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when unsupported formatted record_until is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when unsupported formatted("+unsupported_date+") record_until is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when unsupported formatted record_until is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when unsupported formatted("+unsupported_date+") record_until is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=124)
   	public void callflow_callflow_with_filter_for_supported_formatted_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for supported formatted record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_supported_formatted_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for supported formatted record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] supported_dates = {"2016/07/12 19:18:48", "07-12-2016 19:18:48"}; 
   		for(String supported_date: supported_dates){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "record_until%3d"+supported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with supported formatted record_until is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   				System.out.println(line);
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when supported formatted record_until is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when supported("+supported_date+") formatted record_until is passed in filter.");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when supported formatted record_until is entered for filter
   			   Assert.assertFalse(data_array.isEmpty(), "Data field is empty when supported("+supported_date+") formatted record_until is passed in filter.");
   			   SimpleDateFormat supported_date_formatter;
   			   for(int i=0;i<data_array.size();i++){
   				   JSONObject callflow = (JSONObject)data_array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record supported formatted record_until is passed for filter
   				   Assert.assertTrue(data_array.size()>=1, "callflow/callflow does not return at least 1 record when supported formatted("+supported_date+") record_until is passed in filter.");
   				   if(supported_date.equals("2016/07/12 19:18:48"))
   					   supported_date_formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
   				   else if(supported_date.equals("07-12-2016 19:18:48"))
   					   supported_date_formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
   				   else
   					   supported_date_formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
   				   Date date = supported_date_formatter.parse(supported_date);
   				   Assert.assertEquals(callflow.get("record_until").toString(), date_formatter.format(date), "callflow/callflow api does not return searched callflow when supported formatted("+supported_date+") record_until is passed in filter.");   
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for record_until field.");
   			}
   		}
   	}	
   	
  // @Test(priority=125) // Query Pool Error
   	public void callflow_callflow_with_filter_for_incorrect_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for incorrect record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_incorrect_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for incorrect record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String record_until = "2016-13-32 25:61:62";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "record_until%3d"+record_until));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with incorrect record_until is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when incorrect record_until is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when incorrect record_until is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when incorrect record_until is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when incorrect record_until is passed in filter.");
   		}
   	}
   	
  // @Test(priority=126) // Query Pool Error
   	public void callflow_callflow_with_filter_for_nonexisting_record_until() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for non existing record_until
   		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_record_until", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing record_until");
		test.assignCategory("CFA GET callflow/callflow API");
   		String record_until = "2018-12-12 22:22:22";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "record_until%3d"+record_until));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with non existing record_until is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when non existing record_until is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when non existing record_until is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when non existing record_until is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing record_until is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=127)	
   	public void callflow_callflow_with_valid_filter_for_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with valid filter for created_at
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_created_at", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for created_at.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String created_at = date_formatter.format(date);
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at"+encoded_operator+created_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+created_at+") created_at is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid created_at is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+created_at+") created_at is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   					   Assert.assertEquals(callflow.get("created_at").toString(), created_at, "callflow/callflow api does not return searched callflow when valid("+created_at+") created_at is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("created_at").toString(), created_at, "callflow/callflow api does not return searched callflow when valid("+created_at+") created_at is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for created_at field.");
   			}
   		}
   	}	
   	
  // @Test(priority=128)	//Query Pool Error
   	public void callflow_callflow_with_greater_or_less_filter_for_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with greater or less than filter for created_at
   		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_created_at", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String created_at = date_formatter.format(date);
   		String[] operators = {">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at"+encoded_operator+created_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for created_at");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");			   
   			   Assert.assertEquals(json.get("result").toString(), "success","API retuns error when "+(operator.equals(">=")?">=":"<=")+" operator is used for created_at field to filter the result. Defect reported: CT-17190");
   			   Boolean element_exist = false;
   			   for(int n = 0; n < array.size(); n++)
   			   {
   			       JSONObject object = (JSONObject)array.get(n);
   			       Date record_date = (Date)object.get("created_at");
   			       if(record_date.equals(date)){
   			    	   element_exist = true;
   			       }
   	   			   if(operator.equals(">="))
   	   				   Assert.assertTrue(record_date.after(date) || record_date.equals(date),"callflow/callflow api return result which have created_at less than equal to entered created_at. Found created_at: "+created_at); 
   	   			   else if(operator.equals("<="))
   	   				   Assert.assertTrue(record_date.before(date) || record_date.equals(date),"callflow/callflow api return result which have created_at greater than equal to entered created_at. Found created_at: "+created_at); 
   			   }
   			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for created_at");
   			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for created_at field to filter the result.");
   			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for created_at field to filter the result");
   			}
   		}	
   	}
   	
  // @Test(priority=129)
   	public void callflow_callflow_with_invalid_filter_operator_for_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with invalid filter operator for created_at
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_created_at", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String created_at = date_formatter.format(date);
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at"+encoded_operator+created_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for created_at");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : created_at"+operator+created_at);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for created_at");
   			}	   
   		}
   	}	
   	
  // @Test(priority=130) // Query Pool Error
   	public void callflow_callflow_with_filter_for_invalid_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] created_ats = {"abc","!@#$","abc12","12ab"};
   		for(String created_at : created_ats){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at%3d"+created_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+created_at+") created_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when invalid created_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+created_at+") created_at is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when invalid created_at is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+created_at+") created_at is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=131) // Query Pool Error
   	public void callflow_callflow_with_filter_for_blank_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String created_at = "";
   		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "created_at%3d"+created_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank created_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank created_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank created_at is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank created_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank created_at is passed in filter.");
   		}
   	}
   	
  // @Test(priority=132) //Query Pool error
   	public void callflow_callflow_with_filter_for_unsupported_formatted_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for unsupported formatted created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_unsupported_formatted_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for unsupported formatted created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] unsupported_dates = {"Apr 27 01:28:46 IST 2017", "20-04-2017 01:28:46", "2017-04-20 01:28:46AM", "01:28:46 2017-04-20"}; 
   		for(String unsupported_date: unsupported_dates){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at%3d"+unsupported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with unsupported formatted("+unsupported_date+") created_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when unsupported formatted created_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when unsupported formatted("+unsupported_date+") created_at is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when unsupported formatted created_at is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when unsupported formatted("+unsupported_date+") created_at is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=133)
   	public void callflow_callflow_with_filter_for_supported_formatted_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for supported formatted created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_supported_formatted_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for supported formatted created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] supported_dates = {"2017/04/20 01:28:46", "04-20-2017 01:28:46"}; 
   		for(String supported_date: supported_dates){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "created_at%3d"+supported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with supported formatted created_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   				System.out.println(line);
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when supported formatted created_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when supported("+supported_date+") formatted created_at is passed in filter.");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when supported formatted created_at is entered for filter
   			   Assert.assertFalse(data_array.isEmpty(), "Data field is empty when supported("+supported_date+") formatted created_at is passed in filter.");
   			   SimpleDateFormat supported_date_formatter;
   			   for(int i=0;i<data_array.size();i++){
   				   JSONObject callflow = (JSONObject)data_array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record supported formatted created_at is passed for filter
   				   Assert.assertTrue(data_array.size()>=1, "callflow/callflow does not return at least 1 record when supported formatted("+supported_date+") created_at is passed in filter.");
   				   if(supported_date.equals("2017/04/20 01:28:46"))
   					   supported_date_formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
   				   else if(supported_date.equals("04-20-2017 01:28:46"))
   					   supported_date_formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
   				   else
   					   supported_date_formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
   				   Date date = supported_date_formatter.parse(supported_date);
   				   Assert.assertEquals(callflow.get("created_at").toString(), date_formatter.format(date), "callflow/callflow api does not return searched callflow when supported formatted("+supported_date+") created_at is passed in filter.");   
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for created_at field.");
   			}
   		}
   	}	
   	
  // @Test(priority=134) // Query Pool Error
   	public void callflow_callflow_with_filter_for_incorrect_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for incorrect created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_incorrect_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for incorrect created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String created_at = "2016-13-32 25:61:62";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "created_at%3d"+created_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with incorrect created_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when incorrect created_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when incorrect created_at is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when incorrect created_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when incorrect created_at is passed in filter.");
   		}
   	}
   	
  // @Test(priority=135) // Query Pool Error
   	public void callflow_callflow_with_filter_for_nonexisting_created_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for non existing created_at
   		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_created_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing created_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String created_at = "2018-12-12 22:22:22";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "created_at%3d"+created_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/list api method with non existing created_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when non existing created_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when non existing created_at is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when non existing created_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing created_at is passed in filter.");
   		}
   	}
   	
  // @Test(priority=136)	
   	public void callflow_list_with_valid_filter_for_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/list api with valid filter for updated_at
   		test = extent.startTest("callflow_list_with_valid_filter_for_updated_at", "To validate whether user is able to get callflow through callflow/list api with valid filter for updated_at.");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String updated_at = date_formatter.format(date);
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at"+encoded_operator+updated_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+updated_at+") updated_at is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid updated_at is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+updated_at+") updated_at is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   					   Assert.assertEquals(callflow.get("updated_at").toString(), updated_at, "callflow/callflow api does not return searched callflow when valid("+updated_at+") updated_at is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("updated_at").toString(), updated_at, "callflow/callflow api does not return searched callflow when valid("+updated_at+") updated_at is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for updated_at field.");
   			}
   		}
   	}	
   	
  // @Test(priority=137)	//Query Pool Error
   	public void callflow_callflow_with_greater_or_less_filter_for_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with greater or less than filter for updated_at
   		test = extent.startTest("callflow_callflow_with_greater_or_less_filter_for_updated_at", "To validate whether user is able to get callflows through callflow/callflow api with greater and less than equal to filter for updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String updated_at = date_formatter.format(date);
   		String[] operators = {">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at"+encoded_operator+updated_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with greater or less than equal to filter for updated_at");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");			   
   			   Assert.assertEquals(json.get("result").toString(), "success","API retuns error when "+(operator.equals(">=")?">=":"<=")+" operator is used for updated_at field to filter the result. Defect reported: CT-17190");
   			   Boolean element_exist = false;
   			   for(int n = 0; n < array.size(); n++)
   			   {
   			       JSONObject object = (JSONObject)array.get(n);
   			       Date record_date = (Date)object.get("updated_at");
   			       if(record_date.equals(date)){
   			    	   element_exist = true;
   			       }
   	   			   if(operator.equals(">="))
   	   				   Assert.assertTrue(record_date.after(date) || record_date.equals(date),"callflow/callflow api return result which have updated_at less than equal to entered updated_at. Found updated_at: "+updated_at); 
   	   			   else if(operator.equals("<="))
   	   				   Assert.assertTrue(record_date.before(date) || record_date.equals(date),"callflow/callflow api return result which have updated_at greater than equal to entered updated_at. Found updated_at: "+updated_at); 
   			   }
   			   test.log(LogStatus.PASS, "Check filter is working for "+(operator.equals(">=")?">=":"<=")+" operator for updated_at");
   			   Assert.assertTrue(element_exist,"Boundry value is not included when "+(operator.equals(">=")?">=":"<=")+" operator is used for updated_at field to filter the result.");
   			   test.log(LogStatus.PASS, "Check Boundry value is included when "+(operator.equals(">=")?">=":"<=")+" operator is used for updated_at field to filter the result");
   			}
   		}	
   	}
   	
  // @Test(priority=138)
   	public void callflow_callflow_with_invalid_filter_operator_for_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with invalid filter operator for updated_at
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_updated_at", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		Date date = new Date();
   		date = date_formatter.parse("2017-04-20 01:28:46");
   		String updated_at = date_formatter.format(date);
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at"+encoded_operator+updated_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for updated_at");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : updated_at"+operator+updated_at);
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for updated_at");
   			}	   
   		}
   	}	
   	
  // @Test(priority=139) // Query Pool Error
   	public void callflow_callflow_with_filter_for_invalid_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] updated_ats = {"abc","!@#$","abc12","12ab"};
   		for(String updated_at : updated_ats){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at%3d"+updated_at));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+updated_at+") updated_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when invalid updated_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+updated_at+") updated_at is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when invalid updated_at is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+updated_at+") updated_at is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=140) // Query Pool Error
   	public void callflow_callflow_with_filter_for_blank_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String updated_at = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "updated_at%3d"+updated_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank updated_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank updated_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank updated_at is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank updated_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank updated_at is passed in filter.");
   		}
   	}
   	
  // @Test(priority=141) //Query Pool error
   	public void callflow_callflow_with_filter_for_unsupported_formatted_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for unsupported formatted updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_unsupported_formatted_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for unsupported formatted updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] unsupported_dates = {"Apr 27 01:28:46 IST 2017", "20-04-2017 01:28:46", "2017-04-20 01:28:46AM", "01:28:46 2017-04-20"}; 
   		for(String unsupported_date: unsupported_dates){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at%3d"+unsupported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with unsupported formatted("+unsupported_date+") updated_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when unsupported formatted updated_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when unsupported formatted("+unsupported_date+") updated_at is passed in filter. Defect Reported: CT-17119");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when unsupported formatted updated_at is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when unsupported formatted("+unsupported_date+") updated_at is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=142)
   	public void callflow_callflow_with_filter_for_supported_formatted_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException, java.text.ParseException{
   		// Execute callflow/callflow api with filter for supported formatted updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_supported_formatted_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for supported formatted updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] supported_dates = {"2017/04/20 01:28:46", "04-20-2017 01:28:46"}; 
   		for(String supported_date: supported_dates){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "updated_at%3d"+supported_date));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with supported formatted updated_at is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   				System.out.println(line);
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when supported formatted updated_at is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when supported("+supported_date+") formatted updated_at is passed in filter.");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when supported formatted updated_at is entered for filter
   			   Assert.assertFalse(data_array.isEmpty(), "Data field is empty when supported("+supported_date+") formatted updated_at is passed in filter.");
   			   SimpleDateFormat supported_date_formatter;
   			   for(int i=0;i<data_array.size();i++){
   				   JSONObject callflow = (JSONObject)data_array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record supported formatted updated_at is passed for filter
   				   Assert.assertTrue(data_array.size()>=1, "callflow/callflow does not return at least 1 record when supported formatted("+supported_date+") updated_at is passed in filter.");
   				   if(supported_date.equals("2017/04/20 01:28:46"))
   					   supported_date_formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
   				   else if(supported_date.equals("04-20-2017 01:28:46"))
   					   supported_date_formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
   				   else
   					   supported_date_formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
   				   Date date = supported_date_formatter.parse(supported_date);
   				   Assert.assertEquals(callflow.get("updated_at").toString(), date_formatter.format(date), "callflow/callflow api does not return searched callflow when supported formatted("+supported_date+") updated_at is passed in filter.");   
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for updated_at field.");
   			}
   		}
   	}	
   	
  // @Test(priority=143) // Query Pool Error
   	public void callflow_callflow_with_filter_for_incorrect_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for incorrect updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_incorrect_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for incorrect updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String updated_at = "2016-13-32 25:61:62";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "updated_at%3d"+updated_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with incorrect updated_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when incorrect updated_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when incorrect updated_at is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when incorrect updated_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when incorrect updated_at is passed in filter.");
   		}
   	}
   	
  // @Test(priority=144) // Query Pool Error
   	public void callflow_callflow_with_filter_for_nonexisting_updated_at() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for non existing updated_at
   		test = extent.startTest("callflow_callflow_with_filter_for_nonexisting_updated_at", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing updated_at");
		test.assignCategory("CFA GET callflow/callflow API");
   		String updated_at = "2018-12-12 22:22:22";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "updated_at%3d"+updated_at));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with non existing updated_at is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when non existing updated_at is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when non existing updated_at is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when non existing updated_at is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing updated_at is passed in filter.");
   		}
   	}	
   	
  // @Test(priority=145)	
   	public void callflow_callflow_with_valid_filter_for_email_to_notify() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for email_to_notify
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_email_to_notify", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for email_to_notify");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"=","!="};
   		String encoded_operator = "";
   		String email_to_notify = "saurabhalohak@gmail.com";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "email_to_notify"+encoded_operator+email_to_notify));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+email_to_notify+") email_to_notify is passed in filter with "+operator+" operator.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   JSONArray array = (JSONArray)json.get("data");
   			   // Get the callflow from the callflow/callflow
   			   for(int i=0;i<array.size();i++){
   				   JSONObject callflow = (JSONObject)array.get(i);
   				   // Check whether callflow/callflow returns at least 1 record valid email_to_notify is passed for filter
   				   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+email_to_notify+") email_to_notify is passed in filter with "+operator+" operator.");
   				   if(operator.equals("="))
   				   		Assert.assertEquals(callflow.get("email_to_notify").toString(), email_to_notify, "callflow/callflow api does not return searched callflow when valid("+email_to_notify+") email_to_notify is passed in filter with "+operator+" operator.");   
   				   else
   					   Assert.assertNotEquals(callflow.get("email_to_notify").toString(), email_to_notify, "callflow/callflow api does not return searched callflow when valid("+email_to_notify+") email_to_notify is passed in filter with "+operator+" operator.");
   			   }
   			   test.log(LogStatus.PASS, "Check API returns result based on filter applied for email_to_notify field.");
   			}
   		}	
   	}	
   	
  // @Test(priority=146)
   	public void callflow_callflow_with_invalid_filter_operator_for_email_to_notify() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for email_to_notify
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_email_to_notify", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for email_to_notify");
		test.assignCategory("CFA GET callflow/callflow API");
   		String email_to_notify = "saurabhalohak@gmail.com";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "email_to_notify"+encoded_operator+email_to_notify));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for email_to_notify");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   			   String err_data = json.get("err").toString();
   			   if(operator.equals(">=")||operator.equals("<="))
   				   Assert.assertEquals(err_data, "Invalid comparator type \"string\" used for value being used in rule 1 : email_to_notify"+operator+email_to_notify);
   			   else
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : email_to_notify"+operator+email_to_notify);
   			   test.log(LogStatus.PASS, "Check whether proper validation email_to_notify is displayed when "+ operator +" filter operator is used for email_to_notify");
   			}	   
   		}
   	}	
   	
  // @Test(priority=147)
   	public void callflow_callflow_with_filter_for_invalid_email_to_notify() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid email_to_notify
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_email_to_notify", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid email_to_notify");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] invalid_emails = {"abc","!@$#","123ac","abc.com","saurabhalohak#gmail.com","saurabhalohak@gmail.c"}; 
   		for(String email_to_notify : invalid_emails){
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "email_to_notify%3d"+email_to_notify));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid email_to_notify is passed in filter.");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString();
   			   // Check whether API is returning error when invalid email_to_notify is entered for filter
   			   Assert.assertEquals(result_data, "success", "API is returning error when invalid email_to_notify is passed in filter.");
   			   JSONArray data_array = (JSONArray)json.get("data");
   			   // Check data field is blank when invalid email_to_notify is entered for filter
   			   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid email_to_notify is passed in filter.");
   			}
   		}
   	}
   	
  // @Test(priority=148)
   	public void callflow_callflow_with_filter_for_blank_email_to_notify() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid email_to_notify
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_email_to_notify", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank email_to_notify");
		test.assignCategory("CFA GET callflow/callflow API");
   		String email_to_notify = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "email_to_notify%3d"+email_to_notify));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank email_to_notify is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank email_to_notify is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank email_to_notify is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank email_to_notify is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank email_to_notify is passed in filter.");
   		}
   	}
   	
  // @Test(priority=149)
   	public void callflow_callflow_with_filter_for_non_exist_email_to_notify() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for non existing email_to_notify
   		test = extent.startTest("callflow_callflow_with_filter_for_non_exist_email_to_notify", "To validate whether user is able to get callflow through callflow/callflow api with filter for non existing email_to_notify");
		test.assignCategory("CFA GET callflow/callflow API");
   		String email_to_notify = "aloha.rohit@gmail.com";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "email_to_notify%3d"+email_to_notify));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with non existing email_to_notify is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when non existing email_to_notify is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when non existing email_to_notify is passed in filter.");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is non existing when non existing email_to_notify is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when non existing email_to_notify is passed in filter.");
   		}
   	}
   	
  // @Test(priority=150)	
   	public void callflow_callflow_with_valid_filter_for_webhook_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for webhook_enabled
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_webhook_enabled", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for webhook_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] webhook_enabled_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String webhook_enabled:webhook_enabled_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "webhook_enabled"+encoded_operator+webhook_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+webhook_enabled+") webhook_enabled is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   // Check whether callflow/callflow returns at least 1 record valid webhook_enabled is passed for filter
   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+webhook_enabled+") webhook_enabled is passed in filter with "+operator+" operator.");
   					   if(operator.equals("="))
   					   		Assert.assertEquals(callflow.get("webhook_enabled").toString(), webhook_enabled, "callflow/callflow api does not return searched callflow when valid("+webhook_enabled+") webhook_enabled is passed in filter with "+operator+" operator.");   
   					   else
   						   Assert.assertNotEquals(callflow.get("webhook_enabled").toString(), webhook_enabled, "callflow/callflow api does not return searched callflow when valid("+webhook_enabled+") webhook_enabled is passed in filter with "+operator+" operator.");
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for webhook_enabled field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=151)
   	public void callflow_callflow_with_invalid_filter_operator_for_webhook_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for webhook_enabled
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_webhook_enabled", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for webhook_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String webhook_enabled = "true";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "webhook_enabled"+encoded_operator+webhook_enabled));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for webhook_enabled");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid webhook_enabled is entered for filter
   				   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid webhook_enabled is passed in filter.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : webhook_enabled"+operator+webhook_enabled);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for webhook_enabled");
   			}	   
   		}
   	}
   	
  // @Test(priority=152)
   	public void callflow_callflow_with_greater_less_filter_operator_for_webhook_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with greater or less than filter operator for webhook_enabled
   		test = extent.startTest("callflow_callflow_with_greater_less_filter_operator_for_webhook_enabled", "To validate whether user is able to get callflows through callflow/callflow api with greater or less than filter operator for webhook_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {">=","<="};
   		String[] webhook_enabled_values = {"true","false"};
   		for(String webhook_enabled:webhook_enabled_values){
   			String encoded_operator = "";
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "webhook_enabled"+encoded_operator+webhook_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for webhook_enabled");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   String result_data = json.get("result").toString();
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   for(int i=0;i<data_array.size();i++){
   					   JSONObject call_flow = (JSONObject) data_array.get(i);
   					   Assert.assertEquals(call_flow.get("webhook_enabled").toString(), webhook_enabled, "webhook_enabled in response is not same as passed webhook_enabled.");
   				   }
   				   test.log(LogStatus.PASS, "Check whether API returns valid records when "+ operator +" filter operator is used for webhook_enabled");
   				}	   
   			}
   		}
   	}	
   	
  // @Test(priority=153)
   	public void callflow_callflow_with_filter_for_invalid_webhook_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid webhook_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_webhook_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid webhook_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String webhook_enabled = "invalid_webhook_enabled";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "webhook_enabled%3d"+webhook_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid webhook_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid webhook_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid webhook_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid webhook_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid webhook_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=154)
   	public void callflow_callflow_with_filter_for_blank_webhook_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank webhook_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_webhook_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank webhook_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String webhook_enabled = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "webhook_enabled%3d"+webhook_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank webhook_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank webhook_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank webhook_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank webhook_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank webhook_enabled is passed in filter.");
   		}
   	}
   	
  // @Test(priority=155)
   	public void callflow_callflow_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with agency admin access_token
   		test = extent.startTest("callflow_callflow_with_agency_admin_access_token", "To validate whether user is able to get callflows through callflow/callflow api with agency admin token");
		test.assignCategory("CFA GET callflow/callflow API");
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("limit", "500"));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with agency admin access_token.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {	
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   JSONArray array = (JSONArray)json.get("data");
   		   Boolean company_callflow_exist = false, location_callflow_exist = false;
   		   // Get the callflow from the callflow/callflow
   		   for(int i=0;i<array.size();i++){
   			   JSONObject call_flow = (JSONObject) array.get(i);
   			   Assert.assertNotNull(call_flow.get("group_id"), "group_id field is null for some callflow like: "+call_flow);
   			   String group_id = call_flow.get("group_id").toString();
   			   if(group_id.equals("114"))
   				   company_callflow_exist = true;
   			   else if(group_id.equals("116")){
   				   location_callflow_exist = true;
   				   Assert.assertEquals(group_id, "116", "API is not returning agency level callflows when agency admin access_token is used.");
   			   }
   		   }
   		   Assert.assertTrue(company_callflow_exist, "company level callflows are not returned in response when agency admin access_token is used.");
   		   Assert.assertTrue(location_callflow_exist, "location level callflows are not returned in response when agency admin access_token is used.");
   		   test.log(LogStatus.PASS, "Check API is returning only his own groups callflows when agency admin access_token is used.");
   		   test.log(LogStatus.PASS, "Check company level callflows are returned in response when agency admin access_token is used.");
   		   test.log(LogStatus.PASS, "Check location level callflows are returned in response when agency admin access_token is used.");
   		}
   	}	
   	
  // @Test(priority=156)
   	public void callflow_callflow_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with company admin access_token
   		test = extent.startTest("callflow_callflow_with_company_admin_access_token", "To validate whether user is able to get callflows through callflow/callflow api with company admin token");
		test.assignCategory("CFA GET callflow/callflow API");
   		String access_token = HelperClass.get_oauth_token("company@admin.com", "lmc2demo");
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with company admin access_token.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {	
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   JSONArray array = (JSONArray)json.get("data");
   		   // Get the callflow from the callflow/callflow
   		   for(int i=0;i<array.size();i++){
   			   JSONObject call_flow = (JSONObject) array.get(i);
   			   Assert.assertNotNull(call_flow.get("group_id"), "group_id field is null for some callflow like: "+call_flow+ " <b> Failing Because of garbage data in db. </b>");
   			   String group_id = call_flow.get("group_id").toString();
   			   Assert.assertNotEquals(group_id, "8", "API is returning agency level callflows when agency admin access_token is used.");
   		   }
   		   test.log(LogStatus.PASS, "Check API is not returning agency level callflows when agency admin access_token is used");		   
   		}
   	}
   	
  // @Test(priority=157)
   	public void callflow_callflow_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with location admin access_token
   		test = extent.startTest("callflow_callflow_with_location_admin_access_token", "To validate whether user is able to get callflows through callflow/callflow api with location admin token");
		test.assignCategory("CFA GET callflow/callflow API");
   		String access_token = HelperClass.get_oauth_token("location@admin.com", "lmc2demo");
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/list", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with location admin access_token.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {	
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   JSONArray array = (JSONArray)json.get("data");
   		   // Get the callflow from the callflow/callflow
   		   for(int i=0;i<array.size();i++){
   			   JSONObject call_flow = (JSONObject) array.get(i);
   			   Assert.assertNotNull(call_flow.get("group_id"), "group_id field is null for some callflow like: "+call_flow + " <b> Failing Because of garbage data in db. </b>");
   			   String group_id = call_flow.get("group_id").toString();
   			   Assert.assertEquals(group_id, "116", "API is not returning location level callflows when agency admin access_token is used. <b> Failing Because of garbage data in db. </b>");
   		   }
   		   test.log(LogStatus.PASS, "Check API is returning only his own groups callflows when agency admin access_token is used");		   
   		}
   	}	
   	 
  // @Test(priority=158)	
   	public void callflow_callflow_with_valid_filter_for_repeat_greeting() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for repeat_greeting
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_repeat_greeting", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] repeat_greeting_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String repeat_greeting:repeat_greeting_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "repeat_greeting"+encoded_operator+repeat_greeting));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+repeat_greeting+") repeat_greeting is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   if((operator.equals("=")&&repeat_greeting.equals("true"))||operator.equals("!=")&&repeat_greeting.equals("false")){
	   					   // Check whether callflow/callflow returns at least 1 record valid repeat_greeting is passed for filter
	   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+repeat_greeting+") repeat_greeting is passed in filter with "+operator+" operator.");
	   					   JSONObject ivr_route = (JSONObject)callflow.get("ivr_route");
	   					   if(operator.equals("="))
	   					   		Assert.assertEquals(ivr_route.get("repeat_greeting").toString(), repeat_greeting, "callflow/callflow api does not return searched callflow when valid("+repeat_greeting+") repeat_greeting is passed in filter with "+operator+" operator.");   
	   					   else
	   						   Assert.assertNotEquals(ivr_route.get("repeat_greeting").toString(), repeat_greeting, "callflow/callflow api does not return searched callflow when valid("+repeat_greeting+") repeat_greeting is passed in filter with "+operator+" operator.");
   					   }   
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for repeat_greeting field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=159)
   	public void callflow_callflow_with_invalid_filter_operator_for_repeat_greeting() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for repeat_greeting
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_repeat_greeting", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
   		String repeat_greeting = "true";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "repeat_greeting"+encoded_operator+repeat_greeting));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for repeat_greeting");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid repeat_greeting is entered for filter
   				   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid repeat_greeting is passed in filter.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : repeat_greeting"+operator+repeat_greeting);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for repeat_greeting");
   			}	   
   		}
   	}
   	
  // @Test(priority=160)
   	public void callflow_callflow_with_greater_less_filter_operator_for_repeat_greeting() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with greater or less than filter operator for repeat_greeting
   		test = extent.startTest("callflow_callflow_with_greater_less_filter_operator_for_repeat_greeting", "To validate whether user is able to get callflows through callflow/callflow api with greater or less than filter operator for repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {">=","<="};
   		String repeat_greeting = "true";
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "repeat_greeting"+encoded_operator+repeat_greeting));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for repeat_greeting");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
			   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
			   JSONArray data_array = (JSONArray)json.get("data");
			   for(int i=0;i<data_array.size();i++){
				   JSONObject call_flow = (JSONObject) data_array.get(i);
				   JSONObject ivr_route = (JSONObject)(call_flow.get("ivr_route"));
				   Assert.assertEquals(ivr_route.get("repeat_greeting").toString(), repeat_greeting, "repeat_greeting in response is not same as passed repeat_greeting.");
			   }
			   test.log(LogStatus.PASS, "Check whether API returns valid records when "+ operator +" filter operator is used for repeat_greeting");
			}	   
   		}
   	}	
   	
  // @Test(priority=161)
   	public void callflow_callflow_with_filter_for_invalid_repeat_greeting() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid repeat_greeting
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_repeat_greeting", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
   		String repeat_greeting = "invalid_repeat_greeting";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "repeat_greeting%3d"+repeat_greeting));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid repeat_greeting is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid repeat_greeting is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid repeat_greeting is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid repeat_greeting is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid repeat_greeting is passed in filter.");
   		}
   	}
   	
  // @Test(priority=162)
   	public void callflow_callflow_with_filter_for_blank_repeat_greeting() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank repeat_greeting
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_repeat_greeting", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
   		String repeat_greeting = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "repeat_greeting%3d"+repeat_greeting));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank repeat_greeting is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank repeat_greeting is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank repeat_greeting is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank repeat_greeting is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank repeat_greeting is passed in filter.");
   		}
   	} 
   	
  // @Test(priority=163)	
   	public void callflow_callflow_with_valid_filter_for_ivr_route_options() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for ivr_route_options
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_ivr_route_options", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for repeat_greeting");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  map.put("value","1");  
	   	  map.put("ivr_target_did","3104013386");
	   	  map.put("ivr_play_disclaimer","before");  
	   	  map.put("ivr_record_enabled","true");  
	   	  map.put("ivr_vm_enabled","true");
	   	  map.put("ivr_webhook_enabled","false");  
	   	  map.put("ivr_message","tts://test");  
	   	  map.put("ivr_whisper_enabled","false");
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   // Get the callflow from the callflow/callflow
				   for(int i=0;i<array.size();i++){
					   JSONObject callflow = (JSONObject)array.get(i);  
					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
   					   JSONObject ivr_route = (JSONObject)callflow.get("ivr_route");
   					   JSONArray ivr_options = (JSONArray)ivr_route.get("ivr_options");
   					   Boolean is_searched_elem_present = false;
   					   for(int j=0; j<ivr_options.size(); j++){
	   						JSONObject ivr_option = (JSONObject)ivr_options.get(j);
	   						if(ivr_option.get(m.getKey()).toString().equals(m.getValue())){
	   							is_searched_elem_present = true;
	   						}
   					   }
   					   Assert.assertTrue(is_searched_elem_present, "Searched element is not present when filter applied for "+m.getKey()+" field.");
				   }
				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for "+m.getKey()+" field.");
				}
	   	  }
   	  }
    
  // @Test(priority=164)	
   	public void callflow_callflow_with_invalid_operator_for_ivr_route_options() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid operator in filter for ivr_route_options
   		test = extent.startTest("callflow_callflow_with_invalid_operator_for_ivr_route_options", "To validate whether user is able to get callflow through callflow/callflow api with invalid operator in filter for ivr_route_options");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};  
   		Map<String,String> map=new HashMap<String,String>();
		  map.put("value","1");  
		  map.put("ivr_target_did","3104013386");
		  map.put("ivr_play_disclaimer","before");  
		  map.put("ivr_record_enabled","true");  
		  map.put("ivr_vm_enabled","true");
		  map.put("ivr_webhook_enabled","false");  
		  map.put("ivr_message","tts://test");  
		  map.put("ivr_whisper_enabled","false");	   	  
		  String encoded_operator = "";
		  for(Map.Entry<String,String> m:map.entrySet()){  
			  	for(String operator:operators){
					encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
					CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
					Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
					test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					while ((line = rd.readLine()) != null) {
					   // Convert response to JSON object	
					   JSONParser parser = new JSONParser();
					   JSONObject json = (JSONObject) parser.parse(line);
					   String result_data = json.get("result").toString();
	   				   Assert.assertEquals(result_data, "error", "Invalid result value is returned in resonse when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter");
	   				   String err_data = json.get("err").toString();
	   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : "+m.getKey()+operator+m.getValue());
					   test.log(LogStatus.PASS, "Check API returns proper error when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					}
			  	}
	   	  }
   	  }      
   		
//	@Test(priority=165)	--uncomment when fixed

 	public void callflow_callflow_with_invalid_filter_for_ivr_route_options() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with invalid value in filter for ivr_route_options
 		test = extent.startTest("callflow_callflow_with_invalid_filter_for_ivr_route_options", "To validate whether user is able to get callflow through callflow/callflow api with invalid value in filter for ivr_route_options");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  String invalid_field_value = "invalid_value";
	   	  map.put("value",invalid_field_value);  
	   	  map.put("ivr_target_did",invalid_field_value);
	   	  map.put("ivr_play_disclaimer",invalid_field_value);  
	   	  map.put("ivr_record_enabled",invalid_field_value);  
	   	  map.put("ivr_vm_enabled",invalid_field_value);
	   	  map.put("ivr_webhook_enabled",invalid_field_value);  
	   	  map.put("ivr_message",invalid_field_value);  
	   	  map.put("ivr_whisper_enabled",invalid_field_value);
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when invalid ivr_route_options is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid ivr_route_options is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
 	  }
 	

//	@Test(priority=166)	-- uncomment when fixed

 	public void callflow_callflow_with_blank_filter_for_ivr_route_options() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with blank value in filter for ivr_route_options
 		test = extent.startTest("callflow_callflow_with_blank_filter_for_ivr_route_options", "To validate whether user is able to get callflow through callflow/callflow api with blank value in filter for ivr_route_options");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  map.put("value","");  
	   	  map.put("ivr_target_did","");
	   	  map.put("ivr_play_disclaimer","");  
	   	  map.put("ivr_record_enabled","");  
	   	  map.put("ivr_vm_enabled","");
	   	  map.put("ivr_webhook_enabled","");  
	   	  map.put("ivr_message","");  
	   	  map.put("ivr_whisper_enabled","");
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when blank ivr_route_options is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when blank("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid ivr_route_options is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
 	  } 	

	@Test(priority=167)	
 	public void callflow_callflow_with_valid_filter_for_geo_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with valid filter for geo_route
 		test = extent.startTest("callflow_callflow_with_valid_filter_for_geo_route", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for geo_route");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  map.put("play_branding","false");  
	   	  map.put("radius","999");
	   	  map.put("strategy","Zipcode");  
	   	  map.put("allow_manual_entry","true");  
	   	  map.put("geo_target_did","1234567890");  
	   	  map.put("latitude","41.713");  
	   	  map.put("longitude","-111.69");
	   	  map.put("address","address");
	   	  map.put("city","NY");  
	   	  map.put("geo_created_at","2016-05-09 19:09:11");  
	   	  map.put("geo_updated_at","2016-05-09 19:09:11");
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONArray array = (JSONArray)json.get("data");
				   // Get the callflow from the callflow/callflow
				   for(int i=0;i<array.size();i++){
					   JSONObject callflow = (JSONObject)array.get(i);  
					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
 					   JSONObject geo_route = (JSONObject)callflow.get("geo_route");
 					   if(m.getKey().equals("play_branding")||m.getKey().equals("radius")||m.getKey().equals("strategy")||m.getKey().equals("allow_manual_entry"))
 						   Assert.assertEquals(geo_route.get(m.getKey()).toString(), m.getValue(), "Searched element is not present when filter applied for "+m.getKey()+" field.");
 					   else{
 	 					   JSONArray geo_options = (JSONArray)geo_route.get("geo_options");
 	 					   Boolean is_searched_elem_present = false;
 	 					   for(int j=0; j<geo_options.size(); j++){
 		   						JSONObject geo_option = (JSONObject)geo_options.get(j);
 		   						if(geo_option.get(m.getKey()).toString().equals(m.getValue())){
 		   							is_searched_elem_present = true;
 		   						}
 	 					   }
 	 					   Assert.assertTrue(is_searched_elem_present, "Searched element is not present when filter applied for "+m.getKey()+" field.");
 					   }

				   }
				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for "+m.getKey()+" field.");
				}
	   	  }
 	  }
  
//	@Test(priority=168)	--uncomment when fixed

	public void callflow_callflow_with_invalid_filter_for_geo_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid value in filter for geo_route
		test = extent.startTest("callflow_callflow_with_invalid_filter_for_geo_route", "To validate whether user is able to get callflow through callflow/callflow api with invalid value in filter for geo_route");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  String invalid_field_value = "invalid_value";
	   	  map.put("play_branding",invalid_field_value);  
	   	  map.put("radius",invalid_field_value);
	   	  map.put("strategy",invalid_field_value);  
	   	  map.put("allow_manual_entry",invalid_field_value);  
	   	  map.put("geo_target_did",invalid_field_value);  
	   	  map.put("latitude",invalid_field_value);  
	   	  map.put("longitude",invalid_field_value);
	   	  map.put("address",invalid_field_value);
	   	  map.put("city",invalid_field_value);  
	   	  map.put("geo_created_at",invalid_field_value);  
	   	  map.put("geo_updated_at",invalid_field_value);
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when invalid geo_route is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid geo_route is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
	  }
	
	@Test(priority=169)	
 	public void callflow_callflow_with_invalid_operator_for_geo_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with invalid operator in filter for geo_route
 		test = extent.startTest("callflow_callflow_with_invalid_operator_for_geo_route", "To validate whether user is able to get callflow through callflow/callflow api with invalid operator in filter for geo_route");
		test.assignCategory("CFA GET callflow/callflow API");
 		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};  
 		Map<String,String> map=new HashMap<String,String>();
	   	  map.put("play_branding","false");  
	   	  map.put("radius","999");
	   	  map.put("strategy","Zipcode");  
	   	  map.put("allow_manual_entry","true");  
	   	  map.put("geo_target_did","1234567890");  
	   	  map.put("latitude","41.713");  
	   	  map.put("longitude","-111.69");
	   	  map.put("address","address");
	   	  map.put("city","NY");  
	   	  map.put("geo_created_at","2016-05-09 19:09:11");  
	   	  map.put("geo_updated_at","2016-05-09 19:09:11");   	  
		  String encoded_operator = "";
		  for(Map.Entry<String,String> m:map.entrySet()){  
			  	for(String operator:operators){
					encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
					CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
					Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
					test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					while ((line = rd.readLine()) != null) {
					   // Convert response to JSON object	
					   JSONParser parser = new JSONParser();
					   JSONObject json = (JSONObject) parser.parse(line);
					   String result_data = json.get("result").toString();
	   				   Assert.assertEquals(result_data, "error", "Invalid result value is returned in resonse when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter");
	   				   String err_data = json.get("err").toString();
	   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : "+m.getKey()+operator+m.getValue());
					   test.log(LogStatus.PASS, "Check API returns proper error when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					}
			  	}
	   	  }
 	  }
 	
//	@Test(priority=170)	--uncomment when fixed

 	public void callflow_callflow_with_blank_filter_for_geo_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with blank value in filter for geo_route
 		test = extent.startTest("callflow_callflow_with_blank_filter_for_geo_route", "To validate whether user is able to get callflow through callflow/callflow api with blank value in filter for geo_route");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  map.put("play_branding","");  
	   	  map.put("radius","");
	   	  map.put("strategy","");  
	   	  map.put("allow_manual_entry","");  
	   	  map.put("geo_target_did","");  
	   	  map.put("latitude","");  
	   	  map.put("longitude","");
	   	  map.put("address","");
	   	  map.put("city","");  
	   	  map.put("geo_created_at","");  
	   	  map.put("geo_updated_at","");
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when blank geo_route is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when blank("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid geo_route is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
 	  } 
 	
// 	@Test(priority=171)	-- uncomment when fixed
 	public void callflow_callflow_with_valid_filter_for_percentage_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with valid filter for percentage route parameters
		test = extent.startTest("callflow_callflow_with_valid_filter_for_percentage_route", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for percentage route parameters");
		test.assignCategory("CFA GET callflow/callflow API");
		Map<String,String> map=new HashMap<String,String>();
		map.put("timezone","null");  
		map.put("percentage","60");
		map.put("per_target_did","8018786943");  
		String encoded_operator = "";
		for(Map.Entry<String,String> m:map.entrySet()){  
			encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the callflow from the callflow/callflow
		   for(int i=0;i<array.size();i++){
			   JSONObject callflow = (JSONObject)array.get(i);  
			   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
			   JSONObject percentage_route = (JSONObject)callflow.get("percentage_route");
			   Assert.assertNotNull(percentage_route, "Unable to filter percentage route options. Defect Reported: CT-17211");
			   if(m.getKey().equals("timezone"))
				   Assert.assertNull(percentage_route.get(m.getKey()), "Searched element is not present when filter applied for "+m.getKey()+" field.");
			   else{
				   JSONArray percentage_options = (JSONArray)percentage_route.get("percentage_options");
				   Boolean is_searched_elem_present = false;
				   for(int j=0; j<percentage_options.size(); j++){
						JSONObject percentage_option = (JSONObject)percentage_options.get(j);
						if(percentage_option.get(m.getKey()).toString().equals(m.getValue())){
							is_searched_elem_present = true;
						}
				   }
				   Assert.assertTrue(is_searched_elem_present, "Searched element is not present when filter applied for "+m.getKey()+" field.");
			   }
	   		}		   
		   	test.log(LogStatus.PASS, "Check API returns result based on filter applied for "+m.getKey()+" field.");
		 }
	  }
   }	
 	
//	@Test(priority=172)	--uncomment when fixed

	public void callflow_callflow_with_invalid_filter_for_percentage_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute callflow/callflow api with invalid value in filter for percentage route parameters
		test = extent.startTest("callflow_callflow_with_invalid_filter_for_percentage_route", "To validate whether user is able to get callflow through callflow/callflow api with invalid value in filter for percentage route parameters");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  String invalid_field_value = "invalid_value";
	   	  map.put("timezone",invalid_field_value);  
	   	  map.put("percentage",invalid_field_value);
	   	  map.put("per_target_did",invalid_field_value);
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when invalid percentage route parameters is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid percentage route parameters is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
	  }
	
	@Test(priority=173)	
 	public void callflow_callflow_with_invalid_operator_for_percentage_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with invalid operator in filter for percentage route parameters
 		test = extent.startTest("callflow_callflow_with_invalid_operator_for_percentage_route", "To validate whether user is able to get callflow through callflow/callflow api with invalid operator in filter for percentage route parameters");
		test.assignCategory("CFA GET callflow/callflow API");
 		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};  
 		Map<String,String> map=new HashMap<String,String>();
	   	  map.put("timezone","null");  
	   	  map.put("percentage","60");
	   	  map.put("per_target_did","8018786943"); 	  
		  String encoded_operator = "";
		  for(Map.Entry<String,String> m:map.entrySet()){  
			  	for(String operator:operators){
					encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
					CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
					Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
					test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					while ((line = rd.readLine()) != null) {
					   // Convert response to JSON object	
					   JSONParser parser = new JSONParser();
					   JSONObject json = (JSONObject) parser.parse(line);
					   String result_data = json.get("result").toString();
	   				   Assert.assertEquals(result_data, "error", "Invalid result value is returned in resonse when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter");
	   				   String err_data = json.get("err").toString();
	   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : "+m.getKey()+operator+m.getValue());
					   test.log(LogStatus.PASS, "Check API returns proper error when invalid("+operator+") operator is passed for "+m.getKey()+" field in filter.");
					}
			  	}
	   	  }
 	  }
 	
//	@Test(priority=174)	--uncomment when fixed

 	public void callflow_callflow_with_blank_filter_for_percentage_route() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
 		// Execute callflow/callflow api with blank value in filter for percentage route parameters
 		test = extent.startTest("callflow_callflow_with_blank_filter_for_percentage_route", "To validate whether user is able to get callflow through callflow/callflow api with blank value in filter for percentage route parameters");
		test.assignCategory("CFA GET callflow/callflow API");
	   	  Map<String,String> map=new HashMap<String,String>();
	   	  map.put("timezone","");  
	   	  map.put("percentage","");
	   	  map.put("per_target_did","");
	   	  
	   	  String encoded_operator = "";
	   	  for(Map.Entry<String,String> m:map.entrySet()){  
				encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", m.getKey()+encoded_operator+m.getValue()));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
		   		   String result_data = json.get("result").toString();
		   		   // Check whether API is returning error when blank percentage route parameters is entered for filter
		   		   Assert.assertEquals(result_data, "success", "API is returning error when blank("+m.getValue()+") "+m.getKey()+" is passed in filter. Defect Reported: CT-17119");
		   		   JSONArray data_array = (JSONArray)json.get("data");
		   		   // Check data field is blank when invalid percentage route parameters is entered for filter
		   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank("+m.getValue()+") "+m.getKey()+" is passed in filter.");
				}
	   	  }
 	  }
	
  // @Test(priority=175)	
   	public void callflow_callflow_with_valid_filter_for_vm_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with valid filter for vm_enabled
   		test = extent.startTest("callflow_callflow_with_valid_filter_for_vm_enabled", "To validate whether user is able to get callflow through callflow/callflow api with valid filter for vm_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] vm_enabled_values = {"true","false"}, operators = {"=","!="};
   		String encoded_operator = "";
   		for(String vm_enabled:vm_enabled_values){
   			for(String operator:operators){
   				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   				List<NameValuePair> list = new ArrayList<NameValuePair>();
   				list.add(new BasicNameValuePair("filter", "vm_enabled"+encoded_operator+vm_enabled));
   				CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   				test.log(LogStatus.INFO, "Execute callflow/callflow api method with valid("+vm_enabled+") vm_enabled is passed in filter with "+operator+" operator.");
   				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   				String line = "";
   				while ((line = rd.readLine()) != null) {
   				   // Convert response to JSON object	
   				   JSONParser parser = new JSONParser();
   				   JSONObject json = (JSONObject) parser.parse(line);
   				   JSONArray array = (JSONArray)json.get("data");
   				   // Get the callflow from the callflow/callflow
   				   for(int i=0;i<array.size();i++){
   					   JSONObject callflow = (JSONObject)array.get(i);
   					   if((operator.equals("=")&&vm_enabled.equals("false"))||operator.equals("!=")&&vm_enabled.equals("true")){
	   					   // Check whether callflow/callflow returns at least 1 record valid vm_enabled is passed for filter
	   					   Assert.assertTrue(array.size()>=1, "callflow/callflow does not return at least 1 record when valid("+vm_enabled+") vm_enabled is passed in filter with "+operator+" operator.");
	   					   if(operator.equals("="))
	   					   		Assert.assertEquals(callflow.get("vm_enabled").toString(), vm_enabled, "callflow/callflow api does not return searched callflow when valid("+vm_enabled+") vm_enabled is passed in filter with "+operator+" operator.");   
	   					   else
	   						   Assert.assertNotEquals(callflow.get("vm_enabled").toString(), vm_enabled, "callflow/callflow api does not return searched callflow when valid("+vm_enabled+") vm_enabled is passed in filter with "+operator+" operator.");
   					   }   
   				   }
   				   test.log(LogStatus.PASS, "Check API returns result based on filter applied for vm_enabled field.");
   				}
   			}	
   		}
   	}	
   	
  // @Test(priority=176)
   	public void callflow_callflow_with_invalid_filter_operator_for_vm_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with invalid filter operator for vm_enabled
   		test = extent.startTest("callflow_callflow_with_invalid_filter_operator_for_vm_enabled", "To validate whether user is able to get callflows through callflow/callflow api with invalid filter operator for vm_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String vm_enabled = "false";
   		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
   		String encoded_operator = "";
   		for(String operator:operators){
   			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
   			List<NameValuePair> list = new ArrayList<NameValuePair>();
   			list.add(new BasicNameValuePair("filter", "vm_enabled"+encoded_operator+vm_enabled));
   			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for vm_enabled");
   			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   			String line = "";
   			while ((line = rd.readLine()) != null) {
   			   // Convert response to JSON object	
   			   JSONParser parser = new JSONParser();
   			   JSONObject json = (JSONObject) parser.parse(line);
   			   String result_data = json.get("result").toString(); 
   			   if(operator.equals(">=")||operator.equals("<=")){
   				   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
   				   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
   				   JSONArray data_array = (JSONArray)json.get("data");
   				   // Check data field is blank when invalid vm_enabled is entered for filter
   				   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid vm_enabled is passed in filter.");
   			   }
   			   else{
   				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
   				   String err_data = json.get("err").toString();
   				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : vm_enabled"+operator+vm_enabled);
   			   }
   			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for vm_enabled");
   			}	   
   		}
   	}
   	
  // @Test(priority=177)
   	public void callflow_callflow_with_greater_less_filter_operator_for_vm_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with greater or less than filter operator for vm_enabled
   		test = extent.startTest("callflow_callflow_with_greater_less_filter_operator_for_vm_enabled", "To validate whether user is able to get callflows through callflow/callflow api with greater or less than filter operator for vm_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String[] operators = {">=","<="};
   		String vm_enabled = "false";
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "vm_enabled"+encoded_operator+vm_enabled));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute callflow/callflow api method with "+ operator +" filter operator for vm_enabled");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "Invalid result value is in resonse.");
			   Assert.assertNull(json.get("err"),"err is not null when "+operator+" is passed in filter.");
			   JSONArray data_array = (JSONArray)json.get("data");
			   for(int i=0;i<data_array.size();i++){
				   JSONObject call_flow = (JSONObject) data_array.get(i);
				   Assert.assertEquals(call_flow.get("vm_enabled").toString(), vm_enabled, "vm_enabled in response is not same as passed vm_enabled.");
			   }
			   test.log(LogStatus.PASS, "Check whether API returns valid records when "+ operator +" filter operator is used for vm_enabled");
			}	   
   		}
   	}	
   	
  // @Test(priority=178)
   	public void callflow_callflow_with_filter_for_invalid_vm_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for invalid vm_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_invalid_vm_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for invalid vm_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String vm_enabled = "invalid_vm_enabled";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "vm_enabled%3d"+vm_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with invalid vm_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when invalid vm_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when invalid vm_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when invalid vm_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when invalid vm_enabled is passed in filter.");
   		}
   	}
    
  // @Test(priority=179)
   	public void callflow_callflow_with_filter_for_blank_vm_enabled() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
   		// Execute callflow/callflow api with filter for blank vm_enabled
   		test = extent.startTest("callflow_callflow_with_filter_for_blank_vm_enabled", "To validate whether user is able to get callflow through callflow/callflow api with filter for blank vm_enabled");
		test.assignCategory("CFA GET callflow/callflow API");
   		String vm_enabled = "";
   		List<NameValuePair> list = new ArrayList<NameValuePair>();
   		list.add(new BasicNameValuePair("filter", "vm_enabled%3d"+vm_enabled));
   		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/callflow", access_token, list);
   		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
   		test.log(LogStatus.INFO, "Execute callflow/callflow api method with blank vm_enabled is passed in filter.");
   		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
   		String line = "";
   		while ((line = rd.readLine()) != null) {
   			System.out.println(line);
   		   // Convert response to JSON object	
   		   JSONParser parser = new JSONParser();
   		   JSONObject json = (JSONObject) parser.parse(line);
   		   String result_data = json.get("result").toString();
   		   // Check whether API is returning error when blank vm_enabled is entered for filter
   		   Assert.assertEquals(result_data, "success", "API is returning error when blank vm_enabled is passed in filter. Defect Reported: CT-17119");
   		   JSONArray data_array = (JSONArray)json.get("data");
   		   // Check data field is blank when blank vm_enabled is entered for filter
   		   Assert.assertTrue(data_array.isEmpty(), "Data field is not empty when blank vm_enabled is passed in filter.");
   		}
   	}    
}