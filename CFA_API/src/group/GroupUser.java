package group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
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
public class GroupUser extends BaseClass{
	
	String class_name = "GroupUser";
	ArrayList<String> test_data;

	TestDataYamlReader yamlReader = new TestDataYamlReader();

	@Test(priority=0)
	public void group_user_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_user_without_access_token", "To validate whether user is able to get groups through group/user api without access_token");
		test.assignCategory("CFA GET /group/user API");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/group/user", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status message when access_token is not passed");
	}
	
	@Test(priority=1)
	public void group_user_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_user_with_invalid_access_token", "To validate whether user is able to get groups through group/user api with invalid access_token");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void group_user_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_user_with_expired_access_token", "To validate whether user is able to get groups through group/user api with expired access_token");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void group_user_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_access_token", "To validate whether user is able to get groups through group/user api with valid access_token");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, list);
		String agencyGroup = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when valid access_token is passed.");
		   test.log(LogStatus.PASS, "err data is null when valid access_token is passed.");
		   JSONArray json_array = (JSONArray) json.get("data");
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);
			   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("billing_id"), "ct_user_id field is not present for group object: "+group);
			   
			   HelperClass.multiple_assertnotEquals(group.get("group_id"), "group_id");
			   HelperClass.multiple_assertnotEquals(group.get("group_name"), "group_name");
			   if(!group.get("group_id").toString().equals(agencyGroup))
				   HelperClass.multiple_assertnotEquals(group.get("group_parent_id"), "group_parent_id");
			   HelperClass.multiple_assertnotEquals(group.get("top_group_id"), "top_group_id");
			   HelperClass.multiple_assertnotEquals(group.get("group_status"), "group_status");
			   HelperClass.multiple_assertnotEquals(group.get("billing_id"), "billing_id");
			   
			   Assert.assertEquals(group.get("group_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("group_name").getClass().getName(),"java.lang.String");
			   if(!group.get("group_id").toString().equals(agencyGroup))
				   Assert.assertEquals(group.get("group_parent_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("top_group_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("group_status").getClass().getName(),"java.lang.String");
			   Assert.assertEquals(group.get("billing_id").getClass().getName(),"java.lang.Long");
			   
			   Assert.assertTrue(group.containsKey("users"), "Group does not contain users fields in group/user api.");
			   HelperClass.multiple_assertnotEquals(group.get("users"), "users");
			   JSONArray group_users= (JSONArray)group.get("users");
			   for(int j=0; j<group_users.size(); j++){
				  JSONObject group_user = (JSONObject) group_users.get(j);
				  Assert.assertTrue(group_user.containsKey("ct_user_id"), "ct_user_id field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("user_ext_id"), "user_ext_id field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("user_email"), "user_email field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("first_name"), "first_name field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("last_name"), "last_name field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("user_title"), "user_title field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("role_id"), "role_id field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("user_status"), "user_status field is not present under group users.");
				  Assert.assertTrue(group_user.containsKey("group_id"), "group_id field is not present under group users.");
			   	  HelperClass.multiple_assertnotEquals(group_user.get("ct_user_id"), "ct_user_id");
				  HelperClass.multiple_assertnotEquals(group_user.get("first_name"), "first_name");
				  HelperClass.multiple_assertnotEquals(group_user.get("last_name"), "last_name");
				  HelperClass.multiple_assertnotEquals(group_user.get("role_id"), "role_id");
				  HelperClass.multiple_assertnotEquals(group_user.get("user_status"), "user_status");
				  HelperClass.multiple_assertnotEquals(group_user.get("group_id"), "group_id");  
			   }
		   }
		   test.log(LogStatus.PASS, "To validate whether group_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_ext_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_name is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_parent_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether top_group_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_status is present in response.");
		   test.log(LogStatus.PASS, "To validate whether billing_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_name is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_parent_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether top_group_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_status is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether billing_id is not null or blank in response.");

		   test.log(LogStatus.PASS, "To validate whether ct_user_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether user_ext_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether first_name is present in response.");
		   test.log(LogStatus.PASS, "To validate whether last_name is present in response.");
		   test.log(LogStatus.PASS, "To validate whether user_title is present in response.");
		   test.log(LogStatus.PASS, "To validate whether role_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether user_status is present in response.");
		   test.log(LogStatus.PASS, "To validate data type of fields in response.");
		}
	}	
	
	@Test(priority=4)
	public void group_user_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_limit", "To validate whether user is able to get groups through group/user api with blank limit value.");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when blank limit value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
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
//			Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank limit value is passed.");
		}
	}	
	
	@Test(priority=5)
	public void group_user_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_limit", "To validate whether user is able to get groups through group/user api with invalid limit value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_limit");
		String[] invalid_limit = test_data.get(1).split(",");
		for(String limit:invalid_limit){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("limit", limit));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status message when invalid("+limit+") limit value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
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
//				Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//				String sub_error_description = sub_error_data.get("description").toString();
//				Assert.assertEquals(sub_error_description, "Maximum number of records to return");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+limit+") limit value is passed.");
			}
		}
	}	
	
	@Test(priority=6)
	public void group_user_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_negative_limit", "To validate whether user is able to get groups through group/user api with negative limit value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_negative_limit");
		System.out.println(test_data.get(1));
		int limit_value = Integer.parseInt(test_data.get(1));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", Integer.toString(limit_value)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when negative("+limit_value+") limit value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
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
			ArrayList<String> error = new ArrayList<String>();
			error.add("paths");
			error.add("/group/user");
			error.add("get");
			error.add("parameters");
			error.add("0");
			Assert.assertEquals(error_path, error, "path is not valid when negative("+limit_value+") limit value is passed");                        
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+limit_value+") limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+Integer.toString(limit_value)+" is less than minimum 1", "Invalid message value is returned in response when negative("+limit_value+") limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+limit_value+") limit value is passed");
//			Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+limit_value+") offset value is passed.");
		}
	}		
	
	@Test(priority=6)
	public void group_user_with_valid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_limit", "To validate whether user is able to get groups through group/user api with valid limit value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_limit");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String limit = test_data.get(1);
		params.add(new BasicNameValuePair("limit", limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when valid limit value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid limit value is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid limit value is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when valid limit value is passed.");
		   test.log(LogStatus.PASS, "err data is null when valid limit value is passed.");
		   JSONArray json_array = (JSONArray) json.get("data");
		   Assert.assertTrue(json_array.size()<= Integer.parseInt(limit), "API is not returning number of records when applied limit.");
		   test.log(LogStatus.PASS, "API is returning number of records defined in limit.");
		}
	}
	
	@Test(priority=7)
	public void group_user_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_offset", "To validate whether user is able to get groups through group/user api with blank offset value.");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String offset = "";
		params.add(new BasicNameValuePair("offset", offset));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when blank offset value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
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
//			Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank offset value is passed.");
		}
	}
	
	@Test(priority=8)
	public void group_user_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_offset", "To validate whether user is able to get groups through group/user api with invalid offset value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_offset");
		String[] offset = test_data.get(2).split(",");
		for(String offset_value: offset){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("offset", offset_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status message when invalid("+offset_value+") offset value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+offset_value+") offset value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+offset_value+") offset value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid("+offset_value+") offset value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset_value+") offset value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when invalid("+offset_value+") offset value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid("+offset_value+") offset value is passed");                        
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+offset_value+") offset value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+offset_value+") offset value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+offset_value+") offset value is passed");
//				Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//				String sub_error_description = sub_error_data.get("description").toString();
//				Assert.assertEquals(sub_error_description, "Maximum number of records to return");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+offset_value+") offset value is passed.");
			}
		}
	}
	
	@Test(priority=9)
	public void group_user_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_negative_offset", "To validate whether user is able to get groups through group/user api with negative offset value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_negative_offset");
		int offset_value = Integer.parseInt(test_data.get(2));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("offset", Integer.toString(offset_value)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when negative("+offset_value+") offset value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
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
			error.add("/group/user");
			error.add("get");
			error.add("parameters");
			error.add("1");
			Assert.assertEquals(error_path, error, "path is not valid when negative("+offset_value+") offset value is passed");                        
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative("+offset_value+") offset value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+Integer.toString(offset_value)+" is less than minimum 0", "Invalid message value is returned in response when negative("+offset_value+") offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative("+offset_value+") offset value is passed");
//			Assert.assertNotEquals(sub_error_data.get("description"), null, "Description field is not present in response.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+offset_value+") offset value is passed.");
		}
	}	
	
	@Test(priority=9)
	public void group_user_with_0_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api without offset
		test = extent.startTest("group_user_with_0_offset", "To validate whether user is able to get groups through group/user api with 0 offset value");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_0_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/user api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		JSONArray group_data_array_without_offset = new JSONArray(), group_data_array_with_offset = new JSONArray();
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   group_data_array_without_offset = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when offset value is not passed.");
		   test.log(LogStatus.PASS, "API returns success when offset value is not passed.");
		}
		
		// Execute group/user api with 0 offset value
		nvps.add(new BasicNameValuePair("offset", test_data.get(2)));
		response = HelperClass.make_get_request("/v2/group/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/user api method with 0 offset value");
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   group_data_array_with_offset = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when 0 offset value is passed.");
		   test.log(LogStatus.PASS, "API returns success when 0 offset value is passed.");
		}
		Assert.assertEquals(group_data_array_without_offset, group_data_array_with_offset);
	}	
	
	@Test(priority=10)
	public void group_user_with_valid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_offset", "To validate whether user is able to get groups through group/user api with valid offset value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_offset");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String offset = test_data.get(2);
		params.add(new BasicNameValuePair("offset", offset));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when valid offset value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   String result_data = json.get("result").toString();
		   
		   Assert.assertEquals(result_data, "success", "API returns error when valid offset value is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid offset value is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when valid offset value is passed.");
		   test.log(LogStatus.PASS, "err data is null when valid offset value is passed.");
		}
	}	
	
	@Test(priority=11)
	public void group_user_with_valid_limit_and_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute group/user api without offset value
		test = extent.startTest("group_user_with_valid_limit_and_offset", "To validate whether user is able to get groups through group/user api with valid limit and offset value");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_limit_and_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/user api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = ""; String[] group_id = new String[11];
		while ((line = rd.readLine()) != null) {
		   
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "API returns success when limit is passed and offset is not passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "err is null when limit is passed and offset is not passed.");
		   // Get the 10th group data from the group user
		   for(int i=0;i<array.size();i++){
			   JSONObject nth_camp_data =(JSONObject) array.get(i);
			   String nth_group_id = nth_camp_data.get("group_id").toString();
			   group_id[i] = nth_group_id;
		   }  
		}
	   // Execute group/user api method with offset value
	   nvps.add(new BasicNameValuePair("offset", test_data.get(2)));
	   response = HelperClass.make_get_request("/v2/group/user", access_token, nvps);
	   test.log(LogStatus.INFO, "Execute group/user api method with offset value");
	   rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	   line = "";
	   while ((line = rd.readLine()) != null) {
		   
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when valid limit and offset value are entered.");
		   test.log(LogStatus.PASS, "API returns success when valid limit and offset value are entered.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when valid limit and offset value are entered.");
		   test.log(LogStatus.PASS, "err is null when valid limit and offset value are entered.");
//		   // Get the 10th campaign data from the campaign user
//		   String[] group_id_temp = new String[10];
//		   group_id_temp = Arrays.copyOfRange(group_id,0,10);
//		   boolean element_exist = false;
//		   for(String group_id_val:group_id_temp){
//			   for(int n = 0; n < array.size(); n++)
//			   {
//			       JSONObject object = (JSONObject)array.get(n);
//			       if(object.get("group_id").toString().equals(group_id[10])){
//			    	   element_exist = true;
//			       }
//	   			   Assert.assertNotEquals(object.get("group_id").toString(), group_id_val, "group is not skipped when valid limit and offset value are used");
//			   }
//		   }
//		   Assert.assertTrue(element_exist);
	   }	
	}	
	
	@Test(priority=12)
	public void group_user_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_filter", "To validate whether user is able to get groups through group/user api with blank filter value.");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when blank filter value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when blank filter value is passed.");
		   test.log(LogStatus.PASS, "API returns success when blank filter value is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when blank filter value is passed.");
		   test.log(LogStatus.PASS, "err data is null when blank filter value is passed.");
		}
	}
	
	@Test(priority=13)
	public void group_user_with_invalid_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_filter", "To validate whether user is able to get groups through group/user api with invalid filter value.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_filter");
		String[] filter_values = test_data.get(3).split(",");
		for(String filter:filter_values){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("filter", filter));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status message when invalid filter value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+filter+") filter value is passed");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+filter+") filter value is passed ");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter+"", "Proper validation message is not displayed when invalid("+filter+") filter value is passed");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+filter+") filter value is passed ");
			}
		}
	}
	
	@Test(priority=14)
	public void group_user_with_invalid_filter_field() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_filter", "To validate whether user is able to get groups through group/user api with invalid filter field is passed.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_filter_field");
		String filter = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = "";
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter+encoded_operator+"abc"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when invalid filter field is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+filter+") filter field is passed");
			test.log(LogStatus.PASS, "Check api is returning error when invalid("+filter+") filter field is passed ");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Invalid column specified in filter on rule 1 : test=abc", "Proper validation message is not displayed when invalid("+filter+") filter field is passed");
			test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+filter+") filter field is passed ");
		}
	}
	
	@Test(priority=15)
	public void group_user_with_invalid_filter_format() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_filter_format", "To validate whether user is able to get groups through group/user api with invalid filter format is passed.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_filter_format");
		String filter = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("filter", filter));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when invalid filter format is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when invalid filter format is passed");
			test.log(LogStatus.PASS, "Check api is returning error when invalid filter format is passed ");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter+"", "Proper validation message is not displayed when invalid filter format is passed");
			test.log(LogStatus.PASS, "Proper validation message is displayed when invalid filter format is passed ");
		}
	}	
	
	@Test(priority=16) // Currently this is not working
	public void group_user_with_blank_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_group_id_in_filter", "To validate whether user is able to get groups through group/user api with blank group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_blank_group_id_in_filter");
		String filter_field = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank group_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_id is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_id is passed in filter");
		}
	}
	
