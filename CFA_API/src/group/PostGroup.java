package group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
@SuppressWarnings("unchecked")
public class PostGroup extends BaseClass{
	String class_name = "PostGroup";
	ArrayList<String> test_data;
	
	@Test(priority=0)
	public void post_group_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_group_without_access_token", "To validate whether user is create group through post group api without access_token");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "post_group_without_access_token");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(11)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", "", dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=1)
	public void post_group_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_group_with_invalid_access_token", "To validate whether user is create group through post group api with invalid access_token");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "post_group_with_invalid_access_token");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(11)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", invalid_access_token, dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void post_group_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_group_with_expired_access_token", "To validate whether user is create group through post group api with expired access_token");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "post_group_with_expired_access_token");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(11)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", expired_access_token, dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void post_group_without_any_parameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_group_without_any_parameter", "To validate whether user is create group through post group api without any parameter.");
		test.assignCategory("CFA POST /group API");
		JSONArray dni_array = new JSONArray();
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when no parameter is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);		
			Assert.assertTrue(api_response.size()>=1, "API returns empty array in response when no parameter is passed.");
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when no parameter is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when no parameter is passed.");
			String validation = json_response.get("data").toString();
			Assert.assertEquals(validation, "Data sent in invalid format. Must be an array of objects in JSON format.Must not be empty", "Proper validation is not displayed when no parameter is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when no parameter is passed.");
		}
	}
	
	@Test(priority=4)
	public void post_group_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_group_with_valid_access_token", "To validate whether user is create group through post group api with valid access_token");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "post_group_with_valid_access_token");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid access_token is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=5)
	public void create_group_without_passing_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_group_id", "To validate whether user is create group through post group api without passing group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_group_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_id parameter is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when group_id parameter is not passed while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning success when group_id parameter is not passed while creating group using POST group API.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=6)
	public void create_group_without_passing_group_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_group_name", "To validate whether user is create group through post group api without passing group_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_group_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_name parameter is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when group_name parameter is not passed while creating group using POST group API.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: group_name", "Invalid message value is returned in response when group_name parameter is not passed while creating group using POST group API.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when group_name parameter is not passed while creating group using POST group API.");
		}
	}	
	
	@Test(priority=7)
	public void create_group_with_blank_group_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_group_name", "To validate whether user is create group through post group api with blank group_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_group_name");
		String[] group_names = test_data.get(1).split(",");
		for(String group_name:group_names){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", group_name);
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when blank('"+group_name+"') group_name is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);
				JSONObject response_data = (JSONObject) api_response.get(0);
				Assert.assertEquals(response_data.get("result"), "error", "API is returning success when blank('"+group_name+"') group_name is passed while creating group using POST group API. <b style='red'>Defect Reported: CT-16718</b>");
			}	
		}
	}
	
	@Test(priority=8)
	public void create_group_with_valid_group_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_group_name", "To validate whether user is create group through post group api with valid group_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_group_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid group_name is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject response_data = (JSONObject) api_response.get(0);
			Assert.assertEquals(response_data.get("result"), "success", "API is returning error when valid group_name is passed while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning success when valid group_name is passed while creating group using POST group API.");
			String data_value = response_data.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(response_data.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=9)
	public void create_group_with_long_group_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_long_group_name", "To validate whether user is create group through post group api with more than 35 characters group_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_long_group_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when more than 35 characters group_name is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "error", "API is returning success when more than 35 characters group_name is passed while creating group using POST group API. Defect Reported: CT-16440");
			test.log(LogStatus.PASS, "Check API is returning success when more than 35 characters group_name is passed while creating group using POST group API.");
			String data_value = result.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=10)
	public void create_group_without_group_ext_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_group_ext_id", "To validate whether user is create group through post group api without passing group_ext_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_group_ext_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_ext_id field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "success", "API is returning error when group_ext_id field is not passed while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning success when group_ext_id field is not passed while creating group using POST group API.");
			String data_value = result.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=11)
	public void create_group_with_blank_group_ext_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_group_ext_id", "To validate whether user is create group through post group api with blank group_ext_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_group_ext_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", test_data.get(2));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_ext_id field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "success", "API is returning error when group_ext_id field is not passed while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning success when group_ext_id field is not passed while creating group using POST group API.");
			String data_value = result.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=12)
	public void create_group_with_valid_group_ext_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_group_ext_id", "To validate whether user is create group through post group api with valid group_ext_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_group_ext_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		String group_ext_id = "automation_"+String.valueOf(r.nextInt(500));
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", group_ext_id);
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid group_ext_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "success", "API is returning error when valid group_ext_id is passed while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning success when valid group_ext_id is passed while creating group using POST group API.");
			String data_value = result.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=13) //Need to add validation currently it is not working
	public void create_group_with_duplicate_group_ext_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_duplicate_group_ext_id", "To validate whether user is create group through post group api with duplicate group_ext_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_duplicate_group_ext_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", test_data.get(2));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid group_ext_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "error", "API is returning success when valid group_ext_id is passed while creating group using POST group API. <b style='color:red'>Defect Reported: CT-16442</b>");
			test.log(LogStatus.PASS, "Check API is returning error when valid group_ext_id is passed while creating group using POST group API.");
			String data_value = result.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
	@Test(priority=14)
	public void create_group_with_64_digits_group_ext_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_64_digits_group_ext_id", "To validate whether user is create group through post group api with more than 64 characters group_ext_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_64_digits_group_ext_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		String group_ext_id = "automate_create_group_with_64_digits_group_ext_id_using_post_group_api"+String.valueOf(r.nextInt(500));
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", group_ext_id);
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when more than 64 characters is passed in group_ext_id while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);
			JSONObject result = (JSONObject) api_response.get(0);
			Assert.assertEquals(result.get("result"), "error", "API is returning success when more than 64 characters is passed in group_ext_id while creating group using POST group API.");
			test.log(LogStatus.PASS, "Check API is returning error when more than 64 characters is passed in group_ext_id while creating group using POST group API.");
			String data_value = result.get("data").toString();
			Assert.assertEquals(data_value, "Failed to insert group record - error: value too long for type character varying(64)", "Proper validation is not displayed when more than 64 characters is passed in group_ext_id.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when more than 64 characters is passed in group_ext_id.");
			Assert.assertEquals(result.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}		
	
    @Test(priority=15)
	public void create_group_without_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_group_parent_id", "To validate whether user is create group through post group api without passing group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+ "<b style='color:red'>Defect Reported: CT-17007</b>");
		test.log(LogStatus.PASS, "Check status code when group_parent_id field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when group_parent_id field is not passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when group_parent_id field is not passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when group_parent_id field is not passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when group_parent_id field is not passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when group_parent_id field is not passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when group_parent_id field is not passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: group_parent_id", "Invalid message value is returned in response when group_parent_id field is not passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when group_parent_id field is not passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "group_parent_id", "Invalid path value is displayed when group_parent_id field is not passed while creating group.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique ID for the group that would be a parent to the current group");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when group_parent_id field is not passed while creating group.");
		}
	}	
	
    @Test(priority=16)
	public void create_group_with_blank_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_group_parent_id", "To validate whether user is create group through post group api with blank group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", "");
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_parent_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank group_parent_id is passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank group_parent_id is passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank group_parent_id is passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when blank group_parent_id is passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when blank group_parent_id is passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank group_parent_id is passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank group_parent_id is passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank group_parent_id is passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "group_parent_id", "Invalid path value is displayed when blank group_parent_id is passed while creating group.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique ID for the group that would be a parent to the current group");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank group_parent_id is passed while creating group.");
		}
	}
	
    @Test(priority=17)
	public void create_group_with_invalid_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_group_parent_id", "To validate whether user is create group through post group api with invalid group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		String[] parent_ids= test_data.get(3).split(",");
		for(String parent_id:parent_ids){
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", parent_id);
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+parent_id+") group_parent_id is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+parent_id+") group_parent_id is passed while creating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				if(parent_id.equals("abc"))
					Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				else if(parent_id.equals("!@#$"))
					Assert.assertEquals(sub_error_path.get(0), "1", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				else if(parent_id.equals("abc123"))
					Assert.assertEquals(sub_error_path.get(0), "2", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				else if(parent_id.equals("123"))
					Assert.assertEquals(sub_error_path.get(0), "4", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				else
					Assert.assertEquals(sub_error_path.get(0), "3", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				Assert.assertEquals(sub_error_path.get(1), "group_parent_id", "Invalid path value is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The unique ID for the group that would be a parent to the current group");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+parent_id+") group_parent_id is passed while creating group.");
			}
		}
	}	
	
    @Test(priority=18)
	public void create_group_with_valid_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_group_parent_id", "To validate whether user is create group through post group api with valid group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_group_parent_id");
		Random r = new Random();
		r.nextInt(500);
		String[] group_ids = test_data.get(3).split(",");
		for(String group_parent_id: group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			int parent_id = Integer.parseInt(group_parent_id);
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", parent_id);
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid group_parent_id("+(parent_id==8?"agency level":"company level")+" group) is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid group_parent_id("+(parent_id==8?"agency level":"company level")+" group) is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid group_parent_id("+(parent_id==8?"agency level":"company level")+" group) is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=19)
	public void create_group_with_non_existing_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_non_existing_group_parent_id", "To validate whether user is create group through post group api with non existing group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_non_existing_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || !(response.getStatusLine().getStatusCode() == 502) || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase() + " <b style='color:red'>Defect Reported: CT-17007</b>");
		test.log(LogStatus.PASS, "Check status code when valid group_parent_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject) api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid group_parent_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid group_parent_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=20)
	public void create_group_with_unauthorized_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_unauthorized_group_parent_id", "To validate whether user is create group through post group api with unauthorized group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_unauthorized_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || !(response.getStatusLine().getStatusCode() == 502) || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase() + " <b style='color:red'>Defect Reported: CT-17007</b>");
		test.log(LogStatus.PASS, "Check status code when unauthorized group_parent_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when unauthorized group_parent_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when unauthorized group_parent_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=21)
	public void create_group_with_location_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_location_group_parent_id", "To validate whether user is create group through post group api with location group_parent_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_location_group_parent_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when location group_parent_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when location group_parent_id is passed. <b style='color:red'>Defect Reported: CT-16451</b>");
			test.log(LogStatus.PASS, "Check API is returning error when location group_parent_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=22)
	public void create_group_without_passing_group_status() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_group_status", "To validate whether user is create group through post group api without passing group_status.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_group_status");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_status field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when group_status field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when group_status field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}		
	
    @Test(priority=23)
	public void create_group_with_blank_group_status() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_group_status", "To validate whether user is create group through post group api with blank group_status.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_group_status");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", "");
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_status is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank group_status is passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank group_status is passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank group_status is passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when blank group_status is passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when blank group_status is passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when blank group_status is passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: ", "Invalid message value is returned in response when blank group_status is passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank group_status is passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "group_status", "Invalid path value is displayed when blank group_status is passed while creating group.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank group_status is passed while creating group.");
		}
	}
	
    @Test(priority=24)
	public void create_group_with_invalid_group_status() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_group_status", "To validate whether user is create group through post group api with invalid group_status.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_group_status");
		String [] group_status = test_data.get(5).split(",");
		for(String grp_status:group_status){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", grp_status);
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+grp_status+") group_status is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "No enum match for: "+grp_status, "Invalid message value is returned in response when invalid("+grp_status+") group_status is passed while creating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+grp_status+") group_status is passed while creating group.");
				Assert.assertEquals(sub_error_path.get(1), "group_status", "Invalid path value is displayed when invalid("+grp_status+") group_status is passed while creating group.");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+grp_status+") group_status is passed while creating group.");
			}
		}
	}
	
    @Test(priority=25)
	public void create_group_with_deleted_group_status() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_deleted_group_status", "To validate whether user is create group through post group api with deleted group_status.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_deleted_group_status");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when deleted group_status is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when deleted group_status is passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when deleted group_status is passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when deleted group_status is passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when deleted group_status is passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when deleted group_status is passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when deleted group_status is passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: deleted", "Invalid message value is returned in response when deleted group_status is passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when deleted group_status is passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "group_status", "Invalid path value is displayed when deleted group_status is passed while creating group.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when deleted group_status is passed while creating group.");
		}
	}
	
    @Test(priority=26)
	public void create_group_with_valid_group_status() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_group_status", "To validate whether user is create group through post group api with valid group_status.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_group_status");
		String[] group_status = test_data.get(5).split(",");
		for(String grp_status: group_status){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", grp_status);
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+grp_status+") group_status is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+grp_status+") group_status is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid("+grp_status+") group_status is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response when valid("+grp_status+") group_status is passed.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}	
		}
	}	
	
    @Test(priority=27)
	public void create_group_without_passing_address() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_address", "To validate whether user is create group through post group api without passing address.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_address");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when address field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when address field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when address field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=28)
	public void create_group_with_blank_address() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_address", "To validate whether user is create group through post group api with blank address.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_address");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", "");
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank address field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank address field is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when blank address field is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}	
	

    @Test(priority=29)
	public void create_group_with_valid_address() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_address", "To validate whether user is create group through post group api with valid address.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_address");
		Random r = new Random();
		r.nextInt(500);
		String[] addresses = test_data.get(7).split(",");
		for(String address:addresses){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", address);
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+address+") address field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+address+") address field is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid("+address+") address field is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=30)
	public void create_group_without_passing_city() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_city", "To validate whether user is create group through post group api without passing city.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_city");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when city field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when city field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when city field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=31)
	public void create_group_with_blank_city() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_city", "To validate whether user is create group through post group api with blank city.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_city");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", "");
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank city field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank city field is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when blank city field is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}	
	

    @Test(priority=32)
	public void create_group_with_valid_city() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_city", "To validate whether user is create group through post group api with valid city.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_city");
		Random r = new Random();
		r.nextInt(500);
		String[] cities = test_data.get(8).split(",");
		for(String city:cities){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", city);
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+city+") city field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+city+") city field is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid("+city+") city field is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=33)
	public void create_group_without_passing_state() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_passing_state", "To validate whether user is create group through post group api without passing state.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_passing_state");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when state field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when state field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when state field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=34)
	public void create_group_with_blank_state() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_state", "To validate whether user is create group through post group api with blank state.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_state");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank state field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank state field is passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank state field is passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank state field is passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when blank state field is passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when blank state field is passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when blank state field is passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "String is too short (0 chars), minimum 2", "Invalid message value is returned in response when blank state field is passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank state field is passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "state", "Invalid path value is displayed when blank state field is passed while creating group.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The 2 letter abbrievation of the state or province for the group");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank state field is passed while creating group.");
		}
	}
	
    @Test(priority=35)
	public void create_group_with_3_or_more_char_state() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_3_or_more_char_state", "To validate whether user is create group through post group api with 3 or more characters in state value.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_3_or_more_char_state");
		String[] states = test_data.get(9).split(",");
		for(String state:states){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", state);
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when 3 or more characters("+state+") in state value is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "MAX_LENGTH", "Invalid code value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "String is too long ("+state.length()+" chars), maximum 2", "Invalid message value is returned in response when 3 or more characters("+state+") is passed in state value while creating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when 3 or more characters("+state+") is passed in state value while creating group.");
				Assert.assertEquals(sub_error_path.get(1), "state", "Invalid path value is displayed when 3 or more characters("+state+") is passed in state value while creating group.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The 2 letter abbrievation of the state or province for the group");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when 3 or more characters is passed in state value while creating group.");
			}
		}
	}	
	

    @Test(priority=36)
	public void create_group_with_invalid_state() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_state", "To validate whether user is create group through post group api with invalid state.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_state");
		String[] states = test_data.get(9).split(",");
		for(String state:states){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", state);
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+state+") state field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when invalid("+state+") state is passed. <b style='color:red'>Defect Reported: CT-16473</b>");
				test.log(LogStatus.PASS, "Check API is returning error when invalid("+state+") state is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=37)
	public void create_group_with_valid_state() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_state", "To validate whether user is create group through post group api with valid state.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_state");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid state field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid state field is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid state field is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}	
	
    @Test(priority=38)
	public void create_group_without_zip() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_zip", "To validate whether user is create group through post group api without passing zip field.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_zip");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when zip field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when zip field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when zip field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=39)
	public void create_group_with_blank_zip() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_zip", "To validate whether user is create group through post group api with blank zip.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_zip");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", "");
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank zip field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank zip field is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when blank zip field is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=40)
	public void create_group_with_invalid_zip() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_zip", "To validate whether user is create group through post group api with invalid zip.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_zip");
		String[] zip_values = test_data.get(10).split(",");
		for(String zip:zip_values){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", zip);
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			System.out.println("Request: "+json_obj);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+zip+") zip field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when invalid("+zip+") zip field is passed. <b style='color:red'>Defect Reported: CT-16443</b>");
				test.log(LogStatus.PASS, "Check API is returning error when invalid("+zip+") zip field is passed.");
			}
		}
	}
	
    @Test(priority=41)
	public void create_group_with_more_or_less_than_5_digits_zip() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_more_or_less_than_5_digits_zip", "To validate whether user is create group through post group api with more or less than 5 digits zip.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_more_or_less_than_5_digits_zip");
		String[] zip_values = test_data.get(10).split(",");
		for(String zip:zip_values){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", zip);
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+zip+") zip field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when "+(zip.equals("1001")?"less than ":"greater than ")+"5 digits("+zip+") zip field is passed. <b style='color:red'>Defect Reported: CT-16443</b>");
				test.log(LogStatus.PASS, "Check API is returning error when "+(zip.equals("1001")?"less than ":"greater than ")+"5 digits("+zip+") zip field is passed.");
			}
		}
	}
	
    @Test(priority=42)
	public void create_group_with_valid_zip() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_zip", "To validate whether user is create group through post group api with valid zip.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_zip");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid zip field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid zip field is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid zip field is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=43)
	public void create_group_without_phone_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_phone_number", "To validate whether user is create group through post group api without passing phone_number field.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_phone_number");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when phone_number field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when phone_number field is not passed.");
			test.log(LogStatus.PASS, "Check API is returning success when phone_number field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=44)
	public void create_group_with_blank_phone_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_phone_number", "To validate whether user is create group through post group api with blank phone_number.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_phone_number");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		String phone_number = "";
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", phone_number);
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank phone_number field is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank phone_number value is passed while creating group.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank phone_number value is passed while creating group.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank phone_number value is passed while creating group.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when blank phone_number value is passed while creating group.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when blank phone_number value is passed while creating group.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when blank phone_number value is passed while creating group.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "String is too short ("+phone_number.length()+" chars), minimum 10", "Invalid message value is returned in response when blank phone_number value is passed while creating group.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank phone_number value is passed while creating group.");
			Assert.assertEquals(sub_error_path.get(1), "phone_number", "Invalid path value is displayed when blank phone_number value is passed while creating group.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The phone number for the group");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank phone_number value is passed while creating group.");
		}
	}
	
    @Test(priority=45)
	public void create_group_with_invalid_phone_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_phone_number", "To validate whether user is create group through post group api with invalid phone_number.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_phone_number");
		String[] phone_numbers = test_data.get(11).split(",");
		for(String phone_number:phone_numbers){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", phone_number);
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+phone_number+") phone_number field is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when invalid("+phone_number+") phone_number is passed. <b style='color:red'>Defect Reported: CT-17564</b>");
				test.log(LogStatus.PASS, "Check API is returning error when invalid("+phone_number+") phone_number is passed.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=46)
	public void create_group_with_less_or_more_10_dig_phone_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_less_or_more_10_dig_phone_number", "To validate whether user is create group through post group api with less or more than 10 digits phone_number.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_less_or_more_10_dig_phone_number");
		String[] phone_numbers = test_data.get(11).split(",");
		for(String phone_number:phone_numbers){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			Random r = new Random();
			r.nextInt(500);
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", phone_number);
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank phone_number value is passed while creating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				if(phone_number.length()<10)
					Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				else if(phone_number.length()<10)
					Assert.assertEquals(sub_error_code, "MAX_LENGTH", "Invalid code value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				if(phone_number.length()<10)
					Assert.assertEquals(sub_error_message, "String is too short ("+phone_number.length()+" chars), minimum 10", "Invalid message value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				else if(phone_number.length()<10)
					Assert.assertEquals(sub_error_message, "String is too long ("+phone_number.length()+" chars), maximum 10", "Invalid message value is returned in response when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				Assert.assertEquals(sub_error_path.get(1), "phone_number", "Invalid path value is displayed when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The phone number for the group");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+(phone_number.length()<10? "less than ":(phone_number.length()>10?"greater than ":""))+"phone_number is passed while creating group.");
			}
		}
	}
	
    @Test(priority=47)
	public void create_group_with_valid_phone_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_phone_number", "To validate whether user is create group through post group api with valid phone_number.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_phone_number");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid phone_number is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid phone_number is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid phone_number is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=48) 
	public void create_group_without_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_industry_id", "To validate whether user is create group through post group api without passing industry_id field.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_industry_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when industry_id field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when industry_id field is not passed. <b style='color:red'>Defect Reported: CT-17011</b>");
			test.log(LogStatus.PASS, "Check API is returning error when industry_id field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=49) // Need to add validation
	public void create_group_with_blank_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_industry_id", "To validate whether user is create group through post group api with blank industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_industry_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", "");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank industry_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank industry_id is passed while creating group using POST group API.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank industry_id is passed while creating group using POST group API.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank industry_id is passed while creating group using POST group API.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when blank industry_id is passed while creating group using POST group API.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when blank industry_id is passed while creating group using POST group API.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/group");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank industry_id is passed while creating group using POST group API.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank industry_id is passed while creating group using POST group API.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank industry_id is passed while creating group using POST group API.");
			Assert.assertEquals(sub_error_path.get(1), "industry_id", "Invalid path value is displayed when blank industry_id is passed while creating group using POST group API.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique identifier for the industry selected");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank industry_id is passed while creating group using POST group API.");
		}
	}	
	
    @Test(priority=50)
	public void create_group_with_invalid_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_industry_id", "To validate whether user is create group through post group api with invalid industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_industry_id");
		String[] industry_ids = test_data.get(12).split(",");
		for(String industry_id:industry_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			Random r = new Random();
			r.nextInt(500);
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", industry_id);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+industry_id+") industry_id is passed while creating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+industry_id+") industry_id value is passed while creating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+industry_id+") industry_id value is passed while creating group.");
				Assert.assertEquals(sub_error_path.get(1), "industry_id", "Invalid path value is displayed when invalid("+industry_id+") industry_id value is passed while creating group.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The unique identifier for the industry selected");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+industry_id+") industry_id value is passed while creating group.");
			}
		}
	}
	
    @Test(priority=51) // Need to add validation
	public void create_group_with_negative_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_negative_industry_id", "To validate whether user is create group through post group api with negative industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_negative_industry_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when negative industry_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when negative industry_id is passed. <b style='color:red'>Defect Reported: CT-16459</b>");
			test.log(LogStatus.PASS, "Check API is returning error when negative industry_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=52) // Need to add validation
	public void create_group_with_non_existing_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_non_existing_industry_id", "To validate whether user is create group through post group api with non existing industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_non_existing_industry_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing industry_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non existing industry_id is passed. <b style='color:red'>Defect Reported: CT-16459</b>");
			test.log(LogStatus.PASS, "Check API is returning error when non existing industry_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}	
	
    @Test(priority=53) // Need to add validation
	public void create_group_with_valid_industry_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_industry_id", "To validate whether user is create group through post group api with valid industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_industry_id");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid industry_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid industry_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid industry_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=54) 
	public void create_group_without_industry_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_without_industry_name", "To validate whether user is create group through post group api without passing industry_name field.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_without_industry_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when industry_name field is not passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when industry_name field is not passed. <b style='color:red'>Defect Reported: CT-17011</b>");
			test.log(LogStatus.PASS, "Check API is returning error when industry_name field is not passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=55) // Need to add validation
	public void create_group_with_blank_industry_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_blank_industry_name", "To validate whether user is create group through post group api with blank industry_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_blank_industry_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		json_obj.put("industry_name", "");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank industry_name is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank industry_name is passed. <b style='color:red'>Defect Reported: CT-17011</b>");
			test.log(LogStatus.PASS, "Check API is returning error when blank industry_name is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=56) // Need to add validation
	public void create_group_with_invalid_industry_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_invalid_industry_name", "To validate whether user is create group through post group api with invalid industry_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_invalid_industry_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		json_obj.put("industry_name", test_data.get(13));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when invalid industry_name is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when invalid industry_name is passed. <b style='color:red'>Defect Reported: CT-16459</b>");
			test.log(LogStatus.PASS, "Check API is returning error when invalid industry_name is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=57) // Need to add validation
	public void create_group_with_non_matching_industry_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_non_matching_industry_name", "To validate whether user is create group through post group api with non matching industry_name with industry_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_non_matching_industry_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		json_obj.put("industry_name", test_data.get(13));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non matching industry_name with industry_id is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non matching industry_name with industry_id industry_name is passed. <b style='color:red'>Defect Reported: CT-16459</b>");
			test.log(LogStatus.PASS, "Check API is returning error when non matching industry_name with industry_id industry_name is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=58) 
	public void create_group_with_valid_industry_name() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_valid_industry_name", "To validate whether user is create group through post group api with valid industry_name.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_valid_industry_name");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random r = new Random();
		r.nextInt(500);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		json_obj.put("industry_name", test_data.get(13));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid industry_name is passed while creating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid industry_name is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid industry_name is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=59)
	public void update_group_with_invalid_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_invalid_group_id", "To validate whether user is update group through post group api with invalid group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_invalid_group_id");
		String[] group_ids = test_data.get(14).split(",");
		for(String group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_id", group_id);
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+group_id+") group_id is passed while updating group using POST group API.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed while updating group.");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (groups): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "groups", "Invalid name value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/group");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed while updating group.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+group_id+") group_id is passed while updating group.");
				Assert.assertEquals(sub_error_path.get(1), "group_id", "Invalid path value is displayed when invalid("+group_id+") group_id is passed while updating group.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "Unique identifier for the group");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+group_id+") group_id is passed while updating group.");
			}
		}
	}
	
    @Test(priority=60)
	public void update_group_with_negative_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_negative_group_id", "To validate whether user is update group through post group api with negative group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_negative_group_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when negative("+group_id+") group_id is passed while updating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when negative group_id is passed. <b style='color:red'>Defect Reported: CT-17663</b>");
			test.log(LogStatus.PASS, "Check API is returning error when negative group_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=61)
	public void update_group_with_deleted_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_deleted_group_id", "To validate whether user is update group through post group api with deleted group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_deleted_group_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when deleted("+group_id+") group_id is passed while updating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when deleted group_id is passed. <b style='color:red'>Defect Reported: CT-17097</b>");
			test.log(LogStatus.PASS, "Check API is returning error when deleted group_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=62)
	public void update_group_with_non_existing_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_non_existing_group_id", "To validate whether user is update group through post group api with non existing group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_non_existing_group_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing("+group_id+") group_id is passed while updating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non existing group_id is passed. <b style='color:red'>Defect Reported: CT-17663</b>");
			test.log(LogStatus.PASS, "Check API is returning error when non existing group_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}
	
    @Test(priority=63)
	public void update_group_with_other_billing_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_other_billing_group_id", "To validate whether user is update group through post group api with other billing group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_other_billing_group_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", test_data.get(1));
		json_obj.put("group_ext_id", "");
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when other billing("+group_id+") group_id is passed while updating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when other billing group_id is passed. <b style='color:red'>Defect Reported: CT-17663</b>");
			test.log(LogStatus.PASS, "Check API is returning error when other billing group_id is passed.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
		}
	}	
	
    @Test(priority=64)
	public void update_group_with_valid_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_valid_group_id", "To validate whether user is update group through post group api with valid group_id.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_valid_group_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random rand = new Random();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", test_data.get(1)+String.valueOf(rand.nextInt(500)));
		json_obj.put("group_parent_id", Integer.parseInt(test_data.get(3)));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		String[] group_status = test_data.get(5).split(",");
		json_obj.put("group_status", group_status[rand.nextInt(group_status.length)]);
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		rand.nextInt(500);
		json_obj.put("address", test_data.get(7)+String.valueOf(rand.nextInt(500)));
		json_obj.put("city", test_data.get(8)+String.valueOf(rand.nextInt(500)));
		String[] states = test_data.get(9).split(",");
		json_obj.put("state", states[rand.nextInt(states.length)]);
		String[] zip = test_data.get(10).split(",");
		json_obj.put("zip", zip[rand.nextInt(zip.length)]);
		String[] phone_numbers = test_data.get(11).split(",");
		json_obj.put("phone_number", phone_numbers[rand.nextInt(phone_numbers.length)]);
		String[] industry_ids = test_data.get(12).split(",");
		json_obj.put("industry_id", Integer.parseInt(industry_ids[rand.nextInt(industry_ids.length)]));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid("+group_id+") group_id is passed while updating group using POST group API.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid group_id is passed while updating group.");
			test.log(LogStatus.PASS, "Check API is returning success when valid group_id is passed while updating group.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			Assert.assertEquals(data_value, String.valueOf(group_id), "Passed group is not returning in response. Seems like new group is being created.");
			test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			test.log(LogStatus.PASS, "To Check where user is able to update all fields using POST group API.");
		}
	}	
	
    @Test(priority=65)
	public void update_group_with_group_parent_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_group_parent_id", "To validate whether user is able to update the group_parent_id of any group through POST group api.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_group_parent_id");
		int group_id = Integer.parseInt(test_data.get(14));
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random rand = new Random();
		json_obj.put("group_id", group_id);
		json_obj.put("group_name", "UpdateGroupAPI_"+String.valueOf(rand.nextInt(500)));
		String[] group_parent_id = test_data.get(3).split(",");
		json_obj.put("group_parent_id", Integer.parseInt(group_parent_id[rand.nextInt(group_parent_id.length)]));
		json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
		String[] group_status = test_data.get(5).split(",");
		json_obj.put("group_status", group_status[rand.nextInt(group_status.length)]);
		json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
		rand.nextInt(500);
		json_obj.put("address", test_data.get(7)+String.valueOf(rand.nextInt(500)));
		json_obj.put("city", test_data.get(8)+String.valueOf(rand.nextInt(500)));
		String[] states = test_data.get(9).split(",");
		json_obj.put("state", states[rand.nextInt(states.length)]);
		String[] zip = test_data.get(10).split(",");
		json_obj.put("zip", zip[rand.nextInt(zip.length)]);
		String[] phone_numbers = test_data.get(11).split(",");
		json_obj.put("phone_number", phone_numbers[rand.nextInt(phone_numbers.length)]);
		String[] industry_ids = test_data.get(12).split(",");
		json_obj.put("industry_id", Integer.parseInt(industry_ids[rand.nextInt(industry_ids.length)]));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when user update the group_parent_id of any group through POST group api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response =(JSONArray) parser.parse(line);	
			JSONObject json_response = (JSONObject)api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when user is able to update the group_parent_id of any group through POST group api. <b style='color:red'>Defect Reported: CT-17099</b>");
			test.log(LogStatus.PASS, "Check API is returning error when user is able to update the group_parent_id of any group through POST group api.");
			String data_value = json_response.get("data").toString();
			HelperClass.multiple_assertnotEquals(data_value, "data");
			Assert.assertEquals(data_value, String.valueOf(group_id), "Passed group is not returning in response. Seems like new group is being created.");
			test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			test.log(LogStatus.PASS, "To Check where user is able to update all fields using POST group API.");
		}
	}
	
    @Test(priority=66)
	public void update_group_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_agency_admin_access_token", "To validate whether user is able to update the group using agency admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_agency_admin_access_token");
		String[] groups = test_data.get(14).split(",");
		String[] parent_groups = test_data.get(3).split(",");
		String[] group_names = test_data.get(1).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]), location_group = Integer.parseInt(groups[2]);
		int[] group_ids = {company_group, location_group, agency_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_id", group_id);
			if(group_id==agency_group){
				json_obj.put("group_name", group_names[0]);
				// json_obj.put("group_parent_id", null);
			}
			else if(group_id==company_group){
				json_obj.put("group_name", group_names[1]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[0]));
			}
			else{
				json_obj.put("group_name", group_names[2]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[1]));
			}
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", "active");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using agency admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. <b style='color:red'>Defect Reported: CT-17664</b>"+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using agency admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using agency admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using agency admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				Assert.assertEquals(data_value, String.valueOf(group_id), "Passed group is not returning in response. Seems like new group is being created.");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
				test.log(LogStatus.PASS, "To Check where user is able to update all fields using POST group API.");
			}
		}
	}
	
    @Test(priority=67)
	public void update_group_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_company_admin_access_token", "To validate whether user is able to update the group using company admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_company_admin_access_token");
		String access_token = HelperClass.get_oauth_token("company@admin.com", "lmc2demo");
		String[] groups = test_data.get(14).split(",");
		String[] parent_groups = test_data.get(3).split(",");
		String[] group_names = test_data.get(1).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]), location_group = Integer.parseInt(groups[2]);
		int[] group_ids = {company_group, location_group, agency_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_id", group_id);
			if(group_id==agency_group){
				json_obj.put("group_name", group_names[0]);
				json_obj.put("group_parent_id", null);
			}
			else if(group_id==company_group){
				json_obj.put("group_name", group_names[1]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[0]));
			}
			else{
				json_obj.put("group_name", group_names[2]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[1]));
			}
			json_obj.put("top_group_id", 8);
			json_obj.put("group_status", "active");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api. <b style='color:red'>Defect Reported: CT-17665</b> "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				Assert.assertEquals(data_value, String.valueOf(group_id), "Passed group is not returning in response. Seems like new group is being created.");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
				test.log(LogStatus.PASS, "To Check where user is able to update all fields using POST group API.");
			}
		}
	}
	
    @Test(priority=68)
	public void update_group_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("update_group_with_location_admin_access_token", "To validate whether user is able to update the group using company admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "update_group_with_location_admin_access_token");
		String access_token = HelperClass.get_oauth_token("location@admin.com", "lmc2demo");
		String[] groups = test_data.get(14).split(",");
		String[] parent_groups = test_data.get(3).split(",");
		String[] group_names = test_data.get(1).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]), location_group = Integer.parseInt(groups[2]);
		int[] group_ids = {location_group, company_group, agency_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_id", group_id);
			if(group_id==agency_group){
				json_obj.put("group_name", group_names[0]);
				json_obj.put("group_parent_id", null);
			}
			else if(group_id==company_group){
				json_obj.put("group_name", group_names[1]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[0]));
			}
			else{
				json_obj.put("group_name", group_names[2]);
				json_obj.put("group_parent_id", Integer.parseInt(parent_groups[1]));
			}
			json_obj.put("top_group_id", 8);
			json_obj.put("group_status", "active");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api. <b style='color:red'>Defect Reported: CT-17665</b>"+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());			test.log(LogStatus.PASS, "Check status code when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to update "+(group_id==agency_group?"agency":group_id==company_group?"company":"location")+" level group using company admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				Assert.assertEquals(data_value, String.valueOf(group_id), "Passed group is not returning in response. Seems like new group is being created.");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
				test.log(LogStatus.PASS, "To Check where user is able to update all fields using POST group API.");
			}
		}
	}
	
    @Test(priority=69)
	public void create_group_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_agency_admin_access_token", "To validate whether user is able to create a group using agency admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_agency_admin_access_token");
		String[] groups = test_data.get(3).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]);
		int[] group_ids = {agency_group, company_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			if(group_id==agency_group){
				json_obj.put("group_parent_id", agency_group);
			}
			else if(group_id==company_group){
				json_obj.put("group_parent_id", company_group);
			}
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. <b style='color:red'>Defect Reported: CT-17665</b>"+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=70)
	public void create_group_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_company_admin_access_token", "To validate whether user is able to create a group using company admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_company_admin_access_token");

		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] groups = test_data.get(3).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]);
		int[] group_ids = {company_group,agency_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			if(group_id==agency_group){
				json_obj.put("group_parent_id", agency_group);
			}
			else if(group_id==company_group){
				json_obj.put("group_parent_id", company_group);
			}
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to create group at "+(group_id==agency_group?"agency":"company")+" level using company admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed when user try to create group at "+(group_id==agency_group?"agency":"company")+" level. <b style='color:red'>Defect Reported: CT-17665</b>"+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using company admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				if(group_id==agency_group)
					Assert.assertEquals(result_data, "error", "API is returning success when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
				else
					Assert.assertEquals(result_data, "success", "API is returning error when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using agency admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}
	
    @Test(priority=71)
	public void create_group_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("create_group_with_location_admin_access_token", "To validate whether user is able to create a group using location admin access_token.");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "create_group_with_location_admin_access_token");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String[] groups = test_data.get(3).split(",");
		int agency_group = Integer.parseInt(groups[0]), company_group = Integer.parseInt(groups[1]);
		int[] group_ids = {company_group,agency_group};
		for(int group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("group_name", test_data.get(1));
			json_obj.put("group_ext_id", "");
			json_obj.put("top_group_id", Integer.parseInt(test_data.get(4)));
			json_obj.put("group_status", test_data.get(5));
			json_obj.put("billing_id", Integer.parseInt(test_data.get(6)));
			json_obj.put("address", test_data.get(7));
			json_obj.put("city", test_data.get(8));
			json_obj.put("state", test_data.get(9));
			json_obj.put("zip", test_data.get(10));
			json_obj.put("phone_number", test_data.get(11));
			json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
			if(group_id==agency_group){
				json_obj.put("group_parent_id", agency_group);
			}
			else if(group_id==company_group){
				json_obj.put("group_parent_id", company_group);
			}
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, dni_array);
			test.log(LogStatus.PASS, "Check whether user is able to create group at "+(group_id==agency_group?"agency":"company")+" level using location admin access_token through POST group api.");
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed when user try to create group at "+(group_id==agency_group?"agency":"company")+" level. <b style='color:red'>Defect Reported: CT-17665</b>"+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using location admin access_token through POST group api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray api_response =(JSONArray) parser.parse(line);	
				JSONObject json_response = (JSONObject)api_response.get(0);
				String result_data = json_response.get("result").toString();
				if(group_id==agency_group)
					Assert.assertEquals(result_data, "error", "API is returning success when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using location admin access_token through POST group api.");
				else
					Assert.assertEquals(result_data, "success", "API is returning error when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using location admin access_token through POST group api.");
				test.log(LogStatus.PASS, "Check API is returning success when when user try to create group at "+(group_id==agency_group?"agency":"company")+" level using location admin access_token through POST group api.");
				String data_value = json_response.get("data").toString();
				HelperClass.multiple_assertnotEquals(data_value, "data");
				test.log(LogStatus.PASS, "Check passed group_id is returned in response.");
				Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
				test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			}
		}
	}	
}
