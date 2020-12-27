package campaign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.convirza.core.utils.DateUtils;
import com.convirza.tests.helper.UpdateComponents;
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
import com.convirza.tests.core.io.TestDataYamlReader;
import com.relevantcodes.extentreports.LogStatus;
import com.convirza.tests.core.utils.DBUserUtils;	
import common.*;

import common.*;

@Listeners(Listener.class)
public class CampaignUser extends BaseClass{
	String class_name = "CampaignUser";
	ArrayList<String> test_data;
	String campaign_created="", campaign_owner_user_id = "", group_id="",campaign_ext_id="", campaign_name="", owner_lname="", campaign_end_date="", campaign_id="", owner_fname="", camp_start_date="",
			first_name = "", last_name = "", email = "", phone_number = "", role = "", user_status = "";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@BeforeClass
	public void campaign_user_parameter_setup() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid access_token
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		campaign_id = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		campaign_owner_user_id = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_OWNER_USER_ID).toString();
		group_id = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.GROUP_ID).toString();
		campaign_ext_id = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID).toString();
		campaign_name = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME).toString();
		if (confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_CREATED) != null)	
		  campaign_end_date = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_END_DATE).toString();
		if (confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_CREATED) != null)
			campaign_created = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_CREATED).toString();
		if (confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_START_DATE) != null)
			camp_start_date = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_START_DATE).toString();
		
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY);
		first_name = confUserHierarchy.get(TestDataYamlConstants.UserConstants.FIRST_NAME).toString();
		last_name = confUserHierarchy.get(TestDataYamlConstants.UserConstants.LAST_NAME).toString();
		email = confUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		phone_number = confUserHierarchy.get(TestDataYamlConstants.UserConstants.PHONE_NUMBER).toString();
		role = confUserHierarchy.get(TestDataYamlConstants.UserConstants.ROLE).toString();
		user_status = confUserHierarchy.get(TestDataYamlConstants.UserConstants.STATUS).toString();
	}
	
	@Test(priority=1)
	public void campaign_user_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_user_with_invalid_access_token", "To validate whether user is able to get campaigns and its associated users through campaign/user api with invalid access_token");
		test.assignCategory("CFA GET /campaign/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void campaign_user_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_user_with_expired_access_token", "To validate whether user is able to get campaigns and its associated users through campaign/user api with expired access_token");
		test.assignCategory("CFA GET /campaign/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void campaign_user_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid access_token
		test = extent.startTest("campaign_user_with_valid_access_token", "To validate whether user is able to get campaign through campaign/user api with valid token");
		test.assignCategory("CFA GET /campaign/user API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign user
		   JSONObject first_campaign = (JSONObject)array.get(0);

		   // Check whether campaign user returns 100 record by default	
		   Assert.assertTrue(array.size()<=100, "Campaign user returning more than 100 records");
		   test.log(LogStatus.PASS, "Check whether campaign user returns 100 record by default");
		   // Check response contains the fields
		   Assert.assertTrue(first_campaign.containsKey("campaign_modified"),"campaign/user api does not contain campaign_modified field.");
		   test.log(LogStatus.PASS, "Check whether campaign_modified field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_created"),"campaign/user api does not contain campaign_created field.");
		   test.log(LogStatus.PASS, "Check whether campaign_created field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_owner_user_id"),"campaign/user api does not contain campaign_owner_user_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_owner_user_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("group_id"),"campaign/user api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_ext_id"),"campaign/user api does not contain campaign_ext_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_ext_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_name"),"campaign/user api does not contain campaign_name field.");
		   test.log(LogStatus.PASS, "Check whether campaign_name field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_end_date"),"campaign/user api does not contain campaign_end_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_end_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_id"),"campaign/user api does not contain campaign_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_start_date"),"campaign/user api does not contain campaign_start_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_start_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_status"),"campaign/user api does not contain campaign_status field.");
		   test.log(LogStatus.PASS, "Check whether campaign_status field is present in response");
		   // Check data type of fields
		   Assert.assertTrue(first_campaign.get("campaign_created").getClass().getName().equals("java.lang.String"),"");
		   Assert.assertTrue(first_campaign.get("campaign_owner_user_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("group_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("campaign_name").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("campaign_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("campaign_start_date").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("campaign_status").getClass().getName().equals("java.lang.String"));
		   test.log(LogStatus.PASS, "Check the data type of all fields of campaign/user api response");
		   
		   // Check campaign fields are not null
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_created"), "campaigm_created");
		   test.log(LogStatus.PASS, "Check campaign_created date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_owner_user_id"), "campaign_owner_user_id");
		   test.log(LogStatus.PASS, "Check campaign_owner_user_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("group_id"), "group_id");
		   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_name"), "campaign_name");
		   test.log(LogStatus.PASS, "Check campaign_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_id"), "campaign_id");
		   test.log(LogStatus.PASS, "Check campaign_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_start_date"), "campaign_start_date");
		   test.log(LogStatus.PASS, "Check campaign_start_date date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_status"), "campaign_status");
		   test.log(LogStatus.PASS, "Check campaign_status date is not null or blank in response.");		   
		   
		   JSONArray users_data = (JSONArray) first_campaign.get("users");
		   Assert.assertTrue(users_data.size()>0, "Users field does not contains any data");
		   test.log(LogStatus.INFO, "Check Users field in response contains data");
		   JSONObject first_user_data = (JSONObject)users_data.get(0);
		   Assert.assertTrue(first_user_data.containsKey("first_name"), "first_name field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify first_name field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("role_id"), "role_id field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify role_id field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("ct_user_id"), "ct_user_id field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify ct_user_id field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("group_id"), "group_id field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify group_id field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("last_name"), "last_name field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify last_name field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("user_status"), "user_status field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify user_status field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("user_email"), "user_email field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify user_email field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("password"), "password field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify password field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("user_ext_id"), "user_ext_id field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify user_ext_id field is present in campaign/user api response");
		   Assert.assertTrue(first_user_data.containsKey("user_title"), "user_title field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify user_title field is present in campaign/user api response");
		   
		   // Check users fields are not null
		   HelperClass.multiple_assertnotEquals(first_user_data.get("first_name"), "first_name");
		   test.log(LogStatus.PASS, "Check first_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("role_id"), "role_id");
		   test.log(LogStatus.PASS, "Check role_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("ct_user_id"), "ct_user_id");
		   test.log(LogStatus.PASS, "Check ct_user_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("group_id"), "group_id");
		   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("last_name"), "last_name");
		   test.log(LogStatus.PASS, "Check last_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("user_status"), "user_status");
		   test.log(LogStatus.PASS, "Check user_status date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("user_email"), "user_email");
		   test.log(LogStatus.PASS, "Check user_email date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_user_data.get("password"), "password");
		   test.log(LogStatus.PASS, "Check password date is not null or blank in response.");
		   
		   // Get the nth campaign from the campaign user
		   for(int i=0; i<array.size(); i++){
			   // Check duplicate user is not present in associated users in campaigns
			   JSONObject nth_campaign = (JSONObject)array.get(i);
			   JSONArray cam_users = (JSONArray) nth_campaign.get("users");
			   ArrayList<String> camp_user_ids = new ArrayList<String>();
			   Boolean duplicate_user_presence_in_camp = false;
			   for(int j=0; j<cam_users.size(); j++){
				  JSONObject cam_user = (JSONObject) cam_users.get(j);
				  camp_user_ids.add(cam_user.get("ct_user_id").toString());
			   }
			   Set<String> set = new HashSet<String>(camp_user_ids);
			   if(set.size() < camp_user_ids.size()){
				  duplicate_user_presence_in_camp = true;  
			   }
			   Assert.assertFalse(duplicate_user_presence_in_camp, "Duplicate user is present in associated user in campaign");
		   }
		   test.log(LogStatus.PASS, "Duplicate user is not present in associated user in campaign");
		}
	}
	
	@Test(priority=4)
	public void campaign_user_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/user api with blank limit
		test = extent.startTest("campaign_user_with_blank_limit", "To validate whether user is able to get campaign through campaign/user api with blank limit");
		test.assignCategory("CFA GET /campaign/user API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with blank limit value");
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
			System.out.println(error_path);
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank limit value is passed");
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			System.out.println(sub_error_path);
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank limit value is passed");
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank limit value is passed.");
		}	
	}
	
	@Test(priority=5)
	public void campaign_user_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/user api with invalid limit
		test = extent.startTest("campaign_user_with_invalid_limit", "To validate whether user is able to get campaign through campaign/list api with invalid limit");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_limit");
		String[] values = test_data.get(1).split(",");
		// Add parameters in request
		for(String limit_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("limit", limit_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			if(limit_value.equals("abc"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with char limit value");
			else if(limit_value.equals("!@$#"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with special character limit value");
			else if(limit_value.equals("123abc"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with alpahnumric character limit value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid limit value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid limit value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid limit value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (limit): Expected type integer but found type string", "Invalid message value is returned in response when invalid limit value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when invalid limit value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid limit value is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid limit value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid limit value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank limit value is passed");
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid limit value is passed");
				if(limit_value.equals("abc"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when characters limit value is passed.");
				else if(limit_value.equals("!@$#"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when special characters limit value is passed.");
				else if(limit_value.equals("123abc"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when alphanumeric characters limit value is passed.");
			}	
		}	
	}
	
	@Test(priority=6)
	public void campaign_user_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_user_with_negative_limit", "To validate whether user is able to get campaign through campaign/user api with negative limit");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_negative_limit");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = test_data.get(1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with negative limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when negative limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when negative limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/user");
			list.add("get");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit_value+" is less than minimum 1", "Invalid message value is returned in response when negative limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative limit value is passed.");
		}	
	}
	
	@Test(priority=7)
	public void campaign_user_with_0_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with 0 limit value
		test = extent.startTest("campaign_user_with_0_limit", "To validate whether user is able to get campaign through campaign/user api with 0 limit value");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_0_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = test_data.get(1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when negative limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when negative limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/user");
			list.add("get");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when 0 limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit_value+" is less than minimum 1", "Invalid message value is returned in response when 0 limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when 0 limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when 0 limit value is passed.");
		}	
	}
	
	@Test(priority=8)
	public void campaign_user_with_valid_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid limit value
		test = extent.startTest("campaign_user_with_valid_limit", "To validate whether user is able to get campaign through campaign/user api with valid limit value");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.GROUP_LEVEL)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   JSONArray camp_data_array = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when valid limit value is entered.");
		   test.log(LogStatus.PASS, "API returns success when valid limit value is entered.");
		   Assert.assertEquals(response_json.get("err"), null, "err is not null when valid limit value is entered.");
		   test.log(LogStatus.PASS, "err is null when valid limit value is entered.");
		   // Check whether campaign list returns number of records defined in limit
		   Assert.assertEquals(camp_data_array.size(), Constants.GROUP_LEVEL, "Campaign list does not return number of records defined in limit");
		   test.log(LogStatus.PASS, "Check whether campaign list returns number of records defined in limit");
		   JSONObject first_campaign = (JSONObject) camp_data_array.get(0);
		   JSONArray campaign_users = (JSONArray) first_campaign.get("users");
		   Assert.assertFalse(campaign_users.size() == Constants.GROUP_LEVEL,"Users list is also limited with limit value");
		   test.log(LogStatus.INFO, "Check Users list is also limited with limit value");
		}
	}
	
	@Test(priority=9)
	public void campaign_user_with_gt_2000_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_user_with_gt_2000_limit", "To validate whether user is able to get campaign and its associated users through campaign/user api with greater than 2000 limit");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_gt_2000_limit");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = String.valueOf(Constants.MAX_LIMIT+1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with greater than 2000 limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when greater than 2000 limit value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when greater than 2000 limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when greater than 2000 limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when greater than 2000 limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when greater than 2000 limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/user");
			list.add("get");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MAXIMUM", "Invalid code value is returned in response when greater than 2000 limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit_value+" is greater than maximum 2000", "Invalid message value is returned in response when greater than 2000 limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than 2000 limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 2000 limit value is passed.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 2000 limit value is passed.");
		}	
	}	
	
	@Test(priority=10)
	public void campaign_user_with_2000_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with 2000 limit
		test = extent.startTest("campaign_user_with_2000_limit", "To validate whether user is able to get campaign through campaign/user api with 2000 limit value");
		test.assignCategory("CFA GET /campaign/user API");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.MAX_LIMIT)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {	   
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   JSONArray camp_data_array = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when valid limit value is entered.");
		   test.log(LogStatus.PASS, "API returns success when valid 2000 value is entered.");
		   Assert.assertEquals(response_json.get("err"), null, "err is not null when 2000 limit value is entered.");
		   test.log(LogStatus.PASS, "err is null when valid limit value is entered.");
		   // Check whether campaign/users returns number of records defined in limit
		   Assert.assertFalse(camp_data_array.size() == 100, "campaign/users is returning 100 records");
		   test.log(LogStatus.PASS, "Check whether campaign/users returns success when 2000 limit value is passed");
		}
	}
	
	@Test(priority=11)
	public void campaign_user_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/user api method with blank offset value
		test = extent.startTest("campaign_user_with_blank_offset", "To validate whether user is able to get campaign through campaign/user api with blank offset");
		test.assignCategory("CFA GET /campaign/user API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with blank offset value");
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
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank offset value is passed");
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank offset value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank offset value is passed.");
		}	
	}
	
	@Test(priority=12)
	public void campaign_user_with_blank_limit_and_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_user_with_blank_limit_and_offset", "To validate whether user is able to get campaign through campaign/user api with blank limit and offset");
		test.assignCategory("CFA GET /campaign/user API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		nvps.add(new BasicNameValuePair("offset", ""));		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with blank limit and offset value");
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
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			System.out.println(sub_error_path);
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank limit value is passed");
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank limit value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank limit value is passed.");
			
			error_data = (JSONObject) errors_array.get(1);
			error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank offset value is passed");
			in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when blank offset value is passed");
			message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when blank offset value is passed");
			name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when blank offset value is passed");
			error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(), "path is not blank when blank offset value is passed");
			sub_error_array = (JSONArray) error_data.get("errors");
			sub_error_data = (JSONObject) sub_error_array.get(0);
			sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank offset value is passed");
			sub_error_message = sub_error_data.get("message").toString();
			sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank offset value is passed");
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank offset value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank offset value is passed.");
		}	
	}
	
	@Test(priority=13)
	public void campaign_user_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_user_with_invalid_offset", "To validate whether user is able to get campaign through campaign/user api with invalid offset");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_offset");
		String[] values = test_data.get(2).split(",");
		// Add parameters in request
		for(String offset_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("offset", offset_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			if(offset_value.equals("abc"))
				test.log(LogStatus.INFO, "Execute campaign/user api method with char offset value");
			else if(offset_value.equals("!@$#"))
				test.log(LogStatus.INFO, "Execute campaign/user api method with special character offset value");
			else if(offset_value.equals("123abc"))
				test.log(LogStatus.INFO, "Execute campaign/user api method with alpahnumric character offset value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid offset value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid offset value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when invalid offset value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (offset): Expected type integer but found type string", "Invalid message value is returned in response when invalid offset value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when invalid offset value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertTrue(error_path.isEmpty(), "path is not blank when invalid offset value is passed");
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid offset value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid offset value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid offset value is passed");
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid offset value is passed");
				if(offset_value.equals("abc"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when characters offset value is passed.");
				else if(offset_value.equals("!@$#"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when special characters offset value is passed.");
				else if(offset_value.equals("123abc"))
					test.log(LogStatus.PASS, "Check whether proper validation message is displayed when alphanumeric characters offset value is passed.");
			}	
		}	
	}	
	
	@Test(priority=14)
	public void campaign_user_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/user api with negative offset
		test = extent.startTest("campaign_user_with_negative_offset", "To validate whether user is able to get campaign through campaign/user api with negative offset");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_negative_offset");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String offset_value = test_data.get(2);
		nvps.add(new BasicNameValuePair("offset", offset_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with negative limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when negative offset value is passed");		
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when negative offset value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when negative offset value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (offset): Value failed JSON Schema validation", "Invalid message value is returned in response when negative offset value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "offset", "Invalid name value is returned in response when negative offset value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/user");
			list.add("get");
			list.add("parameters");
			list.add("1");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MINIMUM", "Invalid code value is returned in response when negative offset value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+ offset_value +" is less than minimum 0", "Invalid message value is returned in response when negative offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative offset value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Number of records from the beginning to start the data set from");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative offset value is passed.");
		}	
	}
	
	@Test(priority=15)
	public void campaign_user_with_0_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api without offset
		test = extent.startTest("campaign_user_with_0_offset", "To validate whether user is able to get campaign through campaign/user api with 0 offset value");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_0_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		JSONArray camp_data_array_without_offset = new JSONArray(), camp_data_array_with_offset = new JSONArray();
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   camp_data_array_without_offset = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when offset value is not passed.");
		   test.log(LogStatus.PASS, "API returns success when offset value is not passed.");
		}
		
		// Execute campaign/user api with 0 offset value
		nvps.add(new BasicNameValuePair("offset", test_data.get(2)));
		response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with 0 offset value");
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   camp_data_array_with_offset = (JSONArray)response_json.get("data");
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when 0 offset value is passed.");
		   test.log(LogStatus.PASS, "API returns success when 0 offset value is passed.");
		}
		
		Assert.assertEquals(camp_data_array_without_offset, camp_data_array_with_offset);
	}
	
	@Test(priority=16)
	public void campaign_user_with_valid_limit_and_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api without offset value
		test = extent.startTest("campaign_user_with_valid_limit_and_offset", "To validate whether user is able to get campaign through campaign/user api with valid limit and offset value");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_limit_and_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.GROUP_LEVEL-2)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		ArrayList<String> camp_id = new ArrayList<>();
		while ((line = rd.readLine()) != null) {
		   
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "API returns success when limit is passed and offset is not passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when limit is passed and offset is not passed.");
		   test.log(LogStatus.PASS, "err is null when limit is passed and offset is not passed.");
		   // Get the 10th campaign data from the campaign list
		   for(int i=0;i<Constants.GROUP_LEVEL-2;i++){
			   JSONObject nth_camp_data =(JSONObject) array.get(i);
			   String nth_camp_id = nth_camp_data.get("campaign_id").toString();
			   camp_id.add(nth_camp_id);
		   }  
		}
	   // Execute campaign/list api method with offset value
	   nvps.add(new BasicNameValuePair("offset", String.valueOf(Constants.GROUP_LEVEL-2)));
	   response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
	   test.log(LogStatus.INFO, "Execute campaign/user api method with offset value");
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

		   for (int i=0; i<array.size(); i++) {
			   JSONObject data = (JSONObject) array.get(i);
			   Assert.assertFalse(camp_id.contains(data.get("campaign_id").toString()), "Offset is not working properly.");
		   }
	   }
	}
	
	@Test(priority=17)
	public void campaign_user_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_user_with_blank_filter", "To validate whether user is able to get campaigns and its associated users through campaign/user api with blank filter value");
		test.assignCategory("CFA GET /campaign/user API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with blank filter value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");

		   Assert.assertTrue(array.size()<= 100);	

		   Assert.assertEquals(json.get("result"), "success", "API does not return success when blank filter value is passed.");
		   test.log(LogStatus.PASS, "API returns success when blank filter value is passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when blank filter value is passed.");
		   test.log(LogStatus.PASS, "err is null when blank filter value is passed.");
		}	
	}	
	
	@Test(priority=18)	
	public void campaign_user_with_valid_filter_for_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_id
		test = extent.startTest("campaign_user_with_valid_filter_for_campaign_id", "To validate whether user is able to get campaign through campaign/user api with valid filter for campaign_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_id = campaign_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id"+encoded_operator+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid campaign_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "Campaign user does not return records when valid campaign_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("=")){			   
					   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "campaign/user api does not return campaigns according to passed campaign_id for filter.");
					   Assert.assertTrue(array.size()==1, "campaign/user is returning more than 1 records when = operator is used for campaign_id filter.");
					   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/user api does not contain campaign_modified field.");
					   test.log(LogStatus.PASS, "Check whether campaign_modified field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/user api does not contain campaign_created field.");
					   test.log(LogStatus.PASS, "Check whether campaign_created field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/user api does not contain campaign_owner_user_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_owner_user_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/user api does not contain group_id field.");
					   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/user api does not contain campaign_ext_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_ext_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/user api does not contain campaign_name field.");
					   test.log(LogStatus.PASS, "Check whether campaign_name field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/user api does not contain campaign_end_date field.");
					   test.log(LogStatus.PASS, "Check whether campaign_end_date field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/user api does not contain campaign_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/user api does not contain campaign_start_date field.");
					   test.log(LogStatus.PASS, "Check whether campaign_start_date field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/user api does not contain campaign_status field.");
					   test.log(LogStatus.PASS, "Check whether campaign_status field is present in response");
					   // Check data type of fields
					   Assert.assertTrue(campaign.get("campaign_created").getClass().getName().equals("java.lang.String"),"");
					   Assert.assertTrue(campaign.get("campaign_owner_user_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("group_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("campaign_name").getClass().getName().equals("java.lang.String"));
					   Assert.assertTrue(campaign.get("campaign_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("campaign_start_date").getClass().getName().equals("java.lang.String"));
					   Assert.assertTrue(campaign.get("campaign_status").getClass().getName().equals("java.lang.String"));
					   test.log(LogStatus.PASS, "Check the data type of all fields of campaign/user api response");
					   
					   // Check campaign fields are not null
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_created"), "campaigm_created");
					   test.log(LogStatus.PASS, "Check campaign_created date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_owner_user_id"), "campaign_owner_user_id");
					   test.log(LogStatus.PASS, "Check campaign_owner_user_id date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("group_id"), "group_id");
					   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_name"), "campaign_name");
					   test.log(LogStatus.PASS, "Check campaign_name date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_id"), "campaign_id");
					   test.log(LogStatus.PASS, "Check campaign_id date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_start_date"), "campaign_start_date");
					   test.log(LogStatus.PASS, "Check campaign_start_date date is not null or blank in response.");
					   HelperClass.multiple_assertnotEquals(campaign.get("campaign_status"), "campaign_status");
					   test.log(LogStatus.PASS, "Check campaign_status date is not null or blank in response.");		   
					   
					   JSONArray users_data = (JSONArray) campaign.get("users");
					   Assert.assertTrue(users_data.size()>=0, "Users field does not contains any data");
					   test.log(LogStatus.INFO, "Check Users field in response contains data");
					   if(users_data.size()>0){
						   JSONObject first_user_data = (JSONObject)users_data.get(0);
						   Assert.assertTrue(first_user_data.containsKey("first_name"), "first_name field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify first_name field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("role_id"), "role_id field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify role_id field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("ct_user_id"), "ct_user_id field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify ct_user_id field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("group_id"), "group_id field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify group_id field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("last_name"), "last_name field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify last_name field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("user_status"), "user_status field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify user_status field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("user_email"), "user_email field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify user_email field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("password"), "password field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify password field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("user_ext_id"), "user_ext_id field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify user_ext_id field is present in campaign/user api response");
						   Assert.assertTrue(first_user_data.containsKey("user_title"), "user_title field is not present in campaign/user api response");
						   test.log(LogStatus.PASS, "Verify user_title field is present in campaign/user api response");
					   }
					   
				   }
				   else if(operator.equals(">="))			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_id").toString())>=Integer.parseInt(camp_id), "campaign/user api does not return campaigns according to applied filter for campaign_id");
				   else			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_id").toString())<=Integer.parseInt(camp_id), "campaign/user api does not return campaigns according to applied filter for campaign_id"+Integer.parseInt(campaign.get("campaign_id").toString()));
				   test.log(LogStatus.PASS, "Check campaign/user api does not return campaigns according to passed campaign_id for filter.");
			   }
			}	
		}
	}	
	
	@Test(priority=19)
	public void campaign_user_with_invalid_filter_operator_for_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_campaign_id", "To validate whether user is able to get campaign through campaign/user api with invalid filter operator for campaign_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_id = campaign_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id"+encoded_operator+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_id"+operator+camp_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_id");
			}	   
		}	
	}		
	
	@Test(priority=20)	
	public void campaign_user_with_filter_for_nonexisting_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_id", "To validate whether user is able to get campaign through campaign/user api with filter for non existing campaign_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_camp_id");
		String camp_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_id is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_id is passed.");
		}
	}
	
	@Test(priority=21)
	public void campaign_user_with_valid_filter_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_ext_id
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_ext_id", "To validate whether user is able to get campaign through campaign/user api with valid filter for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_ext_id = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign user
		   JSONObject first_campaign = (JSONObject)array.get(0);
		   // Check whether campaign user returns 1 record valid campaign_ext_id is passed for filter
		   Assert.assertEquals(array.size(), 1, "Campaign user does not return 1 record when valid campaign_ext_id is passed for filter.");
		   Assert.assertEquals(first_campaign.get("campaign_ext_id").toString(), camp_ext_id, "campaign/user api does not return searched campaign when valid campaign_ext_id is passed for filter.");
		}
	}
	
	@Test(priority=22)
	public void campaign_user_with_invalid_filter_operator_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with greater than filter for campaign_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_camp_ext_id", "To validate whether user is able to get campaign through campaign/user api with invalid filter operator for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_ext_id"+encoded_operator+camp_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_ext_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_ext_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_ext_id"+operator+camp_ext_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_ext_id");
			}	   
		}
	}	
	
	@Test(priority=23)	
	public void campaign_user_with_filter_for_nonexisting_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_ext_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_ext_id", "To validate whether user is able to get campaign through campaign/user api with filter for non existing campaign_ext_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with filter for non existing campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign user returns 0 record non existing campaign_ext_id is passed for filter
		   Assert.assertEquals(array.size(), 0, "Campaign user returns record when non existing campaign_ext_id is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/user does not return record when non existing campaign_ext_id is passed for filter.");
		}
	}	
	
	@Test(priority=24)
	public void campaign_user_with_valid_filter_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_name
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_name", "To validate whether user is able to get campaign through campaign/user api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/user API");
		String agency_camp = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME).toString();
		String company_camp = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME).toString();
		String location_camp = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME).toString();
		String[] camp_names = {agency_camp,company_camp,location_camp};
		for(String camp_name:camp_names){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_name field.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   JSONArray data_array = (JSONArray)json.get("data");
			   Assert.assertTrue(data_array.size()>=1, "API does not return result when valid campaign_name is passed for filter.");
			   // Get the first campaign from the campaign list
			   System.out.println(data_array.size());
			   for(int i=0; i < data_array.size(); i++){
				   JSONObject campaign = (JSONObject)(data_array.get(i));
				   Assert.assertEquals(campaign.get("campaign_name").toString(), camp_name, "campaign/user api does not return searched campaign when valid campaign_name is passed for filter.");   
			   }
			   if(camp_name.equals(agency_camp))
				   test.log(LogStatus.PASS, "User is able to filter agency level campaign from campaign/user api.");
			   else if(camp_name.equals(company_camp))
				   test.log(LogStatus.PASS, "User is able to filter company level campaign from campaign/user api.");
			   else if(camp_name.equals(location_camp))
				   test.log(LogStatus.PASS, "User is able to filter location level campaign from campaign/user api.");
			}
		}
	}
	
	@Test(priority=25)
	public void campaign_user_with_invalid_filter_operetor_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with greater than filter for campaign_id
		test = extent.startTest("campaign_user_with_invalid_filter_operetor_for_camp_name", "To validate whether user is able to get campaign through campaign/user api with invalid filter operator for campaign_name");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_name = campaign_name;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name"+encoded_operator+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_name");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_name");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_name"+operator+camp_name);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_name");
			}	   
		}
	}
	
	@Test(priority=26)	
	public void campaign_user_with_filter_for_non_existing_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_name
		test = extent.startTest("campaign_user_with_filter_for_non_existing_camp_name", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_non_existing_camp_name");
		String camp_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with filter for non existing campaign_name.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_name is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_name is entered for filter");
		   test.log(LogStatus.PASS, "Check API is returning success when non existing campaign_name is entered for filter");
		   String error = json.get("err").toString();
		   Assert.assertEquals(error, "no records found", "Proper validation is not displayed when non existing campaign_name is entered for filter.");
		   test.log(LogStatus.PASS, "Check proper validation is not displayed when non existing campaign_name is entered for filter.");
		}
	}	
	
	@Test(priority=27)
	public void campaign_user_with_valid_filter_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_status
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_status", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for campaign_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_camp_status");
		String[] camp_status = {Constants.ComponentStatus.ACTIVE, Constants.ComponentStatus.INACTIVE,
			Constants.ComponentStatus.DELETED};
		for (String camp_stat:camp_status) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_status%3d"+camp_stat));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_status field.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				if (!json.get("result").equals("error")) {
					JSONArray data_array = (JSONArray)json.get("data");
					for(int i=0; i < data_array.size(); i++){
						JSONObject campaign = (JSONObject)(data_array.get(i));
						Assert.assertEquals(campaign.get("campaign_status").toString(), camp_stat, "campaign/user api does not filter campaign based on campaign_status field.");
					}
				} else {
					Assert.assertEquals(json.get("err").toString(), "no records found");
				}
			}
		}
	}
	
	@Test(priority=28)
	public void campaign_user_with_invalid_filter_operetor_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_status
		test = extent.startTest("campaign_user_with_invalid_filter_operetor_for_camp_status", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for campaign_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operetor_for_camp_status");
		String campaign_status = test_data.get(4);
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_status"+encoded_operator+campaign_status));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_status");
				System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_status");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result_data = json.get("result").toString(); 
				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_status"+operator+campaign_status);
				   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_status");
				}	   
			}	
	}
	
	@Test(priority=29)	
	public void campaign_user_with_filter_for_deleted_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for deleted campaign_name
		test = extent.startTest("campaign_user_with_filter_for_deleted_camp_status", "To validate whether user is able to get campaign through campaign/user api with valid filter for deleted campaign_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_deleted_camp_status");
		String campaign_status = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_status%3d"+campaign_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with filter for deleted campaign_status.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when deleted campaign_status is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when deleted campaign_status is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when deleted campaign_status is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when deleted campaign_status is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when deleted campaign_status is passed.");
		}
	}	
	
