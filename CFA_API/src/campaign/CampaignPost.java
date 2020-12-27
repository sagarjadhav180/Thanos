package campaign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
public class CampaignPost extends BaseClass{
	String class_name = "CampaignPost";
	ArrayList<String> test_data;
	
	@Test(priority=0)
	public void campaign_post_without_any_params() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without any parameters
		test = extent.startTest("campaign_post_without_any_params", "To validate whether user is able create/edit campaign through campaign post api with no params");
		test.assignCategory("CFA POST /campaign API");
		JSONArray array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign post api without any parameters");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed with campaign post api method.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed with campaign post api method.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when no parameter is passed with campaign post api method.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			String[] required_fields = {"campaign_start_date", "campaign_owner_user_id", "group_id", "campaign_name"};
			for(int i=0; i<required_fields.length; i++){
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(i);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter is passed with campaign post api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Missing required property: "+required_fields[i], "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when no parameter is passed with campaign post api method.");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when no parameter is passed with campaign post api method.");
			}
		}   
	}
	
	@Test(priority=1)
	public void create_campaign_without_campaign_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_name when campaign_id is not defined
		test = extent.startTest("create_campaign_without_campaign_name", "To validate whether user is able create campaign through campaign post api without campaign_name parameter");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_ext_id", "");
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", test_data.get(8));
		json.put("campaign_modified", test_data.get(9));
		json.put("campaign_start_date", test_data.get(4));
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign post api without campaign_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameter is passed with campaign post api method.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameter is passed with campaign post api method.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when no parameter is passed with campaign post api method.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter is passed with campaign post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: campaign_name", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when no parameter is passed with campaign post api method.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when no parameter is passed with campaign post api method.");
		}   
	}
	
	@Test(priority=2)
	public void create_campaign_with_blank_campaign_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_name when campaign_name is not defined
		test = extent.startTest("create_campaign_with_blank_campaign_name", "To validate whether user is able create campaign through campaign post api with blank campaign_name parameter");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", "");
		json.put("campaign_ext_id", "");
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", test_data.get(8));
		json.put("campaign_modified", test_data.get(9));
		json.put("campaign_start_date", test_data.get(4));
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign post api with blank campaign_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_name is passed with campaign post api method.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_name is passed with campaign post api method.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_name is passed with campaign post api method.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_name is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_name is passed with campaign post api method.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when no parameter is passed with campaign post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "String is too short (0 chars), minimum 3", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			JSONArray path = new JSONArray();
			path.add("0");
			path.add("campaign_name");
			Assert.assertEquals(sub_error_path, path, "Invalid path value is displayed when no parameter is passed with campaign post api method.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "Client defined name for the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when no parameter is passed with campaign post api method.");
		}   
	}	
	
	@Test(priority=3)
	public void create_campaign_with_small_campaign_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with less than 3 chars campaign_name when campaign_name is not defined
		test = extent.startTest("create_campaign_with_small_campaign_name", "To validate whether user is able create campaign through campaign post api with less than 3 characters campaign_name.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_small_campaign_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", test_data.get(8));
		json.put("campaign_modified", test_data.get(9));
		json.put("campaign_start_date", test_data.get(4));
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign post api with less than 3 characters campaign_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MIN_LENGTH", "Invalid code value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "String is too short ("+campaign_name.length()+" chars), minimum 3", "Invalid message value is returned in response when less than 3 characters campaign_name is passed with campaign post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			JSONArray path = new JSONArray();
			path.add("0");
			path.add("campaign_name");
			Assert.assertEquals(sub_error_path, path, "Invalid path value is displayed when less than 3 characters campaign_name is passed with campaign post api method.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Client defined name for the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when less than 3 characters campaign_name is passed with campaign post api method.");
		}   
	}	
	
	@Test(priority=4)
	public void create_campaign_with_3_char_campaign_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with less than 3 chars campaign_name when campaign_name is not defined
		test = extent.startTest("create_campaign_with_3_chars_campaign_name", "To validate whether user is able create campaign through campaign post api with 3 characters campaign_name.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_3_chars_campaign_name");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", test_data.get(8));
		json.put("campaign_modified", test_data.get(9));
		json.put("campaign_start_date", test_data.get(4));
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign post api with 3 characters campaign_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject) json_array.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when 3 characters campaign_name is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when 3 characters campaign_name is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when 3 characters campaign_name is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
			test.log(LogStatus.PASS, "entry_count value is correct when 3 characters value is passed for campaign_name");
		}   
	}	
	
	@Test(priority=5)
	public void create_campaign_with_valid_campaign_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with less than valid campaign_name when campaign_name is not defined
		test = extent.startTest("create_campaign_with_valid_campaign_name", "To validate whether user is able create campaign through campaign post api with valid campaign_name.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_name");
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_names = test_data.get(1).split(",");
		for(String campaign_name: campaign_names){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_status", test_data.get(3));
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign post api with 3 characters campaign_name");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+campaign_name+") campaign_name is passed while creating campaign.");
				test.log(LogStatus.PASS, "API is returning success when valid("+campaign_name+") campaign_name is passed while creating campaign.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when valid("+campaign_name+") campaign_name is passed while creating campaign.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
				test.log(LogStatus.PASS, "entry_count value is correct when valid("+campaign_name+") value is passed for campaign_name");
			}   
		}
	}		
	
	@Test(priority=6)
	public void create_campaign_with_blank_campaign_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_ext_id while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_ext_id", "To validate whether user is able create campaign through campaign post api with blank campaign_ext_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_ext_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_ext_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank campaign_ext_id is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when blank campaign_ext_id is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when blank campaign_ext_id is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
			test.log(LogStatus.PASS, "entry_count value is correct when blank campaign_ext_id is passed while creating campaign");
		}   
	}	
	
	@Test(priority=7)
	public void create_campaign_without_campaign_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_ext_id while creating campaign
		test = extent.startTest("create_campaign_without_campaign_ext_id", "To validate whether user is able create campaign through campaign post api without campaign_ext_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_ext_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_ext_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when campaign_ext_id is not passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when campaign_ext_id is not passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when campaign_ext_id is not passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
			test.log(LogStatus.PASS, "entry_count value is correct when campaign_ext_id is not passed while creating campaign");
		}   
	}		
	
	@Test(priority=8)
	public void create_campaign_with_valid_campaign_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_ext_id while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_ext_id", "To validate whether user is able create campaign through campaign post api with valid campaign_ext_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_ext_id");
		String[] campaign_ext_ids = test_data.get(2).split(",");
		for(String campaign_ext_id: campaign_ext_ids){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			String campaign_name = test_data.get(1);
			Date now = new Date();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
			String today_formated_date = dateFormatter.format(now).toString();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", campaign_ext_id);
			json.put("campaign_status", test_data.get(3));
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with valid campaign_ext_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+campaign_ext_id+") campaign_ext_id is passed while creating campaign.");
				test.log(LogStatus.PASS, "API is returning success when valid("+campaign_ext_id+") campaign_ext_id is passed while creating campaign.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when valid("+campaign_ext_id+") campaign_ext_id is passed while creating campaign.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
				test.log(LogStatus.PASS, "entry_count value is correct when valid("+campaign_ext_id+") campaign_ext_id is passed while creating campaign");
			}   
		}
	}
	
	@Test(priority=9)
	public void create_campaign_with_duplicate_campaign_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with duplicate campaign_ext_id while creating campaign
		test = extent.startTest("create_campaign_with_duplicate_campaign_ext_id", "To validate whether user is able create campaign through campaign post api with duplicate campaign_ext_id.\nDefect Reported: CT-12819");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_duplicate_campaign_ext_id");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		String campaign_ext_id = test_data.get(2);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", campaign_ext_id);
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with valid campaign_ext_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when duplicate campaign_ext_id is passed while creating campaign. Defect Reported: CT-12819");
			test.log(LogStatus.PASS, "API is returning success when blank campaign_ext_id is passed while creating campaign.");
		}   
	}
	
	@Test(priority=10)
	public void create_campaign_without_campaign_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_status while creating campaign
		test = extent.startTest("create_campaign_without_campaign_status", "To validate whether user is able create campaign through campaign post api without campaign_status.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_status");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. Defect Reported: CT-17727. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without passing campaign_status");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when campaign_status is not passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when campaign_status is not passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when campaign_status is not passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
			test.log(LogStatus.PASS, "entry_count value is correct when campaign_status is not passed while creating campaign");
		}   
	}	
	
	@Test(priority=11)
	public void create_campaign_with_blank_campaign_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_status while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_status", "To validate whether user is able create campaign through campaign post api without campaign_status.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_status");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_status", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_status");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameter is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: ", "Invalid message value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_status is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_status", "Invalid path value is displayed when blank campaign_status is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The current status of the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_status is passed through campaign post api while creating campaign.");
		}   
	}		
	
	@Test(priority=12)
	public void create_campaign_with_invalid_campaign_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_status while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_status", "To validate whether user is able create campaign through campaign post api with invalid campaign_status.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_status");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_status = test_data.get(3).split(",");
		for(String camp_stat:campaign_status){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_status", camp_stat);
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_status is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid campaign_status is passed with campaign post api while creating campaign.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid campaign_status is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when invalid campaign_status is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "No enum match for: "+camp_stat, "Invalid message value is returned in response when invalid campaign_status is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid campaign_status is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_status", "Invalid path value is displayed when invalid campaign_status is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The current status of the campaign");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid campaign_status is passed through campaign post api while creating campaign.");
			}
		}	
	}	
	
	@Test(priority=13)
	public void create_campaign_with_valid_campaign_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_status while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_status", "To validate whether user is able create campaign through campaign post api with valid campaign_status.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_status");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_status = test_data.get(3).split(",");
		for(String camp_stat:campaign_status){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_status", camp_stat);
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without passing campaign_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+camp_stat+") campaign_status is passed while creating campaign.");
				test.log(LogStatus.PASS, "API is returning success when valid("+camp_stat+") campaign_status is passed while creating campaign.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when valid("+camp_stat+") campaign_status is passed while creating campaign.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when campaign_status("+camp_stat+") is passed.");
				test.log(LogStatus.PASS, "entry_count value is correct when campaign_status is not passed while creating campaign");
			}   
		}	
	}
	
	@Test(priority=14)
	public void create_campaign_with_deleted_campaign_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with deleted campaign_status while creating campaign
		test = extent.startTest("create_campaign_with_deleted_campaign_status", "To validate whether user is able create campaign through campaign post api with deleted campaign_status.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_deleted_campaign_status");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_status", campaign_status);
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with deleted campaign_status");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when deleted campaign_status is passed with campaign post api while creating campaign.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: "+campaign_status, "Invalid message value is returned in response when deleted campaign_status is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when deleted campaign_status is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_status", "Invalid path value is displayed when deleted campaign_status is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The current status of the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when deleted campaign_status is passed through campaign post api while creating campaign.");
		}	
	}	
	
	@Test(priority=15)
	public void create_campaign_with_active_campaign_status_and_future_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with active campaign_status and future start_date while creating campaign
		test = extent.startTest("create_campaign_with_active_campaign_status_and_future_start_date", "To validate whether user is able create campaign through campaign post api with active campaign_status and future date in campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_active_campaign_status_and_future_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, +10);
        String camp_start_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", camp_start_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with active campaign_status and future campaign_start_date.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when active campaign_status is passed with future campaign_start_date.");
			test.log(LogStatus.PASS, "API is returning success when active campaign_status is passed with future campaign_start_date.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when active campaign_status is passed with future campaign_start_date.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when active campaign_status is passed with future campaign_start_date.");
			test.log(LogStatus.PASS, "entry_count value is correct when active campaign_status is passed with future campaign_start_date.");
		}   
	}
	
	@Test(priority=13)
	public void create_campaign_with_active_campaign_status_and_future_start_time() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with active campaign_status and current start_date but future start time while creating campaign
		test = extent.startTest("create_campaign_with_active_campaign_status_and_future_start_time", "To validate whether user is able create campaign through campaign post api with active campaign_status and current date future time  in campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_active_campaign_status_and_future_start_time");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, +3);
        String camp_start_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", camp_start_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with active campaign_status and current date future time campaign_start_date.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when active campaign_status is passed with current date future time campaign_start_date.");
			test.log(LogStatus.PASS, "API is returning success when active campaign_status is passed with current date future time campaign_start_date.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when active campaign_status is passed with current date future time campaign_start_date.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when active campaign_status is passed with current date future time campaign_start_date.");
			test.log(LogStatus.PASS, "entry_count value is correct when active campaign_status is passed with current date future time campaign_start_date.");
		}   
	}	
	
	@Test(priority=16)
	public void create_campaign_without_campaign_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_created while creating campaign
		test = extent.startTest("create_campaign_without_campaign_created", "To validate whether user is able create campaign through campaign post api without campaign_created.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_created");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when campaign_status is not passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when campaign_created is not passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when campaign_created is not passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when campaign_created is not passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when campaign_created is not passed while creating campaign");
		}	
	}
	
	@Test(priority=17)
	public void create_campaign_with_blank_campaign_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_created while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_created", "To validate whether user is able create campaign through campaign post api without campaign_created.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_created");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", "");
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_created is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: ", "Invalid message value is returned in response when blank campaign_created is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_created is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_created", "Invalid path value is displayed when blank campaign_created is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The date and time of when the campaign record was created");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_created is passed through campaign post api while creating campaign.");
		}   
	}
	
	@Test(priority=18)
	public void create_campaign_with_invalid_campaign_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_created while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_created", "To validate whether user is able create campaign through campaign post api with invalid campaign_created.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_created");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_created = test_data.get(8).split(",");
		for(String camp_created:campaign_created){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", camp_created);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with invalid campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid campaign_created is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_created, "Invalid message value is returned in response when invalid campaign_created is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid campaign_created is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_created", "Invalid path value is displayed when invalid campaign_created is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time of when the campaign record was created");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid campaign_created is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=19)
	public void create_campaign_with_unsupported_formatted_campaign_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with unsupported formatted campaign_created while creating campaign
		test = extent.startTest("create_campaign_with_unsupported_formatted_campaign_created", "To validate whether user is able create campaign through campaign post api with unsupported formatted campaign_created date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_unsupported_formatted_campaign_created");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_created = test_data.get(8).split(",");
		for(String camp_created:campaign_created){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", camp_created);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with unsupported formatted campaign_created date.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when unsupported formatted campaign_created is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_created, "Invalid message value is returned in response when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_created", "Invalid path value is displayed when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time of when the campaign record was created");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when unsupported formatted campaign_created is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=20)
	public void create_campaign_with_past_campaign_created_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with past campaign_created date while creating campaign
		test = extent.startTest("create_campaign_with_past_campaign_created_date", "To validate whether user is able create campaign through campaign post api with past campaign_created date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_past_campaign_created_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_created = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", camp_created);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with past campaign_created date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when past campaign_created date is passed while creating campaign.\nDefect Reported: CT-12853");
			test.log(LogStatus.PASS, "API is returning success when past campaign_created date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when past campaign_created date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when past campaign_created date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when past campaign_created date is passed while creating campaign");
		}	
	}
	
	@Test(priority=21)
	public void create_campaign_with_future_campaign_created_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with future campaign_created date while creating campaign
		test = extent.startTest("create_campaign_with_future_campaign_created_date", "To validate whether user is able create campaign through campaign post api with future campaign_created date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_future_campaign_created_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, +10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_created = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_created", camp_created);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with future campaign_created date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when future campaign_created date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when future campaign_created date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when future campaign_created date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when future campaign_created date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when future campaign_created date is passed while creating campaign");
		}	
	}
	
	@Test(priority=22)
	public void create_campaign_with_valid_campaign_created_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_created date while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_created_date", "To validate whether user is able create campaign through campaign post api with valid campaign_created date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_created_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with valid campaign_created date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid campaign_created date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when valid campaign_created date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when valid campaign_created date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when valid campaign_created date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when valid campaign_created date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=23)
	public void create_campaign_without_campaign_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_modified while creating campaign
		test = extent.startTest("create_campaign_without_campaign_created", "To validate whether user is able create campaign through campaign post api without campaign_modified.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_created");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when campaign_modified is not passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when campaign_modified is not passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when campaign_modified is not passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when campaign_modified is not passed while creating campaign.");
			test.log(LogStatus.PASS, "entry_count value is correct when campaign_modified is not passed while creating campaign");
		}	
	}
	
	@Test(priority=24)
	public void create_campaign_with_blank_campaign_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_modified while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_modified", "To validate whether user is able create campaign through campaign post api with blank campaign_modified.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_modified");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", "");
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_modified is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: ", "Invalid message value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_modified is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_modified", "Invalid path value is displayed when blank campaign_modified is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "A date and time stamp of the last time anything was modified on campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_modified is passed through campaign post api while creating campaign.");
		}   
	}
	
	@Test(priority=25)
	public void create_campaign_with_invalid_campaign_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_modified while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_modified", "To validate whether user is able create campaign through campaign post api with invalid campaign_modified.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_modified");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_modified = test_data.get(9).split(",");
		for(String camp_modified:campaign_modified){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", camp_modified);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_modified");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+camp_modified+") campaign_modified is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_modified, "Invalid message value is returned in response when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_modified", "Invalid path value is displayed when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "A date and time stamp of the last time anything was modified on campaign");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+camp_modified+") campaign_modified is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=26)
	public void create_campaign_with_unsupported_formatted_campaign_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with unsupported formatted campaign_modified while creating campaign
		test = extent.startTest("create_campaign_with_unsupported_formatted_campaign_modified", "To validate whether user is able create campaign through campaign post api with unsupported formatted campaign_modified date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_unsupported_formatted_campaign_modified");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_modified = test_data.get(1).split(",");
		for(String camp_modified:campaign_modified){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", camp_modified);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with unsupported formatted campaign_modified date.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when unsupported formatted campaign_modified is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_modified, "Invalid message value is returned in response when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_modified", "Invalid path value is displayed when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "A date and time stamp of the last time anything was modified on campaign");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when unsupported formatted campaign_modified is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=27)
	public void create_campaign_with_past_campaign_modified_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with past campaign_modified date while creating campaign
		test = extent.startTest("create_campaign_with_past_campaign_modified_date", "To validate whether user is able create campaign through campaign post api with past campaign_modified date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_past_campaign_modified_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_modified = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", camp_modified);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with past campaign_modified date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when past campaign_modified date is passed while creating campaign.\nDefect Reported: CT-12853");
			test.log(LogStatus.PASS, "API is returning success when past campaign_modified date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when past campaign_modified date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when past campaign_modified date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when past campaign_modified date is passed while creating campaign");
		}	
	}
	
	@Test(priority=28)
	public void create_campaign_with_future_campaign_modified_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with future campaign_modified date while creating campaign
		test = extent.startTest("create_campaign_with_future_campaign_modified_date", "To validate whether user is able create campaign through campaign post api with future campaign_modified date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_future_campaign_modified_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, +10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_modified = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", camp_modified);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with future campaign_modified date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when future campaign_modified date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when future campaign_modified date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when future campaign_modified date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when future campaign_modified date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when future campaign_modified date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=29)
	public void create_campaign_with_valid_campaign_modified_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_modified date while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_modified_date", "To validate whether user is able create campaign through campaign post api with valid campaign_modified date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_modified_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with valid campaign_modified date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid campaign_modified date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when valid campaign_modified date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when valid campaign_modified date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when valid campaign_modified date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when valid campaign_modified date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=30)
	public void create_campaign_without_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_without_campaign_start_date", "To validate whether user is able create campaign through campaign post api without campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when campaign_start_date is not passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: campaign_start_date", "Invalid message value is returned in response when campaign_start_date is not passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when campaign_start_date is not passed through campaign post api while creating campaign.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "The date and time that the campaign is activated");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when campaign_start_date is not passed through campaign post api while creating campaign.");
		}	
	}
	
	@Test(priority=31)
	public void create_campaign_with_blank_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_start_date", "To validate whether user is able create campaign through campaign post api with blank campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_start_date");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", "");
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_start_date is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_start_date is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_start_date is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_start_date is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_start_date is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when blank campaign_start_date is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: ", "Invalid message value is returned in response when blank campaign_modified is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_start_date is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_start_date", "Invalid path value is displayed when blank campaign_start_date is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The date and time that the campaign is activated");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_start_date is passed through campaign post api while creating campaign.");
		}   
	}
	
	@Test(priority=32)
	public void create_campaign_with_invalid_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_start_date", "To validate whether user is able create campaign through campaign post api with invalid campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_start_date = test_data.get(4).split(",");
		for(String camp_start_date:campaign_start_date){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", camp_start_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with invalid campaign_start_date");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_start_date, "Invalid message value is returned in response when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_start_date", "Invalid path value is displayed when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time that the campaign is activated");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+camp_start_date+") campaign_start_date is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=33)
	public void create_campaign_with_unsupported_formatted_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with unsupported formatted campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_with_unsupported_formatted_campaign_start_date", "To validate whether user is able create campaign through campaign post api with unsupported formatted campaign_start_date date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_unsupported_formatted_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_start_date = test_data.get(1).split(",");
		for(String camp_start_date:campaign_start_date){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", camp_start_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with unsupported formatted campaign_start_date date.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when unsupported formatted campaign_start_date is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_start_date, "Invalid message value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_start_date", "Invalid path value is displayed when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time that the campaign is activated");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=34)
	public void create_campaign_with_past_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with past campaign_start_date date while creating campaign
		test = extent.startTest("create_campaign_with_past_campaign_start_date", "To validate whether user is able create campaign through campaign post api with past campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_past_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_start_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", camp_start_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with past campaign_start_date date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when past campaign_start_date is passed while creating campaign.\nDefect Reported: CT-12812");
			test.log(LogStatus.PASS, "API is returning success when past campaign_start_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when past campaign_start_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when past campaign_start_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when past campaign_start_date is passed while creating campaign");
		}	
	}
	
	@Test(priority=35)
	public void create_campaign_with_future_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with future campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_with_future_campaign_start_date", "To validate whether user is able create campaign through campaign post api with future campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_future_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, +10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_start_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", camp_start_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with future campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when future campaign_start_date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when future campaign_start_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when future campaign_start_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when future campaign_start_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when future campaign_start_date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=36)
	public void create_campaign_with_valid_campaign_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_start_date while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_start_date", "To validate whether user is able create campaign through campaign post api with valid campaign_start_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_start_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with valid campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid campaign_start_date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when valid campaign_start_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when valid campaign_start_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when valid campaign_start_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when valid campaign_start_date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=37)
	public void create_campaign_without_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_without_campaign_end_date", "To validate whether user is able create campaign through campaign post api without campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when campaign_end_date is not passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when campaign_end_date is not passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when campaign_end_date is not passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when campaign_end_date is not passed while creating campaign.");
			test.log(LogStatus.PASS, "entry_count value is correct when campaign_end_date is not passed while creating campaign");
		}	
	}
	
	@Test(priority=38)
	public void create_campaign_with_blank_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_end_date", "To validate whether user is able create campaign through campaign post api with blank campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_end_date");
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_end_date", "");
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_end_date is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: ", "Invalid message value is returned in response when blank campaign_end_date is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_end_date is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_end_date", "Invalid path value is displayed when blank campaign_end_date is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The date and time at which the campaign will end");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_end_date is passed through campaign post api while creating campaign.");
		}   
	}
	
	@Test(priority=39)
	public void create_campaign_with_invalid_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_end_date", "To validate whether user is able create campaign through campaign post api with invalid campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_end_date = test_data.get(5).split(",");
		for(String camp_end_date:campaign_end_date){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_end_date", camp_end_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with invalid campaign_end_date");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_end_date, "Invalid message value is returned in response when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_end_date", "Invalid path value is displayed when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time at which the campaign will end");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+camp_end_date+") campaign_end_date is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=40)
	public void create_campaign_with_unsupported_formatted_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with unsupported formatted campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_unsupported_formatted_campaign_end_date", "To validate whether user is able create campaign through campaign post api with unsupported formatted campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_unsupported_formatted_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_end_date = {"10-12-2016 10:10:15 PM", "12-14-2016 10:10:15 PM", "10/12/2016 10:10:15 PM", "10-12-2016 20:10:15"};
		for(String camp_end_date:campaign_end_date){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_end_date", camp_end_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with unsupported formatted campaign_end_date.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when unsupported formatted campaign_start_date is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when unsupported formatted campaign_end_date is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_FORMAT", "Invalid code value is returned in response when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Object didn't pass validation for format date-time: "+camp_end_date, "Invalid message value is returned in response when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_end_date", "Invalid path value is displayed when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The date and time at which the campaign will end");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when unsupported formatted campaign_end_date is passed through campaign post api while creating campaign.");
			}  
		}
	}	
	
	@Test(priority=41)
	public void create_campaign_with_past_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with past campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_past_campaign_end_date", "To validate whether user is able create campaign through campaign post api with past campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_past_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_end_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_end_date", camp_end_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with past campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when past campaign_end_date is passed while creating campaign.\nDefect Reported: CT-12812");
			test.log(LogStatus.PASS, "API is returning success when past campaign_end_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when past campaign_end_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when past campaign_end_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when past campaign_end_date is passed while creating campaign");
		}	
	}
	
	@Test(priority=42)
	public void create_campaign_with_future_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with future campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_future_campaign_end_date", "To validate whether user is able create campaign through campaign post api with future campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_future_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, +10);
        System.out.println(dateFormatter.format(cal.getTime()));
		String camp_end_date = dateFormatter.format(cal.getTime());
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_end_date", camp_end_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with future campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when future campaign_end_date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when future campaign_end_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when future campaign_end_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when future campaign_end_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when future campaign_end_date is passed while creating campaign");
		}	
	}	
	
	@Test(priority=43)
	public void create_campaign_with_today_campaign_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with today campaign_end_date while creating campaign
		test = extent.startTest("create_campaign_with_today_campaign_end_date", "To validate whether user is able create campaign through campaign post api with today campaign_end_date.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_today_campaign_end_date");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_end_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with today campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when today campaign_end_date is passed while creating campaign.");
			test.log(LogStatus.PASS, "API is returning success when today campaign_end_date is passed while creating campaign.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when today campaign_end_date is passed while creating campaign.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when today campaign_end_date is passed.");
			test.log(LogStatus.PASS, "entry_count value is correct when today campaign_end_date is passed while creating campaign");
		}	
	}
	
	@Test(priority=44)
	public void create_campaign_without_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_without_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api without campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when campaign_owner_user_id is not passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: campaign_owner_user_id", "Invalid message value is returned in response when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when campaign_owner_user_id is not passed through campaign post api while creating campaign.");
		}	
	}
	
	@Test(priority=45)
	public void create_campaign_with_blank_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with blank campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", "");
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_owner_user_id is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_owner_user_id", "Invalid path value is displayed when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique ID of the user that is the owner of the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_owner_user_id is passed through campaign post api while creating campaign.");
		}	
	}
	
	@Test(priority=46)
	public void create_campaign_with_invalid_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with invalid campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_user_owner = test_data.get(7).split(",");
		for(String camp_user_owner : campaign_user_owner){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", camp_user_owner);
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with invalid("+camp_user_owner+") campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_owner_user_id", "Invalid path value is displayed when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The unique ID of the user that is the owner of the campaign");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+camp_user_owner+") campaign_owner_user_id is passed through campaign post api while creating campaign.");
			}	
		}	
	}	
	
	@Test(priority=47)
	public void create_campaign_with_nonexisting_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with nonexisting campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_nonexisting_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with nonexisting campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_nonexisting_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", campaign_user_owner);
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with nonexisting("+campaign_user_owner+") campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray response_data = (JSONArray) parser.parse(line);
			JSONObject response_json= (JSONObject) response_data.get(0);
			String result_data = response_json.get("result").toString();
			Assert.assertEquals(result_data, "error");		
			Assert.assertEquals(response_json.get("data").toString(), "Failed to find campaign owner user account in system.");
			Assert.assertEquals(response_json.get("entry_count").toString(), "1");		
		}	
	}

	@Test(priority=48)
	public void create_campaign_with_other_billing_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with other billing campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_other_billing_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with other billing campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_other_billing_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", campaign_user_owner);
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with other billing("+campaign_user_owner+") campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray response_data = (JSONArray) parser.parse(line);
			JSONObject response_json= (JSONObject) response_data.get(0);
			String result_data = response_json.get("result").toString();
			Assert.assertEquals(result_data, "error");		
			Assert.assertEquals(response_json.get("data").toString(), "Not authorized to set the specified campaign owner user ("+campaign_user_owner+") belonging to OU 76 to a campaign");
			Assert.assertEquals(response_json.get("entry_count").toString(), "1");
