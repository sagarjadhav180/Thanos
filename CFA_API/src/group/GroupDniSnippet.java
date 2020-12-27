package group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBDNIUtils;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class GroupDniSnippet extends BaseClass{
	String class_name = "GroupDniSnippet";
	ArrayList<String> test_data;
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@Test(priority=0)
	public void group_dnisnippet_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_dnisnippet_without_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet api without access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/group/dnisnippet?id=8", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=1)
	public void group_dnisnippet_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_dnisnippet_with_invalid_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet api with invalid access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_with_invalid_access_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void group_dnisnippet_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("group_dnisnippet_with_expired_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet with expired access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_with_expired_access_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", test_data.get(1)));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void group_dnisnippet_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_with_valid_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet with valid access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_with_valid_access_token");
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		list.add(new BasicNameValuePair("id", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   System.out.println(line);
		   Assert.assertEquals(json.get("result").toString(), "success", "API retuns error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check whether API returns success when valid access_token is passed.");
		   Object err_data = json.get("err");
		   Assert.assertNull(err_data, "err is not null when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check err is null when valid access_token is passed.");
		   JSONArray data_arr = (JSONArray) json.get("data");
		   JSONObject dni_snippet_data = (JSONObject) data_arr.get(0);
		   Assert.assertEquals(dni_snippet_data.size(), 1, "DNI snippet is retuning more than 1 records");
		   test.log(LogStatus.PASS, "Check DNI snippet api is not retuning more than 1 records");
		   Assert.assertTrue(dni_snippet_data.containsKey(groupId), "API does not return dni snippet of passed group id.");
		   test.log(LogStatus.PASS, "Check API is returning dni snippet of passed group id."); 

		   ArrayList<String> src = new ArrayList<String>();
		   src.add("url");
		   ArrayList<String> dni_src = HelperClass.read_config(src);
		   String staging = dni_src.get(0);
		   String stag = (staging.replaceAll("[^\\d]", ""));
		   String dni_code=DBDNIUtils.getDNICode(groupId);
		   
		   String dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag-"+stag.substring(0, 1)+"-dni-1.convirza.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord('"+dni_code+"', 'dni.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
		   
		   Assert.assertEquals(dni_snippet_data.get(groupId), dni_copy_code, "API is not returning dni copy code as it returns on application.");
		   test.log(LogStatus.PASS, "Check dni snippet is retuning dni snippet as it returns on application.");
		}
	}
	
	@Test(priority=4)
	public void group_dnisnippet_api_without_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_api_without_id", "To validate whether user is able to get dnisnippet through group/dnisnippet api without id parameter.");
		test.assignCategory("CFA GET /group/dnisnippet API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when id parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String message = json.get("message").toString();
		   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when id value is not passed");		
		   JSONArray errors_array = (JSONArray)json.get("errors");
		   JSONObject error_data = (JSONObject) errors_array.get(0);
		   String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when id value is not passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Value is required but was not provided", "Invalid message value is returned in response when id value is not passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when id value is not passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			JSONArray path_data = new JSONArray();
			path_data.add("paths");
			path_data.add("/group/dnisnippet");
			path_data.add("get");
			path_data.add("parameters");
			path_data.add("0");
			Assert.assertEquals(error_path, path_data);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "REQUIRED", "Invalid code value is returned in response when id value is not passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Value is required but was not provided", "Invalid message value is returned in response when id value is not passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path, path_data, "path is not blank when id value is not passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when id value is not passed.");
		}   
	}
	
	@Test(priority=5)
	public void group_dnisnippet_api_with_blank_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_api_without_id", "To validate whether user is able to get dnisnippet through group/dnisnippet api with blank id value.");
		test.assignCategory("CFA GET /group/dnisnippet API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when blank id is passed.");
		   test.log(LogStatus.PASS, "Check valid result value is displayed when blank id is passed.");
		   Assert.assertEquals(json.get("err").toString(), "Id cannot be blank", "Proper validation message is not displayed when blank id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when blank id is passed.");
		}   
	}	
	
	@Test(priority=6)
	public void group_dnisnippet_api_with_invalid_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_api_with_invalid_id", "To validate whether user is able to get dnisnippet through group/dnisnippet api with invalid id value.");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_api_with_invalid_id");
		String[] group_ids = test_data.get(1).split(",");
		for(String group_id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", group_id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+group_id+") id value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when invalid("+group_id+") id is passed.");
			   test.log(LogStatus.PASS, "Check valid result value is displayed when invalid("+group_id+") id is passed.");
			   if(group_id.equals("-8"))
				   Assert.assertEquals(json.get("err").toString(), "You are not authorized to view the requested dni snippet for group id: "+group_id, "Proper validation message is not displayed when invalid("+group_id+") id is passed.");
			   else
				   Assert.assertEquals(json.get("err").toString(), "Not a valid group id: "+group_id, "Proper validation message is not displayed when invalid("+group_id+") id is passed.");
			   test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid("+group_id+") id is passed.");
			} 
		}  
	}
	
	@Test(priority=7)
	public void group_dnisnippet_api_with_0_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_api_with_0_id_value", "To validate whether user is able to get dnisnippet through group/dnisnippet api with 0 id value.");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_api_with_0_id_value");
		String group_id = test_data.get(1);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when 0 id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when 0 id is passed.");
		   test.log(LogStatus.PASS, "Check valid result value is displayed when 0 id is passed.");
		   Assert.assertEquals(json.get("err").toString(), "You are not authorized to view the requested dni snippet for group id: "+group_id, "Proper validation message is not displayed when 0 id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when 0 id is passed.");
		} 
	} 
	
	@Test(priority=8)
	public void group_dnisnippet_api_with_nonexisting_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_api_with_nonexisting_id_value", "To validate whether user is able to get dnisnippet through group/dnisnippet api with non existing id value.");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_api_with_nonexisting_id_value");
		String group_id = test_data.get(1);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", group_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing group id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   System.out.println(line);
		   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when non existing group id is passed.");
		   test.log(LogStatus.PASS, "Check valid result value is displayed when non existing group id is passed.");
		   Assert.assertEquals(json.get("err").toString(), "You are not authorized to view the requested dni snippet for group id: "+group_id, "Proper validation message is not displayed when non existing group id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when non existing group id is passed.");
		} 
	} 	
	
	@Test(priority=9) //Uncomment code when CT-15506 is fixed
	public void group_dnisnippet_with_valid_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_with_valid_id_value", "To validate whether user is able to get dnisnippet through group/dnisnippet with valid group id value");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_with_valid_id_value");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		list.add(new BasicNameValuePair("id", groupId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid group id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   System.out.println(line);
		   Assert.assertEquals(json.get("result").toString(), "success", "API retuns error when valid group id is passed.");
		   test.log(LogStatus.PASS, "Check whether API returns success when valid group id is passed.");
		   Object err_data = json.get("err");
		   Assert.assertNull(err_data, "err is not null when valid group id is passed.");
		   test.log(LogStatus.PASS, "Check err is null when valid group id is passed.");
		   JSONArray data_arr = (JSONArray) json.get("data");
		   JSONObject dni_snippet_data = (JSONObject) data_arr.get(0);
		   Assert.assertEquals(dni_snippet_data.size(), 1, "DNI snippet is retuning more than 1 records");
		   test.log(LogStatus.PASS, "Check DNI snippet api is not retuning more than 1 records");
		   Assert.assertTrue(dni_snippet_data.containsKey(groupId), "API does not return dni snippet of passed group id.");
		   test.log(LogStatus.PASS, "Check API is returning dni snippet of passed group id."); 

		   String dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag-7-dni-1.convirza.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord('cedb7f24376214d5fe503683cac8ab74', 'dni.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";

		   Assert.assertEquals(dni_snippet_data.get(groupId), dni_copy_code, "API is not returning dni copy code as it returns on application.");
		   test.log(LogStatus.PASS, "Check dni snippet is retuning dni snippet as it returns on application.");
		}
	}	
	
	@Test(priority=10) //Uncomment code when CT-15506 is fixed
	public void group_dnisnippet_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_with_agency_admin_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet with agency admin access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_with_agency_admin_access_token");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(agency_group) || id.equals(company_group) || id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"location level"))+" group id is passed using agency admin access_token.");
				   JSONArray data_arr = (JSONArray) json.get("data");
				   JSONObject dni_snippet_data = (JSONObject) data_arr.get(0);
				   Assert.assertEquals(dni_snippet_data.size(), 1, "DNI snippet is retuning more than 1 records");
				   test.log(LogStatus.PASS, "Check DNI snippet api is not retuning more than 1 records");
				   Assert.assertTrue(dni_snippet_data.containsKey(id), "API does not return dni snippet of passed group id.");
				   test.log(LogStatus.PASS, "Check API is returning dni snippet of passed group id."); 
//				   String dni_copy_code = "";
//				   if(id.equals(agency_group))
//					   dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord('c9f0f895fb98ab9159f51fd0297e236d', 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   else if(id.equals(company_group))
//					   dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord('5fd0b37cd7dbbb00f97ba6ce92bf5add', 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   else if(id.equals(location_group))
//					   dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord('c45147dee729311ef5b5c3003946c48f', 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   Assert.assertEquals(dni_snippet_data.get(id), dni_copy_code, "API is not returning dni copy code as it returns on application.\nDefect Reported: CT-15506");
//				   test.log(LogStatus.PASS, "Check dni snippet is retuning dni snippet as it returns on application.");
			   }
			   else if(id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when other billing group id is passed using agency admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested dni snippet for group id: "+id, "Proper validation message is not displayed when other billing group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when other billing group id is passed using agency admin access_token.");
			   }
			}
		}
	}	
	
	@Test(priority=11) //Uncomment code when CT-15506 is fixed
	public void group_dnisnippet_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_with_company_admin_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet with company admin access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		
		// Get access_token of company admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		test_data = HelperClass.readTestData(class_name, "group_dnisnippet_with_company_admin_access_token");

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(company_group) || id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using company admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(company_group)?"company level":"location level")+" group id is passed using company admin access_token.");
				   JSONArray data_arr = (JSONArray) json.get("data");
				   JSONObject dni_snippet_data = (JSONObject) data_arr.get(0);
				   Assert.assertEquals(dni_snippet_data.size(), 1, "DNI snippet is retuning more than 1 records");
				   test.log(LogStatus.PASS, "Check DNI snippet api is not retuning more than 1 records");
				   Assert.assertTrue(dni_snippet_data.containsKey(id), "API does not return dni snippet of passed group id.");
				   test.log(LogStatus.PASS, "Check API is returning dni snippet of passed group id."); 