//	@Test(priority=30)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_invalid_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid campaign_name
		test = extent.startTest("campaign_user_with_filter_for_invalid_camp_status", "To validate whether user is able to get campaign through campaign/user api with valid filter for invalid campaign_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_camp_status");
		String[] camp_status = test_data.get(4).split(",");
		test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid campaign_status.");
		for(String campaign_status:camp_status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_status%3d"+campaign_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid campaign_status is entered for filter
			   Assert.assertEquals(result_data, "error", "API is returning success when invalid campaign_status is entered for filter.\nDefect Reported: CT-17153");
			   test.log(LogStatus.PASS, "API is returning error when invalid campaign_status is entered for filter");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when invalid campaign_status is passed. Defect Reported: CT-17153");
			   test.log(LogStatus.PASS, "Proper validation is displayed when invalid campaign_status is passed.");
			}
		}
	}	
	
	@Test(priority=31)
	public void campaign_user_with_valid_filter_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_created
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_created", "To validate whether user is able to get campaign through campaign/user api with valid filter for campaign_created");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_created = campaign_created;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_created%3d"+ DateUtils.getDateFromDateTime(camp_created)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign/user returns at least 1 record when valid campaign_created is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_created is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_created").toString(), DateUtils.getDateTimeFromDate(DateUtils.getDateFromDateTime(camp_created)), "campaign/list api does not return campaigns according to passed campaign_created for filter.");
		   }
		}
	}
	
	@Test(priority=32)
	public void campaign_user_with_invalid_filter_operetor_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_created
		test = extent.startTest("campaign_user_with_invalid_filter_operetor_for_camp_created", "To validate whether user is able to get campaign through campaign/user api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_created = campaign_created;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_created"+encoded_operator+camp_created));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_created");
				System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_created");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result_data = json.get("result").toString(); 
				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_created"+operator+camp_created);
				   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_created");
				}	   
			}	
	}
	
	@Test(priority=33)	
	public void campaign_user_with_filter_for_nonexisting_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_created
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_created", "To validate whether user is able to get campaign through campaign/user api with filter for non existing campaign_created");
		test.assignCategory("CFA GET /campaign/user API");
		Calendar calendar = Calendar.getInstance();
	    // add one day to the date/calendar
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    // now get "tomorrow"
	    Date tomorrow_date = calendar.getTime();
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    format.setTimeZone(tz);
	    String tomorrow = format.format(tomorrow_date);
	    // print out tomorrow's date
	    System.out.println("tomorrow: " + tomorrow);
		String[] campaign_created = {"2016-09-04T00:00:00.000Z",tomorrow.toString()};
		for(String camp_created:campaign_created){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_created%3d"+camp_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with filter for non existing campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_created is entered for filter");
			   test.log(LogStatus.PASS, "API is returning error when non existing campaign_created is entered for filter");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_created is passed.");
			   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_created is passed.");
			}
		}
	}	
	
	@Test(priority=34)
	public void campaign_user_with_valid_filter_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_modified
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_modified", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for campaign_modified");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_modified = UpdateComponents.updateCampaign(Constants.GroupHierarchy.AGENCY);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign user returns at least 1 record when valid campaign_modified is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign user does not return records when valid campaign_modified is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign/user
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_modified").toString(), camp_modified, "campaign/user api does not return campaigns according to passed campaign_modified for filter.");
		   }
		}
	}
	
	@Test(priority=35)
	public void campaign_user_with_invalid_filter_operetor_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_modified
		test = extent.startTest("campaign_user_with_invalid_filter_operetor_for_camp_modified", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operetor_for_camp_modified");
		String campaign_modified = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_modified"+encoded_operator+campaign_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_modified");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_modified");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_modified"+operator+campaign_modified);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_modified");
			}	   
		}	
	}
	
	@Test(priority=36)	
	public void campaign_user_with_filter_for_nonexisting_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_modified
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_modified", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing campaign_modified");
		test.assignCategory("CFA GET /campaign/user API");
		Calendar calendar = Calendar.getInstance();
	    // add one day to the date/calendar
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    // now get "tomorrow"
	    Date tomorrow_date = calendar.getTime();
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    format.setTimeZone(tz);
	    String tomorrow = format.format(tomorrow_date);
	    // print out tomorrow's date
	    System.out.println("tomorrow: " + tomorrow);
		String[] campaign_modified = {"2016-09-04 19:47:53",tomorrow.toString()};
		for(String camp_modified:campaign_modified){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_modified is entered for filter");
			   test.log(LogStatus.PASS, "API is returning error when non existing campaign_modified is entered for filter");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_modified is passed.");
			   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_modified is passed.");
			}
		}
	}
	
	@Test(priority=37)
	public void campaign_user_with_valid_filter_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_start_date
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_start_date", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for campaign_start_date");
		test.assignCategory("CFA GET /campaign/user API");
		String campaign_start_date = DateUtils.convertISOWithoutTchar(camp_start_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign/user returns at least 1 record when valid campaign_start_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid campaign_start_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign/user
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_start_date").toString(), DateUtils.convertISOWithTchar(campaign_start_date), "campaign/list api does not return campaigns according to passed campaign_start_date for filter.");
		   }
		}
	}
	
	@Test(priority=38)
	public void campaign_user_with_invalid_filter_operetor_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_start_date
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_start_date", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for campaign_start_date");
		test.assignCategory("CFA GET /campaign/user API");
		String campaign_start_date = camp_start_date;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_start_date"+encoded_operator+campaign_start_date));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_start_date");
				System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_start_date");
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object	
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result_data = json.get("result").toString(); 
				   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_start_date"+operator+campaign_start_date);
				   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_start_date");
				}	   
			}	
	}
	
	@Test(priority=39)	
	public void campaign_user_with_filter_for_nonexisting_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_start_date
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_start_date", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing campaign_start_date");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_camp_start_date");
		String campaign_start_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_start_date is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_start_date is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_start_date is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_start_date is passed.");
		}
	}	
	
	@Test(priority=40)
	public void campaign_user_with_valid_filter_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_end_date
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_end_date", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for campaign_end_date");
		test.assignCategory("CFA GET /campaign/user API");
		campaign_end_date = DateUtils.convertISOWithoutTchar(campaign_end_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_end_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign user does not return records when valid campaign_end_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign/user
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_end_date").toString(), DateUtils.convertISOWithTchar(campaign_end_date), "campaign/user api does not return campaigns according to passed campaign_end_date for filter.");
		   }
		   test.log(LogStatus.PASS, "Check campaign/user api returns campaigns according to passed campaign_end_date for filter.");
		}
	}
	
	@Test(priority=41)
	public void campaign_user_with_invalid_filter_operator_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_end_date
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_camp_end_date", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for campaign_end_date");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_camp_end_date");
		String campaign_end_date = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_end_date"+encoded_operator+campaign_end_date));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_end_date");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_end_date");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_end_date"+operator+campaign_end_date);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_end_date");
			}	   
		}	
	}
	
	@Test(priority=42)	
	public void campaign_user_with_filter_for_nonexisting_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_end_date
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_end_date", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing campaign_end_date");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_camp_end_date");
		String campaign_end_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_end_date is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_end_date is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_end_date is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_end_date is passed.");
		}
	}	
	
	@Test(priority=43)	
	public void campaign_user_with_valid_filter_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for group_id
		test = extent.startTest("campaign_user_with_valid_filter_for_group_id", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for group_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_group_id");
		String camp_group_id = group_id;
		String[] operators = {"=","<=",">="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid group_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid group_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))			   
					   Assert.assertEquals(campaign.get("group_id").toString(), camp_group_id, "campaign/user api does not return campaigns according to passed group_id for filter. Defect Reported: CT-17152");					   
				   else if(operator.equals(">="))			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())>=Integer.parseInt(camp_group_id), "campaign/user api does not return campaigns according to applied filter for group_id. Defect Reported: CT-17152");
				   else			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())<=Integer.parseInt(camp_group_id), "campaign/user api does not return campaigns according to applied filter for group_id"+Integer.parseInt(campaign.get("group_id").toString())+". Defect Reported: CT-17152");
				   test.log(LogStatus.PASS, "Check campaign/user api does not return campaigns according to passed group_id for filter.");
