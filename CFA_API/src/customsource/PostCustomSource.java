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
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class PostCustomSource extends BaseClass{
	public static final String class_name = "PostCustomSource";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@Test(priority=1)
	public void post_custom_source_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_custom_source_without_access_token", "To validate whether user is able to create custom sources through POST customsource API without access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_without_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_name", test_data.get(2));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", "", array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=2)
	public void post_custom_source_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_custom_source_with_invalid_access_token", "To validate whether user is able to create custom sources through POST customsource API with invalid access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_invalid_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_name", test_data.get(2));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", invalid_access_token, array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified http status code when invalid access_token is passed");
	}
	
	@Test(priority=3)
	public void post_custom_source_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_custom_source_with_expired_access_token", "To validate whether user is able to create custom sources through POST customsource API with expired access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_expired_access_token");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_name", test_data.get(2));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", expired_access_token, array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void post_custom_source_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_valid_access_token", "To validate whether user is able to create custom sources through POST customsource API with valid access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_valid_access_token");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
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
		   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
		   test.log(LogStatus.PASS, "Success message is displayed in response.");
		   String id_returned = data_value.replaceAll("\\D+","");
		   System.out.println("Id: "+id_returned);
		   Assert.assertNotNull(id_returned, "Id is not returned in response.");
		   test.log(LogStatus.PASS, "Id is returned. Returned Id: "+id_returned);
		   
		   // Delete the created custom source
		   delete_custom_source(id_returned);
		}  
	}	
	
	@Test(priority=5)
	public void post_custom_source_with_blank_array() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_blank_array", "To validate whether user is able to create custom sources through POST customsource API when blank array is passed.");
		test.assignCategory("CFA POST /customsources API");
		JSONArray array = new JSONArray();
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank array is passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank array is passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank array is passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when blank array is passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when blank array is passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ARRAY_LENGTH_SHORT", "Invalid code value is returned in response when blank array with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Array is too short (0), minimum 1", "Invalid message value is returned in response when blank array with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when blank array with POST customsource api method.");
		}  
	}
	
	@Test(priority=6)
	public void post_custom_source_with_no_params() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_no_params", "To validate whether user is able to create custom sources through POST customsource API when no parameter is passed.");
		test.assignCategory("CFA POST /customsources API");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when no parameter is passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: custom_source_name", "Invalid message value is returned in response when no parameter with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when no parameter with POST customsource api method.");
		}  
	}
	
	@Test(priority=7)
	public void post_custom_source_without_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_without_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API without custom_source_name field.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_without_custom_source_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code without custom_source_name field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when custom_source_name field is not passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when custom_source_name field is not passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when custom_source_name field is not passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when custom_source_name field is not passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when custom_source_name field is not passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response custom_source_name field is not passed with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: custom_source_name", "Invalid message value is returned in response custom_source_name field is not passed with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed custom_source_name field is not passed with POST customsource api method.");
		}  
	}
	
	@Test(priority=8)
	public void post_custom_source_with_blank_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_blank_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API with blank custom_source_name.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_blank_custom_source_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_name", "");
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank custom_source_name is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank custom_source_name is passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank custom_source_name is passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank custom_source_name is passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when blank custom_source_name is passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when blank custom_source_name is passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when blank custom_source_name is passed with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "String is too short (0 chars), minimum 1", "Invalid message value is returned in response when blank custom_source_name is passed with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank custom_source_name is passed with POST customsource api method.");
			Assert.assertEquals(sub_error_path.get(1), "custom_source_name", "Invalid path value is displayed when blank custom_source_name is passed with POST customsource api method.");
		}  
	}
	
	@Test(priority=9)
	public void post_custom_source_with_empty_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_empty_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API with blank custom_source_name.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_empty_custom_source_name");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", test_data.get(2));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when empty custom_source_name is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when empty custom_source_name is passed.");
			test.log(LogStatus.PASS, "Check API returns error when empty custom_source_name is passed.");
			Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect err value is displayed.");
			String data = json_response.get("data").toString();
			JSONArray validation = new JSONArray();
			validation.add("custom_source_name should not be empty");
			Assert.assertEquals(data, validation.toString(), "API returns success when empty custom_source_name is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when empty custom_source_name is passed.");
		}  
	}
	
	@Test(priority=10)
	public void post_custom_source_with_valid_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_valid_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API with valid custom_source_name");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_valid_custom_source_name");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String[] cs_names = test_data.get(2).split(",");
		for(String name: cs_names){
			Random r = new Random();
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(org_unit_id));
			json.put("custom_source_name", name+r.nextInt(50000));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when valid("+name+") custom_source_name is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when valid("+name+") custom_source_name is passed.");
			   test.log(LogStatus.PASS, "Check API returns success when valid("+name+") custom_source_name is passed.");
			   Assert.assertNull(api_response.get("err"), "API returns validation message when valid("+name+") custom_source_name is passed.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+name+") custom_source_name is passed.");
			   String data_value = api_response.get("data").toString();
			   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
			   test.log(LogStatus.PASS, "Success message is displayed in response.");
			   String id_returned = data_value.replaceAll("\\D+","");
			   System.out.println("Id: "+id_returned);
			   Assert.assertNotNull(id_returned, "Id is not returned in response.");
			   test.log(LogStatus.PASS, "Id is returned. Returned Id: "+id_returned);
			   
			   // Delete the created custom source
			   delete_custom_source(id_returned);
			}
		}  
	}	
	
	@Test(priority=11)
	public void post_custom_source_with_duplicate_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_duplicate_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API with duplicate custom_source_name.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_duplicate_custom_source_name");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Map<String, Object> confCustomSourceHierarchyAgency = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_name_agency=confCustomSourceHierarchyAgency.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_NAME).toString();

		Map<String, Object> confCustomSourceHierarchyCompany = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.COMPANY);
		String custom_source_name_company=confCustomSourceHierarchyCompany.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_NAME).toString();

		Map<String, Object> confCustomSourceHierarchyLocation = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.LOCATION);
		String custom_source_name_location=confCustomSourceHierarchyLocation.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_NAME).toString();
		
		String[] cs_names = test_data.get(2).split(",");
		String agency_cs = custom_source_name_agency, company_cs = custom_source_name_company, location_cs = custom_source_name_location;
		for(String cs_name: new String[]{agency_cs,company_cs,location_cs}){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(org_unit_id));
			json.put("custom_source_name", cs_name);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when duplicate("+cs_name+") custom_source_name is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API returns success when duplicate("+cs_name+") custom_source_name is passed.");
				test.log(LogStatus.PASS, "Check API returns error when duplicate("+cs_name+") custom_source_name is passed.");
				Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect err value is displayed.");
				String data = json_response.get("data").toString();
				JSONArray validation = new JSONArray();
				validation.add("'"+cs_name+"' This custom_source is already exist.");
				Assert.assertEquals(data, validation.toString(), "Proper validation is not displayed when duplicate("+cs_name+") custom_source_name is passed.");
				test.log(LogStatus.PASS, "Check proper validation is displayed when duplicate("+cs_name+") custom_source_name is passed.");
			}
		}  
	}
	
	@Test(priority=12)
	public void post_custom_source_with_deleted_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_deleted_custom_source_name", "To validate whether user is able to create custom sources through POST customsource API with deleted custom_source_name.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_deleted_custom_source_name");
		String[] cs_names = test_data.get(2).split(",");
		String cs_id="";
		
		Map<String, Object> confGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id_agency=confGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		Random r=new Random();
		
		String cs = cs_names[0]+r.nextInt(500);

			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(org_unit_id_agency));
			json.put("custom_source_name", cs);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			
			BufferedReader rd1 = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line1 = "";
			while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line1);
			   
			   String data_value=api_response.get("data").toString();
			   
			   cs_id = data_value.replaceAll("\\D","");
			   
			}
			
			JSONArray cs_sources = new JSONArray();
			JSONObject json1 = new JSONObject();

			cs_sources.add(Integer.parseInt(cs_id));
			json1.put("custom_sources", cs_sources);
			HttpResponse response1= HelperClass.make_delete_request("/v2/customsource", access_token, json1);
			
			JSONArray array2 = new JSONArray();
			JSONObject json2 = new JSONObject();
			json2.put("org_unit_id", Integer.parseInt(org_unit_id_agency));
			json2.put("custom_source_name", cs);
			array2.add(json2);
			CloseableHttpResponse response2 = HelperClass.make_post_request("/v2/customsource", access_token, array2);

			
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when deleted("+cs+") custom_source_name is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when deleted("+cs+") custom_source_name is passed.");
			   test.log(LogStatus.PASS, "Check API returns success when deleted("+cs+") custom_source_name is passed.");
			   Assert.assertNull(api_response.get("err"), "API returns validation message when deleted("+cs+") custom_source_name is passed.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when deleted("+cs+") custom_source_name is passed.");
			   String data_value = api_response.get("data").toString();
			   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
			   test.log(LogStatus.PASS, "Success message is displayed in response.");
			   String id_returned = data_value.replaceAll("\\D+","");
			   System.out.println("Id: "+id_returned);
			   Assert.assertNotNull(id_returned, "Id is not returned in response.");