//				   String dni_copy_code = "";
//				   if(id.equals(company_group))
//					   dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord(5fd0b37cd7dbbb00f97ba6ce92bf5add, 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   else if(id.equals(location_group))
//					   dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord(c45147dee729311ef5b5c3003946c48f, 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   Assert.assertEquals(dni_snippet_data.get(id), dni_copy_code, "API is not returning dni copy code as it returns on application.\nDefect Reported: CT-15506");
//				   test.log(LogStatus.PASS, "Check dni snippet is retuning dni snippet as it returns on application.");
			   }
			   else if(id.equals(agency_group) || id.equals(other_billing_group)){
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agency_group)?"agency level":"other billing")+" id is passed using company admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested dni snippet for group id: "+id, "Proper validation message is not displayed when "+(id.equals(agency_group)?"agency level":"other billing") + " group id is passed using company admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agency_group)?"agency level":"other billing")+" group id is passed using company admin access_token.");
			   }
			}
		}
	}	
	
//	@Test(priority=12) 
	public void group_dnisnippet_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("group_dnisnippet_with_location_admin_access_token", "To validate whether user is able to get dnisnippet through group/dnisnippet with location admin access_token");
		test.assignCategory("CFA GET /group/dnisnippet API");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agency_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String company_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String location_group = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		String other_billing_group = DBGroupUtils.getOtherBillingGroupId(agency_group);
		
		String[] group_ids = {agency_group, company_group, location_group, other_billing_group};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/group/dnisnippet", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":(id.equals(location_group)?"location level":"other billing")))+" group is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(location_group)){
				   Assert.assertEquals(result_data, "success", "API returns error when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when location level group id is passed using location admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when location level group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when location level group id is passed using location admin access_token.");
				   JSONArray data_arr = (JSONArray) json.get("data");
				   JSONObject dni_snippet_data = (JSONObject) data_arr.get(0);
				   Assert.assertEquals(dni_snippet_data.size(), 1, "DNI snippet is retuning more than 1 records");
				   test.log(LogStatus.PASS, "Check DNI snippet api is not retuning more than 1 records");
				   Assert.assertTrue(dni_snippet_data.containsKey(id), "API does not return dni snippet of passed group id.");
				   test.log(LogStatus.PASS, "Check API is returning dni snippet of passed group id."); 				   
//				   String dni_copy_code = "<!-- Convirza Script Begins --><script defer src='http://stag.dni02.logmycalls.com/dni.js?app_id=CT' ></script><script type='text/javascript'>function dniLoadingTimer() { if (typeof(getDNIRecord) == 'function'){ getDNIRecord(c45147dee729311ef5b5c3003946c48f, 'stag.dni02.logmycalls.com'); }else{ setTimeout(dniLoadingTimer, 100); }}setTimeout(dniLoadingTimer, 100);</script><!-- Convirza Script Ends -->";
//				   Assert.assertEquals(dni_snippet_data.get(id), dni_copy_code, "API is not returning dni copy code as it returns on application.\nDefect Reported: CT-15506");
//				   test.log(LogStatus.PASS, "Check dni snippet is retuning dni snippet as it returns on application.");
			   }
			   else if(id.equals(agency_group) || id.equals(company_group) || id.equals(other_billing_group)) {
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"other billing level"))+" id is passed using location admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "You are not authorized to view the requested dni snippet for group id: "+id, "Proper validation message is not displayed when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"other billing level")) +" group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agency_group)?"agency level":(id.equals(company_group)?"company level":"other billing level")) + " group id is passed using location admin access_token.");
			   }
			}
		}
	}	
}
