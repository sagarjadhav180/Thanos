package call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.testng.asserts.SoftAssert;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallUtils;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.convirza.tests.core.utils.DBTagUtils;
import com.convirza.utils.TagsData;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class GetCallTag extends BaseClass{

	String class_name="GetCallTag";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String call_id_loc_level;
	String call_id_comp_level;
	String call_id_agency_level;
	String tag_loc_level;
	String tag_comp_level;
	String tag_agency_level;
	ArrayList<String> testdata;
	Map<String,String> filter_map = new HashMap<String,String>();

	@BeforeClass
	public void setParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {

		@SuppressWarnings("rawtypes")
		List data = TagsData.setParams();
		Map<String,String> call_id_map = (Map<String, String>) data.get(0);
		Map<String,String> access_tokens_map = (Map<String, String>) data.get(1);
		Map<String,String> tags_map = (Map<String, String>) data.get(2);
		access_token_company_admin = access_tokens_map.get("access_token_company_admin");
		access_token_location_admin = access_tokens_map.get("access_token_location_admin");
		call_id_agency_level = call_id_map.get("call_id_agency_level");
		call_id_comp_level = call_id_map.get("call_id_comp_level");
		call_id_loc_level = call_id_map.get("call_id_loc_level");
		tag_agency_level = tags_map.get("agency_tag");
		tag_comp_level = tags_map.get("comp_tag");
		tag_loc_level = tags_map.get("loc_tag");

	}
	
	@Test(priority=1)
    public void getCallTagfieldsVerification() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagfieldsVerification", "To Verify that data fields returned are as per swagger doc");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagfieldsVerification");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   
			   JSONArray data_arr = (JSONArray) json.get("data");
			   JSONObject call_data = (JSONObject) data_arr.get(0);
			   JSONArray tag_arr = (JSONArray) call_data.get("tags");
			   JSONObject tag_obj = (JSONObject) tag_arr.get(0);
			   String [] call_fields= {"call_id","call_flow_id","group_id","disposition","duration","caller_id","tracking_number","ring_to","repeat_call","call_started","tags"};
			   String [] tag_fields= {"tag_id","ct_user_id","call_tag_created","tag_name","tag_created","tag_active"};
			   
			   for(int i=0;i<call_fields.length;i++) {
				   test.log(LogStatus.INFO, "Verifying if "+call_fields[i]+" parameter is present");				   
				   Assert.assertTrue(call_data.containsKey(call_fields[i]),call_fields[i]+" is not present");
				   //storing values in filter_map to use it for filter tcs
				   filter_map.put(call_fields[i], call_data.get(call_fields[i]).toString());
			   }
		
			   for(int i=0;i<tag_fields.length;i++) {
				   test.log(LogStatus.INFO, "Verifying if "+tag_fields[i]+" parameter is present");				   
				   Assert.assertTrue(tag_obj.containsKey(tag_fields[i]),tag_fields[i]+" is not present");
				   //storing values in filter_map to use it for filter tcs
				   filter_map.put(tag_fields[i], tag_obj.get(tag_fields[i]).toString());
			   }
		}
		
	}
	
	
	@Test(priority=2)
    public void getCallTagWithoutFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagWithoutFilter", "To Verify that data is returned irrespective of any filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
	
	
	@Test(priority=3,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallIdFilter", "To Verify if filtered data is returned when call_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_id%3d"+filter_map.get("call_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for call_id");
			   filterCheck(json,filter_map.get("call_id"),"call_id");
		}
		
	}
	
	
	@Test(priority=4,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallFlowIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallFlowIdFilter", "To Verify if filtered data is returned when call_flow_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_flow_id%3d"+filter_map.get("call_flow_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for call_flow_id");
			   filterCheck(json,filter_map.get("call_flow_id"),"call_flow_id");
		}
		
	}
	
	
	@Test(priority=5,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForGroupIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForGroupIdFilter", "To Verify if filtered data is returned when group_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+filter_map.get("group_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for call_flow_id");
			   filterCheck(json,filter_map.get("group_id"),"group_id");
		}
		
	}
	
	
	@Test(priority=6,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForDispositionFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForDispositionFilter", "To Verify if filtered data is returned when disposition filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","disposition%3d"+filter_map.get("disposition")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for disposition");
			   filterCheck(json,filter_map.get("disposition"),"disposition");
		}
		
	}
	
	
	@Test(priority=7,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForDurationFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForDurationFilter", "To Verify if filtered data is returned when duration filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","duration%3d"+filter_map.get("duration")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for duration");
			   filterCheck(json,filter_map.get("duration"),"duration");
		}
		
	}
	
	
	@Test(priority=8,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallerIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallerIdFilter", "To Verify if filtered data is returned when caller_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","caller_id%3d"+filter_map.get("caller_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for caller_id");
			   filterCheck(json,filter_map.get("caller_id"),"caller_id");
		}
		
	}
	
	@Test(priority=9,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForTNFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForTNFilter", "To Verify if filtered data is returned when tracking_number filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tracking_number%3d"+filter_map.get("tracking_number")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tracking_number");
			   filterCheck(json,filter_map.get("tracking_number"),"tracking_number");
		}
		
	}
	
	
	@Test(priority=10,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForRingToNumberFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForRingToNumberFilter", "To Verify if filtered data is returned when ring_to filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ring_to%3d"+filter_map.get("ring_to")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for ring_to");
			   filterCheck(json,filter_map.get("ring_to"),"ring_to");
		}
		
	}
	
	
	@Test(priority=11,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForRepeatCallFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForRepeatCallFilter", "To Verify if filtered data is returned when repeat_call filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","repeat_call%3d"+filter_map.get("repeat_call")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for repeat_call");
			   filterCheck(json,filter_map.get("repeat_call"),"repeat_call");
		}
		
	}
	
	
	@Test(priority=12,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallStartedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallStartedFilter", "To Verify if filtered data is returned when call_started filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_started%3d"+filter_map.get("call_started")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for call_started");
			   filterCheck(json,filter_map.get("call_started"),"call_started");
		}
		
	}
	
	
	@Test(priority=13,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForTagIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForTagIdFilter", "To Verify if filtered data is returned when tag_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+filter_map.get("tag_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tag_id");
			   filterCheck(json,filter_map.get("tag_id"),"tag_id");
		}
		
	}
	
	
	@Test(priority=14,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCTUserIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCTUserIdFilter", "To Verify if filtered data is returned when ct_user_id filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ct_user_id%3d"+filter_map.get("ct_user_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for ct_user_id");
			   filterCheck(json,filter_map.get("ct_user_id"),"ct_user_id");
		}
		
	}
	
	
	@Test(priority=15,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallTagCreatedFilter", "To Verify if filtered data is returned when call_tag_created filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_tag_created%3d"+filter_map.get("call_tag_created")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for call_tag_created");
			   filterCheck(json,filter_map.get("call_tag_created"),"call_tag_created");
		}
		
	}
	
	
	@Test(priority=16,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForCallTagNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForCallTagNameFilter", "To Verify if filtered data is returned when tag_name filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+filter_map.get("tag_name")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tag_name");
			   filterCheck(json,filter_map.get("tag_name"),"tag_name");
		}
		
	}
	
	//not working
