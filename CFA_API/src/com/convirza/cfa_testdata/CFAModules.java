package com.convirza.cfa_testdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import com.convirza.constants.Constants;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.utils.TNUtil;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class CFAModules extends BaseClass implements Modules{
		
	ArrayList<String> test_data;
	
	@SuppressWarnings("unchecked")
	@Override
	public void uploadGroups(String group_name, String accessToken, String group_parent_id, String billing_id) throws Exception {
		String class_name = "PostGroup";
		// TODO Auto-generated method stub
		test = extent.startTest("post_group_with_valid_access_token", "To validate whether user is create group through post group api with valid access_token");
		test.assignCategory("CFA POST /group API");
		test_data = HelperClass.readTestData(class_name, "post_group_with_valid_access_token");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_name", group_name);
		json_obj.put("group_parent_id", Integer.parseInt(group_parent_id));  //-------------input
		json_obj.put("group_status", test_data.get(5));
		json_obj.put("billing_id", Integer.parseInt(billing_id));  //-------------input
		json_obj.put("address", test_data.get(7));
		json_obj.put("city", test_data.get(8));
		json_obj.put("state", test_data.get(9));
		json_obj.put("zip", test_data.get(10));
		json_obj.put("phone_number", test_data.get(11));
		json_obj.put("industry_id", Long.parseLong(test_data.get(12)));
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", accessToken, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid access_token is passed.");
			String data_value = api_response.get("data").toString();
			JSONArray json_data_array = (JSONArray)api_response.get("data");
			JSONObject json_data_object = (JSONObject) json_data_array.get(0);
			HelperClass.multiple_assertnotEquals(data_value, "data");
			test.log(LogStatus.PASS, "Check group_id is returned in response.");
			Assert.assertTrue(!json_data_object.get("data").toString().isEmpty(), "group_id is not returned in response.");
			test.log(LogStatus.PASS, "Check proper entry_count is displayed in response.");
			Assert.assertEquals(json_data_object.get("entry_count").toString(), "1", "Invalid entry_count is displayed in response.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void uploadCampaigns(String campaign_name, String accessToken, String group_id, String campaign_owner_user_id) throws Exception {
		// TODO Auto-generated method stub
		String class_name = "CampaignPost";
		test = extent.startTest("create_campaign_with_valid_campaign_name", "To validate whether user is able create campaign through campaign post api with valid campaign_name.");
		test.assignCategory("CFA POST /campaign API");
		test_data = HelperClass.readTestData(class_name, "create_campaign_with_valid_campaign_name");
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String today_formated_date = dateFormatter.format(now).toString();
		
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("campaign_name", campaign_name);
			json.put("campaign_ext_id", "");
			json.put("campaign_status", test_data.get(3));
			json.put("campaign_created", today_formated_date);
			json.put("campaign_modified", today_formated_date);
			json.put("campaign_start_date", today_formated_date);
			json.put("campaign_owner_user_id", Integer.parseInt(campaign_owner_user_id));  //-------------input
			json.put("group_id", Integer.parseInt(group_id)); //-------------input
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
				JSONArray json_response_array = (JSONArray) parser.parse(line);
				JSONObject json_response = (JSONObject) json_response_array.get(0);
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

	@Override
	public void uploadTrackingNumbers(String level, String accessToken) throws Exception {
		// TODO Auto-generated method stub
		test = extent.startTest("PostCallFlowForSimpleRouteByAgencyAdmin", "To validate whether agency level admin user is able to create callflow of route type Simple");
		test.assignCategory("CFA POST /callflow API");
		
		JSONArray callflowReq = TNUtil.createCallflow(level,"simple",1, "unique", "post"); //-------------input
		CloseableHttpResponse response;

		response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";	
		while ((line = rd.readLine()) != null) {
		   JSONParser parser = new JSONParser();
		   JSONObject jsonResponse = (JSONObject) parser.parse(line);
		   String result_data = jsonResponse.get("result").toString();
		   test.log(LogStatus.INFO, "Verifying if success is returned in result");
		   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
		   test.log(LogStatus.INFO, "Verifying if err is null");
		   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
	}

	@Override
	public void uploadCalls(String accessToken, String level) throws Exception {
		// TODO Auto-generated method stub
		CallUploadUtil callUploadUtil =  new CallUploadUtil();
		callUploadUtil.setUpForCallUpload(level);
		callUploadUtil.uploadCallWithValidCallDate(accessToken);
	}


	@SuppressWarnings({ "unchecked" })
	@Override
	public void uploadWebHooks(String accessToken, String org_unit_id, String webhook_Name) throws IOException, URISyntaxException, ParseException {
		// TODO Auto-generated method stub
		String class_name = "PostWebhook";
		// TODO Auto-generated method stub
		test = extent.startTest("uploadWebHooks", "To validate whether user is able to create webhook valid access_token");
		test.assignCategory("CFA POST /webhook API");
		test_data = HelperClass.readTestData(class_name, "uploadWebHooks");
		
		JSONObject json_obj = new JSONObject();
		json_obj.put("org_unit_id", Integer.parseInt(org_unit_id));
		json_obj.put("webhook_status", test_data.get(1));  //-------------input
		json_obj.put("webhook_Name", webhook_Name);
		json_obj.put("description", test_data.get(2));  //-------------input
		json_obj.put("webhook_Endpoint", test_data.get(3));
		json_obj.put("method", test_data.get(4));
		json_obj.put("format", test_data.get(5));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/webhook", accessToken, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid access_token is passed.");
			JSONObject json_data_object = (JSONObject) api_response.get("data");
			test.log(LogStatus.PASS, "Check insertId is returned in response.");
			
			Assert.assertTrue(json_data_object.containsKey("insertId"), "insertId is not returned in response.");
			
		}
		
	}

}