//	@Test(priority=17) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_group_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_group_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_ids = test_data.get(4).split(",");
		for(String group_id:group_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid group_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid group_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "No record found", "Proper validation message is not displayed when invalid group_id is passed in filter. <b style='color:red'>Defect Reported: CT-17158</b>");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=18)
	public void group_user_with_invalid_operator_for_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_group_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_group_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid operator for group_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid operator for group_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"8", "Proper validation message is not displayed when invalid operator for group_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid operator for group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=19)
	public void group_user_with_nonexisting_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_group_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing group_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_group_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+555555));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing group_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing group_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing group_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing group_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing group_id is passed in filter");
		}
	}	
	
	@Test(priority=20)
	public void group_user_with_valid_operator_for_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_group_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_ID, encoded_operator = "";
		int ouid = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+ouid));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for group_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for group_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when invalid operator("+operator+") is passed for group_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when invalid operator("+operator+") is passed for group_id field in filter.");
				JSONArray group_user = (JSONArray) json.get("data");
				for(int i=0; i<group_user.size(); i++){
					JSONObject group = (JSONObject) group_user.get(i);
					int group_id = Integer.parseInt(group.get("group_id").toString());
					boolean is_mismatched_group_exist = false;
					if(operator.equals(">=")){
						if(group_id<ouid){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("<=")){
						if(group_id>ouid){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("=")){
						if(group_id>ouid || group_id<ouid){
							is_mismatched_group_exist = true;
						}
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
					test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
				}
			}
		}
	}	
	
	@Test(priority=21)
	public void group_user_with_blank_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_group_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with blank group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_blank_group_ext_id_in_filter");
		String filter_field = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_ext_id is passed in filter");
			String err_data = json.get("err").toString();
			Assert.assertEquals(err_data, "Please provide valid data.", "Proper error message is not displayed when blank group_ext_id value is passed.");
			test.log(LogStatus.PASS, "Proper error message is displayed when blank group_ext_id value is passed.");
		}
	}
	
	@Test(priority=22)
	public void group_user_with_null_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_null_group_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with null group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_ext_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when null group_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when null group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when null group_ext_id is passed in filter");
			Object err_data = json.get("err");
			Assert.assertEquals(err_data, null, "err value is not null when null group_ext_id value is passed.");
			test.log(LogStatus.PASS, "err value is not null when null group_ext_id value is passed.");
			JSONArray group_data = (JSONArray) json.get("data");
			for(int i=0;i<group_data.size();i++){
				JSONObject group =(JSONObject) group_data.get(i);
				Object group_ext_id = group.get("group_ext_id");
				Assert.assertNull(group_ext_id, "group_ext_id is not null in response when null group_ext_id value is passed.");
			}
		}
	}	
	
	@Test(priority=23)
	public void group_user_with_invalid_operator_for_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_group_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String external_id = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		String filter_field = "group_ext_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field + encoded_operator + external_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+operator+") operator for group_ext_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
					test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for group_ext_id is passed in filter");
					String error_des = json.get("err").toString();
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+external_id, "Proper validation message is not displayed when invalid("+operator+") operator for group_ext_id is passed in filter");
					test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for group_ext_id is passed in filter");
			}
		}
	}	
	
	@Test(priority=24)
	public void group_user_with_nonexisting_group_ext_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_group_ext_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing group_ext_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_group_ext_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing group_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing group_ext_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing group_ext_id is passed in filter");
		}
	}
	
	@Test(priority=25)
	public void group_user_with_valid_operator_for_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_group_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_ext_id", encoded_operator = "";
		String operator = "=",
			external_id = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field + encoded_operator + external_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator for group_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for group_ext_id field in filter.");
			test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for group_ext_id field in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when invalid operator("+operator+") is passed for group_ext_id field in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when invalid operator("+operator+") is passed for group_ext_id field in filter.");
			JSONArray group_user = (JSONArray) json.get("data");
			for(int i=0; i<group_user.size(); i++){
				JSONObject group = (JSONObject) group_user.get(i);
				String group_ext_id = group.get("group_ext_id").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_ext_id.equals(external_id)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
			}
		}
	}	
	
	@Test(priority=26)
	public void group_user_with_blank_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_group_name_in_filter", "To validate whether user is able to get groups through group/user api with blank group_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_name";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank group_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_name is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_name is passed in filter");
		}
	}
	
	@Test(priority=27)
	public void group_user_with_invalid_operator_for_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_group_name_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for group_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_name", encoded_operator = "",
			groupName = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_NAME).toString();;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+groupName));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+operator+") operator for group_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for group_name is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for group_name is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field + operator + groupName, "Proper validation message is not displayed when invalid("+operator+") operator for group_name is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+groupName, "Proper validation message is not displayed when invalid("+operator+") operator for group_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for group_name is passed in filter");
			}
		}
	}	
	
	@Test(priority=28)
	public void group_user_with_nonexisting_group_name_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_group_name_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing group_name value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_group_name_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing group_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing group_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing group_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing group_name is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing group_name is passed in filter");
		}
	}
	
	@Test(priority=29)
	public void group_user_with_valid_operator_for_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_group_name_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for group_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_NAME, encoded_operator = "", operator = "=";;
		String agency_group = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_NAME).toString(),
			company_group = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_NAME).toString(),
			location_group = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION)
				.get(TestDataYamlConstants.GroupConstants.GROUP_NAME).toString();
		String [] group_names = {agency_group, company_group, location_group};
		for(String group_name: group_names){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for group_ext_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for group_name field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for group_name field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when invalid operator("+operator+") is passed for group_name field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when invalid operator("+operator+") is passed for group_name field in filter.");
				JSONArray group_user = (JSONArray) json.get("data");
				for(int i=0; i<group_user.size(); i++){
					JSONObject group = (JSONObject) group_user.get(i);
					String group_name_value = group.get("group_name").toString();
					boolean is_mismatched_group_exist = false;
					if(operator.equals("=")){
						if(!group_name_value.equals(group_name)){
							is_mismatched_group_exist = true;
						}
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
					test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
				}
			}
		}
	}	
	
	@Test(priority=30) // This is not working right now
	public void group_user_with_blank_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_group_parent_id_in_filter", "To validate whether user is able to get groups through group/user api with blank group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_parent_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_parent_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank group_parent_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_parent_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_parent_id is passed in filter. Defect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_parent_id is passed in filter");
		}
	}
	