//			Assert.assertTrue(false, "Error response format is not universal.");
		}	
	}	
	
	@Test(priority=49)
	public void create_campaign_with_agency_admin_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with agency admin campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_agency_admin_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with agency admin campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_agency_admin_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency admin campaign_user_owner_id.");
			}	
		}	
	}
	
	@Test(priority=50)
	public void create_campaign_with_agency_standard_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with agency standard campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_agency_standard_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with agency standard campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_agency_standard_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_user_owner_id.");
			}	
		}	
	}	
	
	@Test(priority=51)
	public void create_campaign_with_agency_readonly_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with agency readonly campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_agency_readonly_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with agency readonly campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_agency_readonly_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with agency standard campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray response_data = (JSONArray) parser.parse(line);
				JSONObject response_json= (JSONObject) response_data.get(0);
				String result_data = response_json.get("result").toString();
				Assert.assertEquals(result_data, "error");		
				Assert.assertEquals(response_json.get("data").toString(), "Read-only user accounts cannot be owners of campaigns.");
				Assert.assertEquals(response_json.get("entry_count").toString(), "1");	
			}	
		}	
//		Assert.assertTrue(false, "Error response format is not universal.");
	}	
	
	@Test(priority=52)
	public void create_campaign_with_company_admin_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with company admin campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_company_admin_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with company admin campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_company_admin_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company admin campaign_user_owner_id.");
			}	
		}	
	}	
	
	@Test(priority=53)
	public void create_campaign_with_company_standard_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with company standard campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_company_standard_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with company standard campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_company_standard_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company standard campaign_user_owner_id.");
			}	
		}	
	}
	
	@Test(priority=54)
	public void create_campaign_with_company_readonly_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with company readonly campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_company_readonly_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with company readonly campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_company_readonly_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with company readonly campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray response_data = (JSONArray) parser.parse(line);
				JSONObject response_json= (JSONObject) response_data.get(0);
				String result_data = response_json.get("result").toString();
				Assert.assertEquals(result_data, "error");		
				Assert.assertEquals(response_json.get("data").toString(), "Read-only user accounts cannot be owners of campaigns.");
				Assert.assertEquals(response_json.get("entry_count").toString(), "1");	
			}	
		}	
