package oauth_token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import common.*;

@Listeners(Listener.class)
@SuppressWarnings("unchecked")
public class Access_token extends BaseClass{

	//@Test(priority=1)
	// Execute oauth/token api method with blank content-type
	public void oauth_token_without_content_type() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("oauth_token_without_content_type", "To validate whether user is able to get access_token through oauth/token api without content-type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank content-type in header");
		System.out.println("Execute oauth/token api method with blank content-type in header");
		String[] header = {"",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when blank Content-Type is passed in header.");
		test.log(LogStatus.PASS, "Check status code when blank Content-Type is passed in header.");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank Content-Type is passed in header.");
		test.log(LogStatus.PASS, "Check status message when blank Content-Type is passed in header.");
	}
	
	//@Test(priority=2)
	// Execute oauth/token api method with invalid content-type
	public void oauth_token_invalid_content_type() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("oauth_token_invalid_content_type", "To validate whether user is able to get access_token through oauth/token api with invalid content-type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid content-type in header");
		System.out.println("Execute oauth/token api method with invalid content-type in header");
		String[] content_types = {"application/javascript",
									"application/pdf",
									"image/png",
									"text/plain",
									"text/css",
									"text/plain",
									"text/csv",
									"text/html",
									"text/xml",
									"application/xml"};
		for(String content_type:content_types){
			String[] header = {content_type,""};
			// Add parameter and its value to oauth/token
			JSONObject parameters = new JSONObject();
			parameters.put("grant_type", "password");
			parameters.put("client_id", "system");
			parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
			parameters.put("username", "lmcsuper@logmycalls.com");
			parameters.put("password", "ScottIsDaMan");
			StringEntity input = new StringEntity( parameters.toString());
			CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when invalid Content-Type is passed in header.");
			Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when invalid Content-Type is passed in header.");
		}
		test.log(LogStatus.PASS, "Check status code when invalid Content-Type is passed in header.");		
		test.log(LogStatus.PASS, "Check status message when invalid Content-Type is passed in header.");
	}	
	
	//@Test(priority=3)
	// Execute oauth/token api method with valid content-type
	public void oauth_token_with_valid_content_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_content_type", "To validate whether user is able to get access_token through oauth/token api with valid content-type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid content-type in header");
		System.out.println("Execute oauth/token api method with valid content-type in header");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}
	
	//@Test(priority=4)
	// Execute oauth/token api method with blank content-type
	public void oauth_token_without_grant_type() throws ClientProtocolException, IOException, URISyntaxException{
		test = extent.startTest("oauth_token_without_grant_type", "To validate whether user is able to get access_token through oauth/token api without grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank content-type in header");
		System.out.println("Execute oauth/token api method with blank content-type in header");
		String[] header = {"",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when grant_type parameter is not passed.");
		test.log(LogStatus.PASS, "Check status code when grant_type parameter is not passed.");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when grant_type parameter is not passed.");
		test.log(LogStatus.PASS, "Check status message when grant_type parameter is not passed.");
	}
	
	//@Test(priority=5)
	// Execute oauth/token api method with blank grant_type
	public void oauth_token_with_blank_grant_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_grant_type", "To validate whether user is able to get access_token through oauth/token api with blank grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank grant_type");
		System.out.println("Execute oauth/token api method with blank grant_type");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String grant_type = "";
		parameters.put("grant_type", grant_type);
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "unsupported_grant_type","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Unsupported grant type: "+grant_type,"Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when blank grant_type value is displayed.");
		}
	}
	
	//@Test(priority=6)
	// Execute oauth/token api method with blank spaced grant_type
	public void oauth_token_with_blank_spaced_grant_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_spaced_grant_type", "To validate whether user is able to get access_token through oauth/token api with blank spaced grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank spaced grant_type");
		System.out.println("Execute oauth/token api method with blank spaced grant_type");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String grant_type = "   ";
		parameters.put("grant_type", grant_type);
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "unsupported_grant_type","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Unsupported grant type: "+grant_type,"Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when blank spaced grant_type value is displayed.");
		}
	}
	
	//@Test(priority=7)
	// Execute oauth/token api method with invalid grant_type
	public void oauth_token_with_invalid_grant_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_invalid_grant_type", "To validate whether user is able to get access_token through oauth/token api with invalid grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid grant_type");
		System.out.println("Execute oauth/token api method with invalid grant_type");
		String[] grant_types = {"abc","!@#","123abc","1234"};
		for(String grant_type:grant_types){	
			String[] header = {"application/json",""};
			// Add parameter and its value to oauth/token
			JSONObject parameters = new JSONObject();
			parameters.put("grant_type", grant_type);
			parameters.put("client_id", "system");
			parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
			parameters.put("username", "lmcsuper@logmycalls.com");
			parameters.put("password", "ScottIsDaMan");
			StringEntity input = new StringEntity( parameters.toString());
			CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
				Assert.assertEquals(json.get("error").toString(), "unsupported_grant_type","Invalid error value is displayed in response.");
				Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
				Assert.assertEquals(json.get("error_description").toString(), "Unsupported grant type: "+grant_type,"Invalid error_description value is displayed in response.");
			}
		}
		test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid grant_type value is displayed.");
	}
	
	//@Test(priority=8)
	// Execute oauth/token api method with username grant_type
	public void oauth_token_with_username_grant_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_username_grant_type", "To validate whether user is able to get access_token through oauth/token api with username grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with username grant_type");
		System.out.println("Execute oauth/token api method with username grant_type");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "username");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "unsupported_grant_type","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Unsupported grant type: username","Invalid error_description value is displayed in response.");
		}
		test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid grant_type value is displayed.");
	}
	
	//@Test(priority=9)
	// Execute oauth/token api method with valid grant_type
	public void oauth_token_with_valid_grant_type() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_grant_type", "To validate whether user is able to get access_token through oauth/token api with valid grant_type");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid grant_type");
		System.out.println("Execute oauth/token api method with valid grant_type");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}
	
	//@Test(priority=10)
	// Execute oauth/token api method with blank client_id
	public void oauth_token_with_blank_client_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_client_id", "To validate whether user is able to get access_token through oauth/token api with blank client_id");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank client_id");
		System.out.println("Execute oauth/token api method with blank client_id");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String client_id = "";
		parameters.put("grant_type", "password");
		parameters.put("client_id", client_id);
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 501), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when blank client_id is passed.");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank client_id is passed.");
		test.log(LogStatus.PASS, "Check status code when blank client_id is passed in header.");		
		test.log(LogStatus.PASS, "Check status message when blank client_id is passed in header.");
	}
	
	//@Test(priority=11)
	// Execute oauth/token api method with blank spaced client_id
	public void oauth_token_with_blank_spaced_client_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_spaced_client_id", "To validate whether user is able to get access_token through oauth/token api with blank spaced client_id");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank spaced client_id");
		System.out.println("Execute oauth/token api method with blank spaced client_id");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String client_id = "   ";
		parameters.put("grant_type", "password");
		parameters.put("client_id", client_id);
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+"\n Defect Reported: CT-17144");
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201, "Invalid status is displayed: "+ response.getStatusLine().getStatusCode() + " " +response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "server_error","Invalid error value is displayed in response when blank spaced client_id is passed.");
			test.log(LogStatus.PASS, "Invalid error value is displayed in response when blank spaced client_id is passed.");
		}
	}
	
	//@Test(priority=12)
	// Execute oauth/token api method with invalid client_id
	public void oauth_token_with_invalid_client_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_invalid_client_id", "To validate whether user is able to get access_token through oauth/token api with invalid client_id");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid client_id");
		System.out.println("Execute oauth/token api method with invalid client_id");
		String[] client_ids = {"abc","!@#","123abc","1234"};
		for(String client_id:client_ids){	
			String[] header = {"application/json",""};
			// Add parameter and its value to oauth/token
			JSONObject parameters = new JSONObject();
			parameters.put("grant_type", "password");
			parameters.put("client_id", client_id);
			parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
			parameters.put("username", "lmcsuper@logmycalls.com");
			parameters.put("password", "ScottIsDaMan");
			StringEntity input = new StringEntity( parameters.toString());
			CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
			Assert.assertTrue(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201, "Invalid status is displayed: "+ response.getStatusLine().getStatusCode() + " " +response.getStatusLine().getReasonPhrase()+"\n Defect Reported: CT-17144");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {				
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
				Assert.assertEquals(json.get("error").toString(), "server_error","Invalid error value is displayed in response when invalid client_id is passed.");
				test.log(LogStatus.PASS, "Invalid error value is displayed in response when invalid client_id is passed.");
			}
		}
		test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid client_id value is displayed.");
	}
	
	//@Test(priority=13)
	// Execute oauth/token api method with valid client_id
	public void oauth_token_with_valid_client_id() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_client_id", "To validate whether user is able to get access_token through oauth/token api with valid client_id");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid client_id");
		System.out.println("Execute oauth/token api method with valid client_id");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}
	
	//@Test(priority=14)
	// Execute oauth/token api method with blank client_secret
	public void oauth_token_with_blank_client_secret() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_client_secret", "To validate whether user is able to get access_token through oauth/token api with blank client_secret");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank client_secret");
		System.out.println("Execute oauth/token api method with blank client_secret");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String client_secret = "";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", client_secret);
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 501), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+"\n Defect Reported: CT-17144");
		// Check status of response
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 401, "status code is not 401 when blank client_secret is passed.");
		Assert.assertEquals(response.getStatusLine().getReasonPhrase(), "Unauthorized", "Proper message is not displayed when blank client_secret is passed.");
		test.log(LogStatus.PASS, "Check status code when blank client_secret is passed.");		
		test.log(LogStatus.PASS, "Check status message when blank client_secret is passed.");
	}
	
	//@Test(priority=15)
	// Execute oauth/token api method with blank spaced client_id
	public void oauth_token_with_blank_spaced_client_secret() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_spaced_client_id", "To validate whether user is able to get access_token through oauth/token api with blank spaced client_secret");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank spaced client_secret");
		System.out.println("Execute oauth/token api method with blank spaced client_secret");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String client_secret = "   ";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", client_secret);
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 501), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+"\n Defect Reported: CT-17144");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "server_error","Invalid error value is displayed in response when blank spaced client_secret is passed.");
			test.log(LogStatus.PASS, "Invalid error value is displayed in response when blank spaced client_secret is passed.");
		}
	}	
	
	//@Test(priority=16)
	// Execute oauth/token api method with invalid client_secret
	public void oauth_token_with_invalid_client_secret() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_invalid_client_secret", "To validate whether user is able to get access_token through oauth/token api with invalid client_secret");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid client_secret");
		System.out.println("Execute oauth/token api method with invalid client_secret");
		String[] client_secrets = {"abc","!@#","123abc","1234"};
		for(String client_secret:client_secrets){	
			String[] header = {"application/json",""};
			// Add parameter and its value to oauth/token
			JSONObject parameters = new JSONObject();
			parameters.put("grant_type", "password");
			parameters.put("client_id", "system");
			parameters.put("client_secret", client_secret);
			parameters.put("username", "lmcsuper@logmycalls.com");
			parameters.put("password", "ScottIsDaMan");
			StringEntity input = new StringEntity( parameters.toString());
			CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
			Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 501), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase()+"\n Defect Reported: CT-17144");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {		
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(line);
				Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
				Assert.assertEquals(json.get("error").toString(), "server_error","Invalid error value is displayed in response when invalid client_secret is passed.");
				test.log(LogStatus.PASS, "Invalid error value is displayed in response when invalid client_secret is passed.");
			}
		}
		test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid client_secret value is displayed.");
	}
	
	//@Test(priority=17)
	// Execute oauth/token api method with valid client_secret
	public void oauth_token_with_valid_client_secret() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_client_secret", "To validate whether user is able to get access_token through oauth/token api with valid client_secret");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid client_secret");
		System.out.println("Execute oauth/token api method with valid client_secret");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}

	//@Test(priority=18)
	// Execute oauth/token api method with without username
	public void oauth_token_with_without_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_without_username", "To validate whether user is able to get access_token through oauth/token api with without username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with without username");
		System.out.println("Execute oauth/token api method with without username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_request","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Missing required parameter: username","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when without username value is passed.");
		}	
	}	
	
	//@Test(priority=19)
	// Execute oauth/token api method with blank username
	public void oauth_token_with_blank_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_username", "To validate whether user is able to get access_token through oauth/token api with blank username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank username");
		System.out.println("Execute oauth/token api method with blank username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String username = "";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", username);
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_request","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Missing required parameter: username","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when blank username value is passed.");
		}	
	}	
	
	//@Test(priority=20)
	// Execute oauth/token api method with blank spaced username
	public void oauth_token_with_blank_spaced_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_blank_spaced_username", "To validate whether user is able to get access_token through oauth/token api with blank spaced username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank spaced username");
		System.out.println("Execute oauth/token api method with blank spaced username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String username = "    ";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", username);
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_grant","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when blank spaced username value is passed.");
		}	
	}
	
	//@Test(priority=21)
	// Execute oauth/token api method with blank username
	public void oauth_token_with_invalid_format_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_invalid_format_username", "To validate whether user is able to get access_token through oauth/token api with invalid_format username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid_format username");
		System.out.println("Execute oauth/token api method with invalid_format username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String username = "lmcsuper#logmycalls,com";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", username);
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_grant","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid_format username value is passed.");
		}	
	}	
	
	//@Test(priority=22)
	// Execute oauth/token api method with non_existing username
	public void oauth_token_with_non_existing_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_non_existing_username", "To validate whether user is able to get access_token through oauth/token api with non_existing username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with non_existing username");
		System.out.println("Execute oauth/token api method with non_existing username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		String username = "testcfa@test.com";
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", username);
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_grant","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when non_existing username value is passed.");
		}	
	}	
	
	//@Test(priority=23)
	// Execute oauth/token api method with valid username
	public void oauth_token_with_valid_username() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_username", "To validate whether user is able to get access_token through oauth/token api with valid username");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid username");
		System.out.println("Execute oauth/token api method with valid username");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}
	
	//@Test(priority=24)
	// Execute oauth/token api method without password
	public void oauth_token_without_password() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_without_username", "To validate whether user is able to get access_token through oauth/token api without password");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method without password");
		System.out.println("Execute oauth/token api method without password");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_request","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Missing required parameter: password","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when password parameter is not passed.");
		}	
	}
	
	//@Test(priority=25)
	// Execute oauth/token api method with blank password
	public void oauth_token_with_blank_password() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_blank_password", "To validate whether user is able to get access_token through oauth/token api with blank password");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with blank password");
		System.out.println("Execute oauth/token api method with blank password");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_request","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Missing required parameter: password","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when blank password is passed.");
		}	
	}
	
	//@Test(priority=26)
	// Execute oauth/token api method with invalid password
	public void oauth_token_with_invalid_password() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_invalid_password", "To validate whether user is able to get access_token through oauth/token api with invalid password");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with invalid password");
		System.out.println("Execute oauth/token api method with invalid password");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "aloha");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(line);
			Assert.assertTrue(json.containsKey("error"), "error key is present in response.");
			Assert.assertEquals(json.get("error").toString(), "invalid_grant","Invalid error value is displayed in response.");
			Assert.assertTrue(json.containsKey("error_description"), "error_description key is present in response.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials","Invalid error_description value is displayed in response.");
			test.log(LogStatus.PASS, "Check proper validation message is displayed when invalid password is passed.");
		}	
	}	
	
	//@Test(priority=27)
	// Execute oauth/token api method with valid password
	public void oauth_token_with_valid_password() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_valid_password", "To validate whether user is able to get access_token through oauth/token api with valid password");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with valid password");
		System.out.println("Execute oauth/token api method with valid password");
		String[] header = {"application/json",""};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", "lmcsuper@logmycalls.com");
		parameters.put("password", "ScottIsDaMan");
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
		}	
	}
	
	//@Test(priority=28)
	// Execute oauth/token api method with agency admin credential
	public void oauth_token_with_agency_admin_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_agency_admin_credential", "To validate whether user is able to get access_token through oauth/token api with agency admin credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with agency admin credential");
		System.out.println("Execute oauth/token api method with agency admin credential");
		String[] header = {"application/json",""};
		String[] agency_admin_crd = {"lmcsuper@logmycalls.com","ScottIsDaMan"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", agency_admin_crd[0]);
		parameters.put("password", agency_admin_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
			String access_token = json.get("access_token").toString();
			
			// Check user is able to access api with agency admin access_token
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			test.log(LogStatus.INFO, "Execute campaign/list api method with agency admin access_token");
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			line = "";
			while ((line = rd.readLine()) != null) {
			   json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "success", "API is returning failure when user access api with agency admin access_token");
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns 100 record by default
			   Assert.assertEquals(array.size(), 100, "Campaign list does not return 100 records");
			}   
		}	
	}
	
	//@Test(priority=29)
	// Execute oauth/token api method with agency admin credential
	public void oauth_token_with_company_admin_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_company_admin_credential", "To validate whether user is able to get access_token through oauth/token api with company admin credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with company admin credential");
		System.out.println("Execute oauth/token api method with company admin credential");
		String[] header = {"application/json",""};
		String[] company_admin_crd = {"companyautomation@admin.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", company_admin_crd[0]);
		parameters.put("password", company_admin_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
			String access_token = json.get("access_token").toString();
			
			// Check user is able to access api with company admin access_token
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			test.log(LogStatus.INFO, "Execute campaign/list api method with company admin access_token");
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			line = "";
			while ((line = rd.readLine()) != null) {
			   json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "success", "API is returning failure when user access api with company admin access_token");
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns record when company admin access_token is used
			   Assert.assertTrue(array.size()>1, "Campaign list does not return records with company admin access_token");
			}   
		}	
	}
		
