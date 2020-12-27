package misc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.xml.parsers.*;

import com.convirza.constants.Constants;
import com.convirza.tests.core.utils.DBCallUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
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
import com.convirza.tests.core.utils.DBCallUtils;
import com.relevantcodes.extentreports.LogStatus;

import common.AccessGmail;
import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
public class EmailCalldata extends BaseClass{
	String class_name = "EmailCallData";
	ArrayList<String> test_data;

	@Test(priority=1)
	public void email_calldata_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("email_calldata_without_access_token", "To validate whether user is able to email call using email/calldata without access_token");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_without_access_token");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", "", json_obj);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status message when access_token is not passed");
	}
			
	@Test(priority=2)
	public void email_calldata_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("email_calldata_invalid_access_token", "To validate whether user is able to email call using email/calldata with invalid access_token");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_invalid_access_token");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", invalid_access_token, json_obj);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid \"access_token\" is passed");
		test.log(LogStatus.PASS, "Verified http status message when invalid access_token is passed");
	}
		
	@Test(priority=3)
	public void email_calldata_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("email_calldata_with_expired_access_token", "To validate whether user is able to email call using email/calldata with expired access_token");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_expired_access_token");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", expired_access_token, json_obj);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when expired access_token is passed");
		test.log(LogStatus.PASS, "Verified status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired \"access_token\" is passed");
		test.log(LogStatus.PASS, "Verified http status message when expired access_token is passed");
	}
	
	@Test(priority=4)
	public void email_calldata_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_valid_access_token", "To validate whether user is able to email call using email/calldata with valid access_token");
		test.assignCategory("CFA POST /email/calldata API");
		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");
		String email_data = "automation_support@moentek.com";
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("return_type", "xml");
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid \"access_token\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid \"access_token\" is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API does not return any error message when valid \"access_token\" is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct \"email\" is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected \"email\" is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
			test.log(LogStatus.PASS, "Check correct \"from\" data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct \"to\" data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check \"messageId\" is displayed in response.");
			
		}
	}	
		
	@Test(priority=5)
	public void email_calldata_without_any_parameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_without_any_parameter", "To validate whether user is able to email call using email/calldata without any parameter");
		test.assignCategory("CFA POST /email/calldata API");
		JSONObject json_obj = new JSONObject();
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when no parameter is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when no parameters is passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			String[] missing_fields = {"email","call_id"}; 
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when no parameters is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when no parameters is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when no parameters is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when no parameters is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/email/calldata");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			for(int i=0; i<2; i++){
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(i);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameters is passed.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Missing required property: "+missing_fields[i], "Invalid message value is returned in response when no parameters is passed.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when no parameters is passed.");
				test.log(LogStatus.PASS, "Check API returns error when no parameters is passed.");
				test.log(LogStatus.PASS, "Check proper validation is displayed when no parameters is passed.");
			}
		}
	}
	
	@Test(priority=6)
	public void email_calldata_with_blank_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_blank_call_id", "To validate whether user is able to email call using email/calldata with blank call_id.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_blank_call_id");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank \"call_id\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when blank \"call_id\" is passed.");
			String validation = api_response.get("err").toString();
			Assert.assertEquals(validation, "No call ID provided to retrieve records for.", "Proper validation is not displayed when blank \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when blank \"call_id\" is passed.");
		}
	}
	
//	@Test(priority=7) -- Uncomment when defect will be fixed
	public void email_calldata_with_invalid_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_invalid_call_id", "To validate whether user is able to email call using email/calldata with invalid call_id.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_invalid_call_id");
		String[] invalid_call_ids = {"abc","!@$$","abc123","-409976"};
		for(String call_id: invalid_call_ids){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", call_id);
			json_obj.put("return_type", test_data.get(2));
			json_obj.put("email", test_data.get(3));
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+call_id+") \"call_id\" is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when invalid("+call_id+") \"call_id\" is passed.");
				test.log(LogStatus.PASS, "Check API is returning error when invalid("+call_id+") \"call_id\" is passed.");
				String validation = api_response.get("err").toString();
				// Validation need to change
				Assert.assertEquals(validation, "Incorrect call id is passed.", "Proper validation is not displayed when invalid("+call_id+") \"call_id\" is passed. <b style='color:red'>Defect Reported: CT-17585 </b>");
				test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+call_id+") \"call_id\" is passed.");
			}
		}
	}
	
	@Test(priority=8)
	public void email_calldata_without_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_without_call_id", "To validate whether user is able to email call using email/calldata without \"call_id\".");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_without_call_id");
		JSONObject json_obj = new JSONObject();
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when \"call_id\" parameter is not passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when \"call_id\" parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when \"call_id\" parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when \"call_id\" parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when \"call_id\" parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when \"call_id\" parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/email/calldata");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when \"call_id\" parameter is not passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: call_id", "Invalid message value is returned in response when \"call_id\" parameter is not passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when \"call_id\" parameter is not passed.");
			test.log(LogStatus.PASS, "Check API returns error when \"call_id\" parameter is not passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when \"call_id\" parameter is not passed.");
		}
	}	
	
	@Test(priority=9)
	public void email_calldata_with_integer_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_integer_call_id", "To validate whether user is able to email call using email/calldata with integer \"call_id\".");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_integer_call_id");
		int call_id = Integer.parseInt(test_data.get(1));
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", call_id);
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when integer("+call_id+") \"call_id\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when integer("+call_id+") \"call_id\" is passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/email/calldata");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type string but found type integer", "Invalid message value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "call_id", "Invalid path value is displayed when integer("+call_id+") \"call_id\" is passed.");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "The unique identifier for the call data", "Invalid message value is returned in response when integer("+call_id+") \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API returns error when integer \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when integer \"call_id\" is passed.");
		}
	}	
	
	@Test(priority=10)
	public void email_calldata_with_non_existing_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_non_existing_call_id", "To validate whether user is able to email call using email/calldata with non existing \"call_id\".");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_non_existing_call_id");
		String call_id = test_data.get(1);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", call_id);
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing("+call_id+") \"call_id\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when non existing("+call_id+") \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when non existing("+call_id+") \"call_id\" is passed.");
			String validation = api_response.get("err").toString();
			// Validation need to change
			Assert.assertEquals(validation, "Failed to find call record specified", "Proper validation is not displayed when non existing("+call_id+") \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when non existing("+call_id+") \"call_id\" is passed.");
		}
	}	
	

