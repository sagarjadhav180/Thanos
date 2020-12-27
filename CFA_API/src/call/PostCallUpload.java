package call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONTokener;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jfree.data.Values;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBChannelUtils;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class PostCallUpload extends BaseClass{
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String class_name="PostCallUpload";
	ArrayList<String> testdata;
	String caller_id,ring_to ,disposition,call_date,file,org_unit_id,group_ext_id,channel_id,campaign_id,tracking_number,line_type,assign_to;
	String custom_source_type_1 ,custom_source_type_2,custom_source_type_3,custom_source_type_4,custom_source_type_5,company_name;
	String city,zip_code,caller_name,address,state,swap_channels,is_outbound,tag_name;

	JSONObject payload=new JSONObject();
	String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};	
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public void setUpForCallUpload() throws IOException, ParseException {
		testdata = HelperClass.readTestData(class_name, "uploadCallWithValidCallDate");
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
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
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
		
		
		Map<String, Object> confTNHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		channel_id=confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.CHANNEL_ID).toString();
		Map tn_obj= (Map) confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.NUMBER);
		tracking_number=tn_obj.get("phone_number").toString();
		payload.put("tracking_number", tracking_number);
		payload.put("channel_id", channel_id);
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.LOCATION);
		campaign_id=confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		payload.put("campaign_id", campaign_id);
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
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
	
	@SuppressWarnings("unchecked")
	@Test(priority=1)
	public void uploadCallWithBlankCallDate() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankCallDate", "Check API shows error message,when user pass blank Date/time in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload("call_date","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="call_date";
			   String exp_message="Invalid parameter (call_date): Object didn't pass validation for format date-time: ";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=2)
	public void uploadCallWithInvalidCallDate() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidCallDate", "Verify error message displayed if user pass invalid Date/Time in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		ArrayList<String> testdata1 = HelperClass.readTestData(class_name, "uploadCallWithInvalidCallDate");
		String invalid_call_date = testdata1.get(5);
		
		JSONObject json12 = new JSONObject();
		json12=createPayload("call_date",invalid_call_date);
		
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json12);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="call_date";
			   String exp_message="Invalid parameter (call_date): Object didn't pass validation for format date-time: "+invalid_call_date+"";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=3)
	public void uploadCallWithValidCallDate() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
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
	
	
	@Test(priority=4)
	public void uploadCallWithoutCallDateField() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCallDateField", " Verify error message displayed if user doesnt pass Date/Time parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json1 = new JSONObject();
		json1=createPayload("","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json1,"call_date");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="call_date";
			   String exp_message="Invalid parameter (call_date): Value is required but was not provided";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=6)
	public void uploadCallWithCallDateNull() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCallDateNull", "Verify error message displayed if user pass Null Date/Time in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json1=createPayload("","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_with_null_field("/v2/call/upload", access_token, json1,"call_date");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="call_date";
			   String exp_message="Invalid parameter (call_date): Object didn't pass validation for format date-time: null";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=7)
	public void uploadCallWithGrouopIDBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithGrouopIDBlank", "Verify error message displayed if user pass blank Group_id in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload("group_id"," ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="group_id";
			   String exp_message="Invalid parameter (group_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=7)
	public void uploadCallWithInvalidGrouopID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidGrouopID", "Verify error message displayed if user pass invalid Group_id in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload("group_id",org_unit_id.concat("abc"));
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="group_id";
			   String exp_message="Invalid parameter (group_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=8)
	public void uploadCallWithValidGroupID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidGroupID", "Verify success message displayed if user pass valid Group_id(present in system) in request");
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
	
	
	@Test(priority=9)
	public void uploadCallWithGroupIDFromAnotherBilling() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithGroupIDFromAnotherBilling", "Verify error message displayed if user pass\n" + 
				"Group_id from another billing in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		String other_billing_org_unit_id=DBGroupUtils.getOtherBillingGroupId(org_unit_id);
		json=createPayload("group_id", other_billing_org_unit_id);
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   String err = jsonobj.get("err").toString();
			   
			   Assert.assertEquals(result, exp_result,"Result did not return error");
			   Assert.assertEquals(err, "You are unauthorized for this group.");
			   
		}
		
	}
	
	
	@Test(priority=10)
	public void uploadCallWithoutGroupIDField() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutGroupIDField", " Verify error message displayed if user doesnt pass Group_ID parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"group_id");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="group_id";
			   String exp_message="Invalid parameter (group_id): Value is required but was not provided";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=11)
	public void uploadCallWithGroupIDNull() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithGroupIDNull", "Verify error message displayed if user pass Null Group ID in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_with_null_field("/v2/call/upload", access_token, json,"group_id");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="group_id";
			   String exp_message="Invalid parameter (group_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=12)
	public void uploadCallWithRecordingFileBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithRecordingFileBlank", "Verify error message \n" + 
				"displayed if user pass blank for audio\n" + 
				" in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"file");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   String err = jsonobj.get("err").toString();
			   
			   Assert.assertEquals(result, exp_result,"Result did not return error");
			   Assert.assertEquals(err, "Failed to upload File . Please enter call recording file.");
			   
		}
		
	}
	
	@Test(priority=13)
	public void uploadCallWithWAVFile() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithWAVFile", "Verify success message displayed if user pass WAV file in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("file", FileConstants.getWAVFile("1mb"));
		
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
	
	
	@Test(priority=14)
	public void uploadCallWithWVFile() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithWVFile", "Verify success message displayed if user pass WV file in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		String wv_file=FileConstants.getWVFile();
		
		json=createPayload("file", FileConstants.getWVFile());
		
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
	
	@Test(priority=15)
	public void uploadCallWith5MBMP3File() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWith5MBMP3File", "Verify success message displayed if user pass 5mb mp3 file in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("file", FileConstants.getMP3File("5mb"));
		
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
	
	
	@Test(priority=16)
	public void uploadCallWith5MBWAVFile() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWith5MBWAVFile", "Verify success message displayed if user pass 5mb WAV file in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("file", FileConstants.getWAVFile("5mb"));
		
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
	
	
	@Test(priority=17)
	public void uploadCallWithUnspportedFileFormatXLS() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithFileFormatXLS", "Verify error message \n" + 
				"displayed if user pass unspported \n" + 
				"file(.png,.csv,.jar) for audio\n" + 
				"in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("file", FileConstants.getXLSFile());
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Invalid audio format.");
			   
		}
		
	}
	
