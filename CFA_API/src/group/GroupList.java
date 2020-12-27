package group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.convirza.tests.core.utils.DBIndustryUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.convirza.tests.core.utils.DBIndustryUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class GroupList extends BaseClass{
	
	String class_name = "GroupList";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();

	@BeforeClass
	public void group_list_parameter_setup() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute group/list api with valid access_token
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);		   
		}
	}
	
	@Test(priority=0)
	public void group_list_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_list_without_access_token", "To validate whether user is able to get groups through group/list api without access_token");
		test.assignCategory("CFA GET /group/list API");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/group/list", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status message when access_token is not passed");
	}
	
	@Test(priority=1)
	public void group_list_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_list_with_invalid_access_token", "To validate whether user is able to get groups through group/list api with invalid access_token");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void group_list_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_list_with_expired_access_token", "To validate whether user is able to get groups through group/list api with expired access_token");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void group_list_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_access_token", "To validate whether user is able to get groups through group/list api with valid access_token");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, list);
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

		   Assert.assertTrue(json_array.size()<=100, "List of Groups not returning from group/list api method.");
		   test.log(LogStatus.PASS, "Check 100 groups is not returning by default from group/list api method.");

		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);
			   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("billing_id"), "billing_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("address"), "address field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("city"), "city field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("state"), "state field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("zip"), "zip field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("phone_number"), "phone_number field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_created"), "group_created field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_modified"), "group_modified field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_id"), "industry_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_name"), "industry_name field is not present for group object: "+group);
			   
			   HelperClass.multiple_assertnotEquals(group.get("group_id"), "group_id");
			   HelperClass.multiple_assertnotEquals(group.get("group_name"), "group_name");
			 
			   if(!group.get("group_id").equals(group.get("top_group_id"))) {
				   HelperClass.multiple_assertnotEquals(group.get("group_parent_id"), "group_parent_id");	   
			   }
			   HelperClass.multiple_assertnotEquals(group.get("top_group_id"), "top_group_id");
			   HelperClass.multiple_assertnotEquals(group.get("group_status"), "group_status");
			   HelperClass.multiple_assertnotEquals(group.get("billing_id"), "billing_id");
			   HelperClass.multiple_assertnotEquals(group.get("group_created"), "group_created");