//	@Test(priority=11) -- Uncomment when defect will be fixed
	public void email_calldata_with_mutiple_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_mutiple_call_id", "To validate whether user is able to email call using email/calldata with multiple \"call_id\" with comma seperated.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_mutiple_call_id");
		String call_id = test_data.get(1);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", call_id);
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when multiple("+call_id+") \"call_id\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when multiple("+call_id+") \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when multiple("+call_id+") \"call_id\" is passed.");
			String validation = api_response.get("err").toString();
			// Validation need to change
			Assert.assertEquals(validation, "Incorrect call id is passed.", "Proper validation is not displayed when multiple("+call_id+") \"call_id\" is passed. <b style='color:red'>Defect Reported: CT-17585</b> ");
			test.log(LogStatus.PASS, "Check proper validation is displayed when multiple("+call_id+") \"call_id\" is passed.");
		}
	}
	
	@Test(priority=12)
	public void email_calldata_with_valid_call_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_valid_call_id", "To validate whether user is able to email call using email/calldata with valid \"call_id\"");
		test.assignCategory("CFA POST /email/calldata API");
		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");
		String email_data = "automation_support@moentek.com";
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("return_type", "xml");
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid \"call_id\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when valid \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid \"call_id\" is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when valid \"call_id\" is passed.");
			test.log(LogStatus.PASS, "Check API does not return any success when valid \"call_id\" is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect \"email\" is displayed in response.");
				test.log(LogStatus.PASS, "Check correct \"email\" is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "\"Rejected Email\" list is not empty in response");
			test.log(LogStatus.PASS, "Check \"rejected\" email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect \"from\" data is displayed in response");
			test.log(LogStatus.PASS, "Check correct \"from\" data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect \"email\" is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct \"to\" data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check \"messageId\" is displayed in response.");
		}
	}	
	
	@Test(priority=13)
	public void email_calldata_without_return_type_parameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_without_return_type_parameter", "To validate whether user is able to email call using email/calldata without return_type parameter.");
		test.assignCategory("CFA POST /email/calldata API");
		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");
		String email_data = "automation_support@moentek.com";
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when \"return_type\" parameter is not passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when \"return_type\" parameter is not passed.");
			test.log(LogStatus.PASS, "Check API returns success when \"return_type\" parameter is not passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when \"return_type\" parameter is not passed.");
			test.log(LogStatus.PASS, "Check API does not return any success when \"return_type\" parameter is not passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect \"email\" is displayed in response.");
				test.log(LogStatus.PASS, "Check correct \"email\" is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "\"Rejected\" Email list is not empty in response");
			test.log(LogStatus.PASS, "Check \"rejected\" email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect \"from\" data is displayed in response");
			test.log(LogStatus.PASS, "Check correct \"from\" data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct \"to\" data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check \"messageId\" is displayed in response.");
		}
	}	
	
	@Test(priority=14)
	public void email_calldata_with_blank_return_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_blank_return_type", "To validate whether user is able to email call using email/calldata with blank return_type.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_blank_return_type");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank \"return_type\" is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank \"return_type\" is passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank \"return_type\" is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank \"return_type\" is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when blank \"return_type\" is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when blank \"return_type\" is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/email/calldata");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when blank \"return_type\" is passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: ", "Invalid message value is returned in response when blank \"return_type\" is passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "return_type", "Invalid path value is displayed when blank \"return_type\" is passed.");
			String description = sub_error_data.get("description").toString();
			Assert.assertEquals(description, "Returned data format", "Invalid message value is returned in response when blank \"return_type\" is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when blank return_type parameter is passed.");
		}
	}
	
	@Test(priority=15)
	public void email_calldata_with_invalid_return_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_invalid_return_type", "To validate whether user is able to email call using email/calldata with blank return_type.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_invalid_return_type");
		String[] return_types = {"abc","!@#$","1234","123xs"};
		for(String return_type : return_types){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", test_data.get(1));
			json_obj.put("return_type", return_type);
			json_obj.put("email", test_data.get(3));
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+return_type+") \"return_type\" is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String message = api_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+return_type+") \"return_type\" is passed.");		
				JSONArray errors_array = (JSONArray)api_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/email/calldata");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "No enum match for: "+return_type, "Invalid message value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "return_type", "Invalid path value is displayed when invalid("+return_type+") \"return_type\" is passed.");
				String description = sub_error_data.get("description").toString();
				Assert.assertEquals(description, "Returned data format", "Invalid message value is returned in response when invalid("+return_type+") \"return_type\" is passed.");
				test.log(LogStatus.PASS, "Check API returns success when invalid("+return_type+") \"return_type\" is passed.");
				test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+return_type+") \"return_type\" is passed.");
			}	
		}
	}
	
	//@Test(priority=16)
	public void email_calldata_with_xml_return_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, SAXException, ParserConfigurationException, InterruptedException{
		test = extent.startTest("email_calldata_with_xml_return_type", "To validate whether user is able to email call using email/calldata with \"xml\" return_type.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_xml_return_type");
		String email_data = test_data.get(3);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when \"xml\" return_type is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when \"xml\" return_type is passed.");
			test.log(LogStatus.PASS, "Check API returns success when \"xml\" return_type is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when \"xml\" return_type is passed.");
			test.log(LogStatus.PASS, "Check API does not return any success when \"xml\" return_type is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect \"from\" data is displayed in response");
			test.log(LogStatus.PASS, "Check correct \"from\" data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct \"to\" data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check \"messageId\" is displayed in response.");
			
			// read latest email from gmail account
			Thread.sleep(15000);
			String[] email_data_with_header = AccessGmail.read_latest_email();
			Boolean exception_raised = false;
			try{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				StringBuilder xmlStringBuilder = new StringBuilder();
				xmlStringBuilder.append(email_data_with_header[4]);
				ByteArrayInputStream input =  new ByteArrayInputStream(
				   xmlStringBuilder.toString().getBytes("UTF-8"));
				Document doc = builder.parse(input);
				exception_raised = false;
			}
			catch(Exception e){
				e.printStackTrace();
				exception_raised = true;
				System.err.println("Parsing error occured.");
			}
			Assert.assertFalse(exception_raised, "API does not send data in \"xml\" format.");
			test.log(LogStatus.PASS, "Check API send data in \"xml\" format.");
		}
	}
	
	//@Test(priority=17)
	public void email_calldata_with_json_return_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException, ParserConfigurationException, SAXException, MessagingException, InterruptedException{
		test = extent.startTest("email_calldata_with_json_return_type", "To validate whether user is able to email call using email/calldata with \"json\" return_type.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_json_return_type");
		String email_data = test_data.get(3);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when \"json\" return_type is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when \"json\" return_type is passed.");
			test.log(LogStatus.PASS, "Check API returns success when \"json\" return_type is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when \"json\" return_type is passed.");
			test.log(LogStatus.PASS, "Check API does not return any success when \"json\" return_type is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect \"email\" is displayed in response.");
				test.log(LogStatus.PASS, "Check correct \"email\" is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check \"rejected\" email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect \"from\" data is displayed in response");
			test.log(LogStatus.PASS, "Check correct \"from\" data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect \"email\" is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct \"to\" data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check \"messageId\" is displayed in response.");
			
			Thread.sleep(15000);
	    	String[] email_data_with_header = AccessGmail.read_latest_email();
	    	Boolean exception_raised = false;
	    	try{
				parser.parse(email_data_with_header[4]);
				exception_raised = false;
			}
	    	catch(Exception e){
	    		exception_raised = true;
	    		System.err.println("Parsing error occured.");
	    	}
	    	Assert.assertFalse(exception_raised, "API does not send data in json format. <b style='color:red'>Defect Reported: CT-17586</b> ");
	    	test.log(LogStatus.PASS, "Check API send data in json format.");
		}
	}	
	
	@Test(priority=18)
	public void email_calldata_without_email_parameter() throws IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_without_email_parameter", "To validate whether user is able to email call using email/calldata without passing email parameter.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_without_email_parameter");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when email parameter is not passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when email parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when email parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when email parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (calldata): Value failed JSON Schema validation", "Invalid message value is returned in response when email parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "calldata", "Invalid name value is returned in response when email parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/email/calldata");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when email parameter is not passed.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: email", "Invalid message value is returned in response when email parameter is not passed.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertTrue(sub_error_path.isEmpty(), "Invalid path value is displayed when email parameter is not passed.");
			test.log(LogStatus.PASS, "Check API returns success when email parameter is not passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when email parameter is not passed.");
		}	
	}
	

