package customsource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpResponse;
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
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.core.utils.DBCustomSourceUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class PostCallflowCustomSource extends BaseClass{
	public static final String class_name = "PostCallflowCustomSource";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	
	@Test(priority=1)
	public void post_callflow_customsource_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_callflow_customsource_without_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API without access_token");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_without_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", test_data.get(2));
		json.put("custom_source_type_2", test_data.get(3));
		json.put("custom_source_type_3", test_data.get(4));
		json.put("custom_source_type_4", test_data.get(5));
		json.put("custom_source_type_5", test_data.get(6));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", "", array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=2)
	public void post_callflow_customsource_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_callflow_customsource_with_invalid_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with invalid access_token");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_invalid_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", test_data.get(2));
		json.put("custom_source_type_2", test_data.get(3));
		json.put("custom_source_type_3", test_data.get(4));
		json.put("custom_source_type_4", test_data.get(5));
		json.put("custom_source_type_5", test_data.get(6));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", invalid_access_token, array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified http status code when invalid access_token is passed");
	}
	
	@Test(priority=3)
	public void post_callflow_customsource_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_callflow_customsource_with_expired_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with expired access_token");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_expired_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", test_data.get(2));
		json.put("custom_source_type_2", test_data.get(3));
		json.put("custom_source_type_3", test_data.get(4));
		json.put("custom_source_type_4", test_data.get(5));
		json.put("custom_source_type_5", test_data.get(6));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", expired_access_token, array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void post_callflow_customsource_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_valid_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with valid access_token");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_valid_access_token");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid access_token is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid access_token is passed.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when valid access_token is passed.");
		   
		   // Check custom source is added on callflow
		   get_callflow_custom_source(call_flow_id, json);
		   
		   // Delete the added custom source from callflow
		   ArrayList<String> type = new ArrayList<String>();
		   type.add("custom_source_type_1");
		   type.add("custom_source_type_2");
		   type.add("custom_source_type_3");
		   type.add("custom_source_type_4");
		   type.add("custom_source_type_5");
		   
		   System.out.println(test_data);
		   delete_custom_source_from_callflow(call_flow_id, type);
		}  
	}
	
	@Test(priority=5)
	public void post_custom_source_with_blank_array() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_blank_array", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with blank array.");
		test.assignCategory("CFA POST /callflow/customsources API");
		JSONArray array = new JSONArray();
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank array is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank array is passed in POST callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank array is passed in POST callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank array is passed in POST callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when blank array is passed in POST callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when blank array is passed in POST callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ARRAY_LENGTH_SHORT", "Invalid code value is returned in response when blank array with POST callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Array is too short (0), minimum 1", "Invalid message value is returned in response when blank array with POST callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when blank array with POST callflow/customsource api method.");
		}  
	}
	
	@Test(priority=6)
	public void post_callflow_customsource_without_any_params() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_without_any_params", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API without any params.");
		test.assignCategory("CFA POST /callflow/customsources API");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when no parameter is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed in POST callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed in POST callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed in POST callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed in POST callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when no parameter is passed in POST callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter with POST callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: call_flow_id", "Invalid message value is returned in response when no parameter with POST callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when no parameter with POST callflow/customsource api method.");
		}  
	}
	
	@Test(priority=7)
	public void post_callflow_customsource_without_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_without_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API without call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_without_call_flow_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
		json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
		json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when callflow_id parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when callflow_id parameter is not passed in POST callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when callflow_id parameter is not passed in POST callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when callflow_id parameter is not passed in POST callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when callflow_id parameter is not passed in POST callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when callflow_id parameter is not passed in POST callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when callflow_id parameter is not passed with POST callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: call_flow_id", "Invalid message value is returned in response when callflow_id parameter is not passed with POST callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when callflow_id parameter is not passed with POST callflow/customsource api method.");
		}  
	}
	
	@Test(priority=8)
	public void post_callflow_customsource_with_blank_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_blank_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with blank call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_blank_call_flow_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", "");
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
		json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
		json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank call_flow_id is passed in POST callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank call_flow_id is passed in POST callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank call_flow_id is passed in POST callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when blank call_flow_id is passed in POST callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when blank call_flow_id is passed in POST callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank call_flow_id is passed with POST callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank call_flow_id is passed with POST callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank call_flow_id is passed with POST callflow/customsource api method.");
		}  
	}
	
	@Test(priority=9)
	public void post_callflow_customsource_with_invalid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_invalid_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with invalid call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_invalid_call_flow_id");
		for(String callflow_id:test_data.get(1).split(",")){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", "");
			json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
			json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
			json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
			json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
			json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+callflow_id+") call_flow_id is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+callflow_id+") call_flow_id is passed in POST callflow/customsource api.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+callflow_id+") call_flow_id is passed in POST callflow/customsource api.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+callflow_id+") call_flow_id is passed in POST callflow/customsource api.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+callflow_id+") call_flow_id is passed in POST callflow/customsource api.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when invalid("+callflow_id+") call_flow_id is passed in POST callflow/customsource api.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/customsources");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+callflow_id+") call_flow_id is passed with POST callflow/customsource api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+callflow_id+") call_flow_id is passed with POST callflow/customsource api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+callflow_id+") call_flow_id is passed with POST callflow/customsource api method.");
			}  
		}
	}
	
	@Test(priority=10)
	public void post_callflow_customsource_with_negative_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_negative_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with negative call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_negative_call_flow_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
		json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
		json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when negative call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when negative call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when negative call_flow_id is passed");
			String err_data = api_response.get("err").toString();
			Assert.assertEquals(err_data, "Please provide valid call flow id", "Proper validation message is not displayed when negative call_flow_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when negative call_flow_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when negative call_flow_id is passed");
		}  
	}
	
	@Test(priority=11)
	public void post_callflow_customsource_with_non_existing_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_non_existing_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with non existing call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_non_existing_call_flow_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
		json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
		json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when non existing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when non existing call_flow_id is passed");
			String err_data = api_response.get("err").toString();
			Assert.assertEquals(err_data, "Please provide valid call flow id", "Proper validation message is not displayed when non existing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when non existing call_flow_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when non existing call_flow_id is passed");
		}  
	}
	
	@Test(priority=12)
	public void post_callflow_customsource_with_deleted_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_deleted_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with deleted call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_deleted_call_flow_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
		json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
		json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when deleted call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when deleted call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when deleted call_flow_id is passed");
			String err_data = api_response.get("err").toString();
			Assert.assertEquals(err_data, "Please provide valid call flow id", "Proper validation message is not displayed when deleted call_flow_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when deleted call_flow_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when deleted call_flow_id is passed");
		}  
	}
	
	@Test(priority=13)
	public void post_callflow_customsource_with_other_billing_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_other_billing_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with other billing call_flow_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_other_billing_call_flow_id");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String call_flow_id = DBCallFlowsUtils.getOtherBillingCallFlowId(org_unit_id);
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when other billing call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when other billing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when other billing call_flow_id is passed");
			String err_data = api_response.get("err").toString();
			Assert.assertEquals(err_data, "You don't have permission to access call_flow_id "+call_flow_id, "Proper validation message is not displayed when other billing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when other billing call_flow_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when other billing call_flow_id is passed");
		}  
	}	
	
	@Test(priority=14)
	public void post_callflow_customsource_with_valid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_valid_call_flow_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with valid call_flow_id");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_valid_call_flow_id");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid call_flow_id is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid call_flow_id is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when valid call_flow_id is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid call_flow_id is passed.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when valid call_flow_id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when valid call_flow_id is passed.");
		   
		   // Check custom source is added on callflow
		   get_callflow_custom_source(call_flow_id, json);
		   
		   // Delete the added custom source from callflow
		   ArrayList<String> type = new ArrayList<String>();
		   type.add("custom_source_type_1");
		   type.add("custom_source_type_2");
		   type.add("custom_source_type_3");
		   type.add("custom_source_type_4");
		   type.add("custom_source_type_5");
		   
		   delete_custom_source_from_callflow(call_flow_id, type);
		}  
	}	
	
	@Test(priority=15)
	public void post_callflow_customsource_without_any_custom_source() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_without_any_custom_source", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API without any custom_source.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_without_any_custom_source");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when no custom_source is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when no custom_source is passed.");
			test.log(LogStatus.PASS, "Check API returns error when no custom_source is passed");
			String err_data = api_response.get("err").toString();
			Assert.assertEquals(err_data, "There must be at least one custom_source_type", "Proper validation message is not displayed when no custom_source is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when no custom_source is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when no custom_source is passed");
		}  
	}
	
	@Test(priority=16)
	public void post_callflow_customsource_with_blank_custom_source() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_blank_custom_source", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with blank custom_source.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_blank_custom_source");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_type_1", "");
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank custom_source is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank custom_source is passed in POST callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank custom_source is passed in POST callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank custom_source is passed in POST callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when blank custom_source is passed in POST callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when blank custom_source is passed in POST callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank custom_source is passed with POST callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank custom_source is passed with POST callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank custom_source is passed with POST callflow/customsource api method.");
		}  
	}
	
	@Test(priority=17)
	public void post_callflow_customsource_with_invalid_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_invalid_custom_source_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with invalid custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_invalid_custom_source_id");
		for(String custom_source:test_data.get(2).split(",")){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
			json.put("custom_source_type_1", custom_source);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+custom_source+") custom_source is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+custom_source+") custom_source is passed in POST callflow/customsource api.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+custom_source+") custom_source is passed in POST callflow/customsource api.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+custom_source+") custom_source is passed in POST callflow/customsource api.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (callflowcustomsources): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+custom_source+") custom_source is passed in POST callflow/customsource api.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "callflowcustomsources", "Invalid name value is returned in response when invalid("+custom_source+") custom_source is passed in POST callflow/customsource api.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/customsources");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+custom_source+") custom_source is passed with POST callflow/customsource api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+custom_source+") custom_source is passed with POST callflow/customsource api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+custom_source+") custom_source is passed with POST callflow/customsource api method.");
			}  
		}
	}	
	
	@Test(priority=18)
	public void post_callflow_customsource_on_all_custom_source_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_on_all_custom_source_type", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with valid call_flow_id");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_on_all_custom_source_type");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when custom source is passed on all type.");
		   test.log(LogStatus.PASS, "Check API returns success when custom source is passed on all type.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when custom source is passed on all type.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when custom source is passed on all type.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when custom source is passed on all type.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when custom source is passed on all type.");
		   
		   // Delete the added custom source from callflow
		   ArrayList<String> type = new ArrayList<String>();
		   type.add("custom_source_type_1");
		   type.add("custom_source_type_2");
		   type.add("custom_source_type_3");
		   type.add("custom_source_type_4");
		   type.add("custom_source_type_5");
		   
		   delete_custom_source_from_callflow(call_flow_id, type);
		}  
	}	
	
	@Test(priority=19)
	public void post_callflow_customsource_on_some_custom_source_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_on_some_custom_source_type", "To validate whether user is able to add customsources on some custom source type through POST callflow/customsources API");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_on_some_custom_source_type");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when custom source is passed on some custom source type");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when custom source is passed on some custom source type.");
		   test.log(LogStatus.PASS, "Check API returns success when custom source is passed on some custom source type.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when custom source is passed on some custom source type.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when custom source is passed on some custom source type.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when custom source is passed on some custom source type.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when custom source is passed on some custom source type.");
		   
		   // Check custom source is added on callflow
		   get_callflow_custom_source(call_flow_id, json);
		   
		   // Delete the added custom source from callflow
		   ArrayList<String> type = new ArrayList<String>();
		   type.add("custom_source_type_1");
		   type.add("custom_source_type_3");
		   type.add("custom_source_type_5");
		   
		   delete_custom_source_from_callflow(call_flow_id, type);
		}  
	}
	
	@Test(priority=20)
	public void post_callflow_customsource_to_callflow_which_already_have_cs() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_to_callflow_which_already_have_cs", "To validate whether user is able to add customsources to callflow which alredy have custom source.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_to_callflow_which_already_have_cs");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		
		// Again Hit with same data
		response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when user try to add customsources to callflow which alredy have custom source");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when user try to add customsources to callflow which alredy have custom source.");
		   test.log(LogStatus.PASS, "Check API returns success when user try to add customsources to callflow which alredy have custom source.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when user try to add customsources to callflow which alredy have custom source.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when user try to add customsources to callflow which alredy have custom source.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when user try to add customsources to callflow which alredy have custom source.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when user try to add customsources to callflow which alredy have custom source.");
		   
		   // Delete the added custom source from callflow
		   ArrayList<String> type = new ArrayList<String>();
		   type.add("custom_source_type_1");
		   type.add("custom_source_type_2");
		   type.add("custom_source_type_3");
		   type.add("custom_source_type_4");
		   type.add("custom_source_type_5");
		   
		   delete_custom_source_from_callflow(call_flow_id, type);
		}  
	}
	
	@Test(priority=21)
	public void post_callflow_customsource_with_negative_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_negative_custom_source_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with negative custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_negative_custom_source_id");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id="-"+confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when negative custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when no custom_source is passed.");
			test.log(LogStatus.PASS, "Check API returns error when no custom_source is passed");
			String err_data = api_response.get("err").toString();
			JSONArray expected_validation = new JSONArray();
			expected_validation.add("invalid customSourceId "+custom_source_id);
			Assert.assertEquals(err_data, expected_validation.toString(), "Proper validation message is not displayed when no custom_source is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when no custom_source is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when no custom_source is passed");
		}  
	}
	
	@Test(priority=22)
	public void post_callflow_customsource_with_other_billing_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_other_billing_custom_source_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with other billing custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_other_billing_custom_source_id");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		int custom_source_id = DBCustomSourceUtils.getCustomSourceIdOfOtherBilling("8");
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", custom_source_id);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when other billing custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when other billing custom_source_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when other billing custom_source_id is passed");
			String err_data = api_response.get("err").toString();
			JSONArray expected_validation = new JSONArray();
			expected_validation.add("You are not authorized to access "+custom_source_id+" custom source");
			Assert.assertEquals(err_data, expected_validation.toString(), "Proper validation message is not displayed when other billing custom_source_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when other billing custom_source_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when other billing custom_source_id is passed");
		}  
	}
	
	@Test(priority=23)
	public void post_callflow_customsource_with_deleted_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_deleted_custom_source_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with deleted custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_deleted_custom_source_id");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		Random r = new Random();
		String custom_source_name="sjacs-"+r.nextInt(50000);
		//adding new custom source
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", custom_source_name);
		array.add(json);
		HelperClass.make_post_request("/v2/customsource", access_token, array);
		
		int cs_id = DBCustomSourceUtils.getCustomSourceId(custom_source_name, org_unit_id);
		
		//deleting newly added custom source
		JSONArray array1 = new JSONArray();
		JSONObject json1 = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(cs_id);
		json1.put("custom_sources", custom_source);
		HelperClass.make_delete_request("/v2/customsource", access_token, json1);
		
		JSONArray array2 = new JSONArray();
		JSONObject json2 = new JSONObject();
		json2.put("call_flow_id", Integer.parseInt(call_flow_id));
		json2.put("custom_source_type_1", cs_id);
		array2.add(json2);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array2);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when deleted custom_source_id is passed");

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when deleted custom_source_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when deleted custom_source_id is passed");
			String err_data = api_response.get("err").toString();
			JSONArray expected_validation = new JSONArray();
			expected_validation.add("invalid customSourceId "+cs_id);
			Assert.assertEquals(err_data, expected_validation.toString(), "Proper validation message is not displayed when deleted custom_source_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when deleted custom_source_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when deleted custom_source_id is passed");
		}  
	}
	
	@Test(priority=24)
	public void post_callflow_customsource_non_existing_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_non_existing_custom_source_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with non existing custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_non_existing_custom_source_id");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when non existing custom_source_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when non existing custom_source_id is passed");
			String err_data = api_response.get("err").toString();
			JSONArray expected_validation = new JSONArray();
			expected_validation.add("invalid customSourceId "+test_data.get(2));
			Assert.assertEquals(err_data, expected_validation.toString(), "Proper validation message is not displayed when non existing custom_source_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when non existing custom_source_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when non existing custom_source_id is passed");
		}  
	}
	
	@Test(priority=25)
	public void post_callflow_customsource_with_valid_and_invalid_cs_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_valid_and_invalid_cs_id", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with valid and invalid combination custom_source_id.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_valid_and_invalid_cs_id");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		//valid cs
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		//invalid cs
		json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid and invalid custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			// Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when valid and invalid custom_source_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when valid and invalid custom_source_id is passed");
			String err_data = api_response.get("err").toString();
			JSONArray expected_validation = new JSONArray();
			expected_validation.add("invalid customSourceId "+test_data.get(3));
			Assert.assertEquals(err_data, expected_validation.toString(), "Proper validation message is not displayed when valid and invalid custom_source_id is passed.");
			test.log(LogStatus.PASS, "Proper validation message is not displayed when valid and invalid custom_source_id is passed");
			Assert.assertNull(api_response.get("data"), "Data field is not null in response when valid and invalid custom_source_id is passed");
		}  
	}
	
	@Test(priority=26)
	public void post_callflow_customsource_with_non_existing_custom_source_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_non_existing_custom_source_type", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with non existing custom_source_type");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_non_existing_custom_source_type");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_6", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing custom_source_type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when non existing custom_source_type is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when non existing custom_source_type is passed.");
		   Assert.assertEquals(api_response.get("err").toString(), "Please provide valid custom_source_type, Valid parameters are 'custom_source_type_1', 'custom_source_type_2', 'custom_source_type_3', 'custom_source_type_4' and 'custom_source_type_5'", "Proper validation is not displayed when non existing custom_source_type is passed.");
		   test.log(LogStatus.PASS, "Check API returns proper validation message when non existing custom_source_type is passed.");
		   Assert.assertNull(api_response.get("data"),  "Data field is not null when non existing custom_source_type is passed.");
		}  
	}
	
	@Test(priority=27)
	public void post_callflow_customsource_with_more_than_5_custom_source_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_more_than_5_custom_source_type", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with more than 5 custom_source_type");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_more_than_5_custom_source_type");
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
		json.put("custom_source_type_6", Integer.parseInt(custom_source_id));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when with more than 5 custom_source_type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when with more than 5 custom_source_type is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when with more than 5 custom_source_type is passed.");
		   Assert.assertEquals(api_response.get("err").toString(), "Please provide valid custom_source_type, Valid parameters are 'custom_source_type_1', 'custom_source_type_2', 'custom_source_type_3', 'custom_source_type_4' and 'custom_source_type_5'", "Proper validation is not displayed when with more than 5 custom_source_type is passed.");
		   test.log(LogStatus.PASS, "Check API returns proper validation message when with more than 5 custom_source_type is passed.");
		   Assert.assertNull(api_response.get("data"),  "Data field is not null when with more than 5 custom_source_type is passed.");
		}  
	}	
	
	@Test(priority=28)
	public void post_callflow_customsource_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_agency_admin_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with agency admin access_token.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_agency_admin_access_token");
		
		Map<String, Object> confCallFLowHierarchyAgency = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id_agency=confCallFLowHierarchyAgency.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCallFLowHierarchyCompany = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String call_flow_id_compay=confCallFLowHierarchyCompany.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCallFLowHierarchyLocation = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		String call_flow_id_location=confCallFLowHierarchyLocation.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
