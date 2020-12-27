package blacklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBBlacklistedNumbers;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class DeleteBlacklistedNumbers extends BaseClass{

	String class_name = "GetBlacklistNumbers";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	List<Long> blacklisted_numbers=new ArrayList<Long>();
	
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;
			
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;

	}
	
	
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
	public void DeleteBlacklistedNumberAtCompanyLevelAddedByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtCompanyLevelAddedByAgencyAdmin", "Verify Company level user is able to delete number from blacklist which is added from Agency level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_company_admin, json_obj);
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
	

	@Test(priority=2)
	public void DeleteBlacklistedNumberAtLocationLevelAddedByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtLocationLevelAddedByAgencyAdmin", "Verify Location level user is able to delete number from blacklist which is added from Agency level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
	
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_location_admin, json_obj);
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
	
	
	@Test(priority=3)
	public void DeleteBlacklistedNumberAtLocationLevelAddedByCompanyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtLocationLevelAddedByAgencyAdmin", "Verify Location level user is able to delete number from blacklist which is added from Company level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_company_admin);
	
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_location_admin, json_obj);
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
	
	
	@Test(priority=4)
	public void DeleteBlacklistedNumberAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAgencyAdmin", "Verify Admin user is able to delete number from Blacklist.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
	
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=5)
	public void DeleteBlacklistedNumberForAlreadyDeletedNumber() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberForAlreadyDeletedNumber", "Verify Admin user is unable to delete number which is already deleted from Blacklist.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
	
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
		
		//deleting already deleted blacklisted number
		JSONObject json_obj1 = new JSONObject();
		List<Long> list1=new ArrayList<Long>();
		list1.add(number);
		json_obj1.put("number", list1);
        HttpResponse response1 = null; 
		
		try {
			response1 = HelperClass.make_delete_request("/v2/blacklistednumber", access_token, json_obj1);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
	    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser1 = new JSONParser();
			   JSONObject json1 = (JSONObject) parser1.parse(line1);
			   String result = json1.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not return error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   JSONArray err_arr=(JSONArray) json1.get("err");
			   Assert.assertTrue(err_arr.contains("Invalid number(s) : "+number+""));
		}
			
	}
	
	
	@Test(priority=6)
	public void DeleteBlacklistedNumberForMultipleNumbers() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberForMultipleNumbers", "Verify Admin user is able to delete multiple number from Blacklist.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number1 = createBlacklistedNumberForTestData(access_token);
		Long number2 = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number1);
		list.add(number2);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=7)
	public void DeleteBlacklistedNumberForDuplicateNumbers() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberForDuplicateNumbers", "Verify Admin user passes duplicate entries of numbers in array & is able to delete number from Blacklist in single hit.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		list.add(number);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=8)
	public void DeleteBlacklistedNumberFor10DigitNumbers() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberFor10DigitNumbers", "Verify Admin user is able to delete only or exact 10 digit of number from Blacklist.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=9)
	public void DeleteBlacklistedNumberForStatusCodeVerification() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberForStatusCodeVerification", "Verify status code when blacklisted number is deleted successsfully throgh API .");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 200), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
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
	
	
	@Test(priority=10)
	public void DeleteBlacklistedNumberAtCompanyLevelAddedByLocationAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtCompanyLevelAddedByLocationAdmin", "Verify Company level user is able to delete number from blacklist which is added from location level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_location_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_company_admin, json_obj);
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
	
	
	@Test(priority=11)
	public void DeleteBlacklistedNumberAtAgencyLevelAddedByLocationAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtAgencyLevelAddedByLocationAdmin", "Verify Agency level user is able to delete number from Blacklist, which is added from Location level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_location_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=12)
	public void DeleteBlacklistedNumberAtAgencyLevelAddedByCompanyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberAtAgencyLevelAddedByCompanyAdmin", "Verify Agency level user is able to delete number from Blacklist through API, which is added from Company level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_company_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
	
	
	@Test(priority=13)
	public void DeleteBlacklistedNumberDBVerificationForAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberDBVerification", "Verify Agency level user is deleted number from Blacklist that number is deleted from DB.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
			
		//DB Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from db");
		Boolean db_entry = DBBlacklistedNumbers.getBlacklistedNumberDBEntry(String.valueOf(number));