//	@Test(priority=19) -- will be addressed when call upload api will be automated
	public void email_calldata_with_blank_email() throws IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_blank_email", "To validate whether user is able to email call using email/calldata with blank email value.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_blank_email");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", test_data.get(3));
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank email value is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when blank email is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when blank email is passed.");
			String validation = api_response.get("err").toString();
			Assert.assertEquals(validation, "Email - failed. Call id:  "+test_data.get(1)+" err: Error: No recipients defined", "Proper validation is not displayed when blank email is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when blank email is passed.");
		}	
	}
	
	@Test(priority=20)
	public void email_calldata_with_invalid_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_invalid_email", "To validate whether user is able to email call using email/calldata with invalid email.");
		test.assignCategory("CFA POST /email/calldata API");

		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");

		String[] emails = {"1234","123xs","abc"};
		for(String email : emails){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", DBdata.get("call_id"));
			json_obj.put("return_type", "xml");
			json_obj.put("email", email);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+email+") email is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning success when invalid("+email+") email is passed.");
				test.log(LogStatus.PASS, "Check API is returning error when invalid("+email+") email is passed.");
				String validation = api_response.get("err").toString();
				Assert.assertEquals(validation, "Email - failed. Call id:  "+DBdata.get("call_id")+" err: Error: No recipients defined", "Proper validation is not displayed when invalid("+email+") email is passed.");
				test.log(LogStatus.PASS, "Check proper validation is displayed when invalid("+email+") email is passed.");
			}	
		}
	}
	
	@Test(priority=21)
	public void email_calldata_with_special_char_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_special_char_email", "To validate whether user is able to email call using email/calldata with special characters in email.");
		test.assignCategory("CFA POST /email/calldata API");
		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");

		String email = "!@#$";
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("return_type", "xml");
		json_obj.put("email", email);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when special characters("+email+") email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when special characters("+email+") email is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when special characters("+email+") email is passed.");
			String validation = api_response.get("err").toString();
			Assert.assertEquals(validation, "Email - failed. Call id:  "+DBdata.get("call_id")+" err: Error: Can't send mail - all recipients were rejected: 501 5.1.3 Bad recipient address syntax", "Proper validation is not displayed when special characters("+email+") email is passed.");
			test.log(LogStatus.PASS, "Check proper validation is displayed when special characters("+email+") email is passed.");
		}	
	}
	