//				   JSONArray users_data = (JSONArray) campaign.get("users");
//				   Boolean user_exist = false;
//				   for(int j=0; j<users_data.size(); j++){
//					  JSONObject user = (JSONObject)users_data.get(j);
//					  String group_id = user.get("group_id").toString();
//					  if(group_id.equals(camp_group_id)){
//						  user_exist = true;
//					  }
//					  Assert.assertTrue(user_exist, "Passed user_id does not exist in users list of campaign/user response.");
//				   }
			   }
			}	
		}
	}	
	
	
	@Test(priority=44)
	public void campaign_user_with_invalid_filter_operator_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for group_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_group_id", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for group_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_group_id = group_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for group_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : group_id"+operator+camp_group_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for group_id");
			}	   
		}	
	}	
	
	@Test(priority=45)	
	public void campaign_user_with_filter_for_nonexisting_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_owner_user_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_group_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing group_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_group_id");
		String camp_group_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "group_id%3d"+camp_group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing group_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing group_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing group_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing group_id is passed.");
		}
	}	
	
	@Test(priority=46)	
	public void campaign_user_with_valid_filter_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for campaign_owner_user_id
		test = extent.startTest("campaign_user_with_valid_filter_for_camp_owner_id", "To validate whether user is able to get campaign through campaign/user api with valid filter for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign/user returns at least 1 record when valid campaign_owner_user_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid campaign_owner_user_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign/user
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))			   
					   Assert.assertEquals(campaign.get("campaign_owner_user_id").toString(), camp_owner_user_id, "campaign/user api does not return campaigns according to passed campaign_owner_user_id for filter.");
				   else if(operator.equals(">="))			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())>=Integer.parseInt(camp_owner_user_id), "campaign/user api does not return campaigns according to applied filter for campaign_owner_user_id");
				   else			   
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())<=Integer.parseInt(camp_owner_user_id), "campaign/user api does not return campaigns according to applied filter for campaign_owner_user_id");
				   test.log(LogStatus.PASS, "Check campaign/user api does not return campaigns according to passed campaign_owner_user_id for filter.");
			   }
			}	
		}
	}
	
	@Test(priority=47)
	public void campaign_user_with_invalid_filter_operator_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for campaign_owner_user_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_camp_owner_id", "To validate whether user is able to get campaign through campaign/user api with invalid filter operator for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for campaign_owner_user_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_owner_user_id"+operator+camp_owner_user_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_owner_user_id");
			}	   
		}	
	}	
	
