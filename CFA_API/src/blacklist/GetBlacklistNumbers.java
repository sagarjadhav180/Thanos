package blacklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class GetBlacklistNumbers extends BaseClass{
	String class_name = "GetBlacklistNumbers";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	
	
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
	
	
	@Test(priority=0)
	public void blacklistedNumbersListResposnseFieldsValidations() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListResposnseFieldsValidations", "To validate all fields are dispalyed in response.");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute blacklistednumber api method with valid parameter");
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
				   JSONArray data_array = (JSONArray) json.get("data");
				   JSONObject data_object = (JSONObject) data_array.get(0);
				   Assert.assertTrue(data_object.containsKey("ou_id"),"Response data doesnt contain ou_id field");
				   Assert.assertTrue(data_object.containsKey("number"),"Response data doesnt contain number field");
				    
			}

	}
	
	
	@Test(priority=1)
	public void blacklistedNumbersListWithValidAccessToken() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithValidAccessToken", "To validate whether user is able to get blacklisted numbers through blacklistednumber/list api with valid token");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, list);
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
	public void blacklistedNumbersListWithValidLimitInvalidOffset() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithValidLimitInvalidOffset", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using invalid offset with valid limit.");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			
			test_data=HelperClass.readTestData(class_name, "blcklistedNumbersListWithValidLimitInvalidOffset");
			String limit = test_data.get(1);
			String[] offset_values = test_data.get(2).split(",");
			
			for(String offset_value:offset_values) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				
				nvps.add(new BasicNameValuePair("limit",limit));
				nvps.add(new BasicNameValuePair("offset",offset_value));
				
				CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
			    
				while ((line = rd.readLine()) != null) {
					   // Convert response to JSON object
					   JSONParser parser = new JSONParser();
					   JSONObject json = (JSONObject) parser.parse(line);
					   
					   test.log(LogStatus.INFO, "Verifying if Appropriate error message is dispalyed for invalid offset value "+offset_value);
					   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid offset value "+offset_value);
					   
					   JSONArray jsonarray = (JSONArray)json.get("errors");
					   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
					   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
					   
					   String message = jsonobj.get("message").toString();
					   String exp_messgae = "Invalid parameter (offset): Expected type integer but found type string";
					   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for offset value "+offset_value);
					   Assert.assertEquals(jsonobj.get("name").toString(),"offset");
					   					   
				}
				
			}
			
	}
	
	@Test(priority=3)
	public void blacklistedNumbersListWithNullOuidFilter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		//Execute blacklistedNumbersListWithNullOuidFilter api with valid access_token
		test = extent.startTest("blacklistedNumbersListWithNullOuidFilter", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using filter null fr ouid.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.INFO, "Execute blacklistedNumbersListWithNullOuidFilter api method with valid parameter");
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
			   test.log(LogStatus.INFO, "Verifying if No records are found");			   
			   Assert.assertEquals(json.get("data").toString(), "No record found.");
			   
		}
		

	}
	
	@Test(priority=4)
	public void blacklistedNumbersListWithValidOffsetAndInvalidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffsetAndInvalidLimit", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using invalid limit with valid offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithValidOffsetAndInvalidLimit");
		String offset = test_data.get(2);
		String[] limit_values = test_data.get(1).split(",");
		
		for(String limit_value:limit_values) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			nvps.add(new BasicNameValuePair("offset",offset));
			nvps.add(new BasicNameValuePair("limit",limit_value));

			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   
				   test.log(LogStatus.INFO, "Verifying if Appropriate error message is dispalyed for invalid limit value "+limit_value);
				   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid limit value "+limit_values);
				   
				   JSONArray jsonarray = (JSONArray)json.get("errors");
				   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
				   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
				   
				   String message = jsonobj.get("message").toString();
				   String exp_messgae = "Invalid parameter (limit): Expected type integer but found type string";
				   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for limit value "+limit_value);
				   Assert.assertEquals(jsonobj.get("name").toString(),"limit");
				   					   
			}
			
		}		
		
		
	}
	
	
	@Test(priority=5)
	public void blacklistedNumbersListWithblankOffsetAndValidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithblankOffsetAndValidLimit", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using blank offset with valid limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithblankOffsetAndValidLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit",limit));
		nvps.add(new BasicNameValuePair("offset",null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   
			   test.log(LogStatus.INFO, "Verifying if Appropriate error message is dispalyed for valid limit value and blank offset");
			   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for blank offset");
			   
			   JSONArray jsonarray = (JSONArray)json.get("errors");
			   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
			   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
			   
			   String message = jsonobj.get("message").toString();
			   String exp_messgae = "Invalid parameter (offset): Expected type integer but found type string";
			   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for blank offset");
			   Assert.assertEquals(jsonobj.get("name").toString(),"offset");
			   					   
		}
		
		
	}
	
	
	@Test(priority=6)
	public void blacklistedNumbersListWithInValidFieldLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInValidFieldLimit", "To Verify that the list of blacklisted number at agency level with invalid parameter name (lit).");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithInValidFieldLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("lim",limit));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
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
	public void blacklistedNumbersListWithValidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffset", "To verify that the Admin user from Agency level group is able to get balcklisted number by using valid offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithInValidFieldLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
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
	public void blacklistedNumbersListWithValidOffsetAndLimitBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffsetAndLimitBlank", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using valid offset with blank limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		nvps.add(new BasicNameValuePair("limit"," "));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
		
	}
	
	
	@Test(priority=9)
	public void blacklistedNumbersListWithInvalidURL() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidURL", "Verify if the error message is receieved with invalid url.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithInValidFieldLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumberr", access_token, nvps);
		   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed displayed");
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 404), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=10)
	public void blacklistedNumbersListWithInValidParameterNameForOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInValidParameterNameForOffset", "To Verify that the list of blacklisted number at agency level with invalid parameter name (off).");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("off","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
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
	public void blacklistedNumbersListWithLimitBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithLimitBlank", "To Verify that the list of blacklisted number at agency level with blank limit");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit"," "));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=12)
	public void blacklistedNumbersListWithInvalidOUID() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidOUID", "To verify if if appropriate error message is dispalyed if invalid ouid is passed in filter");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+"abc"+org_unit_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error os displayed");
			   Assert.assertTrue(json.get("err").toString().startsWith("Query error in ctPool."),"api returned err "+json.get("err"));
			   					   
		}
		
		
	}
	
	
	@Test(priority=13)
	public void blacklistedNumbersListWithValidOffsetLimitNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffsetLimitNull", ":To verify that the GET response for Admin user from Agency level group is unable to get balcklisted number by using valid offset with null limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		nvps.add(new BasicNameValuePair("limit","null"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=14)
	public void blacklistedNumbersListWithInvalidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidOffset", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using invalid offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","abc"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="offset";
			   String exp_message="Invalid parameter (offset): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=15)
	public void blacklistedNumbersListWithInvalidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidLimit", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using invalid limit");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithValidOffsetAndInvalidLimit");
		String offset = test_data.get(2);
		String[] limit_values = test_data.get(1).split(",");
		
		for(String limit_value:limit_values) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			nvps.add(new BasicNameValuePair("limit",limit_value));

			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   
				   test.log(LogStatus.INFO, "Verifying if Appropriate error message is dispalyed for invalid limit value "+limit_values);
				   Assert.assertTrue(json.get("message").toString().equals("Validation errors"),"api did not Validation errors for invalid limit value "+limit_values);
				   
				   JSONArray jsonarray = (JSONArray)json.get("errors");
				   JSONObject jsonobj = (JSONObject)jsonarray.get(0);
				   Assert.assertTrue(jsonobj.get("code").equals("INVALID_REQUEST_PARAMETER"),"incorrect errors code dispalyed");
				   
				   String message = jsonobj.get("message").toString();
				   String exp_messgae = "Invalid parameter (limit): Expected type integer but found type string";
				   Assert.assertEquals(message,exp_messgae,"Appropriate errors message not displayed for limit value "+limit_value);
				   Assert.assertEquals(jsonobj.get("name").toString(),"limit");
				   					   
			}
			
		}		
		
		
	}
	
	
	@Test(priority=16)
	public void blacklistedNumbersListWithOffsetBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithOffsetBlank", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using blank offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset"," "));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="offset";
			   String exp_message="Invalid parameter (offset): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=17)
	public void blacklistedNumbersListWithLimitNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithLimitNull", "To Verify that the list of blacklisted number at agency level with null limit");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit","null"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=18)
	public void blacklistedNumbersListWithInvalidOUIDParameter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidOUIDParameter", "verify that the GET response for invalid ou_d parameter with filter");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","o_id%3d"+org_unit_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message displayed");
			   Assert.assertEquals(json.get("err"),"Invalid column specified in filter on rule 1 : o_id="+org_unit_id+"","api returned err "+json.get("err"));
			   					   
		}
			
	}
	
	
	@Test(priority=19)
	public void blacklistedNumbersListWithNegativeOUID() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithNegativeOUID", "verify the GET response for admin user at agency level user with negative OU-id");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+"-"+org_unit_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed");
			   Assert.assertEquals(json.get("data"),"No record found.","api returned err "+json.get("err"));
			   					   
		}
			
	}
	
	
	@Test(priority=20)
	public void blacklistedNumbersListWithValidOUIDFilter() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithValidOUIDFilter", "To verify that the Admin user from Agency level group is able to get balcklisted number by using filte for valid OUID.");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
			String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("filter","ou_id%3d"+org_unit_id));
			
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute blacklistednumber api method with valid parameter");
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
	
	
	@Test(priority=21)
	public void blacklistedNumbersListWithOffsetNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithLimitNull", "To verify that the Admin user from Agency level group is unable to get balcklisted number by using null offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","null"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="offset";
			   String exp_message="Invalid parameter (offset): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=22)
	public void blacklistedNumbersListWithValidLimit() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithValidLimit", "To Verify that the list of blacklisted number at agency level with valid limit");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
			String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("limit","1"));
			
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.INFO, "Execute blacklistednumber api method with valid parameter");
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
	
	
	@Test(priority=23)
	public void blacklistedNumbersListWithValidOffsetAndLimitBlankForCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffsetAndLimitBlankForCompanyAdmin", "To verify that the Admin user from Company level group is unable to get balcklisted number by using valid offset with blank limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		nvps.add(new BasicNameValuePair("limit"," "));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
		
	}
	
	
	@Test(priority=24)
	public void blacklistedNumbersListWithInvalidURLComponyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidURLComponyAdmin", "Verify if the error message is receieved with invalid url.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithInValidFieldLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));

		test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumberr", access_token_company_admin, nvps);
		Assert.assertTrue((response.getStatusLine().getStatusCode() == 404), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
	}
	
	
	@Test(priority=24)
	public void blacklistedNumbersListWithValidOffsetLimitNullCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValidOffsetLimitNullCompanyAdmin", ":To verify that the GET response for Admin user from Company level group is unable to get balcklisted number by using valid offset with null limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","1"));
		nvps.add(new BasicNameValuePair("limit","null"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=25)
	public void blacklistedNumbersListWithInvalidOUIDCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidOUIDCompanyAdmin", "To verify if appropriate error message is dispalyed if invalid ouid is passed in filter");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","ou_id%3d"+"abc"+org_unit_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate error os displayed");
			   Assert.assertTrue(json.get("err").toString().startsWith("Query error in ctPool."),"api returned err "+json.get("err"));
			   					   
		}
		
	}
	
	
	@Test(priority=26)
	public void blacklistedNumbersListWithInvalidOffsetCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInvalidOffset", "To verify that the Admin user from Company level group is unable to get balcklisted number by using invalid offset.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","abc"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="offset";
			   String exp_message="Invalid parameter (offset): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=27)
	public void blacklistedNumbersListWithValidLimitAndNullOffsetCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithValiLimitNullOffsetCompanyAdmin", "To verify that the GET response for Admin user from Company level group is unable to get balcklisted number by using valid limit and null offset");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset","null"));
		nvps.add(new BasicNameValuePair("limit","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="offset";
			   String exp_message="Invalid parameter (offset): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=28)
	public void blacklistedNumbersListWithNullLimitCompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithNullLimitCompanyAdmin", "To verify that the GET response for Admin user from Company level group is unable to get balcklisted number by using null limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit","null"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Expected type integer but found type string";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=29)
	public void blacklistedNumbersListWithLimit0CompanyAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithLimit0CompanyAdmin", "To verify that the GET response for Admin user from Company level group is unable to get balcklisted number by using 0 limit.");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit","0"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
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
			   String exp_name="limit";
			   String exp_message="Invalid parameter (limit): Value failed JSON Schema validation";
			   
			   Assert.assertEquals(name, exp_name);
			   Assert.assertEquals(message, exp_message,"Appropriate validation message not displayed");
			   					   
		}
		
	}
	
	
	@Test(priority=30)
	public void blacklistedNumbersListWithInValidFieldOffsetLocationAdmin() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("blacklistedNumbersListWithInValidFieldOffsetLocationAdmin", "To Verify that the list of blacklisted number at agency level with invalid parameter name (off).");
		test.assignCategory("CFA GET /blacklistefdnumber/list API");
		
		test_data=HelperClass.readTestData(class_name, "blacklistedNumbersListWithInValidFieldLimit");
		String limit = test_data.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("off","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
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
	
	
	@Test(priority=31)
	public void blacklistedNumbersListWithInValidParameterFilterLocationAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithInValidParameterFilter", "To verify that the Admin user from Location level group is able to get balcklisted number by using invalid filter parameter(filt).");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
			String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("filt","ou_id%3d"+org_unit_id));
			
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
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
	
	
	@Test(priority=32)
	public void blacklistedNumbersListWithFilterPrameterBlankForOuIDLocationAdmin() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListWithFilterPrameterBlankForOuID", "To verify that the Admin user from Location level group is not able to get balcklisted number by using blank ouid in filter");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
			String org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("filter","ou_id%3d"+""));
			
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_location_admin, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result = json.get("result").toString();
				   test.log(LogStatus.INFO, "Verifying if error message is displayed");
				   Assert.assertTrue(result.equals("error"),"api did not retun error");
				   test.log(LogStatus.INFO, "Verifying err message");
				   Assert.assertEquals(json.get("err"),"Please provide valid data.","api returned err "+json.get("err"));	   
			}

	}
	
	
	@Test(priority=33)
	public void blacklistedNumbersListInheritanceInChildGroups() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		    //Execute blacklistednumber api with valid access_token
			test = extent.startTest("blacklistedNumbersListInheritanceInChildGroups", "To verify if number blacklisted at agency level is blacklisted for all of its subgroups");
			test.assignCategory("CFA GET /blacklistefdnumber/list API");
			
			Map<String, Object> confBlacklistedNumbersHierarchy = yamlReader.readBlacklistNumberInfo(Constants.GroupHierarchy.AGENCY);
			String blacklisted_number=confBlacklistedNumbersHierarchy.get(TestDataYamlConstants.BlacklistNumberConstants.BLACKLISTED_NUMBER).toString();
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
			String group_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("filter","ou_id%3d"+group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/blacklistednumber", access_token_company_admin, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
		    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				      
				   JSONArray array=(JSONArray) json.get("data");
				   
				   for(int i=0;i<array.size();i++) {
					   
					   JSONObject obj=(JSONObject) array.get(i);
					   JSONArray numbers=(JSONArray) obj.get("number");
					   Assert.assertTrue(numbers.contains(blacklisted_number),"number blacklisted at agency level is not blacklisted for all of its subgroups");
				   }
			}

	}
	
}
