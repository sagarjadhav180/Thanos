package call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

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
public class PostCallTag extends BaseClass{
	
	String class_name="PostCallTag";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String call_id_loc_level;
	String call_id_comp_level;
	String call_id_agency_level;
	List<String> tag_names=new ArrayList<String>();
	List<Integer> tag_ids=new ArrayList<Integer>();
	ArrayList<String> testdata;
	JSONObject payload=new JSONObject();
	String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};	
	String caller_id,ring_to ,disposition,call_date,file,org_unit_id,group_ext_id,channel_id,campaign_id,tracking_number,line_type,assign_to;
	String custom_source_type_1 ,custom_source_type_2,custom_source_type_3,custom_source_type_4,custom_source_type_5,company_name;
	String city,zip_code,caller_name,address,state,swap_channels,is_outbound,tag_name;
	
	int tag_count;

	
	
	@BeforeClass
	public void setParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;
			
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;
			
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
			String group_id_comp=confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			call_id_comp_level=DBCallUtils.getCallId(group_id_comp);
			String group_id_agency=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			call_id_agency_level=DBCallUtils.getCallId(group_id_agency);			
			
	}

	public void callData(Map confGroupHierarchy,Map confTNHierarchy,Map confCampaignHierarchy,Map confUserHierarchy) throws IOException {
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
	
	public JSONObject createPayload(String field,String value) {
		
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
		
	public void uploadCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
	
		test = extent.startTest("uploadCallWithValidCallDate", "Verify success message displayed if user pass valid Date/Time in request");
		test.assignCategory("CFA POST /Call Upload API");
		
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
			   String success_message = jsonobj.get("result").toString();
		
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
			   
		}
	
    }
	
	
	@Test(priority=1)
	public void PostCallTagForValidTagName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForValidTagName", "Verify success message displayed if User pass valid tag_name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
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
	
	
	@Test(priority=2)
	public void PostCallTagForMultipleTags() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForMultipleTags", "Verify success message displayed if User pass mutiple tags");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
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
	
	
	@Test(priority=3)
	public void PostCallTagForValidCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForValidCallId", "Verify success message displayed if User pass valid call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
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
	
	
	@Test(priority=4)
	public void PostCallTagForValidTagName100characters() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForValidCallId", "Verify error message displayed if User pass more than 100 chars in tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345");
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(jsonobj.get("err"),"Number of characters should not be greater than 100.","Did not return appropriate error message");
				   
			}
		
	}
	
	
	@Test(priority=5)
	public void PostCallTagForNullCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForNullCallId", "Verify error message displayed if User pass blank call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
//			int call_id = null;
			json.put("tag_name", tag_name);
			json.put("call_id", null);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Expected type integer but found type null";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.contains("call_id"),"Path Array did not show call_id parameter");
				   
			}
		
	}
	
	
	@Test(priority=6)
	public void PostCallTagForInvalidCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForInvalidCallId", "Verify error message displayed if User pass invalid call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id = 0;
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String err = jsonobj.get("err").toString();
				   String exp_err="Call_id does not exist.";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message");
			}
		
	}
	
	
	@Test(priority=7)
	public void PostCallTagWithoutCallIdPrameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithoutCallIdPrameter", "Verify error message displayed if User does not pass call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());			
			json.put("tag_name", tag_name);
			
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Missing required property: call_id";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.size()==0,"Path Array contains unnecessary pareamters");
				   
			}
		
	}
	
	
	@Test(priority=8)
	public void PostCallTagForNegativeCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForNegativeCallId", "Verify error message displayed if User pass negative call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id = 0;
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String err = jsonobj.get("err").toString();
				   String exp_err="Call_id does not exist.";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message");
			}
		
	}
	
	
	@Test(priority=9)
	public void PostCallTagForCallIdFromAnotherBilling() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagForCallIdFromAnotherBilling", "Verify error message displayed if User pass call ID from another Billing account");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
			String group_id=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id = Integer.parseInt(DBCallUtils.getCallIdFromAnotherBilling(group_id));
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String err = jsonobj.get("err").toString().replaceAll("[0-9]", "");
				   String exp_err="You don't have permission to add tag for this group ";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message");
			}
		
	}
	
	
	@Test(priority=10)
	public void PostCallTagByAgencyAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagByAgencyAdmin", "Verify success message displayed if Agency Admin User pass valid call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
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
	
	
	@Test(priority=11)
	public void PostCallTagMultipleCallIds() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagMultipleCallIds", "Verify error message displayed if User pass multiple call IDs");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			List<Integer> call_ids=new ArrayList<Integer>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());

			json.put("tag_name", tag_name);
			json.put("call_id", call_ids);

			test.log(LogStatus.INFO, "Verifying if error message is displayed");
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 500), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
            
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Expected type integer but found type array";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.contains("call_id"),"Path Array contains unnecessary pareamters");
				   
			}
			
			
	}
	
	
	@Test(priority=12)
	public void PostCallTagWithTagNameBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithTagNameBlank", "Verify error message displayed if User pass blank tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add(" ");
			int call_id = Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String err = jsonobj.get("err").toString();
				   String exp_err="tag_name cannot be blank";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message");
			}
		
	}
	
	
	@Test(priority=13)
	public void PostCallTagWithTagNameNull() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithTagNameBlank", "Verify error message displayed if User pass null tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add(null);
			int call_id = Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Expected type string but found type null";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.contains("tag_name"),"Path Array contains unnecessary pareamters");
				   
			}
		
	}
	
	
	@Test(priority=14)
	public void PostCallTagWithoutTagNameParameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithoutTagNameParameter", "Verify error message displayed if User doesn not pass tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