//		Assert.assertTrue(false, "Error response format is not universal.");
	}	
	
	@Test(priority=55)
	public void create_campaign_with_various_campaign_owner_id_and_company_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with campaign_owner_user_id using company admin access_token while creating campaign
		test = extent.startTest("create_campaign_with_various_campaign_owner_id_and_company_access_token", "To validate whether user is able create campaign through campaign post api with various level campaign_owner_user_id using company admin access_token.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_various_campaign_owner_id_and_company_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String company_access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] owners = test_data.get(7).split(",");
		int agency_admin_user = Integer.parseInt(owners[0]), agency_standard_user = Integer.parseInt(owners[1]), company_admin_user = Integer.parseInt(owners[2]), company_standard_user = Integer.parseInt(owners[3]), location_admin_user = Integer.parseInt(owners[4]), location_standard_user = Integer.parseInt(owners[5]);
		int[] campaign_owners = {agency_admin_user, agency_standard_user, company_admin_user, company_standard_user, location_admin_user, location_standard_user};
		for(int campaign_owner: campaign_owners){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_owner);
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", company_access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+" campaign_owner_user_id from campaign post api using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using company admin access_token.\nDefect Reported: CT-17096");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id.");
			}		
		}
	}
	
	@Test(priority=56)
	public void create_campaign_with_campaign_owner_id_and_location_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with campaign_owner_user_id using location admin access_token while creating campaign
		test = extent.startTest("create_campaign_with_various_campaign_owner_id_and_location_access_token", "To validate whether user is able create campaign through campaign post api with various level campaign_owner_user_id using company admin access_token.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_various_campaign_owner_id_and_location_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String location_access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] owners = test_data.get(7).split(",");
		int agency_admin_user = Integer.parseInt(owners[0]), agency_standard_user = Integer.parseInt(owners[1]), company_admin_user = Integer.parseInt(owners[2]), company_standard_user = Integer.parseInt(owners[3]), location_admin_user = Integer.parseInt(owners[4]), location_standard_user = Integer.parseInt(owners[5]);
		int[] campaign_owners = {agency_admin_user, agency_standard_user, company_admin_user, company_standard_user, location_admin_user, location_standard_user};
		for(int campaign_owner: campaign_owners){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_owner);
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", location_access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+" campaign_owner_user_id from campaign post api using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using location admin access_token.\nDefect Reported: CT-15487");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using location admin access_token.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using location admin access_token.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using location admin access_token.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign with "+((campaign_owner==agency_admin_user)?"agency admin":(campaign_owner==agency_standard_user)?"agency standard":(campaign_owner==company_admin_user)?"company admin":(campaign_owner==company_standard_user)? "company standard":(campaign_owner==location_admin_user)? "location admin":"location standard")+ " campaign_user_owner_id using location admin access_token.");
			}		
		}
	}	
	
	@Test(priority=57)
	public void create_campaign_with_location_admin_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with location admin campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_location_admin_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with location admin campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_location_admin_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[1]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location admin campaign_user_owner_id.");
			}	
		}	
	}	
	
	@Test(priority=58)
	public void create_campaign_with_location_standard_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with location standard campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_location_standard_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with location standard campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_location_standard_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[1]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_user_owner_id.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_user_owner_id.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_user_owner_id.");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location standard campaign_user_owner_id.");
			}	
		}	
	}	
	
	@Test(priority=59)
	public void create_campaign_with_location_readonly_campaign_owner_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with location readonly campaign_owner_user_id while creating campaign
		test = extent.startTest("create_campaign_with_location_readonly_campaign_owner_user_id", "To validate whether user is able create campaign through campaign post api with location readonly campaign_owner_user_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_location_readonly_campaign_owner_user_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_user_owner = Integer.parseInt(test_data.get(7));
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[1]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", campaign_user_owner);
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" with location readonly campaign_owner_user_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONArray response_data = (JSONArray) parser.parse(line);
				JSONObject response_json= (JSONObject) response_data.get(0);
				String result_data = response_json.get("result").toString();
				Assert.assertEquals(result_data, "error");		
				Assert.assertEquals(response_json.get("data").toString(), "Read-only user accounts cannot be owners of campaigns.");
				Assert.assertEquals(response_json.get("entry_count").toString(), "1");	
			}	
		}
