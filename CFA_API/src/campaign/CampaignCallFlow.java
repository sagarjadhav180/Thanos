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
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.helper.TestDataConfigReader;
import com.convirza.tests.helper.UpdateComponents;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openxmlformats.schemas.presentationml.x2006.main.STIndex;
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
public class CampaignCallFlow extends BaseClass {

	String class_name = "CampaignCallFlow";
	ArrayList<String> test_data;
	String campaign_created="", campaign_owner_user_id = "", group_id="",campaign_ext_id="", campaign_name="", owner_lname="", campaign_end_date="", campaign_id="", owner_fname="", camp_start_date="";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	TestDataConfigReader configReader = new TestDataConfigReader();

	@BeforeClass
	public void campaign_user_parameter_setup() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid access_token
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
	public void campaign_callflow_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_user_with_invalid_access_token", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid access_token");
		test.assignCategory("CFA GET /campaign/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status message when invalid access_token is passed");
	}

	@Test(priority=2)
	public void campaign_callflow_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("campaign_callflow_with_expired_access_token", "To validate whether user is able to get campaigns and its associated callflow through campaign/callflow api with expired access_token");
		test.assignCategory("CFA GET /campaign/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status message when expired access_token is passed");
	}

	@Test(priority=3)
	public void campaign_callflow_with_valid_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid access_token
		test = extent.startTest("campaign_user_with_valid_access_token", "To validate whether user is able to get campaign and its callflows through campaign/user api with valid token");
		test.assignCategory("CFA GET /campaign/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid parameter");
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
		   Assert.assertTrue(array.size()<=100, "Campaign user does not return 100 records");
		   test.log(LogStatus.PASS, "Check whether campaign user returns 100 record by default");
		   // Check response contains the fields
		   Assert.assertTrue(first_campaign.containsKey("campaign_modified"),"campaign/callflow api does not contain campaign_modified field.");
		   test.log(LogStatus.PASS, "Check whether campaign_modified field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_created"),"campaign/callflow api does not contain campaign_created field.");
		   test.log(LogStatus.PASS, "Check whether campaign_created field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_owner_user_id"),"campaign/callflow api does not contain campaign_owner_user_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_owner_user_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("group_id"),"campaign/callflow api does not contain group_id field.");
		   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_ext_id"),"campaign/callflow api does not contain campaign_ext_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_ext_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_name"),"campaign/callflow api does not contain campaign_name field.");
		   test.log(LogStatus.PASS, "Check whether campaign_name field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_end_date"),"campaign/callflow api does not contain campaign_end_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_end_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_id"),"campaign/callflow api does not contain campaign_id field.");
		   test.log(LogStatus.PASS, "Check whether campaign_id field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_start_date"),"campaign/callflow api does not contain campaign_start_date field.");
		   test.log(LogStatus.PASS, "Check whether campaign_start_date field is present in response");
		   Assert.assertTrue(first_campaign.containsKey("campaign_status"),"campaign/callflow api does not contain campaign_status field.");
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

		   JSONArray callflows_data = (JSONArray) first_campaign.get("campaign_routes");
		   Assert.assertTrue(callflows_data.size()>0, "Users field does not contains any data");
		   test.log(LogStatus.INFO, "Check Users field in response contains data");
		   JSONObject first_callflow_data = (JSONObject)callflows_data.get(0);
		   Assert.assertTrue(first_callflow_data.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_flow_id field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("route_type"), "route_type field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify route_type field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_flow_name field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_flow_created field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_flow_modified field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_flow_status field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify repeat_interval field is present in campaign/user api response");
		   Assert.assertTrue(first_callflow_data.containsKey("call_value"), "call_value field is not present in campaign/user api response");
		   test.log(LogStatus.PASS, "Verify call_value field is present in campaign/user api response");

		   // Check callflow fields are not null
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("call_flow_id"), "call_flow_id");
		   test.log(LogStatus.PASS, "Check call_flow_id date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("route_type"), "route_type");
		   test.log(LogStatus.PASS, "Check route_type date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("call_flow_name"), "call_flow_name");
		   test.log(LogStatus.PASS, "Check call_flow_name date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("call_flow_created"), "call_flow_created");
		   test.log(LogStatus.PASS, "Check call_flow_created date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("call_flow_status"), "call_flow_status");
		   test.log(LogStatus.PASS, "Check call_flow_status date is not null or blank in response.");
		   HelperClass.multiple_assertnotEquals(first_callflow_data.get("repeat_interval"), "repeat_interval");
		   test.log(LogStatus.PASS, "Check repeat_interval date is not null or blank in response.");

		   // Get the nth campaign from the campaign callflow
		   for (int i=0; i<array.size(); i++) {
			   // Check duplicate callflow is not present in associated callflows in campaigns
			   JSONObject nth_campaign = (JSONObject)array.get(i);
			   JSONArray cam_callflows = (JSONArray) nth_campaign.get("campaign_routes");
			   ArrayList<String> camp_callflows_ids = new ArrayList<String>();
			   Boolean duplicate_callflow_presence_in_camp = false;
			   for(int j=0; j<cam_callflows.size(); j++){
				  JSONObject cam_user = (JSONObject) cam_callflows.get(j);
				  camp_callflows_ids.add(cam_user.get("call_flow_id").toString());
			   }
			   // Convert array list to set that so no duplicate callflow will be present
			   Set<String> set = new HashSet<String>(camp_callflows_ids);
			   if(set.size() < camp_callflows_ids.size()){
				   duplicate_callflow_presence_in_camp = true;
			   }
			   Assert.assertFalse(duplicate_callflow_presence_in_camp, "Duplicate callflow is present in associated callflow in campaign");
		   }
		   test.log(LogStatus.PASS, "Duplicate callflow is not present in associated callflow in campaign");
		}
	}

	@Test(priority=4)
	public void campaign_callflow_with_blank_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/callflow api with blank limit
		test = extent.startTest("campaign_callflow_with_blank_limit", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with blank limit");
		test.assignCategory("CFA GET /campaign/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with blank limit value");
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
	public void campaign_callflow_with_invalid_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/callflow api with invalid limit
		test = extent.startTest("campaign_user_with_invalid_limit", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid limit");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_invalid_limit");
		String[] values = test_data.get(1).split(",");
		// Add parameters in request
		for(String limit_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("limit", limit_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			if(limit_value.equals("abc"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with char limit value");
			else if(limit_value.equals("!@$#"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with special character limit value");
			else if(limit_value.equals("123abc"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with alpahnumric character limit value");
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
	public void campaign_callflow_with_negative_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_callflow_with_negative_limit", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with negative limit");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_negative_limit");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = test_data.get(1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with negative limit value");
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
			list.add("/campaign/callflow");
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
	public void campaign_callflow_with_0_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with 0 limit value
		test = extent.startTest("campaign_callflow_with_0_limit", "To validate whether user is able to get campaign and its callflows through campaign/user api with 0 limit value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_0_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = test_data.get(1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when 0 limit value is passed");
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when 0 limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when 0 limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when 0 limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when 0 limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/callflow");
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
	public void campaign_callflow_with_valid_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{	
		// Execute campaign/callflow api with valid limit value	
		test = extent.startTest("campaign_callflow_with_valid_limit", "To validate whether user is able to get campaign through campaign/callflow api with valid limit value");	
		test.assignCategory("CFA GET /campaign/callflow API");	
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_valid_limit");	
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();	
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));	
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);	
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
		   // Check whether campaign callflow returns number of records defined in limit	
		   Assert.assertTrue(camp_data_array.size()<=Integer.parseInt(test_data.get(1)), "Campaign callflow endpoint is not working for limit");	
		   test.log(LogStatus.PASS, "Check whether campaign callflow returns number of records defined in limit");  	
		   for(int i=0; i<camp_data_array.size(); i++){	
			   JSONObject campaign = (JSONObject) camp_data_array.get(i);	
			   JSONArray campaign_callflows = (JSONArray) campaign.get("campaign_routes");	
			   Assert.assertTrue(campaign_callflows.size()>=1,"Check campaign/callflow returns at least one callflow in every campaign.");	
			   Assert.assertFalse(campaign_callflows.size() == 20,"Callflows list is also limited with limit value");	
		   }	
		   test.log(LogStatus.INFO, "Check callflow list is not limited with limit value");	
		   test.log(LogStatus.PASS, "Check campaign/callflow returns at least one callflow in every campaign.");	
		}	
	}

	@Test(priority=9)
	public void campaign_callflow_with_gt_1000_limit() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_callflow_with_gt_1000_limit", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with greater than 1000 limit");
		test.assignCategory("CFA GET /campaign/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String limit_value = String.valueOf(Constants.MAX_LIMIT + 1);
		nvps.add(new BasicNameValuePair("limit", limit_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with greater than 1000 limit value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			String message = json.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when greater than "+ String.valueOf(Constants.MAX_LIMIT) +" limit value is passed");
			JSONArray errors_array = (JSONArray)json.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when greater than "+ String.valueOf(Constants.MAX_LIMIT) +" limit value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid in value is returned in response when greater than "+ String.valueOf(Constants.MAX_LIMIT) +" limit value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (limit): Value failed JSON Schema validation", "Invalid message value is returned in response when greater than "+ String.valueOf(Constants.MAX_LIMIT) +" limit value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "limit", "Invalid name value is returned in response when greater than "+ String.valueOf(Constants.MAX_LIMIT) +" limit value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/campaign/callflow");
			list.add("get");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "MAXIMUM", "Invalid code value is returned in response when greater than 1000 limit value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value "+limit_value+" is greater than maximum 2000", "Invalid message value is returned in response when greater than 1000 limit value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than 1000 limit value is passed");
			String sub_error_description = sub_error_data.get("description").toString();
			Assert.assertEquals(sub_error_description, "Maximum number of records to return");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 1000 limit value is passed.");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when greater than 1000 limit value is passed.");
		}
	}

	@Test(priority=10)
	public void campaign_callflow_with_1000_limit() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflows api with 1000 limit
		test = extent.startTest("campaign_callflow_with_1000_limit", "To validate whether user is able to get campaign and its callflows through campaign/callflows api with 1000 limit value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_1000_limit");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflows api method with valid parameter");
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
		   // Check whether campaign/callflows returns number of records defined in limit
		   Assert.assertFalse(camp_data_array.size() == 100, "campaign/callflows is returning 100 records");
		   test.log(LogStatus.PASS, "Check whether campaign/callflows returns success when 1000 limit value is passed");
		}
	}

	@Test(priority=11)
	public void campaign_callflow_with_blank_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/callflow api method with blank offset value
		test = extent.startTest("campaign_callflow_with_blank_offset", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with blank offset");
		test.assignCategory("CFA GET /campaign/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with blank offset value");
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
	public void campaign_callflow_with_blank_limit_and_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_callflow_with_blank_limit_and_offset", "To validate whether user is able to get campaigns and its callflows through campaign/user api with blank limit and offset");
		test.assignCategory("CFA GET /campaign/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", ""));
		nvps.add(new BasicNameValuePair("offset", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with blank limit and offset value");
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
			JSONArray error_path = (JSONArray) error_data.get("path");
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

	@Test(priority=13)
	public void campaign_callflow_with_invalid_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_callflow_with_invalid_offset", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid offset");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_offset");
		String[] values = test_data.get(2).split(",");
		// Add parameters in request
		for(String offset_value : values){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("offset", offset_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			if(offset_value.equals("abc"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with char offset value");
			else if(offset_value.equals("!@$#"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with special character offset value");
			else if(offset_value.equals("123abc"))
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with alpahnumric character offset value");
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
	public void campaign_callflow_with_negative_offset() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		// Execute campaign/callflow api with negative offset
		test = extent.startTest("campaign_callflow_with_negative_offset", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with negative offset");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_negative_offset");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String offset_value = test_data.get(2);
		nvps.add(new BasicNameValuePair("offset", offset_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with negative limit value");
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
			list.add("/campaign/callflow");
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
	public void campaign_callflow_with_0_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api without offset
		test = extent.startTest("campaign_callflow_with_0_offset", "To validate whether user is able to get campaigns and its callflows through campaign/user api with 0 offset value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_0_offset");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method without offset value");
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
		response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with 0 offset value");
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

	@Test(priority=16) //-- incorrect test case	
	public void campaign_callflow_with_valid_limit_and_offset() throws URISyntaxException, ClientProtocolException, IOException, ParseException{	
			
		// Execute campaign/callflow api without offset value	
		test = extent.startTest("campaign_callflow_with_valid_limit_and_offset", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid limit and offset value");	
		test.assignCategory("CFA GET /campaign/callflow API");	
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_valid_limit_and_offset");	
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();	
		nvps.add(new BasicNameValuePair("limit", test_data.get(1)));	
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);	
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());	
		test.log(LogStatus.INFO, "Execute campaign/callflow api method without offset value");	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
		String line = ""; String[] camp_id = new String[2];	
		while ((line = rd.readLine()) != null) {	
		   	
		   // Convert response to JSON object		
		   JSONParser parser = new JSONParser();	
		   JSONObject json = (JSONObject) parser.parse(line);	
		   JSONArray array = (JSONArray)json.get("data");	
		   Assert.assertEquals(json.get("result"), "success", "API does not return success when limit is passed and offset is not passed.");	
		   test.log(LogStatus.PASS, "API returns success when limit is passed and offset is not passed.");	
		   Assert.assertEquals(json.get("err"), null, "err is not null when limit is passed and offset is not passed.");	
		   test.log(LogStatus.PASS, "err is null when limit is passed and offset is not passed.");	
		  	
		   // Get the 1st campaign data from the campaign list	
		   for(int i=0;i<2;i++){	
			   JSONObject nth_camp_data =(JSONObject) array.get(i);	
			   String nth_camp_id = nth_camp_data.get("campaign_id").toString();	
			   camp_id[i] = nth_camp_id;	
		   }  	
		}	
	   // Execute campaign/callflow api method with offset value	
	   nvps.add(new BasicNameValuePair("offset", test_data.get(2)));	
	   response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);	
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
          	
		   //Get the 1st campaign data from the campaign list	
		   String[] cam_id_temp = new String[1];	
		   cam_id_temp = Arrays.copyOfRange(camp_id,0,1);	
		   boolean element_exist = false;	
		   for(String camp_id_val:cam_id_temp){	
			   for(int n = 0; n < array.size(); n++)	
			   {	
			       JSONObject object = (JSONObject)array.get(n);	
			       if(object.get("campaign_id").toString().equals(camp_id[1])){	
			    	   element_exist = true;	
			       }	
	   			   Assert.assertNotEquals(object.get("campaign_id").toString(), camp_id_val, "campaign is not skipped when valid limit and offset value are used");	
			   }	
		   }	
		   Assert.assertTrue(element_exist);	
	   }	
	}	

	@Test(priority=17)
	public void campaign_callflow_with_blank_filter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("campaign_callflow_with_blank_filter", "To validate whether user is able to get campaigns and its associated callflows through campaign/callflow api with blank filter value");
		test.assignCategory("CFA GET /campaign/callflow API");
		// Add parameters in request
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with blank filter value");
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

	@Test(priority=18)
	public void campaign_callflow_with_valid_filter_for_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_id
		test = extent.startTest("campaign_callflow_with_valid_filter_for_campaign_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for campaign_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_id = campaign_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id"+encoded_operator+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign/callflow returns at least 1 record when valid campaign_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign list
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("=")){
					   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "campaign/callflow api does not return campaigns according to passed campaign_id for filter.");
					   Assert.assertTrue(array.size()==1, "campaign/callflow is returning more than 1 records when = operator is used for campaign_id filter.");
					   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/callflow api does not contain campaign_modified field.");
					   test.log(LogStatus.PASS, "Check whether campaign_modified field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/callflow api does not contain campaign_created field.");
					   test.log(LogStatus.PASS, "Check whether campaign_created field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/callflow api does not contain campaign_owner_user_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_owner_user_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/callflow api does not contain group_id field.");
					   test.log(LogStatus.PASS, "Check whether group_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/callflow api does not contain campaign_ext_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_ext_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/callflow api does not contain campaign_name field.");
					   test.log(LogStatus.PASS, "Check whether campaign_name field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/callflow api does not contain campaign_end_date field.");
					   test.log(LogStatus.PASS, "Check whether campaign_end_date field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/callflow api does not contain campaign_id field.");
					   test.log(LogStatus.PASS, "Check whether campaign_id field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/callflow api does not contain campaign_start_date field.");
					   test.log(LogStatus.PASS, "Check whether campaign_start_date field is present in response");
					   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/callflow api does not contain campaign_status field.");
					   test.log(LogStatus.PASS, "Check whether campaign_status field is present in response");
					   // Check data type of fields
					   Assert.assertTrue(campaign.get("campaign_created").getClass().getName().equals("java.lang.String"),"");
					   Assert.assertTrue(campaign.get("campaign_owner_user_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("group_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("campaign_name").getClass().getName().equals("java.lang.String"));
					   Assert.assertTrue(campaign.get("campaign_id").getClass().getName().equals("java.lang.Long"));
					   Assert.assertTrue(campaign.get("campaign_start_date").getClass().getName().equals("java.lang.String"));
					   Assert.assertTrue(campaign.get("campaign_status").getClass().getName().equals("java.lang.String"));
					   test.log(LogStatus.PASS, "Check the data type of all fields of campaign/callflow api response");

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

					   JSONArray callflows_data = (JSONArray) campaign.get("campaign_routes");
					   Assert.assertTrue(callflows_data.size()>0, "campaign_routes field does not contains any data");
					   test.log(LogStatus.INFO, "Check Users field in response contains data");
					   JSONObject first_callflow_data = (JSONObject)callflows_data.get(0);
					   Assert.assertTrue(first_callflow_data.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify call_flow_name field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify repeat_interval field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("route_type"), "route_type field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify route_type field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("call_value"), "call_value field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify call_value field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify call_flow_created field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify call_flow_id field is present in campaign/callflow api response");
					   Assert.assertTrue(first_callflow_data.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
					   test.log(LogStatus.PASS, "Verify call_flow_status field is present in campaign/callflow api response");
				   }
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_id").toString())>=Integer.parseInt(camp_id), "campaign/callflow api does not return campaigns according to applied filter for campaign_id");
				   else
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_id").toString())<=Integer.parseInt(camp_id), "campaign/callflow api does not return campaigns according to applied filter for campaign_id"+Integer.parseInt(campaign.get("campaign_id").toString()));
				   test.log(LogStatus.PASS, "Check campaign/callflow api does not return campaigns according to passed campaign_id for filter.");
			   }
			}
		}
	}

	@Test(priority=19)
	public void campaign_callflow_with_invalid_filter_operator_for_campaign_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_campaign_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid filter operator for campaign_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_id = campaign_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id"+encoded_operator+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_id");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_id");
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
	public void campaign_callflow_with_filter_for_nonexisting_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_id
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with filter for non existing campaign_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_id");
		String camp_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid parameter");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_id is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_id is entered for filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_id is entered for filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_id is entered for filter.");
		}
	}

	
	//	@Test(priority=21) -- Uncomment when defect will be fixed
	public void campaign_callflow_with_filter_for_invalid_camp_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid campaign_id
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_camp_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with filter for invalid campaign_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_camp_id");
		String[] campaign_ids = test_data.get(4).split(",");
		for(String camp_id:campaign_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_id%3d"+camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid parameter");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid campaign_id is entered for filter
			   Assert.assertEquals(result_data, "error", "API is returning success when invalid("+camp_id+") campaign_id is entered for filter");
			   String err_data = json.get("err").toString();
			   // Check validation message when invalid campaign_id is entered for filter
			   Assert.assertEquals(err_data, "campaign_id must be string", "Invalid validation message is displayed when invalid("+camp_id+") campaign_id is entered for filter"+"\n Defect Reported: CT-17135");
			}
		}
	}

	@Test(priority=22)
	public void campaign_callflow_with_valid_filter_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_ext_id
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_ext_id", "To validate whether user is able to get campaigns and callflows through campaign/user api with valid filter for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_ext_id = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY)
			.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Get the first campaign from the campaign/callflow
		   JSONObject first_campaign = (JSONObject) array.get(0);
		   // Check whether campaign/callflow returns 1 record valid campaign_ext_id is passed for filter
		   Assert.assertEquals(array.size(), 1, "campaign/callflow does not return 1 record when valid campaign_ext_id is passed for filter.");
		   Assert.assertEquals(first_campaign.get("campaign_ext_id").toString(), camp_ext_id, "campaign/callflow api does not return searched campaign when valid campaign_ext_id is passed for filter.");
		}
		test.log(LogStatus.PASS, "campaign/callflow api return searched campaign when valid campaign_ext_id is passed for filter.");
	}

	@Test(priority=23)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operetor_for_camp_ext_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid filter operator for campaign_ext_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operetor_for_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_ext_id"+encoded_operator+camp_ext_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_ext_id");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_ext_id");
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

	@Test(priority=24)
	public void campaign_callflow_with_filter_for_nonexisting_camp_ext_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for non existing campaign_ext_id
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_ext_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with filter for non existing campaign_ext_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_ext_id");
		String camp_ext_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_ext_id%3d"+camp_ext_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for non existing campaign_ext_id field.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_ext_id is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_ext_id is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_ext_id is entered for filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_ext_id is entered for filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_ext_id is entered for filter.");
		}
	}

	@Test(priority=25)
	public void campaign_callflow_with_valid_filter_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_name
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_name", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		String[] campaigns = {(String) configReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY, TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME),
													(String) configReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY, TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME),
													(String) configReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION, TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME)};
		String agency_camp = campaigns[0], company_camp = campaigns[1], location_camp = campaigns[2];
		String[] camp_names = {agency_camp,company_camp,location_camp};
		for(String camp_name:camp_names){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_name field.");
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
				   Assert.assertEquals(campaign.get("campaign_name").toString(), camp_name, "campaign/callflow api does not return searched campaign when valid campaign_name is passed for filter.");
			   }
			   if(camp_name.equals(agency_camp))
				   test.log(LogStatus.PASS, "User is able to filter agency level campaign from campaign/callflow api.");
			   else if(camp_name.equals(company_camp))
				   test.log(LogStatus.PASS, "User is able to filter company level campaign from campaign/callflow api.");
			   else if(camp_name.equals(location_camp))
				   test.log(LogStatus.PASS, "User is able to filter location level campaign from campaign/callflow api.");
			}
		}
	}

	@Test(priority=26)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with greater than filter for campaign_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operetor_for_camp_name", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid filter operator for campaign_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_name = campaign_name;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_name"+encoded_operator+camp_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_name");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_name");
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

	@Test(priority=27)
	public void campaign_callflow_with_filter_for_non_existing_camp_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_name
		test = extent.startTest("campaign_callflow_with_filter_for_non_existing_camp_name", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for campaign_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_non_existing_camp_name");
		String camp_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_name%3d"+camp_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for non existing campaign_name.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when non existing campaign_name is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when non existing campaign_name is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign_name is entered for filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_name is entered for filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_name is entered for filter.");
		}
	}

	@Test(priority=28)
	public void campaign_callflow_with_valid_filter_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_status
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_status", "To validate whether user is able to get campaigns and its callflows through campaign/user api with valid filter for campaign_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		String[] camp_status = {Constants.ComponentStatus.ACTIVE, Constants.ComponentStatus.INACTIVE};
		for(String camp_stat:camp_status) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_status%3d"+camp_stat));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/user api method with valid filter for campaign_status field.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   if (json.get("result").toString().equalsIgnoreCase("error")) {
			   	 Assert.assertEquals(json.get("err").toString(), "no records found");
			   } else {
				   Assert.assertEquals(json.get("result").toString(), "success", "Error is returned");
				   JSONArray data_array = (JSONArray)json.get("data");
				   Assert.assertTrue(data_array.size()>=1, "API does not return result when valid campaign_status is passed for filter.");
				   System.out.println(data_array.size());
				   for(int i=0; i < data_array.size(); i++){
					   JSONObject campaign = (JSONObject)(data_array.get(i));
					   System.out.println(campaign.get("campaign_status").toString());
					   Assert.assertEquals(campaign.get("campaign_status").toString(), camp_stat, "campaign/user api does not filter campaign based on campaign_status field.");
				   }
			   }
			}
		}
	}

	@Test(priority=29)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_status
		test = extent.startTest("campaign_callflow_with_invalid_filter_operetor_for_camp_status", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid filter operator for campaign_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operetor_for_camp_status");
		String campaign_status = test_data.get(4);
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_status"+encoded_operator+campaign_status));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_status");
				System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_status");
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

	@Test(priority=30)
	public void campaign_callflow_with_filter_for_deleted_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for deleted campaign_name
		test = extent.startTest("campaign_callflow_with_filter_for_deleted_camp_status", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for deleted campaign_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_deleted_camp_status");
		String campaign_status = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_status%3d"+campaign_status));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for deleted campaign_status.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether API is returning error when deleted campaign_status is entered for filter
		   Assert.assertEquals(result_data, "error", "API is returning success when deleted campaign_status is entered for filter");
		   test.log(LogStatus.PASS, "API is returning error when deleted campaign_status is entered for filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when deleted campaign_status is entered for filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when deleted campaign_status is entered for filter.");
		}
	}


