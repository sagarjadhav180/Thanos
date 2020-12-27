package com.convirza.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallUtils;
import com.convirza.tests.core.utils.DBTagUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class TagsData extends BaseClass{


	static TestDataYamlReader yamlReader = new TestDataYamlReader();
	static String access_token_company_admin;
	static String access_token_location_admin;
	static String call_id_loc_level;
	static String call_id_comp_level;
	static String call_id_agency_level;
	static List<String> tag_names=new ArrayList<String>();
	static List<Integer> tag_ids=new ArrayList<Integer>();
	static ArrayList<String> testdata;
	static JSONObject payload=new JSONObject();
	static String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};	
	static String caller_id;
	static String ring_to;
	static String disposition;
	static String call_date;
	static String file;
	static String org_unit_id;
	static String group_ext_id;
	static String channel_id;
	static String campaign_id;
	static String tracking_number;
	static String line_type;
	static String assign_to;
	static String custom_source_type_1 ,custom_source_type_2,custom_source_type_3,custom_source_type_4,custom_source_type_5,company_name;
	static String city,zip_code,caller_name,address,state,swap_channels,is_outbound,tag_name;
	static int tag_count;
	static Map<String,String> tags_map = new HashMap<String,String>();
	static Map<String,String> access_tokens_map = new HashMap<String,String>();
	static Map<String,String> call_id_map = new HashMap<String,String>();
	static List<Map> list = new ArrayList<Map>();

	public static List setParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;	
			access_tokens_map.put("access_token_company_admin", access_token_company_admin);
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;
			access_tokens_map.put("access_token_location_admin", access_token_location_admin);			
			Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
			Map<String, Object> confAgencyTNHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);			
			Map<String, Object> confAgencyCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.AGENCY);
			Map<String, Object> confAgencyUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY);

			Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
			Map<String, Object> confCompTNHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
			Map<String, Object> confCompCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
			Map<String, Object> confCompUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			
			Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
			Map<String, Object> confLocTNHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
			Map<String, Object> confLocCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
			Map<String, Object> confLocUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			
			callData(confLocGroupHierarchy,confLocTNHierarchy,confLocCampaignHierarchy,confLocUserHierarchy);
			uploadCall();
			callData(confCompGroupHierarchy,confCompTNHierarchy,confCompCampaignHierarchy,confCompUserHierarchy);
			uploadCall();
			callData(confAgencyGroupHierarchy,confAgencyTNHierarchy,confAgencyCampaignHierarchy,confAgencyUserHierarchy);
			uploadCall();
			
			String group_id_loc=confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			call_id_loc_level=DBCallUtils.getCallId(group_id_loc);
			call_id_map.put("call_id_loc_level", call_id_loc_level);
			String group_id_comp=confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			call_id_comp_level=DBCallUtils.getCallId(group_id_comp);
			call_id_map.put("call_id_comp_level", call_id_comp_level);
			String group_id_agency=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			call_id_agency_level=DBCallUtils.getCallId(group_id_agency);			
			call_id_map.put("call_id_agency_level", call_id_agency_level);
			
			createTag(access_token,call_id_agency_level);
			createTag(access_token_company_admin,call_id_comp_level);
			createTag(access_token_location_admin,call_id_loc_level);
			
			String tag_id_agency = String.valueOf(DBTagUtils.getTagId("sj123",group_id_agency));
			tags_map.put("agency_tag", tag_id_agency);
			String tag_id_comp = String.valueOf(DBTagUtils.getTagId("sj123",group_id_comp));
			tags_map.put("comp_tag", tag_id_comp);
			String tag_id_loc = String.valueOf(DBTagUtils.getTagId("sj123",group_id_loc));
			tags_map.put("loc_tag", tag_id_loc);
			
			list.add(call_id_map);
			list.add(access_tokens_map);
		    list.add(tags_map);
			return list;
	}

	public static void callData(Map confGroupHierarchy,Map confTNHierarchy,Map confCampaignHierarchy,Map confUserHierarchy) throws IOException {
		testdata = HelperClass.readTestData("PostCallUpload", "uploadCallWithValidCallDate");
		caller_id = testdata.get(1);
		payload.put("caller_id", caller_id);
		ring_to = testdata.get(2);
		payload.put("ring_to", ring_to);
		disposition = testdata.get(3);
		payload.put("disposition", disposition);
		line_type=testdata.get(11);
		payload.put("line_type", line_type);
		swap_channels=testdata.get(24);
		payload.put("swap_channels", swap_channels);
		is_outbound=testdata.get(25);
		payload.put("is_outbound", is_outbound);
		company_name=testdata.get(18);
		payload.put("company_name", company_name);
		tag_name=testdata.get(26);
		payload.put("tag_name", tag_name);
		caller_name=testdata.get(21);
		payload.put("caller_name", caller_name);
		custom_source_type_1=testdata.get(13);
		payload.put("custom_source_type_1", custom_source_type_1);
		custom_source_type_2=testdata.get(14);
		payload.put("custom_source_type_2", custom_source_type_2);		
		custom_source_type_3=testdata.get(15);
		payload.put("custom_source_type_3", custom_source_type_3);
		custom_source_type_4=testdata.get(16);
		payload.put("custom_source_type_4", custom_source_type_4);
		custom_source_type_5=testdata.get(17);
		payload.put("custom_source_type_5", custom_source_type_5);
		call_date = DateUtils.getDate(FileConstants.callUploadDateFormat(), "-1");
		payload.put("call_date", call_date);
		file=FileConstants.getMP3File("1mb");
		payload.put("file", file);

		org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		payload.put("group_id", org_unit_id);
//		group_ext_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		group_ext_id=RandomContentGenerator.getRandomString();
		payload.put("group_ext_id", group_ext_id);
		
		city=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.CITY).toString();
		payload.put("city", city);
		zip_code=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ZIP).toString();
		payload.put("zip_code", zip_code);
		address=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ADDRESS).toString();
		payload.put("address", address);
		state=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.STATE).toString();
		payload.put("state", state);