//			   HelperClass.multiple_assertnotEquals(group.get("industry_name"), "industry_name");
//			   HelperClass.multiple_assertnotEquals(group.get("industry_id"), "industry_id");
			   
			   Assert.assertEquals(group.get("group_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("group_name").getClass().getName(),"java.lang.String");
			   if(!group.get("group_id").equals(group.get("top_group_id"))) {
				   Assert.assertEquals(group.get("group_parent_id").getClass().getName(),"java.lang.Long");	   
			   }
			
			   Assert.assertEquals(group.get("top_group_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("group_status").getClass().getName(),"java.lang.String");
			   Assert.assertEquals(group.get("billing_id").getClass().getName(),"java.lang.Long");
			   Assert.assertEquals(group.get("group_created").getClass().getName(),"java.lang.String");
//			   Assert.assertEquals(group.get("industry_id").getClass().getName(),"java.lang.Long");
//			   Assert.assertEquals(group.get("industry_name").getClass().getName(),"java.lang.String");
		   }
		   test.log(LogStatus.PASS, "To validate whether group_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_ext_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_name is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_parent_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether top_group_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_status is present in response.");
		   test.log(LogStatus.PASS, "To validate whether billing_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether address is present in response.");
		   test.log(LogStatus.PASS, "To validate whether city is present in response.");
		   test.log(LogStatus.PASS, "To validate whether state is present in response.");
		   test.log(LogStatus.PASS, "To validate whether zip is present in response.");
		   test.log(LogStatus.PASS, "To validate whether phone_number is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_created is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_modified is present in response.");
		   test.log(LogStatus.PASS, "To validate whether industry_id is present in response.");
		   test.log(LogStatus.PASS, "To validate whether industry_name is present in response.");
		   test.log(LogStatus.PASS, "To validate whether group_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_name is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_parent_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether top_group_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether group_status is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether billing_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether industry_id is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate whether industry_name is not null or blank in response.");
		   test.log(LogStatus.PASS, "To validate data type of fields in response.");
		}
	}	
	
	@Test(priority=4)
	public void group_list_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_limit", "To validate whether user is able to get groups through group/list api with blank limit value.");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_limit", "To validate whether user is able to get groups through group/list api with invalid limit value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_limit");
		System.out.println(test_data.get(1));
		String[] invalid_limit = test_data.get(1).split(",");
		for(String limit:invalid_limit){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("limit", limit));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_negative_limit", "To validate whether user is able to get groups through group/list api with negative limit value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_negative_limit");
		System.out.println(test_data.get(1));
		int limit_value = Integer.parseInt(test_data.get(1));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("limit", Integer.toString(limit_value)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			ArrayList<String> error = new ArrayList();
			error.add("paths");
			error.add("/group/list");
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
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative("+limit_value+") limit value is passed.");
		}
	}
	
	@Test(priority=6)
	public void group_list_with_valid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_limit", "To validate whether user is able to get groups through group/list api with valid limit value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_limit");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String limit = test_data.get(1);
		params.add(new BasicNameValuePair("limit", limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
		   Assert.assertTrue(json_array.size()<=Integer.parseInt(limit), "API is not returning number of records when applied limit.");
		   test.log(LogStatus.PASS, "API is returning number of records defined in limit.");
		}
	}
	
	@Test(priority=7)
	public void group_list_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String offset = "";
		params.add(new BasicNameValuePair("offset", offset));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_offset", "To validate whether user is able to get groups through group/list api with invalid offset value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_offset");
		String[] offset = test_data.get(2).split(",");
		for(String offset_value: offset){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("offset", offset_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_negative_offset", "To validate whether user is able to get groups through group/list api with negative offset value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_negative_offset");
		int offset_value = Integer.parseInt(test_data.get(2));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("offset", Integer.toString(offset_value)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			error.add("/group/list");
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
	public void group_list_with_0_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api without offset
		test = extent.startTest("group_list_with_0_offset", "To validate whether user is able to get groups through group/list api with 0 offset value");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_0_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/list api method without offset value");
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
		
		// Execute group/list api with 0 offset value
		nvps.add(new BasicNameValuePair("offset", test_data.get(2)));
		response = HelperClass.make_get_request("/v2/group/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/list api method with 0 offset value");
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
	public void group_list_with_valid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_offset", "To validate whether user is able to get groups through group/list api with valid offset value.");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		int offset = Constants.GROUP_LEVEL - 2;
		params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_valid_limit_and_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute group/list api without offset value
		test = extent.startTest("group_list_with_valid_limit_and_offset", "To validate whether user is able to get groups through group/list api with valid limit and offset value");
		test.assignCategory("CFA GET /group/list API");

		int limit = Constants.GROUP_LEVEL - 2;

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(limit)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute group/list api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		List<String> group_id = new ArrayList<>();
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "API returns success when limit is passed and offset is not passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "err is null when limit is passed and offset is not passed.");
		   // Get the 10th group data from the group list
		   for(int i=0;i<limit;i++){
			   JSONObject nth_camp_data =(JSONObject) array.get(i);
			   String nth_group_id = nth_camp_data.get("group_id").toString();
			   group_id.add(nth_group_id);
		   }  
		}
	   // Execute group/list api method with offset value
	   nvps.add(new BasicNameValuePair("offset", String.valueOf(Constants.GROUP_LEVEL - 2)));
	   response = HelperClass.make_get_request("/v2/group/list", access_token, nvps);
	   test.log(LogStatus.INFO, "Execute group/list api method with offset value");
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
		   // Get the 10th campaign data from the campaign list
		   for (int i=0; i<array.size(); i++) {
			   JSONObject data = (JSONObject) array.get(i);
			   Assert.assertFalse(group_id.contains(data.get("group_id").toString()), "Offset is not working properly.");
		   }
	   }	
	}  
	
	@Test(priority=12)
	public void group_list_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_filter", "To validate whether user is able to get groups through group/list api with blank filter value.");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_filter", "To validate whether user is able to get groups through group/list api with invalid filter value.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_filter");
		String[] filter_values = test_data.get(3).split(",");
		for(String filter:filter_values){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("filter", filter));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_filter_field() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_filter", "To validate whether user is able to get groups through group/list api with invalid filter field is passed.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_filter_field");
		String filter = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = "";
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_filter_format() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_filter_format", "To validate whether user is able to get groups through group/list api with invalid filter format is passed.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_filter_format");
		String filter = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("filter", filter));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_blank_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_id_in_filter", "To validate whether user is able to get groups through group/list api with blank group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_blank_group_id_in_filter");
		String filter_field = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_invalid_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_group_id_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_group_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_ids = test_data.get(4).split(",");
		for(String group_id:group_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				Assert.assertEquals(error_des, "No record found", "Proper validation message is not displayed when invalid group_id is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=18)
	public void group_list_with_invalid_operator_for_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_operator_for_group_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_nonexisting_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_group_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_group_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	public void group_list_with_valid_operator_for_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_ID, encoded_operator = "";
		int ouid = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).get(
				TestDataYamlConstants.GroupConstants.GROUP_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+ouid));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				JSONArray group_list = (JSONArray) json.get("data");
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
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
					test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
				}
			}
		}
	}
	
	@Test(priority=21)
	public void group_list_with_blank_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_ext_id_in_filter", "To validate whether user is able to get groups through group/list api with blank group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_blank_group_ext_id_in_filter");
		String filter_field = test_data.get(3);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_ext_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_ext_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_ext_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_ext_id is passed in filter");
		}
	}
	
	@Test(priority=22)
	public void group_list_with_invalid_operator_for_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_ext_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_operator_for_group_ext_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"1234", "Proper validation message is not displayed when invalid("+operator+") operator for group_ext_id is passed in filter");
					test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for group_ext_id is passed in filter");
				
			}
		}
	}
	
	@Test(priority=23)
	public void group_list_with_nonexisting_group_ext_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_group_ext_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_ext_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_group_ext_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=24)
	public void group_list_with_null_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_null_group_ext_id_in_filter", "To validate whether user is able to get groups through group/list api with null value for group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_ext_id", encoded_operator = "";
		String operator = "=";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when null is passed for group_ext_id in filter.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data =(String) json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API return error when null is passed for group_ext_id in filter.");
			test.log(LogStatus.PASS, "API return success when null is passed for group_ext_id in filter.");
			Assert.assertEquals(json.get("err"), null, "err data is not null in api response when null is passed for group_ext_id in filter.");
			test.log(LogStatus.PASS, "err data is null in api response when null is passed for group_ext_id in filter.");
			JSONArray group_list = (JSONArray) json.get("data");
			for(int i=0; i<group_list.size(); i++){
				JSONObject group = (JSONObject) group_list.get(i);
				Assert.assertNull(group.get("group_ext_id"), "group_ext_id is not is response when null is passed for group_ext_id in filter.");
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=24)
	public void group_list_with_valid_operator_for_group_ext_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_ext_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for group_ext_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_EXT_ID, encoded_operator = "";
		String operator = "=",
			external_id = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+external_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			JSONArray group_list = (JSONArray) json.get("data");
			for(int i=0; i<group_list.size(); i++){
				JSONObject group = (JSONObject) group_list.get(i);
				String group_ext_id = group.get("group_ext_id").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_ext_id.equals(external_id)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=25)
	public void group_list_with_blank_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_name_in_filter", "To validate whether user is able to get groups through group/list api with blank group_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_name";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=26)
	public void group_list_with_invalid_operator_for_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_name_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_NAME, encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+"Company-CFA-API"));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+"Company-CFA-API", "Proper validation message is not displayed when invalid("+operator+") operator for group_name is passed in filter");
				else if (operator.equals(">")||operator.equals("<"))
					Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid("+operator+") operator for group_name is passed in filter");
				else	
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+"Company-CFA-API", "Proper validation message is not displayed when invalid("+operator+") operator for group_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for group_name is passed in filter");
			}
		}
	}	
	
	@Test(priority=27)
	public void group_list_with_nonexisting_group_name_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_group_name_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_name value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_group_name_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=28)
	public void group_list_with_valid_operator_for_group_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_name_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for group_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");

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
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				JSONArray group_list = (JSONArray) json.get("data");
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
					String group_name_value = group.get("group_name").toString();
					boolean is_mismatched_group_exist = false;
					if(operator.equals("=")){
						if(!group_name_value.equals(group_name)){
							is_mismatched_group_exist = true;
						}
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
					test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
				}
			}
		}
	}	
	
	@Test(priority=29) // This is not working right now
	public void group_list_with_blank_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_parent_id_in_filter", "To validate whether user is able to get groups through group/list api with blank group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_parent_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_parent_id is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_parent_id is passed in filter");
		}
	}
	