//		Assert.assertTrue(false, "Error response format is not universal.");
	}	
	
	@Test(priority=60)
	public void create_campaign_without_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without group_id while creating campaign
		test = extent.startTest("create_campaign_without_group_id", "To validate whether user is able create campaign through campaign post api without group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api without group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when group_id is not passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when group_id is not passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when group_id is not passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when group_id is not passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when group_id is not passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when group_id is not passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: group_id", "Invalid message value is returned in response when group_id is not passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when group_id is not passed through campaign post api while creating campaign.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when group_id is not passed through campaign post api while creating campaign.");
		}	
	}
	
	@Test(priority=61)
	public void create_campaign_with_blank_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank group_id while creating campaign
		test = extent.startTest("create_campaign_with_blank_group_id", "To validate whether user is able create campaign through campaign post api with blank group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_blank_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String campaign_status = test_data.get(3);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", "");
		json.put("campaign_status", campaign_status);
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with blank campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank group_id is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank group_id is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank group_id is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank group_id is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank group_id is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank group_id is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank group_id is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank group_id is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "group_id", "Invalid path value is displayed when blank group_id is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique group to which the campaign belongs to");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank group_id is passed through campaign post api while creating campaign.");
		}	
	}
	
	@Test(priority=62)
	public void create_campaign_with_invalid_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid group_id while creating campaign
		test = extent.startTest("create_campaign_with_invalid_group_id", "To validate whether user is able create campaign through campaign post api with invalid group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] group_ids = test_data.get(6).split(",");
		for(String group_id : group_ids){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with invalid("+group_id+") group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed with campaign post api method.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				Assert.assertEquals(sub_error_path.get(1), "group_id", "Invalid path value is displayed when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "The unique group to which the campaign belongs to");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+group_id+") group_id is passed through campaign post api while creating campaign.");
			}	
		}	
	}
	
	@Test(priority=63)
	public void create_campaign_with_nonexisting_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with nonexisting group_id while creating campaign
		test = extent.startTest("create_campaign_with_nonexisting_group_id", "To validate whether user is able create campaign through campaign post api with nonexisting group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_nonexisting_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int group_id = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", 8);
		json.put("group_id", group_id);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with nonexisting("+group_id+") group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray response_data = (JSONArray) parser.parse(line);
			JSONObject response_json= (JSONObject) response_data.get(0);
			String result_data = response_json.get("result").toString();
			Assert.assertEquals(result_data, "error");		
			Assert.assertEquals(response_json.get("data").toString(), "Failed to find campaign owner user account in system.");
			Assert.assertEquals(response_json.get("entry_count").toString(), "1");		