//	@Test(priority=31) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_group_parent_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_group_parent_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_group_parent_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_parent_ids = test_data.get(3).split(",");
		for(String group_parent_id:group_parent_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_parent_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid group_parent_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid group_parent_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid group_parent_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "No records found", "Proper validation message is not displayed when invalid group_parent_id is passed in filter. Defect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid group_parent_id is passed in filter");
			}
		}
	}
	
	@Test(priority=32)
	public void group_user_with_invalid_operator_for_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_group_parent_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_parent_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for group_parent_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid operator for group_parent_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid operator for group_parent_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"8", "Proper validation message is not displayed when invalid operator for group_parent_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid operator for group_parent_id is passed in filter");
			}
		}
	}
	
	@Test(priority=33)
	public void group_user_with_nonexisting_group_parent_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_group_parent_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing group_parent_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_group_parent_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing group_parent_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing group_parent_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing group_parent_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing group_parent_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing group_parent_id is passed in filter");
		}
	}	
	
	@Test(priority=34)
	public void group_user_with_valid_operator_for_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_group_parent_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, encoded_operator = "";
		int parent_ouid = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+parent_ouid));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for group_parent_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for group_parent_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for group_parent_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for group_parent_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for group_parent_id field in filter.");
				JSONArray group_user = (JSONArray) json.get("data");
				for(int i=0; i<group_user.size(); i++){
					JSONObject group = (JSONObject) group_user.get(i);
					int group_parent_id = Integer.parseInt(group.get("group_parent_id").toString());
					boolean is_mismatched_group_exist = false;
					if(operator.equals(">=")){
						if(group_parent_id<parent_ouid){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("<=")){
						if(group_parent_id>parent_ouid){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("=")){
						if(group_parent_id>parent_ouid || group_parent_id<parent_ouid){
							is_mismatched_group_exist = true;
						}
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
					test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
				}
			}
		}
	}
	
	@Test(priority=35) // This is not working right now
	public void group_user_with_blank_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_top_group_id_in_filter", "To validate whether user is able to get groups through group/user api with blank top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "top_group_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank top_group_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank top_group_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank top_group_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank top_group_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank top_group_id is passed in filter");
		}
	}
	