//	@Test(priority=30) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_group_parent_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_group_parent_id_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_group_parent_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_parent_ids = test_data.get(3).split(",");
		for(String group_parent_id:group_parent_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_parent_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid group_parent_id is passed in filter.\n Defect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid group_parent_id is passed in filter");
			}
		}
	}
	
	@Test(priority=31)
	public void group_list_with_invalid_operator_for_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_parent_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_parent_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=32)
	public void group_list_with_nonexisting_group_parent_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_group_parent_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_parent_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_group_parent_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=33)
	public void group_list_with_valid_operator_for_group_parent_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_parent_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for group_parent_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, encoded_operator = "";
		int parent_ouid = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY).
			get(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+parent_ouid));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				JSONArray group_list = (JSONArray) json.get("data");
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
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
					test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
				}
			}
		}
	}	
	
	@Test(priority=34) // This is not working right now
	public void group_list_with_blank_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_top_group_id_in_filter", "To validate whether user is able to get groups through group/list api with blank top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "top_group_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank top_group_id is passed in filter.\nDefect Reported:CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank top_group_id is passed in filter");
		}
	}
	
//	@Test(priority=35) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_top_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_top_group_id_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_top_group_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] top_group_ids = test_data.get(3).split(",");
		for(String top_group_id:top_group_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+top_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid top_group_id is passed in filter.\nDefect Reported:CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid top_group_id is passed in filter");
			}
		}
	}
	
	@Test(priority=36)
	public void group_list_with_invalid_operator_for_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_top_group_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "top_group_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=37)
	public void group_list_with_nonexisting_top_group_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_top_group_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing top_group_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_top_group_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=38)
	public void group_list_with_valid_operator_for_top_group_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_top_group_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for top_group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_top_group_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
//		int top_group_id = Integer.parseInt(test_data.get(4));
		
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String top_group_id = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+top_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				JSONArray group_list = (JSONArray) json.get("data");
				boolean is_mismatched_group_exist = false; int mistmatched_top_group_id = 0;
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
					int top_group_id_in_response = Integer.parseInt(group.get("top_group_id").toString());
					if(top_group_id_in_response!=Integer.parseInt(top_group_id)){
						is_mismatched_group_exist = true;
						mistmatched_top_group_id = top_group_id_in_response;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter when "+operator+" is used with top_group_id. Returned top_group_id: "+mistmatched_top_group_id);
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}	
	
	@Test(priority=39) // This is not working right now
	public void group_list_with_blank_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_billing_id_in_filter", "To validate whether user is able to get groups through group/list api with blank billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "billing_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank billing_id is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank billing_id is passed in filter");
		}
	}
	
//	@Test(priority=40) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_billing_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_billing_id_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_billing_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] billing_ids = test_data.get(3).split(",");
		for(String billing_id:billing_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+billing_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				Assert.assertEquals(error_des, "no records found", "Proper validation message is not displayed when invalid billing_id is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid billing_id is passed in filter");
			}
		}
	}
	
	@Test(priority=41)
	public void group_list_with_invalid_operator_for_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_billing_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "billing_id", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+8));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=42)
	public void group_list_with_nonexisting_billing_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_billing_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing billing_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_billing_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=43)
	public void group_list_with_valid_operator_for_billing_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_billing_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for billing_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");

		String filter_field = TestDataYamlConstants.GroupConstants.BILLING_ID, encoded_operator = "";
		int billing_id = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
		get(TestDataYamlConstants.GroupConstants.BILLING_ID).toString());

		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+billing_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				JSONArray group_list = (JSONArray) json.get("data");
				boolean is_mismatched_group_exist = false; int mistmatched_group=0;
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
					int billing_id_in_response = Integer.parseInt(group.get("top_group_id").toString());
					if(billing_id_in_response!=billing_id){
						is_mismatched_group_exist = true;
						mistmatched_group = billing_id_in_response;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter. Mismatched billing id: "+mistmatched_group);
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}	
	
	@Test(priority=44) // This is not working right now
	public void group_list_with_blank_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_status_in_filter", "To validate whether user is able to get groups through group/list api with blank group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_status";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_status is passed in filter.\nReported Defect: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_status is passed in filter");
		}
	}	
	
	@Test(priority=45)
	public void group_list_with_invalid_operator_for_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_status_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_status", encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+"active"));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