//			   if(cs_name.equals(agency_cs))
//				   Assert.assertEquals(id_returned, cs_ids[0], "Incorrect Id is displayed in response. Seems like new custom source is being created when agency level CS is passed.");
			   
			   // Delete the created custom source
			   delete_custom_source(id_returned);
			   
			   // Again added deleted custom_source in respective group
//			   if(cs_name.equals(agency_cs))
//				   json.put("org_unit_id", Integer.parseInt("8"));
//			   else if(cs_name.equals(company_cs))
//				   json.put("org_unit_id", Integer.parseInt("114"));
//			   else
//				   json.put("org_unit_id", Integer.parseInt("116"));
//			   HelperClass.make_post_request("/v2/customsource", access_token, array);
			   
			   // Delete the created custom source
//			   delete_custom_source(id_returned);
			}  
		
	}	
	
	@Test(priority=13)
	public void post_custom_source_without_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_without_org_unit_id", "To validate whether user is able to create custom sources through POST customsource API without org_unit_id field.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_without_org_unit_id");
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code without org_unit_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		    // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when org_unit_id field is not passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when org_unit_id field is not passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when org_unit_id field is not passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when org_unit_id field is not passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when org_unit_id field is not passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response org_unit_id field is not passed with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: org_unit_id", "Invalid message value is returned in response org_unit_id field is not passed with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed org_unit_id field is not passed with POST customsource api method.");
		}  
	}
	
	@Test(priority=14)
	public void post_custom_source_with_blank_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_blank_org_unit_id", "To validate whether user is able to create custom sources through POST customsource API with blank org_unit_id.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_blank_org_unit_id");
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", "");
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank org_unit_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank org_unit_id is passed in POST customsource api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank org_unit_id is passed in POST customsource api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank org_unit_id is passed in POST customsource api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when blank org_unit_id is passed in POST customsource api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when blank org_unit_id is passed in POST customsource api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/customsource");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank org_unit_id is passed with POST customsource api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank org_unit_id is passed with POST customsource api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank org_unit_id is passed with POST customsource api method.");
			Assert.assertEquals(sub_error_path.get(1), "org_unit_id", "Invalid path value is displayed when blank org_unit_id is passed with POST customsource api method.");
		}  
	}
	
	@Test(priority=15)
	public void post_custom_source_with_non_existing_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_non_existing_org_unit_id", "To validate whether user is able to create custom sources through POST customsource API with non existing org_unit_id.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_non_existing_org_unit_id");
		Random r = new Random();
		String org_unit_id = test_data.get(1);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing("+org_unit_id+") org_unit_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		    // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when non existing("+org_unit_id+") org_unit_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when non existing("+org_unit_id+") org_unit_id is passed.");
			Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect err value is displayed.");
			String data = json_response.get("data").toString();
			JSONArray validation = new JSONArray();
			validation.add("You are not authorized to create custom sources in group. "+org_unit_id);
			Assert.assertEquals(data, validation.toString(), "Proper validation is not displayed when non existing("+org_unit_id+") org_unit_id is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when non existing("+org_unit_id+") org_unit_id is passed.");
		}  
	}
	
	@Test(priority=16)
	public void post_custom_source_with_other_billing_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_other_billing_org_unit_id", "To validate whether user is able to create custom sources through POST customsource API with other billing org_unit_id.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_other_billing_org_unit_id");
		Random r = new Random();
		String org_unit_id = test_data.get(1);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when other billing("+org_unit_id+") org_unit_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		    // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API returns success when other billing("+org_unit_id+") org_unit_id is passed.");
			test.log(LogStatus.PASS, "Check API returns error when other billing("+org_unit_id+") org_unit_id is passed.");
			Assert.assertEquals(json_response.get("err").toString(), "error", "Incorrect err value is displayed.");
			String data = json_response.get("data").toString();
			JSONArray validation = new JSONArray();
			validation.add("You are not authorized to create custom sources in group. "+org_unit_id);
			Assert.assertEquals(data, validation.toString(), "Proper validation is not displayed when other billing("+org_unit_id+") org_unit_id is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when other billing("+org_unit_id+") org_unit_id is passed.");
		}  
	}
	
	@Test(priority=17)
	public void post_custom_source_with_invalid_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_invalid_org_unit_id", "To validate whether user is able to create custom sources through POST customsource API with invalid org_unit_id.");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_invalid_org_unit_id");
		String[] group_ids = test_data.get(2).split(",");
		for(String group_id:group_ids){
			Random r = new Random();
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", group_id);
			json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+group_id+") org_unit_id is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+group_id+") org_unit_id is passed in POST customsource api.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+group_id+") org_unit_id is passed in POST customsource api.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+group_id+") org_unit_id is passed in POST customsource api.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (customSource): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+group_id+") org_unit_id is passed in POST customsource api.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "customSource", "Invalid name value is returned in response when invalid("+group_id+") org_unit_id is passed in POST customsource api.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/customsource");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+group_id+") org_unit_id is passed with POST customsource api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+group_id+") org_unit_id is passed with POST customsource api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+group_id+") org_unit_id is passed with POST customsource api method.");
				Assert.assertEquals(sub_error_path.get(1), "org_unit_id", "Invalid path value is displayed when invalid("+group_id+") org_unit_id is passed with POST customsource api method.");
			}  
		}
	}	
	
	@Test(priority=18)
	public void post_custom_source_with_multiple_cs_using_same_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_multiple_cs_using_same_name", "To validate whether user is able to create multiple custom sources using same name through POST customsource API with valid access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_multiple_cs_using_same_name");
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(test_data.get(1)));
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when user try to create multiple custom source with same name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when user try to create multiple custom source with same name.");
		   test.log(LogStatus.PASS, "Check API returns error when user try to create multiple custom source with same name.");
		   Assert.assertEquals(api_response.get("err").toString(), "error", "Incorrect err value is displayed.");
		   String data = api_response.get("data").toString();
		   Assert.assertEquals(data, "Repeated custom source names are not allowed", "Proper validation is not displayed when user try to create multiple custom source with same name.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when user try to create multiple custom source with same name.");
		}  
	}
	
	@Test(priority=19)
	public void post_custom_source_with_multiple_cs() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_multiple_cs", "To validate whether user is able to create multiple custom sources using same name through POST customsource API with valid access_token");
		test.assignCategory("CFA POST /customsources API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_multiple_cs");
		Random r = new Random();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("org_unit_id", Integer.parseInt(org_unit_id));
		json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json);
		JSONObject json2 = new JSONObject();
		json2.put("org_unit_id", Integer.parseInt(org_unit_id));
		json2.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
		array.add(json2);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when user try to create multiple custom source with different name.");
		   test.log(LogStatus.PASS, "Check API returns success when user try to create multiple custom source with different name.");
		   Assert.assertNull(api_response.get("err"), "API returns validation message when user try to create multiple custom source with different name.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when user try to create multiple custom source with different name.");
		   String data_value = api_response.get("data").toString();
		   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
		   test.log(LogStatus.PASS, "Success message is displayed in response.");
		   data_value = data_value.replace("Successfully created custom_sources", "");
		   String id_returned = data_value.replaceAll("[\\s\\[\\]]", "");
		   String[] cs_ids = id_returned.split(",");
		   Assert.assertNotNull(id_returned, "Id is not returned in response.");
		   
		   // Delete the created custom source
		   delete_custom_source(cs_ids[0]);
		   delete_custom_source(cs_ids[1]);
		}  
	}	
	
	@Test(priority=20)
	public void post_custom_source_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_agency_admin_access_token", "To validate whether user is able to create custom sources through POST customsource API with agency admin access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_agency_admin_access_token");
		
		Map<String, Object> confGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id_agency=confGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		Map<String, Object> confGroupHierarchyCompany = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String org_unit_id_company=confGroupHierarchyCompany.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		Map<String, Object> confGroupHierarchyLocation = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String org_unit_id_location=confGroupHierarchyLocation.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String[] group_ids = test_data.get(1).split(",");
		String agency_group = org_unit_id_agency, company_group = org_unit_id_company, location_group = org_unit_id_location;
		for(String group_id:new String[]{agency_group,company_group,location_group}){
			Random r = new Random();
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(group_id));
			json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when agency admin access_token is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using agency admin access_token.");
			   test.log(LogStatus.PASS, "Check API returns success when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using agency admin access_token");
			   Assert.assertNull(api_response.get("err"), "API returns validation message when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using agency admin access_token");
			   test.log(LogStatus.PASS, "Check API returns no validation message when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using agency admin access_token");
			   String data_value = api_response.get("data").toString();
			   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
			   test.log(LogStatus.PASS, "Success message is displayed in response.");
			   String id_returned = data_value.replaceAll("\\D+","");
			   System.out.println("Id: "+id_returned);
			   Assert.assertNotNull(id_returned, "Id is not returned in response.");
			   test.log(LogStatus.PASS, "Id is returned. Returned Id: "+id_returned);
			   
			   // Delete the created custom source
			   delete_custom_source(id_returned);
			}  	
		}
	}
	