//	@Test(priority=31)	-- Uncomment when defect will be fixed	
	public void campaign_callflow_with_filter_for_invalid_camp_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for deleted campaign_name
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_camp_status", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for invalid campaign_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_camp_status");
		String[] camp_status = test_data.get(4).split(",");
		test.log(LogStatus.INFO, "Execute campaign/user api method with filter for invalid campaign_status.");
		for(String campaign_status:camp_status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_status%3d"+campaign_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether API is returning error when invalid campaign_status is entered for filter
			   Assert.assertEquals(result_data, "error", "API is returning success when invalid campaign_status is entered for filter");
			   test.log(LogStatus.PASS, "API is returning error when invalid campaign_status is entered for filter.");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when invalid campaign_status is entered for filter."+"\n Defect Reported: CT-17135");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid campaign_status is entered for filter.");
			}
		}
	}

	@Test(priority=32)
	public void campaign_callflow_with_valid_filter_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_created
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_created", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for campaign_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_created = campaign_created;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_created%3d"+ DateUtils.getDateFromDateTime(camp_created)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid campaign_created is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_created is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_created").toString(), DateUtils.getDateTimeFromDate(DateUtils.getDateFromDateTime(camp_created)), "campaign/callflow api does not return campaigns according to passed campaign_created for filter.");
		   }
		   test.log(LogStatus.PASS, "campaign/callflow api returns campaigns according to passed campaign_created for filter.");
		}
	}

	@Test(priority=33)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_created
		test = extent.startTest("campaign_callflow_with_invalid_filter_operetor_for_camp_created", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_created = campaign_created;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_created"+encoded_operator+camp_created));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_created");
				System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_created");
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

	@Test(priority=34)
	public void campaign_callflow_with_filter_for_nonexisting_camp_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_created
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_created", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing campaign_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_created");
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
		String[] campaign_created = {test_data.get(4),tomorrow.toString()};
		for(String camp_created:campaign_created){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_created%3d"+camp_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "error", "API is returing success when non existing campaign_created date is passed in filter.");
			   test.log(LogStatus.PASS, "API is returning error non existing campaign_created date is passed in filter.");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_created date is passed in filter.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_created date is passed in filter.");
			}
		}
	}

	@Test(priority=35)
	public void campaign_callflow_with_valid_filter_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_modified
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for campaign_modified");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_modified = UpdateComponents.updateCampaign(Constants.GroupHierarchy.AGENCY);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/user", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Inupdated_atvalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid campaign_modified is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_modified is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_modified").toString(), camp_modified, "campaign/callflow api does not return campaigns according to passed campaign_modified for filter.");
		   }
		   test.log(LogStatus.PASS, "campaign/callflow api returns campaigns according to passed campaign_modified for filter.");
		}
	}

	@Test(priority=36)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_modified
		test = extent.startTest("campaign_callflow_with_invalid_filter_operetor_for_camp_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for campaign_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_valid_filter_for_camp_modified");
		String camp_modified = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_modified"+encoded_operator+camp_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_modified");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_modified");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : campaign_modified"+operator+camp_modified);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for campaign_modified");
			}
		}
	}

	@Test(priority=37)
	public void campaign_callflow_with_filter_for_nonexisting_camp_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_modified
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing campaign_modified");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_modified");
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
		String[] campaign_modified = {test_data.get(4),tomorrow.toString()};
		for(String camp_modified:campaign_modified){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_modified%3d"+camp_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for non existing campaign_modified");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "error", "API is returing success when non existing campaign_modified date is passed in filter.");
			   test.log(LogStatus.PASS, "API is returning error non existing campaign_modified date is passed in filter.");
			   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_modified date is passed in filter.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_modified date is passed in filter.");
			}
		}
	}

	@Test(priority=38)
	public void campaign_callflow_with_valid_filter_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_start_date
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_start_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for campaign_start_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		String campaign_start_date = DateUtils.convertISOWithoutTchar(camp_start_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign list returns at least 1 record when valid campaign_start_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_start_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaigns
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_start_date").toString(), DateUtils.convertISOWithTchar(campaign_start_date), "campaign/callflow api does not return campaigns according to passed campaign_start_date for filter.");
		   }
		   test.log(LogStatus.PASS, "campaign/callflow api return campaigns according to passed campaign_start_date for filter.");
		}
	}

	@Test(priority=39)
	public void campaign_callflow_with_invalid_filter_operetor_for_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_start_date
		test = extent.startTest("campaign_list_with_invalid_filter_operetor_for_camp_start_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for campaign_start_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		String campaign_start_date = DateUtils.convertISOWithoutTchar(camp_start_date) + "+00";;
			String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
			String encoded_operator = "";
			for(String operator:operators){
				encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "campaign_start_date"+encoded_operator+campaign_start_date));
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_start_date");
				System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_start_date");
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

	@Test(priority=40)
	public void campaign_callflow_with_filter_for_nonexisting_camp_start_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_start_date
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_start_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing campaign_start_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_start_date");
		String campaign_start_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_start_date%3d"+campaign_start_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_start_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "error", "API is returing success when non existing campaign_start_date date is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing campaign_start_date date is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_start_date date is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_start_date date is passed in filter.");
		}
	}

	@Test(priority=41)
	public void campaign_callflow_with_valid_filter_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_end_date
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_end_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for campaign_end_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		campaign_end_date = DateUtils.convertISOWithoutTchar(campaign_end_date) + "+00";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid campaign_end_date is passed for filter
		   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_end_date is passed for filter.");
		   for(int i=0; i<array.size(); i++){
			   // Get the campaign from the campaign list
			   JSONObject campaign = (JSONObject)array.get(i);
			   Assert.assertEquals(campaign.get("campaign_end_date").toString(), DateUtils.convertISOWithTchar(campaign_end_date), "campaign/callflow api does not return campaigns according to passed campaign_end_date for filter.");
		   }
		   test.log(LogStatus.PASS, "Check campaign/callflow api returns campaigns according to passed campaign_end_date for filter.");
		}
	}

	@Test(priority=42)
	public void campaign_callflow_with_invalid_filter_operator_for_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_end_date
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_camp_end_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for campaign_end_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_camp_end_date");
		String campaign_end_date = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_end_date"+encoded_operator+campaign_end_date));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_end_date");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_end_date");
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

	@Test(priority=43)
	public void campaign_callflow_with_filter_for_nonexisting_camp_end_date() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_end_date
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_end_date", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing campaign_end_date");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_end_date");
		String campaign_end_date = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_end_date%3d"+campaign_end_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_end_date");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "error", "API is returing success when non existing campaign_end_date is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing campaign_end_date is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_end_date is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_end_date is passed in filter.");
		}
	}

	@Test(priority=44)
	public void campaign_callflow_with_valid_filter_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for group_id
		test = extent.startTest("campaign_callflow_with_valid_filter_for_group_id", "To validate whether user is able to get campaigns and its callflows through campaign/callflow api with valid filter for group_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_group_id = group_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for group_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign/callflow returns at least 1 record when valid group_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid group_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign/callflow
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))
					   Assert.assertEquals(campaign.get("group_id").toString(), camp_group_id, "campaign/callflow api does not return campaigns according to passed group_id for filter.");
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())>=Integer.parseInt(camp_group_id), "campaign/callflow api does not return campaigns according to applied filter for group_id");
				   else
					   Assert.assertTrue(Integer.parseInt(campaign.get("group_id").toString())<=Integer.parseInt(camp_group_id), "campaign/callflow api does not return campaigns according to applied filter for group_id"+Integer.parseInt(campaign.get("group_id").toString()));
				   test.log(LogStatus.PASS, "Check campaign/callflow api does not return campaigns according to passed group_id for filter.");
			   }
			}
		}
	}


	@Test(priority=45)
	public void campaign_callflow_with_invalid_filter_operator_for_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for group_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_group_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for group_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_group_id = group_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "group_id"+encoded_operator+camp_group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for group_id");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for group_id");
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

	@Test(priority=46)
	public void campaign_callflow_with_filter_for_nonexisting_group_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing group_id
		test = extent.startTest("campaign_user_with_filter_for_nonexisting_group_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing group_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_user_with_filter_for_nonexisting_group_id");
		String camp_group_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "group_id%3d"+camp_group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for group_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returing success when non existing group_id is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing group_id is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing group_id is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing group_id is passed in filter.");
		}
	}

	@Test(priority=47)
	public void campaign_callflow_with_valid_filter_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for campaign_owner_user_id
		test = extent.startTest("campaign_callflow_with_valid_filter_for_camp_owner_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"=","<=","<="};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_owner_user_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign/callflow returns at least 1 record when valid campaign_owner_user_id is passed for filter
			   Assert.assertTrue(array.size()>=1, "campaign/callflow does not return records when valid campaign_owner_user_id is passed for filter.");
			   for(int i=0; i<array.size(); i++){
				   // Get the campaign from the campaign/callflow
				   JSONObject campaign = (JSONObject)array.get(i);
				   if(operator.equals("="))
					   Assert.assertEquals(campaign.get("campaign_owner_user_id").toString(), camp_owner_user_id, "campaign/callflow api does not return campaigns according to passed campaign_owner_user_id for filter.");
				   else if(operator.equals(">="))
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())>=Integer.parseInt(camp_owner_user_id), "campaign/callflow api does not return campaigns according to applied filter for campaign_owner_user_id");
				   else
					   Assert.assertTrue(Integer.parseInt(campaign.get("campaign_owner_user_id").toString())<=Integer.parseInt(camp_owner_user_id), "campaign/callflow api does not return campaigns according to applied filter for campaign_owner_user_id");
				   test.log(LogStatus.PASS, "Check campaign/callflow api does not return campaigns according to passed campaign_owner_user_id for filter.");
			   }
			}
		}
	}

	@Test(priority=48)
	public void campaign_callflow_with_invalid_filter_operator_for_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for campaign_owner_user_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_camp_owner_id", "To validate whether user is able to get campaign through campaign/callflow api with invalid filter operator for campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		String camp_owner_user_id = campaign_owner_user_id;
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id"+encoded_operator+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for campaign_owner_user_id");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for campaign_owner_user_id");
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

	
//	@Test(priority=49)	-- Uncomment when defect will be fixed	
	public void campaign_callflow_with_filter_for_invalid_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid campaign_owner_user_id
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_camp_owner_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_camp_owner_id");
		String[] campaign_owner_user_id = test_data.get(4).split(",");
		for(String camp_owner_user_id:campaign_owner_user_id){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "campaign_owner_user_id%3d"+camp_owner_user_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for invalid campaign_owner_user_id");
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
			   // Check whether campaign/callflow returns record when invalid campaign_owner_user_id is passed for filter
			   Assert.assertEquals(err_data, "No record found", "Invalid err message is returned when invalid("+camp_owner_user_id+") campaign_owner_user_id is passed for filter."+"\n Defect Reported CT-16135");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+camp_owner_user_id+") campaign_user_owner_id is passed for filter.");
			}
		}
	}

	@Test(priority=50)
	public void campaign_callflow_with_filter_for_nonexisting_camp_owner_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing campaign_owner_user_id
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_camp_owner_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing campaign_owner_user_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_camp_owner_id");
		String camp_owner_user_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "campaign_owner_user_id%3d"+camp_owner_user_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for campaign_owner_user_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returing success when non existing campaign_owner_user_id is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing campaign_owner_user_id is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing campaign_owner_user_id is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing campaign_owner_user_id is passed in filter.");
		}
	}

	@Test(priority=51)
	public void campaign_callflow_with_valid_filter_for_callflow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_flow_id
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_flow_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_flow_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String,Object> callflowAgencyData = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id = String.valueOf(callflowAgencyData.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID)),
			camp_id_of_callflow = String.valueOf(callflowAgencyData.get(TestDataYamlConstants.CallflowConstants.CAMPAIGN_ID));
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_id is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid call_flow_id is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_flow_id is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid call_flow_id is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at 1 record when valid call_flow_id is passed for filter
		   Assert.assertTrue(data.size()==1, "campaign/callflow does not return 1 record when valid call_flow_id is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return 1 record when valid call_flow_id is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   Assert.assertEquals(campaign_data.get("campaign_id").toString(), camp_id_of_callflow, "Invalid campaign_id is displayed when valid call_flow_id is passed for filter.");
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");
			   if(campaign_route.get("call_flow_id").toString().equals(call_flow_id))
				   call_flow_exist = true;
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response");
		}
	}