//	@Test(priority=36) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_top_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_top_group_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_top_group_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] top_group_ids = test_data.get(3).split(",");
		for(String top_group_id:top_group_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+top_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid top_group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid top_group_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid top_group_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "No record found", "Proper validation message is not displayed when invalid top_group_id is passed in filter. <b style='color:red'>Defect Reported: CT-17158</b>");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid top_group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=37)
	public void group_user_with_invalid_operator_for_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_top_group_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "top_group_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for top_group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid operator for top_group_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid operator for top_group_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"8", "Proper validation message is not displayed when invalid operator for top_group_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid operator for top_group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=38)
	public void group_user_with_nonexisting_top_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_top_group_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing top_group_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_top_group_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing top_group_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing top_group_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing top_group_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing top_group_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing top_group_id is passed in filter");
		}
	}	
	
	@Test(priority=39)
	public void group_user_with_valid_operator_for_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_top_group_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.TOP_GROUP_ID, encoded_operator = "";
		int top_group_id = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.GroupConstants.TOP_GROUP_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+top_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for top_group_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for top_group_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for top_group_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for top_group_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for top_group_id field in filter.");
				JSONArray group_user = (JSONArray) json.get("data");
				for(int i=0; i<group_user.size(); i++){
					JSONObject group = (JSONObject) group_user.get(i);
					int top_group_id_in_response = Integer.parseInt(group.get("top_group_id").toString());
					boolean is_mismatched_group_exist = false;
					if(top_group_id_in_response!=top_group_id){
						is_mismatched_group_exist = true;
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter. failed for group: "+group);
				}
				test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=40) // This is not working right now
	public void group_user_with_blank_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_billing_id_in_filter", "To validate whether user is able to get groups through group/user api with blank billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "billing_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank billing_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank billing_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank billing_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank billing_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank billing_id is passed in filter");
		}
	}
	
//	@Test(priority=41) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_billing_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_billing_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_billing_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] billing_ids = test_data.get(3).split(",");
		for(String billing_id:billing_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+billing_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid billing_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid billing_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid billing_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid billing_id is passed in filter. <b style='color:red'>Defect Reported: CT-17158</b>");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=42)
	public void group_user_with_invalid_operator_for_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_billing_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "billing_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for billing_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for billing_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for billing_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"8", "Proper validation message is not displayed when invalid("+operator+") operator for billing_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=43)
	public void group_user_with_nonexisting_billing_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_billing_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing billing_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_billing_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing billing_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing billing_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing billing_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing billing_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing billing_id is passed in filter");
		}
	}	
	
	@Test(priority=44)
	public void group_user_with_valid_operator_for_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_billing_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.BILLING_ID, encoded_operator = "";
		int billing_id = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.GroupConstants.BILLING_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+billing_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for billing_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for billing_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for billing_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for billing_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for billing_id field in filter.");
				JSONArray group_user = (JSONArray) json.get("data");
				for(int i=0; i<group_user.size(); i++){
					JSONObject group = (JSONObject) group_user.get(i);
					int billing_id_in_response = Integer.parseInt(group.get("top_group_id").toString());
					boolean is_mismatched_group_exist = false;
					if(billing_id_in_response!=billing_id){
						is_mismatched_group_exist = true;
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter. Failed for group: "+group);
				}
				test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
			}
		}
	}	
	
	@Test(priority=45) // This is not working right now
	public void group_user_with_blank_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_group_status_in_filter", "To validate whether user is able to get groups through group/user api with blank group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_status";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank group_status is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_status is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_status is passed in filter. Defect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_status is passed in filter");
		}
	}	
	
	@Test(priority=46)
	public void group_user_with_invalid_operator_for_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_group_status_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "group_status", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+"active"));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for group_status is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for group_status is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for group_status is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+"active", "Proper validation message is not displayed when invalid("+operator+") operator for group_status is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"active", "Proper validation message is not displayed when invalid("+operator+") operator for group_status is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for group_status is passed in filter");
			}
		}
	}
	
//	@Test(priority=47) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_group_status_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_group_status_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_group_status_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_status = test_data.get(4).split(",");
		for(String grp_status:group_status){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid group_status is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+grp_status+") group_status is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+grp_status+") group_status is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid("+grp_status+") group_status is passed in filter. <b style='color:red'>Defect Reported: CT-17158</b>");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+grp_status+") group_status is passed in filter");
			}
		}
	}	
	
	@Test(priority=48)
	public void group_user_with_valid_operator_for_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_group_status_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_operator_for_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator for group_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for group_status field in filter.");
			test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for group_status field in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for group_status field in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for group_status field in filter.");
			JSONArray group_user = (JSONArray) json.get("data");
			for(int i=0; i<group_user.size(); i++){
				JSONObject group = (JSONObject) group_user.get(i);
				String group_status_value = group.get("group_status").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_status_value.equals(grp_status)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=49)
	public void group_user_with_valid_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_group_status_in_filter", "To validate whether user is able to get groups through group/user api with valid group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_operator_for_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid group_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when valid group_status value is passed in filter.");
			test.log(LogStatus.PASS, "API return success when valid group_status value is passed in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid group_status value is passed in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when valid group_status value is passed in filter.");
			JSONArray group_user = (JSONArray) json.get("data");
			for(int i=0; i<group_user.size(); i++){
				JSONObject group = (JSONObject) group_user.get(i);
				String group_status_value = group.get("group_status").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_status_value.equals(grp_status)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/user api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=50)
	public void group_user_with_inactive_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_inactive_group_status_in_filter", "To validate whether user is able to get groups through group/user api with inactive group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_inactive_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when 'inactive' group_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API return success when 'inactive' group_status value is passed in filter.");
			test.log(LogStatus.PASS, "API return success when 'inactive' group_status value is passed in filter.");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when 'inactive' group_status is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when 'inactive' group_status is passed in filter");
		}
	}
	
	@Test(priority=51)
	public void group_user_with_deleted_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_deleted_group_status_in_filter", "To validate whether user is able to get groups through group/user api with 'deleted' group_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_deleted_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when 'deleted' group_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API return success when 'deleted' group_status value is passed in filter.");
			test.log(LogStatus.PASS, "API return success when 'deleted' group_status value is passed in filter.");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when 'deleted' group_status is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when 'deleted' group_status is passed in filter");
		}
	}
	
	@Test(priority=52) // This is not working right now
	public void group_user_with_blank_ct_user_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_ct_user_id_in_filter", "To validate whether user is able to get groups through group/user api with blank ct_user_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "ct_user_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank ct_user_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank ct_user_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank ct_user_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank ct_user_id is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank ct_user_id is passed in filter");
		}
	}
	