//	@Test(priority=48)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_invalid_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid campaign_owner_user_id
		test = extent.startTest("campaign_user_with_filter_for_invalid_camp_owner_id", "To validate whether user is able to get campaign through campaign/user api with filter for invalid campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_camp_owner_id");
		String[] campaign_owner_user_id = test_data.get(4).split(",");
		for(String camp_owner_user_id:campaign_owner_user_id){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id%3d"+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+camp_owner_user_id+") campaign_owner_user_id is passed for filter");
			   test.log(LogStatus.PASS, "API returns success when invalid campaign_owner_user_id is passed for filter");
			   // Check whether campaign list returns record when invalid campaign_owner_user_id is passed for filter
			   Assert.assertEquals(err_data, "no records found", "Invalid err message is returned when invalid("+camp_owner_user_id+") campaign_owner_user_id is passed for filter. Defect Reported: CT-17153");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+camp_owner_user_id+") campaign_user_owner_id is passed for filter.");
			}
		}
	}
	
	@Test(priority=49)	
	public void campaign_user_with_filter_for_nonexisting_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing campaign_owner_user_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_camp_owner_id", "To validate whether user is able to get campaign through campaign/user api with filter for non existing campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_camp_owner_id");
		String camp_owner_user_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_owner_user_id%3d"+camp_owner_user_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_owner_user_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_owner_user_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_owner_user_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing campaign_owner_user_id is passed.");
		}
	}	
	
	@Test(priority=50)	
	public void campaign_user_with_valid_filter_for_ct_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for ct_user_id
		test = extent.startTest("campaign_user_with_valid_filter_for_ct_user_id", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for ct_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_ct_user_id");
		String ct_user_id = campaign_owner_user_id;
		String[] operators = {"="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ct_user_id"+encoded_operator+ct_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for ct_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid ct_user_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid ct_user_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   JSONArray users_data = (JSONArray) campaign.get("users");
				   Boolean user_exist = false;
				   for(int j=0; j<users_data.size(); j++){
					  JSONObject user = (JSONObject)users_data.get(j);
					  String user_id = user.get("ct_user_id").toString();
					  if(user_id.equals(ct_user_id)){
						  user_exist = true;
					  }
				   }
				   Assert.assertTrue(user_exist, "Passed user_id does not exist in campaign/user response.");
			   }
			}	
		}
	}	
	
	
	@Test(priority=51)
	public void campaign_user_with_invalid_filter_operator_for_ct_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for ct_user_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_ct_user_id", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for ct_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_ct_user_id");
		String ct_user_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ct_user_id"+encoded_operator+ct_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for ct_user_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for ct_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : ct_user_id"+operator+ct_user_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for ct_user_id");
			}	   
		}	
	}	
	
	@Test(priority=52)	
	public void campaign_user_with_filter_for_nonexisting_ct_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing ct_user_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_ct_user_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing ct_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_ct_user_id");
		String ct_user_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "ct_user_id%3d"+ct_user_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing ct_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing ct_user_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing ct_user_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing ct_user_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing ct_user_id is passed.");
		}
	}
	
