package campaign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class Campaign extends BaseClass{
	String class_name = "Campaign";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();	
	
	@Test(priority=1)
	public void campaign_with_blank_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign api with blank id value
		test = extent.startTest("campaign_with_blank_id", "To validate whether user is able to get campaign through campaign api with blank id value");
		test.assignCategory("CFA GET /campaign API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign api method with blank id value");
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
			Assert.assertEquals(in_value, "query", "Invalid \'in\' value is returned in response when blank id value is passed");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when blank id value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when blank id value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertEquals(error_path, error_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank id value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank id value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than blank id value is passed");
//			String sub_error_description = sub_error_data.get("description").toString();
//			Assert.assertEquals(sub_error_description, "");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank id value is passed.");
		}
	}
	
	//@Test(priority=2)
	public void campaign_with_invalid_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign api with invalid id value
		test = extent.startTest("campaign_with_invalid_id", "To validate whether user is able to get campaign through campaign api with invalid id value");
		test.assignCategory("CFA GET /campaign API");
		test_data = HelperClass.readTestData(class_name, "campaign_with_invalid_id");
		String[] invalid_id = test_data.get(1).split(",");
		for(String id_value:invalid_id){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id_value));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute campaign api method with invalid id value");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid id("+ id_value + ") value is passed");		
				JSONArray errors_array = (JSONArray)json.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid id("+ id_value + ") value is passed");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "query", "Invalid \'in\' value is returned in response when invalid id("+ id_value + ") value is passed");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when invalid id("+ id_value + ") value is passed");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when invalid id("+ id_value + ") value is passed");
				JSONArray error_path = (JSONArray)error_data.get("path");
				Assert.assertEquals(error_path, error_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid id("+ id_value + ") value is passed");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid id("+ id_value + ") value is passed");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when greater than invalid id("+ id_value + ") value is passed");