//	pending -- technical limitation
//	@Test(priority=18)
    public void uploadCallWithExcessFileSize() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWith5MBMP3File", "Verify error code displayed if user pass more than 50mb mp3 file in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("file", FileConstants.getMP3File("55mb"));
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 413 , "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		
	}
	
	
	@Test(priority=19)
	public void uploadCallWithoutAddressParameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutAddressParameter", "Verify success message displayed if user doesnt pass Address paramter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"address");
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
	
	
	@Test(priority=20)
	public void uploadCallWithInvalidOuboundValue() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidOuboundValue", "Verify error message is displayed if user pass invalid is_outbound in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload("is_outbound", "123");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="is_outbound";
			   String exp_message="Invalid parameter (is_outbound): Expected type boolean but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=21)
	public void uploadCallWithValidRingToNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidRingToNumber", "Verify success message displayed if user pass valid Ring to Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
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
	
	
	@Test(priority=22)
	public void uploadCallWithValidAddress() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidAddress", "Verify success message displayed if user pass valid Address in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
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
	
	
	@Test(priority=23)
	public void uploadCallWithValidLineType() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidLineType", "Verify success message displayed if user pass valid Line Type in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
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
	
	
	@Test(priority=24)
	public void uploadCallWithoutCampaignIdParameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCampaignIdParameter", "Verify success message displayed if user doesnt pass Campaign ID parameter in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"campaign_id");
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
	
	
	@Test(priority=25)
	public void uploadCallWithInvalidDisposition() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidDisposition", "Verify error message displayed if user pass invalid Disposition");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("disposition", "ANS");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Invalid disposition.");
			   
		}
		
	}
	
	
	@Test(priority=26)
	public void uploadCallWithAssignToBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithAssignToBlank", "Verify error message displayed if user pass blank for assign_to in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("assign_to", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   String err = jsonobj.get("err").toString();
			   
			   Assert.assertEquals(result, exp_result,"Result did not return error");
			   Assert.assertEquals(err, "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=27)
	public void uploadCallWithInvalidRingToNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidRingToNumber", "Verify error message displayed if user pass invalid Ring to Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("ring_to", "123456789");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Failed to upload File . Please enter valid ring to.");
			   
		}
		
	}
	
	
	@Test(priority=28)
	public void uploadCallWithInvalidChannelID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidChannelID", "Verify error message is displayed if user pass invalid channel id in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("channel_id", "123");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Invalid channel id");
			   
		}
		
	}
	
	
	@Test(priority=29)
	public void uploadCallWithoutZipParameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutZipParameter", "Verify success message displayed if user doesnt pass Zip parameter in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"zip_code");
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
	
	
	@Test(priority=30)
	public void uploadCallWithoutTrackingNumberParameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutTrackingNumberParameter", "Verify success message displayed if user doesnt pass Tracking Number parameter in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"tracking_number");
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
	
	
	@Test(priority=31)
	public void uploadCallWithExistingCustomSource() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithExistingCustomSource", "Verify success message displayed if user pass existing value for custom source in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("custom_source_type_1",custom_source_type_1);
		
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
	
	
	@Test(priority=32)
	public void uploadCallWithCampaignIDBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCampaignIDBlank", "Verify error message is displayed if user pass blank Campaign_id in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("campaign_id", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="campaign_id";
			   String exp_message="Invalid parameter (campaign_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=33)
	public void uploadCallWithIsOutboundAsTrue() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithIsOutboundAsTrue", "Verify success message is displayed if user pass true for is_outbound in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" "," ");
		
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
	
	
	@Test(priority=34)
	public void uploadCallWithValidDispositions() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidDispositions", "Verify success message displayed if user pass valid Disposition(ANSWERED, NO ANSWER, BUSY)in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		String[] dispositions= {"ANSWERED","NO ANSWER","BUSY"};
		
		for(String disposition:dispositions) {
			JSONObject json = new JSONObject();
			
			json=createPayload("disposition",disposition);
			
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
		
		
	}
	
	
	@Test(priority=35)
	public void uploadCallWithBlankTag() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankTag", "Verify error message displayed if user pass blank Tags in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("tag_name", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=36)
	public void uploadCallWithCallerIDNull() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCallerIDNull", "Verify error message displayed if user pass Null Caller_ID in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json1=createPayload("","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_with_null_field("/v2/call/upload", access_token, json1,"caller_id");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="caller_id";
			   String exp_message="Invalid parameter (caller_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=37)
	public void uploadCallWithInvalidcampaignID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidcampaignID", "Verify error message is displayed if user pass invalid Campaign_id(Which is not present in system) in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		json=createPayload("campaign_id", "campaign123!@#");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="campaign_id";
			   String exp_message="Invalid parameter (campaign_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=38)
	public void uploadCallWithBlankCity() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankCity", "Verify error message displayed if user pass blank for City in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("tag_name", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=39)
	public void uploadCallWithBlankCompanyName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankCompanyName", "Verify error message displayed if user pass blank for Company name in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("company_name", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=40)
	public void uploadCallWithBlankState() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankState", "Verify error message displayed if user pass blank for State / Province in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("state", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=41)
	public void uploadCallWithoutAgent() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutAgent", "Verify success message displayed if user doesnt pass Agent parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"assign_to");
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
	
	
	@Test(priority=42)
	public void uploadCallWithBlankIsOutbound() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankIsOutbound", "Verify error message is displayed if user pass blank is_outbound in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("is_outbound", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="is_outbound";
			   String exp_message="Invalid parameter (is_outbound): Expected type boolean but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=43)
	public void uploadCallWithInvalidCallerID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidCallerID", "Verify error message displayed if user pass invalid Caller_Id in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("caller_id", "!@#qwe123");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="caller_id";
			   String exp_message="Invalid parameter (caller_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=44)
	public void uploadCallWithValidState() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidState", "Verify success message displayed if user pass valid State / Province in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=45)
	public void uploadCallWithDefaultTageName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithDefaultTageName", "Verify success message is displayed if user specified tag name same as default tag name(API Calls)");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload("tag_name","API Calls");
			
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
	
	
	@Test(priority=46)
	public void uploadCallWithoutChannelID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutChannelID", "Verify success message is displayed if user doesnt pass channel_id parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"channel_id");
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
	
	
	@Test(priority=47)
	public void uploadCallWithIncompleteTimeStamp() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithIncompleteTimeStamp", "Verify error message displayed if user pass incomplete Date/Time in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("call_date", "2020-08-16T04:36:38.000");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Failed to upload File. Please enter valid date and time.");
			   
		}
		
	}
	
	
	@Test(priority=48)
	public void uploadCallWithoutDisposition() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutDisposition", "Verify success message displayed if user doesnt pass Disposition parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"disposition");
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
	
	
	@Test(priority=49)
	public void uploadCallWithoutCallerID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCallerID", "Verify success message displayed if user doesnt pass Caller ID parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"caller_id");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="caller_id";
			   String exp_message="Invalid parameter (caller_id): Value is required but was not provided";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=50)
	public void uploadCallWithOutBouboundFalse() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithOutBouboundFalse", "Verify success message is displayed if user pass false for is_outbound in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload("is_outbound","false");
			
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

	
	@Test(priority=51)
	public void uploadCallWithoutOutBoubound() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutOutBoubound", "Verify success message is displayed if user doesnt pass is_outbound parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"is_outbound");
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

	
	@Test(priority=52)
	public void uploadCallWithValidCallerID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidCallerID", "Verify success message displayed if user pass valid Caller_Id in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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

	@Test(priority=53)
	public void uploadCallWithCallerIDBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCallerIDBlank", "Verify error message displayed if user pass blank caller _ID in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json1=createPayload("caller_id","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json1);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="caller_id";
			   String exp_message="Invalid parameter (caller_id): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
		}
		
	}
	
	
	@Test(priority=54)
	public void uploadCallWithBlankCallerName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankCallerName", "Verify error message displayed if user pass blank for Caller name in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("caller_name", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=55)
	public void uploadCallWithBlankChannelID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithBlankChannelID", "Verify error message displayed if user pass blank for channel id in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("caller_name", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=56)
	public void uploadCallWithInvalidCompanyName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidCompanyName", "Verify success message displayed if user pass invalid Company name in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload("company_name","C123-!@#$%^&*()_+?><:{}");
			
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
	
	
	@Test(priority=57)
	public void uploadCallWithNewCustomSourceName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithNewCustomSourceName", "Verify success message displayed if user pass new value for custom source in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload("custom_source_type_1",RandomContentGenerator.getRandomString());
			
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
	
	
	@Test(priority=58)
	public void uploadCallWithAllValidChannelID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithAllValidChannelID", "Verify success message displayed if user pass valid Channel ID in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		Map channel_ids = DBChannelUtils.getAllValidChannelID();
		
		for(Object value:channel_ids.values()) {
			
			System.out.println(value);

            JSONObject json = new JSONObject();
			
			json=createPayload("channel_id",value.toString());
			
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
		
	}
	
	
	@Test(priority=59)
	public void uploadCallWithRingToNumberBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithRingToNumberBlank", "Verify error message is displayed if user pass blank Ring TO Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("ring_to", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="ring_to";
			   String exp_message="Invalid parameter (ring_to): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=60)
	public void uploadCallWithTagName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithTagName", "Verify success message displayed if user pass tag value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		
			JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=61)
	public void uploadCallWithoutCustomSource() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCustomSource", "Verify success message displayed if user doesnt pass Custom Sources parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		for(int i=1;i<6;i++) {
            JSONObject json = new JSONObject();
			
            String cs="custom_source_type_"+i;
            
			json=createPayload(cs," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,cs);
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
		
					
	}
	
	
	@Test(priority=62)
	public void uploadCallWithoutGroupID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithotGroupID", "Verify error message displayed if user doesnt pass Group_id parameter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"group_id");
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="group_id";
			   String exp_message="Invalid parameter (group_id): Value is required but was not provided";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=63)
	public void uploadCallWithValidZip() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidZip", "Verify success message displayed if user pass valid Zip/ Postal Code in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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

	
	@Test(priority=64)
	public void uploadCallWithTrackingNumberBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithTrackingNumberBlank", "Verify error message is displayed if user pass blank tracking Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("tracking_number", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="tracking_number";
			   String exp_message="Invalid parameter (tracking_number): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=65)
	public void uploadCallWithLineTypeBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithLineTypeBlank", "Verify error message displayed if user pass blank for Line Type in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("line_type", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Value required but was not provided");
			   
		}
		
	}
	
	
	@Test(priority=66)
	public void uploadCallWithZipBlank() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithZipBlank", "Verify error message displayed if user pass blank for Zip in request.");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("zip_code", " ");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			 JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="zip_code";
			   String exp_message="Invalid parameter (zip_code): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	@Test(priority=67)
	public void uploadCallWithoutState() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutState", "Verify success message displayed if user doesnt pass State / Province paramter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"state");
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
	
	
	@Test(priority=68)
	public void uploadCallWithValidCampaignID() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidCampaignID", "Verify success message is displayed if user pass valid Campaign_id(Which is present in system) in request ");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=69)
	public void uploadCallWithValidCity() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidCity", "Verify success message displayed if user pass valid City in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=70)
	public void uploadCallWithoutCallerName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCallerName", "Verify success message displayed if user doesnt pass Caller Name paramter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,caller_name);
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
	
	
	@Test(priority=71)
	public void uploadCallWithInvalidTrackingNumnber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidTrackingNumnber", "Verify error message is displayed if user pass invalid Tracking Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("tracking_number", "123");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Please enter valid tracking number.");
			   
		}
		
	}
	
	
	@Test(priority=72)
	public void uploadCallWithValidAgent() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidAgent", "Verify success message displayed if user pass valid agent in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=73)
	public void uploadCallWithoutCompanyName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCompanyName", "Verify success message displayed if user doesnt pass Company name paramter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"company_name");
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
	
	
	@Test(priority=74)
	public void uploadCallWithoutCity() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithoutCompanyName", "Verify success message displayed if user doesnt pass City paramter and its value in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
			CloseableHttpResponse response = HelperClass.make_post_request_without_field("/v2/call/upload", access_token, json,"city");
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
	
	
	@Test(priority=75)
	public void uploadCallWithValidCallerName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidCallerName", "Verify success message displayed if user pass valid Caller Name in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=76)
	public void uploadCallWithValidCompanyName() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithValidCompanyName", "Verify success message displayed if user pass valid Company name in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload(" "," ");
			
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
	
	
	@Test(priority=77)
	public void uploadCallWithInvalidZipCode() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithInvalidTrackingNumnber", "Verify error message is displayed if user pass invalid Tracking Number in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload("zip_code", "123456");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="error";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return appropriate error message");
			   Assert.assertEquals(jsonobj.get("err").toString(), "Please enter valid zip code.");
			   
		}
		
	}
	
	
	@Test(priority=78)
	public void uploadCallWithAddressHavingAlphaNumericSpecialChars() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithAddressHavingAlphaNumericSpecialChars", "Verify success message displayed if user pass Address having alphnumeroc and special characters in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload("address","abc123~!@#");
			
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
	
	
	@Test(priority=79)
	public void uploadCallWithCityHavingAlphaNumericSpecialChars() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCityHavingAlphaNumericSpecialChars", "Verify success message displayed if user pass City having alphnumeroc and special characters in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload("city","abc123~!@#");
			
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
	
	
	@Test(priority=80)
	public void uploadCallWithCallerNameHavingAlphaNumericSpecialChars() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithCallerNameHavingAlphaNumericSpecialChars", "Verify success message displayed if user pass Caller Name having alphnumeroc and special characters in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload("caller_name","abc123~!@#");
			
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
	
	
	@Test(priority=81)
	public void uploadCallWithLineTypeHavingAlphaNumericSpecialChars() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithLineTypeHavingAlphaNumericSpecialChars", "Verify success message displayed if user pass Line Type having alphnumeroc and special characters in request");
		test.assignCategory("CFA POST /Call Upload API");
		
	        JSONObject json = new JSONObject();
			
			json=createPayload("caller_name","abc123~!@#");
			
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
	
//	pending -- technical limitation
//	@Test(priority=82)
	public void uploadCallWithDuplicateMandatoryParameters() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("uploadCallWithDuplicateMandatoryParameters", "Verify error message is displayed if user pass multiple values for any field except tags in request");
		test.assignCategory("CFA POST /Call Upload API");
		
		JSONObject json = new JSONObject();
		
		json=createPayload(" ", " ");
		json.put("caller_idd", caller_id);
	
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String validation_message = jsonobj.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation message is displayed");
			   Assert.assertTrue(validation_message.equals("Validation errors"));
			   
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray errors = (JSONArray)jsonobj.get("errors");
			   
			   JSONObject jsonobj1=(JSONObject) errors.get(0);
			   String name = jsonobj1.get("name").toString();
			   String message = jsonobj1.get("message").toString();
			   String exp_name="caller_id";
			   String exp_message="Invalid parameter (caller_id): Expected type integer but found type object";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   
		}
		
	}
	
	
	
	
	
}