//	@Test(priority=53)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_invalid_ct_user_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid ct_user_id
		test = extent.startTest("campaign_user_with_filter_for_invalid_ct_user_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for invalid ct_user_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_ct_user_id");
		String[] ct_user_ids = test_data.get(4).split(",");
		for(String ct_user_id:ct_user_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "ct_user_id%3d"+ct_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid ct_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid ct_user_id is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid ct_user_id is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+ct_user_id+") ct_user_id is passed in filter. Defect Reported: CT-17153");
			}
		}
	}	
	

	@Test(priority=54)		
	public void campaign_user_with_valid_filter_for_user_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{	
		// Execute campaign/user api with valid filter for user_ext_id	
		test = extent.startTest("campaign_user_with_valid_filter_for_user_ext_id", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for user_ext_id");	
		test.assignCategory("CFA GET /campaign/user API");	
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_user_ext_id");	
		String user_ext_id = "1234";	
		Map<String, Object> confGroupHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY);	
		String user_id=confGroupHierarchy.get(TestDataYamlConstants.UserConstants.ID).toString();	
			
//		DBUserUtils.updateExternalID(user_id, user_ext_id);	
			
		List<NameValuePair> list = new ArrayList<NameValuePair>();	
		list.add(new BasicNameValuePair("filter", "user_ext_id%3d"+user_ext_id));	
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);	
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());	
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for user_ext_id");	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
		String line = "";	
		while ((line = rd.readLine()) != null) {	
		   // Convert response to JSON object		
		   JSONParser parser = new JSONParser();	
		   JSONObject json = (JSONObject) parser.parse(line);	
		   JSONArray array = (JSONArray)json.get("data");	
		   // Check whether campaign list returns at least 1 record when valid user_ext_id is passed for filter	
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid user_ext_id is passed for filter.");	
		   for(int i=0; i<array.size(); i++){	
			   // Get the campaign from the campaign list	
			   JSONObject campaign = (JSONObject)array.get(i);	
			   JSONArray users_data = (JSONArray) campaign.get("users");	
			   Boolean user_exist = false;	
			   for(int j=0; j<users_data.size(); j++){	
				  JSONObject user = (JSONObject)users_data.get(j);	
				  if(user.get("user_ext_id")!=null){	
					  String user_ex_id = user.get("user_ext_id").toString();	
					  if(user_ex_id.equals(user_ext_id)){	
						  user_exist = true;	
					  }	
				  }	
			   }	
			   Assert.assertTrue(user_exist, "Passed user_ext_id does not exist in campaign/user response.");	
		   }	
		}		
	}	
	
	
	@Test(priority=55)
	public void campaign_user_with_invalid_filter_operator_for_user_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for user_ext_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_user_ext_id", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for user_ext_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_user_ext_id");
		String user_ext_id = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_ext_id"+encoded_operator+user_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for user_ext_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for user_ext_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : user_ext_id"+operator+user_ext_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for user_ext_id");
			}	   
		}	
	}	
	
	@Test(priority=56)	
	public void campaign_user_with_filter_for_nonexisting_user_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing user_ext_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_user_ext_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing user_ext_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_user_ext_id");
		String user_ext_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_ext_id%3d"+user_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing user_ext_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing user_ext_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing user_ext_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing user_ext_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing user_ext_id is passed.");
		}
	}
	
	@Test(priority=57)	
	public void campaign_user_with_valid_filter_for_first_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for first_name
		test = extent.startTest("campaign_user_with_valid_filter_for_first_name", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for first_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_first_name");
		String first_name = this.first_name;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "first_name%3d"+first_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for first_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid first_name is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid first_name is passed for filter.");
		   System.out.println("Array Size: "+array.size());
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   JSONArray users_data = (JSONArray) campaign.get("users");
			   Boolean user_exist = false;
			   for(int j=0; j<users_data.size(); j++){
				  JSONObject user = (JSONObject)users_data.get(j);
				  String f_name = user.get("first_name").toString();
				  if(f_name.equals(first_name)){
					  user_exist = true;
				  }
			   }
			   Assert.assertTrue(user_exist, "Passed first_name in filter does not exist in campaign/user response.");
		   }
		}	
	}	
	
	@Test(priority=58)
	public void campaign_user_with_invalid_filter_operator_for_first_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for first_name
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_first_name", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for first_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_first_name");
		String first_name = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "first_name"+encoded_operator+first_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for first_name");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for first_name");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : first_name"+operator+first_name);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for first_name");
			}	   
		}	
	}	
	
	@Test(priority=59)	
	public void campaign_user_with_filter_for_nonexisting_first_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing first_name
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_first_name", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing first_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_first_name");
		String first_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "first_name%3d"+first_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing first_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing first_name is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing first_name is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing first_name is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing first_name is passed.");
		}
	}
	
	@Test(priority=60)	
	public void campaign_user_with_valid_filter_for_last_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for first_name
		test = extent.startTest("campaign_user_with_valid_filter_for_last_name", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for last_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_last_name");
		String last_name = this.last_name;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "last_name%3d"+last_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for last_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid last_name is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid last_name is passed for filter.");
		   System.out.println("Array Size: "+array.size());
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   JSONArray users_data = (JSONArray) campaign.get("users");
			   Boolean user_exist = false;
			   for(int j=0; j<users_data.size(); j++){
				  JSONObject user = (JSONObject)users_data.get(j);
				  String l_name = user.get("last_name").toString();
				  if(l_name.equals(last_name)){
					  user_exist = true;
				  }
			   }
			   Assert.assertTrue(user_exist, "Passed last_name in filter does not exist in campaign/user response.");
		   }
		}	
	}	
	
	@Test(priority=61)
	public void campaign_user_with_invalid_filter_operator_for_last_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for last_name
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_last_name", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for last_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_last_name");
		String last_name = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "last_name"+encoded_operator+last_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for last_name");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for last_name");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : last_name"+operator+last_name);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for last_name");
			}	   
		}	
	}	
	
	@Test(priority=62)	
	public void campaign_user_with_filter_for_nonexisting_last_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing last_name
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_last_name", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing last_name");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_last_name");
		String last_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "last_name%3d"+last_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing last_name");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing last_name is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing last_name is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing last_name is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing last_name is passed.");
		}
	}
	
