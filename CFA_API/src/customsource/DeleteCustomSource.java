package customsource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.convirza.tests.core.utils.DBCustomSourceUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class DeleteCustomSource extends BaseClass{
	public static final String class_name = "DeleteCustomSource";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@Test(priority=1)
	public void delete_custom_source_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_custom_source_without_access_token", "To validate whether user is able to delete custom sources through Delete customsource API without access_token");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_without_access_token");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(test_data.get(1)));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", "", json);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=2)
	public void delete_custom_source_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_custom_source_with_invalid_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with invalid access_token");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_invalid_access_token");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(test_data.get(1)));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", invalid_access_token, json);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified http status code when invalid access_token is passed");
	}
	
	@Test(priority=3)
	public void delete_custom_source_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("delete_custom_source_with_expired_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with expired access_token");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_expired_access_token");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(test_data.get(1)));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", expired_access_token, json);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void delete_custom_source_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_valid_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with valid access_token");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_valid_access_token");
		
		Map<String, Object> confGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String id = create_cs_for_deletion(test_data.get(2),group_id);
				
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(id));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error while deleting custom source.");
		   test.log(LogStatus.PASS, "Check API returns success when valid access_token is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation when valid access_token is passed.");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid access_token is passed.");
		   // Delete the created custom source
		}  
	}

	@Test(priority=5)
	public void delete_custom_source_with_blank_json() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_blank_json", "To validate whether user is able to delete custom sources through Delete customsource API when blank json is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		JSONObject json = new JSONObject();
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank json is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank json is passed in Delete customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank json is passed in Delete customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank json is passed in Delete customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when blank json is passed in Delete customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when blank json is passed in Delete customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when blank json with Delete customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: custom_sources", "Invalid message value is returned in response when blank json with Delete customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when blank json with Delete customsource api method.");
		}  
	}
	
	@Test(priority=6)
	public void delete_custom_source_with_blank_custom_source() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_blank_custom_source", "To validate whether user is able to delete custom sources through Delete customsource API when blank custom_source is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank custom_source is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank json is passed in Delete customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank json is passed in Delete customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank json is passed in Delete customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when blank json is passed in Delete customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when blank json is passed in Delete customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ARRAY_LENGTH_SHORT", "Invalid code value is returned in response when blank json with Delete customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Array is too short (0), minimum 1", "Invalid message value is returned in response when blank json with Delete customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			ArrayList<String> sub_path = new ArrayList<String>();
			sub_path.add("custom_sources");
			Assert.assertEquals(sub_error_path, sub_path, "Invalid path value is displayed when blank json with Delete customsource api method.");
		}  
	}
	
	// @Test(priority=7)
	public void delete_custom_source_with_invalid_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_invalid_custom_source_id", "To validate whether user is able to delete custom sources through Delete customsource API when invalid custom_source_id is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_invalid_custom_source_id");
		String[] custom_sources = test_data.get(1).split(",");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		for(String cs : custom_sources){
			custom_source.clear();
			json.clear();
			custom_source.add(cs);
			json.put("custom_sources", custom_source);
			HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+cs+") custom_source is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				// Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+cs+") custom_source is passed in Delete customsource api.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+cs+") custom_source is passed in Delete customsource api.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+cs+") custom_source is passed in Delete customsource api.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+cs+") custom_source is passed in Delete customsource api.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when invalid("+cs+") custom_source is passed in Delete customsource api.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/customsource");
				list.add("delete");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+cs+") custom_source with Delete customsource api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+cs+") custom_source with Delete customsource api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				ArrayList<String> sub_path = new ArrayList<String>();
				sub_path.add("custom_sources");
				sub_path.add("0");
				Assert.assertEquals(sub_error_path, sub_path, "Invalid path value is displayed when invalid("+cs+") custom_source with Delete customsource api method.");
			}  
		}
	}
	
	@Test(priority=8)
	public void delete_custom_source_with_negative_custom_source() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_negative_custom_source", "To validate whether user is able to delete custom sources through Delete customsource API when negative custom_source is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_negative_custom_source");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(test_data.get(1));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when negative custom_source is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative custom is passed in Delete customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative custom is passed in Delete customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when negative custom is passed in Delete customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when negative custom is passed in Delete customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when negative custom is passed in Delete customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("delete");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when negative custom with Delete customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when negative custom with Delete customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			ArrayList<String> sub_path = new ArrayList<String>();
			sub_path.add("custom_sources");
			sub_path.add("0");
			Assert.assertEquals(sub_error_path, sub_path, "Invalid path value is displayed when negative custom with Delete customsource api method.");
		}  
	}
	
	@Test(priority=9)
	public void delete_custom_source_with_deleted_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_deleted_custom_source_id", "To validate whether user is able to delete custom sources through Delete customsource API when deleted custom_source is passed.");
		test.assignCategory("CFA DELETE /customsource API");

		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_deleted_custom_source_id");
		Map<String, Object> confGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		int deleted_custom_source_id = DBCustomSourceUtils.getDeletedCustomSourceId(group_id);
		
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		
		custom_source.add(deleted_custom_source_id);
		json.put("custom_sources", custom_source);
		
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when deleted custom_source id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success while deleted custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check API returns error when deleted custom_source id is passed.");
		   String validation_message = api_response.get("err").toString();
		   Assert.assertEquals(validation_message, "[\"custom_source_id "+deleted_custom_source_id+" is already deleted\"]", "Proper validation message is not displayed when deleted custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when deleted custom_source id is passed.");
		   // Delete the created custom source
		}  
	}	
	
	@Test(priority=10)
	public void delete_custom_source_with_valid_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_valid_custom_source_id", "To validate whether user is able to delete custom sources through Delete customsource API when valid custom_source is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_valid_custom_source_id");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String id = create_cs_for_deletion(test_data.get(2),org_unit_id);
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(id));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid custom_source id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error while valid custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid custom_source id is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation when valid custom_source id is passed.");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when valid custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when valid custom_source id is passed.");
		   