//	@Test(priority=21)
	public void post_custom_source_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_company_admin_access_token", "To validate whether user is able to create custom sources through POST customsource API with company admin access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_company_admin_access_token");
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] group_ids = test_data.get(1).split(",");
		String agency_group = group_ids[0], company_group = group_ids[1], location_group = group_ids[2];
		for(String group_id:new String[]{agency_group,company_group,location_group}){
			Random r = new Random();
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(group_id));
			json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when company admin access_token is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(group_id.equals(agency_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when user try to create custom source at agency level using company admin access_token.");
					test.log(LogStatus.PASS, "Check API returns error when user try to create custom source at agency level using company admin access_token.");
					Assert.assertEquals(api_response.get("err").toString(), "error", "Incorrect err value is displayed.");
					String data = api_response.get("data").toString();
					JSONArray validation = new JSONArray();
					validation.add("You are not authorized to create custom sources in group. "+group_id);
					Assert.assertEquals(data, validation.toString(), "Proper validation is not displayed when user try to create custom source at agency level using company admin access_token.");
					test.log(LogStatus.PASS, "Check proper validation is displayed when user try to create custom source at agency level using company admin access_token.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using company admin access_token");
				   Assert.assertNull(api_response.get("err"), "API returns validation message when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using company admin access_token");
				   test.log(LogStatus.PASS, "Check API returns no validation message when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using company admin access_token");
				   String data_value = api_response.get("data").toString();
				   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
				   test.log(LogStatus.PASS, "Success message is displayed in response.");
				   String id_returned = data_value.replaceAll("\\D+","");
				   System.out.println("Id: "+id_returned);
				   Assert.assertNotNull(id_returned, "Id is not returned in response.");
				   test.log(LogStatus.PASS, "Id is returned. Returned Id: "+id_returned);
				   
				   // Delete the created custom source
				   delete_custom_source(id_returned);
			   }
			}  	
		}
	}
	