//	@Test(priority=53) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_ct_user_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_ct_user_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid ct_user_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_ct_user_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] ct_user_ids = test_data.get(3).split(",");
		for(String ct_user_id:ct_user_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+ct_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid ct_user_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid ct_user_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid ct_user_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid ct_user_id is passed in filter. Defect Reported: CT-17171");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid ct_user_id is passed in filter");
			}
		}
	}
	
	@Test(priority=54)
	public void group_user_with_invalid_operator_for_ct_user_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_ct_user_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for ct_user_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_ct_user_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for ct_user_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for ct_user_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for ct_user_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"8", "Proper validation message is not displayed when invalid("+operator+") operator for ct_user_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=55)
	public void group_user_with_nonexisting_ct_user_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_ct_user_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing ct_user_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_ct_user_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Long.parseLong(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing ct_user_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing ct_user_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing ct_user_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing ct_user_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing ct_user_id is passed in filter");
		}
	}	
	
	@Test(priority=56)
	public void group_user_with_valid_operator_for_ct_user_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_ct_user_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for ct_user_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.User.CT_USER_ID, encoded_operator = "";
		int agency_user_id = Integer.parseInt(yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.UserConstants.ID).toString()),
			company_user_id = Integer.parseInt(yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.UserConstants.ID).toString()),
			location_user_id = Integer.parseInt(yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION)
				.get(TestDataYamlConstants.UserConstants.ID).toString());
		int[] ct_user_ids = {agency_user_id,company_user_id,location_user_id};
		for(int ct_user_id : ct_user_ids){
			String[] operators = {">=","<=","="};
			for(String operator:operators){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+ct_user_id));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.PASS, "Check status code when valid operator for ct_user_id is passed in filter");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {		
					JSONParser parser = new JSONParser();
					JSONObject json = (JSONObject) parser.parse(line);
					String result_data =(String) json.get("result").toString();
					Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for ct_user_id field in filter.");
					test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for ct_user_id field in filter.");
					Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for ct_user_id field in filter.");
					test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for ct_user_id field in filter.");
					JSONArray groups_data = (JSONArray) json.get("data");
					for(int i=0; i<groups_data.size(); i++){
						JSONObject group = (JSONObject) groups_data.get(i);
						JSONArray users_list = (JSONArray)group.get("users");
						Boolean user_exist = false;
						for(int j=0; j<users_list.size(); j++){
							JSONObject user= (JSONObject)users_list.get(j);
							Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
							Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
							Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
							Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
							Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
							Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
							Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
							Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
							Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
							Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");
							
							HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
							HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
							HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
							HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
							HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
							HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
							HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");
							
							String user_id = user.get("ct_user_id").toString();
							if(operator.equals(">=")){
								if(Integer.parseInt(user_id)>=ct_user_id)
									user_exist = true;
							}
							else if(operator.equals("<=")){
								if(Integer.parseInt(user_id)<=ct_user_id)
									user_exist = true;
							}
							else if(operator.equals("=")){
								if(user_id.equals(String.valueOf(ct_user_id)))
									user_exist = true;
							}	
						}
						Assert.assertTrue(user_exist);
						test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(ct_user_id));
					}
				}
			}
		}
	}
	
	@Test(priority=57) // This is not working right now
	public void group_user_with_blank_user_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_user_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with blank user_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "user_ext_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank user_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank user_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank user_ext_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank user_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank user_ext_id is passed in filter");
		}
	}
	
	@Test(priority=58) // This is not working right now
	public void group_user_with_invalid_user_ext_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_user_ext_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid user_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_operator_for_ct_user_id_in_filter");
		String filter_field = test_data.get(3);
		String[] user_ext_ids = test_data.get(4).split(",");
		for(String user_ext_id:user_ext_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid user_ext_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid user_ext_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid user_ext_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid user_ext_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid user_ext_id is passed in filter");
			}
		}
	}
	
	@Test(priority=59)
	public void group_user_with_invalid_operator_for_user_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_user_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for user_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");

		String filter_field = TestDataYamlConstants.UserConstants.USER_EXT_ID, encoded_operator = "",
			user_ext_id = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};

		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for user_ext_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for user_ext_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for user_ext_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+user_ext_id, "Proper validation message is not displayed when invalid("+operator+") operator for user_ext_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=60)
	public void group_user_with_nonexisting_user_ext_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_user_ext_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing user_ext_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_user_ext_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Long.parseLong(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing user_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing user_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing user_ext_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing user_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing user_ext_id is passed in filter");
		}
	}
	
	@Test(priority=61)
	public void group_user_with_valid_operator_for_user_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_user_ext_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for user_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.UserConstants.USER_EXT_ID, encoded_operator = "";
		String company_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.UserConstants.USER_EXT_ID).toString(),
			location_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION)
				.get(TestDataYamlConstants.UserConstants.USER_EXT_ID).toString();
		String[] user_ext_ids = {company_user_id,location_user_id};
		for(String user_ext_id : user_ext_ids){
			String operator = "=";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for user_ext_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for user_ext_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for user_ext_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for user_ext_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for user_ext_id field in filter.");
				JSONArray groups_data = (JSONArray) json.get("data");
				for(int i=0; i<groups_data.size(); i++){
					JSONObject group = (JSONObject) groups_data.get(i);
					JSONArray users_list = (JSONArray)group.get("users");
					Boolean user_exist = false;
					System.out.println(user_ext_id);
					for(int j=0; j<users_list.size(); j++){
						JSONObject user= (JSONObject)users_list.get(j);
						Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
						Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
						Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
						Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
						Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
						Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
						Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");
						
						HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
						HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
						HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
						HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
						HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
						HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
						HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");
						
						if(user.get("user_ext_id")!=null){	
							String user_id = user.get("user_ext_id").toString();
							if(operator.equals("=")){
								if(user_id.equals(String.valueOf(user_ext_id)))
									user_exist = true;
							}	
						}
					}
					Assert.assertTrue(user_exist);
					test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(user_ext_id));
				}
			}
		}
	}	
	
	@Test(priority=62) 
	public void group_user_with_blank_user_email_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_user_email_in_filter", "To validate whether user is able to get groups through group/user api with blank user_email is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "user_email";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank user_email is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank user_email is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank user_email is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank user_email is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank user_email is passed in filter");
		}
	}	
	
	@Test(priority=63) // This is not working right now
	public void group_user_with_invalid_user_email_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_user_email_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid user_email is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_valid_operator_for_user_ext_id_in_filter");
		String filter_field = test_data.get(3);
		String[] user_emails = test_data.get(4).split(",");
		for(String user_email:user_emails){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_email));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid user_email is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid user_email is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid user_email is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid user_email is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid user_email is passed in filter");
			}
		}
	}
	
	@Test(priority=64)
	public void group_user_with_invalid_operator_for_user_email_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_user_email_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for user_email is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_user_email_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", user_email=test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_email));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for user_email is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for user_email is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for user_email is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+user_email, "Proper validation message is not displayed when invalid("+operator+") operator for user_email is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+user_email, "Proper validation message is not displayed when invalid("+operator+") operator for user_email is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=65)
	public void group_user_with_nonexisting_user_email_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_user_email_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing user_email value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_user_email_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing user_email is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing user_email is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing user_email is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing user_email is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing user_email is passed in filter");
		}
	}
	
	@Test(priority=66)
	public void group_user_with_valid_operator_for_user_email_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_user_email_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for user_email is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.User.USER_EMAIL, encoded_operator = "";
		String agency_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY).get(
			TestDataYamlConstants.UserConstants.EMAIL).toString(),
			company_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY).get(
				TestDataYamlConstants.UserConstants.EMAIL).toString(),
			location_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION).get(
				TestDataYamlConstants.UserConstants.EMAIL).toString();
		String[] user_emails = {agency_user_id,company_user_id,location_user_id};
		for(String user_email : user_emails){
			String operator = "=";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_email));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for user_email is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				System.out.println(line);
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for user_email field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for user_email field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for user_email field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for user_email field in filter.");
				JSONArray groups_data = (JSONArray) json.get("data");
				for(int i=0; i<groups_data.size(); i++){
					JSONObject group = (JSONObject) groups_data.get(i);
					JSONArray users_list = (JSONArray)group.get("users");
					Boolean user_exist = false;
					for(int j=0; j<users_list.size(); j++){
						JSONObject user= (JSONObject)users_list.get(j);
						Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
						Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
						Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
						Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
						Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
						Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
						Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");
						
						HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
						HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
						HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
						HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
						HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
						HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
						HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");
						
						if(user.get("user_email")!=null){	
							String user_id = user.get("user_email").toString();
							if(operator.equals("=")){
								if(user_id.equals(String.valueOf(user_email)))
									user_exist = true;
							}	
						}
					}
					Assert.assertTrue(user_exist);
					test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(user_email));
				}
			}
		}
	}
	
	@Test(priority=67) 
	public void group_user_with_blank_first_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_first_name_in_filter", "To validate whether user is able to get groups through group/user api with blank first_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "first_name";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank first_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank first_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank first_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank first_name is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank first_name is passed in filter");
		}
	}	
	
	@Test(priority=68)
	public void group_user_with_invalid_operator_for_first_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_first_name_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for first_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_first_name_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", first_name=test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+first_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for first_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for first_name is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for first_name is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.contains(">=")||operator.contains("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : first_name"+operator+first_name, "Proper validation message is not displayed when invalid("+operator+") operator for first_name is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+first_name, "Proper validation message is not displayed when invalid("+operator+") operator for first_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for first_name is passed in filter");
			}
		}
	}
	
	@Test(priority=69)
	public void group_user_with_nonexisting_first_name_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_first_name_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing first_name value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_first_name_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing first_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing first_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing first_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing first_name is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing first_name is passed in filter");
		}
	}
	
	@Test(priority=70)
	public void group_user_with_valid_operator_for_first_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_first_name_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for first_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.UserConstants.FIRST_NAME, encoded_operator = "";
		String agency_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY).get(
			TestDataYamlConstants.UserConstants.FIRST_NAME).toString(),
			company_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY).get(
				TestDataYamlConstants.UserConstants.FIRST_NAME).toString(),
			location_user_id = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION).get(
				TestDataYamlConstants.UserConstants.FIRST_NAME).toString();
		String[] first_names = {agency_user_id,company_user_id,location_user_id};
		for(String first_name : first_names){
			String operator = "=";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+first_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for first_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for first_name field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for first_name field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for first_name field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for first_name field in filter.");
				JSONArray groups_data = (JSONArray) json.get("data");
				for(int i=0; i<groups_data.size(); i++){
					JSONObject group = (JSONObject) groups_data.get(i);
					JSONArray users_list = (JSONArray)group.get("users");
					Boolean user_exist = false;
					for(int j=0; j<users_list.size(); j++){
						JSONObject user= (JSONObject)users_list.get(j);
						Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
						Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
						Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
						Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
						Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
						Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
						Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");
						
						HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
						HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
						HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
						HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
						HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
						HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
						HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");
						
						if(user.get("first_name")!=null){	
							String user_name = user.get("first_name").toString();
							if(operator.equals("=")){
								if(user_name.equals(String.valueOf(first_name)))
									user_exist = true;
							}	
						}
					}
					Assert.assertTrue(user_exist);
					test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(first_name));
				}
			}
		}
	}	
	
	@Test(priority=71) 
	public void group_user_with_blank_last_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_last_name_in_filter", "To validate whether user is able to get groups through group/user api with blank last_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "last_name";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank last_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank last_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank last_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank last_name is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank last_name is passed in filter");
		}
	}	
	
	@Test(priority=72)
	public void group_user_with_invalid_operator_for_last_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_last_name_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for last_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_last_name_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", last_name=test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+last_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for last_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for last_name is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for last_name is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.contains(">=")||operator.contains("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : last_name"+operator+last_name, "Proper validation message is not displayed when invalid("+operator+") operator for last_name is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+last_name, "Proper validation message is not displayed when invalid("+operator+") operator for last_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for last_name is passed in filter");
			}
		}
	}
	
	@Test(priority=73)
	public void group_user_with_nonexisting_last_name_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_last_name_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing last_name value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_last_name_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing last_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing last_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing last_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing last_name is passed in filter. Defect Reported CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing last_name is passed in filter");
		}
	}
	
	@Test(priority=74)
	public void group_user_with_valid_operator_for_last_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_last_name_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for last_name is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.UserConstants.LAST_NAME, encoded_operator = "";
		String agency_last_name = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY).get(
			TestDataYamlConstants.UserConstants.LAST_NAME).toString(),
			company_last_name = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY).get(
				TestDataYamlConstants.UserConstants.LAST_NAME).toString(),
			location_last_name = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION).get(
				TestDataYamlConstants.UserConstants.LAST_NAME).toString();
		String[] last_names = {agency_last_name,company_last_name,location_last_name};
		for(String last_name : last_names){
			String operator = "=";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+last_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for last_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for last_name field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for last_name field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for last_name field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for last_name field in filter.");
				JSONArray groups_data = (JSONArray) json.get("data");
				for(int i=0; i<groups_data.size(); i++){
					JSONObject group = (JSONObject) groups_data.get(i);
					JSONArray users_list = (JSONArray)group.get("users");
					Boolean user_exist = false;
					for(int j=0; j<users_list.size(); j++){
						JSONObject user= (JSONObject)users_list.get(j);
						Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
						Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
						Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
						Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
						Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
						Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
						Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");
						
						HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
						HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
						HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
						HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
						HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
						HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
						HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");
						
						if(user.get("last_name")!=null){	
							String user_name = user.get("last_name").toString();
							if(operator.equals("=")){
								if(user_name.equals(String.valueOf(last_name)))
									user_exist = true;
							}	
						}
					}
					Assert.assertTrue(user_exist);
					test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(last_name));
				}
			}
		}
	}	
	
	@Test(priority=75) 
	public void group_user_with_blank_user_title_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_user_title_in_filter", "To validate whether user is able to get groups through group/user api with blank user_title is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "user_title";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank user_title is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank user_title is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank user_title is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank user_title is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank user_title is passed in filter");
		}
	}	
	
	@Test(priority=76)
	public void group_user_with_invalid_operator_for_user_title_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_user_title_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for user_title is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_user_title_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", user_title=test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_title));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for user_title is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for user_title is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for user_title is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.contains(">=")||operator.contains("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : user_title"+operator+user_title, "Proper validation message is not displayed when invalid("+operator+") operator for user_title is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+user_title, "Proper validation message is not displayed when invalid("+operator+") operator for user_title is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for user_title is passed in filter");
			}
		}
	}
	
	@Test(priority=77)
	public void group_user_with_nonexisting_user_title_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_user_title_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing user_title value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_user_title_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing user_title is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing user_title is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing user_title is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing user_title is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing user_title is passed in filter");
		}
	}
	
	@Test(priority=78)
	public void group_user_with_valid_operator_for_user_title_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_user_title_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for user_title is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.User.USER_TITLE, encoded_operator = "";
		String user_title = null;
		String operator = "=";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_title));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator for user_title is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for user_title field in filter.");
			test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for user_title field in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for user_title field in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for user_title field in filter.");
			JSONArray groups_data = (JSONArray) json.get("data");
			for(int i=0; i<groups_data.size(); i++){
				JSONObject group = (JSONObject) groups_data.get(i);
				JSONArray users_list = (JSONArray)group.get("users");
				Boolean user_exist = false;
				for(int j=0; j<users_list.size(); j++){
					JSONObject user= (JSONObject)users_list.get(j);
					Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
					Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
					Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
					Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
					Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
					Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
					Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
					Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
					Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");

					HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
					HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
					HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
					HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
					HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
					HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
					HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");

					String user_name = String.valueOf(user.get("user_title"));
					if(operator.equals("=")){
						if(user_name.equals(String.valueOf(user_title)))
							user_exist = true;
					}
				}
				Assert.assertTrue(user_exist);
				test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(user_title));
			}
		}
	}
	
	@Test(priority=79) 
	public void group_user_with_blank_role_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_role_id_in_filter", "To validate whether user is able to get groups through group/user api with blank role_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "role_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank role_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank role_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank role_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank role_id is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank role_id is passed in filter");
		}
	}	
	