//				String sub_error_description = sub_error_data.get("description").toString();
//				Assert.assertEquals(sub_error_description, "");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid id("+ id_value + ") value is passed.");
				test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid id("+ id_value + ") value is passed.");
			}
		}
	}
	
	//@Test(priority=3)
	public void campaign_with_non_existing_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign api with non existing id value
		test = extent.startTest("campaign_with_invalid_id", "To validate whether user is able to get campaign through campaign api with non exiting id value");
		test.assignCategory("CFA GET /campaign API");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Random rand = new Random();
		int nonexisting_id = rand.nextInt((999999 - 444444) + 1) + 444444;
		params.add(new BasicNameValuePair("id", Integer.toString(nonexisting_id)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign api method with invalid id value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result").toString(), "error", "API is returning success when non existing campaign id is passed.");
		   test.log(LogStatus.PASS, "API is returning error when non existing campaign id is passed");
		   Assert.assertEquals(json.get("err"), "User is unauthorized to view the requested campaign", "Invalid error message is displayed when non existing campaign id is passed.");
		   test.log(LogStatus.PASS, "Proper validation message is displayed when non existing campaign id is passed");
		}	
	}		
	
	//@Test(priority=4)
	public void campaign_with_valid_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign api with valid id value
		test = extent.startTest("campaign_with_valid_id", "To validate whether user is able to get campaign through campaign api with valid id value");
		test.assignCategory("CFA GET /campaign API");
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String campaign_id = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", campaign_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign api method with invalid id value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   JSONObject campaign = (JSONObject)json.get("data");
		   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/list api does not contain campaign_modified field.");
		   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/list api does not contain campaign_created field.");
		   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/list api does not contain campaign_owner_user_id field.");
		   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/list api does not contain group_id field.");
		   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/list api does not contain campaign_ext_id field.");
		   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/list api does not contain campaign_name field.");
		   Assert.assertTrue(campaign.containsKey("owner_lname"),"campaign/list api does not contain owner_lname field.");	   
		   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/list api does not contain campaign_end_date field.");
		   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/list api does not contain campaign_id field.");
		   Assert.assertTrue(campaign.containsKey("owner_fname"),"campaign/list api does not contain owner_fname field.");	   
		   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/list api does not contain campaign_start_date field.");
		   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/list api does not contain campaign_status field."); 
		   Assert.assertEquals(campaign.get("campaign_id").toString(), campaign_id, "campaign_id is not matched response when valid id value is passed.");
		   test.log(LogStatus.PASS, "All fields are present in response");
		   test.log(LogStatus.PASS, "campaign_id in response is matched passed id in campaign api method");
		}	
	}
	
	//@Test(priority=5)
	public void campaign_with_other_billing_id() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign api with other billing id value
		test = extent.startTest("campaign_with_other_billing_id", "To validate whether user is able to get campaign through campaign api with other billing id value");
		test.assignCategory("CFA GET /campaign API");
		test_data = HelperClass.readTestData(class_name, "campaign_with_other_billing_id");
		String campaign_id = test_data.get(1);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", campaign_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute campaign api method with other billing id value");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
	    JSONParser parser = new JSONParser();
	    JSONObject json = (JSONObject) parser.parse(line);
	    String result = json.get("result").toString();
			Assert.assertEquals(result, "error", "result value is not \'error\' when other billing campaign id is passed.");
			String err_value = json.get("err").toString();
			Assert.assertEquals(err_value, "User is unauthorized to view the requested campaign", "Invalid err is displayed when other billing campaign id is passed.");
		}
	}
	
	//@Test(priority=6)
	public void campaign_id_with_agency_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign id api with agency admin access_token
		test = extent.startTest("campaign_id_with_agency_admin_access_token", "To validate whether user is able to get campaign through campaign api with agency admin access token");
		test.assignCategory("CFA GET /campaign API");
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		String[] campaign_id = {agency_level_camp, company_level_camp, location_level_camp};
		test.log(LogStatus.INFO, "Execute campaign api method with agency admin access token");
		for(String camp_id:campaign_id){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   JSONObject campaign = (JSONObject)json.get("data");
			   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/list api does not contain campaign_modified field.");
			   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/list api does not contain campaign_created field.");
			   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/list api does not contain campaign_owner_user_id field.");
			   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/list api does not contain group_id field.");
			   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/list api does not contain campaign_ext_id field.");
			   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/list api does not contain campaign_name field.");
			   Assert.assertTrue(campaign.containsKey("owner_lname"),"campaign/list api does not contain owner_lname field.");	   
			   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/list api does not contain campaign_end_date field.");
			   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/list api does not contain campaign_id field.");
			   Assert.assertTrue(campaign.containsKey("owner_fname"),"campaign/list api does not contain owner_fname field.");	   
			   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/list api does not contain campaign_start_date field.");
			   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/list api does not contain campaign_status field."); 
			   if(camp_id.equals(agency_level_camp)){
				   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get agency level campaign using access_token of agency admin.");
				   test.log(LogStatus.PASS, "User is able to get agency level campaign using access_token of agency admin.");
			   }		
			   else if(camp_id.equals(company_level_camp)){
				   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get company level campaign using access_token of agency admin.");
				   test.log(LogStatus.PASS, "User is able to get company level campaign using access_token of agency admin.");
			   }
			   else if(camp_id.equals(location_level_camp)){
				   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get location level campaign using access_token of agency admin.");
				   test.log(LogStatus.PASS, "User is able to get location level campaign using access_token of agency admin.");
			   }
			   test.log(LogStatus.PASS, "All fields are present in response");
			}
		}
	}
	
	//@Test(priority=7)
	public void campaign_id_with_company_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign id api with company admin access_token
		test = extent.startTest("campaign_id_with_company_admin_access_token", "To validate whether user is able to get campaign through campaign api with company admin access token");
		test.assignCategory("CFA GET /campaign API");
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		if (access_token == null) {
			Assert.assertTrue(false, "Access token is not returned by company admin user.");
			test.log(LogStatus.FAIL, "Access token is not returned by company admin user.");
		}

		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		String[] campaign_id = {agency_level_camp, company_level_camp, location_level_camp};
		test.log(LogStatus.INFO, "Execute campaign api method with company admin access token");
		for(String camp_id:campaign_id){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			JSONParser parser = new JSONParser();
			if(camp_id.equals(company_level_camp) || camp_id.equals(location_level_camp)){
				while ((line = rd.readLine()) != null) {
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONObject campaign = (JSONObject)json.get("data");
				   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/list api does not contain campaign_modified field.");
				   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/list api does not contain campaign_created field.");
				   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/list api does not contain campaign_owner_user_id field.");
				   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/list api does not contain group_id field.");
				   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/list api does not contain campaign_ext_id field.");
				   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/list api does not contain campaign_name field.");
				   Assert.assertTrue(campaign.containsKey("owner_lname"),"campaign/list api does not contain owner_lname field.");	   
				   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/list api does not contain campaign_end_date field.");
				   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/list api does not contain campaign_id field.");
				   Assert.assertTrue(campaign.containsKey("owner_fname"),"campaign/list api does not contain owner_fname field.");	   
				   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/list api does not contain campaign_start_date field.");
				   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/list api does not contain campaign_status field."); 	
				   if(camp_id.equals(company_level_camp)){
					   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get company level campaign using access_token of company admin.");
					   test.log(LogStatus.PASS, "User is able to get company level campaign using access_token of company admin.");
				   }
				   else if(camp_id.equals(location_level_camp)){
					   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get location level campaign using access_token of company admin.");
					   test.log(LogStatus.PASS, "User is able to get location level campaign using access_token of company admin.");
				   }
				   test.log(LogStatus.PASS, "All fields are present in response");
				}
			}
			else if(camp_id.equals(agency_level_camp)){
				while ((line = rd.readLine()) != null) {
					JSONObject json = (JSONObject) parser.parse(line);
				    String result = json.get("result").toString();
					Assert.assertEquals(result, "error", "result value is in response not \'error\' when agency level campaign id is passed with access token of company admin.");
					test.log(LogStatus.PASS,"result value is not \'error\' when agency level campaign id is passed with access token of company admin.");
					String err_value = json.get("err").toString();
					Assert.assertEquals(err_value, "User is unauthorized to view the requested campaign", "Invalid err is displayed when agency level campaign id is passed with access token of company admin.");
					test.log(LogStatus.PASS,"Proper validation is displayed when agency level campaign id is passed with access token of company admin.");
				}
			}
		}	
	}
	
	//@Test(priority=8)
	public void campaign_id_with_location_admin_access_token() throws URISyntaxException, ClientProtocolException, IOException, ParseException{
		// Execute campaign id api with location admin access_token
		test = extent.startTest("campaign_id_with_location_admin_access_token", "To validate whether user is able to get campaign through campaign api with location admin access token");
		test.assignCategory("CFA GET /campaign API");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		if (access_token == null) {
			Assert.assertTrue(false, "Access token is not returned by location admin user.");
			test.log(LogStatus.FAIL, "Access token is not returned by location admin user.");
		}
		
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
		String agency_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		String company_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		String location_level_camp = confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		
		String[] campaign_id = {agency_level_camp, company_level_camp, location_level_camp};
		test.log(LogStatus.INFO, "Execute campaign api method with location admin access token");
		for(String camp_id:campaign_id){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", camp_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/campaign", access_token, params);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			JSONParser parser = new JSONParser();
			if(camp_id.equals(location_level_camp)){
				while ((line = rd.readLine()) != null) {
				   JSONObject json = (JSONObject) parser.parse(line);
				   JSONObject campaign = (JSONObject)json.get("data");
				   Assert.assertTrue(campaign.containsKey("campaign_modified"),"campaign/list api does not contain campaign_modified field.");
				   Assert.assertTrue(campaign.containsKey("campaign_created"),"campaign/list api does not contain campaign_created field.");
				   Assert.assertTrue(campaign.containsKey("campaign_owner_user_id"),"campaign/list api does not contain campaign_owner_user_id field.");
				   Assert.assertTrue(campaign.containsKey("group_id"),"campaign/list api does not contain group_id field.");
				   Assert.assertTrue(campaign.containsKey("campaign_ext_id"),"campaign/list api does not contain campaign_ext_id field.");
				   Assert.assertTrue(campaign.containsKey("campaign_name"),"campaign/list api does not contain campaign_name field.");
				   Assert.assertTrue(campaign.containsKey("owner_lname"),"campaign/list api does not contain owner_lname field.");	   
				   Assert.assertTrue(campaign.containsKey("campaign_end_date"),"campaign/list api does not contain campaign_end_date field.");
				   Assert.assertTrue(campaign.containsKey("campaign_id"),"campaign/list api does not contain campaign_id field.");
				   Assert.assertTrue(campaign.containsKey("owner_fname"),"campaign/list api does not contain owner_fname field.");	   
				   Assert.assertTrue(campaign.containsKey("campaign_start_date"),"campaign/list api does not contain campaign_start_date field.");
				   Assert.assertTrue(campaign.containsKey("campaign_status"),"campaign/list api does not contain campaign_status field."); 	
				   Assert.assertEquals(campaign.get("campaign_id").toString(), camp_id, "User is not able to get location level campaign using access_token of location admin.");
				   test.log(LogStatus.PASS, "User is able to get location level campaign using access_token of location admin.");
				   test.log(LogStatus.PASS, "All fields are present in response");
				}
			}
			else if(camp_id.equals(agency_level_camp)){
				while ((line = rd.readLine()) != null) {
					JSONObject json = (JSONObject) parser.parse(line);
				    String result = json.get("result").toString();
					Assert.assertEquals(result, "error", "result value is in response not \'error\' when agency level campaign id is passed with access token of location admin.");
					test.log(LogStatus.PASS,"result value is not \'error\' when agency level campaign id is passed with access token of location admin.");
					String err_value = json.get("err").toString();
					Assert.assertEquals(err_value, "User is unauthorized to view the requested campaign", "Invalid err is displayed when agency level campaign id is passed with access token of location admin.");
					test.log(LogStatus.PASS,"Proper validation is displayed when agency level campaign id is passed with access token of location admin.");
				}
			}
			else if(camp_id.equals(company_level_camp)){
				while ((line = rd.readLine()) != null) {
					JSONObject json = (JSONObject) parser.parse(line);
				    String result = json.get("result").toString();
					Assert.assertEquals(result, "error", "result value in response is not \'error\' when company level campaign id is passed with access token of location admin.");
					test.log(LogStatus.PASS,"result value is not \'error\' when company level campaign id is passed with access token of location admin.");
					String err_value = json.get("err").toString();
					Assert.assertEquals(err_value, "User is unauthorized to view the requested campaign", "Invalid err is displayed when company level campaign id is passed with access token of location admin.");
					test.log(LogStatus.PASS,"Proper validation is displayed when company level campaign id is passed with access token of location admin.");
				}	
			}
		}	
	}		
}
