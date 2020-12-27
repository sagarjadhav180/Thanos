package customsource;

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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class GetCustomSource extends BaseClass{
	public static final String class_name = "GetCustomSource";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@Test(priority=1)
	public void get_custom_source_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_custom_source_without_access_token", "To validate whether user is able to get custom sources through get customsource API without access_token");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_without_access_token");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/customsource?ouId="+groupId, access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=2)
	public void get_custom_source_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_custom_source_with_invalid_access_token", "To validate whether user is able to get custom sources through get customsource API with invalid access_token");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_invalid_access_token");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
	@Test(priority=3)
	public void get_custom_source_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_custom_source_with_expired_access_token", "To validate whether user is able to get custom sources through get customsource API with expired access_token");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_expired_access_token");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void get_custom_source_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_valid_access_token", "To validate whether user is able to get custom sources through get customsource API with valid access_token");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_valid_access_token");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "API returns validation message when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid access_token is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
			   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
			   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
			   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
		   }
		   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
		}   
	}	
	
	@Test(priority=5)
	public void get_custom_source_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_blank_limit", "To validate whether user is able to get custom sources through get customsource API with blank limit value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_blank_limit");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
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
	
	@Test(priority=6)
	public void get_custom_source_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_invalid_limit", "To validate whether user is able to get custom sources through get customsource API with invalid limit value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_invalid_limit");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		for(String limit_val:test_data.get(2).split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			list.add(new BasicNameValuePair("limit", limit_val));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+limit_val+") limit value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+limit_val+") limit value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+limit_val+") limit value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+limit_val+") limit value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (limit): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+limit_val+") limit value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when invalid("+limit_val+") limit value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank limit value is passed");                        
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+limit_val+") limit value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+limit_val+") limit value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+limit_val+") limit value is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+limit_val+") limit value is passed.");
			}   
		}
	}
	
	@Test(priority=7)
	public void get_custom_source_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_negative_limit");
		String groupId = test_data.get(1), limit_value = test_data.get(2);	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when negative("+limit_value+") limit is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative("+limit_value+") limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative("+limit_value+") limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative("+limit_value+") limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when negative("+limit_value+") limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when negative("+limit_value+") limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> error = new ArrayList();
			error.add("paths");
			error.add("/customsource");
			error.add("get");
			error.add("parameters");
			error.add("1");
			Assert.assertEquals(error_path, error, "path is not valid when negative("+limit_value+") limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+limit_value+") limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit_value+" is less than minimum 1", "Invalid message value is returned in response when negative("+limit_value+") limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+limit_value+") limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+limit_value+") limit value is passed.");
		}   
	}
	
	@Test(priority=8)
	public void get_custom_source_with_valid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_valid_limit", "To validate whether user is able to get custom sources through get customsource API with valid limit value");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_valid_limit");

