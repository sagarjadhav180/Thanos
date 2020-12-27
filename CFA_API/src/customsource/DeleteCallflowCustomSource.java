package customsource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class DeleteCallflowCustomSource extends BaseClass{
	public static final String class_name = "DeleteCallflowCustomSource";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@Test(priority=1)
	public void delete_callflow_customsource_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_callflow_customsource_without_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API without access_token");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_without_access_token");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		ArrayList<String> cs_type = new ArrayList<String>();
		for(String type:test_data.get(2).split(",")){
			cs_type.add(type);
		}
		json.put("type", cs_type);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", "", custom_source);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=2)
	public void delete_callflow_customsource_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_callflow_customsource_with_invalid_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with invalid access_token");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_invalid_access_token");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		ArrayList<String> cs_type = new ArrayList<String>();
		for(String type:test_data.get(2).split(",")){
			cs_type.add(type);
		}
		json.put("type", cs_type);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", invalid_access_token, custom_source);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified http status code when invalid access_token is passed");
	}
	
	@Test(priority=3)
	public void delete_callflow_customsource_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_callflow_customsource_with_expired_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with expired access_token");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_expired_access_token");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		ArrayList<String> cs_type = new ArrayList<String>();
		for(String type:test_data.get(2).split(",")){
			cs_type.add(type);
		}
		json.put("type", cs_type);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", expired_access_token, custom_source);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void delete_callflow_customsource_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_valid_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with expired access_token");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_valid_access_token");

		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid access_token is passed.");
		   Assert.assertNull(api_response.get("err"),"err data is not null in response");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid access_token is passed.");
		}   
		
//		get_callflow_custom_source(test_data.get(1),null);
	}
	
	@Test(priority=5)
	public void delete_callflow_customsource_with_blank_array() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_blank_array", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with blank array.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		JSONArray custom_source = new JSONArray();
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code without passing any params.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank array is passed in DELETE callflow/customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank array is passed in DELETE callflow/customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank array is passed in DELETE callflow/customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when blank array is passed in DELETE callflow/customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when blank array is passed in DELETE callflow/customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ARRAY_LENGTH_SHORT", "Invalid code value is returned in response when blank array with DELETE callflow/customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Array is too short (0), minimum 1", "Invalid message value is returned in response when blank array with DELETE callflow/customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when blank array with DELETE callflow/customsource api method.");
		}
	}	
	
	@Test(priority=6)
	public void delete_callflow_customsource_without_any_params() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_without_any_params", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API without passing any parameter.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code without passing any parameter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when no parameter is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when DELETE no parameter is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: type", "Invalid message value is returned in response when DELETE no parameter is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when DELETE no parameter is passed.");
		}
	}
	
	@Test(priority=7)
	public void delete_callflow_customsource_without_passing_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_without_passing_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API without passing custom_source_id.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_without_passing_call_flow_id");
		JSONArray custom_source = new JSONArray();
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		JSONObject json = new JSONObject();
		json.put("type", cs_types);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code without passing custom_source_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when no parameter is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when DELETE no parameter is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: call_flow_id", "Invalid message value is returned in response when DELETE no parameter is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when DELETE no parameter is passed.");
		}
	}	
	
	@Test(priority=8)
	public void delete_callflow_customsource_with_blank_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_blank_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with blank custom_source_id.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_blank_call_flow_id");
		JSONArray custom_source = new JSONArray();
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		JSONObject json = new JSONObject();
		json.put("call_flow_id", test_data.get(1));
		json.put("type", cs_types);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code with blank custom_source_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank call_flow_id is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank call_flow_id is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank call_flow_id is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when blank call_flow_id is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when blank call_flow_id is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when DELETE blank call_flow_id is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when DELETE blank call_flow_id is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when DELETE blank call_flow_id is passed.");
		}
	}
	
	@Test(priority=9)
	public void delete_callflow_customsource_with_nonexisting_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_nonexisting_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with non existing custom_source_id.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_nonexisting_call_flow_id");
		JSONArray custom_source = new JSONArray();
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(test_data.get(1)));
		json.put("type", cs_types);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code with non existing call_flow_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result = json_response.get("result").toString();
			Assert.assertEquals(result, "error", "API is returning success when non existing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning erro when non existing call_flow_id is passed");
			Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect error data is displayed when non existing call_flow_id is passed.");
			Assert.assertEquals(json_response.get("data").toString(), "Please provide valid call flow id", "Incorrect validation message is displayed when non existing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when non existing call_flow_id is passed");
		}
	}
	
	@Test(priority=10)
	public void delete_callflow_customsource_with_other_billing_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_other_billing_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with other billing custom_source_id.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_other_billing_call_flow_id");
		JSONArray custom_source = new JSONArray();
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		String call_flow_id = DBCallFlowsUtils.getOtherBillingCallFlowIdWithCustomSource("8");
		
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code with other billing call_flow_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result = json_response.get("result").toString();
			Assert.assertEquals(result, "error", "API is returning success when other billing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning erro when other billing call_flow_id is passed");
			Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect error data is displayed when other billing call_flow_id is passed.");
			Assert.assertEquals(json_response.get("data").toString(), "You don't have permission to access call_flow_id "+call_flow_id, "Incorrect validation message is displayed when other billing call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when other billing call_flow_id is passed");
		}
	}	
	
	@Test(priority=11)
	public void delete_callflow_customsource_with_invalid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_invalid_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with blank custom_source_id.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_invalid_call_flow_id");
		for(String callflow:test_data.get(1).split(",")){
			JSONArray custom_source = new JSONArray();
			ArrayList<String> cs_types = new ArrayList<String>();
			for(String cs_type:test_data.get(2).split(",")){
				cs_types.add(cs_type);
			}
			JSONObject json = new JSONObject();
			json.put("call_flow_id", callflow);
			json.put("type", cs_types);
			custom_source.add(json);
			HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code with invalid("+callflow+") custom_source_id.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+callflow+") custom_source_id is passed.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/customsources");
				list.add("delete");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+callflow+") custom_source_id is passed.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when invalid("+callflow+") custom_source_id is passed.");
			}
		}
	}
	
	@Test(priority=12)
	public void delete_callflow_customsource_with_valid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_valid_call_flow_id", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with valid call_flow_id");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_valid_call_flow_id");
	
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
	
		
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid custom_source_id is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid custom_source_id is passed.");
		   Assert.assertNull(api_response.get("err"),"err data is not null in response");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when valid custom_source_id is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid custom_source_id is passed.");
		}   
		