//	@Test(priority=22)
	public void post_custom_source_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_custom_source_with_location_admin_access_token", "To validate whether user is able to create custom sources through POST customsource API with company admin access_token");
		test.assignCategory("CFA POST /customsources API");
		test_data = HelperClass.readTestData(class_name, "post_custom_source_with_location_admin_access_token");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] group_ids = test_data.get(1).split(",");
		String agency_group = group_ids[0], company_group = group_ids[1], location_group = group_ids[2];
		for(String group_id:new String[]{agency_group,company_group,location_group}){
			Random r = new Random();
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("org_unit_id", Integer.parseInt(group_id));
			json.put("custom_source_name", test_data.get(2)+r.nextInt(50000));
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/customsource", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when company admin access_token is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject api_response = (JSONObject) parser.parse(line);
			   String result_data = api_response.get("result").toString();
			   if(group_id.equals(agency_group) || group_id.equals(company_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using location admin access_token.");
					test.log(LogStatus.PASS, "Check API returns error when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using location admin access_token.");
					Assert.assertEquals(api_response.get("err").toString(), "error", "Incorrect err value is displayed.");
					String data = api_response.get("data").toString();
					JSONArray validation = new JSONArray();
					validation.add("You are not authorized to create custom sources in group. "+group_id);
					Assert.assertEquals(data, validation.toString(), "Proper validation is not displayed when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using location admin access_token.");
					test.log(LogStatus.PASS, "Check proper validation is displayed when user try to create custom source at "+(group_id.equals(agency_group)?"agency":group_id.equals(company_group)?"company":"location")+" level using location admin access_token.");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when user try to create custom source at location level using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when user try to create custom source at location level using location admin access_token");
				   Assert.assertNull(api_response.get("err"), "API returns validation message when user try to create custom source at location level using location admin access_token");
				   test.log(LogStatus.PASS, "Check API returns no validation message when user try to create custom source at location level using location admin access_token");
				   String data_value = api_response.get("data").toString();
				   Assert.assertTrue(data_value.contains("Successfully created custom_sources"), "Success message is not returned in response.");
				   test.log(LogStatus.PASS, "Success message is displayed in response.");
				   String id_returned = data_value.replaceAll("\\D+","");
				   System.out.println("Id: "+id_returned);
				   Assert.assertNotNull(id_returned, "Id is not returned in response.");
				   test.log(LogStatus.PASS, "Id is returned. Returned Id: "+id_returned);
				   
				   // Delete the created custom source
				   delete_custom_source(id_returned);
			   }
			}  	
		}
	}	
	
	public void delete_custom_source(String id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		JSONObject json = new JSONObject();
		JSONArray custom_source = new JSONArray();
		custom_source.add(Integer.parseInt(id));
		json.put("custom_sources", custom_source);
		HttpResponse response = HelperClass.make_delete_request("/v2/customsource", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		   JSONParser parser = new JSONParser();
		   JSONObject api_response = (JSONObject) parser.parse(line);
		   String result_data = api_response.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error while deleting custom source.");
		}   
	}
}