//	@Test(priority=46) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_group_status_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_group_status_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_group_status_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_status = test_data.get(4).split(",");
		for(String grp_status:group_status){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
				Assert.assertEquals(error_des, "No record found", "Proper validation message is not displayed when invalid("+grp_status+") group_status is passed in filter.\nReported Defect: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+grp_status+") group_status is passed in filter");
			}
		}
	}	
	
	@Test(priority=47)
	public void group_list_with_valid_operator_for_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_status_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			JSONArray group_list = (JSONArray) json.get("data");
			for(int i=0; i<group_list.size(); i++){
				JSONObject group = (JSONObject) group_list.get(i);
				String group_status_value = group.get("group_status").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_status_value.equals(grp_status)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=48)
	public void group_list_with_valid_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_group_status_in_filter", "To validate whether user is able to get groups through group/list api with valid group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
			JSONArray group_list = (JSONArray) json.get("data");
			for(int i=0; i<group_list.size(); i++){
				JSONObject group = (JSONObject) group_list.get(i);
				String group_status_value = group.get("group_status").toString();
				boolean is_mismatched_group_exist = false;
				if(operator.equals("=")){
					if(!group_status_value.equals(grp_status)){
						is_mismatched_group_exist = true;
					}
				}
				Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
				test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
			}
		}
	}
	
	@Test(priority=49)
	public void group_list_with_inactive_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_inactive_group_status_in_filter", "To validate whether user is able to get groups through group/list api with inactive group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_inactive_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=50)
	public void group_list_with_deleted_group_status_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_deleted_group_status_in_filter", "To validate whether user is able to get groups through group/list api with 'deleted' group_status is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_deleted_group_status_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "", operator = "=";
		String grp_status = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+grp_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
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
	
	@Test(priority=51)
	public void group_list_with_blank_address_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_address_in_filter", "To validate whether user is able to get groups through group/list api with blank address is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "address";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank address is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank address is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank address is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank address is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank address is passed in filter");
		}
	}	
	
	@Test(priority=52)
	public void group_list_with_non_existing_address_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_address_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing address is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_non_existing_address_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_address = test_data.get(3).split(",");
		for(String address:group_address){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+address));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing address is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+address+") address is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+address+") address is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+address+") address is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+address+") address is passed in filter");
			}
		}
	}	
	
	@Test(priority=53)
	public void group_list_with_invalid_operator_for_address_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_address_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for address is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.ADDRESS, encoded_operator = "",
			address_value = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.ADDRESS).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+address_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for address is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for address is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for address is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : address"+operator+address_value+"");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+address_value, "Proper validation message is not displayed when invalid("+operator+") operator for address is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for address is passed in filter");
			}
		}
	}	
	
	@Test(priority=54)
	public void group_list_with_valid_operator_for_address_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_address_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for address in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_address_in_filter");
		String filter_field = TestDataYamlConstants.GroupConstants.ADDRESS,
			group_address = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY).
				get(TestDataYamlConstants.GroupConstants.ADDRESS).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_address));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator is passed for address field in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for address field.");
			test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for address field.");
			JSONArray data_value = (JSONArray)json.get("data");
			for(int i=0; i<data_value.size();i++){
				JSONObject group_data =(JSONObject) data_value.get(i);
				Assert.assertEquals(group_data.get("address").toString(), group_address, "API returns group which does not matched address passed in filter.");
			}
			test.log(LogStatus.PASS, "API returns group according to applied filter for address field.");
		}
	}
	
	@Test(priority=55)
	public void group_list_with_blank_city_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_city_in_filter", "To validate whether user is able to get groups through group/list api with blank city is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "city";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank city is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank city is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank city is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank city is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank city is passed in filter");
		}
	}	
	
	@Test(priority=56)
	public void group_list_with_non_existing_city_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_city_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing city is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_address_in_filter");
		String filter_field = test_data.get(3);
		String[] group_city = test_data.get(4).split(",");
		for(String city:group_city){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+city));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing city is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+city+") city is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+city+") city is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+city+") city is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+city+") city is passed in filter");
			}
		}
	}	
	
	@Test(priority=57)
	public void group_list_with_invalid_operator_for_city_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_city_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for city is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.CITY, encoded_operator = "",
			city_value = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.CITY).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+city_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for city is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for city is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for city is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : city"+operator+city_value+"");
				else if (operator.equals(">")||operator.equals("<"))
					Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid("+operator+") operator for city is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+city_value, "Proper validation message is not displayed when invalid("+operator+") operator for city is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for city is passed in filter");
			}
		}
	}	
	
	@Test(priority=58)
	public void group_list_with_valid_operator_for_city_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_city_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for city in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_city_in_filter");
		String filter_field = test_data.get(3), city_value = test_data.get(4);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+city_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator is passed for city field in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for city field.");
			test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for city field.");
			JSONArray data_value = (JSONArray)json.get("data");
			for(int i=0; i<data_value.size();i++){
				JSONObject group_data =(JSONObject) data_value.get(i);
				Assert.assertEquals(group_data.get("city").toString(), city_value, "API returns group which does not matched city passed in filter.");
			}
			test.log(LogStatus.PASS, "API returns group according to applied filter for city field.");
		}
	}	
	
	@Test(priority=59)
	public void group_list_with_blank_state_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_state_in_filter", "To validate whether user is able to get groups through group/list api with blank state is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "state";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank state is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank state is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank state is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank state is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank state is passed in filter");
		}
	}	
	
	@Test(priority=60)
	public void group_list_with_non_existing_state_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_state_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing state is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_non_existing_state_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_state = test_data.get(4).split(",");
		for(String state:group_state){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+state));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing state is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+state+") state is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+state+") state is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+state+") state is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+state+") state is passed in filter");
			}
		}
	}	
	
	@Test(priority=61)
	public void group_list_with_invalid_operator_for_state_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_state_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for state is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_operator_for_state_in_filter");
		String filter_field = TestDataYamlConstants.GroupConstants.STATE, encoded_operator = "",
			state_value = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.STATE).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for (String operator:operators) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+state_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for state is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for state is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for state is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+state_value, "Proper validation message is not displayed when invalid("+operator+") operator for state is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+state_value, "Proper validation message is not displayed when invalid("+operator+") operator for state is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for state is passed in filter");
			}
		}
	}	
	
	@Test(priority=62)
	public void group_list_with_valid_operator_for_state_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_state_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for state in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_valid_operator_for_state_in_filter");
		String filter_field = TestDataYamlConstants.GroupConstants.STATE, state_value = yamlReader
			.readGroupInfo(Constants.GroupHierarchy.AGENCY).get(TestDataYamlConstants.GroupConstants.STATE).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+state_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator is passed for state field in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for state field.");
			test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for state field.");
			JSONArray data_value = (JSONArray)json.get("data");
			for(int i=0; i<data_value.size();i++){
				JSONObject group_data =(JSONObject) data_value.get(i);
				Assert.assertEquals(group_data.get("state").toString(), state_value, "API returns group which does not matched state passed in filter.");
			}
			test.log(LogStatus.PASS, "API returns group according to applied filter for state field.");
		}
	}	
	
	@Test(priority=63)
	public void group_list_with_blank_zip_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_zip_in_filter", "To validate whether user is able to get groups through group/list api with blank zip is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "zip";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank zip is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank zip is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank zip is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank zip is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank zip is passed in filter");
		}
	}	
	
	@Test(priority=64)
	public void group_list_with_non_existing_zip_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_zip_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing zip is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_non_existing_zip_value_in_filter");
		String filter_field = test_data.get(3);
		String[] zip_values = test_data.get(4).split(",");
		for(String zip:zip_values){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+zip));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing zip is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {			
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+zip+") zip is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+zip+") zip is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+zip+") zip is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+zip+") zip is passed in filter");
			}
		}
	}	
	
	@Test(priority=65)
	public void group_list_with_invalid_operator_for_zip_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_zip_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for zip is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_operator_for_zip_in_filter");
		String filter_field = TestDataYamlConstants.GroupConstants.ZIP, encoded_operator = "",
			zip_value = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
				.get(TestDataYamlConstants.GroupConstants.ZIP).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+zip_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for zip is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for zip is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for zip is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+zip_value, "Proper validation message is not displayed when invalid("+operator+") operator for zip is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for zip is passed in filter");
			}
		}
	}	
	
	@Test(priority=66)
	public void group_list_with_valid_operator_for_zip_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_zip_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for zip in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.ZIP,
			zipcode = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
				.get(TestDataYamlConstants.GroupConstants.ZIP).toString();

		String[] operators = {"=",">=","<="};
		for(String operator: operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+zipcode));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator is passed for zip field in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for zip field.");
				test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for zip field.");
				JSONArray data_value = (JSONArray)json.get("data");
				for(int i=0; i<data_value.size();i++){
					JSONObject group_data =(JSONObject) data_value.get(i);
					try{
						if(operator.equals(">="))
							Assert.assertTrue(Integer.parseInt(group_data.get("zip").toString())>=Integer.parseInt(zipcode), "groups is not returned according to passed zip filter.");
						else if(operator.equals("<="))
							Assert.assertTrue(Integer.parseInt(group_data.get("zip").toString())<=Integer.parseInt(zipcode), "groups is not returned according to passed zip filter.");
						else
							Assert.assertEquals(group_data.get("zip").toString(), zipcode, "API returns group which does not matched zip passed in filter.");
					}
					catch(NumberFormatException ne){
						Assert.assertFalse(true, "Filter is not working for zip code with "+ operator + "operator.");
					}
				}
				test.log(LogStatus.PASS, "API returns group according to applied filter for zip field.");
			}
		}
	}	
	
	@Test(priority=67)
	public void group_list_with_blank_phone_number_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_phone_number_in_filter", "To validate whether user is able to get groups through group/list api with blank phone_number is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "phone_number";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank phone_number is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank phone_number is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank phone_number is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank phone_number is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank phone_number is passed in filter");
		}
	}	
	
	@Test(priority=68)
	public void group_list_with_non_existing_phone_number_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_phone_number_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing phone_number is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_non_existing_phone_number_value_in_filter");
		String filter_field = test_data.get(3);
		String[] phone_numbers = test_data.get(4).split(",");
		for(String phone_number:phone_numbers){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+phone_numbers));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing phone_number is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+phone_number+") phone_number is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+phone_number+") phone_number is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+phone_number+") phone_number is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+phone_number+") phone_number is passed in filter");
			}
		}
	}	
	
	@Test(priority=69)
	public void group_list_with_invalid_operator_for_phone_number_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_phone_number_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for phone_number is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.PHONE_NUMBER, encoded_operator = "", phone_number_value =
			yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY).get(TestDataYamlConstants.GroupConstants.PHONE_NUMBER).toString();
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+phone_number_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for phone_number is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for phone_number is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for phone_number is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+phone_number_value, "Proper validation message is not displayed when invalid("+operator+") operator for phone_number is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+phone_number_value, "Proper validation message is not displayed when invalid("+operator+") operator for phone_number is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for phone_number is passed in filter");
			}
		}
	}	
	
	@Test(priority=70)
	public void group_list_with_valid_operator_for_phone_number_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_phone_number_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for phone_number in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.PHONE_NUMBER, phone_number = yamlReader
			.readGroupInfo(Constants.GroupHierarchy.COMPANY).get(TestDataYamlConstants.GroupConstants.PHONE_NUMBER).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+phone_number));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator is passed for phone_number field in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for phone_number field.");
			test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for phone_number field.");
			JSONArray data_value = (JSONArray)json.get("data");
			for(int i=0; i<data_value.size();i++){
				JSONObject group_data =(JSONObject) data_value.get(i);
				Assert.assertEquals(group_data.get("phone_number").toString(), phone_number, "API returns group which does not matched phone_number passed in filter.");
			}
			test.log(LogStatus.PASS, "API returns group according to applied filter for phone_number field.");
		}
	}	
	
	@Test(priority=71) //This method is not working
	public void group_list_with_blank_industry_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_industry_id_in_filter", "To validate whether user is able to get groups through group/list api with blank industry_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "industry_id";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank industry_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when blank industry_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank industry_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank industry_id is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank industry_id is passed in filter");
		}
	}
	