//			Assert.assertTrue(false, "Error response format is not universal.");
		}	
	}
	
	@Test(priority=64)
	public void create_campaign_with_deleted_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with deleted group_id while creating campaign
		test = extent.startTest("create_campaign_with_deleted_group_id", "To validate whether user is able create campaign through campaign post api with deleted group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_deleted_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int group_id = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", 8);
		json.put("group_id", group_id);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with deleted("+group_id+") group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray response_data = (JSONArray) parser.parse(line);
			JSONObject response_json= (JSONObject) response_data.get(0);
			String result_data = response_json.get("result").toString();
			Assert.assertEquals(result_data, "error");		
			Assert.assertEquals(response_json.get("data").toString(), "Failed to find campaign owner user account in system.");
			Assert.assertEquals(response_json.get("entry_count").toString(), "1");		
//			Assert.assertTrue(false, "Error response format is not universal.");
		}	
	}	

	@Test(priority=65)
	public void create_campaign_with_other_billing_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with other billing group_id while creating campaign
		test = extent.startTest("create_campaign_with_other_billing_group_id", "To validate whether user is able create campaign through campaign post api with other billing group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_other_billing_group_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int group_id = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", group_id);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign from campaign post api with other billing("+group_id+") group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray response_data = (JSONArray) parser.parse(line);
			JSONObject response_json= (JSONObject) response_data.get(0);
			String result_data = response_json.get("result").toString();
			Assert.assertEquals(result_data, "error");		
			Assert.assertEquals(response_json.get("data").toString(), "Not authorized to create campaigns for group ID 76");
			Assert.assertEquals(response_json.get("entry_count").toString(), "1");
