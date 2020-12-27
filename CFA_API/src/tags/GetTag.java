package tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallUtils;
import com.convirza.tests.core.utils.DBGroupUtils;
import com.convirza.tests.core.utils.DBTagUtils;
import com.convirza.utils.TagsData;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class GetTag extends BaseClass{
	
	String class_name="GetTag";
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	ArrayList<String> testdata;
	String access_token_company_admin;
	String access_token_location_admin;
	String call_id_loc_level;
	String call_id_comp_level;
	String call_id_agency_level;
	String tag_loc_level;
	String tag_comp_level;
	String tag_agency_level;
	Map<String,String> filter_map = new HashMap<String,String>();
	
	@BeforeClass
	public void setParams() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		List data = TagsData.setParams();
		Map<String,String> call_id_map = (Map<String, String>) data.get(0);
		Map<String,String> access_tokens_map = (Map<String, String>) data.get(1);
		Map<String,String> tags_map = (Map<String, String>) data.get(2);
		access_token_company_admin = access_tokens_map.get("access_token_company_admin");
		access_token_location_admin = access_tokens_map.get("access_token_location_admin");
		call_id_agency_level = call_id_map.get("call_id_agency_level");
		call_id_comp_level = call_id_map.get("call_id_comp_level");
		call_id_loc_level = call_id_map.get("call_id_loc_level");
		tag_agency_level = tags_map.get("agency_tag");
		tag_comp_level = tags_map.get("comp_tag");
		tag_loc_level = tags_map.get("loc_tag");		

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
	
	@Test(priority=1)
    public void getTagDBComparison() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagByAgncyAdmin", "To Verify that the list of all tags under that group & its subgroup is displayed.");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagByAgncyAdmin");
		String limit = testdata.get(1);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit",limit));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   					
			   List<Integer> tag_ids_from_api=new ArrayList<Integer>();
			   List<Integer> tag_ids_from_db=new ArrayList<Integer>();
			   JSONArray data=(JSONArray) json.get("data");
			   for(int i=0;i<data.size();i++) {
				   JSONObject jo=(JSONObject) data.get(i);
				   String tag=jo.get("tag_id").toString();
				   tag_ids_from_api.add(Integer.parseInt(tag));
			   }
			   Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
				String org_unit_id = confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
			   tag_ids_from_db=DBTagUtils.getTagIds("",org_unit_id);
			   
			   test.log(LogStatus.INFO, "Verifying if tags dispalyed in response are matching with tags from db");
			   
			   Collections.sort(tag_ids_from_db);
			   Collections.sort(tag_ids_from_api);
			   boolean flag = tag_ids_from_db.equals(tag_ids_from_api);
			   Assert.assertEquals("true", String.valueOf(flag),"tags dispalyed in response are not matching eith tags from db");	   
		}
		
	}
	
	
	@Test(priority=2)
    public void getTagWithValidDateFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithValidDateFilter", "To Verify that the list of all tags when valid date filter is passed");
		test.assignCategory("CFA GET /tag API");
		String tag_created = null;
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   	
			   JSONArray data=(JSONArray) json.get("data");
			   JSONObject tag_detail=(JSONObject) data.get(0);
			   tag_created=tag_detail.get("tag_created").toString();
		}
		
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("filter","tag_created%3d"+tag_created));
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/tag", access_token, nvps1);
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
			   	
			   JSONArray data=(JSONArray) json.get("data");
			   JSONObject tag_detail=(JSONObject) data.get(0);
			   String tag_created_after_filer = tag_detail.get("tag_created").toString();
			   Assert.assertEquals(tag_created_after_filer, tag_created,"API is not returning data after applying Filter for tag_created");
		}
	}
	
	
	@Test(priority=3)
    public void getTagWithFilterForTagIdNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdNull", "To Verify that no records found when null is passed in tag_id filter");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when null is passed in tag_id filter");
		}
		
	}
	
	
	@Test(priority=4)
    public void getTagWithFilterForTagIdBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdBlank", "To Verify that no records found when blank is passed in tag_id filter");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+""));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data.","Appropriate mesassge not dispalyed when blank is passed in tag_id filter");
		}
		
	}
	
	
	@Test(priority=5)
    public void getTagWithFilterForTagIdInvalid() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdBlank", "To Verify that no records found when invalid id is passed in tag_id filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagIdInvalid");
		String tagId = testdata.get(2);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+tagId));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data object");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when invalid id is passed in tag_id filter");
		}
		
	}
	
	
	@Test(priority=6)
    public void getTagWithFilterForTagIdAlphabets() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdAlphabets", "To Verify that no records found when aplhabets are passed in tag_id filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagIdAlphabets");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+testdata.get(2)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data","Appropriate mesassge not dispalyed when aplphabets are passed in tag_id filter");
		}
		
	}

	
	@Test(priority=7)
    public void getTagWithFilterForTagIdSpecialChars() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdSpecialChars", "To Verify that no records found when special characters are passed in tag_id filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagIdSpecialChars");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+testdata.get(2)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data","Appropriate mesassge not dispalyed when special characters are passed in tag_id filter");
		}
		
	}
	
	
	@Test(priority=8)
    public void getTagWithFilterForTagIdFromAnotherBilling() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdFromAnotherBilling", "To Verify that no records found when tag_id is passed from another billing");
		test.assignCategory("CFA GET /tag API");
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tagId =DBTagUtils.getTagIdFromANotherBilling(group_id);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+tagId));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_id from another billing is passed");
		}
		
	}
	
	
	@Test(priority=9)
    public void getTagWithFilterForTagIdPassOUID() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagIdPassOUID", "To Verify that no records found when tag_ou_id is passed in tag_id ");
		test.assignCategory("CFA GET /tag API");
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
	
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+group_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_ouid is passed in tag_id");
		}
		
	}
	
	
	@Test(priority=10)
    public void getTagWithFilterForTagNameNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagNameNull", "To Verify that no records found when null is passed in tag_name filter ");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_name is passed null");
		}
		
	}
	
	
	@Test(priority=11)
    public void getTagWithFilterForInvalidTagName() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagNameNull", "To Verify that no records found when invalid value is passed in tag_name filter ");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForInvalidTagName");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+testdata.get(3)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when when invalid value is apssed in tag_name filter");
		}
		
	}
	
	
	@Test(priority=12)
    public void getTagWithFilterForTagNameblank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagNameblank", "To Verify that no records found when blank is passed in tag_name filter");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+""));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data.","Appropriate mesassge not dispalyed when when blank is passed in tag_name filter");
		}
		
	}
	
	
	@Test(priority=13)
    public void getTagWithFilterForTagOUIDNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIDNull", "To Verify that no records found when null is passed in tag_ouid filter ");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_ouid is passed null");
		}
		
	}
	
	
	@Test(priority=14)
    public void getTagWithFilterForTagOUIDblank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIDblank", "To Verify that no records found when blank is passed in tag_ouid filter");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+""));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data.","Appropriate mesassge not dispalyed when when blank is passed in tag_ouid filter");
		}
		
	}
	
	
	@Test(priority=15)
    public void getTagWithFilterForInvalidTagOUID() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForInvalidTagOUID", "To Verify that no records found when invalid value is passed in tag_ouid filter ");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForInvalidTagOUID");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+testdata.get(4)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when when invalid value is apssed in tag_ouid filter");
		}
		
	}
	
	
	@Test(priority=16)
    public void getTagWithFilterForTagOUIDAlphabets() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIDAlphabets", "To Verify that no records found when aplhabets are passed in tag_ouid filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagOUIDAlphabets");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+testdata.get(4)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data","Appropriate mesassge not dispalyed when aplphabets are passed in tag_ouid filter");
		}
		
	}
	
	
	@Test(priority=17)
    public void getTagWithFilterForTagOUIdSpecialChars() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIdSpecialChars", "To Verify that no records found when special characters are passed in tag_ouid filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagOUIdSpecialChars");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+testdata.get(2)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data.","Appropriate mesassge not dispalyed when special characters are passed in tag_ouid filter");
		}
		
	}
	
	
	@Test(priority=18)
    public void getTagWithFilterForTagOUIdFromAnotherBilling() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIdFromAnotherBilling", "To Verify that no records found when tag_ouid is passed from another billing");
		test.assignCategory("CFA GET /tag API");
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		String tagOuId =DBGroupUtils.getOtherBillingGroupId(group_id);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+tagOuId));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_id from another billing is passed");
		}
		
	}
	
	
	@Test(priority=19)
    public void getTagWithFilterForTagOUIdPassTagId() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagOUIdPassTagId", "To Verify that no records found when tag_id is passed instead of tag_ouid");
		test.assignCategory("CFA GET /tag API");
		
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String group_id=confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tagId =DBTagUtils.getTagId("", group_id);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+tagId));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertEquals(json.get("result").toString(), "success","success message not displayed in result");
			   test.log(LogStatus.INFO, "Verifying if err is null");
			   Assert.assertNull(json.get("err"),"api returned err "+json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if appropriate message is displayed in data");
			   Assert.assertEquals(json.get("data").toString(), "No data found","Appropriate mesassge not dispalyed when tag_id is passed instead of tag_ouid");
		}
		
	}
	
	
	@Test(priority=20)
    public void getTagWithFilterForTagCreatedInvalidDateFormat() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithFilterForTagCreatedInvalidDateFormat", "To Verify that appropriate error message displayed when incorrect date format is passed in tag_created filter");
		test.assignCategory("CFA GET /tag API");
		
		testdata=HelperClass.readTestData(class_name, "getTagWithFilterForTagCreatedInvalidDateFormat");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_created%3d"+testdata.get(5)));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is dispalyed");
			   Assert.assertEquals(json.get("err").toString(), "Please provide valid data","Appropriate error message not displayed when incorrect date format is passed in tag_created");
		}
		
	}
	
	
	@Test(priority=0)
    public void getTagRsponseParametersVerification() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagRsponseParametersVerification", "To Verify parameters recieved in response are as per swagger");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit","1"));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));
			   test.log(LogStatus.INFO, "Verifying if data object is displayed");
			   Assert.assertTrue(json.containsKey("data"),"data object is not present");
			   
			   JSONArray data_arr = (JSONArray) json.get("data");
			   String [] tag_fields= {"tag_id","tag_name","tag_created","tag_ouid"};
			  	   
			   for(int i=0;i<tag_fields.length;i++) {
				   test.log(LogStatus.INFO, "Verifying if "+tag_fields[i]+" parameter is present");
				   for(int j=0;j<data_arr.size();j++) {
					   JSONObject json_obj = (JSONObject) data_arr.get(j);
					   Assert.assertTrue(json_obj.containsKey(tag_fields[i]),tag_fields[i]+" is not present");					   
					   filter_map.put(tag_fields[i], json_obj.get(tag_fields[i]).toString());
				   }
			   }
		}
		
	}
	
	
	@Test(priority=22)
    public void getTagWithValidOUIDFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagEithValidOUIDFilter", "To Verify data recieved in response is as per filter applied");
		test.assignCategory("CFA GET /tag API");
		
		String group_id = filter_map.get("tag_ouid");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+group_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));
			   
			   test.log(LogStatus.INFO, "Verifying if all tags displyed are form filtered ou");
			   JSONArray data_arr=(JSONArray) json.get("data");
			   
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject tag_object=(JSONObject) data_arr.get(i);
				   String tag_ouid = tag_object.get("tag_ouid").toString();
				   Assert.assertEquals(tag_ouid,group_id,"tag from "+tag_ouid+" is displayed");
			   }
			   
		}
		
	}
	
	
	@Test(priority=23)
    public void getTagWithValidTagNameFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagEithValidTagNameFilter", "To Verify data recieved in response is as per filter applied");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String tag_name = filter_map.get("tag_name");
		nvps.add(new BasicNameValuePair("filter","tag_name%3d"+tag_name));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));
			   
			   test.log(LogStatus.INFO, "Verifying if all tags displyed are form filtered ou");
			   JSONArray data_arr=(JSONArray) json.get("data");
			   
			   for(int i=0;i<data_arr.size();i++) {
				   JSONObject tag_object=(JSONObject) data_arr.get(i);
				   String tag = tag_object.get("tag_name").toString();
				   Assert.assertEquals(tag,tag_name,"incorrect tag "+tag+" is displayed");
			   }
			   
		}
		
	}
	
	
	@Test(priority=24)
    public void getTagWithMultipleFilters() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithMultipleFilters", "To Verify data recieved in response when multiple filters are applied");
		test.assignCategory("CFA GET /tag API");

		String tag_id = filter_map.get("tag_ouid");
		String group_id = filter_map.get("tag_ouid");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+group_id+",tag_id%3d"+tag_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		   
		}
		
	}
	
	
	@Test(priority=25)
    public void getTagAccessVerificationCompanyLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagAccessVerificationCompanyLevel", "To Verify tag added by Agnecy level admin is not visible to company level admin.");
		test.assignCategory("CFA GET /tag API");

		List<String> tags_by_agency_Admin=new ArrayList<String>();
		List<String> tags_by_company_Admin=new ArrayList<String>();
		
		//Get tags from added by agency level admin
		String group_id = filter_map.get("tag_ouid");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+group_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		   
			   
			   JSONArray data_arr=(JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   String tag_id = data_arr.get(i).toString();
				   tags_by_agency_Admin.add(tag_id);
			   }
		}
		
		//Get tags from Company level admin
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/tag", access_token_company_admin, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
	    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		   
			   
			   JSONArray data_arr=(JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   String tag_id = data_arr.get(i).toString();
				   tags_by_company_Admin.add(tag_id);
			   }
		}
		
		//compare tags from agnecy and company admin
		Collections.sort(tags_by_agency_Admin);
		Collections.sort(tags_by_company_Admin);
		test.log(LogStatus.INFO, "Verify if Tags added by Agnecy level admin are not visible to Company level admin");
		boolean flag = tags_by_agency_Admin.equals(tags_by_company_Admin);
		
		Assert.assertEquals("false", String.valueOf(flag),"Tags added by Agnecy level admin are visible to Company level admin");
	}
	
	
	@Test(priority=26)
    public void getTagAccessVerificationLocationLevel() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagAccessVerificationLocationLevel", "To Verify tag added by Agnecy level admin is not visible to Location level admin.");
		test.assignCategory("CFA GET /tag API");

		List<String> tags_by_agency_Admin=new ArrayList<String>();
		List<String> tags_by_location_Admin=new ArrayList<String>();
		
		//Get tags from added by agency level admin
		String group_id = filter_map.get("tag_ouid");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter","tag_ouid%3d"+group_id));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		   
			   
			   JSONArray data_arr=(JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   String tag_id = data_arr.get(i).toString();
				   tags_by_agency_Admin.add(tag_id);
			   }
		}
		
		//Get tags from location level admin
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/tag", access_token_location_admin, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
	    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		   
			   
			   JSONArray data_arr=(JSONArray) json.get("data");
			   for(int i=0;i<data_arr.size();i++) {
				   String tag_id = data_arr.get(i).toString();
				   tags_by_location_Admin.add(tag_id);
			   }
		}
		
		//compare tags from agnecy and company admin
		Collections.sort(tags_by_agency_Admin);
		Collections.sort(tags_by_location_Admin);
		test.log(LogStatus.INFO, "Verify if Tags added by Agnecy level admin are not visible to Company level admin");
		boolean flag = tags_by_agency_Admin.equals(tags_by_location_Admin);
		
		Assert.assertEquals("false", String.valueOf(flag),"Tags added by Agnecy level admin are visible to Location level admin");
	}
	
	
	@Test(priority=27)
    public void getTagWithLimitBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithLimitBlank", "To Verify that appropriate error message is displayed when blank limit is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit",""));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_message = jo.get("message").toString();
			   String error_name = jo.get("name").toString();		
			   JSONArray path_arr=(JSONArray) jo.get("path");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (limit): Expected type integer but found type string","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if path is empty");
			   Assert.assertEquals(String.valueOf(path_arr.size()), "0","path array is not empty");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is displayed");
			   Assert.assertEquals(error_name, "limit","path array is not empty");
		}
		
	}
	
	
	@Test(priority=28)
    public void getTagWithLimitNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithLimitNull", "To Verify that appropriate error message is displayed when null limit is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("limit",null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_name = jo.get("name").toString();			   
			   String error_message = jo.get("message").toString();
			   JSONArray path_arr=(JSONArray) jo.get("path");
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (limit): Expected type integer but found type string","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if path is empty");
			   Assert.assertEquals(String.valueOf(path_arr.size()), "0","path array is not empty");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is displayed");
			   Assert.assertEquals(error_name, "limit","path array is not empty");
			   
		}
		
	}
	
	
	@Test(priority=29)
    public void getTagWithInvalidLimit() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithInvalidLimit", "To Verify that appropriate error message is displayed when invalid limit is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getTagWithInvalidLimit");
		String limit=testdata.get(1);
		nvps.add(new BasicNameValuePair("limit",limit));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_name = jo.get("name").toString();		
			   String error_message = jo.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (limit): Value failed JSON Schema validation","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is displayed");
			   Assert.assertEquals(error_name, "limit","path array is not empty");
		}
		
	}
	
	
	@Test(priority=30)
    public void getTagWithValidLimitAndValidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithValidLimitAndValidOffset", "To Verify that success message dispalyed when valid limit and offset applied");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getTagWithValidLimitAndValidOffset");
		String limit=testdata.get(1);
		String offset=testdata.get(6);
		String tag_id_in_2nd_obj = null;
		String tag_id_in_1st_obj = null;
		
		nvps.add(new BasicNameValuePair("limit",limit));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		 
			   JSONArray data_arr=(JSONArray) json.get("data");
			   JSONObject data_obj=(JSONObject) data_arr.get(1);
			   tag_id_in_2nd_obj=data_obj.get("tag_id").toString();
			   
		}
		
		List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
		nvps1.add(new BasicNameValuePair("limit",limit));
		nvps1.add(new BasicNameValuePair("offset",offset));
		CloseableHttpResponse response1 = HelperClass.make_get_request("/v2/tag", access_token, nvps1);
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
	    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");
			   Assert.assertTrue(result.equals("success"),"api did not retun success");
			   test.log(LogStatus.INFO, "Verifying if error is null");
			   Assert.assertNull(json.get("err"));		 
			   JSONArray data_arr=(JSONArray) json.get("data");
			   JSONObject data_obj=(JSONObject) data_arr.get(0);
			   tag_id_in_1st_obj=data_obj.get("tag_id").toString();
		}
		
		test.log(LogStatus.INFO, "Verifying if offset is working");
		Assert.assertEquals(tag_id_in_1st_obj, tag_id_in_2nd_obj,"Offset is not working");	
	}
	
	
	@Test(priority=31)
    public void getTagWithOffsetBlank() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithOffsetBlank", "To Verify that appropriate error message is displayed when blank offset is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset",""));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   String error_name = jo.get("name").toString();
			   JSONArray path_arr=(JSONArray) jo.get("path");
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_message = jo.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (offset): Expected type integer but found type string","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if path is empty");
			   Assert.assertEquals(String.valueOf(path_arr.size()), "0","path array is not empty");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is dispalyed");
			   Assert.assertEquals(error_name, "offset","offset is not dispalyed in error name");
		}
		
	}
	
	
	@Test(priority=32)
    public void getTagWithOffsetNull() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithOffsetNull", "To Verify that appropriate error message is displayed when null offset is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("offset",null));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   String error_name = jo.get("name").toString();
			   JSONArray path_arr=(JSONArray) jo.get("path");
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_message = jo.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (offset): Expected type integer but found type string","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if path is empty");
			   Assert.assertEquals(String.valueOf(path_arr.size()), "0","path array is not empty");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is dispalyed");
			   Assert.assertEquals(error_name, "offset","offset is not dispalyed in error name");
		}
		
	}
	
	
	@Test(priority=33)
    public void getTagWithInvalidOffset() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithInvalidOffset", "To Verify that appropriate error message is displayed when invalid offset is passed");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		testdata=HelperClass.readTestData(class_name, "getTagWithInvalidOffset");
		String offset = testdata.get(6);
		nvps.add(new BasicNameValuePair("offset",offset));
		
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line);
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if Validation error is displayed");
			   Assert.assertEquals(message, "Validation errors","Validation errors are not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error code is displayed");
			   JSONArray errors_arr=(JSONArray) json.get("errors");
			   JSONObject jo = (JSONObject) errors_arr.get(0);
			   String error_code = jo.get("code").toString();
			   String error_name = jo.get("name").toString();
			   Assert.assertEquals(error_code, "INVALID_REQUEST_PARAMETER","Appropriate error code not displayed");
			   String error_message = jo.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if appropriate error message is displayed");
			   Assert.assertEquals(error_message, "Invalid parameter (offset): Value failed JSON Schema validation","appropriate error message is not displayed");
			   test.log(LogStatus.INFO, "Verifying if appropriate error name is dispalyed");
			   Assert.assertEquals(error_name, "offset","offset is not dispalyed in error name");
		}
		
	}
	
	
	@Test(priority=34)
    public void getTagWithoutAnyFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithoutAnyFilter", "To Verify that the list of all tags is displayed.");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
	
	@Test(priority=35)
    public void getTagWithTagFilter() throws IOException, URISyntaxException, ParseException {
		
		test = extent.startTest("getTagWithTagFilter", "To Verify that the list of all tags is displayed when tag filter is applied.");
		test.assignCategory("CFA GET /tag API");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String group_id = filter_map.get("tag_ouid");
		int tag_id=DBTagUtils.getTagId("", group_id);
		nvps.add(new BasicNameValuePair("filter","tag_id%3d"+tag_id));
		CloseableHttpResponse response = HelperClass.make_get_request("/v2/tag", access_token, nvps);
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
	
	
	@AfterClass
	public void cleanUpTags() throws UnsupportedOperationException, IOException, ParseException {
		
		TagsData.cleanUpTags();
		
	}
}