//	@Test(priority=72) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_industry_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_industry_id_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid group_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_industry_id_value_in_filter");
		String filter_field = test_data.get(3);
		String[] industry_ids = test_data.get(4).split(",");
		for(String industry_id:industry_ids){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+industry_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid industry_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid industry_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid industry_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "No record found", "Proper validation message is not displayed when invalid industry_id is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid industry_id is passed in filter");
			}
		}
	}
	
	@Test(priority=73) 
	public void group_list_with_invalid_operator_for_industry_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_industry_id_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for industry_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_operator_for_industry_id_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+test_data.get(4)));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for industry_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid operator for industry_id is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid operator for industry_id is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+test_data.get(4), "Proper validation message is not displayed when invalid operator for industry_id is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid operator for industry_id is passed in filter");
			}
		}
	}
	
	@Test(priority=74)
	public void group_list_with_nonexisting_industry_id_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_nonexisting_industry_id_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing industry_id value is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_nonexisting_industry_id_value_in_filter");
		String filter_field = test_data.get(3), encoded_operator = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+Integer.parseInt(test_data.get(4))));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing industry_id is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is not returning success when non existing industry_id is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when non existing industry_id is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing industry_id is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing industry_id is passed in filter");
		}
	}	
	
	@Test(priority=75)
	public void group_list_with_valid_operator_for_industry_id_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_industry_id_in_filter", "To validate whether user is able to get groups through group/list api with valid operator for industry_id is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.INDUSTRY_ID, encoded_operator = "";
		int industry_id = Integer.parseInt(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.GroupConstants.INDUSTRY_ID).toString());
		String[] operators = {">=","<=","="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+industry_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator for industry_id is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {			
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data =(String) json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API return error when valid operator("+operator+") is passed for industry_id field in filter.");
				test.log(LogStatus.PASS, "API return success when valid operator("+operator+") is passed for industry_id field in filter.");
				Assert.assertEquals(json.get("err"), null, "err data is not null in api response when invalid operator("+operator+") is passed for industry_id field in filter.");
				test.log(LogStatus.PASS, "err data is null in api response when invalid operator("+operator+") is passed for industry_id field in filter.");
				JSONArray group_list = (JSONArray) json.get("data");
				for(int i=0; i<group_list.size(); i++){
					JSONObject group = (JSONObject) group_list.get(i);
					int indus_id = Integer.parseInt(group.get("industry_id").toString());
					boolean is_mismatched_group_exist = false;
					if(operator.equals(">=")){
						if(indus_id<industry_id){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("<=")){
						if(indus_id>industry_id){
							is_mismatched_group_exist = true;
						}
					}
					else if(operator.equals("=")){
						if(indus_id>industry_id || indus_id<industry_id){
							is_mismatched_group_exist = true;
						}
					}
					Assert.assertFalse(is_mismatched_group_exist, "API does not return record according to applied filter.");
					test.log(LogStatus.PASS, "Group/list api returns record according to applied filter.");
				}
			}
		}
	}
	
	@Test(priority=76)
	public void group_list_with_blank_industry_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_industry_name_in_filter", "To validate whether user is able to get groups through group/list api with blank industry_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "industry_name";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank industry_name is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank industry_name is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank industry_name is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank industry_name is passed in filter");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank industry_name is passed in filter");
		}
	}	
	
	@Test(priority=77)
	public void group_list_with_non_existing_industry_name_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_non_existing_industry_name_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing industry_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_non_existing_industry_name_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_industry_name = test_data.get(4).split(",");
		for(String industry_name:group_industry_name){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+industry_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing industry_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+industry_name+") industry_name is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+industry_name+") industry_name is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+industry_name+") industry_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+industry_name+") industry_name is passed in filter");
			}
		}
	}	
	
	@Test(priority=78)
	public void group_list_with_invalid_operator_for_industry_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_industry_name_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for industry_name is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.IndustryConstants.INDUSTRY_NAME, encoded_operator = "",
			industry_name_value = DBIndustryUtils.getIndustryNameUsingID(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.INDUSTRY_ID).toString());
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":",">=","<="};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+industry_name_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for industry_name is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for industry_name is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for industry_name is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"string\" used for value being used in rule 1 : "+filter_field+operator+industry_name_value, "Proper validation message is not displayed when invalid("+operator+") operator for industry_name is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+industry_name_value, "Proper validation message is not displayed when invalid("+operator+") operator for industry_name is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for industry_name is passed in filter");
			}
		}
	}
	
	@Test(priority=79) //This is not working right now
	public void group_list_with_valid_operator_for_industry_name_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_industry_name_in_filter", "To validate whether user is able to get groups through group/list api with valid operator is passed for industry_name in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.IndustryConstants.INDUSTRY_NAME,
			industry_name_value = DBIndustryUtils.getIndustryNameUsingID(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.INDUSTRY_ID).toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+industry_name_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid operator is passed for industry_name field in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid operator is passed for industry_name field.");
			test.log(LogStatus.PASS, "Check api is returning success when valid operator is passed for industry_name field.");
			JSONArray data_value = (JSONArray)json.get("data");
			for(int i=0; i<data_value.size();i++){
				JSONObject group_data =(JSONObject) data_value.get(i);
				Assert.assertEquals(group_data.get("industry_name").toString(), industry_name_value, "API returns group which does not matched industry_name passed in filter.");
			}
			test.log(LogStatus.PASS, "API returns group according to applied filter for industry_name field.");
		}
	}	
	
	@Test(priority=80) //This is not working right now
	public void group_list_with_blank_group_created_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_created_in_filter", "To validate whether user is able to get groups through group/list api with blank group_created is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_created";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_created is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank group_created is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_created is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_created is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_created is passed in filter");
		}
	}	
	