//	@Test(priority=80) -- Uncomment when defect will be fixed
	public void group_user_with_invalid_role_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_role_id_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid role_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_role_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] role_ids = test_data.get(4).split(",");
		for(String role_id:role_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid role_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid role_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid role_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid role_id is passed in filter. Defect Reported: CT-17171");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid role_id is passed in filter");
			}
		}
	}	
	
	@Test(priority=81)
	public void group_user_with_invalid_operator_for_role_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_role_id_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for role_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_role_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", role_id=test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for role_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for role_id is passed in filter.\n"+line);
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for role_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+role_id, "Proper validation message is not displayed when invalid("+operator+") operator for role_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for role_id is passed in filter");
			}
		}
	}
	
//	@Test(priority=82)  -- Uncomment when defect will be fixed
	public void group_user_with_nonexisting_role_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_role_id_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing role_id value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_role_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing role_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing role_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing role_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing role_id is passed in filter. Defect reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing role_id is passed in filter");
		}
	}
	
	@Test(priority=83)
	public void group_user_with_valid_operator_for_role_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_role_id_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for role_id is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.User.ROLE_ID, encoded_operator = "";
		String role_id = "1";
		String[] operators = {"=",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator("+operator+") for role_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for role_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for role_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for role_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for role_id field in filter.");
				JSONArray groups_data = (JSONArray) json.get("data");
				for(int i=0; i<groups_data.size(); i++){
					JSONObject group = (JSONObject) groups_data.get(i);
					JSONArray users_list = (JSONArray)group.get("users");
					Boolean user_exist = false;
					for(int j=0; j<users_list.size(); j++){
						JSONObject user= (JSONObject)users_list.get(j);
						Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
						Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
						Assert.assertTrue(user.containsKey("role_id"), "role_id field is not present in response");
						Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
						Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
						Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
						Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
						Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");

						HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
						HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
						HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
						HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
						HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
						HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
						HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");

						if(user.get("role_id")!=null){
							String user_role = user.get("role_id").toString();
							if(operator.equals("=")){
								if(user_role.equals(String.valueOf(role_id)))
									user_exist = true;
							}
							else if(operator.equals(">=")){
								if(Integer.parseInt(user_role)>=Integer.parseInt(role_id))
									user_exist = true;
							}
							else if(operator.equals("<=")){
								if(Integer.parseInt(user_role)<=Integer.parseInt(role_id))
									user_exist = true;
							}
						}
					}
					Assert.assertTrue(user_exist);
					test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(role_id));
				}
			}
		}
	}	
	
	@Test(priority=84) 
	public void group_user_with_blank_user_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_blank_user_status_in_filter", "To validate whether user is able to get groups through group/user api with blank user_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = "user_status";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank user_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank user_status is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank user_status is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank user_status is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank user_status is passed in filter");
		}
	}	
	