//	@Test(priority=63)	--trivial
	public void campaign_user_with_valid_filter_for_user_title() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for user_title
		test = extent.startTest("campaign_user_with_valid_filter_for_user_title", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for user_title");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_user_title");
		String user_title = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_title%3d"+user_title));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for user_title");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid user_title is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid user_title is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   JSONArray users_data = (JSONArray) campaign.get("users");
			   Boolean user_exist = false;
			   for(int j=0; j<users_data.size(); j++){
				  JSONObject user = (JSONObject)users_data.get(j);
				  if(user.get("user_title")!=null){
					  String user_title_value = user.get("user_title").toString();
					  if(user_title_value.equals(user_title)){
						  user_exist = true;
					  }
				  }
			   }
			   Assert.assertTrue(user_exist, "Passed user_title in filter does not exist in campaign/user response.");
		   }
		}	
	}	
	
	@Test(priority=64)
	public void campaign_user_with_invalid_filter_operator_for_user_title() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for user_title
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_user_title", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for user_title");
		test.assignCategory("CFA GET /campaign/user API");
		String user_title = "";
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_title"+encoded_operator+user_title));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for user_title");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for user_title");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : user_title"+operator+user_title);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for user_title");
			}	   
		}	
	}	
	
	@Test(priority=65)	
	public void campaign_user_with_filter_for_nonexisting_user_title() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing user_title
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_user_title", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing user_title");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_user_title");
		String user_title = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_title%3d"+user_title));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing user_title");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing user_title is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing user_title is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing user_title is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing user_title is passed.");
		}
	}
	
	@Test(priority=66)	
	public void campaign_user_with_valid_filter_for_user_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for user_status
		test = extent.startTest("campaign_user_with_valid_filter_for_user_status", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for user_status");
		test.assignCategory("CFA GET /campaign/user API");
		String[] status = {Constants.ComponentStatus.ACTIVE};
			for(String user_status: status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_status%3d"+user_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for user_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid user_status is passed for filter
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   JSONArray users_data = (JSONArray) campaign.get("users");
				   Boolean user_exist = false;
				   for(int j=0; j<users_data.size(); j++){
					  JSONObject user = (JSONObject)users_data.get(j);
					  String user_status_val = user.get("user_status").toString();
					  if(user_status_val.equals(user_status))
						  user_exist = true;
				   }
				   switch (i) {
				   case 0:
					if(user_exist == true)   
						test.log(LogStatus.PASS, "Passed user_status ("+user_status+") in filter exists in campaign/user response.");
		  			break;
				   }
				   Assert.assertTrue(user_exist, "Passed user_status in filter does not exist in campaign/user response.");
			   }
			}   
		}
	}	
	
	@Test(priority=67)
	public void campaign_user_with_invalid_filter_operator_for_user_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for user_status
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_user_status", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for user_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_user_status");
		String user_status = this.user_status;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_status"+encoded_operator+user_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for user_status");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for user_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : user_status"+operator+user_status);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for user_status");
			}	   
		}	
	}	
	