//	@Test(priority=17,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForTagCreatedFilter", "To Verify if filtered data is returned when tag_created filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_created%3d"+filter_map.get("tag_created")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tag_created");
			   filterCheck(json,filter_map.get("tag_created"),"tag_created");
		}
		
	}
	
	
	@Test(priority=18,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForTagActiveFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForTagActiveFilter", "To Verify if filtered data is returned when tag_active filter is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_active%3d"+filter_map.get("tag_active")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tag_active");
			   filterCheck(json,filter_map.get("tag_active"),"tag_active");
		}
		
	}
	
	
	@Test(priority=19)
    public void getCallTagWithValidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagWithValidLimit", "To Verify data is returned as per applied limit when valid limit is passed");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagWithValidLimit");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if data is returned as per applied limit");
			   JSONArray data_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(data_arr.size(), 1,"API returned "+data_arr.size()+" when limit applied for "+limit+" object");
		}
		
	}
	
	
	@Test(priority=20)
    public void getCallTagWithValidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagWithValidOffset", "To Verify data is returned as per applied offset when valid offset is passed");
		test.assignCategory("CFA GET call/tag API");
	
		testdata = HelperClass.readTestData(class_name, "getCallTagWithValidOffset");
		String offset = testdata.get(2);
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("offset",String.valueOf(offset)));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/call/tag", access_token, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
	
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
		}
	}
	
	
	@Test(priority=21)
    public void getCallTagWithValidLimitAndOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagWithValidLimitAndOffset", "To Verify data is returned as per applied limit and offset when valid limit and offset is passed");
		test.assignCategory("CFA GET call/tag API");
	
		String first_object = null;
		String second_object = null;
		//with limit 
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagWithValidLimitAndOffset");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   JSONArray data_arr = (JSONArray) json.get("data");
			   JSONObject first_obj = (JSONObject) data_arr.get(0);
			   JSONObject second_obj = (JSONObject) data_arr.get(1);
			   first_object = first_obj.get("call_id").toString();
			   second_object = second_obj.get("call_id").toString();
		}
		
		//with limit and offset
		testdata = HelperClass.readTestData(class_name, "getCallTagWithValidLimitAndOffset");
		String offset = testdata.get(2);
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("offset",offset));
		nvps1.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/call/tag", access_token, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
	
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if offset is working");
			   JSONArray data_arr = (JSONArray) json.get("data");
			   JSONObject first_obj1 = (JSONObject) data_arr.get(0);
			   String first_obj = first_obj1.get("call_id").toString();
			   Assert.assertEquals(first_obj, second_object,"offset is not working");
			   
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject obj = (JSONObject) data_arr.get(i);
				   int exp_call_id = Integer.parseInt(obj.get("call_id").toString());
				   int act_call_id = Integer.parseInt(first_object);
				   Assert.assertNotEquals(exp_call_id, act_call_id);
			   }
		}
	}
	
	
	@Test(priority=22)
    public void getCallTagDBVerification() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagDBVerification", "To Verify data is returned is matching with db");
		test.assignCategory("CFA GET call/tag API");
		
		List<Integer> tagids_act=new ArrayList<Integer>();
		List<Integer> tagids_exp=new ArrayList<Integer>();
		String call_id = null;
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagWithValidLimit");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   JSONArray data_arr = (JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject data_obj = (JSONObject) data_arr.get(i);
				   JSONObject data_obj1 = (JSONObject) data_arr.get(0);
				   call_id = data_obj1.get("call_id").toString();
				   JSONArray tags_arr = (JSONArray) data_obj.get("tags");
				   for(int j=0;j<tags_arr.size();j++) {
					   JSONObject tags_obj = (JSONObject) tags_arr.get(j);
					   String tag_id = tags_obj.get("tag_id").toString();
					   tagids_act.add(Integer.parseInt(tag_id));
				   }
			   }
		}
		tagids_exp = DBTagUtils.getTagIds(call_id);
		Collections.sort(tagids_exp);
		Collections.sort(tagids_act);
		test.log(LogStatus.INFO, "Verifying if tags attached to call are matching with db");
		Assert.assertEquals(tagids_act, tagids_exp, "tags attached to call are not matching with db");
	}
	
	