//		   Boolean is_deleted_cs_present = get_custom_source_with_id(id, test_data.get(3));
//		   Assert.assertFalse(is_deleted_cs_present, "User is able to see deleted cs in get custom_source API.");
//		   test.log(LogStatus.PASS, "Check whether user is able to see deleted CS in get customsource API.");
		}  
	}	
	
	// @Test(priority=11)
	public void delete_custom_source_with_duplicate_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_duplicate_custom_source_id", "To validate whether user is able to delete custom sources through Delete customsource API when duplicate custom_source is passed.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_duplicate_custom_source_id");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(test_data.get(1)));
		custom_source.add(Integer.parseInt(test_data.get(1)));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when duplicate custom_source id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success while duplicate custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check API returns error when duplicate custom_source id is passed.");
		   String error_data = api_response.get("err").toString();
		   Assert.assertEquals(error_data, "error", "Incorrect error data is displayed when duplicate custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check proper error data is displayed when duplicate custom_source id is passed.");
		   String validation = api_response.get("data").toString();
		   Assert.assertEquals(validation, "Repeated custom_source_id not allowed", "Proper validation is not displayed when duplicate custom_source id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when duplicate custom_source id is passed.");
		   // Delete the created custom source
		}  
	}	
	
	// @Test(priority=12)
	public void delete_custom_source_with_multiple_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_multiple_custom_source_id", "To validate whether user is able to delete custom sources through Delete customsource API with multiple custom_source_id.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_multiple_custom_source_id");
		String id1 = create_cs_for_deletion(test_data.get(2),test_data.get(3));
		String id2 = create_cs_for_deletion(test_data.get(2),test_data.get(3));
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(id1));
		custom_source.add(Integer.parseInt(id2));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when multiple custom_source_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when multiple custom_source_id is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when multiple custom_source_id is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation when multiple custom_source_id is passed.");
		   String success_message = api_response.get("data").toString();
		   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when multiple custom_source_id is passed.");
		   test.log(LogStatus.PASS, "Check proper success message is displayed when multiple custom_source_id is passed.");
		   
		   Boolean is_deleted_cs_present = get_custom_source_with_id(id1, test_data.get(3));
		   Assert.assertFalse(is_deleted_cs_present, "User is able to see deleted cs in get custom_source API.");
		   is_deleted_cs_present = get_custom_source_with_id(id2, test_data.get(3));
		   Assert.assertFalse(is_deleted_cs_present, "User is able to see deleted cs in get custom_source API.");
		   test.log(LogStatus.PASS, "Check whether user is able to see deleted CS in get customsource API.");
		}  
	}	
	
	// @Test(priority=13)
	public void delete_custom_source_with_agency_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_agency_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with agency admin access_token.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_agency_access_token");
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		String[] groups = test_data.get(3).split(",");
		String agency_grp = groups[0], company_grp = groups[1], location_grp = groups[2];
		for(String group_id : new String[]{agency_grp,company_grp,location_grp}){
			String id = create_cs_for_deletion(test_data.get(2),group_id);
			custom_source.clear();
			json.clear();
			custom_source.add(Integer.parseInt(id));	
			json.put("custom_sources", custom_source);
			HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			   test.log(LogStatus.PASS, "Check API returns success when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			   Assert.assertNull(api_response.get("err"), "API returns validation when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			   String success_message = api_response.get("data").toString();
			   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			   test.log(LogStatus.PASS, "Check proper success message is displayed when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			} 
		}
	}
	
//	@Test(priority=14)
	public void delete_custom_source_with_company_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_company_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with company admin access_token.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_company_access_token");
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		String[] groups = test_data.get(3).split(",");
		String agency_grp = groups[0], company_grp = groups[1], location_grp = groups[2];
		for(String group_id : new String[]{agency_grp,company_grp,location_grp}){
			String id = "";
			if(!group_id.equals(agency_grp))
				id = create_cs_for_deletion(test_data.get(2),group_id);
			else
				id = test_data.get(1);
			custom_source.clear();
			json.clear();
			custom_source.add(Integer.parseInt(id));	
			json.put("custom_sources", custom_source);
			HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(group_id.equals(agency_grp)){
				   Assert.assertEquals(result_data, "error", "API returns success when agency level custom_source id is passed is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns error when agency level custom_source id is passed is passed using company admin access_token.");
				   String validation_message = api_response.get("err").toString();
				   Assert.assertEquals(validation_message, "[\"You are not authorized to manage this custom_source_id "+test_data.get(1)+"\"]", "Proper validation message is not displayed when agency level custom_source id is passed is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check proper validation message is displayed when agency level custom_source id is passed is passed using company admin access_token.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when "+(group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
				   Assert.assertNull(api_response.get("err"), "API returns validation when "+(group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
				   String success_message = api_response.get("data").toString();
				   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when "+(group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check proper success message is displayed when "+(group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using agency admin access_token.");
			   }
			} 
		}
	}
	
//	@Test(priority=15)
	public void delete_custom_source_with_location_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("delete_custom_source_with_location_access_token", "To validate whether user is able to delete custom sources through Delete customsource API with company admin access_token.");
		test.assignCategory("CFA DELETE /customsource API");
		test_data = HelperClass.readTestData(class_name, "delete_custom_source_with_location_access_token");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		String[] groups = test_data.get(3).split(",");
		String agency_grp = groups[0], company_grp = groups[1], location_grp = groups[2];
		for(String group_id : new String[]{agency_grp,company_grp,location_grp}){
			String id = "";
			if(group_id.equals(location_grp))
				id = create_cs_for_deletion(test_data.get(2),group_id);
			else if(group_id.equals(company_grp)){
				id = test_data.get(1).split(",")[1];
			}
			else
				id = test_data.get(1).split(",")[0];
			custom_source.clear();
			json.clear();
			custom_source.add(Integer.parseInt(id));	
			json.put("custom_sources", custom_source);
			HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(group_id.equals(agency_grp)?"agency":group_id.equals(company_grp)?"company":"location")+" level custom_source id is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(group_id.equals(agency_grp)||group_id.equals(company_grp)){
				   Assert.assertEquals(result_data, "error", "API returns success when "+(group_id.equals(agency_grp)?"agency":"company")+" level custom_source id is passed is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns error when "+(group_id.equals(agency_grp)?"agency":"company")+" level custom_source id is passed is passed using company admin access_token.");
				   String validation_message = api_response.get("err").toString();
				   Assert.assertEquals(validation_message, "[\"You are not authorized to manage this custom_source_id "+id+"\"]", "Proper validation message is not displayed when "+(group_id.equals(agency_grp)?"agency":"company")+" level custom_source id is passed is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check proper validation message is displayed when "+(group_id.equals(agency_grp)?"agency":"company")+" level custom_source id is passed is passed using company admin access_token.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when location level custom_source id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when location level custom_source id is passed using agency admin access_token.");
				   Assert.assertNull(api_response.get("err"), "API returns validation when location level custom_source id is passed using agency admin access_token.");
				   String success_message = api_response.get("data").toString();
				   Assert.assertEquals(success_message, "Successfully deleted the Custom Source.", "Proper success message is displayed when location level custom_source id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check proper success message is displayed when location level custom_source id is passed using agency admin access_token.");
			   }
			} 
		}
	}	
	
	public String create_cs_for_deletion(String cs_name, String org_unit_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", cs_name+r.nextInt(500000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = ""; String created_cs = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when valid access_token is passed.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
		   created_cs = data_value.replaceAll("\\D+","");
		} 
		return created_cs;
	}	
	
	public Boolean get_custom_source_with_id(String id, String group_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = ""; Boolean is_custom_source_present = false;
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   if(custom_source.get("custom_source_id").toString().equals(id))
				   is_custom_source_present = true;			   
		   }
		}   
		return is_custom_source_present;
	}
}	