//	@Test(priority=81) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_group_created_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_group_created_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_created is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_group_created_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_created_date = test_data.get(4).split(",");
		for(String group_created:group_created_date){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid group_created is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {			
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+group_created+") group_created is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+group_created+") group_created is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid("+group_created+") group_created is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+group_created+") group_created is passed in filter");
			}
		}
	}
	
//	@Test(priority=82) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_formatted_group_created_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_formatted_group_created_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid formatted group_created is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_formatted_group_created_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_created_date = test_data.get(4).split(",");
		for(String group_created:group_created_date){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing group_created is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+group_created+") group_created is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+group_created+") group_created is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+group_created+") group_created is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+group_created+") group_created is passed in filter");
			}
		}
	}	
	
	@Test(priority=83)
	public void group_list_with_invalid_operator_for_group_created_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_created_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_created is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_CREATED, encoded_operator = "",
			group_created_value = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_created");
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for (String operator:operators) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_created_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for group_created is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for group_created is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for group_created is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+group_created_value, "Proper validation message is not displayed when invalid("+operator+") operator for group_created is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for industry_name is passed in filter");
			}
		}
	}
	
	@Test(priority=84)
	public void group_list_with_valid_operator_for_group_created_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_created_in_filter", "To validate whether user is able to get groups through group/list api with valid operator and valid value is passed for group_created in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_CREATED;
		String agencyGroupCreated = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_created");
		String companyGroupCreated = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY).
			get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_created");
		String locationGroupCreated = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION).
			get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_created");
		String group_created_value[] = {agencyGroupCreated, companyGroupCreated, locationGroupCreated};
		for(String group_created:group_created_value){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator and valid("+group_created+") value and valid value is passed for group_created field in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid operator and valid("+group_created+") value is passed for group_created field.");
				test.log(LogStatus.PASS, "Check api is returning success when valid operator and valid("+group_created+") value is passed for group_created field.");
				JSONArray data_value = (JSONArray)json.get("data");
				for(int i=0; i<data_value.size();i++){
					JSONObject group_data =(JSONObject) data_value.get(i);
					Assert.assertEquals(group_data.get("group_created").toString(), DateUtils.getDateTimeFromDate(group_created), "API returns group which does not matched group_created passed in filter.");
				}
			}
		}
		test.log(LogStatus.PASS, "API returns group according to applied filter for group_created field.");
	}		
	
	@Test(priority=85) //This is not working right now
	public void group_list_with_blank_group_modified_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_blank_group_modified_in_filter", "To validate whether user is able to get groups through group/list api with blank group_modified is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = "group_modified";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
		params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_modified is passed in filter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String result_data = json.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank group_modified is passed in filter");
			test.log(LogStatus.PASS, "Check api is returning error when blank group_modified is passed in filter");
			String error_des = json.get("err").toString();
			Assert.assertEquals(error_des, "Please provide valid data.", "Proper validation message is not displayed when blank group_modified is passed in filter.\nDefect Reported: CT-17158");
			test.log(LogStatus.PASS, "Proper validation message is displayed when blank group_modified is passed in filter");
		}
	}	
	
