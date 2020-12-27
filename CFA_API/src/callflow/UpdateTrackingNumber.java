package callflow;

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
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.utils.TNUtil;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;

public class UpdateTrackingNumber extends BaseClass{
	
	String class_name = "PostTrackingNumber";
	ArrayList<String> test_data;
	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	

	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException    {
		
//		Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
//		String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
//		String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
//		access_token_company_admin=response;
			
//		Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
//		String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
			
//		String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
//		access_token_location_admin=response1;
		
	}

	
//	@Test(priority=1)
	public void updateTrackingNumberForSimpleRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("updateTrackingNumberForSimpleRoute", "To validate whether agency level admin user is able to update callflow of route type Simple");
		test.assignCategory("CFA UPDATE /callflow API");
		
		Integer call_flow_id;
		
		//fetching call_flow_id
		Map<String, Object> template_data = yamlReader.readCallflowTemplete("simple");
//		String cf = template_data.get("call_flow_name").toString();
		JSONObject obj = getCallFlowData(template_data.get("call_flow_name").toString());
		call_flow_id = Integer.parseInt(obj.get("call_flow_id").toString());	
		//updating call flow
//		HelperClass.readTestData(class_name, "UpdateTrackingNumberForSimpleRoute");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"simple",1, "unique", "update,"+call_flow_id+"");
		CloseableHttpResponse response1;
		response1 = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";	
		while ((line1 = rd1.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line1);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line1);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
		
	}
	
	
//	@Test(priority=2)
	public void updateTrackingNumberForNumberPoolRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("updateTrackingNumberForNumberPoolRoute", "To validate whether agency level admin user is able to update callflow of route type Number pool");
		test.assignCategory("CFA UPDATE /callflow API");
		
		Integer call_flow_id;
		
		//fetching call_flow_id
		Map<String, Object> template_data = yamlReader.readCallflowTemplete("pool");
//		String cf = template_data.get("call_flow_name").toString();
		JSONObject obj = getCallFlowData(template_data.get("call_flow_name").toString());
		call_flow_id = Integer.parseInt(obj.get("call_flow_id").toString());	
		//updating call flow
//		HelperClass.readTestData(class_name, "UpdateTrackingNumberForSimpleRoute");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"pool",1, "unique", "update,"+call_flow_id+"");
		CloseableHttpResponse response1;
		response1 = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";	
		while ((line1 = rd1.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line1);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line1);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
		
	}
	
	
	@Test(priority=3)
	public void updateTrackingNumberForIvrRoute() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("updateTrackingNumberForIvrRoute", "To validate whether agency level admin user is able to update callflow of route type IVR");
		test.assignCategory("CFA UPDATE /callflow API");
		
		Integer call_flow_id;
		
		//fetching call_flow_id
		Map<String, Object> template_data = yamlReader.readCallflowTemplete("ivr");
//		String cf = template_data.get("call_flow_name").toString();
		JSONObject obj = getCallFlowData(template_data.get("call_flow_name").toString());
		call_flow_id = Integer.parseInt(obj.get("call_flow_id").toString());	
		//updating call flow
//		HelperClass.readTestData(class_name, "UpdateTrackingNumberForSimpleRoute");
		
		JSONArray callflowReq = TNUtil.createCallflow(Constants.GroupHierarchy.AGENCY,"ivr",1, "unique", "update,"+call_flow_id+"");
		CloseableHttpResponse response1;
		response1 = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status Code Code: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";	
		while ((line1 = rd1.readLine()) != null) {
			 JSONParser parser = new JSONParser();
			   JSONObject jsonResponse = (JSONObject) parser.parse(line1);
			   String result_data = jsonResponse.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success is returned in result");
			   Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line1);
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(jsonResponse.get("err"),"API did not return null error.");
		}	
		
	}
	
	public JSONObject getCallFlowData(String call_flow_name) throws UnsupportedOperationException, IOException, URISyntaxException, ParseException {
		
		JSONObject data_obj = null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","call_flow_name%3d"+call_flow_name));
		nvps.add(new BasicNameValuePair("limit","1"));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/callflow/list", access_token, nvps);
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
			   data_obj = (JSONObject) data_arr.get(0);
			   
		}
		return data_obj;
	}
	
	
}