//		get_callflow_custom_source(test_data.get(1),null);
	}	
	
	@Test(priority=13)
	public void delete_callflow_customsource_without_passing_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_without_passing_type", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API without passing type.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_without_passing_type");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", test_data.get(1));
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code without passing type.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when  is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when no parameter is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: type", "Invalid message value is returned in response when no parameter is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when no parameter is passed.");
		}
	}	
	
	@Test(priority=14)
	public void delete_callflow_customsource_with_blank_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_blank_type", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with blank type value.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_blank_type");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", test_data.get(1));
		json.put("type", "");
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code with blank type.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank type is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank type is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank type is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when blank type is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when blank type is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank type is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type array but found type string", "Invalid message value is returned in response when blank type is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when blank type is passed.");
			String error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(error_description, "Valid Custom source type should be 'custom_source_type_1', 'custom_source_type_2', 'custom_source_type_3', 'custom_source_type_4', and 'custom_source_type_5'.");
		}
	}
	
	@Test(priority=15)
	public void delete_callflow_customsource_with_invalid_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_invalid_type", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with invalid type value.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_invalid_type");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		for(String type:test_data.get(2).split(",")){
			json.put("call_flow_id", test_data.get(1));
			json.put("type", type);
			custom_source.add(json);
			HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code with invalid("+type+") type.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+type+") type is passed.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+type+") type is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+type+") type is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+type+") type is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when invalid("+type+") type is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/customsources");
				list.add("delete");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+type+") type is passed.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type array but found type string", "Invalid message value is returned in response when invalid("+type+") type is passed.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when invalid("+type+") type is passed.");
				String error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(error_description, "Valid Custom source type should be 'custom_source_type_1', 'custom_source_type_2', 'custom_source_type_3', 'custom_source_type_4', and 'custom_source_type_5'.");
			}
		}	
	}
	
	@Test(priority=16)
	public void delete_callflow_customsource_with_non_existing_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_non_existing_type", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with non existing type value.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_non_existing_type");
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", test_data.get(1));
		ArrayList<String> type = new ArrayList<String>();
		type.add(test_data.get(2));
		json.put("type", type);
		custom_source.add(json);
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code with non existing type.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when non existing type is passed.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when non existing type is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when non existing type is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (CallflowCustomSourceDelete): Value failed JSON Schema validation", "Invalid message value is returned in response when non existing type is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "CallflowCustomSourceDelete", "Invalid name value is returned in response when non existing type is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/customsources");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when non existing type is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: "+test_data.get(2), "Invalid message value is returned in response when non existing type is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(!sub_error_path.isEmpty(), "Invalid path value is displayed when blank array when non existing type is passed.");
		}
	}
	
	@Test(priority=17)
	public void delete_callflow_customsource_with_valid_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_valid_type", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with valid type value");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_valid_type");
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid type is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid type is passed.");
		   Assert.assertNull(api_response.get("err"),"err data is not null in response");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when valid type is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid custom_source_id is passed.");
		}   
		
