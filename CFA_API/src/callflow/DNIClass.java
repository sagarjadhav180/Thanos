package callflow;

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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.helper.CreateDNIClassOnCallFlow;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class DNIClass extends BaseClass{

	TestDataYamlReader yamlReader = new TestDataYamlReader();
	
  @Test(priority=0)
	public void get_dniclass_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_dniclass_without_access_token", "To validate whether user is able to dniclass through callflow/dniclass api without access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");
		CloseableHttpResponse response = HelperClass.make_get_request_without_authorization("/v2/callflow/dniclass?id=5484", access_token);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
  @Test(priority=1)
	public void get_dniclass_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_dniclass_with_invalid_access_token", "To validate whether user is able to get dniclass through callflow/dniclass with invalid access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", "5484"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", invalid_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
  @Test(priority=2)
	public void get_dniclass_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("get_dniclass_with_expired_access_token", "To validate whether user is able to get dniclass through callflow/dniclass api with expired access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", "5484"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", expired_access_token, list);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 400 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
  @Test(priority=3)
	public void get_dniclass_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_with_valid_access_token", "To validate whether user is able to get dniclass through callflow/dniclass api with valid access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");
		
		Map<String,Object> callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String callFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String dniClass = "lmc_track";		

		new CreateDNIClassOnCallFlow(groupId,callFlowId,dniClass);
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			 System.out.println(line);
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is retuning error when valid access_token is passed.");
		   test.log(LogStatus.PASS, "Check API is retuning success when valid access_token is passed.");
		   Assert.assertNull(json.get("err"), "API is returning validation message when valid access_token is passed");
		   test.log(LogStatus.PASS, "Check API is not returning validation message when valid access_token is passed");
		   JSONObject data_value = (JSONObject)json.get("data");
		   JSONArray classes = (JSONArray) data_value.get("classes");
		   Assert.assertEquals(classes.size(), 1, "API is returning more than 1 dniclass for a callflow.");
		   test.log(LogStatus.PASS, "Check API is not returning more than 1 dniclass for a callflow.");
		   String dni_class = classes.get(0).toString();
		   Assert.assertEquals(dni_class, dniClass, "API is not returning correct dniclass for a callflow.");
		   test.log(LogStatus.PASS, "Check API is returning correct dniclass for a callflow.");   
		}
	}
	
  @Test(priority=4)
	public void get_dniclass_api_without_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_api_without_id", "To validate whether user is able to get dniclass through callflow/dniclass api without id parameter.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
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
			path_data.add("/callflow/dniclass");
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
	public void get_dniclass_api_with_blank_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_api_with_blank_id", "To validate whether user is able to get dniclass through callflow/dniclass api with blank id value.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", ""));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when blank id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String message = json.get("message").toString();
		   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank is value is passed");		
		   JSONArray errors_array = (JSONArray)json.get("errors");
		   JSONObject error_data = (JSONObject) errors_array.get(0);
		   String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank is value is passed");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when blank is value is passed");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when blank is value is passed");
			JSONArray error_path = (JSONArray)error_data.get("path");
			Assert.assertTrue(error_path.isEmpty(),"Check path is not blank.");                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank is value is passed");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank is value is passed");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when blank is value is passed");
			test.log(LogStatus.PASS, "Check whether proper validation message is displayed when blank is value is passed.");
		}   
	}	
	
  @Test(priority=6)
	public void get_dniclass_api_with_invalid_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_api_with_invalid_id", "To validate whether user is able to get dniclass through callflow/dniclass api with invalid id value.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		String[] callflow_ids = {"abc","!#^%#","5848ab","12avc","-5848"};
		for(String id:callflow_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", ""));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when invalid("+id+") id value is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+id+") id value is passed");		
			   JSONArray errors_array = (JSONArray)json.get("errors");
			   JSONObject error_data = (JSONObject) errors_array.get(0);
			   String error_code = error_data.get("code").toString();
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+id+") id value is passed");
			   String in_value = error_data.get("in").toString();
			   Assert.assertEquals(in_value, "query", "Invalid parameter (id): Value is required but was not provided");
			   String message_value = error_data.get("message").toString();
			   Assert.assertEquals(message_value, "Invalid parameter (id): Expected type integer but found type string", "Invalid message value is returned in response when invalid("+id+") id value is passed");
			   String name_value = error_data.get("name").toString();
			   Assert.assertEquals(name_value, "id", "Invalid name value is returned in response when invalid("+id+") id value is passed");
			   JSONArray error_path = (JSONArray)error_data.get("path");
			   Assert.assertTrue(error_path.isEmpty(),"Check path is not blank.");                           
			   JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			   JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			   String sub_error_code = sub_error_data.get("code").toString();
			   Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+id+") id value is passed");
			   String sub_error_message = sub_error_data.get("message").toString();
			   Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+id+") id value is passed");
			   JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			   Assert.assertTrue(sub_error_path.isEmpty(), "path is not blank when invalid("+id+") id value is passed");
			   test.log(LogStatus.PASS, "Check whether proper validation message is displayed when invalid("+id+") id value is passed.");
			}   
		}
	}	
	
  @Test(priority=7)
	public void get_dniclass_api_with_nonexisting_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_api_with_nonexisting_id_value", "To validate whether user is able to get dniclass through callflow/dniclass api with non existing id value.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		String callflow_id = "45454554";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callflow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when non existing callflow id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   System.out.println(line);
		   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when non existing callflow id is passed.");
		   test.log(LogStatus.PASS, "Check valid result value is displayed when non existing callflow id is passed.");
		   Assert.assertEquals(json.get("err").toString(), "No dni setting available for call flow id: "+callflow_id, "Proper validation message is not displayed when non existing callflow id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when non existing callflow id is passed.");
		} 
	}
	
  @Test(priority=8)
	public void get_dniclass_api_with_0_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_api_with_0_id_value", "To validate whether user is able to get dniclass through callflow/dniclass api with 0 id value.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		String callflow_id = "0";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callflow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when 0 callflow id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   System.out.println(line);
		   Assert.assertEquals(json.get("result"), "error", "Invalid result is displayed in response when 0 callflow id is passed.");
		   test.log(LogStatus.PASS, "Check valid result value is displayed when 0 callflow id is passed.");
		   Assert.assertEquals(json.get("err").toString(), "No dni setting available for call flow id: "+callflow_id, "Proper validation message is not displayed when 0 callflow id is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is displayed when 0 callflow id is passed.");
		} 
	}
	
  @Test(priority=9)
	public void get_dniclass_with_valid_id_value() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_with_valid_id_value", "To validate whether user is able to get dniclass through callflow/dniclass api with valid id value");
		test.assignCategory("CFA GET /callflow/dniclass API");
		
		Map<String,Object> callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String callFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();

		String dniClass = "lmc_track";		
		new CreateDNIClassOnCallFlow(groupId,callFlowId,dniClass);
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", callFlowId));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when valid id value is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "success", "API is retuning error when valid id value is passed.");
		   test.log(LogStatus.PASS, "Check API is retuning success when valid id value is passed.");
		   Assert.assertNull(json.get("err"), "API is returning validation message when valid id value is passed");
		   test.log(LogStatus.PASS, "Check API is not returning validation message when valid id value is passed");
		   JSONObject data_value = (JSONObject)json.get("data");
		   JSONArray classes = (JSONArray) data_value.get("classes");
		   Assert.assertEquals(classes.size(), 1, "API is returning more than 1 dniclass for a callflow.");
		   test.log(LogStatus.PASS, "Check API is not returning more than 1 dniclass for a callflow.");
		   String dni_class = classes.get(0).toString();
		   Assert.assertEquals(dni_class, dniClass, "API is not returning correct dniclass for a callflow.");
		   test.log(LogStatus.PASS, "Check API is returning correct dniclass for a callflow.");   
		}
	}
	
  @Test(priority=10)	
	public void get_dniclass_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{	
		test = extent.startTest("get_dniclass_with_agency_admin_access_token", "To validate whether user is able to get dniclass through callflow/dniclass with agency admin access_token");	
		test.assignCategory("CFA GET /callflow/dniclass API");	
			
		Map<String,Object> callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);	
		String groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();	
		String agencyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();	
		String agencyDniClass = "agency_dni_class";			
		new CreateDNIClassOnCallFlow(groupId,agencyCallFlowId,agencyDniClass);	
			
		String otherBillingCallflow = DBCallFlowsUtils.getOtherBillingCallFlowId(groupId);	
					
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);	
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();	
		String companyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();	
		String companyDniClass = "company_dni_class";			
		new CreateDNIClassOnCallFlow(groupId,companyCallFlowId,companyDniClass);	
			
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);	
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();	
		String locationCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();	
		String locationDniClass = "location_dni_class";			
		new CreateDNIClassOnCallFlow(groupId,locationCallFlowId,locationDniClass);	
			
		String[] group_ids = {agencyCallFlowId,companyCallFlowId,locationCallFlowId,otherBillingCallflow};			
		for(String id:group_ids){	
			List<NameValuePair> list = new ArrayList<NameValuePair>();	
			list.add(new BasicNameValuePair("id", id));	
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);	
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());	
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":(id.equals(locationCallFlowId)?"location level":"other billing")))+" group is passed using agency admin access_token.");	
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
			String line = "";	
			while ((line = rd.readLine()) != null) {	
			   // Convert response to JSON object		
			   JSONParser parser = new JSONParser();	
			   JSONObject json = (JSONObject) parser.parse(line);	
			   System.out.println(line);	
			   String result_data = json.get("result").toString();	
			   if(id.equals(agencyCallFlowId) || id.equals(companyCallFlowId) || id.equals(locationCallFlowId)){	
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level"))+" callflow id is passed using agency admin access_token.");	
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level"))+" callflow id is passed using agency admin access_token.");	
				   Object err_data = json.get("err");	
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level"))+" callflow id is passed using agency admin access_token.");	
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level"))+" callflow id is passed using agency admin access_token.");	
				   JSONObject data_value = (JSONObject)json.get("data");	
				   JSONArray classes = (JSONArray) data_value.get("classes");	
				   Assert.assertEquals(classes.size(), 1, "API is returning more than 1 dniclass for a callflow.");	
				   test.log(LogStatus.PASS, "Check API is not returning more than 1 dniclass for a callflow.");	
				   String dni_class = classes.get(0).toString();	
				   if(id.equals(agencyCallFlowId))	
					   Assert.assertEquals(dni_class, "lmc_track", "API is not returning correct dniclass for a callflow.");	
				   else if(id.equals(companyCallFlowId))	
					   Assert.assertEquals(dni_class, "lmc_track", "API is not returning correct dniclass for a callflow.");	
				   else if(id.equals(locationCallFlowId))	
					   Assert.assertEquals(dni_class, "lmc_track", "API is not returning correct dniclass for a callflow.");	
				   test.log(LogStatus.PASS, "API is returning correct dniclass when user passed callflow id of "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level")));	
			   }	
			   else if(id.equals(otherBillingCallflow)){	
				   Assert.assertEquals(result_data, "error", "API returns success when other billing group id is passed using agency admin access_token.");	
				   String err_data = json.get("err").toString();	
				   Assert.assertEquals(err_data, "No access for call flow id: "+id, "Proper validation message is not displayed when other billing group id is passed using agency admin access_token.");	
				   test.log(LogStatus.PASS, "Proper validation message is displayed when other billing group id is passed using agency admin access_token.");	
			   }	
			}	
		}	
	}	
		