//		Assert.assertTrue(db_entry.compareTo(false),"Blacklisted number entry removed form db");
		Assert.assertEquals(String.valueOf(db_entry),"false","Blacklisted number entry removed form db");
		
	}
	
	
	@Test(priority=14)
	public void DeleteBlacklistedNumberWithNumberBlank() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberBlank", "Verify Admin user is unable to delete number from Blacklist which is blank.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
//		list.add(number);
		json_obj.put("number", list);
		
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
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Array is too short (0), minimum 1";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
			
		
	}
	
	
	@Test(priority=15)
	public void DeleteBlacklistedNumberWithMultipleNumbersContainingOneincorrectNumber() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithMultipleNumbersContainingOneincorrectNumber", "Verify Admin user passes valid,invalid entries of numbers in array & is unable to delete number from Blacklist in single hit.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		Long incorret_number = 12345678L;
		
		//Deleting blacklisted number
		list.add(number);
		list.add(incorret_number);
		json_obj.put("number", list);
		
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
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Value "+incorret_number+" is less than minimum 1000000000";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
			
	}

	
	@Test(priority=16)
	public void DeleteBlacklistedNumberWithNumberLessThan10Digit() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberLessThan10Digit", "Verify Admin user is unable to delete number from Blacklist which is less than 10 digit.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
//		Long number = createBlacklistedNumberForTestData(access_token);
		Long incorret_number = 12345678L;
		
		//Deleting blacklisted number
//		list.add(number);
		list.add(incorret_number);
		json_obj.put("number", list);
		
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
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Value "+incorret_number+" is less than minimum 1000000000";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
			
	}
	
	
	@Test(priority=17)
	public void DeleteBlacklistedNumberWithNumberStartingFrom0() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberStartingFrom0", "Verify Admin user is unable to delete number from Blacklist which start with zero.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		String incorret_number = "012345678";
		
		//Deleting blacklisted number
		list.add(Long.parseLong(incorret_number));
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		test.log(LogStatus.INFO, "Verifying if status code 400 is displayed");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
			
	}
	
	
	@Test(priority=18)
	public void DeleteBlacklistedNumberWithNumberMoreThan10Digit() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberMoreThan10Digit", "Verify Admin user is unable to delete number from Blacklist which Is more than 10 digit.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long incorret_number = 12345678999L;
		
		//Deleting blacklisted number
		list.add(incorret_number);
		json_obj.put("number", list);
		
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
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Value "+incorret_number+" is greater than maximum 9999999999";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
			
	}
	
	
	@Test(priority=19)
	public void DeleteBlacklistedNumberWithNumberHavingAplhabet() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberHavingAplhabet", "Verify Admin user is unable to delete number from Blacklist which is invalid.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<String> list=new ArrayList<String>();
		String incorret_number = "123456789A";
		
		//Deleting blacklisted number
		try {
			list.add(incorret_number);			
		}catch(Exception e) {
			e.printStackTrace();
		}

		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		test.log(LogStatus.INFO, "Verifying if status code 400 is displayed");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=19)
	public void DeleteBlacklistedNumberWithNumberNull() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithNumberNull", "Verify Admin user is unable to delete number from Blacklist wich is null.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		
		//Deleting blacklisted number
		list.add(null);
		json_obj.put("number", list);
		
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
			   
			   test.log(LogStatus.INFO, "Verifying if Validations error message is dispalyed.");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not give Validation errors");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   JSONArray nestede_rror_arr = (JSONArray) jsonobj.get("errors");
			   JSONObject nested_error_obj = (JSONObject) nestede_rror_arr.get(0);
			   JSONArray path=(JSONArray) nested_error_obj.get("path");
			   
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = nested_error_obj.get("message").toString();
			   String exp_messgae = "Expected type integer but found type null";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed ");
			   Assert.assertTrue(path.contains("number"));
			   					   
		}
			
	}
	
	
	@Test(priority=20)
	public void DeleteBlacklistedNumberDBVerificationForCompanyAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberDBVerificationForCompanyAdmin", "Verify company level user is deleted number from Blacklist that number is deleted from DB.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_company_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
			
		//DB Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from db");
		Boolean db_entry = DBBlacklistedNumbers.getBlacklistedNumberDBEntry(String.valueOf(number));