//		String groupId = test_data.get(1);
		String limit_value = test_data.get(2);
		
		Map<String, Object> config_group = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		
		Object groupId =  config_group.get(TestDataYamlConstants.GroupConstants.GROUP_ID);
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId.toString()));

		list.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid("+limit_value+") limit is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid("+limit_value+") limit is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid("+limit_value+") limit is passed.");
		   Assert.assertNull(json.get("err"), "API returns validation message when valid("+limit_value+") limit is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+limit_value+") limit is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   Assert.assertTrue(custom_source_data.size()<=Integer.parseInt(limit_value), "API does not return records according to the passed limit value");
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
			   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
			   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
			   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
		   }
		   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
		}   
	}	
	
	@Test(priority=9)
	public void get_custom_source_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_blank_offset", "To validate whether user is able to get custom sources through get customsource API with blank offset value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_blank_offset");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
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
	
	@Test(priority=10)
	public void get_custom_source_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_invalid_offset", "To validate whether user is able to get custom sources through get customsource API with invalid offset value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_invalid_offset");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		for(String offset_val:test_data.get(3).split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			list.add(new BasicNameValuePair("offset", offset_val));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+offset_val+") offset value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+offset_val+") offset value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+offset_val+") offset value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+offset_val+") offset value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset_val+") offset value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when invalid("+offset_val+") offset value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank offset value is passed");                        
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+offset_val+") offset value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset_val+") offset value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+offset_val+") offset value is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+offset_val+") offset value is passed.");
			}   
		}
	}	
	
	@Test(priority=11)
	public void get_custom_source_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_negative_offset", "To validate whether user is able to get custom sources through get customsource API with negative offset value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_negative_offset");
		String groupId = test_data.get(1), offset_value = test_data.get(3);	
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("offset", offset_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when negative("+offset_value+") offset is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative("+offset_value+") offset value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative("+offset_value+") offset value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative("+offset_value+") offset value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (offset): Value failed JSON Schema validation", "Invalid message value is returned in response when negative("+offset_value+") offset value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when negative("+offset_value+") offset value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> error = new ArrayList();
			error.add("paths");
			error.add("/customsource");
			error.add("get");
			error.add("parameters");
			error.add("2");
			Assert.assertEquals(error_path, error, "path is not valid when negative("+offset_value+") offset value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+offset_value+") offset value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+offset_value+" is less than minimum 0", "Invalid message value is returned in response when negative("+offset_value+") offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+offset_value+") offset value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+offset_value+") offset value is passed.");
		}   
	}
	
	@Test(priority=12)
	public void get_custom_source_with_valid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_valid_offset", "To validate whether user is able to get custom sources through get customsource API with valid offset value");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_valid_offset");

		String groupId = "", offset_value = test_data.get(3), limit_value = test_data.get(2);
		
		Map<String, Object> groupconfig = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		groupId=groupconfig.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		
		// Without offset
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when offset parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		ArrayList<String> custom_source_list_without_offset = new ArrayList<String>();
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid("+offset_value+") offset is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid("+offset_value+") offset is passed.");
		   Assert.assertNull(json.get("err"), "API returns validation message when valid("+offset_value+") offset is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+offset_value+") offset is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   test.log(LogStatus.INFO, "<b style='color:lime'>Custom Source list without offset:</b> "+custom_source_data);
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   custom_source_list_without_offset.add(custom_source.get("custom_source_id").toString());
		   }
		} 
		
		list.add(new BasicNameValuePair("offset", offset_value));
		response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid offset value is passed");
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid("+offset_value+") offset is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when valid("+offset_value+") offset is passed.");
		   Assert.assertNull(json.get("err"), "API returns validation message when valid("+offset_value+") offset is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+offset_value+") offset is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   test.log(LogStatus.INFO, "<b style='color:lime'>Custom Source list with offset:</b> "+custom_source_data);
		   ArrayList<String> custom_source_list_with_offset = new ArrayList<String>();
		   for(int j=0; j<Integer.valueOf(offset_value); j++){
			   for(int i=0; i<custom_source_data.size(); i++){
				   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
				   String custom_source_id = custom_source.get("custom_source_id").toString();
				   Assert.assertNotEquals(custom_source_id, custom_source_list_without_offset.get(j), "Some custom_source are not skipped. like - "+custom_source_id);
				   custom_source_list_with_offset.add(custom_source_id);
			   }
		   }
		   test.log(LogStatus.PASS, "Check offset is skipping defined number of offset records");
		   Assert.assertEquals(custom_source_list_with_offset.get(0), custom_source_list_without_offset.get(Integer.valueOf(offset_value)), "First element of API response with offset is not matching nth element of API response without offset.");
		   test.log(LogStatus.PASS, "Check First element of API response with offset is matching nth element of API response without offset.");
	    } 
	}
	
	@Test(priority=13)
	public void get_custom_source_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_blank_filter", "To validate whether user is able to get custom sources through get customsource API with blank filter value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_blank_filter");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank filter value is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when blank filter value is passed.");
		   test.log(LogStatus.PASS, "Check API returns success when blank filter value is passed.");
		   Assert.assertNull(json.get("err"), "API returns validation message when blank filter value is passed.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when blank filter value is passed.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
			   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
			   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
			   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
			   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
			   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
		   }
		   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
		   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
		}   
	}
	
	@Test(priority=14)
	public void get_custom_source_with_invalid_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_invalid_filter", "To validate whether user is able to get custom sources through get customsource API with invalid filter value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_invalid_filter");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		for(String filter_value:test_data.get(5).split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			list.add(new BasicNameValuePair("filter", filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+filter_value+") filter value is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+filter_value+") filter value is passed.");
			   test.log(LogStatus.PASS, "Check API returns error when invalid("+filter_value+") filter value is passed.");
			   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when invalid("+filter_value+") filter value is passed.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when invalid("+filter_value+") filter value is passed.");
			   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_value, "Proper validation is not displayed when invalid("+filter_value+") filter value is passed.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+filter_value+") filter value is passed.");
			}   
		}
	}
	
	@Test(priority=15)
	public void get_custom_source_with_filter_for_custom_source_id_without_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id_without_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_id without its value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id_without_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("filter", filter_field));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" without its value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field, "Proper validation is not displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" without its value.");
		}
	}
	
	@Test(priority=16)
	public void get_custom_source_with_filter_for_custom_source_id_with_blank_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id_with_blank_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_id with blank value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id_with_blank_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with blank value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("data").toString(), "Please provide valid data.", "Proper validation is not displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with blank value.");
		}
	}
	
	@Test(priority=17)
	public void get_custom_source_with_filter_for_custom_source_id_with_invalid_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id_with_invalgroupId", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_id with invalid value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id_with_invalid_value");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		for(String value:filter_value.split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with invalid("+value+") value.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   Assert.assertEquals(json.get("err").toString(), "Please Provide valid custom_source_id", "Invalid err value is displayed when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   test.log(LogStatus.PASS, "Check API returns proper validation message when filter is applied for "+filter_field+" with invalid("+value+") value.");
			}
		}
	}	
	
	@Test(priority=18)
	public void get_custom_source_with_filter_for_custom_source_id_with_invalid_operator() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id_with_invalid_operator", "To validate whether user is able to get groups through group/list api with invalid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id_with_invalid_operator");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		Map<String, Object> confCustomSourceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String customSourceId = confCustomSourceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		String filter_field = test_data.get(4), filter_value = customSourceId;
		String encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+filter_value, "Proper validation is not displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			}
		}
	}	
	
	@Test(priority=19)
	public void get_custom_source_with_filter_for_custom_source_id_with_non_existing_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id_with_non_existing_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_id with non existing value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id_with_non_existing_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   test.log(LogStatus.PASS, "Check API returns success when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   Assert.assertNull(json.get("err"), "Invalid err value is displayed when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   JSONArray data_array = (JSONArray) json.get("data");
		   Assert.assertTrue(data_array.isEmpty(), "API returns data when non existing custom_source_name is passed.");
		   test.log(LogStatus.PASS, "Check API does not return any data non existing custom_source_name is passed.");
		}
	}	
	
	@Test(priority=20)
	public void get_custom_source_with_filter_for_custom_source_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_id", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_id.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_id");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Map<String, Object> confCustomSourceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String customSourceId = confCustomSourceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID).toString();
		
		String filter_field = test_data.get(4), filter_value = customSourceId;
		String[] operators = {"=",">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			   test.log(LogStatus.PASS, "Check API returns success when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			   Assert.assertNull(json.get("err"), "API returns validation message when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			   JSONArray custom_source_data = (JSONArray) json.get("data");
			   Assert.assertTrue(custom_source_data.size()>=1, "No records are displayed when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			   for(int i=0; i<custom_source_data.size(); i++){
				   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
				   if(operator.equals("=")){
					   Assert.assertEquals(custom_source_data.size(), 1, "API return more than 1 record when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
					   Assert.assertEquals(custom_source.get("custom_source_id").toString(), filter_value, "API returns non matching custom_source_id when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
					   test.log(LogStatus.PASS, "Check API does not return more than 1 record when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
					   test.log(LogStatus.PASS, "API returns same custom_source_id when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
				   }
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.valueOf(custom_source.get("custom_source_id").toString())>=Integer.valueOf(filter_value), "API returns non matching custom_source_id when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
				   else if(operator.equals("<="))
					   Assert.assertTrue(Integer.valueOf(custom_source.get("custom_source_id").toString())<=Integer.valueOf(filter_value), "API returns non matching custom_source_id when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");	   
			   }
			   test.log(LogStatus.PASS, "API returns correct custom_source when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
			}   
		}
	}	
	
	@Test(priority=21)
	public void get_custom_source_with_filter_for_custom_source_name_without_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_name_without_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_name without its value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_name_without_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("filter", filter_field));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" without its value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field, "Proper validation is not displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" without its value.");
		}
	}
	
	@Test(priority=22)
	public void get_custom_source_with_filter_for_custom_source_name_with_blank_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_name_with_blank_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_name with blank value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_name_with_blank_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with blank value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("data").toString(), "Please provide valid data.", "Proper validation is not displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with blank value.");
		}
	}
	
	@Test(priority=23)
	public void get_custom_source_with_filter_for_custom_source_name_with_non_existing_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_name_with_non_existing_value", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_name with non existing value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_name_with_non_existing_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		for(String value:filter_value.split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with non existing("+value+") value.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when filter is applied for "+filter_field+" with non existing("+value+") value.");
			   test.log(LogStatus.PASS, "Check API returns success when filter is applied for "+filter_field+" with non existing("+value+") value.");
			   Assert.assertNull(json.get("err"), "Invalid err value is displayed when filter is applied for "+filter_field+" with non existing("+value+") value.");
			   JSONArray data_array = (JSONArray) json.get("data");
			   Assert.assertTrue(data_array.isEmpty(), "API returns data when non existing custom_source_name is passed.");
			   test.log(LogStatus.PASS, "Check API does not return any data non existing custom_source_name is passed.");
			}
		}
	}	
	
	@Test(priority=24)
	public void get_custom_source_with_filter_for_custom_source_name_with_invalid_operator() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_name_with_invalid_operator", "To validate whether user is able to get groups through group/list api with invalid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_name_with_invalid_operator");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		String encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   if(operator.equals(">=") || operator.equals("<="))
				   Assert.assertEquals(json.get("data").toString(), "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+filter_value, "Proper validation is not displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   else
				   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+filter_value, "Proper validation is not displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			}
		}
	}	
	
	@Test(priority=25)
	public void get_custom_source_with_filter_for_custom_source_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_custom_source_name", "To validate whether user is able to get custom sources through get customsource API with filter for custom_source_name.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_custom_source_name");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		String filter_field = test_data.get(4), filter_value = "";
		
		Map<String, Object> confCustomSourcceHierarchy = yamlReader.readCustomSourceInfo(Constants.GroupHierarchy.AGENCY);
		String custom_source_name=confCustomSourcceHierarchy.get(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_NAME).toString();
		filter_value=custom_source_name;
		
		String operator = "=", encoded_operator = "";
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   test.log(LogStatus.PASS, "Check API returns success when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   Assert.assertNull(json.get("err"), "API returns validation message when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   JSONArray custom_source_data = (JSONArray) json.get("data");
		   Assert.assertTrue(custom_source_data.size()>=1, "No records are displayed when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   for(int i=0; i<custom_source_data.size(); i++){
			   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
			   Assert.assertEquals(custom_source_data.size(), 1, "API return more than 1 record when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
			   Assert.assertEquals(custom_source.get("custom_source_name").toString(), filter_value, "API returns non matching custom_source_name when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
			   test.log(LogStatus.PASS, "Check API does not return more than 1 record when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
			   test.log(LogStatus.PASS, "API returns same custom_source_name when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_name.");
		   }
		   test.log(LogStatus.PASS, "API returns correct custom_source when valid("+filter_value+") filter is passed with "+operator+" operator for custom_source_id.");
		}   
	}	
	
	@Test(priority=26)
	public void get_custom_source_with_filter_for_org_unit_id_without_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id_without_value", "To validate whether user is able to get custom sources through get customsource API with filter for org_unit_id without its value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id_without_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		list.add(new BasicNameValuePair("filter", filter_field));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" without its value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" without its value.");
		   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field, "Proper validation is not displayed when filter is applied for "+filter_field+" without its value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" without its value.");
		}
	}
	
	@Test(priority=27)
	public void get_custom_source_with_filter_for_org_unit_id_with_blank_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id_with_blank_value", "To validate whether user is able to get custom sources through get customsource API with filter for org_unit_id with blank value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id_with_blank_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with blank value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check API returns no validation message when filter is applied for "+filter_field+" with blank value.");
		   Assert.assertEquals(json.get("data").toString(), "Please provide valid data.", "Proper validation is not displayed when filter is applied for "+filter_field+" with blank value.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with blank value.");
		}
	}
	
	@Test(priority=28)
	public void get_custom_source_with_filter_for_org_unit_id_with_invalgroupId() throws Exception{

		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id_with_invalgroupId", "To validate whether user is able to get custom sources through get customsource API with filter for org_unit_id with invalid value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id_with_invalgroupId");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		for(String value:filter_value.split(",")){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with invalid("+value+") value.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   Assert.assertEquals(json.get("err").toString(), "Please Provide valid org_unit_id", "Invalid err value is displayed when filter is applied for "+filter_field+" with invalid("+value+") value.");
			   test.log(LogStatus.PASS, "Check API returns proper validation message when filter is applied for "+filter_field+" with invalid("+value+") value.");
			}
		}
	}	
	
	@Test(priority=29)
	public void get_custom_source_with_filter_for_org_unit_id_with_invalid_operator() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id_with_invalid_operator", "To validate whether user is able to get groups through group/list api with invalid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id_with_invalid_operator");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		String encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check API returns error when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   Assert.assertEquals(json.get("err").toString(), "error", "Invalid err value is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   Assert.assertEquals(json.get("data").toString(), "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+filter_value, "Proper validation is not displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when filter is applied for "+filter_field+" with invalid("+operator+") operator.");
			}
		}
	}	
	
	@Test(priority=30)
	public void get_custom_source_with_filter_for_org_unit_id_with_non_existing_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id_with_non_existing_value", "To validate whether user is able to get custom sources through get customsource API with filter for org_unit_id with non existing value.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id_with_non_existing_value");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String filter_field = test_data.get(4), filter_value = test_data.get(5);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("ouId", groupId));
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   test.log(LogStatus.PASS, "Check API returns success when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   Assert.assertNull(json.get("err"), "Invalid err value is displayed when filter is applied for "+filter_field+" with non existing("+filter_value+") value.");
		   JSONArray data_array = (JSONArray) json.get("data");
		   Assert.assertTrue(data_array.isEmpty(), "API returns data when non existing custom_source_name is passed.");
		   test.log(LogStatus.PASS, "Check API does not return any data non existing custom_source_name is passed.");
		}
	}	
	
	@Test(priority=31)
	public void get_custom_source_with_filter_for_org_unit_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_filter_for_org_unit_id", "To validate whether user is able to get custom sources through get customsource API with filter for org_unit_id.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_filter_for_org_unit_id");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String filter_field = test_data.get(4), filter_value = groupId;

		String[] operators = {"=",">=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", groupId));
			list.add(new BasicNameValuePair("filter", filter_field+encoded_operator+filter_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "success", "API returns error when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			   test.log(LogStatus.PASS, "Check API returns success when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			   Assert.assertNull(json.get("err"), "API returns validation message when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			   test.log(LogStatus.PASS, "Check API returns no validation message when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			   JSONArray custom_source_data = (JSONArray) json.get("data");
			   Assert.assertTrue(custom_source_data.size()>=1, "No records are displayed when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			   for(int i=0; i<custom_source_data.size(); i++){
				   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
				   if(operator.equals("=")){
					   Assert.assertEquals(custom_source.get("org_unit_id").toString(), filter_value, "API returns non matching org_unit_id when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
					   test.log(LogStatus.PASS, "Check API does not return more than 1 record when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
					   test.log(LogStatus.PASS, "API returns same org_unit_id when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
				   }
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.valueOf(custom_source.get("org_unit_id").toString())>=Integer.valueOf(filter_value), "API returns non matching org_unit_id when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
				   else if(operator.equals("<="))
					   Assert.assertTrue(Integer.valueOf(custom_source.get("org_unit_id").toString())<=Integer.valueOf(filter_value), "API returns non matching org_unit_id when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");	   
			   }
			   test.log(LogStatus.PASS, "API returns correct custom_source when valid("+filter_value+") filter is passed with "+operator+" operator for org_unit_id.");
			}   
		}
	}	
	
	@Test(priority=32)
	public void get_custom_source_with_group_id_which_have_no_cs() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_group_id_which_have_no_cs", "To validate whether user is able to get custom sources through get customsource API with group id which does not have any custom_source.");
		test.assignCategory("CFA GET /customsource API");
		test_data = HelperClass.readTestData(class_name, "get_custom_source_with_group_id_which_have_no_cs");

		String groupId = "", username = test_data.get(6), password = test_data.get(7);
		
		Map<String, Object> confGroup = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String top_ou_id = confGroup.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		groupId=DBGroupUtils.getOrgUnitIdWithoutCustomSource(top_ou_id);
		
		String operator="=";
		String encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		
//		String access_token = HelperClass.get_oauth_token(username,password);
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		list.add(new BasicNameValuePair("ouId", top_ou_id));
		list.add(new BasicNameValuePair("filter", "org_unit_id"+encoded_operator+groupId));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when group id is passed which does not have any custom source.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns success when group id is passed which does not have any custom source.");
		   test.log(LogStatus.PASS, "Check API returns error when group id is passed which does not have any custom source.");
		  
		   try {
			   Assert.assertEquals(json.get("err").toString(), "null");   
		   }
		   catch(Exception e) {
			   test.log(LogStatus.PASS, "Check API returns proper validation message when group id is passed which does not have any custom source.");			   
		   }
		   
		   JSONArray data = (JSONArray) json.get("data");
		   Assert.assertTrue(data.size()==0, "Invalid err value is displayed when group id is passed which does not have any custom source.");
		   
		}
	}
	
	@Test(priority=33)
	public void get_custom_source_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_agency_admin_access_token", "To validate whether user is able to get custom sources through get customsource API with agency admin access_token");
		test.assignCategory("CFA GET /customsource API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		for(String id: new String[] {agency_group, company_group, location_group, other_billing_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			
			list.add(new BasicNameValuePair("ouId", id));
			list.add(new BasicNameValuePair("limit", "1000"));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"location")+" level group id is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when other billing group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns error when other billing group id is passed using agency admin access_token.");
				   Assert.assertEquals(json.get("err").toString(), "error", "Incorrect error data is displayed in response.");
				   Assert.assertEquals(json.get("data").toString(), "You don't have permission to access this group.", "Proper validation is not displayed when other billing group_id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check proper validation is displayed when other billing group_id is passed using agency admin access_token");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   Assert.assertNull(json.get("err"), "API returns validation message when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns no validation message when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   JSONArray custom_source_data = (JSONArray) json.get("data");
				   Boolean is_agency_data_present = false, is_company_data_present = false, is_location_data_present = false;
				   for(int i=0; i<custom_source_data.size(); i++){
					   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
					   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
					   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
					   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
					   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
					   if(custom_source.get("org_unit_id").toString().equals(agency_group))
						   is_agency_data_present = true;
					   else if(custom_source.get("org_unit_id").toString().equals(company_group))
						   is_company_data_present = true;
					   else
						   is_location_data_present = true;
				   }
				   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
				   
				   Assert.assertTrue(is_agency_data_present, "Agency level custom_source is not present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   Assert.assertTrue(is_company_data_present, "Company level custom_source is not present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   Assert.assertTrue(is_location_data_present, "Location level custom_source is not present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   
				   test.log(LogStatus.PASS, "Check Agency level custom_source is present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check Company level custom_source is present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check Location level custom_source is present when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":id.equals(location_group)?"location":"other_billing")+" level group id is passed using agency admin access_token.");
			   }
			}   
		}
	}		
	