//	@Test(priority=85) // This is not working right now
	public void group_user_with_invalid_user_status_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_user_status_value_in_filter", "To validate whether user is able to get groups through group/user api with invalid user_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_user_status_value_in_filter");
		String filter_field = test_data.get(3);
		String[] user_stat = test_data.get(4).split(",");
		for(String user_status:user_stat){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid user_status is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid user_status is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid user_status is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid user_status is passed in filter. Defect Reported: CT-17171");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid user_status is passed in filter");
			}
		}
	}	
	
	@Test(priority=86)
	public void group_user_with_invalid_operator_for_user_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_invalid_operator_for_user_status_in_filter", "To validate whether user is able to get groups through group/user api with invalid operator for user_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_operator_for_user_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", user_status = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for user_status is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for user_status is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for user_status is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.contains(">=")||operator.contains("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : user_status"+operator+user_status, "Proper validation message is not displayed when invalid("+operator+") operator for user_status is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+user_status, "Proper validation message is not displayed when invalid("+operator+") operator for user_status is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for user_status is passed in filter");
			}
		}
	}
	
//	@Test(priority=87) -- Uncomment when defect will be fixed
	public void group_user_with_nonexisting_user_status_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_nonexisting_user_status_value_in_filter", "To validate whether user is able to get groups through group/user api with non existing user_status value is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_nonexisting_user_status_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing user_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing user_status is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing user_status is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing user_status is passed in filter. Defect Reported: CT-17171");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing user_status is passed in filter");
		}
	}
	
	@Test(priority=88)
	public void group_user_with_valid_operator_for_user_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_valid_operator_for_user_status_in_filter", "To validate whether user is able to get groups through group/user api with valid operator for user_status is passed in filter.");
		test.assignCategory("CFA GET /group/user API");
		String filter_field = TestDataYamlConstants.GroupConstants.User.USER_STATUS, encoded_operator = "";
		String user_status = Constants.ComponentStatus.ACTIVE;
		String operator = "=";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+user_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator for user_status is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for user_status field in filter.");
			test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for user_status field in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when valid operator("+operator+") is passed for user_status field in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when valid operator("+operator+") is passed for user_status field in filter.");
			JSONArray groups_data = (JSONArray) json.get("data");
			for(int i=0; i<groups_data.size(); i++){
				JSONObject group = (JSONObject) groups_data.get(i);
				JSONArray users_list = (JSONArray)group.get("users");
				Boolean user_exist = false;
				for(int j=0; j<users_list.size(); j++){
					JSONObject user= (JSONObject)users_list.get(j);
					Assert.assertTrue(user.containsKey("first_name"), "first_name field is not present in response");
					Assert.assertTrue(user.containsKey("last_name"), "last_name field is not present in response");
					Assert.assertTrue(user.containsKey("user_status"), "role_id field is not present in response");
					Assert.assertTrue(user.containsKey("ct_user_id"), "ct_user_id field is not present in response");
					Assert.assertTrue(user.containsKey("group_id"), "group_id field is not present in response");
					Assert.assertTrue(user.containsKey("user_status"), "user_status field is not present in response");
					Assert.assertTrue(user.containsKey("user_email"), "user_email field is not present in response");
					Assert.assertTrue(user.containsKey("user_ext_id"), "user_ext_id field is not present in response");
					Assert.assertTrue(user.containsKey("user_title"), "user_title field is not present in response");

					HelperClass.multiple_assertnotEquals(user.get("first_name"), "first_name");
					HelperClass.multiple_assertnotEquals(user.get("last_name"), "last_name");
					HelperClass.multiple_assertnotEquals(user.get("role_id"), "role_id");
					HelperClass.multiple_assertnotEquals(user.get("ct_user_id"), "ct_user_id");
					HelperClass.multiple_assertnotEquals(user.get("group_id"), "group_id");
					HelperClass.multiple_assertnotEquals(user.get("user_status"), "user_status");
					HelperClass.multiple_assertnotEquals(user.get("user_email"), "user_email");

					if(user.get("user_status")!=null){
						String user_name = user.get("user_status").toString();
						if(operator.equals("=")){
							if(user_name.equals(String.valueOf(user_status)))
								user_exist = true;
						}
					}
				}
				Assert.assertTrue(user_exist);
				test.log(LogStatus.PASS, "Users in response is returning as per passed ct_user_id: "+String.valueOf(user_status));
			}
		}
	}
	
	@Test(priority=89)
	public void group_user_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_agency_admin_access_token", "To validate whether user is able to get groups through group/user api with agency admin access_token");
		test.assignCategory("CFA GET /group/user API");
		String[] group_ids = {yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(),
			yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(),
			yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION)
				.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(),
			};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when agency admin access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when agency admin access_token is passed.");
		   test.log(LogStatus.PASS, "API returns success when agency admin access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when agency admin access_token is passed.");
		   test.log(LogStatus.PASS, "err data is null when agency admin access_token is passed.");
		   JSONArray json_array = (JSONArray) json.get("data");
		   Boolean is_agency_group_exist = false, is_company_group_exist = false, is_location_group_exist = false, is_other_billing_group_exist = false;
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);	   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
		   }
		   Assert.assertTrue(is_agency_group_exist, "group/user api does not return agency level group when agency admin access_token is used.");
		   Assert.assertTrue(is_company_group_exist, "group/user api does not return company level group when agency admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/user api does not return location level group when agency admin access_token is used.");
		   Assert.assertFalse(is_other_billing_group_exist, "group/user api return other billing group when agency admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");
		   test.log(LogStatus.PASS, "Check group/user api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/user api return agency level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return company level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return location level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return does not return other billing group when agency admin access_token is used.");
		}
	}
	