//		get_callflow_custom_source(test_data.get(1),null);
		test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
	}	
	
	@Test(priority=18)
	public void delete_callflow_customsource_with_all_types() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_all_types", "To validate whether user is able to remove all added customsources from callflow through delete callflow/customsources API");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_all_types");
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when all custom source types is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when all custom source types are passed while deleting custom source.");
		   test.log(LogStatus.PASS, "API returns success when all custom source types are passed while deleting custom source.");
		   Assert.assertNull(api_response.get("err"),"err data is not null in response");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when all custom source types are passed while deleting custom source.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when all custom source types are passed while deleting custom source.");
		}   
		
//		get_callflow_custom_source(test_data.get(1),null);
		test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
	}
	
	@Test(priority=19)
	public void delete_callflow_customsource_with_some_types_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_some_types_value", "To validate whether user is able to remove some customsources from callflow through delete callflow/customsources API");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_some_types_value");
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", new ArrayList<String>(cs_types.subList(0, 3)));
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when some custom source types is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when all custom source types are passed while deleting custom source.");
		}   
//		cs_types.removeAll(new ArrayList<String>(cs_types.subList(0, 3)));
//		get_callflow_custom_source(test_data.get(1),cs_types);
//		test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
	}	
	
	@Test(priority=20)
	public void delete_callflow_customsource_with_type_where_cs_is_not_added() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_type_where_cs_is_not_added", "To validate delete callflow/customsources with type value where custom_source was not added.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_type_where_cs_is_not_added");
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		Map<String, Object> confCallFLowHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id=confCallFLowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, new ArrayList<String>(cs_types.subList(0, 3)), custom_source_id);	
		
		JSONArray custom_source = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		json.put("type", cs_types);
		custom_source.add(json);		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when such type value is passed where custom_source was not added");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when such type value is passed where custom_source was not added while deleting custom source.");
		   test.log(LogStatus.PASS, "API returns error when such type value is passed where custom_source was not added while deleting custom source.");
		   Assert.assertEquals(api_response.get("err"), "error", "Incorrect err data is displayed.");
		   String data_value = api_response.get("data").toString();
		   cs_types.removeAll(new ArrayList<String>(cs_types.subList(0, 3)));
		   Assert.assertEquals(data_value, "You are trying to delete non existing values '"+cs_types.get(0)+"','"+cs_types.get(1)+"' of call_flow_id = "+call_flow_id);
		}   
	}	
	
	@Test(priority=21)
	public void delete_callflow_customsource_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_agency_admin_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with agency admin access_token.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_agency_admin_access_token");
//		String[] callflows = test_data.get(1).split(",");
//		String agency_callflow = callflows[0], company_callflow = callflows[1], location_callflow = callflows[2];
		
		Map<String, Object> confCallFLowHierarchyAgency = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String agency_call_flow_id=confCallFLowHierarchyAgency.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCallFLowHierarchyCompany = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String company_call_flow_id=confCallFLowHierarchyCompany.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCallFLowHierarchyLocation = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		String location_call_flow_id=confCallFLowHierarchyLocation.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		for(String call_flow_id:new String[]{agency_call_flow_id,company_call_flow_id,location_call_flow_id}){
			ArrayList<String> cs_types = new ArrayList<String>();
			for(String cs_type:test_data.get(2).split(",")){
				cs_types.add(cs_type);
			}
			post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, custom_source_id);	
			
			JSONArray custom_source = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(call_flow_id));
			json.put("type", cs_types);
			custom_source.add(json);		
			HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when agency admin try to remove custom_source from "+(call_flow_id.equals(agency_call_flow_id)?"agency":call_flow_id.equals(company_call_flow_id)?"company":"location"+ " level callflow."));
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when agency admin try to remove custom_source from "+(call_flow_id.equals(agency_call_flow_id)?"agency":call_flow_id.equals(company_call_flow_id)?"company":"location")+ " level callflow.");
			   test.log(LogStatus.PASS, "API returns success when agency admin try to remove custom_source from "+(call_flow_id.equals(agency_call_flow_id)?"agency":call_flow_id.equals(company_call_flow_id)?"company":"location")+ " level callflow.");
			   Assert.assertNull(api_response.get("err"),"err data is not null in response");
			   String success_message = api_response.get("data").toString();
			   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when agency admin try to remove custom_source from "+(call_flow_id.equals(agency_call_flow_id)?"agency":call_flow_id.equals(company_call_flow_id)?"company":"location")+ " level callflow.");
			   test.log(LogStatus.PASS, "Check proper success message is displayed when agency admin try to remove custom_source from "+(call_flow_id.equals(agency_call_flow_id)?"agency":call_flow_id.equals(company_call_flow_id)?"company":"location")+ " level callflow.");
			}   
			