//			List<String> tag_name=new ArrayList<String>();
//			tag_name.add(null);
			int call_id = Integer.parseInt(call_id_loc_level);
//			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Missing required property: tag_name";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.size()==0,"Path Array contains unnecessary pareamters");
				   
			}
		
	}
	
	
	@Test(priority=15)
	public void PostCallTagWithInvalidTagName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithInvalidTagName", "Verify error message displayed if User pass invalid tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<Integer> tag_name=new ArrayList<Integer>();
		    testdata = HelperClass.readTestData(class_name, "PostCallTagWithInvalidTagName");
			tag_name.add(Integer.parseInt(testdata.get(2)));
			int call_id = Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				  
				   JSONArray errorsArray = (JSONArray) jsonobj.get("errors");
				   JSONObject errorObject=(JSONObject) errorsArray.get(0);
				   String code=errorObject.get("code").toString();
				   String exp_code="INVALID_REQUEST_PARAMETER";
				   
				   JSONArray nestedErrorsArray=(JSONArray) errorObject.get("errors");
				   JSONObject  nestedErrorObject=(JSONObject) nestedErrorsArray.get(0);
				   String err_message = nestedErrorObject.get("message").toString();
				   String exp_err_message="Expected type string but found type integer";
				   JSONArray pathArray=(JSONArray) nestedErrorObject.get("path");
				   
				   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
				   Assert.assertEquals(code, exp_code,"Result did not return appropriate error code");
				   Assert.assertEquals(err_message, exp_err_message,"Result did not return appropriate  error message");
				   Assert.assertTrue(pathArray.contains("tag_name"),"Path Array contains unnecessary pareamters");
				   
			}
		
	}
	
	
	@Test(priority=16)
	public void PostCallTagWithTagNameHavingSpecialChars() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagWithTagNameHavingSpecialChars", "Verify success message displayed User pass special chars in tag name");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			testdata = HelperClass.readTestData(class_name, "PostCallTagWithTagNameHavingSpecialChars");
			tag_name.add("sj123"+testdata.get(2));
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token, json);
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
	
	
	@Test(priority=17)
	public void PostCallTagByCompanyAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagByCompanyAdmin", "Verify success message displayed if Company Admin User pass valid call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token_company_admin, json);
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
	
	
	@Test(priority=18)
	public void PostCallTagByLocationAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagByLocationAdmin", "Verify success message displayed if Location Admin User pass valid call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_loc_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token_location_admin, json);
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
	
	
	@Test(priority=19)
	public void PostCallTagByLocationAdminForAgencyLevelCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagByLocationAdminForAgencyLevelCall", "Verify error message displayed if Location Admin User pass agency level call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_agency_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token_location_admin, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String error = jsonobj.get("err").toString().replaceAll("[0-9]", "");
				   String exp_error="You don't have permission to add tag for this group ";
				   
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(error, exp_error,"Result did not return appropriate error message");
			
			}
		
	}
	
	
	@Test(priority=20)
	public void PostCallTagByCompanyAdminForAgencyLevelCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallTagByCompanyAdminForAgencyLevelCall", "Verify error message displayed if Company Admin User pass agency level call ID");
		    test.assignCategory("CFA POST /Call/Tag Upload API");
	
			JSONObject json = new JSONObject();
			List<String> tag_name=new ArrayList<String>();
			tag_name.add("sj123"+RandomContentGenerator.getRandomString());
			int call_id=Integer.parseInt(call_id_agency_level);
			json.put("tag_name", tag_name);
			json.put("call_id", call_id);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", access_token_company_admin, json);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   String error = jsonobj.get("err").toString().replaceAll("[0-9]", "");
				   String exp_error="You don't have permission to add tag for this group ";
				   
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   Assert.assertEquals(error, exp_error,"Result did not return appropriate error message");
			
			}
		
	}
	
	
	public void getTagIds() {
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		tag_ids = DBTagUtils.getTagIds("sj123",org_unit_id);
	}
	
	@AfterClass
	public void cleanUpTags() throws UnsupportedOperationException, IOException, ParseException {
		
		if(tag_count>0) {
			getTagIds();
			test = extent.startTest("cleanUpTags", "Deleting all tags");
			test.assignCategory("CFA POST /Call/tag Upload API");
		
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