//		tracking_number=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.PHONE_NUMBER).toString();
//		payload.put("tracking_number", tracking_number);

		channel_id=confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.CHANNEL_ID).toString();
		payload.put("channel_id", channel_id);
		Map tn_obj= (Map) confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.NUMBER);
		tracking_number=tn_obj.get("phone_number").toString();
		payload.put("tracking_number", tracking_number);
		
		campaign_id=confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		payload.put("campaign_id", campaign_id);

		assign_to=confUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		payload.put("assign_to", assign_to);
	}
	
	public static JSONObject createPayload(String field,String value) {
		
		JSONObject json = new JSONObject();
	
		for(String field1:fields) {
			
			if(field1.equals(field)){
				json.put(field1, value);
			}
			else {
				if(field1.equals("zip_code") || field1.equals("campaign_id")) {
					json.put(field1, Integer.parseInt(payload.get(field1).toString()));
				}
				else if (field1.equals("swap_channels") || field1.equals("is_outbound")){
					json.put(field1,Boolean.getBoolean(payload.get(field1).toString()));
				}
				else {
					json.put(field1, payload.get(field1));					
				}
			}
		}
	
		return json;
		
	}
		
	public static void uploadCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
	
		test = extent.startTest("TagsDataCreation", "Creating tags at 3 levels");
		test.assignCategory("Data Creation");
		
		JSONObject json = new JSONObject();
		json=createPayload("","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
			   
		}
	
    }
	
	public static void createTag(String token, String id) throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
	    test = extent.startTest("PostTag", "Verify success message displayed if Admin User pass valid call ID");
	    test.assignCategory("CFA GET call/tag API");

		JSONObject json = new JSONObject();
		List<String> tag_name=new ArrayList<String>();
		tag_name.add("sj123"+RandomContentGenerator.getRandomString());
		tag_names.add(tag_name.get(0));
		int call_id=Integer.parseInt(id);
		json.put("tag_name", tag_name);
		json.put("call_id", call_id);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
			   
		}
		tag_count++;	
    }
	
	public static void getTagIds() {
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		tag_ids = DBTagUtils.getTagIds("sj123",org_unit_id);
	}
	
	public static void cleanUpTags() throws UnsupportedOperationException, IOException, ParseException {
		if(tag_count>0) {
			getTagIds();
			test = extent.startTest("TagsDataDeletion", "Deleting tags at 3 levels");
			test.assignCategory("Data Creation");
		
			JSONObject json_obj = new JSONObject();
			json_obj.put("tag_id", tag_ids);
			
			HttpResponse response = null; 
			
			try {
				response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
			} catch (Exception c) {
				// TODO Auto-generated catch block
				c.printStackTrace();
			}
			
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String success = json.get("result").toString();
				   test.log(LogStatus.INFO, "Verifying if success message is displayed");
				   Assert.assertTrue(success.equals("success"),"api did not retun success");
				   test.log(LogStatus.INFO, "Verifying if err is null");
				   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			}
		}
	}
	
}