//	@Test(priority=52)	-- Uncomment when defect will be fixed	
	public void campaign_callflow_with_filter_for_invalid_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid call_flow_id
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_call_flow_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid call_flow_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_call_flow_id");
		String[] call_flow_ids = test_data.get(4).split(",");
		for(String call_flow_id:call_flow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for invalid call_flow_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+call_flow_id+") call_flow_id is passed for filter");
			   test.log(LogStatus.PASS, "API returns success when invalid call_flow_id is passed for filter");
			   // Check whether campaign/callflow returns record when invalid campaign_owner_user_id is passed for filter
			   Assert.assertEquals(err_data, "No record found", "Invalid err message is returned when invalid("+call_flow_id+") call_flow_id is passed for filter."+"\n Defect Reported: CT-17135");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+call_flow_id+") call_flow_id is passed for filter.");
			}
		}
	}


	@Test(priority=53)
	public void campaign_callflow_with_invalid_filter_operator_for_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_flow_id
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_flow_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_flow_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_flow_id");
		String call_flow_id = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_id"+encoded_operator+call_flow_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_id");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_id");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_id"+operator+call_flow_id);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_id");
			}
		}
	}

	@Test(priority=54)
	public void campaign_callflow_with_filter_for_nonexisting_call_flow_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing call_flow_id
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_call_flow_id", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing call_flow_id");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_call_flow_id");
		String call_flow_id = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+call_flow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_id");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returing success when non existing call_flow_id is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing call_flow_id is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing call_flow_id is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing call_flow_id is passed in filter.");
		}
	}

	@Test(priority=55)
	public void campaign_callflow_with_valid_filter_for_route_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for route_type
		test = extent.startTest("campaign_callflow_with_valid_filter_for_route_type", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for route_type");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String,Object> callflowAgencyData = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String route_type = String.valueOf(callflowAgencyData.get(TestDataYamlConstants.CallflowConstants.ROUTABLE_TYPE));
		if (route_type.toLowerCase().contains(Constants.CallFlowCategory.SIMPLE)) {
			route_type = Constants.CallFlowCategory.SIMPLE;
		} else if (route_type.toLowerCase().contains(Constants.CallFlowCategory.IVR)) {
			route_type = Constants.CallFlowCategory.IVR;
		} else if (route_type.toLowerCase().contains(Constants.CallFlowCategory.GEO)) {
			route_type = Constants.CallFlowCategory.GEO;
		} else if (route_type.toLowerCase().contains(Constants.CallFlowCategory.PERCENTAGE)) {
			route_type = Constants.CallFlowCategory.PERCENTAGE;
		}
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "route_type%3d"+route_type));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for route_type");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid route_type("+route_type+") is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid("+route_type+") route_type is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid route_type("+route_type+") is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid route_type("+route_type+") is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid route_type is passed for filter
		   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid route_type("+route_type+") is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid route_type is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");
			   if(campaign_route.get("route_type").toString().equals(route_type))
				   call_flow_exist = true;
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response when route_type is filtered with "+route_type);
		}
	}

	@Test(priority=56)
	public void campaign_callflow_with_invalid_filter_operator_for_route_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for route_type
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_route_type", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for route_type");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_route_type");
		String route_type = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "route_type"+encoded_operator+route_type));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for route_type");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for route_type");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : route_type"+operator+route_type);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for route_type");
			}
		}
	}

	@Test(priority=57)
	public void campaign_callflow_with_filter_for_invalid_route_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid route_type
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_route_type", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid route_type");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_route_type");
		String[] route_types = test_data.get(4).split(",");
		for(String route_type:route_types){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "route_type%3d"+route_type));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with filter for invalid route_type");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+route_type+") call_flow_id is passed for filter");
			   test.log(LogStatus.PASS, "API returns success when invalid route_type("+route_type+") is passed for filter");
			   // Check whether campaign/callflow returns record when invalid route_type is passed for filter
			   Assert.assertEquals(err_data, "no records found", "Invalid err message is returned when invalid("+route_type+") call_flow_id is passed for filter.");
			   test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+route_type+") call_flow_id is passed for filter.");
			}
		}
	}

	@Test(priority=58)
	public void campaign_callflow_with_filter_for_nonexisting_route_type() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing route_type
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_route_type", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing route_type");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_route_type");
		String route_type = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "route_type%3d"+route_type));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for route_type");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is returing success when non existing route_type is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning error non existing route_type is passed in filter.");
		   Assert.assertEquals(json.get("err").toString(), "no records found", "Proper validation is not displayed when non existing route_type is passed in filter.");
		   test.log(LogStatus.PASS, "Check proper validation is displayed when non existing route_type is passed in filter.");
		}
	}