//	@Test(priority=34)
	public void get_custom_source_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_company_admin_access_token", "To validate whether user is able to get custom sources through get customsource API with company admin access_token");
		test.assignCategory("CFA GET /customsource API");
		
		// Get access_token of company admin user
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
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		for(String id: new String[] {agency_group, company_group, location_group, other_billing_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(agency_group) || id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agency_group)?"agency":"other billing")+" group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns error when "+(id.equals(agency_group)?"agency":"other billing")+" group id is passed using company admin access_token.");
				   Assert.assertEquals(json.get("err").toString(), "error", "Incorrect error data is displayed in response.");
				   Assert.assertEquals(json.get("data").toString(), "You don't have permission to access this group.", "Proper validation is not displayed when "+(id.equals(agency_group)?"agency":"other billing")+" group_id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check proper validation is displayed when "+(id.equals(agency_group)?"agency":"other billing")+" group_id is passed using company admin access_token");
			   }
			   else{
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   Assert.assertNull(json.get("err"), "API returns validation message when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns no validation message when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   JSONArray custom_source_data = (JSONArray) json.get("data");
				   Boolean is_agency_data_present = false, is_company_data_present = false, is_location_data_present = false;
				   for(int i=0; i<custom_source_data.size(); i++){
					   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
					   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
					   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
					   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
					   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
					   if(custom_source.get("org_unit_id").toString().equals(agency_group))
						   is_agency_data_present = true;
					   else if(custom_source.get("org_unit_id").toString().equals(company_group))
						   is_company_data_present = true;
					   else
						   is_location_data_present = true;
				   }
				   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
				   
				   Assert.assertTrue(is_agency_data_present, "Agency level custom_source is not present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   Assert.assertTrue(is_company_data_present, "Company level custom_source is not present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   Assert.assertTrue(is_location_data_present, "Location level custom_source is not present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   
				   test.log(LogStatus.PASS, "Check Agency level custom_source is present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check Company level custom_source is present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check Location level custom_source is present when "+(id.equals(company_group)?"company":"location")+" level group id is passed using company admin access_token.");
			   }
			}   
		}
	}
	