//	@Test(priority=68)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_nonexisting_user_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing user_status
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_user_status", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing user_status");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_user_status");
		String user_status = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_status%3d"+user_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing user_status");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing user_status is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing user_status is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing user_status is passed. Defect Reported: CT-17153");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing user_status is passed.");
		}
	}	
	
	@Test(priority=69)	
	public void campaign_user_with_valid_filter_for_role_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for role_id
		test = extent.startTest("campaign_user_with_valid_filter_for_role_id", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for role_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_role_id");
		String[] role_ids = test_data.get(4).split(",");
		for(String ct_user_role_id:role_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "role_id%3d"+ct_user_role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for role_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			    
			   String error = json.get("result").toString();
			   
			   if(!error.equals("error")) {
				// Check whether campaign list returns at least 1 record when valid role_id is passed for filter
				   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid role_id is passed for filter.");
				   for(int i=0; i<array.size(); i++){
					   // Get the campaign from the campaign list
					   JSONObject campaign = (JSONObject)array.get(i);
					   JSONArray users_data = (JSONArray) campaign.get("users");
					   Boolean user_exist = false;
					   for(int j=0; j<users_data.size(); j++){
						  JSONObject user = (JSONObject)users_data.get(j);
						  String role_id = user.get("role_id").toString();
						  if(role_id.equals(ct_user_role_id)){
							  user_exist = true;
						  }
					   }
					   switch (i) {
					   case 0:
						if(user_exist == true)   
							test.log(LogStatus.PASS, "Check passed user_id ("+ct_user_role_id+") in filter exists in campaign/user response.");
			  			break;
					   }
					   Assert.assertTrue(user_exist, "Passed user_id does not exist in campaign/user response.");
				   }
			   }
			}	
		}
	}	
	
	
	@Test(priority=70)
	public void campaign_user_with_invalid_filter_operator_for_role_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for role_id
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_role_id", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for role_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_role_id");
		String role_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "role_id"+encoded_operator+role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for role_id");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for role_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : role_id"+operator+role_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for role_id");
			}	   
		}	
	}	
	
	@Test(priority=71)	
	public void campaign_user_with_filter_for_nonexisting_role_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing role_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_role_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing role_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_role_id");
		String role_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "role_id%3d"+role_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing role_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing role_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing role_id is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing role_id is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing role_id is passed.");
		}
	}
	