//			Assert.assertTrue(false, "Error response format is not universal.");
		}	
	}	
	
	@Test(priority=66)
	public void create_campaign_with_valid_group_id_and_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid group_id while creating campaign
		test = extent.startTest("create_campaign_with_valid_group_id_and_agency_admin_access_token", "To validate whether user is able create campaign using agency admin access_token through campaign post api with valid group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_group_id_and_agency_admin_access_token");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray json_array_response = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject)json_array_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group.":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" using agency admin access_token");
				test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" using agency admin access_token");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" using agency admin access_token");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group.":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" using agency admin access_token");
				test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign in "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency group":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company group":"location group"))+" using agency admin access_token");
			}	
		}	
	}
	
	@Test(priority=67)
	public void create_campaign_with_agency_group_id_and_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with agency group_id while creating campaign
		test = extent.startTest("create_campaign_with_agency_group_id_and_company_admin_access_token", "To validate whether user is able create campaign using company admin access_token through campaign post api with agency group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_agency_group_id_and_company_admin_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int agency_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", agency_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in agency group from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when user tried creating campaign in agency group using company admin access_token");
			test.log(LogStatus.PASS, "API is returning error when user tried creating campaign in agency group using company admin access_token");
			String data_value = json_response.get("data").toString();
			Assert.assertEquals(data_value, "Not authorized to set the specified campaign owner user (2) belonging to OU 8 to a campaign", "Proper validation is not displayed.");
			test.log(LogStatus.PASS, "Validation message is displayed when user tried creating campaign in agency group using company admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in agency group using company admin access_token");
//			Assert.assertTrue(false, "Validation format is not universal.");
		}	
	}
	
	@Test(priority=68)
	public void create_campaign_with_company_group_id_and_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with company group_id while creating campaign
		test = extent.startTest("create_campaign_with_company_group_id_and_company_admin_access_token", "To validate whether user is able create campaign using company admin access_token through campaign post api with company group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_company_group_id_and_company_admin_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int company_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", company_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in company group from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in company group using company admin access_token");
			test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in company group using company admin access_token");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in company group using company admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in company group using company admin access_token");
		}	
	}
	
	@Test(priority=69)
	public void create_campaign_with_location_group_id_and_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with location group_id and company admin access_token while creating campaign
		test = extent.startTest("create_campaign_with_location_group_id_and_company_admin_access_token", "To validate whether user is able create campaign using company admin access_token through campaign post api with company group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_location_group_id_and_company_admin_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int location_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", location_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in location group using company admin access_token from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in location group using company admin access_token");
			test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in location group using company admin access_token");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in location group using company admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in location group using company admin access_token");
		}	
	}	
	
	@Test(priority=70)
	public void create_campaign_with_agency_group_id_and_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with agency group_id using location access_token while creating campaign
		test = extent.startTest("create_campaign_with_agency_group_id_and_location_admin_access_token", "To validate whether user is able create campaign using location admin access_token through campaign post api with agency group_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_agency_group_id_and_location_admin_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int agency_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", agency_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in agency group using location admin access_token from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when user tried creating campaign in agency group using location admin access_token");
			test.log(LogStatus.PASS, "API is returning error when user tried creating campaign in agency group using location admin access_token");
			String data_value = json_response.get("data").toString();
			Assert.assertEquals(data_value, "Not authorized to create campaigns for group ID 8", "Proper validation is not displayed.");
			test.log(LogStatus.PASS, "Validation message is displayed when user tried creating campaign in agency group using location admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when user tried creating campaign in agency group using location admin access_token");
//			Assert.assertTrue(false, "Validation format is not universal.");
		}	
	}
	
	@Test(priority=71)
	public void create_campaign_with_company_group_id_and_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with company group_id using location access_token while creating campaign
		test = extent.startTest("create_campaign_with_company_group_id_and_location_admin_access_token", "To validate whether user is able create campaign using location admin access_token with company group_id through campaign post api.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_company_group_id_and_location_admin_access_token");
		String campaign_name = test_data.get(1);
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int company_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", company_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in company group using location admin access_token from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when user tried creating campaign in company group using location admin access_token");
			test.log(LogStatus.PASS, "API is returning error when user tried creating campaign in company group using location admin access_token");
			String data_value = json_response.get("data").toString();
			Assert.assertEquals(data_value, "Not authorized to create campaigns for group ID 114", "Proper validation is not displayed.");
			test.log(LogStatus.PASS, "Validation message is displayed when user tried creating campaign in company group using location admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when user tried creating campaign in company group using location admin access_token");