//		String[] callflows = test_data.get(1).split(",");
		String agency_callflow = call_flow_id_agency, company_callflow = call_flow_id_compay, location_callflow = call_flow_id_location;
		
		
		for(String callflow:new String[]{agency_callflow,company_callflow,location_callflow}){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(callflow));
			json.put("custom_source_type_1", Integer.parseInt(custom_source_id));
			json.put("custom_source_type_2", Integer.parseInt(custom_source_id));
			json.put("custom_source_type_3", Integer.parseInt(custom_source_id));
			json.put("custom_source_type_4", Integer.parseInt(custom_source_id));
			json.put("custom_source_type_5", Integer.parseInt(custom_source_id));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   test.log(LogStatus.PASS, "Check API returns success when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   Assert.assertNull(api_response.get("err"), "API returns validation message when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   String data_value = api_response.get("data").toString();
			   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper message is not displayed when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   test.log(LogStatus.PASS, "Check proper message is displayed when agency admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			   
			   // Delete the added custom source from callflow
			   ArrayList<String> type = new ArrayList<String>();
			   type.add("custom_source_type_1");
			   type.add("custom_source_type_2");
			   type.add("custom_source_type_3");
			   type.add("custom_source_type_4");
			   type.add("custom_source_type_5");
			   
			   delete_custom_source_from_callflow(callflow, type);
			}  
		}
	}
	