//			get_callflow_custom_source(call_flow_id,null);
//			test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
		}
	}
	
//	@Test(priority=22)
	public void delete_callflow_customsource_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_company_admin_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with company admin access_token.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_company_admin_access_token");
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] callflows = test_data.get(1).split(",");
		String agency_callflow = callflows[0], company_callflow = callflows[1], location_callflow = callflows[2];
		for(String call_flow_id:new String[]{agency_callflow,company_callflow,location_callflow}){
			ArrayList<String> cs_types = new ArrayList<String>();
			for(String cs_type:test_data.get(2).split(",")){
				cs_types.add(cs_type);
			}
			post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, test_data.get(3));	
			
			JSONArray custom_source = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(call_flow_id));
			json.put("type", cs_types);
			custom_source.add(json);		
			HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when company admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":call_flow_id.equals(agency_callflow)?"company":"location"+ " level callflow."));
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(call_flow_id.equals(agency_callflow)){
				   Assert.assertEquals(result_data, "error", "API returns success when company admin try to remove custom_source from agency level callflow.");
				   test.log(LogStatus.PASS, "Check API returns error when company admin try to remove custom_source from agency level callflow.");
				   Assert.assertEquals(api_response.get("err").toString(), "error", "Incorrect err value is displayed in API response.");
				   Assert.assertEquals(api_response.get("data").toString(), "You don't have permission to access call_flow_id "+call_flow_id, "Proper validation is not displayed when company admin try to remove custom_source from agency level callflow.");
				   test.log(LogStatus.PASS, "Check whether proper validation is displayed when company admin try to remove custom_source from agency level callflow");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when company admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"company":"location")+ " level callflow.");
				   test.log(LogStatus.PASS, "API returns success when company admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"company":"location")+ " level callflow.");
				   Assert.assertNull(api_response.get("err"),"err data is not null in response");
				   String success_message = api_response.get("data").toString();
				   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when company admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"company":"location")+ " level callflow.");
				   test.log(LogStatus.PASS, "Check proper success message is displayed when company admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"company":"location")+ " level callflow.");
				   get_callflow_custom_source(call_flow_id,null);
				   test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource"); 
			   }
			}   
		}
	}
	