//	@Test(priority=30)
	// Execute oauth/token api method with location admin credential
	public void oauth_token_with_location_admin_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_company_admin_credential", "To validate whether user is able to get access_token through oauth/token api with location admin credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with location admin credential");
		System.out.println("Execute oauth/token api method with location admin credential");
		String[] header = {"application/json",""};
		String[] location_admin_crd = {"locationautomation@admin.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", location_admin_crd[0]);
		parameters.put("password", location_admin_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check fields are present in oauth/token response
			Assert.assertTrue(json.containsKey("access_token"), "access_token field is not present in response");
			test.log(LogStatus.PASS, "Check access_token field is present in response");
			Assert.assertTrue(json.containsKey("refresh_token"), "refresh_token field is not present in response");
			test.log(LogStatus.PASS, "Check refresh_token field is present in response");
			Assert.assertTrue(json.containsKey("expires_in"), "expires_in field is not present in response");
			test.log(LogStatus.PASS, "Check expires_in field is present in response");
			Assert.assertTrue(json.containsKey("status"), "status field is not present in response");
			test.log(LogStatus.PASS, "Check status field is present in response");
			Assert.assertTrue(json.containsKey("token_type"), "token_type field is not present in response");
			test.log(LogStatus.PASS, "Check token_type field is present in response");
			
			// Check fields are not null in /oauth/token api response
			HelperClass.multiple_assertnotEquals(json.get("access_token"), "access_token");
			HelperClass.multiple_assertnotEquals(json.get("refresh_token"), "refresh_token");
			HelperClass.multiple_assertnotEquals(json.get("expires_in"), "expires_in");
			HelperClass.multiple_assertnotEquals(json.get("status"), "status");
			HelperClass.multiple_assertnotEquals(json.get("token_type"), "token_type");
			test.log(LogStatus.PASS, "Check fields are not null in response.");
			
			Assert.assertEquals(json.get("token_type"), "Bearer", "token_type is not Bearer in response");
			test.log(LogStatus.PASS, "Check token_type is Bearer in response");
			Assert.assertEquals(json.get("status"), "success", "status is not success in response");
			test.log(LogStatus.PASS, "Check status is success in response");
			Assert.assertEquals(json.get("expires_in").toString(), "3600", "expires_in is not 3600 in response");
			test.log(LogStatus.PASS, "Check expires_in is 3600 in response");
			String access_token = json.get("access_token").toString();
			
			// Check user is able to access api with location admin access_token
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			response = HelperClass.make_get_request("/v2/campaign/list", access_token, list);
			test.log(LogStatus.INFO, "Execute campaign/list api method with agency admin access_token");
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			line = "";
			while ((line = rd.readLine()) != null) {
			   json = (JSONObject) parser.parse(line);
			   Assert.assertEquals(json.get("result"), "success", "API is returning failure when user access api with location admin access_token");
			   JSONArray array = (JSONArray)json.get("data");
			   // Check whether campaign list returns record when location admin access_token is used
			   Assert.assertTrue(array.size()>1, "Campaign list does not return records with location admin access_token");
			}   
		}	
	}	
	
	//@Test(priority=31)
	// Execute oauth/token api method with agency standard credential
	public void oauth_token_with_agency_standard_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_agency_admin_credential", "To validate whether user is able to get access_token through oauth/token api with agency standard credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with agency standard credential");
		System.out.println("Execute oauth/token api method with agency standard credential");
		String[] header = {"application/json",""};
		String[] agency_standard_crd = {"agency@standard.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", agency_standard_crd[0]);
		parameters.put("password", agency_standard_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when agency standard access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when agency standard access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when agency standard access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when agency standard access_token is used");
		}
	}
	
	//@Test(priority=32)
	// Execute oauth/token api method with agency readonly credential
	public void oauth_token_with_agency_readonly_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_agency_readonly_credential", "To validate whether user is able to get access_token through oauth/token api with agency readonly credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with agency readonly credential");
		System.out.println("Execute oauth/token api method with agency readonly credential");
		String[] header = {"application/json",""};
		String[] agency_readonly_crd = {"agency@readonly.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", agency_readonly_crd[0]);
		parameters.put("password", agency_readonly_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when agency readonly access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when agency readonly access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when agency readonly access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when agency readonly access_token is used");
		}
	}	
	
	//@Test(priority=33)
	// Execute oauth/token api method with company standard credential
	public void oauth_token_with_company_standard_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_company_admin_credential", "To validate whether user is able to get access_token through oauth/token api with company standard credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with company standard credential");
		System.out.println("Execute oauth/token api method with company standard credential");
		String[] header = {"application/json",""};
		String[] company_standard_crd = {"company@standard.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", company_standard_crd[0]);
		parameters.put("password", company_standard_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when company standard access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when company standard access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when company standard access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when company standard access_token is used");
		}
	}
	
	//@Test(priority=34)
	// Execute oauth/token api method with company readonly credential
	public void oauth_token_with_company_readonly_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_company_readonly_credential", "To validate whether user is able to get access_token through oauth/token api with company readonly credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with company readonly credential");
		System.out.println("Execute oauth/token api method with company readonly credential");
		String[] header = {"application/json",""};
		String[] company_readonly_crd = {"company@readonly.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", company_readonly_crd[0]);
		parameters.put("password", company_readonly_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when company readonly access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when company readonly access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when company readonly access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when company readonly access_token is used");
		}
	}
	
	//@Test(priority=35)
	// Execute oauth/token api method with location standard credential
	public void oauth_token_with_location_standard_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_company_admin_credential", "To validate whether user is able to get access_token through oauth/token api with location standard credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with location standard credential");
		System.out.println("Execute oauth/token api method with location standard credential");
		String[] header = {"application/json",""};
		String[] location_standard_crd = {"locationstandard@yopmail.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", location_standard_crd[0]);
		parameters.put("password", location_standard_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when location standard access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when location standard access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when location standard access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when location standard access_token is used");
		}
	}
	
	//@Test(priority=36)
	// Execute oauth/token api method with location readonly credential
	public void oauth_token_with_location_readonly_credential() throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		test = extent.startTest("oauth_token_with_location_readonly_credential", "To validate whether user is able to get access_token through oauth/token api with location readonly credential");
		test.assignCategory("CFA GET /oauth/token API");
		test.log(LogStatus.INFO, "Execute oauth/token api method with location readonly credential");
		System.out.println("Execute oauth/token api method with location readonly credential");
		String[] header = {"application/json",""};
		String[] location_readonly_crd = {"locationreadonly@yopmail.com","lmc2demo"};
		// Add parameter and its value to oauth/token
		JSONObject parameters = new JSONObject();
		parameters.put("grant_type", "password");
		parameters.put("client_id", "system");
		parameters.put("client_secret", "f558ba166258089b2ef322c340554c");
		parameters.put("username", location_readonly_crd[0]);
		parameters.put("password", location_readonly_crd[1]);
		StringEntity input = new StringEntity( parameters.toString());
		CloseableHttpResponse response = HelperClass.make_post_request_with_header("/oauth/token", access_token, header, input);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(line);
			// Check whether error is displayed when location readonly access_token is used
			Assert.assertEquals(json.get("error").toString(), "invalid_grant", "Invalid error value is displayed in response when location readonly access_token is displayed.");
			Assert.assertEquals(json.get("error_description").toString(), "Invalid resource owner credentials", "Invalid error_description value is displayed in response when location readonly access_token is displayed.");
			test.log(LogStatus.PASS, "Check whether error is displayed when location readonly access_token is used");
		}
	}	
}