//	@Test(priority=90)
	public void group_user_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_company_admin_access_token", "To validate whether user is able to get groups through group/user api with company admin access_token");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		
		test_data = HelperClass.readTestData(class_name, "group_user_with_company_admin_access_token");
		String[] group_ids = test_data.get(1).split(",");
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when company admin access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);		   
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when company admin access_token is passed.");
		   test.log(LogStatus.PASS, "API returns success when company admin access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when company admin access_token is passed.");
		   test.log(LogStatus.PASS, "err data is null when company admin access_token is passed.");
		   JSONArray json_array = (JSONArray) json.get("data");
		   Boolean is_agency_group_exist = false, is_company_group_exist = false, is_location_group_exist = false, is_other_billing_group_exist = false;
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);		   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
		   }
		   Assert.assertFalse(is_agency_group_exist, "group/user api return agency level group when company admin access_token is used.");
		   Assert.assertTrue(is_company_group_exist, "group/user api does not return company level group when company admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/user api does not return location level group when company admin access_token is used.");
		   Assert.assertFalse(is_other_billing_group_exist, "group/user api does not return other billing group when company admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");
		   test.log(LogStatus.PASS, "Check group/user api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/user api does not return agency level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return company level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return location level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return return other billing group when company admin access_token is used.");
		}
	}
	
//	@Test(priority=91)
	public void group_user_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_user_with_location_admin_access_token", "To validate whether user is able to get groups through group/user api with location admin access_token");
		test.assignCategory("CFA GET /group/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		
		test_data = HelperClass.readTestData(class_name, "group_user_with_location_admin_access_token");
		String[] group_ids = test_data.get(1).split(",");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status message when location admin access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when location admin access_token is passed.");
		   test.log(LogStatus.PASS, "API returns success when location admin access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when location admin access_token is passed.");
		   test.log(LogStatus.PASS, "err data is null when location admin access_token is passed.");
		   JSONArray json_array = (JSONArray) json.get("data");
		   Boolean is_agency_group_exist = false, is_company_group_exist = false, is_location_group_exist = false, is_other_billing_group_exist=false;
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);	   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
		   }
		   Assert.assertFalse(is_agency_group_exist, "group/user api return agency level group when location admin access_token is used.");
		   Assert.assertFalse(is_company_group_exist, "group/user api return company level group when location admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/user api does not return location level group when location admin access_token is used.");
		   Assert.assertFalse(is_other_billing_group_exist, "group/user api other billing group when location admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");
		   test.log(LogStatus.PASS, "Check group/user api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/user api does not return agency level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api does not return company level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return location level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/user api return does not return other billing group when location admin access_token is used.");
		}
	}		
}
