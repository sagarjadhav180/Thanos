package callflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@Listeners(Listener.class)
@SuppressWarnings("unchecked")
public class PostCallFlowDNI extends BaseClass{

	@Test(priority=0)
	public void post_dni_without_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_dni_without_access_token", "To validate whether user is create dni setting through post callflow/dni api without access_token");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", 8);
		json_obj.put("custom_cookie", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", "", dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when access_token is not passed");
		test.log(LogStatus.PASS, "Verified status code when access_token is not passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when access_token is not passed");
		test.log(LogStatus.PASS, "Verified http status code when access_token is not passed");
	}
		
	@Test(priority=1)
	public void post_dni_with_invalid_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_dni_with_invalid_access_token", "To validate whether user is able to create dni setting with invalid access_token");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", 8);
		json_obj.put("custom_cookie", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", invalid_access_token, dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check status code when invalid access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when invalid access_token is passed");
	}
	
	@Test(priority=2)
	public void post_dni_with_expired_access_token() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("post_dni_with_expired_access_token", "To validate whether user is able to create dni setting through post callflow/dni api with expired access_token");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", 8);
		json_obj.put("custom_cookie", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", expired_access_token, dni_array);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when expired access_token is passed");
		test.log(LogStatus.PASS, "Check status code when expired access_token is passed");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when expired access_token is passed");
		test.log(LogStatus.PASS, "Check http status code when expired access_token is passed");
	}
	
	@Test(priority=3)
	public void post_dni_with_valid_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_access_token", "To validate whether user is able to create dni setting through post callflow/dni api with valid access_token");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", 8);
		json_obj.put("custom_cookie", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid access_token is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			System.out.println(json_response);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid access_token is passed.");
			Assert.assertNull(json_response.get("err"), "API is returning validation when valid access_token is passed.");
			test.log(LogStatus.PASS, "Check API is not returning any validation when valid access_token is passed.");
			JSONArray data_array = (JSONArray)json_response.get("data");
			JSONObject data_value = (JSONObject)data_array.get(0);
			result_data = data_value.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
			Assert.assertEquals(data_value.get("entry_count").toString(), "1", "API is returning incorrect entry_count when valid access_token is passed.");
		}
	}
	
	@Test(priority=4) //Need to add validation
	public void post_dni_without_any_parameter() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_any_parameter", "To validate whether user is able to create dni setting through post callflow/dni api without any parameter");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when no parameter is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "error", "API is returning success when no parameters is passed. Defect Reported: CT-17169");
			test.log(LogStatus.PASS, "Check API is returning error when no parameters is passed.");
		}
	}
	
	@Test(priority=5)
	public void post_dni_without_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_group_id", "To validate whether user is able to create dni setting through post callflow/dni api without group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when group_id parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when group_id parameter is not passed in post dni api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when group_id parameter is not passed in post dni api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when group_id parameter is not passed in post dni api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when group_id parameter is not passed in post dni api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when group_id parameter is not passed in post dni api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when no parameter is passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: group_id", "Invalid message value is returned in response when no parameter is passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when no parameter is passed with callflow/dni post api method.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
		}
	}
	
	@Test(priority=6)
	public void post_dni_with_blank_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_group_id", "To validate whether user is able to create dni setting through post callflow/dni api with blank group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", "");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank group_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			String message = json_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank group_id value is passed in post dni api.");		
			JSONArray errors_array = (JSONArray)json_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank group_id value is passed in post dni api.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank group_id value is passed in post dni api.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when blank group_id value is passed in post dni api.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when blank group_id value is passed in post dni api.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank group_id is passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank group_id is passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank group_id is passed with callflow/dni post api method.");
//			Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
		}
	}	
	
	@Test(priority=7)
	public void post_dni_with_invalid_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_group_id", "To validate whether user is able to create dni setting through post callflow/dni api with invalid group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] group_ids = {"abc","!@$^","8abc","-8"};
		for(String group_id:group_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", group_id);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", "post_source_dni");
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+group_id+") group_id is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json_response =(JSONObject) parser.parse(line);
				String message = json_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+group_id+") group_id value is passed in post dni api.");		
				JSONArray errors_array = (JSONArray)json_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+group_id+") group_id value is passed in post dni api.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+group_id+") group_id value is passed in post dni api.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+group_id+") group_id value is passed in post dni api.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when invalid("+group_id+") group_id value is passed in post dni api.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/dni");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+group_id+") group_id is passed with callflow/dni post api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+group_id+") group_id is passed with callflow/dni post api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+group_id+") group_id is passed with callflow/dni post api method.");