//	@Test(priority=23)
	public void delete_callflow_customsource_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_with_location_admin_access_token", "To validate whether user is able to remove customsources from callflow through delete callflow/customsources API with location admin access_token.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_with_location_admin_access_token");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] callflows = test_data.get(1).split(",");
		String agency_callflow = callflows[0], company_callflow = callflows[1], location_callflow = callflows[2];
		for(String call_flow_id:new String[]{agency_callflow,company_callflow,location_callflow}){
			ArrayList<String> cs_types = new ArrayList<String>();
			for(String cs_type:test_data.get(2).split(",")){
				cs_types.add(cs_type);
			}
			post_callflow_customsource_to_add_cs_at_callflow(call_flow_id, cs_types, test_data.get(3));	
			
			JSONArray custom_source = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("call_flow_id", Integer.parseInt(call_flow_id));
			json.put("type", cs_types);
			custom_source.add(json);		
			HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when location admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":call_flow_id.equals(agency_callflow)?"company":"location"+ " level callflow."));
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(call_flow_id.equals(agency_callflow) || call_flow_id.equals(company_callflow)){
				   Assert.assertEquals(result_data, "error", "API returns success when location admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":"company")+ " level callflow.");
				   test.log(LogStatus.PASS, "Check API returns error when location admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":"company")+ " level callflow.");
				   Assert.assertEquals(api_response.get("err").toString(), "error", "Incorrect err value is displayed in API response.");
				   Assert.assertEquals(api_response.get("data").toString(), "You don't have permission to access call_flow_id "+call_flow_id, "Proper validation is not displayed when location admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":"company")+ " level callflow.");
				   test.log(LogStatus.PASS, "Check whether proper validation is displayed when location admin try to remove custom_source from "+(call_flow_id.equals(agency_callflow)?"agency":"company")+ " level callflow");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when location admin try to remove custom_source from location level callflow.");
				   test.log(LogStatus.PASS, "API returns success when location admin try to remove custom_source from location level callflow.");
				   Assert.assertNull(api_response.get("err"),"err data is not null in response");
				   String success_message = api_response.get("data").toString();
				   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when location admin try to remove custom_source from location level callflow.");
				   test.log(LogStatus.PASS, "Check proper success message is displayed when location admin try to remove custom_source from location level callflow.");
				   get_callflow_custom_source(call_flow_id,null);
				   test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
			   }
			}   
		}
	}	
	
	@Test(priority=24)
	public void delete_callflow_customsource_from_multiple_callflow() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_callflow_customsource_from_multiple_callflow", "To validate whether user is able to remove customsources from multiple callflows through delete callflow/customsources API.");
		test.assignCategory("CFA DELETE /callflow/customsources API");
		test_data = HelperClass.readTestData(class_name, "delete_callflow_customsource_from_multiple_callflow");
		String[] call_flows = test_data.get(1).split(",");
		ArrayList<String> cs_types = new ArrayList<String>();
		for(String cs_type:test_data.get(2).split(",")){
			cs_types.add(cs_type);
		}
		
		Map<String, Object> confCallFLowHierarchyAgency = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String agency_call_flow_id=confCallFLowHierarchyAgency.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		Map<String, Object> confCallFLowHierarchyCompany = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		String company_call_flow_id=confCallFLowHierarchyCompany.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_id=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		
		post_callflow_customsource_to_add_cs_at_callflow(agency_call_flow_id, cs_types, custom_source_id);	
		post_callflow_customsource_to_add_cs_at_callflow(company_call_flow_id, cs_types, custom_source_id);
		
		JSONArray custom_source = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.put("call_flow_id", Integer.parseInt(agency_call_flow_id));
		json1.put("type", cs_types);
		custom_source.add(json1);
		
		JSONObject json2 = new JSONObject();
		json2.put("call_flow_id", Integer.parseInt(company_call_flow_id));
		json2.put("type", cs_types);
		custom_source.add(json2);
		
		HttpResponse response = HelperClass.make_delete_request("/v2/callflow/customsources", access_token, custom_source);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid type is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid type is passed.");
		   Assert.assertNull(api_response.get("err"),"err data is not null in response");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted Custom Source to the call flow.", "Incorrect success message is displayed when valid type is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid custom_source_id is passed.");
		}   
		
//		get_callflow_custom_source(call_flows[0],null);
//		get_callflow_custom_source(call_flows[1],null);
//		test.log(LogStatus.PASS, "To check whether removed custom source is not displaying in get callflow/customsource");
	}	
	
	public void post_callflow_customsource_to_add_cs_at_callflow(String call_flow_id, ArrayList<String> cs_type, String cs_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("call_flow_id", Integer.parseInt(call_flow_id));
		for(String custom_source_type: cs_type){
			json.put(custom_source_type, Integer.parseInt(cs_id));
		}
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
		   Assert.assertNull(api_response.get("err"), "API returns validation message when valid access_token is passed.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertEquals(data_value, "Successfully added custom sources to the call flow", "Proper validation is not displayed when valid access_token is passed.");
		} 
	}
	
	public void get_callflow_custom_source(String callflow_id, ArrayList<String> cs_data) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
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
		   if(cs_data==null){
			   Assert.assertEquals(data_value, new JSONObject(), "Deleted data is still displaying in get callflow/customsource response.");
		   }
		   else{
			   for(String custom_source_type:cs_data){
				   Assert.assertTrue(data_value.containsKey(custom_source_type), "custom_source_type("+custom_source_type+") is not displayed.");
			   }
		   }
		}   
	}	
}
