package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
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

@Listeners(Listener.class)
public class GetAudioList extends BaseClass{
	
	String class_name = "GetAudioList";
	ArrayList<String> testdata;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	Map<String,String> filter_map = new HashMap<String,String>();
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException    {
		
			Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
			String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
			access_token_company_admin=response;
			
			Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
			String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
			String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
			access_token_location_admin=response1;
	}
	
	
	@Test(priority=1)
    public void getAudioListfieldsVerification() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListfieldsVerification", "To Verify that data fields returned are as per swagger doc");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   JSONObject data_obj = (JSONObject) data_arr.get(0);
			   String [] exp_fields= {"uploaded_on","file_name","org_unit_id","type","reference_id","recordURL"};
			   
			   for(int i=0;i<exp_fields.length;i++) {
				   test.log(LogStatus.INFO, "Verifying if "+exp_fields[i]+" parameter is present");				   
				   Assert.assertTrue(data_obj.containsKey(exp_fields[i]),exp_fields[i]+" is not present");
				   //storing values in filter_map to use it for filter tcs
				   filter_map.put(exp_fields[i], data_obj.get(exp_fields[i]).toString());
			   }
		}
		
	}
	
	
	@Test(priority=2)
    public void getAudioListWithoutFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListWithoutFilter", "To Verify that data is returned irrespective of any filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
    public void getAudioListWithValidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListWithValidLimit", "To Verify data is returned as per applied limit when valid limit is passed");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getAudioListWithValidLimit");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   Assert.assertEquals(data_arr.size(), Integer.parseInt(limit),"API returned "+data_arr.size()+" when limit applied for "+limit+" object");
		}
		
	}
	
	
	@Test(priority=4)
    public void getAudioListWithValidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListWithValidOffset", "To Verify data is returned as per applied offset when valid offset is passed");
		test.assignCategory("CFA GET audio/list API");
	
		testdata = HelperClass.readTestData(class_name, "getAudioListWithValidOffset");
		String offset = testdata.get(2);
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("offset",String.valueOf(offset)));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps1);
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
	
	
	@Test(priority=5)
    public void getAudioListWithValidLimitAndOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListWithValidLimitAndOffset", "To Verify data is returned as per applied limit and offset when valid limit and offset is passed");
		test.assignCategory("CFA GET audio/list API");
	
		String first_object = null;
		String second_object = null;
		//with limit 
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getAudioListWithValidLimitAndOffset");
		String limit = testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   first_object = first_obj.get("reference_id").toString();
			   second_object = second_obj.get("reference_id").toString();
		}
		
		//with limit and offset
		testdata = HelperClass.readTestData(class_name, "getAudioListWithValidLimitAndOffset");
		String offset = testdata.get(2);
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("offset",offset));
		nvps1.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps1);
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
			   String first_obj = first_obj1.get("reference_id").toString();
			   Assert.assertEquals(first_obj, second_object,"offset is not working");
			   
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject obj = (JSONObject) data_arr.get(i);
				   String exp_reference_id = (obj.get("reference_id").toString());
				   String act_reference_id = (first_object);
				   Assert.assertNotEquals(exp_reference_id, act_reference_id);
			   }
		}
	}
	
	
	@Test(priority=6,dependsOnMethods= {"getAudioListfieldsVerification"})
    public void getAudioListForAgencyLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForAgencyLevel", "To Verify data returned for agency level");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   filterCheck(json,filter_map.get("org_unit_id"),"org_unit_id");
		}
		
	}
	
	
	@Test(priority=7,dependsOnMethods= {"getAudioListfieldsVerification"})
    public void getAudioListForCompanyLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForCompanyLevel", "To Verify data returned for company level");
		test.assignCategory("CFA GET audio/list API");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token_company_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		Map<String, Object> compConfGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String org_unit_id=compConfGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
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
			   test.log(LogStatus.INFO, "Verifying if only audio files are returned added by Company Admin");
			   filterCheck(json,org_unit_id,"org_unit_id");
		}
		
	}
	
	
	@Test(priority=8,dependsOnMethods= {"getAudioListfieldsVerification"})
    public void getAudioListForLocationLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForLocationLevel", "To Verify data returned for company level");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token_location_admin, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());

		Map<String, Object> locConfGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String org_unit_id=locConfGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
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
			   test.log(LogStatus.INFO, "Verifying if only audio files are returned added by Location Admin");
			   filterCheck(json,org_unit_id,"org_unit_id");
		}
		
	}
	
	
	@Test(priority=9,dependsOnMethods= {"getAudioListfieldsVerification"})	
    public void getAudioListForUploadedOnFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForUploadedOnFilter", "To Verify if filtered data is returned when uploaded_on filter is passed");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","uploaded_on%3d"+filter_map.get("uploaded_on")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for uploaded_on");
			   filterCheck(json,filter_map.get("uploaded_on"),"uploaded_on");
		}
		
	}

	
	@Test(priority=10,dependsOnMethods= {"getAudioListfieldsVerification"})	
    public void getAudioListForFileNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForFileNameFilter", "To Verify if filtered data is returned when file_name filter is passed");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","file_name%3d"+filter_map.get("file_name")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for file_name");
			   filterCheck(json,filter_map.get("file_name"),"file_name");
		}
		
	}

	
	@Test(priority=11,dependsOnMethods= {"getAudioListfieldsVerification"})	
    public void getAudioListForOrgUnitFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForOrgUnitFilter", "To Verify if filtered data is returned when org_unit_id filter is passed");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","org_unit_id%3d"+filter_map.get("org_unit_id")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for org_unit_id");
			   filterCheck(json,filter_map.get("org_unit_id"),"org_unit_id");
		}
		
	}
	
	
	@Test(priority=12,dependsOnMethods= {"getAudioListfieldsVerification"})	
    public void getAudioListForTypeFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForTypeFilter", "To Verify if filtered data is returned when type filter is passed");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","type%3d"+filter_map.get("type")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if filtered data is returned when filter applied for type");
			   filterCheck(json,filter_map.get("type"),"type");
		}
		
	}
	
	
	@Test(priority=13)
    public void getAudioListForBlankUploadedOnFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForBlankUploadedOnFilter", "To Verify if appropriate error message is displayed when blank uploaded_on is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","uploaded_on%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
	
	
	@Test(priority=14)
    public void getAudioListForInvalidUploadedOnFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForInvalidUploadedOnFilter", "To Verify if appropriate error message is displayed when invalid uploaded_on is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getAudioListForInvalidUploadedOnFilter");
		String uploaded_on = testdata.get(3);
		nvps.add(new BasicNameValuePair("filter","uploaded_on%3d"+uploaded_on));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is returned in result");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   String err = json.get("err").toString();
			   String exp_err = "Query error in ctPool";
			   Assert.assertTrue(err.startsWith(exp_err),"api did not retun appropriate error message");
		}
	}
	
	
	@Test(priority=15)
    public void getAudioListForNullUploadedOnFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForNullUploadedOnFilter", "To Verify if appropriate error message is displayed when null uploaded_on is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","uploaded_on%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   JSONArray err_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
		}
	}
	
	
	@Test(priority=16)
    public void getAudioListForBlankFileNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForBlankFileNameFilter", "To Verify if appropriate error message is displayed when blank file_name is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","file_name%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
	
	
	@Test(priority=17)
    public void getAudioListForNullFileNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForNullFileNameFilter", "To Verify if appropriate error message is displayed when null file_name is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","file_name%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   JSONArray err_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
		}
	}
	
	
	@Test(priority=18,dependsOnMethods= {"getAudioListfieldsVerification"})
    public void getAudioListForInvalidFileNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForInvalidFileNameFilter", "To Verify if no recors are returned when invalid file_name is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","file_name%3d"+filter_map.get("file_name").concat(".mp3")));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			  // Convert response to JSON object
			  JSONParser parser = new JSONParser();
			  JSONObject json = (JSONObject) parser.parse(line);
			  String result = json.get("result").toString();
			  test.log(LogStatus.INFO, "Verifying if success is returned in result");
			  Assert.assertTrue(result.equals("success"),"api did not retun success");
			  test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			  JSONArray err_arr = (JSONArray) json.get("data");
			  Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
		}
	}
	
	
	@Test(priority=19)
    public void getAudioListForBlankOrgUnitFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForBlankOrgUnitFilter", "To Verify if appropriate error message is displayed when blank org_unit_id is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","org_unit_id%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
	
	
	@Test(priority=20)
    public void getAudioListForNullOrgUnitFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForNullOrgUnitFilter", "To Verify if appropriate error message is displayed when null org_unit_id is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","org_unit_id%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   JSONArray err_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
		}
	}
	
	
	@Test(priority=21)
    public void getAudioListForInvalidOrgUnitFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForInvalidOrgUnitFilter", "To Verify if appropriate error message is displayed when invalid org_unit_id is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getAudioListForInvalidOrgUnitFilter");
		String org_unit_id = testdata.get(4);
		nvps.add(new BasicNameValuePair("filter","org_unit_id%3d"+org_unit_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is returned in result");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   String err = json.get("err").toString();
			   String exp_err = "Query error in ctPool";
			   Assert.assertTrue(err.startsWith(exp_err),"api did not retun appropriate error message");
		}
	}
	
	
	@Test(priority=22)
    public void getAudioListForBlankTypeFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForBlankTypeFilter", "To Verify if appropriate error message is displayed when blank type is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","type%3d"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
	
	
	@Test(priority=23)
    public void getAudioListForNullTypeFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForNullTypeFilter", "To Verify if appropriate error message is displayed when null type is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","type%3d"+null));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
	
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
			   JSONArray err_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
		}
	}
	
	
	@Test(priority=24)
    public void getAudioListForInvalidTypeFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForInvalidTypeFilter", "To Verify if appropriate error message is displayed when invalid type is passed in filter");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata = HelperClass.readTestData(class_name, "getAudioListForInvalidTypeFilter");
		String type = testdata.get(5);
		String[] types = type.split(",");
		
		for(int i=0;i<types.length;i++) {
			nvps.add(new BasicNameValuePair("filter","type%3d"+types[i]));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";    
			while ((line = rd.readLine()) != null) {
				   // Convert response to JSON object
				   JSONParser parser = new JSONParser();
				   JSONObject json = (JSONObject) parser.parse(line);
				   String result = json.get("result").toString();
				   test.log(LogStatus.INFO, "Verifying if success is returned in result");
				   Assert.assertTrue(result.equals("success"),"api did not retun success");
				   test.log(LogStatus.INFO, "Verifying if appropriate error is displayed");
				   JSONArray err_arr = (JSONArray) json.get("data");
				   Assert.assertEquals(err_arr.size(),0,"api did not empty data array");
			}	
		}
		
	}
	
	
	@Test(priority=25)
    public void getAudioListForDefaultRecordsCountCheck() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getAudioListForDefaultRecordsCountCheck", "To Verify that 100 records are returned if no filter and offset is applied");
		test.assignCategory("CFA GET audio/list API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/misc/getaudiolist", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if 100 records are returned");
			   JSONArray data_arr = (JSONArray) json.get("data");
			   Assert.assertEquals(data_arr.size(),100,"api did not return 100 records.No of records are "+data_arr.size());
		}
		
	}
	
    public void filterCheck(JSONObject resposne,String filter_value,String filter_element) {
		
		JSONArray data_arr = (JSONArray) resposne.get("data");
		
		for(int i=0;i<data_arr.size();i++) {
			JSONObject json = (JSONObject) data_arr.get(i);
			String filtered_value = json.get(filter_element).toString();
			String exp_filtered_value = filter_value;
			Assert.assertEquals(filtered_value, exp_filtered_value, "The value _>"+filtered_value+" is not as per filter applied");
		}	
		
	}
	

}