//			Assert.assertTrue(false, "Validation format is not universal.");
		}	
	}
	
	@Test(priority=72)
	public void create_campaign_with_location_group_id_and_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with location group_id using location access_token while creating campaign
		test = extent.startTest("create_campaign_with_location_group_id_and_location_admin_access_token", "To validate whether user is able create campaign using location admin access_token with location group_id through campaign post api.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_location_group_id_and_location_admin_access_token");
		String campaign_name = test_data.get(1);

		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int location_group = Integer.parseInt(test_data.get(6));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", location_group);
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign in location group using location admin access_token from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when user tried creating campaign in location group using company admin access_token");
			test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in location group using location admin access_token");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign in location group using location admin access_token");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign in location group using location admin access_token");
		}	
	}	
	
	
	@Test(priority=73)
	public void create_campaign_without_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api without campaign_users while creating campaign
		test = extent.startTest("create_campaign_without_campaign_users", "To validate whether user is able create campaign through campaign post api without campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_without_campaign_users");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign without campaign_users from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject)json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning error when user tried creating campaign without passing campaign_users. Defect Reported: CT-16906");
			test.log(LogStatus.PASS, "API is returning success when user tried creating campaign in without passing campaign_users.");
			String id_data = json_response.get("data").toString();
			Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
			test.log(LogStatus.PASS, "API is returning valid id when user tried creating campaign without passing campaign_users.");
			String entry_count = json_response.get("entry_count").toString();
			Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response when when user tried creating campaign without passing campaign_users.");
			test.log(LogStatus.PASS, "entry_count value is correct when when user tried creating campaign without passing campaign_users.");
		}		
	}
	
	@Test(priority=74)
	public void create_campaign_with_blank_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_users while creating campaign
		test = extent.startTest("create_campaign_with_blank_campaign_users", "To validate whether user is able create campaign through campaign post api with blank campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_users", "");
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign with blank campaign_users from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_users is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type array but found type string", "Invalid message value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_users is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_users", "Invalid path value is displayed when blank campaign_users is passed through campaign post api while creating campaign.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "The unique identifier for a users account");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_users is passed through campaign post api while creating campaign.");
		}		
	}	
	
	@Test(priority=75)
	public void create_campaign_with_invalid_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_users while creating campaign
		test = extent.startTest("create_campaign_with_invalid_campaign_users", "To validate whether user is able create campaign through campaign post api with invalid campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_invalid_campaign_users");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] users = test_data.get(10).split(",");
		JSONArray invalid_campaign_users = new JSONArray();
		invalid_campaign_users.add(Integer.parseInt(users[0])); invalid_campaign_users.add(users[1]); invalid_campaign_users.add(users[2]);
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_users", invalid_campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign with invalid campaign_users from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_users is passed through campaign post api while creating campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid campaign_users is passed through campaign post api while creating campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid campaign_users is passed through campaign post api while creating campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid campaign_users is passed with campaign post api method.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid campaign_users is passed through campaign post api while creating campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid campaign_users is passed through campaign post api while creating campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid campaign_users is passed through campaign post api while creating campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid campaign_users is passed through campaign post api while creating campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_users", "Invalid path value is displayed when invalid campaign_users is passed through campaign post api while creating campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "The unique identifier for a users account");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid campaign_users is passed through campaign post api while creating campaign.");
		}		
	}	
	
	@Test(priority=76)
	public void create_campaign_with_nonexisting_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with non existing campaign_users while creating campaign
		test = extent.startTest("create_campaign_with_nonexisting_campaign_users", "To validate whether user is able create campaign through campaign post api with non existing campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_nonexisting_campaign_users");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] users = test_data.get(10).split(",");
		JSONArray nonexisting_campaign_users = new JSONArray();
		nonexisting_campaign_users.add(Integer.parseInt(users[0])); nonexisting_campaign_users.add(Integer.parseInt(users[1]));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_users", nonexisting_campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign with non existing campaign_users from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray api_response = (JSONArray)parser.parse(line);
			JSONObject json_response = (JSONObject) api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign user is passed in assignee.");
			test.log(LogStatus.PASS, "API is returning error when non existing campaign user is passed in assignee.");
			String error_description = json_response.get("data").toString();
			Assert.assertEquals(error_description, "Failed to retrieve user account information for user 55555", "Proper validation message is not displayed when non existing campaign user is passed in assignee.");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing campaign user is passed in assignee.");
			Assert.assertEquals(json_response.get("entry_count").toString(), "1", "Incorrect entry_count is returned.");
			test.log(LogStatus.PASS, "Correct entry_count value is returned in response when non existing campaign user is passed in assignee.");
