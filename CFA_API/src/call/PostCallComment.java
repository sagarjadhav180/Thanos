package call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.utils.TagsData;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class PostCallComment extends BaseClass{
	
	String class_name="PostCallComment";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String call_id_agency_level;
	String call_id_comp_level;
	String call_id_loc_level;
	ArrayList<String> testdata;
	
	
	@BeforeClass
	public void setParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {

		@SuppressWarnings("rawtypes")
		List data = TagsData.setParams();
		Map<String,String> call_id_map = (Map<String, String>) data.get(0);
		Map<String,String> access_tokens_map = (Map<String, String>) data.get(1);
		access_token_company_admin = access_tokens_map.get("access_token_company_admin");
		access_token_location_admin = access_tokens_map.get("access_token_location_admin");
		call_id_agency_level = call_id_map.get("call_id_agency_level");
		call_id_comp_level = call_id_map.get("call_id_comp_level");
		call_id_loc_level = call_id_map.get("call_id_loc_level");
		
	}

	
	@Test(priority=1)
	public void PostCallCommentForNullCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForNullCallId", "Verify appropriate error message displayed if User pass null call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", null);
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if validation errorss message is displayed");	   
				   String message = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				   Assert.assertEquals(message, exp_result,"Result did not return validation error message");
				   JSONArray errors = (JSONArray) jsonobj.get("errors");
				   JSONObject errors_obj = (JSONObject) errors.get(0);
				   String name = errors_obj.get("name").toString();
				   String exp_name = "comments";
				   test.log(LogStatus.INFO, "Verifying if error field name is displayed");
				   Assert.assertEquals(name, exp_name,"error field name is not displayed.Field name dispalyed is "+errors_obj.get("name").toString());
				   JSONArray nested_errors_arr = (JSONArray) errors_obj.get("errors");
				   JSONObject nested_errors_obj = (JSONObject) nested_errors_arr.get(0);
				   String err_message = nested_errors_obj.get("message").toString();
				   String exp_err_message = "Expected type integer but found type null";
				   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
				   Assert.assertEquals(err_message, exp_err_message,"appropriate error message is not displayed.error dispalyed is "+nested_errors_obj.get("message").toString());
					  
			}
	}
	
	
	@Test(priority=2)
	public void PostCallCommentForNonExistingCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForNonExistingCallId", "Verify appropriate error message displayed if User pass non existing call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			testdata = HelperClass.readTestData(class_name, "PostCallCommentForNonExistingCallId");
			int call_id = Integer.parseInt(testdata.get(1));
			json.put("call_id",call_id);
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if validation errorss message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   String err = jsonobj.get("err").toString();
				   String exp_err = "Call_id does not exist.";
				   test.log(LogStatus.INFO, "Verifying if appropriate errors message is displayed");
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate error message");
			}
	}
	
	
	@Test(priority=3)
	public void PostCallCommentForInvalidCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForInvalidCallId", "Verify appropriate error message displayed if User pass non invalid call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			testdata = HelperClass.readTestData(class_name, "PostCallCommentForInvalidCallId");
			int call_id = Integer.parseInt(testdata.get(1));
			json.put("call_id",call_id);
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if validation errorss message is displayed");	   
				   String message = jsonobj.get("message").toString();
				   String exp_message="Validation errors";
				   Assert.assertEquals(message, exp_message,"Result did not return error message");
				   JSONArray errors = (JSONArray) jsonobj.get("errors");
				   JSONObject errors_obj = (JSONObject) errors.get(0);
				   JSONArray nested_errors_arr = (JSONArray) errors_obj.get("errors");
				   JSONObject nested_errors_obj = (JSONObject) nested_errors_arr.get(0);
				   String err_message = nested_errors_obj.get("message").toString();
				   String exp_err_message = "Value -123 is less than minimum 0";
				   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
				   Assert.assertEquals(err_message, exp_err_message,"appropriate error message is not displayed.error dispalyed is "+nested_errors_obj.get("message").toString());
				
			}
			
	}
	
	
	@Test(priority=4)
	public void PostCallCommentForNullComment() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForNullComment", "Verify appropriate error message displayed if User pass null comment");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add(null);
			json.put("call_id", Integer.parseInt(call_id_agency_level));			
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if validation errorss message is displayed");	   
				   String message = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				   Assert.assertEquals(message, exp_result,"Result did not return validation error message");
				   JSONArray errors = (JSONArray) jsonobj.get("errors");
				   JSONObject errors_obj = (JSONObject) errors.get(0);
				   String name = errors_obj.get("name").toString();
				   String exp_name = "comments";
				   test.log(LogStatus.INFO, "Verifying if error field name is displayed");
				   Assert.assertEquals(name, exp_name,"error field name is not displayed.Field name dispalyed is "+errors_obj.get("name").toString());
				   JSONArray nested_errors_arr = (JSONArray) errors_obj.get("errors");
				   JSONObject nested_errors_obj = (JSONObject) nested_errors_arr.get(0);
				   String err_message = nested_errors_obj.get("message").toString();
				   String exp_err_message = "Expected type string but found type null";
				   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
				   Assert.assertEquals(err_message, exp_err_message,"appropriate error message is not displayed.error dispalyed is "+nested_errors_obj.get("message").toString());
					  
			}
	}
	
	
	@Test(priority=5)
	public void PostCallCommentForInvalidComment() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForInvalidComment", "Verify appropriate error message displayed if User pass invalid comment");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<Integer> comments=new ArrayList<Integer>();
			testdata = HelperClass.readTestData(class_name, "PostCallCommentForInvalidComment");
			int comment = Integer.parseInt(testdata.get(1));
			comments.add(comment);
			json.put("call_id", Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if validation errorss message is displayed");	   
				   String message = jsonobj.get("message").toString();
				   String exp_result="Validation errors";
				   Assert.assertEquals(message, exp_result,"Result did not return validation error message");
				   JSONArray errors = (JSONArray) jsonobj.get("errors");
				   JSONObject errors_obj = (JSONObject) errors.get(0);
				   String name = errors_obj.get("name").toString();
				   String exp_name = "comments";
				   test.log(LogStatus.INFO, "Verifying if error field name is displayed");
				   Assert.assertEquals(name, exp_name,"error field name is not displayed.Field name dispalyed is "+errors_obj.get("name").toString());
				   JSONArray nested_errors_arr = (JSONArray) errors_obj.get("errors");
				   JSONObject nested_errors_obj = (JSONObject) nested_errors_arr.get(0);
				   String err_message = nested_errors_obj.get("message").toString();
				   String exp_err_message = "Expected type string but found type integer";
				   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
				   Assert.assertEquals(err_message, exp_err_message,"appropriate error message is not displayed.error dispalyed is "+nested_errors_obj.get("message").toString());
					  
			}
	}
	
	
	@Test(priority=6)
	public void PostCallCommentWithValidParameters() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentWithValidParameters", "Verify success message displayed if User pass valid parameters");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=7)
	public void PostCallCommentByAgencyAdminForCompanyLevelCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByAgencyAdminForCompanyLevelCallId", "Verify success message displayed if Agency User pass call_id from company level parameters");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", Integer.parseInt(call_id_comp_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=8)
	public void PostCallCommentByAgencyAdminForLocationLevelCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByAgencyAdminForLocationLevelCallId", "Verify success message displayed if Agency User pass call_id from location level parameters");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", Integer.parseInt(call_id_loc_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=9)
	public void PostCallCommentByCompanyAdminForLocationLevelCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByCompanyAdminForLocationLevelCallId", "Verify success message displayed if Company User pass call_id from location level parameters");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", Integer.parseInt(call_id_loc_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token_company_admin, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=10)
	public void PostCallCommentWithMultipleObjectsForSameCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentWithMultipleObjectsForSameCallId", "Verify success message displayed if Admin User pass multiple objects with same call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment "+RandomContentGenerator.getRandomString());
			comments.add("Test comment "+RandomContentGenerator.getRandomString());
			json1.put("call_id", Integer.parseInt(call_id_loc_level));
			json1.put("comment", new ArrayList<String>(Arrays.asList(comments.get(0))));
			json2.put("call_id", Integer.parseInt(call_id_loc_level));
			json2.put("comment", json1.put("comment", new ArrayList<String>(Arrays.asList(comments.get(1)))));
			json_arr.add(json1);
			json_arr.add(json2);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=11)
	public void PostCallCommentWithMultipleObjectsForDifferentCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentWithMultipleObjectsForDifferentCallId", "Verify success message displayed if Admin User pass multiple objects with differnent call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment "+RandomContentGenerator.getRandomString());
			comments.add("Test comment "+RandomContentGenerator.getRandomString());
			json1.put("call_id", Integer.parseInt(call_id_comp_level));
			json1.put("comment", new ArrayList<String>(Arrays.asList(comments.get(0))));
			json2.put("call_id", Integer.parseInt(call_id_loc_level));
			json2.put("comment", json1.put("comment", new ArrayList<String>(Arrays.asList(comments.get(1)))));
			json_arr.add(json1);
			json_arr.add(json2);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=12)
	public void PostCallCommentFor50CommentsWithSameCallId() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentFor50CommentsWithSameCallId", "Verify success message displayed if User pass 50 comments to same call_id");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			for(int i=0;i<51;i++) {
				comments.add("Test comment"+RandomContentGenerator.getRandomString());				
			}
			json.put("call_id", Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=13)
	public void PostCallCommentFor50CommentsWithMultipleCallIds() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentFor50CommentsWithMultipleCallIds", "Verify success message displayed if User pass 50 comments to multiple call_ids");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			List<String> comments1=new ArrayList<String>();
			List<String> comments2=new ArrayList<String>();
			for(int i=0;i<51;i++) {
				comments1.add("Test comment"+RandomContentGenerator.getRandomString());				
			}
			for(int i=0;i<51;i++) {
				comments2.add("Test comment"+RandomContentGenerator.getRandomString());				
			}
			json1.put("call_id", Integer.parseInt(call_id_comp_level));
			json1.put("comment", comments1);
			json2.put("call_id", Integer.parseInt(call_id_loc_level));
			json2.put("comment", comments2);
			json_arr.add(json1);
			json_arr.add(json2);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
			}
	}
	
	
	@Test(priority=14)
	public void PostCallCommentForDuplicateComments() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentForDuplicateComments", "Verify error message displayed if User pass duplicate commnent");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("Test comment"+RandomContentGenerator.getRandomString());
			json.put("call_id", Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
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
				   test.log(LogStatus.INFO, "Verifying if err is null");	   
				   String exp_err="null";
				   Assert.assertEquals(String.valueOf(jsonobj.get("err")), exp_err,"Result did not null error.");
				   test.log(LogStatus.INFO, "Verifying if success message is displayed in data field");
				   String data = jsonobj.get("data").toString();
				   String exp_data="Comment added successfully";
				   Assert.assertEquals(data, exp_data,"Result did not success message in data field");
				   
			}
			
			//For duplicate comment
			CloseableHttpResponse response1 = HelperClass.make_post_request("/v2/call/comment", access_token, json_arr);
			Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
			BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
			String line1 = "";
			while ((line1 = rd1.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line1); 
			
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   Assert.assertEquals(result, exp_result,"Result did not return error message");
				   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");	   
				   String err = jsonobj.get("err").toString();
				   String exp_err="Same call_id should not have duplicate comments ";
				   Assert.assertEquals(err, exp_err,"Result did not return appropriate err message");
				   
			}
			
	}
	
	
	@Test(priority=15)
	public void PostCallCommentByCompanyAdminForAgencyLevelCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByCompanyAdminForAgencyLevelCall", "Verify appropriate error message displayed if Company level User pass call_id from Agency level");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			json.put("call_id",Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token_company_admin, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error is displayed in result object");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   Assert.assertEquals(result, exp_result,"Result did not return error");
				   String err = jsonobj.get("err").toString();   
				   String exp_err = "You do not have permission to add comment on to the call id ";
				   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed ");
				   Assert.assertEquals(err, exp_err,"Result did not return error.Error returnrd is "+err);
			}
			
	}
	
	
	@Test(priority=16)
	public void PostCallCommentByLocationAdminForCompanyLevelCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByLocationAdminForCompanyLevelCall", "Verify appropriate error message displayed if Location level User pass call_id from Company level");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			json.put("call_id",Integer.parseInt(call_id_comp_level));
			json.put("comment", comments);
			json_arr.add(json);
			
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token_location_admin, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error is displayed in result object");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   Assert.assertEquals(result, exp_result,"Result did not return error");
				   String err = jsonobj.get("err").toString();   
				   String exp_err = "You do not have permission to add comment on to the call id ";
				   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed ");
				   Assert.assertEquals(err, exp_err,"Result did not return error.Error returnrd is "+err);			
			}
			
	}
	
	
	@Test(priority=17)
	public void PostCallCommentByLocationAdminForAgencyLevelCall() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    test = extent.startTest("PostCallCommentByLocationAdminForAgencyLevelCall", "Verify appropriate error message displayed if Location level User pass call_id from Agency level");
		    test.assignCategory("CFA POST /Call/Comment API");
	
			JSONArray json_arr = new JSONArray();		
			JSONObject json = new JSONObject();
			List<String> comments=new ArrayList<String>();
			comments.add("sj123_comment"+RandomContentGenerator.getRandomString());
			json.put("call_id",Integer.parseInt(call_id_agency_level));
			json.put("comment", comments);
			json_arr.add(json);
		
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/comment", access_token_location_admin, json_arr);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			
				   test.log(LogStatus.INFO, "Verifying if error is displayed in result object");	   
				   String result = jsonobj.get("result").toString();
				   String exp_result="error";
				   Assert.assertEquals(result, exp_result,"Result did not return error");
				   String err = jsonobj.get("err").toString();   
				   String exp_err = "You do not have permission to add comment on to the call id ";
				   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed ");
				   Assert.assertEquals(err, exp_err,"Result did not return error.Error returnrd is "+err);			
			}
			
	}
	
	@AfterClass
	public void cleanUpTags() throws UnsupportedOperationException, IOException, ParseException {
		
		TagsData.cleanUpTags();
		
	}
	
}