//	@Test(priority=86) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_group_modified_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_group_modified_value_in_filter", "To validate whether user is able to get groups through group/list api with non existing group_modified is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_group_modified_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_modified_date = test_data.get(4).split(",");
		for(String group_modified:group_modified_date){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid group_modified is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+group_modified+") group_modified is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+group_modified+") group_modified is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when invalid("+group_modified+") group_modified is passed in filter.\nDefect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+group_modified+") group_modified is passed in filter");
			}
		}
	}
	
//	@Test(priority=87) -- Uncomment when defect will be fixed
	public void group_list_with_invalid_formatted_group_modified_value_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_formatted_group_modified_value_in_filter", "To validate whether user is able to get groups through group/list api with invalid formatted group_modified is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		test_data = HelperClass.readTestData(class_name, "group_list_with_invalid_formatted_group_modified_value_in_filter");
		String filter_field = test_data.get(3);
		String[] group_modified_date = test_data.get(4).split(",");
		for(String group_modified:group_modified_date){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when non existing group_modified is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when non existing("+group_modified+") group_modified is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when non existing("+group_modified+") group_modified is passed in filter");
				String error_des = json.get("err").toString();
				Assert.assertEquals(error_des, "no records found.", "Proper validation message is not displayed when non existing("+group_modified+") group_modified is passed in filter.\n Defect Reported: CT-17158");
				test.log(LogStatus.PASS, "Proper validation message is displayed when non existing("+group_modified+") group_modified is passed in filter");
			}
		}
	}	
	
	@Test(priority=88)
	public void group_list_with_invalid_operator_for_group_modified_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_invalid_operator_for_group_modified_in_filter", "To validate whether user is able to get groups through group/list api with invalid operator for group_modified is passed in filter.");
		test.assignCategory("CFA GET /group/list API");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_MODIFIED, encoded_operator = "",
			group_modified_value = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
				get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_modified");
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		for(String operator:operators){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_modified_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid operator for group_modified is passed in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is not returning success when invalid("+operator+") operator for group_modified is passed in filter");
				test.log(LogStatus.PASS, "Check api is returning error when invalid("+operator+") operator for group_modified is passed in filter");
				String error_des = json.get("err").toString();
				if(operator.equals(">=")||operator.equals("<="))
					Assert.assertEquals(error_des, "Invalid comparator type \"datetime\" used for value being used in rule 1 : "+filter_field+operator+group_modified_value, "Proper validation message is not displayed when invalid("+operator+") operator for group_modified is passed in filter");
				else
					Assert.assertEquals(error_des, "Unsupported comparator used in filter on rule 1 : "+filter_field+operator+group_modified_value, "Proper validation message is not displayed when invalid("+operator+") operator for group_modified is passed in filter");
				test.log(LogStatus.PASS, "Proper validation message is displayed when invalid("+operator+") operator for industry_name is passed in filter");
			}
		}
	}	
	