//	 @Test(priority=59)
	public void campaign_callflow_with_valid_filter_for_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_flow_name
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_flow_name", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_flow_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_valid_filter_for_call_flow_name");
		String[] callflows = test_data.get(4).split(",");
		String agency_level_callflow = callflows[0], company_level_callflow = callflows[1], location_level_callflow = callflows[2], other_billing_callflow = callflows[3];
		String[] callflow_names = {agency_level_callflow, company_level_callflow, location_level_callflow, other_billing_callflow};
		for(String callflow_name: callflow_names){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_name%3d"+callflow_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_name");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(callflow_name.equals(agency_level_callflow)||callflow_name.equals(company_level_callflow)||callflow_name.equals(location_level_callflow)){
				   Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_name("+callflow_name+") is passed in filter.");
				   test.log(LogStatus.PASS, "API is returning success when valid("+callflow_name+") call_flow_name is passed in filter.");
				   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_flow_name("+callflow_name+") is passed in filter.");
				   test.log(LogStatus.PASS, "err_data is null when valid call_flow_name("+callflow_name+") is passed in filter.");

				   JSONArray data = (JSONArray)json.get("data");
				   // Check whether campaign/callflow returns at least 1 record when valid call_flow_name is passed for filter
				   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid call_flow_name("+callflow_name+") is passed for filter.");
				   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid call_flow_name is passed for filter.");
				   JSONObject campaign_data = (JSONObject)data.get(0);
				   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
				   Boolean call_flow_exist = false;
				   for(int i=0; i<campaign_routes.size(); i++){
					   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
					   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

					   // Check callflow fields are not null
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");

					   if(campaign_route.get("call_flow_name").toString().equals(callflow_name))
						   call_flow_exist = true;
				   }
				   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
				   if(callflow_name.equals(agency_level_callflow))
					   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_name is filtered with agency level callflow");
				   else if(callflow_name.equals(company_level_callflow))
					   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_name is filtered with company level callflow");
				   else if(callflow_name.equals(location_level_callflow))
					   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_name is filtered with location level callflow");
			   }
			   else if(callflow_name.equals(other_billing_callflow)){
				 Assert.assertEquals(result_data, "error", "API is returning success when call_flow_name is filtered with other billing level callflow");
				 test.log(LogStatus.PASS, "API is returning error when call_flow_name is filtered with other billing level callflow");
				 String err_data = json.get("err").toString();
				 Assert.assertEquals(err_data, "no records found", "Invalid validation message is displayed when call_flow_name is filtered with other billing level callflow");
				 test.log(LogStatus.PASS, "Proper validation message is displayed when call_flow_name is filtered with other billing level callflow");
			   }
		   }
		}
	}

	@Test(priority=60)
	public void campaign_callflow_with_invalid_filter_operator_for_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_flow_name
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_flow_name", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_flow_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_flow_name");
		String call_flow_name = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_name"+encoded_operator+call_flow_name));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for route_type");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for route_type");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_name"+operator+call_flow_name);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_name");
			}
		}
	}

	@Test(priority=61)
	public void campaign_callflow_with_filter_for_nonexisting_call_flow_name() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing call_flow_name
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_call_flow_name", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing call_flow_name");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_call_flow_name");
		String call_flow_name = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_name%3d"+call_flow_name));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for route_type");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether campaign/callflow returns error when non existing call_flow_name is passed for filter
		   Assert.assertEquals(result_data, "error", "API returns success when non existing call_flow_name is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing call_flow_name is passed for filter");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when non existing call_flow_name is passed for filter");
		   test.log(LogStatus.PASS, "Proper validation is not displayed when non existing call_flow_name is passed for filter");
		}
	}

	@Test(priority=62)
	public void campaign_callflow_with_valid_filter_for_call_flow_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_flow_created
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_flow_created", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_flow_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String, Object> confCallflow = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id =
			confCallflow.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String call_flow_created_date = DBCallFlowsUtils.getCreatedDateById(call_flow_id);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_created%3d"+call_flow_created_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_created("+call_flow_created_date+") is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid("+call_flow_created_date+") call_flow_created is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_flow_created("+call_flow_created_date+") is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid call_flow_created("+call_flow_created_date+") is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid call_flow_created is passed for filter
		   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid call_flow_created("+call_flow_created_date+") is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid call_flow_created is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   System.out.println(campaign_route);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");

			   if(campaign_route.get("call_flow_created").toString().equals(call_flow_created_date))
				   call_flow_exist = true;
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_created is filtered with valid value");
	   }
	}

	@Test(priority=63)
	public void campaign_callflow_with_invalid_filter_operator_for_call_flow_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_flow_created
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_flow_created", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_flow_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_flow_created");
		String call_flow_created = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_created"+encoded_operator+call_flow_created));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_created");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_created");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_created"+operator+call_flow_created);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_created");
			}
		}
	}

	@Test(priority=64)
	public void campaign_callflow_with_filter_for_nonexisting_call_flow_created() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing call_flow_created
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_call_flow_created", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing call_flow_created");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_call_flow_created");
		String call_flow_created = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_created%3d"+call_flow_created));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_created");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether campaign/callflow returns error when non existing call_flow_created is passed for filter
		   Assert.assertEquals(result_data, "error", "API returns success when non existing call_flow_created is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing call_flow_created is passed for filter");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when non existing call_flow_created is passed for filter");
		   test.log(LogStatus.PASS, "Proper validation is not displayed when non existing call_flow_created is passed for filter");
		}
	}

	@Test(priority=65)
	public void campaign_callflow_with_valid_filter_for_call_flow_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_flow_modified
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_flow_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_flow_modified");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String, Object> confCallflow = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_flow_id =
			confCallflow.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String call_flow_modified_date = DBCallFlowsUtils.getModifiedDateById(call_flow_id);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_modified%3d"+call_flow_modified_date));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_modified("+call_flow_modified_date+") is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid("+call_flow_modified_date+") call_flow_modified is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_flow_modified("+call_flow_modified_date+") is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid call_flow_modified("+call_flow_modified_date+") is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid call_flow_modified is passed for filter
		   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid call_flow_modified("+call_flow_modified_date+") is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid call_flow_modified is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   System.out.println(campaign_route);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");
			   if(campaign_route.get("call_flow_modified") != null){
				   if(campaign_route.get("call_flow_modified").toString().equals(call_flow_modified_date))
					   call_flow_exist = true;
			   }
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_modified is filtered with valid value");
	   }
	}

	@Test(priority=66)
	public void campaign_callflow_with_invalid_filter_operator_for_call_flow_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_flow_modified
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_flow_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_flow_modified");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_flow_modified");
		String call_flow_modified = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_modified"+encoded_operator+call_flow_modified));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_modified");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_modified");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_modified"+operator+call_flow_modified);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_modified");
			}
		}
	}

	@Test(priority=67)
	public void campaign_callflow_with_filter_for_nonexisting_call_flow_modified() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing call_flow_modified
		test = extent.startTest("campaign_callflow_with_filter_for_nonexisting_call_flow_modified", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing call_flow_modified");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_nonexisting_call_flow_modified");
		String call_flow_modified = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_flow_modified%3d"+call_flow_modified));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for non existing call_flow_modified");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether campaign/callflow returns error when non existing call_flow_modified is passed for filter
		   Assert.assertEquals(result_data, "error", "API returns success when non existing call_flow_modified is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing call_flow_modified is passed for filter");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when non existing call_flow_modified is passed for filter");
		   test.log(LogStatus.PASS, "Proper validation is not displayed when non existing call_flow_modified is passed for filter");
		}
	}

	@Test(priority=68)
	public void campaign_callflow_with_valid_filter_for_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_flow_status
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_flow_status", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_flow_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_valid_filter_for_call_flow_status");
		String[] callflow_status = {Constants.ComponentStatus.ACTIVE,Constants.ComponentStatus.INACTIVE};
		for(String call_flow_status: callflow_status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_status%3d"+call_flow_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_flow_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if (result_data.equals("error")) {
			   	 Assert.assertEquals(json.get("err").toString(), "no records found");
			   } else {
				   Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_status("+call_flow_status+") is passed in filter.");
				   test.log(LogStatus.PASS, "API is returning success when valid("+call_flow_status+") call_flow_status is passed in filter.");
				   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_flow_status("+call_flow_status+") is passed in filter.");
				   test.log(LogStatus.PASS, "err_data is null when valid call_flow_status("+call_flow_status+") is passed in filter.");

				   JSONArray data = (JSONArray)json.get("data");
				   // Check whether campaign/callflow returns at least 1 record when valid call_flow_status is passed for filter
				   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid call_flow_status("+call_flow_status+") is passed for filter.");
				   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid call_flow_status is passed for filter.");
				   JSONObject campaign_data = (JSONObject)data.get(0);
				   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
				   Boolean call_flow_exist = false;
				   for(int i=0; i<campaign_routes.size(); i++){
					   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
					   System.out.println(campaign_route);
					   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
					   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

					   // Check callflow fields are not null
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
					   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");

					   if(campaign_route.get("call_flow_status").toString().equals(call_flow_status))
						   call_flow_exist = true;
				   }
				   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
				   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_status is filtered with <b>("+ call_flow_status +")</b> call_flow_status");
			   }
		   }
		}
	}

	@Test(priority=69)
	public void campaign_callflow_with_invalid_filter_operator_for_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_flow_status
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_flow_status", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_flow_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_flow_status");
		String call_flow_status = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_status"+encoded_operator+call_flow_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_status");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for call_flow_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_flow_status"+operator+call_flow_status);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_flow_status");
			}
		}
	}