//	@Test(priority=35)
	public void get_custom_source_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_custom_source_with_location_admin_access_token", "To validate whether user is able to get custom sources through get customsource API with location admin access_token");

		test.assignCategory("CFA GET /customsource API");
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
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		for(String id: new String[] {agency_group, company_group, location_group, other_billing_group}){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ouId", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/customsource", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"location")+" level group id is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when location level group id is passed using location admin access_token.");
				   Assert.assertNull(json.get("err"), "API returns validation message when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns no validation message when location level group id is passed using location admin access_token.");
				   JSONArray custom_source_data = (JSONArray) json.get("data");
				   Boolean is_agency_data_present = false, is_company_data_present = false, is_location_data_present = false;
				   for(int i=0; i<custom_source_data.size(); i++){
					   JSONObject custom_source = (JSONObject) custom_source_data.get(i);
					   Assert.assertTrue(custom_source.containsKey("custom_source_id"), "API response does not contains custom_source_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_id"), "custom_source_id");
					   Assert.assertEquals(custom_source.get("custom_source_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("custom_source_name"), "API response does not contains custom_source_name field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("custom_source_name"), "custom_source_name");
					   Assert.assertEquals(custom_source.get("custom_source_name").getClass().getName(), "java.lang.String", "Incorrect data_type is displayed for custom_source_name field.");
					   Assert.assertTrue(custom_source.containsKey("org_unit_id"), "API response does not contains org_unit_id field for "+custom_source);
					   HelperClass.multiple_assertnotEquals(custom_source.get("org_unit_id"), "org_unit_id");
					   Assert.assertEquals(custom_source.get("org_unit_id").getClass().getName(), "java.lang.Long", "Incorrect data_type is displayed for org_unit_id field.");
					   if(custom_source.get("org_unit_id").toString().equals(agency_group))
						   is_agency_data_present = true;
					   else if(custom_source.get("org_unit_id").toString().equals(company_group))
						   is_company_data_present = true;
					   else
						   is_location_data_present = true;
				   }
				   test.log(LogStatus.PASS, "To validate whether custom_source_name field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether custom_source_id field is displayed in API response");
				   test.log(LogStatus.PASS, "To validate whether org_unit_id field is displayed in API response");
				   
				   Assert.assertTrue(is_agency_data_present, "Agency level custom_source is not present when location level group id is passed using location admin access_token.");
				   Assert.assertTrue(is_company_data_present, "Company level custom_source is not present when location level group id is passed using location admin access_token.");
				   Assert.assertTrue(is_location_data_present, "Location level custom_source is not present when location level group id is passed using location admin access_token.");
				   
				   test.log(LogStatus.PASS, "Check Agency level custom_source is present when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check Company level custom_source is present when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check Location level custom_source is present when location level group id is passed using location admin access_token.");
			   }
			   else{
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"other billing")+" group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns error when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"other billing")+" group id is passed using location admin access_token.");
				   Assert.assertEquals(json.get("err").toString(), "error", "Incorrect error data is displayed in response.");
				   Assert.assertEquals(json.get("data").toString(), "You don't have permission to access this group.", "Proper validation is not displayed when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"other billing")+" group_id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check proper validation is displayed when "+(id.equals(agency_group)?"agency":id.equals(company_group)?"company":"other billing")+" group_id is passed using location admin access_token");
			   }
			}   
		}
	}	
}