//	@Test(priority=89) -- Uncomment when defect will be fixed
	public void group_list_with_valid_operator_for_group_modified_in_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_valid_operator_for_group_modified_in_filter", "To validate whether user is able to get groups through group/list api with valid operator and valid value is passed for group_modified in filter.");
		test.assignCategory("CFA GET /group/list API");

		String agencyGroupModified = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY).
			get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_modified");
		String companyGroupModified = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY).
			get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_modified");
		String locationGroupModified = DBGroupUtils.getGroupFieldById(yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION).
			get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString(), "org_unit_modified");
		String filter_field = TestDataYamlConstants.GroupConstants.GROUP_MODIFIED;
		String[] group_modified_date = {agencyGroupModified, companyGroupModified, locationGroupModified};

		for(String group_modified:group_modified_date){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String encoded_operator = java.net.URLEncoder.encode("=", "UTF-8");
			params.add(new BasicNameValuePair("filter", filter_field+encoded_operator+group_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid operator and valid("+group_modified+") value and valid value is passed for group_modified field in filter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String result_data = json.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid operator and valid("+group_modified+") value is passed for group_modified field.\nDefect Reported: CT-17168");
				test.log(LogStatus.PASS, "Check api is returning success when valid operator and valid("+group_modified+") value is passed for group_modified field.");
				JSONArray data_value = (JSONArray)json.get("data");
				for(int i=0; i<data_value.size();i++){
					JSONObject group_data =(JSONObject) data_value.get(i);
					Assert.assertEquals(group_data.get("group_modified"), group_modified, "API returns group which does not matched group_modified passed in filter.");
				}
			}
		}
		test.log(LogStatus.PASS, "API returns group according to applied filter for group_modified field.");
	}	
	
	@Test(priority=90)
	public void group_list_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_agency_admin_access_token", "To validate whether user is able to get groups through group/list api with agency admin access_token");
		test.assignCategory("CFA GET /group/list API");
		String agencyGroupId = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String companyGroupId = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String locationGroupId = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION)
			.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String[] group_ids = {agencyGroupId,companyGroupId,locationGroupId};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "1000"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, list);
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
			   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("billing_id"), "billing_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("address"), "address field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("city"), "city field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("state"), "state field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("zip"), "zip field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("phone_number"), "phone_number field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_created"), "group_created field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_modified"), "group_modified field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_id"), "industry_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_name"), "industry_name field is not present for group object: "+group);
			   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
		   }
		   Assert.assertTrue(is_agency_group_exist, "group/list api does not return agency level group when agency admin access_token is used.");
		   Assert.assertTrue(is_company_group_exist, "group/list api does not return company level group when agency admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/list api does not return location level group when agency admin access_token is used.");
		   Assert.assertFalse(is_other_billing_group_exist, "group/user api return other billing group when agency admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");		   test.log(LogStatus.PASS, "Check group/list api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/list api return agency level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return company level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return location level group when agency admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return does not return other billing group when agency admin access_token is used.");
		}
	}
	
//	@Test(priority=91)
	public void group_list_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_company_admin_access_token", "To validate whether user is able to get groups through group/list api with company admin access_token");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));

		test_data = HelperClass.readTestData(class_name, "group_list_with_company_admin_access_token");
		String[] group_ids = test_data.get(1).split(",");
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, list);
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
		   Boolean is_agency_group_exist = false, is_company_group_exist = false, is_location_group_exist = false, is_other_billing_group_exist=false;
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);
			   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("billing_id"), "billing_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("address"), "address field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("city"), "city field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("state"), "state field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("zip"), "zip field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("phone_number"), "phone_number field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_created"), "group_created field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_modified"), "group_modified field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_id"), "industry_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_name"), "industry_name field is not present for group object: "+group);
			   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
		   }
		   Assert.assertFalse(is_agency_group_exist, "group/list api return agency level group when company admin access_token is used.");
		   Assert.assertTrue(is_company_group_exist, "group/list api does not return company level group when company admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/list api does not return location level group when company admin access_token is used.");
		   Assert.assertTrue(is_other_billing_group_exist, "group/list api does not return other billing group when company admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");
		   test.log(LogStatus.PASS, "Check group/list api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/list api does not return agency level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return company level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return location level group when company admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return return other billing group when company admin access_token is used.");
		}
	}
	
//	@Test(priority=92)
	public void group_list_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_list_with_location_admin_access_token", "To validate whether user is able to get groups through group/list api with location admin access_token");
		test.assignCategory("CFA GET /group/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		
		test_data = HelperClass.readTestData(class_name, "group_list_with_location_admin_access_token");
		String[] group_ids = test_data.get(1).split(",");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/list", access_token, list);
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
		   Boolean is_agency_group_exist = false, is_company_group_exist = false, is_location_group_exist = false, is_other_billing_group_exist = false;
		   for(int i=0; i<json_array.size(); i++){
			   JSONObject group = (JSONObject) json_array.get(i);
			   Assert.assertTrue(group.containsKey("group_id"), "group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_ext_id"), "group_ext_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_name"), "group_name field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_parent_id"), "group_parent_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("top_group_id"), "top_group_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_status"), "group_status field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("billing_id"), "billing_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("address"), "address field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("city"), "city field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("state"), "state field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("zip"), "zip field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("phone_number"), "phone_number field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_created"), "group_created field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("group_modified"), "group_modified field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_id"), "industry_id field is not present for group object: "+group);
			   Assert.assertTrue(group.containsKey("industry_name"), "industry_name field is not present for group object: "+group);
			   
			   if(group.get("group_id").toString().equals(group_ids[0]))
				   is_agency_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[0]))
				   is_company_group_exist = true;
			   else if(group.get("group_parent_id").toString().equals(group_ids[1]))
				   is_location_group_exist = true;
			   else if(!group.get("top_group_id").toString().equals(group_ids[0]))
				   is_other_billing_group_exist = true;
			   
		   }
		   Assert.assertFalse(is_agency_group_exist, "group/list api return agency level group when location admin access_token is used.");
		   Assert.assertFalse(is_company_group_exist, "group/list api return company level group when location admin access_token is used.");
		   Assert.assertTrue(is_location_group_exist, "group/list api does not return location level group when location admin access_token is used.");
		   Assert.assertTrue(is_other_billing_group_exist, "group/list api other billing group when location admin access_token is used. <b style='color:red'>Defect Reported: CT-16478</b>");
		   test.log(LogStatus.PASS, "Check group/list api returns all fields.");
		   test.log(LogStatus.PASS, "Check group/list api does not return agency level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api does not return company level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return location level group when location admin access_token is used.");
		   test.log(LogStatus.PASS, "Check group/list api return does not return other billing group when location admin access_token is used.");
		}
	}	
}