//	@Test(priority=70)	-- Uncomment when defect will be fixed	
	public void campaign_callflow_with_filter_for_invalid_call_flow_status() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid call_flow_status
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_call_flow_status", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid call_flow_status");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_call_flow_status");
		String[] callflow_status = test_data.get(4).split(",");
		for(String call_flow_status:callflow_status){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_flow_status%3d"+call_flow_status));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for invalid call_flow_status");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether campaign/callflow returns error when invalid call_flow_status is passed for filter
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+call_flow_status+") call_flow_status is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid<b>("+call_flow_status+")</b> call_flow_status is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+call_flow_status+") call_flow_status is passed for filter.\nDefect Reported: CT-17135");
			   test.log(LogStatus.PASS, "Proper validation is not displayed when invalid<b>("+call_flow_status+")</b> call_flow_status is passed for filter");
			}
		}
	}


	@Test(priority=71)
	public void campaign_callflow_with_valid_filter_for_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for repeat_interval
		test = extent.startTest("campaign_callflow_with_valid_filter_for_repeat_interval", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for repeat_interval");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String, Object> confCallflow = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String repeat_interval = confCallflow.get(TestDataYamlConstants.CallflowConstants.REPEAT_INTERVAL).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "repeat_interval%3d"+repeat_interval));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for repeat_interval");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid repeat_interval("+repeat_interval+") is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid("+repeat_interval+") call_flow_status is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid repeat_interval("+repeat_interval+") is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid repeat_interval("+repeat_interval+") is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid repeat_interval is passed for filter
		   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid repeat_interval("+repeat_interval+") is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid repeat_interval is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   System.out.println(campaign_route);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");

			   if(campaign_route.get("repeat_interval").toString().equals(repeat_interval))
				   call_flow_exist = true;
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_status is filtered with <b>("+ repeat_interval +")</b> repeat_interval");
	   }
	}

	@Test(priority=72)
	public void campaign_callflow_with_invalid_filter_operator_for_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for repeat_interval
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_repeat_interval", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for repeat_interval");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_repeat_interval");
		String repeat_interval = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "repeat_interval"+encoded_operator+repeat_interval));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for repeat_interval");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for repeat_interval");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : repeat_interval"+operator+repeat_interval);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for repeat_interval");
			}
		}
	}