//	@Test(priority=29)
	public void post_callflow_customsource_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_company_admin_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with company admin access_token.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_company_admin_access_token");

		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] callflows = test_data.get(1).split(",");
		String agency_callflow = callflows[0], company_callflow = callflows[1], location_callflow = callflows[2];
		for(String callflow:new String[]{agency_callflow,company_callflow,location_callflow}){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(callflow));
			json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
			json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
			json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
			json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
			json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when company admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(callflow.equals(agency_callflow)){
					Assert.assertEquals(result_data, "error", "API returns success when company admin try to add custom_source at agency level callflow.");
					test.log(LogStatus.PASS, "Check API returns error when company admin try to add custom_source at agency level callflow.");
					String err_data = api_response.get("err").toString();
					Assert.assertEquals(err_data, "You don't have permission to access call_flow_id "+callflow, "Proper validation message is not displayed when company admin try to add custom_source at agency level callflow.");
					test.log(LogStatus.PASS, "Proper validation message is not displayed when company admin try to add custom_source at agency level callflow.");
					Assert.assertNull(api_response.get("data"), "Data field is not null in response when company admin try to add custom_source at agency level callflow.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   test.log(LogStatus.PASS, "Check API returns success when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   Assert.assertNull(api_response.get("err"), "API returns validation message when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   test.log(LogStatus.PASS, "Check API returns no validation message when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   String data_value = api_response.get("data").toString();
				   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper message is not displayed when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   test.log(LogStatus.PASS, "Check proper message is displayed when company admin try to add custom_source of all level groups at "+(callflow.equals(company_callflow)?"company":"location")+" level callflow.");
				   
				   // Delete the added custom source from callflow
				   ArrayList<String> type = new ArrayList<String>();
				   type.add("custom_source_type_1");
				   type.add("custom_source_type_2");
				   type.add("custom_source_type_3");
				   type.add("custom_source_type_4");
				   type.add("custom_source_type_5");
				   
				   delete_custom_source_from_callflow(callflow, type);
			   } 
			}  
		}
	}
	