//	@Test(priority=72)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_invalid_role_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid role_id
		test = extent.startTest("campaign_user_with_filter_for_invalid_role_id", "To validate whether user is able to get campaign and its users through campaign/user api with filter for invalid role_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_role_id");
		String[] user_role_ids = test_data.get(4).split(",");
		for(String role_id:user_role_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "role_id%3d"+role_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid role_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid role_id is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid role_id is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+role_id+") role_id is passed in filter. Defect Reported: CT-17153");
			}
		}
	}	
	
	@Test(priority=73)	
	public void campaign_user_with_valid_filter_for_user_email() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with valid filter for user_email
		test = extent.startTest("campaign_user_with_valid_filter_for_user_email", "To validate whether user is able to get campaign and its users through campaign/user api with valid filter for user_email");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_valid_filter_for_user_email");
		String ct_user_user_email = this.email;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_email%3d"+ct_user_user_email));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for user_email");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid user_email is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/user does not return records when valid user_email is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   JSONArray users_data = (JSONArray) campaign.get("users");
			   Boolean user_exist = false;
			   for(int j=0; j<users_data.size(); j++){
				  JSONObject user = (JSONObject)users_data.get(j);
				  String user_email = user.get("user_email").toString();
				  if(user_email.equals(ct_user_user_email)){
					  user_exist = true;
				  }
			   }
			   switch (i) {
			   case 0:
				if(user_exist == true)   
					test.log(LogStatus.PASS, "Check passed user_email ("+ct_user_user_email+") in filter exists in campaign/user response.");
	  			break;
			   }
			   Assert.assertTrue(user_exist, "Passed user_email does not exist in campaign/user response.");
		   }
		}	
	}	
	
	
	@Test(priority=74)
	public void campaign_user_with_invalid_filter_operator_for_user_email() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with invalid filter for user_email
		test = extent.startTest("campaign_user_with_invalid_filter_operator_for_user_email", "To validate whether user is able to get campaign and its users through campaign/user api with invalid filter operator for user_email");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_filter_operator_for_user_email");
		String ct_user_user_email = this.email;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_email"+encoded_operator+ct_user_user_email));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with "+ operator +" filter operator for user_email");
			System.out.println("Execute campaign/user api method with "+ operator +" filter operator for user_email");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString(); 
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : user_email"+operator+ct_user_user_email);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for user_email");
			}	   
		}	
	}	
	
	@Test(priority=75)	
	public void campaign_user_with_filter_for_nonexisting_user_email() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for non existing user_email
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_user_email", "To validate whether user is able to get campaign and its users through campaign/user api with filter for non existing user_email");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_user_email");
		String user_email = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "user_email%3d"+user_email));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for non existing user_email");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing user_email is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing user_email is entered for filter");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing user_email is passed.");
		   test.log(LogStatus.PASS, "Proper validation is displayed when non existing user_email is passed.");
		}
	}
	
	@Test(priority=76)	
	public void campaign_user_with_filter_for_invalid_user_email() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid user_email
		test = extent.startTest("campaign_user_with_filter_for_invalid_user_email", "To validate whether user is able to get campaign and its users through campaign/user api with filter for invalid user_email");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_user_email");
		String[] user_emails = test_data.get(4).split(",");
		for(String user_email:user_emails){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "user_email%3d"+user_email));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid user_email");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid user_email is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid user_email is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+user_email+") user_email is passed in filter. Defect Reported: CT-17153");
			}
		}
	}
	
	@Test(priority=77)
	public void campaign_user_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with agency admin access_token
		test = extent.startTest("campaign_user_with_agency_admin_access_token", "To validate whether user is able to get campaign and its users through campaign/user api with agency admin access_token");
		test.assignCategory("CFA GET /campaign/user API");

		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		 
		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign user returns 100 record by default
//		   Assert.assertEquals(array.size(), 500, "Campaign user does not return 500 records");
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false;
		   for(int i=0; i< array.size(); i++){
			   // Get the nth campaign from the campaign user
			   JSONObject campaign = (JSONObject) array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertTrue(agency_camp_present, "Agency level campaign is not present when campaign/user api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Agency level campaign is present when campaign/user api is executed with agency admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/user api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Company level campaign is present when campaign/user api is executed with agency admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/list api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Location level campaign is not present when campaign/user api is executed with agency admin access_token.");
		}   
	}
	
//	@Test(priority=78)	
	public void campaign_user_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with company admin access_token
		test = extent.startTest("campaign_user_with_company_admin_access_token", "To validate whether user is able to get campaign and its users through campaign/user api with company admin access_token");
		test.assignCategory("CFA GET /campaign/user API");
		
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		 
		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};
		
		String access_token = HelperClass.get_oauth_token("company@admin.com", "lmc2demo");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false, sibling_child_camp_present = false;
		   for(int i=0; i< array.size(); i++){
			   // Get the nth campaign from the campaign user
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/user api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/user api is executed with company admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/user api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/user api is executed with company admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/user api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/user api is executed with company admin access_token.");
		}   
	}
	
//	@Test(priority=79)	
	public void campaign_user_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with location admin access_token
		test = extent.startTest("campaign_user_with_location_admin_access_token", "To validate whether user is able to get campaign and its users through campaign/user api with location admin access_token");
		test.assignCategory("CFA GET /campaign/user API");
		
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		 
		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};
		
		String access_token = HelperClass.get_oauth_token("location@admin.com", "lmc2demo");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false, sibling_location_camp_present=false;
		   for(int i=0; i< array.size(); i++){
			   // Get the nth campaign from the campaign user
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/user api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/user api is executed with location admin access_token.");
		   Assert.assertFalse(company_camp_present, "Company level campaign is not present when campaign/user api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/user api is executed with location admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/user api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/user api is executed with location admin access_token.");
		}   
	}
	
//	@Test(priority=80)	-- Uncomment when defect will be fixed	
	public void campaign_user_with_filter_for_invalid_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/user api with filter for invalid campaign_id
		test = extent.startTest("campaign_user_with_filter_for_invalid_camp_id", "To validate whether user is able to get campaign through campaign/user api with filter for invalid campaign_id");
		test.assignCategory("CFA GET /campaign/user API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_invalid_camp_id");
		String[] campaign_ids = test_data.get(4).split(",");
		for(String camp_id:campaign_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid parameter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid campaign_id is entered for filter
			   Assert.assertEquals(result_data, "error", "API is returning success when invalid campaign_id is entered for filter");
			   String err_data = json.get("err").toString();
			   // Check validation message when invalid campaign_id is entered for filter
			   Assert.assertEquals(err_data, "campaign_id must be string", "Invalid validation message is displayed when invalid campaign_id is entered for filter.\nDefect Reported: CT-17153");
			}
		}
	}	
}