//	@Test(priority=23)
    public void getCallTagDAMCheckForAgencyLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagDAMCheckForAgencyLevel", "To Verify agency level level admin has data access for its group and subgroups");
		test.assignCategory("CFA GET call/tag API");
		
		List<Integer> callids_act=new ArrayList<Integer>();
		List<Integer> callids_exp=new ArrayList<Integer>();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagDAMCheckForAgencyLevel");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
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
			   JSONArray data_arr = (JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject data_obj = (JSONObject) data_arr.get(i);
				   String call_id = data_obj.get("call_id").toString();
				   callids_act.add(Integer.parseInt(call_id));
			   }
		}
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		callids_exp = DBTagUtils.getCallIds(group_id, "agency");
		Collections.sort(callids_exp);
		Collections.sort(callids_act);
		test.log(LogStatus.INFO, "Verifying if data reuturned for agency admin is as per DAM");
		Assert.assertEquals(callids_act, callids_exp, "data reuturned for agency admin is is not as per DAM");
	}
	
	
	@Test(priority=24)
    public void getCallTagDAMCheckForCompanyLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagDAMCheckForCompanyLevel", "To Verify company level level admin has data access for its group and subgroups");
		test.assignCategory("CFA GET call/tag API");
		
		List<Integer> callids_act=new ArrayList<Integer>();
		List<Integer> callids_exp=new ArrayList<Integer>();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagDAMCheckForCompanyLevel");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token_company_admin, nvps);
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
			   JSONArray data_arr = (JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject data_obj = (JSONObject) data_arr.get(i);
				   String call_id = data_obj.get("call_id").toString();
				   callids_act.add(Integer.parseInt(call_id));
			   }
		}
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		callids_exp = DBTagUtils.getCallIds(group_id, "company");
		Collections.sort(callids_exp);
		Collections.sort(callids_act);
		test.log(LogStatus.INFO, "Verifying if data reuturned for company admin is as per DAM");
		Assert.assertEquals(callids_act, callids_exp, "data reuturned for company admin is is not as per DAM");
	}
	
	
	@Test(priority=25)
    public void getCallTagDAMCheckForLocationLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagDAMCheckForLocationLevel", "To Verify location level level admin has data access for its group and subgroups");
		test.assignCategory("CFA GET call/tag API");
		
		List<Integer> callids_act=new ArrayList<Integer>();
		List<Integer> callids_exp=new ArrayList<Integer>();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getCallTagDAMCheckForLocationLevel");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token_location_admin, nvps);
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
			   JSONArray data_arr = (JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject data_obj = (JSONObject) data_arr.get(i);
				   String call_id = data_obj.get("call_id").toString();
				   callids_act.add(Integer.parseInt(call_id));
			   }
		}
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		callids_exp = DBTagUtils.getCallIds(group_id, "location");
		Collections.sort(callids_exp);
		Collections.sort(callids_act);
		test.log(LogStatus.INFO, "Verifying if data reuturned for location admin is as per DAM");
		Assert.assertEquals(callids_act, callids_exp, "data reuturned for location admin is is not as per DAM");
	}
	
	
	@Test(priority=26)
    public void getCallTagBYCompanyAdminForAgencyLevelData() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagBYCompanyAdminForAgencyLevelData", "To Verify no records are returned when company admin try to access data from agency level");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=27)
    public void getCallTagBYLocationAdminForAgencyLevelData() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagBYLocationAdminForAgencyLevelData", "To Verify no records are returned when location admin try to access data from agency level");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token_location_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=28)
    public void getCallTagBYLocationAdminForCompanyLevelData() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagBYLocationAdminForCompanyLevelData", "To Verify no records are returned when location admin try to access data from company level");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String group_id = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token_location_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=29)
    public void getCallTagForAnotherBilling() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForAnotherBilling", "To Verify no records are returned when location admin try to access data from company level");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String group_id = DBGroupUtils.getOtherBillingGroupId(groupId);
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=30,dependsOnMethods= {"getCallTagfieldsVerification"})
    public void getCallTagForDeletedTag() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForDeletedTag", "To Verify if tag ascociation with call is not broken even if tag is deleted");
		test.assignCategory("CFA GET call/tag API");
		
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		int tag_id = Integer.parseInt(filter_map.get("tag_id"));
		tag_ids.add(tag_id);

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
		
		//getting call/tag details
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("filter","tag_id%3d"+filter_map.get("tag_id")));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/call/tag", access_token, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
	
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for tag_id");
			   filterCheck(json,filter_map.get("tag_id"),"tag_id");
		}
		
	}
	
	
	@Test(priority=31)
    public void getCallTagForBlankGroupIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankGroupIdFilter", "To Verify if appropriate error message is displayed when blank group_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=32)
    public void getCallTagForNullGroupIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullGroupIdFilter", "To Verify if appropriate error message is displayed when null group_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
	
	@Test(priority=33)
    public void getCallTagForInvalidGroupIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidGroupIdFilter", "To Verify if appropriate error message is displayed when invalid group_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidGroupIdFilter");
		String invalid_group_id = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","group_id%3d"+invalid_group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool.";
		       Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	   
		}
	}
	
	
    @Test(priority=34)
    public void getCallTagForBlankDispositionFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankDispositionFilter", "To Verify if appropriate error message is displayed when blank disposition is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","disposition%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
    
    @Test(priority=35)
    public void getCallTagForNullDispositionFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullDispositionFilter", "To Verify if appropriate error message is displayed when null disposition is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","disposition%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
	
    
    @Test(priority=36)
    public void getCallTagForInvalidDispositionFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidDispositionFilter", "To Verify if appropriate error message is displayed when invalid disposition is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidDispositionFilter");
		String disposition = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","disposition%3d"+disposition));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
		       Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=37)
    public void getCallTagForBlankDurationFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankDurationFilter", "To Verify if appropriate error message is displayed when blank duration is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","duration%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=38)
    public void getCallTagForNullDurationFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullDurationFilter", "To Verify if appropriate error message is displayed when null duration is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","duration%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=39)
    public void getCallTagForInvalidDurationFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidDurationFilter", "To Verify if appropriate error message is displayed when invalid duration is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidDurationFilter");
		String duration = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","duration%3d"+duration));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
		       Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=40)
    public void getCallTagForBlankCallerIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankCallerIdFilter", "To Verify if appropriate error message is displayed when blank caller_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","caller_id%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=41)
    public void getCallTagForNullCallerIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullCallerIdFilter", "To Verify if appropriate error message is displayed when null caller_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","caller_id%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=42)
    public void getCallTagForInvalidCallerIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidCallerIdFilter", "To Verify if appropriate error message is displayed when invalid caller_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidCallerIdFilter");
		String caller_id = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","caller_id%3d"+caller_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=43)
    public void getCallTagForBlankTNFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankTNFilter", "To Verify if appropriate error message is displayed when blank tracking_number is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tracking_number%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=44)
    public void getCallTagForNullTNFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullTNFilter", "To Verify if appropriate error message is displayed when null tracking_number is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tracking_number%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=45)
    public void getCallTagForInvalidTNFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidTNFilter", "To Verify if appropriate error message is displayed when invalid tracking_number is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidTNFilter");
		String tracking_number = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","tracking_number%3d"+tracking_number));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=46)
    public void getCallTagForBlankRingToFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankRingToFilter", "To Verify if appropriate error message is displayed when blank ring_to is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ring_to%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=47)
    public void getCallTagForNullRingToFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullRingToFilter", "To Verify if appropriate error message is displayed when null ring_to is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ring_to%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=48)
    public void getCallTagForInvalidRingToFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidRingToFilter", "To Verify if appropriate error message is displayed when invalid ring_to is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidRingToFilter");
		String ring_to = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","ring_to%3d"+ring_to));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=49)
    public void getCallTagForBlankRepeatCallFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankRepeatCallFilter", "To Verify if appropriate error message is displayed when blank repeat_call is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","repeat_call%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=50)
    public void getCallTagForNullRepeatCallFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullRepeatCallFilter", "To Verify if appropriate error message is displayed when null repeat_call is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","repeat_call%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=51)
    public void getCallTagForInvalidRepeatCallFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidRepeatCallFilter", "To Verify if appropriate error message is displayed when invalid repeat_call is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidRepeatCallFilter");
		String ring_to = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","repeat_call%3d"+ring_to));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");
		}
	}
    
    
    @Test(priority=52)
    public void getCallTagForBlankCallStartedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankCallStartedFilter", "To Verify if appropriate error message is displayed when blank call_started is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_started%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=53)
    public void getCallTagForNullCallStartedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullCallStartedFilter", "To Verify if appropriate error message is displayed when null call_started is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_started%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=54)
    public void getCallTagForInvalidCallStartedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidCallStartedFilter", "To Verify if appropriate error message is displayed when invalid call_started is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidCallStartedFilter");
		String call_started = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","call_started%3d"+call_started));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	
		}
	}
    
    
    @Test(priority=55)
    public void getCallTagForBlankTagIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankTagIdFilter", "To Verify if appropriate error message is displayed when blank tag_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=56)
    public void getCallTagForNullTagIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullTagIdFilter", "To Verify if appropriate error message is displayed when null tag_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=57)
    public void getCallTagForInvalidTagIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidTagIdFilter", "To Verify if appropriate error message is displayed when invalid tag_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidTagIdFilter");
		String tag_id = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+tag_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");
		}
	}
    
    
    @Test(priority=58)
    public void getCallTagForBlankCTUserIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankCTUserIdFilter", "To Verify if appropriate error message is displayed when blank ct_user_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ct_user_id%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=59)
    public void getCallTagForNullCTUserIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullCTUserIdFilter", "To Verify if appropriate error message is displayed when null ct_user_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ct_user_id%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=60)
    public void getCallTagForInvalidCTUserIdFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidCTUserIdFilter", "To Verify if appropriate error message is displayed when invalid ct_user_id is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidCTUserIdFilter");
		String ct_user_id = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","ct_user_id%3d"+ct_user_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");
		}
	}
    
    
    @Test(priority=61)
    public void getCallTagForBlankCallTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankCallTagCreatedFilter", "To Verify if appropriate error message is displayed when blank call_tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_tag_created%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=62)
    public void getCallTagForNullCallTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullCTUserIdFilter", "To Verify if appropriate error message is displayed when null call_tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_tag_created%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=63)
    public void getCallTagForInvalidCallTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidCallTagCreatedFilter", "To Verify if appropriate error message is displayed when invalid call_tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidCallTagCreatedFilter");
		String call_tag_created = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","call_tag_created%3d"+call_tag_created));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	  
		}
	}
    
    
    @Test(priority=64)
    public void getCallTagForBlankTagNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankTagNameFilter", "To Verify if appropriate error message is displayed when blank tag_name is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=65)
    public void getCallTagForNullTagNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullTagNameFilter", "To Verify if appropriate error message is displayed when null tag_name is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=66)
    public void getCallTagForBlankTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankTagCreatedFilter", "To Verify if appropriate error message is displayed when blank tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_created%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=67)
    public void getCallTagForNullTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullTagCreatedFilter", "To Verify if appropriate error message is displayed when null tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_created%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=68)
    public void getCallTagForInvalidTagCreatedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidTagCreatedFilter", "To Verify if appropriate error message is displayed when invalid tag_created is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidTagCreatedFilter");
		String tag_created = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","tag_created%3d"+tag_created));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
			   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=69)
    public void getCallTagForBlankTagActiveFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForBlankTagActiveFilter", "To Verify if appropriate error message is displayed when blank tag_active is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_active%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "Please provide valid data.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=70)
    public void getCallTagForNullTagActivedFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForNullTagActivedFilter", "To Verify if appropriate error message is displayed when null tag_active is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_active%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   String err_act = json.get("err").toString();
			   String err_exp = "No records found.";
		       Assert.assertEquals(err_act, err_exp, "appropriate error message is not displayed.");	   
		}
	}
    
    
    @Test(priority=71)
    public void getCallTagForInvalidTagActiveFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getCallTagForInvalidTagActiveFilter", "To Verify if appropriate error message is displayed when invalid tag_active is passed in filter");
		test.assignCategory("CFA GET call/tag API");
		SoftAssert softassert = new SoftAssert();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getCallTagForInvalidTagActiveFilter");
		String tag_active_values = testdata.get(3);
		String [] tag_active_arr = tag_active_values.split(",");
		
		for(String tag_active:tag_active_arr) {
			nvps.add(new BasicNameValuePair("filter","tag_active%3d"+tag_active));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/call/tag", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result = json.get("result").toString();
				   test.log(LogStatus.INFO, "Verifying if error is displayed");
				   Assert.assertTrue(result.equals("error"),"api did not retun error");
				   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
				   String err_act = json.get("err").toString();
				   String err_exp = "Failed to query list of calls with tag data. Query error in ctPool";
				   Assert.assertTrue(err_act.startsWith(err_exp), "appropriate error message is not displayed.");	 
			}	
		}
		softassert.assertAll();
	}
    
    
	public void filterCheck(JSONObject resposne,String filter_value,String filter_element) {
		
		JSONArray data_arr = (JSONArray) resposne.get("data");
		
		if(filter_element.equals("tag_id") || filter_element.equals("ct_user_id") || filter_element.equals("call_tag_created") || filter_element.equals("tag_name") || filter_element.equals("tag_created") || filter_element.equals("tag_active")) {
			for(int i=0;i<data_arr.size();i++) {
				JSONObject json = (JSONObject) data_arr.get(i);
				JSONArray tag_arr = (JSONArray) json.get("tags");
				
				for(int j=0;j<tag_arr.size();j++) {
					JSONObject tags_obj = (JSONObject) tag_arr.get(j);
					String filtered_value = tags_obj.get(filter_element).toString();
					String exp_filtered_value = filter_value;
					if(filtered_value.equals(exp_filtered_value)) 
						Assert.assertEquals(filtered_value, exp_filtered_value, "The value _>"+filtered_value+" is not as per filter applied");	
					else 
						continue;
					
				}			
			}	
		}else {
			for(int i=0;i<data_arr.size();i++) {
				JSONObject json = (JSONObject) data_arr.get(i);
				String filtered_value = json.get(filter_element).toString();
				String exp_filtered_value = filter_value;
				Assert.assertEquals(filtered_value, exp_filtered_value, "The value _>"+filtered_value+" is not as per filter applied");
			}	
		}
		
	}
	
	@AfterClass
	public void cleanUpTags() throws UnsupportedOperationException, IOException, ParseException {
		
		TagsData.cleanUpTags();
		
	}
	
}
