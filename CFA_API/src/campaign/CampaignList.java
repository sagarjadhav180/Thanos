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
import java.util.List;
import java.util.Map;
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

import common.*;

@Listeners(Listener.class)
public class CampaignList extends BaseClass{
	String class_name = "Campaign_list";
	ArrayList<String> test_data;
	
	String campaign_created="", campaign_owner_user_id = "", group_id="",campaign_ext_id="", campaign_name="", owner_lname="", campaign_end_date="", campaign_id="", owner_fname="", camp_start_date="";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@BeforeClass
	public void campaign_list_parameter_setup() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid access_token
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
	}
	
	@Test(priority=1)
	public void campaign_list_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid access_token
		test = extent.startTest("campaign_list_with_valid_access_token", "To validate whether user is able to get campaign through campaign/list api with valid token");
		test.assignCategory("CFA GET /campaign/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {

		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign list
		   JSONObject first_campaign = (JSONObject)array.get(1);

		   test.log(LogStatus.PASS, "Check whether campaign list returns 100 record by default");
		   // Check response contains the fields
		   Assert.assertTrue(first_campaign.containsKey("campaign_modified"),"campaign/list api does not contain campaign_modified field.");
		   test.log(LogStatus.PASS, "Check whether campaign_modified field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_created"),"campaign/list api does not contain campaign_created field.");
		   test.log(LogStatus.PASS, "Check whether campaign_created field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_owner_user_id"),"campaign/list api does not contain campaign_owner_user_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_owner_user_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("group_id"),"campaign/list api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_ext_id"),"campaign/list api does not contain campaign_ext_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_ext_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_name"),"campaign/list api does not contain campaign_name field.");
		   test.log(LogStatus.PASS, "Check whether campaign_name field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("owner_lname"),"campaign/list api does not contain owner_lname field.");
		   test.log(LogStatus.PASS, "Check whether owner_lname field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_end_date"),"campaign/list api does not contain campaign_end_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_end_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_id"),"campaign/list api does not contain campaign_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("owner_fname"),"campaign/list api does not contain owner_fname field.");
		   test.log(LogStatus.PASS, "Check whether owner_fname field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_start_date"),"campaign/list api does not contain campaign_start_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_start_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_status"),"campaign/list api does not contain campaign_status field.");
		   test.log(LogStatus.PASS, "Check whether campaign_status field is present in response");
		   // Check data type of fields
		   Assert.assertTrue(first_campaign.get("campaign_created").getClass().getName().equals("java.lang.String"),"");
		   Assert.assertTrue(first_campaign.get("campaign_owner_user_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("group_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("campaign_name").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("owner_lname").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("campaign_id").getClass().getName().equals("java.lang.Long"));
		   Assert.assertTrue(first_campaign.get("owner_fname").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("campaign_start_date").getClass().getName().equals("java.lang.String"));
		   Assert.assertTrue(first_campaign.get("campaign_status").getClass().getName().equals("java.lang.String"));
		   test.log(LogStatus.PASS, "Check the data type of all fields of campaign/list api response");

		   // Check fields are not null
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_created"), "campaigm_created");
		   test.log(LogStatus.PASS, "Check campaign_created date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_owner_user_id"), "campaign_owner_user_id");
		   test.log(LogStatus.PASS, "Check campaign_owner_user_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("group_id"), "group_id");
		   test.log(LogStatus.PASS, "Check group_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_name"), "campaign_name");
		   test.log(LogStatus.PASS, "Check campaign_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("owner_lname"), "owner_lname");
		   test.log(LogStatus.PASS, "Check owner_lname date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_id"), "campaign_id");
		   test.log(LogStatus.PASS, "Check campaign_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("owner_fname"), "owner_fname");
		   test.log(LogStatus.PASS, "Check owner_fname date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_start_date"), "campaign_start_date");
		   test.log(LogStatus.PASS, "Check campaign_start_date date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_campaign.get("campaign_status"), "campaign_status");
		   test.log(LogStatus.PASS, "Check campaign_status date is not null or blank in response.");
		}
	}

	@Test(priority=2)
	public void campaign_list_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		// Execute campaign/list api method without access_token
		test = extent.startTest("campaign_list_without_access_token", "To validate whether user is able to get campaign through campaign/list api without access_token");
		test.assignCategory("CFA GET /campaign/list API");
		test.log(LogStatus.INFO, "Execute campaign/list api method without access_token");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/campaign/list", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when Authorization token is not passed");
		test.log(LogStatus.PASS, "Check status code when Authorization token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when Authorization token is not passed");
		test.log(LogStatus.PASS, "Check status message when Authorization token is not passed");
	}

	@Test(priority=3)
	public void campaign_list_with_blank_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_list_with_blank_access_token", "To validate whether user is able to get campaign through campaign/list api with blank access_token");
		test.assignCategory("CFA GET /campaign/list API");
		String blank_access_token = ""; List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", blank_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 400, "status code is not 400 when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status code when blank access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Bad Request", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when blank access_token is passed");
	}

	@Test(priority=4)
	public void campaign_list_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_list_with_invalid_token", "To validate whether user is able to get campaign through campaign/list api with invalid access_token");
		test.assignCategory("CFA GET /campaign/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}

	@Test(priority=5)
	public void campaign_list_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_list_with_expired_token", "To validate whether user is able to get campaign through campaign/list api with expired access_token");
		test.assignCategory("CFA GET /campaign/list API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}

	@Test(priority=6)
	public void campaign_list_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_blank_limit", "To validate whether user is able to get campaign through campaign/list api with blank limit");
		test.assignCategory("CFA GET /campaign/list API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
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


	@Test(priority=7)
	public void campaign_list_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/list api with invalid limit
		test = extent.startTest("campaign_list_with_invalid_limit", "To validate whether user is able to get campaign through campaign/list api with invalid limit");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_limit");
		String[] values = test_data.get(1).split(",");
		// Add parameters in request
		for(String limit_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("limit", limit_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
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

	@Test(priority=8)
	public void campaign_list_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_negative_limit", "To validate whether user is able to get campaign through campaign/list api with negative limit");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_negative_limit");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with negative limit value");
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
			list.add("/campaign/list");
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
			Assert.assertEquals(sub_error_message, "Value -5 is less than minimum 1", "Invalid message value is returned in response when negative limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative limit value is passed.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative limit value is passed.");
		}
	}

	@Test(priority=9)
	public void campaign_list_with_valid_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid limit value
		test = extent.startTest("campaign_list_with_valid_limit", "To validate whether user is able to get campaign through campaign/list api with valid limit value");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_valid_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.GROUP_LEVEL)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
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
		}
	}

	@Test(priority=10)
	public void campaign_list_with_gt_2000_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_gt_1000_limit", "To validate whether user is able to get campaign through campaign/list api with greater than 1000 limit");
		test.assignCategory("CFA GET /campaign/list API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.MAX_LIMIT + 1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with greater than "+Constants.MAX_LIMIT+" limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when greater than "+Constants.MAX_LIMIT+" limit value is passed");
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when greater than "+Constants.MAX_LIMIT+" limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when greater than "+Constants.MAX_LIMIT+" limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when greater than 1000 limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when greater than "+Constants.MAX_LIMIT+" limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/list");
			list.add("get");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MAXIMUM", "Invalid code value is returned in response when greater than "+Constants.MAX_LIMIT+" limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+String.valueOf(Constants.MAX_LIMIT + 1) +" is greater than maximum " + Constants.MAX_LIMIT, "Invalid message value is returned in response when greater than 1000 limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than 1000 limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 1000 limit value is passed.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 1000 limit value is passed.");
		}
	}

	@Test(priority=11)
	public void campaign_list_with_1000_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with 1000 limit
		test = extent.startTest("campaign_list_with_1000_limit", "To validate whether user is able to get campaign through campaign/list api with 1000 limit value");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_1000_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject response_json = (JSONObject) parser.parse(line);
		   JSONArray camp_data_array = (JSONArray)response_json.get("data");
		   System.out.println(camp_data_array.size());
		   Assert.assertEquals(response_json.get("result"), "success", "API does not return success when valid limit value is entered.");
		   test.log(LogStatus.PASS, "API returns success when valid 1000 value is entered.");
		   Assert.assertEquals(response_json.get("err"), null, "err is not null when 1000 limit value is entered.");
		   test.log(LogStatus.PASS, "err is null when valid limit value is entered.");
		   // Check whether campaign list returns number of records defined in limit
		   Assert.assertTrue(!(camp_data_array.size() > 1000), "Campaign list return more number of records defined in limit");
		   test.log(LogStatus.PASS, "Check whether campaign list returns success when 1000 limit value is passed");
		}
	}

	@Test(priority=12)
	public void campaign_list_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_blank_offset", "To validate whether user is able to get campaign through campaign/list api with blank offset");
		test.assignCategory("CFA GET /campaign/list API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with blank offset value");
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

	@Test(priority=13)
	public void campaign_list_with_blank_limit_and_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_blank_limit_and_offset", "To validate whether user is able to get campaign through campaign/list api with blank limit and offset");
		test.assignCategory("CFA GET /campaign/list API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		nvps.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with blank limit and offset value");
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

	@Test(priority=14)
	public void campaign_list_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_char_offset", "To validate whether user is able to get campaign through campaign/list api with invalid offset");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_offset");
		String[] values = test_data.get(2).split(",");
		// Add parameters in request
		for(String offset_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("offset", offset_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			if(offset_value.equals("abc"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with char offset value");
			else if(offset_value.equals("!@$#"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with special character offset value");
			else if(offset_value.equals("123abc"))
				test.log(LogStatus.INFO, "Execute campaign/list api method with alpahnumric character offset value");
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

	@Test(priority=15)
	public void campaign_list_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_negative_offset", "To validate whether user is able to get campaign through campaign/list api with negative offset");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_negative_offset");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset", test_data.get(2)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
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
			list.add("/campaign/list");
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
			Assert.assertEquals(sub_error_message, "Value -5 is less than minimum 0", "Invalid message value is returned in response when negative offset value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when negative offset value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Number of records from the beginning to start the data set from");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when negative offset value is passed.");
		}
	}

	@Test(priority=16)
	public void campaign_list_with_valid_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api without offset value access_token
		test = extent.startTest("campaign_list_with_valid_offset", "To validate whether user is able to get campaign through campaign/list api with valid offset value");
		test.assignCategory("CFA GET /campaign/list API");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		test.log(LogStatus.INFO, "Execute campaign/list api method without offset value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		List<String> camp_id = new ArrayList<String>();
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when parameter is not passed.");
		   test.log(LogStatus.PASS, "API returns success when parameter is not passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when parameter is not passed.");
		   test.log(LogStatus.PASS, "err is null when parameter is not passed.");
		   // Get the 10th campaign data from the campaign list
		   for(int i=0;i<Constants.GROUP_LEVEL-1;i++){
			   JSONObject nth_camp_data =(JSONObject) array.get(i);
			   String nth_camp_id = nth_camp_data.get("campaign_id").toString();
			   camp_id.add(nth_camp_id);
		   }
		}
	   // Execute campaign/list api method with offset value
	   nvps.add(new BasicNameValuePair("offset", String.valueOf(Constants.GROUP_LEVEL-1)));
	   response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
	   Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	   test.log(LogStatus.INFO, "Execute campaign/list api method with offset value");
	   rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	   line = "";
	   while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray) json.get("data");
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when valid offset value is entered.");
		   test.log(LogStatus.PASS, "API returns success when valid offset value is entered.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when valid offset value is entered.");
		   test.log(LogStatus.PASS, "err is null when valid offset value is entered.");
		   for (int i=0; i<array.size(); i++) {
			   JSONObject data = (JSONObject) array.get(i);
			   Assert.assertFalse(camp_id.contains(data.get("campaign_id").toString()), "Offset is not working properly.");
		   }
	   }
	}

	@Test(priority=17)
	public void campaign_list_with_valid_limit_and_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api without offset value
		test = extent.startTest("campaign_list_with_valid_limit_and_offset", "To validate whether user is able to get campaign through campaign/list api with valid limit and offset value");
		test.assignCategory("CFA GET /campaign/list API");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", String.valueOf(Constants.GROUP_LEVEL-1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method without offset value");
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
	   response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
	   test.log(LogStatus.INFO, "Execute campaign/list api method with offset value");
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

	@Test(priority=18)
	public void campaign_list_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_list_with_blank_filter", "To validate whether user is able to get campaign through campaign/list api with blank filter value");
		test.assignCategory("CFA GET /campaign/list API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with blank filter value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Assert.assertTrue(array.size()<=100);
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when blank filter value is passed.");
		   test.log(LogStatus.PASS, "API returns success when blank filter value is passed.");
		   Assert.assertEquals(json.get("err"), null, "err is not null when blank filter value is passed.");
		   test.log(LogStatus.PASS, "err is null when blank filter value is passed.");
		}
	}

	@Test(priority=19)
	public void campaign_list_with_valid_filter_for_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_id
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_id", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_id = campaign_id;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign list
		   JSONObject first_campaign = (JSONObject)array.get(0);
		   // Check whether campaign list returns 1 record valid campaign_id is passed for filter
		   Assert.assertEquals(array.size(), 1, "Campaign list does not return 1 record when valid campaign_id is passed for filter.");
		   Assert.assertEquals(first_campaign.get("campaign_id").toString(), camp_id, "campaign/list api does not return searched campaign when valid campaign_id is passed for filter.");
		}
	}

	@Test(priority=20)
	public void campaign_list_with_gtq_filter_for_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with greater than filter for campaign_id
		test = extent.startTest("campaign_list_with_gtq_filter_for_camp_id", "To validate whether user is able to get campaign through campaign/list api with greater than equal to filter for campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_id = campaign_id;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3e%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with greater than equal to filter for campaign_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");

		   Boolean element_exist = false;
		   for(int n = 0; n < array.size(); n++)
		   {
		       JSONObject object = (JSONObject)array.get(n);
		       if(object.get("campaign_id").toString().equals(camp_id)){
		    	   element_exist = true;
		       }
		       int campaign_id = Integer.parseInt(object.get("campaign_id").toString());
   			   Assert.assertTrue(campaign_id>=Integer.parseInt(camp_id),"campaign/list api return result which have campaign_id greater than equal to entered campaign_id. Found campaign_id: "+campaign_id);
		   }
		   test.log(LogStatus.PASS, "Check filter is working for >= operator for campaign_id");
		   Assert.assertTrue(element_exist,"Boundry value is not included when >= operator is used for campaign_id field to filter the result.");
		   test.log(LogStatus.PASS, "Check Boundry value is included when >= operator is used for campaign_id field to filter the result");
		}
	}

	@Test(priority=21)
	public void campaign_list_with_ltq_filter_for_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with greater than filter for campaign_id
		test = extent.startTest("campaign_list_with_ltq_filter_for_camp_id", "To validate whether user is able to get campaign through campaign/list api with less than equal to filter for campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_id = campaign_id;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3c%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with less than equal to filter for campaign_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Boolean element_exist = false;
		   for(int n = 0; n < array.size(); n++)
		   {
		       JSONObject object = (JSONObject)array.get(n);
		       if(object.get("campaign_id").toString().equals(camp_id)){
		    	   element_exist = true;
		       }
		       int campaign_id = Integer.parseInt(object.get("campaign_id").toString());
   			   Assert.assertTrue(campaign_id<=Integer.parseInt(camp_id),"campaign/list api return result which have campaign_id less than equal to entered campaign_id. Found campaign_id: "+campaign_id);
		   }
		   test.log(LogStatus.PASS, "Check filter is working for <= operator for campaign_id");
		   Assert.assertTrue(element_exist,"Boundry value is not included when <= operator is used for campaign_id field to filter the result.");
		   test.log(LogStatus.PASS, "Check Boundry value is included when <= operator is used for campaign_id field to filter the result");
		}
	}

	@Test(priority=22)
	public void campaign_list_with_invalid_filter_operetor_for_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with greater than filter for campaign_id
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_id", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_id = campaign_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id"+encoded_operator+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_id");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_id");
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


	@Test(priority=23)
	public void campaign_list_with_filter_for_nonexisting_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_id
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_id", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_camp_id");
		String camp_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when non existing campaign_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when non existing campaign_id is entered for filter
		   Assert.assertTrue(data_array.isEmpty(), "Data is not empty when non existing campaign_id is entered for filter");
		}
	}

	@Test(priority=24)
	public void campaign_list_with_filter_for_agency_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for company campaign_id
		test = extent.startTest("campaign_list_with_filter_for_agency_camp_id", "To validate whether user is able to get campaign through campaign/list api with filter for agency campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_id = campaign_id;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for agency campaign_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when agency campaign_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when agency campaign_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   // Check data field is blank when agency campaign_id is entered for filter
		   Assert.assertTrue(!data_array.isEmpty(), "Data is not empty when agency campaign_id is entered for filter");
		}
	}

	@Test(priority=25)
	public void campaign_list_with_filter_for_company_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for company campaign_id
		test = extent.startTest("campaign_list_with_filter_for_company_camp_id", "To validate whether user is able to get campaign through campaign/list api with filter for company campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_company_camp_id");
		String camp_id = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for company campaign_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when company campaign_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when company campaign_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   Assert.assertEquals(data_array.size(), 1, "Data array size is not 1 when company campaign_id is entered for filter");
		   // Check data field is blank when company campaign_id is entered for filter
		   JSONObject first_campaign = (JSONObject) data_array.get(0);
		   String campaign_id = first_campaign.get("campaign_id").toString();
		   Assert.assertEquals(campaign_id, camp_id, "API does not returned searched campaign");
		}
	}

	@Test(priority=26)
	public void campaign_list_with_filter_for_location_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for location campaign_id
		test = extent.startTest("campaign_list_with_filter_for_location_camp_id", "To validate whether user is able to get campaign through campaign/list api with filter for location campaign_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_location_camp_id");
		String camp_id = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for location campaign_id.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when location campaign_id is entered for filter
		   Assert.assertEquals(result_data, "success", "API is returning error when location campaign_id is entered for filter");
		   JSONArray data_array = (JSONArray)json.get("data");
		   Assert.assertEquals(data_array.size(), 1, "Data array size is not 1 when location campaign_id is entered for filter");
		   // Check data field is blank when location campaign_id is entered for filter
		   JSONObject first_campaign = (JSONObject) data_array.get(0);
		   String campaign_id = first_campaign.get("campaign_id").toString();
		   Assert.assertEquals(campaign_id, camp_id, "API does not returned searched campaign");
		}
	}

	@Test(priority=27)
	public void campaign_list_with_valid_filter_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_ext_id
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_ext_id", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_ext_id = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign list
		   JSONObject first_campaign = (JSONObject)array.get(0);
		   // Check whether campaign list returns 1 record valid campaign_ext_id is passed for filter
		   Assert.assertEquals(array.size(), 1, "Campaign list does not return 1 record when valid campaign_ext_id is passed for filter.");
		   Assert.assertEquals(first_campaign.get("campaign_ext_id").toString(), camp_ext_id, "campaign/list api does not return searched campaign when valid campaign_ext_id is passed for filter.");
		}
	}

	@Test(priority=28)
	public void campaign_list_with_invalid_filter_operetor_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with greater than filter for campaign_id
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_ext_id", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_filter_operetor_for_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_ext_id"+encoded_operator+camp_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_ext_id");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_ext_id");
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

	@Test(priority=29)
	public void campaign_list_with_filter_for_nonexisting_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_ext_id
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_ext_id", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_ext_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for non existing campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns 0 record non existing campaign_ext_id is passed for filter
		   Assert.assertEquals(array.size(), 0, "Campaign list does not return 1 record when non existing campaign_ext_id is passed for filter.");
		}
	}

