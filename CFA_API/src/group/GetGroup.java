package group;

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
public class GetGroup extends BaseClass{
	String class_name = "GetGroup";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
    @Test(priority=0)
	public void get_group_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_group_without_access_token", "To validate whether user is able to get group through group api without access_token");
		test.assignCategory("CFA GET /group API");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/group/user?id=8", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
  @Test(priority=1)
	public void group_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_user_with_invalid_access_token", "To validate whether user is able to get groups through group api with invalid access_token");
		test.assignCategory("CFA GET /group API");
		test_data = HelperClass.readTestData(class_name, "group_user_with_invalid_access_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
    @Test(priority=2)
	public void group_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_with_expired_access_token", "To validate whether user is able to get groups through group api with expired access_token");
		test.assignCategory("CFA GET /group API");
		test_data = HelperClass.readTestData(class_name, "group_with_expired_access_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
    @Test(priority=3)
	public void group_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_with_valid_access_token", "To validate whether user is able to get groups through group api with valid access_token");
		test.assignCategory("CFA GET /group API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
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
		   test.log(LogStatus.PASS, "API returns success when valid access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when valid access_token is passed.");
		   test.log(LogStatus.PASS, "err data is null when valid access_token is passed.");
		   JSONObject group = (JSONObject) json.get("data");
		   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("billing_id"), "ct_user_id field is not present for group object: "+group);
		   
		   HelperClass.multiple_assertnotEquals(group.get("group_id"), "group_id");
		   HelperClass.multiple_assertnotEquals(group.get("group_name"), "group_name");
		   if(!group.get("group_id").toString().equals(groupId))
			   HelperClass.multiple_assertnotEquals(group.get("group_parent_id"), "group_parent_id");
		   HelperClass.multiple_assertnotEquals(group.get("top_group_id"), "top_group_id");
		   HelperClass.multiple_assertnotEquals(group.get("group_status"), "group_status");
		   HelperClass.multiple_assertnotEquals(group.get("billing_id"), "billing_id");
		   
		   Assert.assertEquals(group.get("group_id").getClass().getName(),"java.lang.Long");
		   Assert.assertEquals(group.get("group_name").getClass().getName(),"java.lang.String");
		   if(!group.get("group_id").toString().equals(groupId))
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
	public void group_api_without_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_id_without_id", "To validate whether user is able to get groups through group api without id parameter.");
		test.assignCategory("CFA GET /group API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when id parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String message = json.get("message").toString();
		   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when id value is not passed");		
		   JSONArray errors_array = (JSONArray)json.get("errors");
		   JSONObject error_data = (JSONObject) errors_array.get(0);
		   String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when id value is not passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Value is required but was not provided", "Invalid message value is returned in response when id value is not passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when id value is not passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			JSONArray path_data = new JSONArray();
			path_data.add("paths");
			path_data.add("/group");
			path_data.add("get");
			path_data.add("parameters");
			path_data.add("0");
			Assert.assertEquals(error_path, path_data);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "REQUIRED", "Invalid code value is returned in response when id value is not passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value is required but was not provided", "Invalid message value is returned in response when id value is not passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path, path_data, "path is not blank when id value is not passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when id value is not passed.");
		}   
	}	
	
    @Test(priority=5)
	public void group_api_with_blank_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_blank_id", "To validate whether user is able to get groups through group api with blank id value");
		test.assignCategory("CFA GET /group API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String message = json.get("message").toString();
		   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank id value is passed");		
		   JSONArray errors_array = (JSONArray)json.get("errors");
		   JSONObject error_data = (JSONObject) errors_array.get(0);
		   String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank id value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when blank id value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when blank id value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty());                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank id value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank id value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank id value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank id value is passed.");
		}   
	}	
	
    @Test(priority=6)
	public void group_api_with_invalid_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_invalid_id", "To validate whether user is able to get groups through group api with invalid id value");
		test.assignCategory("CFA GET /group API");
		test_data = HelperClass.readTestData(class_name, "group_api_with_invalid_id");
		String[] invalid_ids = test_data.get(1).split(",");
		for(String id:invalid_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+id+") id value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+id+") id value is passed");		
			   JSONArray errors_array = (JSONArray)json.get("errors");
			   JSONObject error_data = (JSONObject) errors_array.get(0);
			   String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+id+") id value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+id+") id value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when invalid("+id+") id value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty());                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+id+") id value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+id+") id value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than invalid("+id+") id value is passed");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+id+") id value is passed.");
			}   
		}
	}
	
    @Test(priority=7)
	public void group_api_with_valid_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_valid_id", "To validate whether user is able to get groups through group api with valid id");
		test.assignCategory("CFA GET /group API");
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when valid id is passed.");
		   test.log(LogStatus.PASS, "API returns success when valid id is passed.");
		   Object err_data = json.get("err");
		   Assert.assertEquals(err_data, null, "err data is not null when valid id is passed.");
		   test.log(LogStatus.PASS, "err data is null when valid id is passed.");
		   JSONObject group = (JSONObject) json.get("data");
		   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
		   Assert.assertTrue(group.containsKey("billing_id"), "ct_user_id field is not present for group object: "+group);
		   
		   HelperClass.multiple_assertnotEquals(group.get("group_id"), "group_id");
		   HelperClass.multiple_assertnotEquals(group.get("group_name"), "group_name");
		   if(!group.get("group_id").toString().equals(groupId))
			   HelperClass.multiple_assertnotEquals(group.get("group_parent_id"), "group_parent_id");
		   HelperClass.multiple_assertnotEquals(group.get("top_group_id"), "top_group_id");
		   HelperClass.multiple_assertnotEquals(group.get("group_status"), "group_status");
		   HelperClass.multiple_assertnotEquals(group.get("billing_id"), "billing_id");
		   
		   Assert.assertEquals(group.get("group_id").getClass().getName(),"java.lang.Long");
		   Assert.assertEquals(group.get("group_name").getClass().getName(),"java.lang.String");
		   if(!group.get("group_id").toString().equals(groupId))
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
	
    @Test(priority=8)
	public void group_api_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_agency_admin_access_token", "To validate whether user is able to get groups through group api with agency admin access_token");
		test.assignCategory("CFA GET /group API");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(agency_group) || id.equals(company_group) || id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   JSONObject group = (JSONObject) json.get("data");
				   Assert.assertEquals(group.get("group_id").toString(), id, "other groups are returned when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check api returned the passed group when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
			   }
			   else if(id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns error when other billing group id is passed using agency admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested group.", "Proper validation message is not displayed when other billing group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when other billing group id is passed using agency admin access_token.");
			   }
			}
		}
	}
	
    @Test(priority=9)
	public void group_api_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_company_admin_access_token", "To validate whether user is able to get groups through group api with company admin access_token");
		test.assignCategory("CFA GET /group API");
		
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
		
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(company_group) || id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
				   JSONObject group = (JSONObject) json.get("data");
				   Assert.assertEquals(group.get("group_id").toString(), id, "other groups are returned when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check api returned the passed group when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using agency admin access_token.");
			   }
			   else if(id.equals(agency_group) || id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns error when "+(id.equals(agency_group)?"agency level":"other billing")+"group id is passed using agency admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested group.", "Proper validation message is not displayed when "+(id.equals(agency_group)?"agency level":"other billing")+"group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agency_group)?"agency level":"other billing")+" group id is passed using agency admin access_token.");
			   }
			}
		}
	}	
	
//    @Test(priority=10)
	public void group_api_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_api_with_location_admin_access_token", "To validate whether user is able to get groups through group api with location admin access_token");
		test.assignCategory("CFA GET /group API");
		
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
		
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when location level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when location level group id is passed using agency admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when location level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when location level group id is passed using agency admin access_token.");
				   JSONObject group = (JSONObject) json.get("data");
				   Assert.assertEquals(group.get("group_id").toString(), id, "other groups are returned when location level group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check api returned the passed group when location level group id is passed using agency admin access_token.");
			   }
			   else if(id.equals(agency_group) || id.equals(company_group) || id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns error when "+(id.equals(agency_group)?"agency level": (id.equals(company_group)?"company level":"other billing"))+"group id is passed using agency admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested group.", "Proper validation message is not displayed when "+(id.equals(agency_group)?"agency level": (id.equals(company_group)?"company level":"other billing"))+"group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agency_group)?"agency level": (id.equals(company_group)?"company level":"other billing"))+" group id is passed using agency admin access_token.");
			   }
			}
		}
	}		
}