//	@Test(priority=73)	-- Uncomment when defect will be fixed	
	public void campaign_callflow_with_filter_for_invalid_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid repeat_interval
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_repeat_interval", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid repeat_interval");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_repeat_interval");
		String[] repeat_intervals = test_data.get(4).split(",");
		for(String repeat_interval:repeat_intervals){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "repeat_interval%3d"+repeat_interval));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for invalid repeat_interval");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether campaign/callflow returns error when invalid repeat_interval is passed for filter
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+repeat_interval+") repeat_interval is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid<b>("+repeat_interval+")</b> repeat_interval is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+repeat_interval+") repeat_interval is passed for filter.\nDefect Reported: CT-17135");
			   test.log(LogStatus.PASS, "Proper validation is not displayed when invalid<b>("+repeat_interval+")</b> repeat_interval is passed for filter");
			}
		}
	}


//	@Test(priority=74) -- uncomment when fixed
	public void campaign_callflow_with_filter_for_non_existing_repeat_interval() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid repeat_interval
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_repeat_interval", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing repeat_interval");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_repeat_interval");
		String repeat_interval = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "repeat_interval%3d"+repeat_interval));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for non existing repeat_interval");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether campaign/callflow returns error when non existing repeat_interval is passed for filter
		   Assert.assertEquals(result_data, "error", "API returns success when non existing("+repeat_interval+") repeat_interval is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing<b>("+repeat_interval+")</b> repeat_interval is passed for filter");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when non existing("+repeat_interval+") repeat_interval is passed for filter");
		   test.log(LogStatus.PASS, "Proper validation is not displayed when non existing<b>("+repeat_interval+")</b> repeat_interval is passed for filter");
		}
	}

	@Test(priority=75)
	public void campaign_callflow_with_valid_filter_for_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with valid filter for call_value
		test = extent.startTest("campaign_callflow_with_valid_filter_for_call_value", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with valid filter for call_value");
		test.assignCategory("CFA GET /campaign/callflow API");
		Map<String, Object> confCallflow = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String call_value = confCallflow.get(TestDataYamlConstants.CallflowConstants.CALL_VALUE).toString();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for call_value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			 System.out.println("============= campaign_callflow_with_valid_filter_for_call_value ==============");
			 System.out.println(line);
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is returning error when valid call_value("+call_value+") is passed in filter.");
		   test.log(LogStatus.PASS, "API is returning success when valid("+call_value+") call_flow_status is passed in filter.");
		   Assert.assertTrue(json.get("err")==null, "err_data is not null when valid call_value("+call_value+") is passed in filter.");
		   test.log(LogStatus.PASS, "err_data is null when valid call_value("+call_value+") is passed in filter.");

		   JSONArray data = (JSONArray)json.get("data");
		   // Check whether campaign/callflow returns at least 1 record when valid call_value is passed for filter
		   Assert.assertTrue(data.size()>=1, "campaign/callflow does not return at least 1 record when valid call_value("+call_value+") is passed for filter.");
		   test.log(LogStatus.PASS, "campaign/callflow does not return at least 1 record when valid call_value is passed for filter.");
		   JSONObject campaign_data = (JSONObject)data.get(0);
		   JSONArray campaign_routes = (JSONArray)campaign_data.get("campaign_routes");
		   Boolean call_flow_exist = false;
		   for(int i=0; i<campaign_routes.size(); i++){
			   JSONObject campaign_route = (JSONObject)campaign_routes.get(i);
			   System.out.println(campaign_route);
			   Assert.assertTrue(campaign_route.containsKey("call_flow_id"), "call_flow_id field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("route_type"), "route_type field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_name"), "call_flow_name field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_created"), "call_flow_created field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_modified"), "call_flow_modified field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_flow_status"), "call_flow_status field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("repeat_interval"), "repeat_interval field is not present in campaign/user api response");
			   Assert.assertTrue(campaign_route.containsKey("call_value"), "call_value field is not present in campaign/user api response");

			   // Check callflow fields are not null
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_id"), "call_flow_id");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("route_type"), "route_type");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_name"), "call_flow_name");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_created"), "call_flow_created");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("call_flow_status"), "call_flow_status");
			   HelperClass.multiple_assertnotEquals(campaign_route.get("repeat_interval"), "repeat_interval");
			   if(campaign_route.get("call_value") != null)
				   if(campaign_route.get("call_value").toString().equals(call_value))
					   call_flow_exist = true;
		   }
		   Assert.assertTrue(call_flow_exist, "Filtered callflow does not exist in response");
		   test.log(LogStatus.PASS, "Filtered callflow exist in response when call_flow_status is filtered with <b>("+ call_value +")</b> call_value");
	   }
	}

	@Test(priority=76)
	public void campaign_callflow_with_invalid_filter_operator_for_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with invalid filter for call_value
		test = extent.startTest("campaign_callflow_with_invalid_filter_operator_for_call_value", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with invalid filter operator for call_value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_invalid_filter_operator_for_call_value");
		String call_value = test_data.get(4);
		String[] operators = {"+","!","~","#","@","$","%","^","&","*","-","/",":"};
		String encoded_operator = "";
		for(String operator:operators){
			encoded_operator = java.net.URLEncoder.encode(operator, "UTF-8");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_value"+encoded_operator+call_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with "+ operator +" filter operator for call_value");
			System.out.println("Execute campaign/callflow api method with "+ operator +" filter operator for call_value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   Assert.assertEquals(result_data, "error", "Invalid result value is in resonse");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "Unsupported comparator used in filter on rule 1 : call_value"+operator+call_value);
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when "+ operator +" filter operator is used for call_value");
			}
		}
	}