//	@Test(priority=22) -- Uncomment when defect will be fixed
	public void email_calldata_with_incorrect_formatted_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_incorrect_formatted_email", "To validate whether user is able to email call using email/calldata with incorrect formatted value in email.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_incorrect_formatted_email");
		String[] incorrect_emails = {"@gmail.com","CT Automation <automation_support@moentek.com>","ctappautomation.gmail.com","email@domain@domain.com",".email@domain.com","email.@domain.com","email..email@domain.com","email@-domain.com","email@111.222.333.44444","email@domain..com"};
		Boolean is_error_occured = false;
		for(String email:incorrect_emails){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", test_data.get(1));
			json_obj.put("return_type", test_data.get(2));
			json_obj.put("email", email);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when incorrect formatted("+email+") email is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result_data = api_response.get("result").toString();
				try{
					Assert.assertEquals(result_data, "error", "API is returning success when incorrect formatted("+email+") email is passed.");
					test.log(LogStatus.PASS, "Check API is returning error when incorrect formatted("+email+") email is passed.");
					String validation = api_response.get("err").toString();
					Assert.assertEquals(validation, "Email - failed. Call id:  "+test_data.get(1)+" err: Error: No recipients defined", "Proper validation is not displayed when special characters("+email+") email is passed.");
					test.log(LogStatus.PASS, "Check proper validation is displayed when incorrect formatted("+email+") email is passed.");
				}
				catch(AssertionError e){
					test.log(LogStatus.FAIL, e.getMessage());
					System.out.println("Assertion failed: when incorrect "+email+ " is passed.");
					is_error_occured = true;
				}
			}	
		}
		Assert.assertFalse(is_error_occured, "Error is occured when incorrect email is passed. <b style='color:red'>Defect Reported: CT-17587</b> ");
	}
	
	@Test(priority=23)
	public void email_calldata_with_non_existing_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_non_existing_email", "To validate whether user is able to email call using email/calldata with non existing email in email.");
		test.assignCategory("CFA POST /email/calldata API");

		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");

		String email = "monetek.com";
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("return_type", "xml");
		json_obj.put("email", email);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing("+email+") email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result_data = api_response.get("result").toString();
			try{
				Assert.assertEquals(result_data, "error", "API is returning success when non existing("+email+") email is passed.");
				test.log(LogStatus.PASS, "Check API is returning error when non existing("+email+") email is passed.");
				String validation = api_response.get("err").toString();
				Assert.assertEquals(validation, "Email - failed. Call id:  "+DBdata.get("call_id")+" err: Error: No recipients defined", "Proper validation is not displayed when non existing("+email+") email is passed.");
				test.log(LogStatus.PASS, "Check proper validation is displayed when non existing("+email+") email is passed.");
			}
			catch(AssertionError e){
				test.log(LogStatus.FAIL, e.getMessage());
				if(result_data.equals("success")){
					JSONObject data = (JSONObject)api_response.get("data");
					JSONArray rejected_emails = (JSONArray) data.get("rejected");
					Assert.assertFalse(rejected_emails.isEmpty(), "Rejected Email list is empty in response when non existing("+email+") email is passed. <b style='color:red'>Defect Reported: CT-17588</b> ");
					Assert.assertEquals(rejected_emails.get(0).toString(), email, "Rejected Email list is empty in response.");
				}
			}			
		}	
	}
	