//			Assert.assertTrue(false, "API response is not in proper format");
		}		
	}
	
	@Test(priority=77)
	public void create_campaign_with_other_billing_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with other billing campaign_users while creating campaign
		test = extent.startTest("create_campaign_with_other_billing_campaign_users", "To validate whether user is able create campaign through campaign post api with other billing campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_other_billing_campaign_users");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] users = test_data.get(10).split(",");
		JSONArray other_billing_camp_users = new JSONArray();
		other_billing_camp_users.add(Integer.parseInt(users[0])); other_billing_camp_users.add(Integer.parseInt(users[1]));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_name", campaign_name);
		json.put("campaign_ext_id", "");
		json.put("campaign_created", today_formated_date);
		json.put("campaign_modified", today_formated_date);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		json.put("campaign_users", other_billing_camp_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able create campaign with other billing campaign_users from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONArray api_response = (JSONArray)parser.parse(line);
			JSONObject json_response = (JSONObject) api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when other billing campaign user is passed in assignee.");
			test.log(LogStatus.PASS, "API is returning error when other billing campaign user is passed in assignee.");
			String error_description = json_response.get("data").toString();
			Assert.assertEquals(error_description, "Not authorized to set the specified user ("+users[1]+") to the campaign 5353", "Proper validation message is not displayed when other billing campaign user is passed in assignee.");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing campaign user is passed in assignee.");
			Assert.assertEquals(json_response.get("entry_count"), 1, "Incorrect entry_count is returned.");
			test.log(LogStatus.PASS, "Correct entry_count value is returned in response when other billing campaign user is passed in assignee.");
//			Assert.assertTrue(false, "API response is not in proper format");
		}		
	}
	
	@Test(priority=78)
	public void create_campaign_with_valid_campaign_users() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_users while creating campaign
		test = extent.startTest("create_campaign_with_valid_campaign_users", "To validate whether user is able create campaign through campaign post api with valid campaign_users.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_users");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] users = test_data.get(10).split(",");
		JSONArray camp_assigned_users = new JSONArray();
		int agency_user = Integer.parseInt(users[0]), company_user = Integer.parseInt(users[1]), location_user = Integer.parseInt(users[2]);
		camp_assigned_users.add(agency_user); camp_assigned_users.add(company_user); camp_assigned_users.add(location_user); 
		
		String[] group_ids = test_data.get(6).split(",");
		int agency_group = Integer.parseInt(group_ids[0]), company_group = Integer.parseInt(group_ids[1]), location_group = Integer.parseInt(group_ids[2]);
		int[] groups = {agency_group, company_group, location_group};
		for(int group_id:groups){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", group_id);
			json.put("campaign_status", test_data.get(3));
			json.put("campaign_users", camp_assigned_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able create campaign with valid campaign_users from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray api_response = (JSONArray)parser.parse(line);
				JSONObject json_response = (JSONObject) api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when all level users is passed in campaign_users for "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency level group.":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company level group.":"location level group.")));
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				test.log(LogStatus.PASS, "API is returning valid id when all level users is passed in campaign_users for "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency level group.":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company level group.":"location level group.")));
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
				test.log(LogStatus.PASS, "entry_count value is correct when all level users is passed in campaign_users for "+(String.valueOf(group_id).equals(String.valueOf(agency_group))?"agency level group.":(String.valueOf(group_id).equals(String.valueOf(company_group))?"company level group.":"location level group.")));
			}		
		}	
	}	
	
	@Test(priority=79)
	public void edit_campaign_with_blank_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with blank campaign_id while editing campaign
		test = extent.startTest("edit_campaign_with_blank_campaign_id", "To validate whether user is able edit campaign through campaign post api with invalid campaign_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "edit_campaign_with_blank_campaign_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_id", "");
		json.put("campaign_name", campaign_name);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		campaign_users.add(2);
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able edit campaign with blank campaign_id from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json_response = (JSONObject)parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank campaign_id is passed in campaign post api while editing campaign.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank campaign_id is passed in campaign post api while editing campaign.");
			Assert.assertEquals(sub_error_path.get(1), "campaign_id", "Invalid path value is displayed when blank campaign_id is passed in campaign post api while editing campaign.");
			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Unique identifier representing the campaign");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank campaign_id is passed in campaign post api while editing campaign.");
		}  		
	}	
	
	@Test(priority=80)
	public void edit_campaign_with_invalid_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with invalid campaign_id while editing campaign
		test = extent.startTest("edit_campaign_with_invalid_campaign_id", "To validate whether user is able edit campaign through campaign post api with invalid campaign_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "edit_campaign_with_invalid_campaign_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] invalid_camp_id = test_data.get(11).split(",");
		for(String campaign_id:invalid_camp_id){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_id", campaign_id);
			json.put("campaign_name", campaign_name);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			json.put("group_id", Integer.parseInt(test_data.get(6)));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			campaign_users.add(2);
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able edit campaign with invalid campaign_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONObject json_response = (JSONObject)parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (campaign): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "campaign", "Invalid name value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/campaign");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid campaign_id is passed in campaign post api while editing campaign.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid campaign_id is passed in campaign post api while editing campaign.");
				Assert.assertEquals(sub_error_path.get(1), "campaign_id", "Invalid path value is displayed when invalid campaign_id is passed in campaign post api while editing campaign.");
				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
				String sub_error_description = sub_error_data.get("description").toString();
				Assert.assertEquals(sub_error_description, "Unique identifier representing the campaign");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid campaign_id is passed in campaign post api while editing campaign.");
			}  		
		}	
	}
	
	@Test(priority=81)
	public void edit_campaign_with_non_existing_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with non existing campaign_id while editing campaign
		test = extent.startTest("edit_campaign_with_non_existing_campaign_id", "To validate whether user is able edit campaign through campaign post api with non existing campaign_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "edit_campaign_with_non_existing_campaign_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_id = Integer.parseInt(test_data.get(11));
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_id", campaign_id);
		json.put("campaign_name", campaign_name);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able edit campaign with non existing campaign_id from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response = (JSONArray)parser.parse(line);
			JSONObject json_response = (JSONObject) api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_id is passed in campaign post api while editing campaign.");
			test.log(LogStatus.PASS, "API is returning error when other billing campaign user is passed in assignee.");
			String error_description = json_response.get("data").toString();
			// General message is passed currently error is not returned
			Assert.assertEquals(error_description, "cannot find the campaign record "+String.valueOf(campaign_id), "Proper validation message is not displayed when non existing campaign_id is passed in campaign post api while editing campaign.");
			test.log(LogStatus.PASS, "Proper validation message is displayed when non existing campaign_id is passed in campaign post api while editing campaign.");
			Assert.assertEquals(Integer.valueOf(json_response.get("entry_count").toString()), Integer.valueOf(1), "Incorrect entry_count is returned.");
			test.log(LogStatus.PASS, "Correct entry_count value is returned in response when non existing campaign_id is passed in campaign post api while editing campaign.");
//			Assert.assertTrue(false, "API response is not in proper format");
		}	
	}
	
	@Test(priority=82)
	public void edit_campaign_with_other_billing_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with other billing campaign_id while editing campaign
		test = extent.startTest("edit_campaign_with_other_billing_campaign_id", "To validate whether user is able edit campaign through campaign post api with other billing campaign_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "edit_campaign_with_other_billing_campaign_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		int campaign_id = Integer.parseInt(test_data.get(11)); // Belongs to group 76 and owner 3326
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("campaign_id", campaign_id);
		json.put("campaign_name", campaign_name);
		json.put("campaign_start_date", today_formated_date);
		json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
		json.put("group_id", Integer.parseInt(test_data.get(6)));
		json.put("campaign_status", test_data.get(3));
		JSONArray campaign_users = new JSONArray();
		json.put("campaign_users", campaign_users);
		array.add(json);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "To validate whether user is able edit campaign with other billing campaign_id from campaign post api.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray api_response = (JSONArray)parser.parse(line);
			JSONObject json_response = (JSONObject) api_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when other billing campaign_id is passed in campaign post api while editing campaign.\n CT-15440 is reported for this.");
			test.log(LogStatus.PASS, "API is returning error when other billing campaign user is passed in assignee.");
			String error_description = json_response.get("data").toString();
			// General message is passed currently error is not returned
			Assert.assertEquals(error_description, "Invalid campaign_id is entered.", "Proper validation message is not displayed when other billing campaign_id is passed in campaign post api while editing campaign.");
			test.log(LogStatus.PASS, "Proper validation message is displayed when other billing campaign_id is passed in campaign post api while editing campaign.");
			Assert.assertEquals(json_response.get("entry_count"), 1, "Incorrect entry_count is returned.");
			test.log(LogStatus.PASS, "Correct entry_count value is returned in response when other billing campaign_id is passed in campaign post api while editing campaign.");
			Assert.assertTrue(false, "API response is not in proper format");
		}	
	}
	
	@Test(priority=83)
	public void edit_campaign_with_valid_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign post api with valid campaign_id while editing campaign
		test = extent.startTest("edit_campaign_with_valid_campaign_id", "To validate whether user is able edit campaign through campaign post api with valid campaign_id.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "edit_campaign_with_valid_campaign_id");
		String campaign_name = test_data.get(1);
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		String[] campaign_ids = test_data.get(11).split(",");
		int agency_campaign_id = Integer.parseInt(campaign_ids[0]), company_campaign_id = Integer.parseInt(campaign_ids[1]), location_campaign_id = Integer.parseInt(campaign_ids[2]);
		int campaigns[] = {agency_campaign_id, company_campaign_id, location_campaign_id};
		for(int campaign_id: campaigns){
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_id", campaign_id);
			json.put("campaign_name", campaign_name);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(test_data.get(7)));
			
			String[] groups = test_data.get(6).split(",");
			if(campaign_id==agency_campaign_id)
				json.put("group_id", Integer.parseInt(groups[0]));
			else if(campaign_id==company_campaign_id)
				json.put("group_id", Integer.parseInt(groups[1]));
			else
				json.put("group_id", Integer.parseInt(groups[2]));
			json.put("campaign_status", test_data.get(3));
			JSONArray campaign_users = new JSONArray();
			json.put("campaign_users", campaign_users);
			array.add(json);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "To validate whether user is able edit campaign with valid campaign_id from campaign post api.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				
				JSONParser parser = new JSONParser();
				JSONArray api_response = (JSONArray)parser.parse(line);
				JSONObject json_response = (JSONObject) api_response.get(0);
				String result_data = json_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when campaign_id for "+(String.valueOf(campaign_id).equals(String.valueOf(agency_campaign_id))?"agency level campaign":(String.valueOf(campaign_id).equals(String.valueOf(company_campaign_id))?"company level campapign":"location level campaign"))+" is passed while editing campaign.");
				test.log(LogStatus.PASS, "API is returning error when valid campaign user is passed in assignee.");
				String id_data = json_response.get("data").toString();
				Assert.assertFalse(id_data.isEmpty(), "date field is blank in response.");
				Assert.assertEquals(id_data, String.valueOf(campaign_id));
				test.log(LogStatus.PASS, "API is returning valid id when when campaign_id for "+(String.valueOf(campaign_id).equals(String.valueOf(agency_campaign_id))?"agency level campaign":(String.valueOf(campaign_id).equals(String.valueOf(company_campaign_id))?"company level campapign":"location level campaign"))+" is passed while editing campaign.");
				String entry_count = json_response.get("entry_count").toString();
				Assert.assertEquals(entry_count, "1", "Incorrect entry_count value is displayed in response.");
				test.log(LogStatus.PASS, "entry_count value is correct when campaign_id for "+(String.valueOf(campaign_id).equals(String.valueOf(agency_campaign_id))?"agency level campaign":(String.valueOf(campaign_id).equals(String.valueOf(company_campaign_id))?"company level campapign":"location level campaign"))+" is passed while editing campaign.");
			}
		}	
	}		
}