//	@Test(priority=77)	-- Uncomment when defect will be fixed
	public void campaign_callflow_with_filter_for_invalid_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for invalid call_value
		test = extent.startTest("campaign_callflow_with_filter_for_invalid_call_value", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for invalid call_value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_invalid_call_value");
		String[] call_values = test_data.get(4).split(",");
		for(String call_value:call_values){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for invalid repeat_interval");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   // Check whether campaign/callflow returns error when invalid call_value is passed for filter
			   Assert.assertEquals(result_data, "error", "API returns success when invalid("+call_value+") repeat_interval is passed for filter");
			   test.log(LogStatus.PASS, "API returns error when invalid<b>("+call_value+")</b> repeat_interval is passed for filter");
			   String err_data = json.get("err").toString();
			   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when invalid("+call_value+") call_value is passed for filter.\nDefect Reported: CT-17135");
			   test.log(LogStatus.PASS, "Proper validation is not displayed when invalid<b>("+call_value+")</b> call_value is passed for filter");
			}
		}
	}

	@Test(priority=78)
	public void campaign_callflow_with_filter_for_non_existing_call_value() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with filter for non existing call_value
		test = extent.startTest("campaign_callflow_with_filter_for_non_existing_call_value", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with filter for non existing call_value");
		test.assignCategory("CFA GET /campaign/callflow API");
		test_data = HelperClass.readTestData(class_name, "campaign_callflow_with_filter_for_non_existing_call_value");
		String call_value = test_data.get(4);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("filter", "call_value%3d"+call_value));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign/callflow api method with valid filter for non existing call_value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   // Check whether campaign/callflow returns error when non existing call_value is passed for filter
		   Assert.assertEquals(result_data, "error", "API returns success when non existing("+call_value+") call_value is passed for filter");
		   test.log(LogStatus.PASS, "API returns error when non existing<b>("+call_value+")</b> call_value is passed for filter");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data, "no records found", "Proper validation is not displayed when non existing("+call_value+") call_value is passed for filter");
		   test.log(LogStatus.PASS, "Proper validation is not displayed when non existing<b>("+call_value+")</b> call_value is passed for filter");
		}
	}

	@Test(priority=79)
	public void campaign_callflow_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with agency admin access_token
		test = extent.startTest("campaign_callflow_with_agency_admin_access_token", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with agency admin access_token");
		test.assignCategory("CFA GET /campaign/callflow API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");

			 Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
			 String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

			 confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
			 String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

			 confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
			 String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();

			 String[] campaign_ids = {agency_level_camp, company_level_camp, location_level_camp};
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false, other_billing_camp = false;
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
		   Assert.assertTrue(agency_camp_present, "Agency level campaign is not present when campaign/callflow api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Agency level campaign is present when campaign/callflow api is executed with agency admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/callflow api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Check Company level campaign is present when campaign/callflow api is executed with agency admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/callflow api is executed with agency admin access_token.");
		   test.log(LogStatus.PASS, "Location level campaign is not present when campaign/callflow api is executed with agency admin access_token.");
		 }
	}