//@Test(priority=11)

	public void get_dniclass_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_with_company_admin_access_token", "To validate whether user is able to get dniclass through callflow/dniclass with company admin access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");

		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		Map<String,Object> callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String agencyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String agencyDniClass = "agency_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,agencyCallFlowId,agencyDniClass);
		
		String otherBillingCallflow = DBCallFlowsUtils.getOtherBillingCallFlowId(groupId);
				
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String companyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String companyDniClass = "company_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,companyCallFlowId,companyDniClass);
		
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String locationCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String locationDniClass = "location_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,locationCallFlowId,locationDniClass);
		
		String[] group_ids = {agencyCallFlowId, companyCallFlowId, locationCallFlowId, otherBillingCallflow};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":(id.equals(locationCallFlowId)?"location level":"other billing")))+" group is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   System.out.println(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(companyCallFlowId) || id.equals(locationCallFlowId)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using agency admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using agency admin access_token.");
				   JSONObject data_value = (JSONObject)json.get("data");
				   JSONArray classes = (JSONArray) data_value.get("classes");
				   Assert.assertEquals(classes.size(), 1, "API is returning more than 1 dniclass for a callflow.");
				   test.log(LogStatus.PASS, "Check API is not returning more than 1 dniclass for a callflow.");
				   String dni_class = classes.get(0).toString();
				   if(id.equals(agencyCallFlowId))
					   Assert.assertEquals(dni_class, agencyDniClass, "API is not returning correct dniclass for a callflow.");
				   else if(id.equals(companyCallFlowId))
					   Assert.assertEquals(dni_class, companyDniClass, "API is not returning correct dniclass for a callflow.");
				   else if(id.equals(locationCallFlowId))
					   Assert.assertEquals(dni_class, locationDniClass, "API is not returning correct dniclass for a callflow.");
				   test.log(LogStatus.PASS, "API is returning correct dniclass when user passed callflow id of "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"location level")));
			   }
			   else if(id.equals(agencyCallFlowId) || id.equals(agencyCallFlowId) || id.equals(otherBillingCallflow)){
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agencyCallFlowId)?"agency level":"other billing level")+" group id is passed using agency admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "No access for call flow id: "+id, "Proper validation message is not displayed when "+(id.equals(agencyCallFlowId)?"agency level":"other billing level")+" group id is passed using agency admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agencyCallFlowId)?"agency level":"other billing level")+" group id is passed using agency admin access_token.");
			   }
			}
		}
	}
	