//				Assert.assertFalse(sub_error_data.get("description")==null, "Description field is not present in reponse.");
			}
		}
	}	
	
	@Test(priority=8)
	public void post_dni_with_non_existing_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_non_existing_group_id", "To validate whether user is able to create dni setting through post callflow/dni api with non existing group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		int group_id = 324521;
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", group_id);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "post_source_dni");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non existing group_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when non existing group_id is passed");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when non existing group_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when non existing group_id is passed");
			Assert.assertEquals(error_response.get("data").toString(), "Not authorized to make changes for Group ID "+group_id, "Incorrect validation is returning when non existing group_id is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when non existing group_id is passed");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when non existing group_id is passed.");
			test.log(LogStatus.PASS, "Check incrrct entry count is displayed when non existing group_id is passed");
		}
	}
	
	@Test(priority=9)
	public void post_dni_with_valid_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_group_id", "To validate whether user is able to create dni setting through post callflow/dni api with valid group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		int group_id = 8;
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", group_id);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid group_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid group_id is passed");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when valid group_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid group_id is passed");
			Assert.assertFalse(error_response.containsKey("data"));
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid group_id is passed.");
			test.log(LogStatus.PASS, "Check incrrct entry count is displayed when valid group_id is passed");
		}
	}	
	
	@Test(priority=10)
	public void post_dni_with_unauthorised_group_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_unauthorised_group_id", "To validate whether user is able to create dni setting through post callflow/dni api with unauthorised group_id");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		int group_id = 76;
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", group_id);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when unauthorised group_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when unauthorised group_id is passed");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when unauthorised group_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when valid group_id is passed");
			Assert.assertEquals(error_response.get("data").toString(), "Not authorized to make changes for Group ID "+group_id, "Incorrect validation is returning when unauthorised group_id is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when unauthorised group_id is passed");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when unauthorised group_id is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when unauthorised group_id is passed");
		}
	}	
	
	@Test(priority=11)
	public void post_dni_with_group_id_nonmatching_callflow_group() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_group_id_nonmatching_callflow_group", "To validate whether user is able to create dni setting through post callflow/dni api with group_id where passed callflow does not belong.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		int group_id = 114;
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", group_id);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when non matching group_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when non matching group_id is passed. Defect Reported: CT-17172");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when non matching group_id is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when non matching group_id is passed");
			Assert.assertEquals(error_response.get("data").toString(), "Invalid group_id. Callflow belonging to another group", "Incorrect validation is returning when non matching group_id is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when non matching group_id is passed");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when non matching group_id is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when non matching group_id is passed");
		}
	}		
	
	@Test(priority=12)
	public void post_dni_without_custom_cookie() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_custom_cookie", "To validate whether user is able to create dni setting through post callflow/dni api without passing custom_cookie.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when custom_cookie parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when custom_cookie parameter is not passed");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when custom_cookie parameter is not passed.");
			test.log(LogStatus.PASS, "Check API is returning error when custom_cookie parameter is not passed");
			Assert.assertEquals(error_response.get("data").toString(), "Missing required field custom_cookie", "Incorrect validation is returning when custom_cookie parameter is not passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when custom_cookie parameter is not passed");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when custom_cookie parameter is not passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when custom_cookie parameter is not passed");
		}
	}	
	
	@Test(priority=13)
	public void post_dni_with_blank_custom_cookie() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_custom_cookie", "To validate whether user is able to create dni setting through post callflow/dni api with blank custom_cookie.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] custom_cookies = {"", "   "};
		for(String custom_cookie:custom_cookies){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_source_dni_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", custom_cookie);
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when blank" + (custom_cookie.equals("")? "":"spaced") + " custom_cookie is passed.");
			}
		}
	}
	
	@Test(priority=14)
	public void post_dni_with_spl_char_custom_cookie() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_spl_char_custom_cookie", "To validate whether user is able to create dni setting through post callflow/dni api with special character in custom_cookie.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] spl_chars= {".","@","#","_","-","!","~","%","^","&","*","+","=",":",";","'","?",">","<","|"};
		for(String spl_char:spl_chars){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_source_dni_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "automation"+spl_char+"cookie");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when special character("+spl_char+") in custom_cookie is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when special character("+spl_char+") in custom_cookie is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				if(spl_char.equals("-")||spl_char.equals("_")){
					Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when special character("+spl_char+") in custom_cookie is passed.");
					test.log(LogStatus.PASS, "Check API is returning success when special character("+spl_char+") in custom_cookie is passed.");
				}
				else{
					Assert.assertEquals(error_response.get("data").toString(), "Invalid custom_cookie", "Incorrect validation is returning when spl character("+spl_char+") in custom_cookie is passed.");
					test.log(LogStatus.PASS, "Proper validation is returned when special character("+spl_char+") in custom_cookie is passed.");
				}
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when special character("+spl_char+") in custom_cookie is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when special character("+spl_char+") in custom_cookie is passed.");
			}
		}
	}	
	
	@Test(priority=15)
	public void post_dni_valid_custom_cookie() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_valid_custom_cookie", "To validate whether user is able to create dni setting through post callflow/dni api with valid custom_cookie.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "automation cookie");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid custom_cookie is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid custom_cookie is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when valid custom_cookie is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when valid custom_cookie is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid custom_cookie is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid custom_cookie is passed.");
		}
	}
	
	@Test(priority=16)
	public void post_dni_multiple_custom_cookie() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_multiple_custom_cookie", "To validate whether user is able to create dni setting through post callflow/dni api with multiple custom_cookie.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "automation cookie,automation cookie2");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when multiple custom_cookie is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when multiple custom_cookie is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject data_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(data_response.get("result").toString(), "success", "API is returning error when multiple custom_cookie is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when multiple custom_cookie is passed.");
			Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when multiple custom_cookie is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when multiple custom_cookie is passed.");
		}
	}
	
	@Test(priority=17)
	public void post_dni_without_destination_url() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_destination_url", "To validate whether user is able to create dni setting through post callflow/dni api without passing destination_url.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when destination_url parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when destination_url parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when destination_url parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when destination_url parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when destination_url parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when destination_url parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when destination_url parameter is not passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: destination_url", "Invalid message value is returned in response when destination_url parameter is not passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when destination_url parameter is not passed with callflow/dni post api method.");
		}
	}	
	
	@Test(priority=18)
	public void post_dni_with_blank_destination_url() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_destination_url", "To validate whether user is able to create dni setting through post callflow/dni api with blank destination_url.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank destination_url is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank destination_url is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("data").toString(), "Invalid Destination Url undefined", "Incorrect validation is returning when blank destination_url is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when blank destination_url is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when blank destination_url is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when blank destination_url is passed.");
		}
	}	
	
	@Test(priority=19)
	public void post_dni_with_invalid_destination_url() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_destination_url", "To validate whether user is able to create dni setting through post callflow/dni api with invalid destination_url.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] destination_urls = {"abc","*.",".*","*google.com","google*","*google*","*.google."};
		for(String destination_url:destination_urls){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", destination_url);
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+destination_url+") destination_url is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when invalid("+destination_url+") destination_url is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "error", "API returns success when invalid("+destination_url+") destination_url is passed.\nDefect Reported: CT-17162");
				Assert.assertEquals(error_response.get("data").toString(), "Invalid Destination Url "+destination_url, "Incorrect validation is returning when invalid("+destination_url+") destination_url is passed.");
				test.log(LogStatus.PASS, "Proper validation is returned when invalid("+destination_url+") destination_url is passed.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when invalid("+destination_url+") destination_url is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when invalid("+destination_url+") destination_url is passed.");
			}
		}
	}	
	
	@Test(priority=20)
	public void post_dni_with_valid_destination_url() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_destination_url", "To validate whether user is able to create dni setting through post callflow/dni api with valid destination_url.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] destination_urls = {"dev.app.logmycalls.com","*.google.com","*.google.*","google.*","*.*","*.yahoo.com","*.bing.*","google.com"};
		for(String destination_url:destination_urls){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", destination_url);
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+destination_url+") destination_url is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				System.out.println(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+destination_url+") destination_url is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject data_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(data_response.get("result").toString(), "success", "API is returning error when valid("+destination_url+") destination_url is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid("+destination_url+") destination_url is passed.");
				Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid("+destination_url+") destination_url is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid("+destination_url+") destination_url is passed.");
			}
		}
	}	
	
	@Test(priority=21)
	public void post_dni_without_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_referrer", "To validate whether user is able to create dni setting through post callflow/dni api without passing referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when referrer parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when referrer parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when referrer parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when referrer parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when referrer parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when referrer parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when referrer parameter is not passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: referrer", "Invalid message value is returned in response when referrer parameter is not passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when referrer parameter is not passed with callflow/dni post api method.");
		}
	}	
	
	@Test(priority=22)
	public void post_dni_with_blank_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_referrer", "To validate whether user is able to create dni setting through post callflow/dni api with blank referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);	
		json_obj.put("referrer", "");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank referrer is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank referrer is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("data").toString(), "Invalid Referrer undefined", "Incorrect validation is returning when blank referrer is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when blank referrer is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when blank referrer is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when blank referrer is passed.");
		}
	}	
	
	@Test(priority=23)
	public void post_dni_with_invalid_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_referrer", "To validate whether user is able to create dni setting through post callflow/dni api with invalid referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] referrers = {"abc","*.",".*","*google.com","google*","*google*","*.google."};
		for(String referrer:referrers){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");			
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", referrer);			
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+referrer+") referrer is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when invalid("+referrer+") referrer is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "error", "API returns success when invalid("+referrer+") referrer is passed.\nDefect Reported: CT-17162");
				Assert.assertEquals(error_response.get("data").toString(), "Invalid Referrer "+referrer, "Incorrect validation is returning when invalid("+referrer+") referrer is passed.");
				test.log(LogStatus.PASS, "Proper validation is returned when invalid("+referrer+") referrer is passed.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when invalid("+referrer+") referrer is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when invalid("+referrer+") referrer is passed.");
			}
		}
	}	
	
	@Test(priority=24)
	public void post_dni_with_valid_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_referrer", "To validate whether user is able to create dni setting through post callflow/dni api with valid referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] referrers = {"dev.app.logmycalls.com","*.google.com","*.google.*","google.*","*.*","*.yahoo.com","*.bing.*","google.com"};
		for(String referrer:referrers){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("referrer", referrer);
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+referrer+") referrer is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				System.out.println(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+referrer+") referrer is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject data_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(data_response.get("result").toString(), "success", "API is returning error when valid("+referrer+") referrer is passed.");
				test.log(LogStatus.PASS, "Check API is returning success when valid("+referrer+") referrer is passed.");
				Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid("+referrer+") referrer is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid("+referrer+") referrer is passed.");
			}
		}
	}	
	
	@Test(priority=25)
	public void post_dni_without_dni_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_dni_type", "To validate whether user is able to create dni setting through post callflow/dni api without passing dni_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when dni_type parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when dni_type parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when dni_type parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when dni_type parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when dni_type parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when dni_type parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when dni_type parameter is not passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: dni_type", "Invalid message value is returned in response when dni_type parameter is not passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when dni_type parameter is not passed with callflow/dni post api method.");
		}
	}	

	@Test(priority=26)
	public void post_dni_with_blank_dni_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_dni_type", "To validate whether user is able to create dni setting through post callflow/dni api with blank dni_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");			
		json_obj.put("dni_type", "");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");			
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank dni_type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank dni_type value is passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank dni_type value is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank dni_type value is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when blank dni_type value is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when blank dni_type value is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when blank dni_type value is passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "No enum match for: ", "Invalid message value is returned in response when blank dni_type value is passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank dni_type value is passed with callflow/dni post api method.");
			Assert.assertEquals(sub_error_path.get(1), "dni_type", "Invalid path value is displayed when blank dni_type value is passed with callflow/dni post api method.");
		}
	}	
	
	@Test(priority=27)
	public void post_dni_with_invalid_dni_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_dni_type", "To validate whether user is able to create dni setting through post callflow/dni api with invalid dni_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] dni_types = {"abc","single","!@#$","abc12","123a"};
		for(String dni_type:dni_types){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");			
			json_obj.put("dni_type", "");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");			
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+dni_type+") dni_type is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String message = api_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+dni_type+") dni_type value is passed.");		
				JSONArray errors_array = (JSONArray)api_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+dni_type+") dni_type value is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+dni_type+") dni_type value is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+dni_type+") dni_type value is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when invalid("+dni_type+") dni_type value is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/dni");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "ENUM_MISMATCH", "Invalid code value is returned in response when invalid("+dni_type+") dni_type value is passed with callflow/dni post api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "No enum match for: ", "Invalid message value is returned in response when invalid("+dni_type+") dni_type value is passed with callflow/dni post api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+dni_type+") dni_type value is passed with callflow/dni post api method.");
				Assert.assertEquals(sub_error_path.get(1), "dni_type", "Invalid path value is displayed when invalid("+dni_type+") dni_type value is passed with callflow/dni post api method.");
			}
		}
	}
	
	@Test(priority=28)
	public void post_dni_with_url_and_source_dni_type_for_pool() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_url_and_source_dni_type_for_pool", "To validate whether user is able to create dni setting through post callflow/dni api with url and source dni_type for pool based callflow.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] dni_types = {"source","url"};
		for(String dni_type:dni_types){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("referrer", "*.*");
			json_obj.put("dni_type", dni_type);
			if(dni_type.equals("source"))
				json_obj.put("dni_element", dni_class);
			else
				json_obj.put("dni_element", "lmc_track");
			json_obj.put("call_flow_id", 6552);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+dni_type+" dni_type is passed for pool based callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				System.out.println(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when "+dni_type+" dni_type is passed for pool based callflow.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject data_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(data_response.get("result").toString(), "error", "API is returning success when "+dni_type+" dni_type is passed for pool based callflow.");
				test.log(LogStatus.PASS, "Check API is returning error when "+dni_type+" dni_type is passed for pool based callflow.");
				Assert.assertEquals(data_response.get("data").toString(), "Invalid Dni Type "+dni_type, "Proper validation is not displayed when "+dni_type+" dni_type is passed for pool based callflow.");
				test.log(LogStatus.PASS, "Check proper validation is not displayed when "+dni_type+" dni_type is passed for pool based callflow.");
				Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when "+dni_type+" dni_type is passed for pool based callflow.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when "+dni_type+" dni_type is passed for pool based callflow.");
			}
		}
	}	
	
	@Test(priority=29)
	public void post_dni_with_session_dni_type_for_single_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_session_dni_type_for_single_number", "To validate whether user is able to create dni setting through post callflow/dni api with session dni_type for tracking number based callflow.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("referrer", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when session dni_type is passed for tracking number based callflow.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when session dni_type is passed for tracking number based callflow.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject data_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(data_response.get("result").toString(), "error", "API is returning success when session dni_type is passed for tracking number based callflow.");
			test.log(LogStatus.PASS, "Check API is returning error when session dni_type is passed for tracking number based callflow.");
			Assert.assertEquals(data_response.get("data").toString(), "Invalid Dni Type session", "Proper validation is not displayed when session dni_type is passed for tracking number based callflow.");
			test.log(LogStatus.PASS, "Check proper validation is not displayed when session dni_type is passed for tracking number based callflow.");
			Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when session dni_type is passed for tracking number based callflow.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when session dni_type is passed for tracking number based callflow.");
		}
	}	
	
	@Test(priority=30)
	public void post_dni_with_url_and_source_dni_type_for_single_number() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_url_and_source_dni_type_for_single_number", "To validate whether user is able to create dni setting through post callflow/dni api with url and source dni_type for tracking_number based callflow.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		String[] dni_types = {"source","url"};
		for(String dni_type:dni_types){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("referrer", "*.*");
			json_obj.put("dni_type", dni_type);
			if(dni_type.equals("source"))
				json_obj.put("dni_element", dni_class);
			else
				json_obj.put("dni_element", "lmc_track");
			json_obj.put("call_flow_id", 6509);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when "+dni_type+" dni_type is passed for tracking_number based callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				System.out.println(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when "+dni_type+" dni_type is passed for tracking_number based callflow.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject data_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(data_response.get("result").toString(), "success", "API is returning error when "+dni_type+" dni_type is passed for tracking_number based callflow.");
				test.log(LogStatus.PASS, "Check API is returning success when "+dni_type+" dni_type is passed for tracking_number based callflow.");
				Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when "+dni_type+" dni_type is passed for tracking_number based callflow.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when "+dni_type+" dni_type is passed for tracking_number based callflow.");
			}
		}
	}	
	
	@Test(priority=31)
	public void post_dni_with_session_dni_type_for_pool() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_session_dni_type_for_pool", "To validate whether user is able to create dni setting through post callflow/dni api with session dni_type for pool based callflow.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_source_dni_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("referrer", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6552);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when session dni_type is passed for pool based callflow.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when session dni_type is passed for pool based callflow.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject data_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(data_response.get("result").toString(), "success", "API is returning error when session dni_type is passed for pool based callflow.");
			test.log(LogStatus.PASS, "Check API is returning success when session dni_type is passed for pool based callflow.");
			Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when session dni_type is passed for pool based callflow.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when session dni_type is passed for pool based callflow.");
		}
	}	
	
	@Test(priority=32)
	public void post_dni_without_dni_element() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_dni_element", "To validate whether user is able to create dni setting through post callflow/dni api without passing dni_element.");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when dni_element parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when dni_element parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when dni_element parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when dni_element parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when dni_element parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when dni_element parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when dni_element parameter is not passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: dni_element", "Invalid message value is returned in response when dni_element parameter is not passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when dni_element parameter is not passed with callflow/dni post api method.");
		}
	}
	
	@Test(priority=33)
	public void post_dni_with_blank_dni_element() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_dni_element", "To validate whether user is able to create dni setting through post callflow/dni api with blank dni_element.");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", "");
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank dni_element is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank dni_element is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when blank dni_element is passed.");
			Assert.assertEquals(error_response.get("data").toString(), "dni_element cannot be blank.", "Incorrect validation is returning when blank dni_element is passed.");
			test.log(LogStatus.PASS, "Proper validation is returned when blank dni_element is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when blank dni_element is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when blank dni_element is passed.");
		}
	}	
	
	@Test(priority=34)
	public void post_dni_with_invalid_dni_element() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_dni_element", "To validate whether user is able to create dni setting through post callflow/dni api with invalid dni_element.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] invalid_classes = { "  ","test aloha"};
		for(String dni_class : invalid_classes){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+dni_class+") dni_element is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "error", "API is returning error when invalid("+dni_class+") dni_element is passed.");		
				test.log(LogStatus.PASS, "API is returning success when invalid("+dni_class+") dni_element is passed.");
				Assert.assertEquals(api_response.get("err").toString(), "Invalid data. Data of dni_element '" +dni_class+ "' contains empty spaces.", "Incorrect validation is returning when invalid("+dni_class+") dni_element is passed.");
				test.log(LogStatus.PASS, "Proper validation is returned when invalid("+dni_class+") dni_element is passed.");
			}	
		}
	}
	
	@Test(priority=35)
	public void post_dni_with_valid_dni_element() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_dni_element", "To validate whether user is able to create dni setting through post callflow/dni api with valid dni_element.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] valid_classes = {"abc1","!@#$","acb12","12abc","123","-123"};
		for(String dni_class : valid_classes){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+dni_class+") dni_element is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+dni_class+") dni_element is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when valid("+dni_class+") dni_element is passed.");
				test.log(LogStatus.PASS, "Check API returns success when valid("+dni_class+") dni_element is passed.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid("+dni_class+") dni_element is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid("+dni_class+") dni_element is passed.");
			}	
		}
	}
	
	@Test(priority=36)
	public void post_dni_with_lmc_track_dni_element() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_lmc_track_dni_element", "To validate whether user is able to create dni setting through post callflow/dni api with lmc_track dni_element.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] dni_types = {"session","source","url"};
		for(String dni_type:dni_types){
			test.log(LogStatus.PASS, "Check post dni api when lmc_track dni_element is passed for "+dni_type+" dni_type.");
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", dni_type);
			json_obj.put("dni_element", "lmc_track");
			if(dni_type.equals("source")||dni_type.equals("url"))
				json_obj.put("call_flow_id", 6509);
			else
				json_obj.put("call_flow_id", 6552);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when lmc_track dni_element is passed for "+dni_type+" dni_type.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				System.out.println(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when lmc_track dni_element is passed for "+dni_type+" dni_type.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				if(dni_type.equals("url")){
					Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					test.log(LogStatus.PASS, "Check API returns success when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when lmc_track dni_element is passed for "+dni_type+" dni_type.");
				}
				else{
					Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					test.log(LogStatus.PASS, "Check API returns error when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					Assert.assertEquals(error_response.get("data").toString(), "Invalid dni_element 'lmc_track' for '"+dni_type, "Proper validation is not displayed when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					test.log(LogStatus.PASS, "Check proper validation is displayed when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when lmc_track dni_element is passed for "+dni_type+" dni_type.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when lmc_track dni_element is passed for "+dni_type+" dni_type.");
				}
			}	
		}
	}	
	
	@Test(priority=37)
	public void post_dni_without_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_call_flow_id", "To validate whether user is able to create dni setting through post callflow/dni api without passing call_flow_id.");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when call_flow_id parameter is not passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when call_flow_id parameter is not passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when call_flow_id parameter is not passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when call_flow_id parameter is not passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when call_flow_id parameter is not passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when call_flow_id parameter is not passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "OBJECT_MISSING_REQUIRED_PROPERTY", "Invalid code value is returned in response when call_flow_id parameter is not passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Missing required property: call_flow_id", "Invalid message value is returned in response when call_flow_id parameter is not passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when call_flow_id parameter is not passed with callflow/dni post api method.");
		}
	}	
	
	@Test(priority=38)
	public void post_dni_with_blank_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_call_flow_id", "To validate whether user is able to create dni setting through post callflow/dni api with blank call_flow_id.");
		test.assignCategory("CFA POST /callflow/dni API");
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", "");
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String message = api_response.get("message").toString();
			Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when blank call_flow_id is passed.");		
			JSONArray errors_array = (JSONArray)api_response.get("errors");
			JSONObject error_data = (JSONObject) errors_array.get(0);
			String error_code = error_data.get("code").toString();
			Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when blank call_flow_id is passed.");
			String in_value = error_data.get("in").toString();
			Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when blank call_flow_id is passed.");
			String message_value = error_data.get("message").toString();
			Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when blank call_flow_id is passed.");
			String name_value = error_data.get("name").toString();
			Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when blank call_flow_id is passed.");
			JSONArray error_path = (JSONArray)error_data.get("path");
			ArrayList<String> list = new ArrayList<String>();
			list.add("paths");
			list.add("/callflow/dni");
			list.add("post");
			list.add("parameters");
			list.add("0");
			JSONArray ex_path= new JSONArray();
			ex_path.addAll(list);
			Assert.assertEquals(error_path, ex_path);                           
			JSONArray sub_error_array = (JSONArray) error_data.get("errors");
			JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
			String sub_error_code = sub_error_data.get("code").toString();
			Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when blank call_flow_id is passed with callflow/dni post api method.");
			String sub_error_message = sub_error_data.get("message").toString();
			Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when blank call_flow_id is passed with callflow/dni post api method.");
			JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
			Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when blank call_flow_id is passed with callflow/dni post api method.");
			Assert.assertEquals(sub_error_path.get(1), "call_flow_id", "Invalid path value is displayed when blank call_flow_id is passed with callflow/dni post api method.");
			Assert.assertEquals(sub_error_data.get("description").toString(), "The unique ID of the call flow directly attached to this DNI configuration", "Incorrect description is returned when blank call_flow_id is passed with callflow/dni post api method.");
		}
	}
	
	@Test(priority=39)
	public void post_dni_with_invalid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_call_flow_id", "To validate whether user is able to create dni setting through post callflow/dni api with invalid call_flow_id.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] callflow_ids = {"abc","!@#$","abc1","6552ab","-6509"};
		for(String call_flow_id: callflow_ids){
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", call_flow_id);
			json_obj.put("referrer", "*.*");
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+call_flow_id+") call_flow_id is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String message = api_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+call_flow_id+") call_flow_id is passed.");		
				JSONArray errors_array = (JSONArray)api_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+call_flow_id+") call_flow_id is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+call_flow_id+") call_flow_id is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+call_flow_id+") call_flow_id is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when invalid("+call_flow_id+") call_flow_id is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/dni");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+call_flow_id+") call_flow_id is passed with callflow/dni post api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+call_flow_id+") call_flow_id is passed with callflow/dni post api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+call_flow_id+") call_flow_id is passed with callflow/dni post api method.");
				Assert.assertEquals(sub_error_path.get(1), "call_flow_id", "Invalid path value is displayed when invalid("+call_flow_id+") call_flow_id is passed with callflow/dni post api method.");
				Assert.assertEquals(sub_error_data.get("description").toString(), "The unique ID of the call flow directly attached to this DNI configuration", "Incorrect description is returned when invalid("+call_flow_id+") call_flow_id is passed with callflow/dni post api method.");
			}
		}
	}	
	
	@Test(priority=40)
	public void post_dni_with_valid_call_flow_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_call_flow_id", "To validate whether user is able to create dni setting through post callflow/dni api with valid call_flow_id.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when valid call_flow_id is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid call_flow_id is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when valid call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid call_flow_id is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid call_flow_id is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid call_flow_id is passed.");
		}	
	}	
	
	@Test(priority=41)
	public void post_dni_with_blank_referrer_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_blank_referrer_type", "To validate whether user is able to create dni setting through post callflow/dni api with blank referrer_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "source");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6509);
		json_obj.put("referrer", "*.*");
		json_obj.put("referrer_type", "");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when blank referrer_type is passed");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when blank referrer_type is passed.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when blank referrer_type is passed.");
			test.log(LogStatus.PASS, "Check API returns success when valid call_flow_id is passed.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when blank referrer_type is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when blank referrer_type is passed.");
		}	
	}
	
	@Test(priority=42)
	public void post_dni_with_invalid_referrer_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_referrer_type", "To validate whether user is able to create dni setting through post callflow/dni api with invalid referrer_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] invalid_ref_types = {"abc","!@#","123"};
		for(String ref_type:invalid_ref_types){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.google.*");
			json_obj.put("referrer_type", ref_type);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when invalid("+ref_type+") referrer_type is passed");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when invalid("+ref_type+") referrer_type is passed.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when invalid("+ref_type+") referrer_type is passed.");
				test.log(LogStatus.PASS, "Check API returns success when invalid("+ref_type+") call_flow_id is passed.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when invalid("+ref_type+") referrer_type is passed.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when invalid("+ref_type+") referrer_type is passed.");
				
				// Check dni setting in get callflow/dni
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+6509));
				CloseableHttpResponse get_dni_response = HelperClass.make_get_request("/v2/callflow/dni", access_token, list);
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(get_dni_response.getEntity().getContent()));
				String line1 = "";
				while ((line1 = rd1.readLine()) != null) {
					JSONObject get_dni_res = (JSONObject) parser.parse(line1);
					JSONArray call_flow_data = (JSONArray)get_dni_res.get("data");
					JSONObject call_flow = (JSONObject)call_flow_data.get(0);
					JSONArray dnis = (JSONArray)call_flow.get("dni");
					JSONObject dni = (JSONObject)dnis.get(0);
					Assert.assertEquals(dni.get("referrer_type").toString(), "null", "referrer_type is not null when invalid referrer_type is passed");
				}
			}				
		}
	}
	
	@Test(priority=43)
	public void post_dni_with_valid_referrer_type_for_other_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_referrer_type_for_other_referrer", "To validate whether user is able to create dni setting through post callflow/dni api with valid referrer_type for other than google and yahoo referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] valid_ref_types = {"paid","organic"};
		for(String ref_type:valid_ref_types){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			json_obj.put("referrer_type", ref_type);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
				test.log(LogStatus.PASS, "Check API returns success when valid("+ref_type+") call_flow_id is passed for other than google and yahoo referrer.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
				
				// Check dni setting in get callflow/dni
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+6509));
				CloseableHttpResponse get_dni_response = HelperClass.make_get_request("/v2/callflow/dni", access_token, list);
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(get_dni_response.getEntity().getContent()));
				String line1 = "";
				while ((line1 = rd1.readLine()) != null) {
					JSONObject get_dni_res = (JSONObject) parser.parse(line1);
					JSONArray call_flow_data = (JSONArray)get_dni_res.get("data");
					JSONObject call_flow = (JSONObject)call_flow_data.get(0);
					JSONArray dnis = (JSONArray)call_flow.get("dni");
					JSONObject dni = (JSONObject)dnis.get(0);
					Assert.assertEquals(dni.get("referrer_type").toString(), "null", "referrer_type is not null when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
				}
			}				
		}
	}	
	
	@Test(priority=44)
	public void post_dni_with_valid_referrer_type_for_google_yahoo_referrer() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_valid_referrer_type_for_google_yahoo_referrer", "To validate whether user is able to create dni setting through post callflow/dni api with valid referrer_type for google and yahoo referrer.");
		test.assignCategory("CFA POST /callflow/dni API");
		String google_ref = "*.google.*", yahoo_ref = "*.yahoo.com";
		String[] referrers = {google_ref,yahoo_ref};
		String[] valid_ref_types = {"paid","organic"};
		for(String ref:referrers){
			for(String ref_type:valid_ref_types){
				Random rand = new Random();
				int n = rand.nextInt(50) + 1;
				String dni_class = "post_dni_api_" + String.valueOf(n);
				JSONArray dni_array = new JSONArray();
				JSONObject json_obj = new JSONObject();
				json_obj.put("custom_cookie", "");
				json_obj.put("group_id", 8);
				json_obj.put("destination_url", "*.*");
				json_obj.put("dni_type", "source");
				json_obj.put("dni_element", dni_class);
				json_obj.put("call_flow_id", 6509);
				json_obj.put("referrer", ref);
				json_obj.put("referrer_type", ref_type);
				dni_array.add(json_obj);
				CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
				Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
				test.log(LogStatus.PASS, "Check status code when valid("+ref_type+") referrer_type is passed for "+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					JSONParser parser = new JSONParser();
					JSONObject api_response =(JSONObject) parser.parse(line);
					String result_data = api_response.get("result").toString();
					Assert.assertEquals(result_data, "success", "API is returning error when valid("+ref_type+") referrer_type is passed for "+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));		
					Object err_data = api_response.get("err");
					Assert.assertNull(err_data,"Invalid err data is displayed in response.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject error_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when invalid("+ref_type+") referrer_type is passed for ."+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));
					test.log(LogStatus.PASS, "Check API returns success when invalid("+ref_type+") call_flow_id is passed for "+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));
					Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when invalid("+ref_type+") referrer_type is passed for "+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when invalid("+ref_type+") referrer_type is passed for "+(ref.equals(google_ref)?"google":"yahoo"+" referrer."));
					
					// Check dni setting in get callflow/dni
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+6509));
					CloseableHttpResponse get_dni_response = HelperClass.make_get_request("/v2/callflow/dni", access_token, list);
					BufferedReader rd1 = new BufferedReader(new InputStreamReader(get_dni_response.getEntity().getContent()));
					String line1 = "";
					while ((line1 = rd1.readLine()) != null) {
						JSONObject get_dni_res = (JSONObject) parser.parse(line1);
						JSONArray call_flow_data = (JSONArray)get_dni_res.get("data");
						JSONObject call_flow = (JSONObject)call_flow_data.get(0);
						JSONArray dnis = (JSONArray)call_flow.get("dni");
						JSONObject dni = (JSONObject)dnis.get(0);
						Assert.assertEquals(dni.get("referrer_type").toString(), ref_type, "Incorrect referrer_type is displayed when valid("+ref_type+") referrer_type is passed for other than google and yahoo referrer.");
						Assert.assertEquals(dni.get("provisioned_route_id").toString(), "6509", "Incorrect provisioned_route_id is displayed in callflow/dni api response.");
						Assert.assertEquals(dni.get("referrer").toString(), ref, "Incorrect referrer is displayed in callflow/dni api response.");
						Assert.assertEquals(dni.get("dni_element").toString(), dni_class, "Incorrect dni_element is displayed in callflow/dni api response.");
						Assert.assertEquals(dni.get("destination_url").toString(), "*.*", "Incorrect destination_url is displayed in callflow/dni api response.");
						Assert.assertEquals(dni.get("dni_type").toString(), "source", "Incorrect dni_type is displayed in callflow/dni api response.");
					}
				}				
			}
		}
	}
	
	@Test(priority=45)
	public void post_dni_without_ttl_for_session() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_without_ttl_for_session", "To validate whether user is able to create dni setting through post callflow/dni api without ttl parameter for session dni.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6552);
		json_obj.put("referrer", "*.*");
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when ttl parameter is not passed for session dni");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			String result_data = api_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when ttl parameter is not passed for session dni.");		
			Object err_data = api_response.get("err");
			Assert.assertNull(err_data,"Invalid err data is displayed in response.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject error_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(error_response.get("result").toString(), "error", "API is returning success when ttl parameter is not passed for session dni.");
			test.log(LogStatus.PASS, "Check API returns error when ttl parameter is not passed for session dni.");
			Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when ttl parameter is not passed for session dni.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when ttl parameter is not passed for session dni.");
		}	
	}	
	
	@Test(priority=46)
	public void post_dni_with_invalid_ttl_for_session() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_invalid_ttl_for_session", "To validate whether user is able to create dni setting through post callflow/dni api with invalid ttl value.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] invalid_ttls = {"","  ","abc","!@#$","123ab"};
		for(String ttl:invalid_ttls){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "session");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6552);
			json_obj.put("referrer", "*.*");
			json_obj.put("TTL", ttl);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when with blank ttl value.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String message = api_response.get("message").toString();
				Assert.assertEquals(message, "Validation errors", "Invalid message value is returned in response when invalid("+ttl+") ttl value is passed.");		
				JSONArray errors_array = (JSONArray)api_response.get("errors");
				JSONObject error_data = (JSONObject) errors_array.get(0);
				String error_code = error_data.get("code").toString();
				Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER", "Invalid code value is returned in response when invalid("+ttl+") ttl value is passed.");
				String in_value = error_data.get("in").toString();
				Assert.assertEquals(in_value, "body", "Invalid in value is returned in response when invalid("+ttl+") ttl value is passed.");
				String message_value = error_data.get("message").toString();
				Assert.assertEquals(message_value, "Invalid parameter (dni): Value failed JSON Schema validation", "Invalid message value is returned in response when invalid("+ttl+") ttl value is passed.");
				String name_value = error_data.get("name").toString();
				Assert.assertEquals(name_value, "dni", "Invalid name value is returned in response when invalid("+ttl+") ttl value is passed.");
				JSONArray error_path = (JSONArray)error_data.get("path");
				ArrayList<String> list = new ArrayList<String>();
				list.add("paths");
				list.add("/callflow/dni");
				list.add("post");
				list.add("parameters");
				list.add("0");
				JSONArray ex_path= new JSONArray();
				ex_path.addAll(list);
				Assert.assertEquals(error_path, ex_path);                           
				JSONArray sub_error_array = (JSONArray) error_data.get("errors");
				JSONObject sub_error_data = (JSONObject) sub_error_array.get(0);
				String sub_error_code = sub_error_data.get("code").toString();
				Assert.assertEquals(sub_error_code, "INVALID_TYPE", "Invalid code value is returned in response when invalid("+ttl+") ttl value is passed with callflow/dni post api method.");
				String sub_error_message = sub_error_data.get("message").toString();
				Assert.assertEquals(sub_error_message, "Expected type integer but found type string", "Invalid message value is returned in response when invalid("+ttl+") ttl value is passed with callflow/dni post api method.");
				JSONArray sub_error_path = (JSONArray)sub_error_data.get("path");
				Assert.assertEquals(sub_error_path.get(0), "0", "Invalid path value is displayed when invalid("+ttl+") ttl value is passed with callflow/dni post api method.");
				Assert.assertEquals(sub_error_path.get(1), "TTL", "Invalid path value is displayed when invalid("+ttl+") ttl value is passed with callflow/dni post api method.");
				Assert.assertEquals(sub_error_data.get("description"), "Time to Live in minutes  Max - 525,600 min", "Invalid description value is returned.");
			}	
		}
	}		
	
	@Test(priority=47)
	public void post_dni_with_0_ttl_for_session() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_0_ttl_for_session", "To validate whether user is able to create dni setting through post callflow/dni api with 0 ttl value.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6552);
		json_obj.put("referrer", "*.*");
		json_obj.put("TTL", 0);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when with 0 ttl value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			Assert.assertEquals(api_response.get("result"), "error", "API is returning success when 0 TTL value is passed.");
			test.log(LogStatus.PASS, "API is returning error when 0 TTL value is passed.");
			Assert.assertEquals(api_response.get("err"), "TTL Value is required and should be greater than 15 minutes.", "Proper validation is not displayed when 0 TTL value is passed.");
			test.log(LogStatus.PASS, "Proper validation is displayed when 0 TTL value is passed.");
		}	
	}
	
	@Test(priority=48)
	public void post_dni_with_max_ttl_for_session() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_max_ttl_for_session", "To validate whether user is able to create dni setting through post callflow/dni api with 525,601 ttl value.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6552);
		json_obj.put("referrer", "*.*");
		json_obj.put("TTL", 525601);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when with 525,601 ttl value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			Assert.assertEquals(api_response.get("result"), "success", "API is returning error when 525,601 TTL value is passed.");
			Assert.assertNull(api_response.get("err"), "err field is not null.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject data_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(data_response.get("result"), "error", "API is returning success when 525,601 TTL value is passed.");
			test.log(LogStatus.PASS, "Check API is returning error when 525,601 TTL value is passed.");
			Assert.assertEquals(data_response.get("data").toString(), "TTL is over max limit.", "Proper validation is not displayed when 525,601 TTL value is passed.");
			Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when 525,601 TTL value is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when 525,601 TTL value is passed.");
			test.log(LogStatus.PASS, "Proper validation is displayed when 525,601 TTL value is passed.");
		}	
	}
	
	@Test(priority=49)
	public void post_dni_with_boundary_ttl_for_session() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_boundary_ttl_for_session", "To validate whether user is able to create dni setting through post callflow/dni api with 525,600 ttl value.");
		test.assignCategory("CFA POST /callflow/dni API");
		Random rand = new Random();
		int n = rand.nextInt(50) + 1;
		String dni_class = "post_dni_api_" + String.valueOf(n);
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("custom_cookie", "");
		json_obj.put("group_id", 8);
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "session");
		json_obj.put("dni_element", dni_class);
		json_obj.put("call_flow_id", 6552);
		json_obj.put("referrer", "*.*");
		json_obj.put("TTL", 525600);
		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		test.log(LogStatus.PASS, "Check status code when with 525,600 ttl value.");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject api_response =(JSONObject) parser.parse(line);
			System.out.println(line);
			Assert.assertEquals(api_response.get("result"), "success", "API is returning error when 525,600 TTL value is passed.");
			Assert.assertNull(api_response.get("err"), "err field is not null.");
			JSONArray data_array = (JSONArray) api_response.get("data");
			JSONObject data_response = (JSONObject)data_array.get(0);
			Assert.assertEquals(data_response.get("result"), "success", "API is returning error when 525,600 TTL value is passed.");
			test.log(LogStatus.PASS, "Check API is returning success when 525,600 TTL value is passed.");
			Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when 525,600 TTL value is passed.");
			test.log(LogStatus.PASS, "Check incorrect entry count is displayed when 525,600 TTL value is passed.");
		}	
	}
	
	@Test(priority=50)
	public void post_dni_with_ttl_for_source_and_url() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_ttl_for_source_and_url", "To validate whether user is able to create dni setting through post callflow/dni api with TTL field for source and url dni_type.");
		test.assignCategory("CFA POST /callflow/dni API");
		String[] dni_types = {"source","url"};
		for(String dni_type:dni_types){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			json_obj.put("group_id", 8);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", dni_type);
			if(dni_type.equals("url"))
				json_obj.put("dni_element", "lmc_track");
			else
				json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", 6509);
			json_obj.put("referrer", "*.*");
			json_obj.put("TTL", 10);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when TTL value is passed for "+dni_type+ " type dni.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				String result_data = api_response.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error when when TTL value is passed for "+dni_type+ " type dni.");		
				Object err_data = api_response.get("err");
				Assert.assertNull(err_data,"Invalid err data is displayed in response.");
				JSONArray data_array = (JSONArray) api_response.get("data");
				JSONObject error_response = (JSONObject)data_array.get(0);
				Assert.assertEquals(error_response.get("result").toString(), "success", "API is returning error when when TTL value is passed for "+dni_type+ " type dni.");
				test.log(LogStatus.PASS, "Check API returns success when when TTL value is passed for "+dni_type+ " type dni.");
				Assert.assertEquals(error_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when when TTL value is passed for "+dni_type+ " type dni.");
				test.log(LogStatus.PASS, "Check incorrect entry count is displayed when when TTL value is passed for "+dni_type+ " type dni.");
				
				// Check dni setting in get callflow/dni
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("filter", "call_flow_id%3d"+6509));
				CloseableHttpResponse get_dni_response = HelperClass.make_get_request("/v2/callflow/dni", access_token, list);
				BufferedReader rd1 = new BufferedReader(new InputStreamReader(get_dni_response.getEntity().getContent()));
				String line1 = "";
				while ((line1 = rd1.readLine()) != null) {
					JSONObject get_dni_res = (JSONObject) parser.parse(line1);
					JSONArray call_flow_data = (JSONArray)get_dni_res.get("data");
					JSONObject call_flow = (JSONObject)call_flow_data.get(0);
					JSONArray dnis = (JSONArray)call_flow.get("dni");
					JSONObject dni = (JSONObject)dnis.get(0);
					Assert.assertFalse(dni.containsKey("TTL"), "TTL value is displayed in get callflow/dni when TTL value is passed for "+dni_type+ " type dni.");
				}
			}				
		}
	}
	
	@Test(priority=51)
	public void post_dni_with_agency_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_agency_admin_access_token", "To validate whether user is able to create dni setting through post callflow/dni api with agency admin access_token.");
		test.assignCategory("CFA POST /callflow/dni API");
		int agency_callflow = 6509, company_callflow = 6662, location_callflow = 6664, other_billing_callflow = 7800; 
		int[] callflows = {agency_callflow, company_callflow, location_callflow, other_billing_callflow};
		for(int callflow: callflows){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			if(callflow == agency_callflow)
				json_obj.put("group_id", 8);
			else if(callflow == company_callflow)
				json_obj.put("group_id", 114);
			else if(callflow == location_callflow)
				json_obj.put("group_id", 116);
			else if(callflow == other_billing_callflow)
				json_obj.put("group_id", 76);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", callflow);
			json_obj.put("referrer", "*.*");
			json_obj.put("TTL", 525600);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":(callflow==location_callflow)?"location":"other billing")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				if(callflow==agency_callflow || callflow==company_callflow || callflow==location_callflow){
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"location")+" level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "success", "API is returning error when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"location")+" level callflow.");
					test.log(LogStatus.PASS, "Check API is returning success when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"location")+" level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"location")+" level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when agency admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"location")+" level callflow.");
				}
				else{
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when agency admin user create dni setting at other billing level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "error", "API is returning success when agency admin user create dni setting at other billing level callflow.");
					test.log(LogStatus.PASS, "Check API is returning error when agency admin user create dni setting at other billing level callflow.");
					Assert.assertEquals(data_response.get("data"), "Not authorized to make changes for Call Flow ID "+callflow, "API is returning success when agency admin user create dni setting at other billing level callflow.");
					test.log(LogStatus.PASS, "Check proper validation message is displayed when agency admin user create dni setting at other billing level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when agency admin user create dni setting at other billing level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when agency admin user create dni setting at other billing level callflow.");
				}
			}		
		}
	}	
	
	@Test(priority=52)
	public void post_dni_with_company_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_company_admin_access_token", "To validate whether user is able to create dni setting through post callflow/dni api with company admin access_token.");
		test.assignCategory("CFA POST /callflow/dni API");
		String access_token = HelperClass.get_oauth_token("company@admin.com", "lmc2demo");
		int agency_callflow = 6509, company_callflow = 6662, location_callflow = 6664, other_billing_callflow = 7800; 
		int[] callflows = {agency_callflow, company_callflow, location_callflow, other_billing_callflow};
		for(int callflow: callflows){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			if(callflow == agency_callflow)
				json_obj.put("group_id", 8);
			else if(callflow == company_callflow)
				json_obj.put("group_id", 114);
			else if(callflow == location_callflow)
				json_obj.put("group_id", 116);
			else if(callflow == other_billing_callflow)
				json_obj.put("group_id", 76);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", callflow);
			json_obj.put("referrer", "*.*");
			json_obj.put("TTL", 525600);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":(callflow==location_callflow)?"location":"other billing")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				if(callflow==company_callflow || callflow==location_callflow){
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when company admin user create dni setting at "+((callflow==company_callflow)?"company":"location")+" level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "success", "API is returning error when company admin user create dni setting at "+((callflow==company_callflow)?"company":"location")+" level callflow.");
					test.log(LogStatus.PASS, "Check API is returning success when company admin user create dni setting at "+((callflow==company_callflow)?"company":"location")+" level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when company admin user create dni setting at "+((callflow==company_callflow)?"company":"location")+" level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when company admin user create dni setting at "+((callflow==company_callflow)?"company":"location")+" level callflow.");
				}
				else{
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "error", "API is returning success when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check API is returning error when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					Assert.assertEquals(data_response.get("data"), "Not authorized to make changes for Call Flow ID "+callflow, "API is returning success when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check proper validation message is displayed when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when company admin user create dni setting at "+((callflow==agency_callflow)?"agency":"other billing")+" level callflow.");
				}
			}		
		}
	}		
	
	@Test(priority=53)
	public void post_dni_with_location_admin_access_token() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("post_dni_with_company_admin_access_token", "To validate whether user is able to create dni setting through post callflow/dni api with company admin access_token.");
		test.assignCategory("CFA POST /callflow/dni API");
		String access_token = HelperClass.get_oauth_token("location@admin.com", "lmc2demo");
		int agency_callflow = 6509, company_callflow = 6662, location_callflow = 6664, other_billing_callflow = 7800; 
		int[] callflows = {agency_callflow, company_callflow, location_callflow, other_billing_callflow};
		for(int callflow: callflows){
			Random rand = new Random();
			int n = rand.nextInt(50) + 1;
			String dni_class = "post_dni_api_" + String.valueOf(n);
			JSONArray dni_array = new JSONArray();
			JSONObject json_obj = new JSONObject();
			json_obj.put("custom_cookie", "");
			if(callflow == agency_callflow)
				json_obj.put("group_id", 8);
			else if(callflow == company_callflow)
				json_obj.put("group_id", 114);
			else if(callflow == location_callflow)
				json_obj.put("group_id", 116);
			else if(callflow == other_billing_callflow)
				json_obj.put("group_id", 76);
			json_obj.put("destination_url", "*.*");
			json_obj.put("dni_type", "source");
			json_obj.put("dni_element", dni_class);
			json_obj.put("call_flow_id", callflow);
			json_obj.put("referrer", "*.*");
			json_obj.put("TTL", 525600);
			dni_array.add(json_obj);
			CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			test.log(LogStatus.PASS, "Check status code when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":(callflow==location_callflow)?"location":"other billing")+" level callflow.");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject api_response =(JSONObject) parser.parse(line);
				if(callflow==location_callflow){
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when location admin user create dni setting at location level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "success", "API is returning error when location admin user create dni setting at location level callflow.");
					test.log(LogStatus.PASS, "Check API is returning success when location admin user create dni setting at location level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when location admin user create dni setting at location level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when location admin user create dni setting at location level callflow.");
				}
				else{
					Assert.assertEquals(api_response.get("result"), "success", "API is returning error when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					Assert.assertNull(api_response.get("err"), "err field is not null.");
					JSONArray data_array = (JSONArray) api_response.get("data");
					JSONObject data_response = (JSONObject)data_array.get(0);
					Assert.assertEquals(data_response.get("result"), "error", "API is returning success when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check API is returning error when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					Assert.assertEquals(data_response.get("data"), "Not authorized to make changes for Call Flow ID "+callflow, "API is returning success when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check proper validation message is displayed when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					Assert.assertEquals(data_response.get("entry_count").toString(), "1", "Incorrect entry_count is returning when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
					test.log(LogStatus.PASS, "Check incorrect entry count is displayed when location admin user create dni setting at "+((callflow==agency_callflow)?"agency":(callflow==company_callflow)?"company":"other billing")+" level callflow.");
				}
			}		
		}
	}			
}
