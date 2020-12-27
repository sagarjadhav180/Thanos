package blacklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBBlacklistedNumbers;
import com.convirza.tests.core.utils.DBPhoneNumberUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class PostBlacklistedNumbers extends BaseClass{

	
	String class_name = "GetBlacklistNumbers";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	Long blacklisted_number_company_admin;
	Long blacklisted_number_location_admin;
	List<Long> blacklisted_numbers=new ArrayList<Long>();
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;
			blacklisted_number_company_admin=createBlacklistedNumberForTestData(access_token_company_admin);
			blacklisted_numbers.add(blacklisted_number_company_admin);
			System.out.println(blacklisted_number_company_admin);
			
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;
			blacklisted_number_location_admin=createBlacklistedNumberForTestData(access_token_location_admin);
			blacklisted_numbers.add(blacklisted_number_location_admin);
			System.out.println(blacklisted_number_location_admin);
	}
	
	
	//
	public Long createBlacklistedNumberForTestData(String token) throws UnsupportedOperationException, IOException {

		JSONObject json_obj = new JSONObject();
		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);

		json_obj.put("number", number);
		CloseableHttpResponse response = null; 
		
		try {
			response = HelperClass.make_post_request("/v2/blacklistednumber", token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502
			        || !(response.getStatusLine().getStatusCode() == 502) || response.getStatusLine().getStatusCode() == 401),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
		return number;
		
			
	}
	
	
	@Test(priority=1)
	public void PostBlacklistedNumberForCENumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForCENumber", "To verify that CE numbers can be added to blacklist number by Post method through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = DBPhoneNumberUtils.getPhoneNumberByVendor("ce");
		payload.put("number", Long.parseLong(number));
		blacklisted_numbers.add(Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
	
	
	@Test(priority=2)
	public void PostBlacklistedNumberFor9DigitNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberFor9DigitNumber", "To verify the error message for balcklist less than 10 digit number passed through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "123456789";
		payload.put("number", Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid offset value "+number);
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Value "+number+" is less than minimum 1000000000";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for offset value "+number);
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
		
	}
	
	
	@Test(priority=3)
	public void PostBlacklistedNumberByAgencyAdminForNumberAddedByCompanyAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByAgencyAdminForNumberAddedByCompanyAdmin", "To verify that the Blacklist number added from Company level group that same number can be added from Agency level group");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		Long number = blacklisted_number_company_admin;
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
	
	
	@Test(priority=4)
	public void PostBlacklistedNumberByAgencyAdminForNumberAddedByLocationAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByAgencyAdminForNumberAddedByLocationAdmin", "To verify that the Blacklist number added from Location level group that same number can be added from Agency level group");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number = blacklisted_number_location_admin;
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
	
	
	@Test(priority=5)
	public void PostBlacklistedNumberByAgencyAdminWithValidAccessToken() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByAgencyAdminWithValidAccessToken", "To verify that Admin user from Agency level group is able to add the Blacklist number by Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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

	
	@Test(priority=6)
	public void PostBlacklistedNumberByCompanyAdminWithValidAccessToken() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByCompanyAdminWithValidAccessToken", "To verify that Admin user from Company level group is able to add the Blacklist number by Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token_company_admin, payload);
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
	
	
	@Test(priority=7)
	public void PostBlacklistedNumberBlockedForNewlyAddedGroupAtCompanyLevel() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberBlockedForNewlyAddedGroupAtCompanyLevel", "To verify through API that is a number is blacklisted by billing OU admin user and after if any new group is added then blacklisted number is also bloacked for that new group as well");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		//Blacklisting Number
		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
		
		//Creating Group
		ArrayList<String> test_data1 = HelperClass.readTestData("PostGroup", "post_group_with_valid_access_token");
		String group_id = null;
		Map<String, Object> compConfAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id_agency=compConfAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj1 = new JSONObject();
		json_obj1.put("group_name", test_data1.get(1));
		json_obj1.put("group_parent_id", Integer.parseInt(org_unit_id_agency));
		json_obj1.put("billing_id", Integer.parseInt(org_unit_id_agency));
		json_obj1.put("industry_id", Long.parseLong(test_data1.get(12)));
		
		dni_array.add(json_obj1);
		CloseableHttpResponse response1 = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed.");
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
		while ((line1 = rd1.readLine()) != null) {
			JSONParser parser1 = new JSONParser();
			JSONObject api_response1 =(JSONObject) parser1.parse(line1);	
			JSONArray data_array=(JSONArray) api_response1.get("data");
			JSONObject data_object=(JSONObject) data_array.get(0);
			group_id = data_object.get("data").toString();
			
		}
		
		//Getting blacklisted numbers list
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+group_id));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser2 = new JSONParser();
			   JSONObject json2 = (JSONObject) parser2.parse(line2);
			   String result2 = json2.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result2.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed");
			   Assert.assertNotNull(json2.get("data"),"api returned err "+json2.get("err"));
			   					   
		}
	}
	
	
	@Test(priority=8)
	public void PostBlacklistedNumberByLocationAdminWithValidAccessToken() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByLocationAdminWithValidAccessToken", "To verify that Admin user from Location level group is able to add the Blacklist number by Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token_location_admin, payload);
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
	
	
	@Test(priority=9)
	public void PostBlacklistedNumberWithIncorrectURL() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberWithIncorrectURL", "To verify the error message for incorrect URL passsed for Blacklist number through Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));

		test.log(LogStatus.INFO, "Verifying if 404 status code is receieved");
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumberr", access_token, payload);
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 404), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
	}
	
	
	@Test(priority=10)
	public void PostBlacklistedNumberBlockedForNewlyAddedGroupAtLocationLevel() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberBlockedForNewlyAddedGroupAtLocationLevel", "To verify through API that is a number is blacklisted by company admin user and after if any new child group is added then blacklisted number is also blocked for that child group as well");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		//Blacklisting Number
		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token_company_admin, payload);
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
		
		//Creating Group
		ArrayList<String> test_data1 = HelperClass.readTestData("PostGroup", "post_group_with_valid_access_token");
		String group_id = null;
		Map<String, Object> compConfCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String org_unit_id_company=compConfCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj1 = new JSONObject();
		json_obj1.put("group_name", test_data1.get(1));
		json_obj1.put("group_parent_id", Integer.parseInt(org_unit_id_company));
		json_obj1.put("billing_id", Integer.parseInt(org_unit_id_company));
		json_obj1.put("industry_id", Long.parseLong(test_data1.get(12)));
		
		dni_array.add(json_obj1);
		CloseableHttpResponse response1 = HelperClass.make_post_request("/v2/group", access_token, dni_array);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed.");
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
		while ((line1 = rd1.readLine()) != null) {
			JSONParser parser1 = new JSONParser();
			JSONObject api_response1 =(JSONObject) parser1.parse(line1);	
			JSONArray data_array=(JSONArray) api_response1.get("data");
			JSONObject data_object=(JSONObject) data_array.get(0);
			group_id = data_object.get("data").toString();
			
		}
		
		//Getting blacklisted numbers list
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+group_id));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser2 = new JSONParser();
			   JSONObject json2 = (JSONObject) parser2.parse(line2);
			   String result2 = json2.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result2.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed");
			   Assert.assertNotNull(json2.get("data"),"api returned err "+json2.get("err"));
			   					   
		}
	}
	
	
	@Test(priority=11)
	public void PostBlacklistedNumberFor11DigitNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberFor11DigitNumber", "To verify the error message for balcklist more than 10 digit number passed through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "12345678911";
		payload.put("number", Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid offset value "+number);
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Value "+number+" is greater than maximum 9999999999";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for offset value "+number);
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
		
	}
	
	
	@Test(priority=12)
	public void PostBlacklistedNumberByAgencyAdminWithSingleNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByAgencyAdminWithSingleNumber", "To verify through API that Admin user from Agency level group is able to Blacklist single number at one time");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
	
	
	@Test(priority=13)
	public void PostBlacklistedNumberWithNullNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberWithNullNumber", "To verify the error message for Null Blacklist number is passed through Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "12345678911";
		payload.put("number", null);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid offset value "+number);
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Expected type integer but found type null";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for offset value "+number);
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
		
	}
	
	
	@Test(priority=14)
	public void PostBlacklistedNumberWithIncorrectPrameterNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberWithIncorrectPrameterNumber", "To verify the error message if in the Post method payload passed incorrect filled name 'number' through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "1234567891";
		payload.put("numberr", Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid offset value "+number);
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Missing required property: number";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for offset value "+number);
			   Assert.assertEquals(path.size(),0);
			   					   
		}
		
	}
	
	
	@Test(priority=15)
	public void PostBlacklistedNumberWithAlreadyBlacklistedNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberWithAlreadyBlacklistedNumber", "To verify the error message for duplicate Blacklist number is passed through Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		Map<String, Object> ConfblacklistHierarchy = yamlReader.readBlacklistNumberInfo(Constants.GroupHierarchy.AGENCY);
		String existing_blacklisted_number=ConfblacklistHierarchy.get(TestDataYamlConstants.BlacklistNumberConstants.BLACKLISTED_NUMBER).toString();
		
		payload.put("number", Long.parseLong(existing_blacklisted_number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not return error");
			   test.log(LogStatus.INFO, "Verifying if appoprriate err is displayed");
			   Assert.assertEquals(json.get("err").toString(),existing_blacklisted_number+" is already added in blacklist","api returned err "+json.get("err"));	   
		}		
	}
	
	
	@Test(priority=16)
	public void PostBlacklistedNumberWithoutPayload() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberWithoutPayload", "To verify the error message for empty payload is passed through Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Missing required property: number";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertEquals(path.size(),0);
			   					   
		}
		
	}
	
	
	@Test(priority=0)
	public void PostBlacklistedNumberByCompanyAdminForNumberAddedByLocationAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberByCompanyAdminForNumberAddedByLocationAdmin", "To verify that the Blacklist number added from Location level group that same number can be added from Company level group");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
	
		Long number = blacklisted_number_location_admin;
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token_company_admin, payload);
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
	
	
	@Test(priority=18)
	public void PostBlacklistedNumberDBCheck() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberDBCheck", "To verify through API that Blacklist number added by Admin user on all groups is reflecting on the database");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();

		String num=(RandomContentGenerator.createPhoneNumber());		
		Long number=Long.parseLong(num);
		payload.put("number", (number));
		blacklisted_numbers.add((number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
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
		
		Boolean dbEntry = DBBlacklistedNumbers.getBlacklistedNumberDBEntry(num);
		Assert.assertEquals(String.valueOf(dbEntry),"true","Blacklisted number entry is not reflecting in db");
	}
	
	
	@Test(priority=19)
	public void PostBlacklistedNumberForSPNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForSPNumber", "To verify that SP numbers can be added to blacklist number by Post method through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = DBPhoneNumberUtils.getPhoneNumberByVendor("sp");
		payload.put("number", Long.parseLong(number));
		blacklisted_numbers.add(Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   if(result.equals("error")) {
				   test.log(LogStatus.INFO, json.get("err").toString());
			   }else {
				   test.log(LogStatus.INFO, "Verifying if success message is displayed");
				   Assert.assertTrue(result.equals("success"),"api did not retun success");
				   test.log(LogStatus.INFO, "Verifying if err is null");
				   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));   
			   }	   
		}		
	}
	
	
	@Test(priority=20)
	public void PostBlacklistedNumberForSIPNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForSIPNumber", "To verify that SIP numbers can be added to blacklist number by Post method through API");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = DBPhoneNumberUtils.getPhoneNumberByVendor("sip");
		payload.put("number", Long.parseLong(number));
		blacklisted_numbers.add(Long.parseLong(number));
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   if(result.equals("error")) {
				   test.log(LogStatus.INFO, json.get("err").toString());
			   }else {
				   test.log(LogStatus.INFO, "Verifying if success message is displayed");
				   Assert.assertTrue(result.equals("success"),"api did not retun success");
				   test.log(LogStatus.INFO, "Verifying if err is null");
				   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));   
			   }	  
		}		
	}
	
	
	@Test(priority=21)
	public void PostBlacklistedNumberForSpecialChar() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForSpecialChar", "To verify the error message when charcters are passed in Blacklist number Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "12345678#1";
		payload.put("number", number);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		test.log(LogStatus.INFO, "Verifying if 400 status code is received");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=22)
	public void PostBlacklistedNumberForAlphabet() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForSpecialChar", "To verify the error message when charcters are passed in Blacklist number Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "12345678A7";
		payload.put("number", number);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		test.log(LogStatus.INFO, "Verifying if 400 status code is received");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=23)
	public void PostBlacklistedNumberForBlankNumber() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForBlankNumber", "To verify the error message for Blank Blacklist number is passed through Post method");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "";
		payload.put("number", number);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		test.log(LogStatus.INFO, "Verifying if 400 status code is received");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=24)
	public void PostBlacklistedNumberForNumberStartingWith0() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("PostBlacklistedNumberForNumberStartingWith0", "To verify the error message through API if in 'number' value starting with 0");
		test.assignCategory("CFA POST /blacklistefdnumber/list API");
		
		JSONObject payload=new JSONObject();
		String number = "0234567891";
		payload.put("number", Long.parseLong(number));

		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, payload);
		test.log(LogStatus.INFO, "Verifying if 400 status code is received");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@AfterClass
	public void CleanUpBlacklistedNumber() throws UnsupportedOperationException, IOException, ParseException {

		JSONObject json_obj = new JSONObject();
		json_obj.put("number", blacklisted_numbers);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token, json_obj);
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