//	@Test(priority=80)
	public void campaign_callflow_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with company admin access_token
		test = extent.startTest("campaign_callflow_with_company_admin_access_token", "To validate whether user is able to get campaign and its callflows through campaign/callflow api with company admin access_token");
		test.assignCategory("CFA GET /campaign/callflow API");

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
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
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
			   // Get the nth campaign from the campaign callflow
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/callflow api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/callflow api is executed with company admin access_token.");
		   Assert.assertTrue(company_camp_present, "Company level campaign is not present when campaign/callflow api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/callflow api is executed with company admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/callflow api is executed with company admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/callflow api is executed with company admin access_token.");
		}
	}


//	@Test(priority=81)
	public void campaign_user_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign/callflow api with location admin access_token
		test = extent.startTest("campaign_callflow_with_location_admin_access_token", "To validate whether user is able to get campaign and its users through campaign/callflow api with location admin access_token");
		test.assignCategory("CFA GET /campaign/callflow API");
		
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
		list.add(new BasicNameValuePair("limit", "500"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign/callflow", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONArray array = (JSONArray)json.get("data");
		   Boolean agency_camp_present = false , company_camp_present = false, location_camp_present = false;
		   for(int i=0; i< array.size(); i++){
			   // Get the nth campaign from the campaign/callflow
			   JSONObject campaign = (JSONObject)array.get(i);
			   if(campaign.get("campaign_id").toString().equals(campaign_ids[0]))
				   agency_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[1]))
				   company_camp_present = true;
			   else if(campaign.get("campaign_id").toString().equals(campaign_ids[2]))
				   location_camp_present = true;
		   }
		   Assert.assertFalse(agency_camp_present, "Agency level campaign is present when campaign/callflow api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check agency level campaign is present when campaign/callflow api is executed with location admin access_token.");
		   Assert.assertFalse(company_camp_present, "Company level campaign is not present when campaign/callflow api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check company level campaign is not present when campaign/callflow api is executed with location admin access_token.");
		   Assert.assertTrue(location_camp_present, "Location level campaign is not present when campaign/callflow api is executed with location admin access_token.");
		   test.log(LogStatus.PASS, "Check location level campaign is not present when campaign/callflow api is executed with location admin access_token.");
		}   
	}	
}