//		Assert.assertTrue(db_entry.compareTo(false),"Blacklisted number entry removed form db");
		Assert.assertEquals(String.valueOf(db_entry),"false","Blacklisted number entry removed form db");
		
	}
	
	
	@Test(priority=21)
	public void DeleteBlacklistedNumberDBVerificationForLocationAdmin() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberDBVerificationForLocationAdmin", "Verify location level user is deleted number from Blacklist that number is deleted from DB.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_location_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
			
		//DB Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from db");
		Boolean db_entry = DBBlacklistedNumbers.getBlacklistedNumberDBEntry(String.valueOf(number));
//		Assert.assertTrue(db_entry.compareTo(false),"Blacklisted number entry removed form db");
		Assert.assertEquals(String.valueOf(db_entry),"false","Blacklisted number entry removed form db");
		
	}
	
	
	@Test(priority=22)
	public void DeleteBlacklistedNumberWithIncorrectParameterName() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithIncorrectParameterName", "Verify error message if you are sending request with incorrect parameter name in payload.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number=createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("numb", list);
		
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
			   Assert.assertTrue(path.size()==0);
			   					   
		}
			
	}
	
	
	@Test(priority=23)
	public void DeleteBlacklistedNumberByLocationAdminAddedByCompanyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteBlacklistedNumberByLocationAdminAddedByCompanyAdmin", "Verify Location level user deleted number(which was added from agency level) from Blacklist that number is deleted only for location level");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_company_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_location_admin, json_obj);
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
			
		//Location Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from location level");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
        Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
        String org_unit_id_loc = confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_loc+",number%3d"+number));
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithLocationOuidAndnumberFilter api method with valid parameter");
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
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		
		//Company Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is not removed from company level");
        List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
        String org_unit_id_comp = confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps2.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_comp+",number%3d"+number));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps2);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithCompanyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line2);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if records are found");			 
			   JSONArray data=(JSONArray) json.get("data");
			   JSONObject jo=(JSONObject) data.get(0);
			   Assert.assertEquals(jo.get("ou_id").toString(), org_unit_id_comp);
			   JSONArray number_arr=(JSONArray) jo.get("number");
			   Assert.assertTrue(number_arr.contains(number.toString()));
			   
		}
		
	}
	
	
	@Test(priority=24)
	public void DeleteBlacklistedNumberByLocationAdminAddedByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteBlacklistedNumberByLocationAdminAddedByCompanyAdmin", "Verify Location level user deleted number(which was added from agency level) from Blacklist that number is deleted only for location level");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_location_admin, json_obj);
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
			
		//Location Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from location level");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
        Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
        String org_unit_id_loc = confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_loc+",number%3d"+number));
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithLocationOuidAndnumberFilter api method with valid parameter");
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
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		
		//Company Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is not removed from company level");
        List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
        String org_unit_id_comp = confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps2.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_comp+",number%3d"+number));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps2);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithCompanyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line2);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if records are found");			 
			   JSONArray data=(JSONArray) json.get("data");
			   JSONObject jo=(JSONObject) data.get(0);
			   Assert.assertEquals(jo.get("ou_id").toString(), org_unit_id_comp);
			   JSONArray number_arr=(JSONArray) jo.get("number");
			   Assert.assertTrue(number_arr.contains(number.toString()));
			   
		}
		
		//Agency Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is not removed from agency level");
        List<NameValuePair> nvps3 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
        String org_unit_id_agency = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps3.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_agency+",number%3d"+number));
		
		CloseableHttpResponse response3 = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps3);
		Assert.assertTrue(!(response3.getStatusLine().getStatusCode() == 500 || response3.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response3.getStatusLine().getStatusCode()+" "+response3.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithAgencyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd3 = new BufferedReader(new InputStreamReader(response3.getEntity().getContent()));
		String line3 = "";
	    
		while ((line3 = rd3.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line3);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if records are found");			 
			   JSONArray data=(JSONArray) json.get("data");
			   JSONObject jo=(JSONObject) data.get(0);
			   Assert.assertEquals(jo.get("ou_id").toString(), org_unit_id_agency);
			   JSONArray number_arr=(JSONArray) jo.get("number");
			   Assert.assertTrue(number_arr.contains(number.toString()));
			   
		}		
		
	}
	
	
	@Test(priority=25)
	public void DeleteBlacklistedNumberWithoutPayload() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberWithoutPayload", "Verify error message if you are sending request without payload.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number=createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("numb", list);
		
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
			   Assert.assertTrue(path.size()==0);
			   					   
		}
			
	}
	
	
	@Test(priority=26)
	public void DeleteBlacklistedNumberIncorrectURL() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteBlacklistedNumberIncorrectURL", "Verify error message if you are sending request with incorrect URL.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number=createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("numb", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumberr", access_token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		test.log(LogStatus.INFO, "Verifying if status code 404 is displayed");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 404), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());		
	
	}
	
	
	@Test(priority=27)
	public void DeleteBlacklistedNumberByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteBlacklistedNumberByAgencyAdmin", "Verify Agency level user deleted number from Blacklist that number is deleted form Company & Location level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
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
			
		//Location Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from location level");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
        Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
        String org_unit_id_loc = confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_loc+",number%3d"+number));
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithLocationOuidAndnumberFilter api method with valid parameter");
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
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		
		//Company Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from company level");
        List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
        String org_unit_id_comp = confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps2.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_comp+",number%3d"+number));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps2);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithCompanyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line2);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		
		//Agency Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is not removed from agency level");
        List<NameValuePair> nvps3 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
        String org_unit_id_agency = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps3.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_agency+",number%3d"+number));
		
		CloseableHttpResponse response3 = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps3);
		Assert.assertTrue(!(response3.getStatusLine().getStatusCode() == 500 || response3.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response3.getStatusLine().getStatusCode()+" "+response3.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithAgencyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd3 = new BufferedReader(new InputStreamReader(response3.getEntity().getContent()));
		String line3 = "";
	    
		while ((line3 = rd3.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line3);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}		
		
	}
	
	
	@Test(priority=28)
	public void DeleteBlacklistedNumberByCompanyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteBlacklistedNumberByCompanyAdmin", "Verify Company level user deleted number from Blacklist that number is deleted form Company & Location level.");
		test.assignCategory("CFA DELETE /blacklistefdnumber/list API");
	
		//blacklisting number
		JSONObject json_obj = new JSONObject();
		List<Long> list=new ArrayList<Long>();
		Long number = createBlacklistedNumberForTestData(access_token_company_admin);
		
		//Deleting blacklisted number
		list.add(number);
		json_obj.put("number", list);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/blacklistednumber", access_token_company_admin, json_obj);
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
			
		//Location Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from location level");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
        Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
        String org_unit_id_loc = confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_loc+",number%3d"+number));
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithLocationOuidAndnumberFilter api method with valid parameter");
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
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		
		//Company Level Verification
		test.log(LogStatus.INFO, "Verifying if blacklisted number is removed from company level");
        List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		
        Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
        String org_unit_id_comp = confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		nvps2.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id_comp+",number%3d"+number));
		
		CloseableHttpResponse response2 = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps2);
		Assert.assertTrue(!(response2.getStatusLine().getStatusCode() == 500 || response2.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response2.getStatusLine().getStatusCode()+" "+response2.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithCompanyOuidAndNumberFilter api method with valid parameter");
		BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
		String line2 = "";
	    
		while ((line2 = rd2.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line2);
			   String success = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(success.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));	   
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
				
	}

	
	@AfterClass
	public void CleanUpBlacklistedNumber() throws UnsupportedOperationException, IOException, ParseException {

        Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
        String org_unit_id_agency = confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
        Map<String, Object> confCompGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
        String org_unit_id_comp = confCompGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
        Map<String, Object> confLocGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
        String org_unit_id_loc = confLocGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
        
        List blckNumersByAgency = DBBlacklistedNumbers.getBlacklistedNumbers(org_unit_id_agency);
        List blckNumersByCompany = DBBlacklistedNumbers.getBlacklistedNumbers(org_unit_id_comp);
        List blckNumersByLocationy = DBBlacklistedNumbers.getBlacklistedNumbers(org_unit_id_loc);       
		
        blacklisted_numbers.addAll(blckNumersByAgency);
        blacklisted_numbers.addAll(blckNumersByCompany);
        blacklisted_numbers.addAll(blckNumersByLocationy);
        
		if(blacklisted_numbers.size()>0) {
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
	
}