//	@Test(priority=24)
	public void email_calldata_with_valid_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_valid_email", "To validate whether user is able to email call using email/calldata with valid email");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_valid_email");
		String email_data = test_data.get(3);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when valid email is passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid email is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when valid email is passed.");
			test.log(LogStatus.PASS, "Check API does not return any error validation when valid email is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
			test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check messageId is displayed in response.");	
		}
	}
	
	//@Test(priority=25)
	public void email_calldata_with_multiple_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, InterruptedException{
		test = extent.startTest("email_calldata_with_multiple_email", "To validate whether user is able to email call using email/calldata with multiple email");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_multiple_email");
		String email_data = test_data.get(3);
		String[] emails = email_data.split(",");
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", test_data.get(1));
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when multiple email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when multiple email is passed.");
			test.log(LogStatus.PASS, "Check API returns success when multiple email is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when multiple email is passed.");
			test.log(LogStatus.PASS, "Check API does not return any error validation when multiple email is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, emails[i], "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
			test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, emails[i], "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check messageId is displayed in response.");	
			
			Thread.sleep(15000);
			String[] email_data_with_header = AccessGmail.read_latest_email();
			String to_header = email_data_with_header[1];
			Assert.assertEquals(to_header.replace(" ", ""), email_data, "Email does not send to multiple recipient");
			test.log(LogStatus.PASS, "Check Email is sent to multiple recipient");
		}
	}	
	
	@Test(priority=26)
	public void email_calldata_with_duplicate_email() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("email_calldata_with_duplicate_email", "To validate whether user is able to email call using email/calldata with duplicate email");
		test.assignCategory("CFA POST /email/calldata API");

		Map<String,String> DBdata = DBCallUtils.getModifiedDateById(Constants.GroupHierarchy.AGENCY);
		String access_token = HelperClass.get_oauth_token(DBdata.get("username"),"lmc2demo");

		String email_data = "automation_support@moentek.com";
		String[] emails = {email_data, email_data};
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", DBdata.get("call_id"));
		json_obj.put("return_type", "xml");
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when duplicate email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when duplicate email is passed.");
			test.log(LogStatus.PASS, "Check API returns success when duplicate email is passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when duplicate email is passed.");
			test.log(LogStatus.PASS, "Check API does not return any error validation when duplicate email is passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, emails[i], "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
			test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, emails[i], "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check messageId is displayed in response.");	
		}
	}
	
	//@Test(priority=27)
	public void check_email_triggered_with_call_information() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, ParserConfigurationException, SAXException, java.text.ParseException, InterruptedException{
		test = extent.startTest("check_email_triggered_with_call_information", "To validate whether user is able to email call using email/calldata.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "check_email_triggered_with_call_information");
		String email_data = test_data.get(3), call_id = test_data.get(1);
		JSONObject json_obj = new JSONObject();
		json_obj.put("call_id", call_id);
		json_obj.put("return_type", test_data.get(2));
		json_obj.put("email", email_data);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid parameters are passed.");
		
		// Get the current data and time on which email/calldata API is send 
		SimpleDateFormat output_format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
		Date current_date = output_format.parse(output_format.format(new Date()));
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);	
			String result = api_response.get("result").toString();
			Assert.assertEquals(result, "success", "API returns error when valid parameters are passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid parameters are passed.");
			Assert.assertNull(api_response.get("err"), "API returns err data when valid parameters are passed.");
			test.log(LogStatus.PASS, "Check API does not return any error validation when valid parameters are passed.");
			JSONObject data = (JSONObject)api_response.get("data");
			JSONArray accepted_emails = (JSONArray) data.get("accepted");
			for(int i=0; i<accepted_emails.size(); i++){
				String mail = accepted_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
				test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
			}
			JSONArray rejected_emails = (JSONArray) data.get("rejected");
			Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
			test.log(LogStatus.PASS, "Check rejected email is empty in response.");
			HelperClass.multiple_assertnotEquals(data.get("response"), "response");
			JSONObject envelope = (JSONObject) data.get("envelope");
			String from_data = envelope.get("from").toString();
			Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
			test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
			JSONArray to_emails = (JSONArray) envelope.get("to");
			for(int i=0; i<to_emails.size(); i++){
				String mail = to_emails.get(i).toString();
				Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
			}
			test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
			HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
			test.log(LogStatus.PASS, "Check messageId is displayed in response.");
			
			Thread.sleep(15000);
			String[] email_data_with_header = AccessGmail.read_latest_email();
			
			// Verify headers(To, From, Subject) in triggered email
			String from_header = email_data_with_header[0], to_header = email_data_with_header[1], email_subject = email_data_with_header[2], email_date = email_data_with_header[3];		
			Assert.assertEquals(from_header, "no-reply@messages.services", "Incorrect email sender is displayed in triggered email.");
			test.log(LogStatus.PASS, "Check correct \"sender\" email is displayed in trigerred email.");
			Assert.assertEquals(to_header, email_data, "Incorrect email receiver is displayed in triggered email.");
			test.log(LogStatus.PASS, "Check correct \"receiver\" email is displayed in trigerred email.");
			Assert.assertEquals(email_subject, "ADF Lead", "Incorrect email subject is displayed in triggered email.");
			test.log(LogStatus.PASS, "Check correct \"email subject\" is displayed in trigerred email.");
			
			// Verify call information(Id, Audio file) in triggered email
			String[] call_info = AccessGmail.retrieve_call_info(email_data_with_header[4]);
			String call_id_in_email = call_info[0], tracking_num_in_email = call_info[1], audio_in_response = call_info[2], duration_in_email = call_info[3], disposition_in_email = call_info[4], repeat_call_in_email = call_info[5];
			Assert.assertEquals(call_id_in_email, test_data.get(1), "Incorrect call_id is displayed in email");
			test.log(LogStatus.PASS, "Check correct \"call_id\" is displayed in trigerred email.");
			HelperClass.multiple_assertnotEquals(audio_in_response, "audio in email");
			test.log(LogStatus.PASS, "Check \"Audio\" is displayed in trigerred email.");
			
			// Get the call from GET call?id API
			String[] call_log = getCallDataFromGetCallAPI(call_id);
			String tracking_num = call_log[0], duration = call_log[1], repeat_call = call_log[2], disposition = call_log[3];
			
			// Verify call information(duration, tracking_number, repeat_call, disposition) in triggered email
			Assert.assertEquals(tracking_num_in_email, tracking_num, "Incorrect tracking number is displayed in email");
			test.log(LogStatus.PASS, "Check correct \"tracking_number\" is displayed in trigerred email.");
			Assert.assertEquals(duration_in_email, duration, "Incorrect duration is displayed in email");
			test.log(LogStatus.PASS, "Check correct \"duration\" is displayed in trigerred email.");
			Assert.assertTrue(repeat_call_in_email.contains(repeat_call), "Incorrect repeat_call is displayed in email");
			test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");
			Assert.assertTrue(disposition_in_email.contains(disposition), "Incorrect repeat_call is displayed in email");
			test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");
			
			  /* Get the expiration time from Audio link of call trigerred in email
			   Convert the email triggered date and audio link time in IST
			   Get the time difference between Audio link of call and email triggered date
			   Compare with s3_exp_hr of group*/
			
			String expiration_time_in_epoch = audio_in_response.substring(audio_in_response.indexOf("Expires")+8, audio_in_response.indexOf("Expires")+18);
			Date date = new Date(Long.parseLong(expiration_time_in_epoch)*1000l);
			output_format.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date expiration_time = output_format.parse(output_format.format(date));
			System.out.println("Expiration Time: "+expiration_time);
			Date email_triggered_date = output_format.parse(email_date);
			System.out.println("Email Triggered Date: "+email_triggered_date);
			test.log(LogStatus.INFO, "Email Time: "+email_triggered_date);
			test.log(LogStatus.INFO, "Expiration Time of Audio link: "+expiration_time);
			long time_diff_in_hour = (expiration_time.getTime() - email_triggered_date.getTime())/(1000l*3600l);
			Assert.assertEquals(String.valueOf(time_diff_in_hour), test_data.get(4), "Expiration Time of the audio is not as per s3_exp_hr defined of that group.");
			test.log(LogStatus.PASS, "Expiration Time of the audio is as per s3_exp_hr defined of that group.");
			
			System.out.println("Time taken to recieve email: "+(current_date.getTime()-email_triggered_date.getTime())/1000l + "sec.");
			Assert.assertEquals((current_date.getTime()-email_triggered_date.getTime())/1000l, 0l, "Email triggered time is delayed.");
		}
	}
	
	
	//@Test(priority=28)
	public void email_calldata_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, ParserConfigurationException, SAXException, java.text.ParseException, InterruptedException{
		test = extent.startTest("email_calldata_with_agency_admin_access_token", "To validate whether user is able to email call using email/calldata with agency admin access_token.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_agency_admin_access_token");
		String email_data = test_data.get(3); 
		String[] calls = test_data.get(1).split(",");
		String agency_level_call_id = calls[0], company_level_call_id = calls[1], location_level_call_id = calls[2], other_billing_call_id = calls[3];
		for(String call_id:calls){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", call_id);
			json_obj.put("return_type", test_data.get(2));
			json_obj.put("email", email_data);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result = api_response.get("result").toString();
				if(call_id.equals(agency_level_call_id) || call_id.equals(company_level_call_id) || call_id.equals(location_level_call_id)){
					Assert.assertEquals(result, "success", "API returns error when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					test.log(LogStatus.PASS, "Check API returns success when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					Assert.assertNull(api_response.get("err"), "API returns err data when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					test.log(LogStatus.PASS, "Check API does not return any error validation when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					JSONObject data = (JSONObject)api_response.get("data");
					JSONArray accepted_emails = (JSONArray) data.get("accepted");
					for(int i=0; i<accepted_emails.size(); i++){
						String mail = accepted_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
						test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
					}
					JSONArray rejected_emails = (JSONArray) data.get("rejected");
					Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
					test.log(LogStatus.PASS, "Check rejected email is empty in response.");
					HelperClass.multiple_assertnotEquals(data.get("response"), "response");
					JSONObject envelope = (JSONObject) data.get("envelope");
					String from_data = envelope.get("from").toString();
					Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
					test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
					JSONArray to_emails = (JSONArray) envelope.get("to");
					for(int i=0; i<to_emails.size(); i++){
						String mail = to_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
					}
					test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
					HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
					test.log(LogStatus.PASS, "Check messageId is displayed in response.");
					
					Thread.sleep(20000);
					String[] email_data_with_header = AccessGmail.read_latest_email();
					
					// Verify headers(To, From, Subject) in triggered email
					String from_header = email_data_with_header[0], to_header = email_data_with_header[1], email_subject = email_data_with_header[2], email_date = email_data_with_header[3];		
					Assert.assertEquals(from_header, "no-reply@messages.services", "Incorrect email sender is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"sender\" email is displayed in trigerred email.");
					Assert.assertEquals(to_header, email_data, "Incorrect email receiver is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"receiver\" email is displayed in trigerred email.");
					Assert.assertEquals(email_subject, "ADF Lead", "Incorrect email subject is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"email subject\" is displayed in trigerred email.");
					
					// Verify call information(Id, Audio file) in triggered email
					String[] call_info = AccessGmail.retrieve_call_info(email_data_with_header[4]);
					String call_id_in_email = call_info[0], tracking_num_in_email = call_info[1], audio_in_response = call_info[2], duration_in_email = call_info[3], disposition_in_email = call_info[4], repeat_call_in_email = call_info[5];
					Assert.assertEquals(call_id_in_email, call_id, "Incorrect call_id is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"call_id\" is displayed in trigerred email.");
					HelperClass.multiple_assertnotEquals(audio_in_response, "audio in email");
					test.log(LogStatus.PASS, "Check \"Audio\" is displayed in trigerred email.");
					
					// Get the call from GET call?id API
					String[] call_log = getCallDataFromGetCallAPI(call_id);
					String tracking_num = call_log[0], duration = call_log[1], repeat_call = call_log[2], disposition = call_log[3];
					
					// Verify call information(duration, tracking_number, repeat_call, disposition) in triggered email
					Assert.assertEquals(tracking_num_in_email, tracking_num, "Incorrect tracking number is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"tracking_number\" is displayed in trigerred email.");
					Assert.assertEquals(duration_in_email, duration, "Incorrect duration is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"duration\" is displayed in trigerred email.");
					Assert.assertTrue(repeat_call_in_email.contains(repeat_call), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");
					Assert.assertTrue(disposition_in_email.contains(disposition), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");					
				}
				else{
					Assert.assertEquals(result, "error", "API returns error when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					test.log(LogStatus.PASS, "Check API returns error when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					Assert.assertEquals(api_response.get("err").toString(), "User is not authorized to view a call from this group", "API returns err data when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
					test.log(LogStatus.PASS, "Check validation is displayed when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using agency admin access_token.");
				}
			}
		}
	}	
	
	//@Test(priority=29)
	public void email_calldata_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, ParserConfigurationException, SAXException, java.text.ParseException, InterruptedException{
		test = extent.startTest("email_calldata_with_company_admin_access_token", "To validate whether user is able to email call using email/calldata with company admin access_token.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_company_admin_access_token");
		
		// Get access_token of company admin
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("company_admin_email");
		get_credential.add("company_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String email_data = test_data.get(3); 
		String[] calls = test_data.get(1).split(",");
		String agency_level_call_id = calls[0], company_level_call_id = calls[1], location_level_call_id = calls[2], other_billing_call_id = calls[3];
		for(String call_id:calls){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", call_id);
			json_obj.put("return_type", test_data.get(2));
			json_obj.put("email", email_data);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result = api_response.get("result").toString();
				if(call_id.equals(company_level_call_id) || call_id.equals(location_level_call_id)){
					Assert.assertEquals(result, "success", "API returns error when "+(call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check API returns success when "+(call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					Assert.assertNull(api_response.get("err"), "API returns err data when "+(call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check API does not return any error validation when "+(call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					JSONObject data = (JSONObject)api_response.get("data");
					JSONArray accepted_emails = (JSONArray) data.get("accepted");
					for(int i=0; i<accepted_emails.size(); i++){
						String mail = accepted_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
						test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
					}
					JSONArray rejected_emails = (JSONArray) data.get("rejected");
					Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
					test.log(LogStatus.PASS, "Check rejected email is empty in response.");
					HelperClass.multiple_assertnotEquals(data.get("response"), "response");
					JSONObject envelope = (JSONObject) data.get("envelope");
					String from_data = envelope.get("from").toString();
					Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
					test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
					JSONArray to_emails = (JSONArray) envelope.get("to");
					for(int i=0; i<to_emails.size(); i++){
						String mail = to_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
					}
					test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
					HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
					test.log(LogStatus.PASS, "Check messageId is displayed in response.");
					
					Thread.sleep(15000);
					String[] email_data_with_header = AccessGmail.read_latest_email();
					
					// Verify headers(To, From, Subject) in triggered email
					String from_header = email_data_with_header[0], to_header = email_data_with_header[1], email_subject = email_data_with_header[2], email_date = email_data_with_header[3];		
					Assert.assertEquals(from_header, "no-reply@messages.services", "Incorrect email sender is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"sender\" email is displayed in trigerred email.");
					Assert.assertEquals(to_header, email_data, "Incorrect email receiver is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"receiver\" email is displayed in trigerred email.");
					Assert.assertEquals(email_subject, "ADF Lead", "Incorrect email subject is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"email subject\" is displayed in trigerred email.");
					
					// Verify call information(Id, Audio file) in triggered email
					String[] call_info = AccessGmail.retrieve_call_info(email_data_with_header[4]);
					String call_id_in_email = call_info[0], tracking_num_in_email = call_info[1], audio_in_response = call_info[2], duration_in_email = call_info[3], disposition_in_email = call_info[4], repeat_call_in_email = call_info[5];
					Assert.assertEquals(call_id_in_email, call_id, "Incorrect call_id is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"call_id\" is displayed in trigerred email.");
					HelperClass.multiple_assertnotEquals(audio_in_response, "audio in email");
					test.log(LogStatus.PASS, "Check \"Audio\" is displayed in trigerred email.");
					
					// Get the call from GET call?id API
					String[] call_log = getCallDataFromGetCallAPI(call_id);
					String tracking_num = call_log[0], duration = call_log[1], repeat_call = call_log[2], disposition = call_log[3];
					
					// Verify call information(duration, tracking_number, repeat_call, disposition) in triggered email
					Assert.assertEquals(tracking_num_in_email, tracking_num, "Incorrect tracking number is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"tracking_number\" is displayed in trigerred email.");
					Assert.assertEquals(duration_in_email, duration, "Incorrect duration is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"duration\" is displayed in trigerred email.");
					Assert.assertTrue(repeat_call_in_email.contains(repeat_call), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");
					Assert.assertTrue(disposition_in_email.contains(disposition), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");					
				}
				else{
					Assert.assertEquals(result, "error", "API returns success when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check API returns error when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					Assert.assertEquals(api_response.get("err").toString(), "User is not authorized to view a call from this group", "API returns err data when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check validation is displayed when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using company admin access_token.");
				}
			}
		}
	}	
	
	//@Test(priority=30)
	public void email_calldata_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException, MessagingException, ParserConfigurationException, SAXException, java.text.ParseException, InterruptedException{
		test = extent.startTest("email_calldata_with_location_admin_access_token", "To validate whether user is able to email call using email/calldata with location admin access_token.");
		test.assignCategory("CFA POST /email/calldata API");
		test_data = HelperClass.readTestData(class_name, "email_calldata_with_location_admin_access_token");
		
		// Get access_token of location admin user
		ArrayList<String> get_credential = new ArrayList<String>();
		get_credential.add("location_admin_email");
		get_credential.add("location_admin_password");
		ArrayList<String> config = HelperClass.read_config(get_credential);
		String access_token = HelperClass.get_oauth_token(config.get(0),config.get(1));
		
		String email_data = test_data.get(3); 
		String[] calls = test_data.get(1).split(",");
		String agency_level_call_id = calls[0], company_level_call_id = calls[1], location_level_call_id = calls[2], other_billing_call_id = calls[3];
		for(String call_id:calls){
			JSONObject json_obj = new JSONObject();
			json_obj.put("call_id", call_id);
			json_obj.put("return_type", test_data.get(2));
			json_obj.put("email", email_data);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/email/calldata", access_token, json_obj);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using location admin access_token.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);	
				String result = api_response.get("result").toString();
				if(call_id.equals(location_level_call_id)){
					Assert.assertEquals(result, "success", "API returns error when location level call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check API returns success when location level call id is passed using company admin access_token.");
					Assert.assertNull(api_response.get("err"), "API returns err data when location level call id is passed using company admin access_token.");
					test.log(LogStatus.PASS, "Check API does not return any error validation when location level call id is passed using company admin access_token.");
					JSONObject data = (JSONObject)api_response.get("data");
					JSONArray accepted_emails = (JSONArray) data.get("accepted");
					for(int i=0; i<accepted_emails.size(); i++){
						String mail = accepted_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
						test.log(LogStatus.PASS, "Check correct email is displayed under accepted email in response.");
					}
					JSONArray rejected_emails = (JSONArray) data.get("rejected");
					Assert.assertTrue(rejected_emails.isEmpty(), "Rejected Email list is not empty in response");
					test.log(LogStatus.PASS, "Check rejected email is empty in response.");
					HelperClass.multiple_assertnotEquals(data.get("response"), "response");
					JSONObject envelope = (JSONObject) data.get("envelope");
					String from_data = envelope.get("from").toString();
					Assert.assertEquals(from_data, "no-reply@messages.services", "Incorrect from data is displayed in response");
					test.log(LogStatus.PASS, "Check correct from data is displayed in response.");
					JSONArray to_emails = (JSONArray) envelope.get("to");
					for(int i=0; i<to_emails.size(); i++){
						String mail = to_emails.get(i).toString();
						Assert.assertEquals(mail, email_data, "Incorrect email is displayed in response.");
					}
					test.log(LogStatus.PASS, "Check correct to data is displayed in response.");
					HelperClass.multiple_assertnotEquals(data.get("messageId"), "messageId");
					test.log(LogStatus.PASS, "Check messageId is displayed in response.");
					
					Thread.sleep(15000);
					String[] email_data_with_header = AccessGmail.read_latest_email();
					
					// Verify headers(To, From, Subject) in triggered email
					String from_header = email_data_with_header[0], to_header = email_data_with_header[1], email_subject = email_data_with_header[2], email_date = email_data_with_header[3];		
					Assert.assertEquals(from_header, "no-reply@messages.services", "Incorrect email sender is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"sender\" email is displayed in trigerred email.");
					Assert.assertEquals(to_header, email_data, "Incorrect email receiver is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"receiver\" email is displayed in trigerred email.");
					Assert.assertEquals(email_subject, "ADF Lead", "Incorrect email subject is displayed in triggered email.");
					test.log(LogStatus.PASS, "Check correct \"email subject\" is displayed in trigerred email.");
					
					// Verify call information(Id, Audio file) in triggered email
					String[] call_info = AccessGmail.retrieve_call_info(email_data_with_header[4]);
					String call_id_in_email = call_info[0], tracking_num_in_email = call_info[1], audio_in_response = call_info[2], duration_in_email = call_info[3], disposition_in_email = call_info[4], repeat_call_in_email = call_info[5];
					Assert.assertEquals(call_id_in_email, call_id, "Incorrect call_id is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"call_id\" is displayed in trigerred email.");
					HelperClass.multiple_assertnotEquals(audio_in_response, "audio in email");
					test.log(LogStatus.PASS, "Check \"Audio\" is displayed in trigerred email.");
					
					// Get the call from GET call?id API
					String[] call_log = getCallDataFromGetCallAPI(call_id);
					String tracking_num = call_log[0], duration = call_log[1], repeat_call = call_log[2], disposition = call_log[3];
					
					// Verify call information(duration, tracking_number, repeat_call, disposition) in triggered email
					Assert.assertEquals(tracking_num_in_email, tracking_num, "Incorrect tracking number is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"tracking_number\" is displayed in trigerred email.");
					Assert.assertEquals(duration_in_email, duration, "Incorrect duration is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"duration\" is displayed in trigerred email.");
					Assert.assertTrue(repeat_call_in_email.contains(repeat_call), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");
					Assert.assertTrue(disposition_in_email.contains(disposition), "Incorrect repeat_call is displayed in email");
					test.log(LogStatus.PASS, "Check correct \"repeat_call\" is displayed in trigerred email.");					
				}
				else{
					Assert.assertEquals(result, "error", "API returns success when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using location admin access_token.");
					test.log(LogStatus.PASS, "Check API returns error when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using location admin access_token.");
					Assert.assertEquals(api_response.get("err").toString(), "User is not authorized to view a call from this group", "API returns err data when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using location admin access_token.");
					test.log(LogStatus.PASS, "Check validation is displayed when "+(call_id.equals(agency_level_call_id)?"agency level":call_id.equals(company_level_call_id)?"company level":call_id.equals(location_level_call_id)?"location level":"other billing")+" call id is passed using location admin access_token.");
				}
			}
		}
	}	
	
	// Get call API to get call info 
	public String[] getCallDataFromGetCallAPI(String call_id) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", call_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/call", access_token, list);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid email is passed.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		String[] call_log = new String[4];
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response = (JSONObject)parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "Get Call API returns error when valid parameters are passed.");
			JSONObject call_data = (JSONObject)api_response.get("data");
			String tracking_number = call_data.get("tracking_number").toString();
			String duration = call_data.get("duration").toString();
			String repeat_call = call_data.get("repeat_call").toString();
			String disposition = call_data.get("disposition").toString();	
			call_log[0] = tracking_number;
			call_log[1] = duration;
			call_log[2] = repeat_call;
			call_log[3] = disposition;
		}
		return call_log;
	}
}