//  @Test(priority=12)
	public void get_dniclass_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_with_location_admin_access_token", "To validate whether user is able to get dniclass through callflow/dniclass with location admin access_token");
		test.assignCategory("CFA GET /callflow/dniclass API");

		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));

		Map<String,Object> callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.AGENCY);
		String groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String agencyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String agencyDniClass = "agency_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,agencyCallFlowId,agencyDniClass);
		
		String otherBillingCallflow = DBCallFlowsUtils.getOtherBillingCallFlowId(groupId);
				
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String companyCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String companyDniClass = "company_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,companyCallFlowId,companyDniClass);
		
		callFlowConfig = yamlReader.readCallflowInfo(Constants.GroupHierarchy.LOCATION);
		groupId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.GROUP_ID).toString();
		String locationCallFlowId = callFlowConfig.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString();
		String locationDniClass = "location_dni_class";		
		new CreateDNIClassOnCallFlow(groupId,locationCallFlowId,locationDniClass);
		
		String[] group_ids = {agencyCallFlowId, companyCallFlowId, locationCallFlowId, otherBillingCallflow};		
		for(String id:group_ids){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check http status code when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":(id.equals(locationCallFlowId)?"location level":"other billing")))+" group is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object	
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   System.out.println(line);
			   String result_data = json.get("result").toString();
			   if(id.equals(locationCallFlowId)){
				   Assert.assertEquals(result_data, "success", "API returns error when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Check API returns success when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using location admin access_token.");
				   Object err_data = json.get("err");
				   Assert.assertEquals(err_data, null, "err data is not null when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "err data is null when "+(id.equals(companyCallFlowId)?"company level":"location level")+" callflow id is passed using location admin access_token.");
				   JSONObject data_value = (JSONObject)json.get("data");
				   JSONArray classes = (JSONArray) data_value.get("classes");
				   Assert.assertEquals(classes.size(), 1, "API is returning more than 1 dniclass for a callflow.");
				   test.log(LogStatus.PASS, "Check API is not returning more than 1 dniclass for a callflow.");
				   String dni_class = classes.get(0).toString();
				   Assert.assertEquals(dni_class, locationDniClass, "API is not returning correct dniclass for a callflow.");
				   test.log(LogStatus.PASS, "API is returning correct dniclass when user passed callflow id of location level");
			   }
			   else if(id.equals(agencyCallFlowId) || id.equals(companyCallFlowId) || id.equals(otherBillingCallflow)){
				   Assert.assertEquals(result_data, "error", "API returns success when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"other billing level"))+" group id is passed using location admin access_token.");
				   String err_data = json.get("err").toString();
				   Assert.assertEquals(err_data, "No access for call flow id: "+id, "Proper validation message is not displayed when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"other billing level"))+" group id is passed using location admin access_token.");
				   test.log(LogStatus.PASS, "Proper validation message is displayed when "+(id.equals(agencyCallFlowId)?"agency level":(id.equals(companyCallFlowId)?"company level":"other billing level"))+" group id is passed using location admin access_token.");
			   }
			}
		}
	}				
	
  @Test(priority=13)
	public void get_dniclass_with_callflow_having_no_dni_setting() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("get_dniclass_with_callflow_having_no_dni_setting", "To validate whether user is able to get dniclass through callflow/dniclass api with callflow id which don't have dni setting.");
		test.assignCategory("CFA GET /callflow/dniclass API");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String callflow_id = "5502";
		list.add(new BasicNameValuePair("id", callflow_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/dniclass", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check http status code when callflow id which don't have dni setting is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
		   // Convert response to JSON object	
		   JSONParser parser = new JSONParser();
		   JSONObject json = (JSONObject) parser.parse(line);
		   String result_data = json.get("result").toString();
		   Assert.assertEquals(result_data, "error", "API is retuning success when callflow id which don't have dni setting is passed.");
		   test.log(LogStatus.PASS, "Check API is retuning error when callflow id which don't have dni setting is passed.");
		   String err_data = json.get("err").toString();
		   Assert.assertEquals(err_data,"No dni setting available for call flow id: "+callflow_id, "Proper validation message is not displayed when callflow id which don't have dni setting is passed.");
		   test.log(LogStatus.PASS, "Check proper validation message is not displayed when callflow id which don't have dni setting is passed.");
		}
	}	
}