//	@Test(priority=30)
	public void post_callflow_customsource_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_callflow_customsource_with_location_admin_access_token", "To validate whether user is able to add customsources to callflow through POST callflow/customsources API with location admin access_token.");
		test.assignCategory("CFA POST /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "post_callflow_customsource_with_location_admin_access_token");

		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] callflows = test_data.get(1).split(",");
		String agency_callflow = callflows[0], company_callflow = callflows[1], location_callflow = callflows[2];
		for(String callflow:new String[]{agency_callflow,company_callflow,location_callflow}){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(callflow));
			json.put("custom_source_type_1", Integer.parseInt(test_data.get(2)));
			json.put("custom_source_type_2", Integer.parseInt(test_data.get(3)));
			json.put("custom_source_type_3", Integer.parseInt(test_data.get(4)));
			json.put("custom_source_type_4", Integer.parseInt(test_data.get(5)));
			json.put("custom_source_type_5", Integer.parseInt(test_data.get(6)));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/customsources", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when location admin try to add custom_source of all level groups at "+(callflow.equals(agency_callflow)?"agency":callflow.equals(company_callflow)?"company":"location")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(callflow.equals(agency_callflow) || callflow.equals(company_callflow)){
					Assert.assertEquals(result_data, "error", "API returns success when location admin try to add custom_source at "+(callflow.equals(agency_callflow)?"agency":"company")+" level callflow.");
					test.log(LogStatus.PASS, "Check API returns error when location admin try to add custom_source at "+(callflow.equals(agency_callflow)?"agency":"company")+" level callflow.");
					String err_data = api_response.get("err").toString();
					Assert.assertEquals(err_data, "You don't have permission to access call_flow_id "+callflow, "Proper validation message is not displayed when location admin try to add custom_source at "+(callflow.equals(agency_callflow)?"agency":"company")+" level callflow.");
					test.log(LogStatus.PASS, "Proper validation message is not displayed when location admin try to add custom_source at "+(callflow.equals(agency_callflow)?"agency":"company")+" level callflow.");
					Assert.assertNull(api_response.get("data"), "Data field is not null in response when location admin try to add custom_source at "+(callflow.equals(agency_callflow)?"agency":"company")+" level callflow.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when location admin try to add custom_source of all level groups at location level callflow.");
				   test.log(LogStatus.PASS, "Check API returns success when location admin try to add custom_source of all level groups at location level callflow.");
				   Assert.assertNull(api_response.get("err"), "API returns validation message when location admin try to add custom_source of all level groups at location level callflow.");
				   test.log(LogStatus.PASS, "Check API returns no validation message when location admin try to add custom_source of all level groups at location level callflow.");
				   String data_value = api_response.get("data").toString();
				   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper message is not displayed when location admin try to add custom_source of all level groups at location level callflow.");
				   test.log(LogStatus.PASS, "Check proper message is displayed when location admin try to add custom_source of all level groups at location level callflow.");
				   
				   // Delete the added custom source from callflow
				   ArrayList<String> type = new ArrayList<String>();
				   type.add("custom_source_type_1");
				   type.add("custom_source_type_2");
				   type.add("custom_source_type_3");
				   type.add("custom_source_type_4");
				   type.add("custom_source_type_5");
				   
				   delete_custom_source_from_callflow(callflow, type);
			   } 
			}  
		}
	}	
	
	public void delete_custom_source_from_callflow(String id, ArrayList<String> cs_type) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(id));
		json.put("type", cs_type);
		custom_source.add(json);
		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error while deleting custom source.");
		}   
	}
	
	public void get_callflow_custom_source(String callflow_id, JSONObject cs_data) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callflow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/customsources", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_content = (JSONObject) parser.parse(line);
		   JSONObject data_value = (JSONObject)response_content.get("data");
		   Set map = cs_data.entrySet();
		   Iterator itr = map.iterator();
		   while(itr.hasNext()){
			  Map.Entry entry = (Map.Entry)itr.next();
			  if(entry.getKey()!="call_flow_id"){
				  Assert.assertTrue(data_value.containsKey(entry.getKey()), "Added custom_source_type("+entry.getKey()+") is not displayed.");
				  JSONObject custom_source_type = (JSONObject)data_value.get(entry.getKey());
				  Assert.assertEquals(Integer.parseInt(custom_source_type.get("custom_source_id").toString()), entry.getValue(), "Incorrect value is displayed for custom_source_type("+entry.getKey()+") .");	  
			  }
		   }
		}   
	}
	
}
