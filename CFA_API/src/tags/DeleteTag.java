package tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import com.convirza.tests.core.utils.DBTagUtils;
import com.convirza.utils.TagsData;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class DeleteTag extends BaseClass{
	
	String class_name="DeleteTag";
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

		
	public void createTag(String token, String id) throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
	    test = extent.startTest("PostTag", "Verify success message displayed if Admin User pass valid call ID");
	    test.assignCategory("CFA DELETE /Tag API");

		JSONObject json = new JSONObject();
		List<String> tag_name=new ArrayList<String>();
		tag_name.add("sj123"+RandomContentGenerator.getRandomString());
		int call_id=Integer.parseInt(id);
		json.put("tag_name", tag_name);
		json.put("call_id", call_id);
		
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/call/tag", token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
		
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
			   
		}
    }
	
	
	@Test(priority=1)
	public void DeleteTagForValidTagId() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForValidTagId", "Verify success message is displayed when valid tag id is passed in payload");
		test.assignCategory("CFA DELETE /Tag API");

		//creating tag
		createTag(access_token,call_id_agency_level);
		
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
	public void DeleteTagAtAgencyLevelAddedByLocAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagAtAgencyLevelAddedByLocAdmin", "Verify success message is displayed if agency admin try to delete tag which added by location admin");
		test.assignCategory("CFA DELETE /Tag API");
		
		//creating tag
		createTag(access_token,call_id_loc_level);
		
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
	public void DeleteTagAtCompanyLevelAddedByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagAtCompanyLevelAddedByAgencyAdmin", "Verify if error message is displayed if company admin try to delete tag which added by agency admin");
		test.assignCategory("CFA DELETE /Tag API");
	
		//creating tag
		createTag(access_token,call_id_agency_level);
				
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token_company_admin, json_obj);
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
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   JSONArray err_arr=(JSONArray) json.get("err");
			   String err_message=err_arr.get(0).toString();
			   String exp_err_message="Tag(s) not permissible for deletion : "+tag_id+"";
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err");	   
		}
			
	}
	
	
	@Test(priority=4)
	public void DeleteTagFromAnotherBilling() throws UnsupportedOperationException, IOException, ParseException {

		test = extent.startTest("DeleteTagFromAnotherBilling", "Verify if error message is displayed if agnecy admin try to delete tag from another billing account");
		test.assignCategory("CFA DELETE /Tag API");
	
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagIdFromANotherBilling(org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   JSONArray err_arr=(JSONArray) json.get("err");
			   String err_message=err_arr.get(0).toString();
			   String exp_err_message="Tag(s) not permissible for deletion : "+tag_id+"";
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err");	   
		}
			
	}

	
	@Test(priority=5)
	public void DeleteTagAddedByAgencyAdmin() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagAddedByAgencyAdmin", "Verify success message is displayed when agency admin try delete the tag.");
		test.assignCategory("CFA DELETE /Tag API");
	
		//creating tag
		createTag(access_token,call_id_agency_level);
				
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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

	
	@Test(priority=6)
	public void DeleteTagDBVerification() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagDBVerification", "Verify tag active status has turned into false when tag is deleted");
		test.assignCategory("CFA DELETE /Tag API");
	
		//creating tag
		createTag(access_token,call_id_agency_level);
						
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   Boolean flag = DBTagUtils.getTagStatus(tag_id);
			   Boolean exp_flag= false;
			   test.log(LogStatus.INFO, "Verifying if tag active status has turned into false");
			   Assert.assertTrue(flag.equals(exp_flag),"tag active status has not turned into false");
		}
			
	}

	
	@Test(priority=7)
	public void DeleteTagForMultipleTagIds() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForMultipleTagIds", "Verify able to delete multiple tags");
		test.assignCategory("CFA DELETE /Tag API");
	
		//creating tag
		createTag(access_token,call_id_agency_level);
		createTag(access_token,call_id_agency_level);
						
		//deleting tag
		List<Integer> tags_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		tags_ids = DBTagUtils.getTagIds("sj123",org_unit_id);
		
		List<Integer> tag_ids=new ArrayList<Integer>();
		tag_ids.add(tags_ids.get(0));
		tag_ids.add(tags_ids.get(1));
		
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
	public void DeleteTagVerificationForDefaultTag() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagVerificationForDefaultTag", "Verify if user is not able to delete default tag");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		tag_ids = DBTagUtils.getTagIds("callback",org_unit_id);
		
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   JSONArray err_arr=(JSONArray) json.get("err");
			   String err_message=err_arr.get(0).toString();
			   String exp_err_message="Predefined tag cannot be deleted ";
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err");	   
		}
			
	}

	
	@Test(priority=9)
	public void DeleteTagForAlreadyDeletedTag() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForAlreadyDeletedTag", "Verify error message is displayed when agency admin try delete the already deleted tag.");
		test.assignCategory("CFA DELETE /Tag API");
	
		//creating tag
		createTag(access_token,call_id_agency_level);
				
		//deleting tag
		List<Integer> tag_ids=new ArrayList<Integer>();
		Map<String, Object> confAgencyGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String org_unit_id= confAgencyGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		int tag_id = DBTagUtils.getTagId("", org_unit_id);
		tag_ids.add(tag_id);

		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
		
		//deleting already deleted tag
		JSONObject json_obj1 = new JSONObject();
		json_obj1.put("tag_id", tag_ids);
		
		HttpResponse response1 = null; 
		
		try {
			response1 = HelperClass.make_delete_request("/v2/tag", access_token, json_obj1);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		Assert.assertTrue(!(response1.getStatusLine().getStatusCode() == 500 || response1.getStatusLine().getStatusCode() == 401), "Invalid status code is displayed. "+ "Returned Status: "+response1.getStatusLine().getStatusCode()+" "+response1.getStatusLine().getReasonPhrase());
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		String line1 = "";
	    
		while ((line1 = rd1.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(line1);
			   String result = json.get("result").toString();
			   test.log(LogStatus.INFO, "Verifying if error is displayed");
			   Assert.assertTrue(result.equals("error"),"api did not retun error");
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is displayed");
			   JSONArray err_arr=(JSONArray) json.get("err");
			   String err_message= err_arr.get(0).toString();
			   String exp_err_message= "Tag(s) already deleted : "+tag_id+"";
			   Assert.assertEquals(err_message,exp_err_message,"api did not returned appropriate err");	   
		}
		
	}

	
	@Test(priority=10)
	public void DeleteTagForblankTagId() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForblankTagId", "Verify if appropriate error message is dispalyed if user pass blank tag_id");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<String> tag_ids=new ArrayList<String>();
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation error is displayed");
			   Assert.assertTrue(message.equals("Validation errors"),"api did not retun Validation error");
			   JSONArray err_arr=(JSONArray) json.get("errors");
			   JSONObject err_object=(JSONObject) err_arr.get(0);
			   String err_message= err_object.get("message").toString();
			   String exp_err_message= "Invalid parameter (tag_id): Value failed JSON Schema validation";
			   String err_name= err_object.get("name").toString();
			   String exp_err_name="tag_id";
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err message");	   
			   test.log(LogStatus.INFO, "Verifying if appropriate err name is dispalyed");
			   Assert.assertEquals(err_name,exp_err_name,"api did not return appropriate err name");
		}
			
	}

	
	@Test(priority=11)
	public void DeleteTagForNullTagId() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForNullTagId", "Verify if appropriate error message is dispalyed if user pass null tag_id");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<String> tag_ids=new ArrayList<String>();
		tag_ids.add(null);
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation error is displayed");
			   Assert.assertTrue(message.equals("Validation errors"),"api did not retun Validation error");
			   JSONArray err_arr=(JSONArray) json.get("errors");
			   JSONObject err_object=(JSONObject) err_arr.get(0);
			   String err_message= err_object.get("message").toString();
			   String exp_err_message= "Invalid parameter (tag_id): Value failed JSON Schema validation";
			   String err_name= err_object.get("name").toString();
			   String exp_err_name="tag_id";
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err message");	   
			   test.log(LogStatus.INFO, "Verifying if appropriate err name is dispalyed");
			   Assert.assertEquals(err_name,exp_err_name,"api did not return appropriate err name");
		}
			
	}
	
	
	@Test(priority=12)
	public void DeleteTagForInvalidTagId() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagForInvalidTagId", "Verify if appropriate error message is dispalyed if user pass invalid tag_id");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<String> tag_ids=new ArrayList<String>();
		testdata=HelperClass.readTestData(class_name, "DeleteTagForInvalidTagId");
		tag_ids.add(testdata.get(1));
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation error is displayed");
			   Assert.assertTrue(message.equals("Validation errors"),"api did not retun Validation error");
			   JSONArray err_arr=(JSONArray) json.get("errors");
			   JSONObject err_object=(JSONObject) err_arr.get(0);
			   String err_message= err_object.get("message").toString();
			   String exp_err_message= "Invalid parameter (tag_id): Value failed JSON Schema validation";
			   String err_name= err_object.get("name").toString();
			   String exp_err_name="tag_id";
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err message");	   
			   test.log(LogStatus.INFO, "Verifying if appropriate err name is dispalyed");
			   Assert.assertEquals(err_name,exp_err_name,"api did not return appropriate err name");
		}
			
	}
	
	
	@Test(priority=13)
	public void DeleteTagWithTagIdContainsString() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagWithTagIdContainsString", "Verify if appropriate error message is dispalyed if user pass string instead of int for tag_id");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<String> tag_ids=new ArrayList<String>();
		testdata=HelperClass.readTestData(class_name, "DeleteTagWithTagIdContainsString");
		tag_ids.add(testdata.get(1));
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_id", tag_ids);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation error is displayed");
			   Assert.assertTrue(message.equals("Validation errors"),"api did not retun Validation error");
			   JSONArray err_arr=(JSONArray) json.get("errors");
			   JSONObject err_object=(JSONObject) err_arr.get(0);
			   String err_message= err_object.get("message").toString();
			   String exp_err_message= "Invalid parameter (tag_id): Value failed JSON Schema validation";
			   String err_name= err_object.get("name").toString();
			   String exp_err_name="tag_id";
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err message");	   
			   test.log(LogStatus.INFO, "Verifying if appropriate err name is dispalyed");
			   Assert.assertEquals(err_name,exp_err_name,"api did not return appropriate err name");
		}
			
	}

	
	@Test(priority=14)
	public void DeleteTagWithIncorrectParameterName() throws UnsupportedOperationException, IOException, ParseException, URISyntaxException {

		test = extent.startTest("DeleteTagWithIncorrectParameterName", "Verify if appropriate error message is dispalyed if user pass incorrect parameter name");
		test.assignCategory("CFA DELETE /Tag API");
	
		//deleting tag
		List<String> tag_ids=new ArrayList<String>();
		JSONObject json_obj = new JSONObject();
		json_obj.put("tag_idd", 123);
		
		HttpResponse response = null; 
		
		try {
			response = HelperClass.make_delete_request("/v2/tag", access_token, json_obj);
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
			   String message = json.get("message").toString();
			   test.log(LogStatus.INFO, "Verifying if validation error is displayed");
			   Assert.assertTrue(message.equals("Validation errors"),"api did not retun Validation error");
			   JSONArray err_arr=(JSONArray) json.get("errors");
			   JSONObject err_object=(JSONObject) err_arr.get(0);
			   String err_message= err_object.get("message").toString();
			   String exp_err_message= "Invalid parameter (tag_id): Value failed JSON Schema validation";
			   String err_name= err_object.get("name").toString();
			   String exp_err_name="tag_id";
			   test.log(LogStatus.INFO, "Verifying if appropriate err message is dispalyed");
			   Assert.assertEquals(err_message,exp_err_message,"api did not return appropriate err message");	   
			   test.log(LogStatus.INFO, "Verifying if appropriate err name is dispalyed");
			   Assert.assertEquals(err_name,exp_err_name,"api did not return appropriate err name");
		}
			
	}
	
}