//	@Test(priority=30)
	public void campaign_list_with_valid_filter_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_name
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_name", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_valid_filter_for_camp_name");
		String[] campaigns = test_data.get(4).split(",");
		String agency_camp = campaigns[0], company_camp = campaigns[1], location_camp = campaigns[2];
		String[] camp_names = {agency_camp,company_camp,location_camp};
		for(String camp_name:camp_names){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_name field.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray data_array = (JSONArray)json.get("data");
			   Assert.assertTrue(data_array.size()>=1, "API does not return result when valid campaign_name is passed for filter.");
			   // Get the first campaign from the campaign list
			   for(int i=0; i < data_array.size(); i++){
				   JSONObject campaign = (JSONObject)(data_array.get(i));
				   Assert.assertEquals(campaign.get("campaign_name").toString(), camp_name, "campaign/list api does not return searched campaign when valid campaign_name is passed for filter.");
			   }
			   if(camp_name.equals(agency_camp))
				   test.log(LogStatus.PASS, "User is able to filter agency level campaign from campaign/list.");
			   else if(camp_name.equals(company_camp))
				   test.log(LogStatus.PASS, "User is able to filter company level campaign from campaign/list.");
			   else if(camp_name.equals(location_camp))
				   test.log(LogStatus.PASS, "User is able to filter location level campaign from campaign/list.");
			}
		}
	}

	@Test(priority=31)
	public void campaign_list_with_invalid_filter_operetor_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with greater than filter for campaign_id
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_name", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_name");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_name = campaign_name;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name"+encoded_operator+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_name");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_name");
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

	@Test(priority=32)
	public void campaign_list_with_filter_for_non_existing_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_name
		test = extent.startTest("campaign_list_with_filter_for_non_existing_camp_name", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_non_existing_camp_name");
		String camp_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for non existing campaign_name.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray data_array = (JSONArray)json.get("data");
		   Assert.assertTrue(data_array.size()==0, "API is returning result when non existing campaign_name is passed for filter.");
		}
	}

	@Test(priority=33)
	public void campaign_list_with_valid_filter_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_status
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_status", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_status");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_valid_filter_for_camp_status");
		String[] camp_status = {Constants.ComponentStatus.ACTIVE, Constants.ComponentStatus.INACTIVE, Constants.ComponentStatus.DELETED};
		for(String camp_stat:camp_status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_status%3d"+camp_stat));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_status field.");
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
					   Assert.assertEquals(campaign.get("campaign_status").toString(), camp_stat, "campaign/list api does not filter campaign based on campaign_status field.");
				   }
			   } else {
				   Assert.assertEquals(json.get("err").toString(), "no records found");
			   }
			}
		}
	}

	@Test(priority=34)
	public void campaign_list_with_invalid_filter_operetor_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_status
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_status", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_status");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_filter_operetor_for_camp_status");
		String campaign_status = test_data.get(4);
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_status"+encoded_operator+campaign_status));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_status");
				System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_status");
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

	@Test(priority=35)
	public void campaign_list_with_filter_for_deleted_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for deleted campaign_name
		test = extent.startTest("campaign_list_with_filter_for_deleted_camp_status", "To validate whether user is able to get campaign through campaign/list api with valid filter for deleted campaign_status");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_deleted_camp_status");
		String campaign_status = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_status%3d"+campaign_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with filter for deleted campaign_status.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   JSONArray data_array = (JSONArray)json.get("data");
		   Assert.assertTrue(data_array.size()==0, "API is returning result when deleted campaign_status is passed for filter.");
		}
	}

	@Test(priority=36)
	public void campaign_list_with_valid_filter_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_created
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_created", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_created");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_created = campaign_created;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_created%3d"+ DateUtils.getDateFromDateTime(camp_created)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_created is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_created is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_created").toString(), DateUtils.getDateTimeFromDate(DateUtils.getDateFromDateTime(camp_created)), "campaign/list api does not return campaigns according to passed campaign_created for filter.");
		   }
		}
	}

	@Test(priority=37)
	public void campaign_list_with_invalid_filter_operetor_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_created
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_created", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_created = campaign_created;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_created"+encoded_operator+camp_created));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_created");
				System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_created");
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

	@Test(priority=38)
	public void campaign_list_with_filter_for_nonexisting_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_created
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_created", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_created");
		test.assignCategory("CFA GET /campaign/list API");
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
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "success", "API is returing error when non existing campaign_created date is passed in filter.");
			   Assert.assertEquals(json.get("err"), null, "err value is not null when non existing campaign_created date is passed in filter.");
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns record when non existing campaign_created is passed for filter
			   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing campaign_created is passed for filter.");
			}
		}
	}

	@Test(priority=39)
	public void campaign_list_with_valid_filter_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_modified
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_modified", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_modified");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_modified = UpdateComponents.updateCampaign(Constants.GroupHierarchy.AGENCY);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_modified is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_modified is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_modified").toString(), camp_modified, "campaign/list api does not return campaigns according to passed campaign_modified for filter.");
		   }
		}
	}

	@Test(priority=40)
	public void campaign_list_with_invalid_filter_operetor_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_modified
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_modified", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_filter_operetor_for_camp_modified");
		String campaign_modified = test_data.get(4);
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_modified"+encoded_operator+campaign_modified));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_modified");
				System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_modified");
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

	@Test(priority=41)
	public void campaign_list_with_filter_for_nonexisting_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_modified
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_modified", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_modified");
		test.assignCategory("CFA GET /campaign/list API");
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
		String[] campaign_modified = {"2016-09-04 19:47:53",tomorrow.toString()};
		for(String camp_modified:campaign_modified){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "success", "API is returing error when non existing campaign_modified date is passed in filter.");
			   Assert.assertEquals(json.get("err"), null, "err value is not null when non existing campaign_modified date is passed in filter.");
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns record when non existing campaign_modified is passed for filter
			   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing campaign_modified is passed for filter.");
			}
		}
	}

	@Test(priority=42)
	public void campaign_list_with_valid_filter_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_start_date
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_start_date", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_start_date");
		test.assignCategory("CFA GET /campaign/list API");
		String campaign_start_date = DateUtils.convertISOWithoutTchar(camp_start_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_start_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_start_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_start_date").toString(), DateUtils.convertISOWithTchar(campaign_start_date), "campaign/list api does not return campaigns according to passed campaign_start_date for filter.");
		   }
		}
	}

	@Test(priority=43)
	public void campaign_list_with_invalid_filter_operetor_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_start_date
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_start_date", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_start_date");
		test.assignCategory("CFA GET /campaign/list API");
		String campaign_start_date = camp_start_date;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_start_date"+encoded_operator+campaign_start_date));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_start_date");
				System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_start_date");
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

	@Test(priority=44)
	public void campaign_list_with_filter_for_nonexisting_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_start_date
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_start_date", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_start_date");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_camp_start_date");
		String campaign_start_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "success", "API is returing error when non existing campaign_start_date date is passed in filter.");
		   Assert.assertEquals(json.get("err"), null, "err value is not null when non existing campaign_start_date date is passed in filter.");
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns record when non existing campaign_start_date is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing campaign_start_date is passed for filter.");
		}
	}

	@Test(priority=45)
	public void campaign_list_with_valid_filter_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_end_date
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_end_date", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_end_date");
		test.assignCategory("CFA GET /campaign/list API");
		campaign_end_date = DateUtils.convertISOWithoutTchar(campaign_end_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_end_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_end_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_end_date").toString(), DateUtils.convertISOWithTchar(campaign_end_date), "campaign/list api does not return campaigns according to passed campaign_end_date for filter.");
		   }
		}
	}

	@Test(priority=46)
	public void campaign_list_with_invalid_filter_operator_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_end_date
		test = extent.startTest("campaign_list_with_invalid_filter_operator_for_camp_end_date", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_end_date");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_invalid_filter_operator_for_camp_end_date");
		String campaign_end_date = test_data.get(4);
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_end_date"+encoded_operator+campaign_end_date));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_end_date");
				System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_end_date");
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

	@Test(priority=47)
	public void campaign_list_with_filter_for_nonexisting_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_end_date
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_end_date", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_end_date");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_camp_end_date");
		String campaign_end_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "success", "API is returing error when non existing campaign_end_date date is passed in filter.");
		   Assert.assertEquals(json.get("err"), null, "err value is not null when non existing campaign_end_date date is passed in filter.");
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns record when non existing campaign_end_date is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing campaign_end_date is passed for filter.");
		}
	}

	@Test(priority=48)
	public void campaign_list_with_valid_filter_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for campaign_owner_user_id
		test = extent.startTest("campaign_list_with_valid_filter_for_camp_owner_id", "To validate whether user is able to get campaign through campaign/list api with valid filter for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid campaign_owner_user_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid campaign_owner_user_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))
					   Assert.assertEquals(campaign.get("campaign_owner_user_id").toString(), camp_owner_user_id, "campaign/list api does not return campaigns according to passed campaign_owner_user_id for filter.");
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())>=Integer.parseInt(camp_owner_user_id), "campaign/list api does not return campaigns according to applied filter for campaign_owner_user_id");
				   else
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())<=Integer.parseInt(camp_owner_user_id), "campaign/list api does not return campaigns according to applied filter for campaign_owner_user_id");
				   test.log(LogStatus.PASS, "Check campaign/list api does not return campaigns according to passed campaign_owner_user_id for filter.");
			   }
			}
		}
	}

	@Test(priority=49)
	public void campaign_list_with_invalid_filter_operator_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for campaign_owner_user_id
		test = extent.startTest("campaign_list_with_invalid_filter_operator_for_camp_owner_id", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for campaign_owner_user_id");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for campaign_owner_user_id");
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

	@Test(priority=50)
	public void campaign_list_with_filter_for_nonexisting_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_owner_user_id
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_camp_owner_id", "To validate whether user is able to get campaign through campaign/list api with filter for non existing campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_camp_owner_id");
		String camp_owner_user_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_owner_user_id%3d"+camp_owner_user_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when non existing campaign_owner_user_id is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing campaign_owner_user_id is passed for filter");
		   // Check whether campaign list returns at least 1 record when non existing campaign_owner_user_id is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing campaign_owner_user_id is passed for filter.");
		   test.log(LogStatus.PASS, "Check campaign/list api returns record when non existing campaign_user_owner_id is passed for filter.");
		}
	}

	@Test(priority=51)
	public void campaign_list_with_valid_filter_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for group_id
		test = extent.startTest("campaign_list_with_valid_filter_for_group_id", "To validate whether user is able to get campaign through campaign/list api with valid filter for group_id");
		test.assignCategory("CFA GET /campaign/list API");

		String camp_group_id = group_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns at least 1 record when valid group_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid group_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))
					   Assert.assertEquals(campaign.get("group_id").toString(), camp_group_id, "campaign/list api does not return campaigns according to passed group_id for filter.");
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())>=Integer.parseInt(camp_group_id), "campaign/list api does not return campaigns according to applied filter for group_id");
				   else
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())<=Integer.parseInt(camp_group_id), "campaign/list api does not return campaigns according to applied filter for group_id"+Integer.parseInt(campaign.get("group_id").toString()));
				   test.log(LogStatus.PASS, "Check campaign/list api does not return campaigns according to passed group_id for filter.");
			   }
			}
		}
	}


	@Test(priority=52)
	public void campaign_list_with_invalid_filter_operator_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for group_id
		test = extent.startTest("campaign_list_with_invalid_filter_operator_for_group_id", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for group_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_group_id = group_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for group_id");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for group_id");
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

	@Test(priority=53)
	public void campaign_list_with_filter_for_nonexisting_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing campaign_owner_user_id
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_group_id", "To validate whether user is able to get campaign through campaign/list api with filter for non existing group_id");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_group_id");
		String camp_group_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "group_id%3d"+camp_group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when non existing group_id is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing group_id is passed for filter");
		   // Check whether campaign list returns at least 1 record when non existing group_id is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing group_id is passed for filter.");
		   test.log(LogStatus.PASS, "Check campaign/list api returns record when non existing group_id is passed for filter.");
		}
	}

	@Test(priority=54)
	public void campaign_list_with_valid_filter_for_owner_fname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for owner_fname
		test = extent.startTest("campaign_list_with_valid_filter_for_owner_fname", "To validate whether user is able to get campaign through campaign/list api with valid filter for owner_fname");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_fname = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.UserConstants.FIRST_NAME).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "owner_fname%3d"+camp_owner_fname));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401),
			"Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for owner_fname");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid owner_fname is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid owner_fname is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("owner_fname").toString(), camp_owner_fname, "campaign/list api does not return campaigns according to passed owner_fname for filter.");
			   test.log(LogStatus.PASS, "Check campaign/list api does not return campaigns according to passed owner_fname for filter.");
		   }
		}
	}

	@Test(priority=55)
	public void campaign_list_with_invalid_filter_operator_for_owner_fname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for owner_fname
		test = extent.startTest("campaign_list_with_invalid_filter_operator_for_owner_fname", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for owner_fname");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_fname = owner_fname;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "owner_fname"+encoded_operator+camp_owner_fname));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for owner_fname");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for owner_fname");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : owner_fname"+operator+camp_owner_fname);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for owner_fname");
			}
		}
	}

	@Test(priority=56)
	public void campaign_list_with_filter_for_nonexisting_owner_fname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing owner_fname
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_owner_fname", "To validate whether user is able to get campaign through campaign/list api with filter for non existing owner_fname");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_owner_fname");
		String camp_owner_fname = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "owner_fname%3d"+camp_owner_fname));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for owner_fname");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when non existing owner_fname is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing owner_fname is passed for filter");
		   // Check whether campaign list returns at least 1 record when non existing owner_fname is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing owner_fname is passed for filter.");
		   test.log(LogStatus.PASS, "Check campaign/list api returns record when non existing owner_fname is passed for filter.");
		}
	}

	@Test(priority=57)
	public void campaign_list_with_valid_filter_for_owner_lname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with valid filter for owner_lname
		test = extent.startTest("campaign_list_with_valid_filter_for_owner_lname", "To validate whether user is able to get campaign through campaign/list api with valid filter for group_id");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_lname = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.UserConstants.LAST_NAME).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "owner_lname%3d"+camp_owner_lname));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for owner_lname");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);

		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid owner_lname is passed for filter
		   Assert.assertTrue(array.size()>=1, "Campaign list does not return records when valid owner_lname is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("owner_lname").toString(), camp_owner_lname, "campaign/list api does not return campaigns according to passed owner_lname for filter.");
			   test.log(LogStatus.PASS, "Check campaign/list api does not return campaigns according to passed owner_lname for filter.");
		   }
		}
	}

	@Test(priority=58)
	public void campaign_list_with_invalid_filter_operator_for_owner_lname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with invalid filter for owner_fname
		test = extent.startTest("campaign_list_with_invalid_filter_operator_for_owner_lname", "To validate whether user is able to get campaign through campaign/list api with invalid filter operator for owner_lname");
		test.assignCategory("CFA GET /campaign/list API");
		String camp_owner_lname = owner_lname;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "owner_lname"+encoded_operator+camp_owner_lname));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/list api method with "+ operator +" filter operator for owner_lname");
			System.out.println("Execute campaign/list api method with "+ operator +" filter operator for owner_lname");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : owner_lname"+operator+camp_owner_lname);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for owner_lname");
			}
		}
	}

	@Test(priority=59)
	public void campaign_list_with_filter_for_nonexisting_owner_lname() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with filter for non existing owner_lname
		test = extent.startTest("campaign_list_with_filter_for_nonexisting_owner_lname", "To validate whether user is able to get campaign through campaign/list api with filter for non existing owner_lname");
		test.assignCategory("CFA GET /campaign/list API");
		test_data = HelperClass.readTestData(class_name, "campaign_list_with_filter_for_nonexisting_owner_lname");
		String camp_owner_lname = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "owner_lname%3d"+camp_owner_lname));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/list api method with valid filter for owner_lname");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API returns error when non existing owner_lname is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing owner_lname is passed for filter");
		   // Check whether campaign list returns at least 1 record when non existing owner_lname is passed for filter
		   Assert.assertTrue(array.size()==0, "Campaign list returns records when non existing owner_lname is passed for filter.");
		   test.log(LogStatus.PASS, "Check campaign/list api returns record when non existing owner_lname is passed for filter.");
		}
	}

	@Test(priority=60)
	public void campaign_list_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with agency admin access_token
		test = extent.startTest("campaign_list_with_agency_admin_access_token", "To validate whether user is able to get campaign through campaign/list api with agency admin access_token");
		test.assignCategory("CFA GET /campaign/list API");

		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false, other_billing_camp = false;
		   for(int i=0; i< array.size(); i++){
			   // Get the nth campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertTrue(agency_camp_present, "Agency level campaign is not present when campaign/list api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Agency level campaign is present when campaign/list api is executed with agency admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/list api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Company level campaign is present when campaign/list api is executed with agency admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/list api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Location level campaign is not present when campaign/list api is executed with agency admin access_token.");
		}
	}

//	@Test(priority=61)
	public void campaign_list_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with company admin access_token
		test = extent.startTest("campaign_list_with_company_admin_access_token", "To validate whether user is able to get campaign through campaign/list api with company admin access_token");
		test.assignCategory("CFA GET /campaign/list API");

		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "1000"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
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
			   // Get the nth campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/list api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/list api is executed with company admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/list api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/list api is executed with company admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/list api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/list api is executed with company admin access_token.");
		}
	}

//	@Test(priority=62)
	public void campaign_list_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign_list api with location admin access_token
		test = extent.startTest("campaign_list_with_location_admin_access_token", "To validate whether user is able to get campaign through campaign/list api with location admin access_token");
		test.assignCategory("CFA GET /campaign/list API");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		 
		String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
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
			   // Get the nth campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/list api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/list api is executed with location admin access_token.");
		   Assert.assertFalse(company_camp_present, "Company level campaign is not present when campaign/list api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/list api is executed with location admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/list api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/list api is executed with location admin access_token.");
		}   
	}	
}
