package call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.relevantcodes.extentreports.LogStatus;

import common.BaseClass;
import common.HelperClass;
import common.Listener;

@SuppressWarnings("unchecked")
@Listeners(Listener.class)
public class BulkCallUpload extends BaseClass{

	

	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String class_name="PostCallUpload";
	ArrayList<String> testdata;
	String caller_id,ring_to ,disposition,call_date,file,org_unit_id,group_ext_id,channel_id,campaign_id,tracking_number,line_type,assign_to;
	String custom_source_type_1 ,custom_source_type_2,custom_source_type_3,custom_source_type_4,custom_source_type_5,company_name;
	String city,zip_code,caller_name,address,state,swap_channels,is_outbound,tag_name;

	JSONObject payload=new JSONObject();
	String[] fields= {"tracking_number","caller_id","ring_to" ,"disposition","call_date","file","group_id","group_ext_id","channel_id","campaign_id","line_type","assign_to","custom_source_type_1","custom_source_type_2","custom_source_type_3","custom_source_type_4","custom_source_type_5","company_name","city","zip_code","caller_name","address","state","swap_channels","is_outbound","tag_name"};	
	
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public void setUpForCallUpload() throws IOException, ParseException {
		testdata = HelperClass.readTestData(class_name, "uploadCallWithValidCallDate");
		caller_id = testdata.get(1);
		payload.put("caller_id", caller_id);
		ring_to = testdata.get(2);
		payload.put("ring_to", ring_to);
		disposition = testdata.get(3);
		payload.put("disposition", disposition);
		line_type=testdata.get(11);
		payload.put("line_type", line_type);
		swap_channels=testdata.get(24);
		payload.put("swap_channels", swap_channels);
		is_outbound=testdata.get(25);
		payload.put("is_outbound", is_outbound);
		company_name=testdata.get(18);
		payload.put("company_name", company_name);
		tag_name=testdata.get(26);
		payload.put("tag_name", tag_name);
		caller_name=testdata.get(21);
		payload.put("caller_name", caller_name);
		custom_source_type_1=testdata.get(13);
		payload.put("custom_source_type_1", custom_source_type_1);
		custom_source_type_2=testdata.get(14);
		payload.put("custom_source_type_2", custom_source_type_2);		
		custom_source_type_3=testdata.get(15);
		payload.put("custom_source_type_3", custom_source_type_3);
		custom_source_type_4=testdata.get(16);
		payload.put("custom_source_type_4", custom_source_type_4);
		custom_source_type_5=testdata.get(17);
		payload.put("custom_source_type_5", custom_source_type_5);
		call_date = DateUtils.getDate(FileConstants.callUploadDateFormat(), "-1");
		payload.put("call_date", call_date);
		file=FileConstants.getMP3File("1mb");
		payload.put("file", file);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		org_unit_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		payload.put("group_id", org_unit_id);
//		group_ext_id=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID).toString();
		group_ext_id=RandomContentGenerator.getRandomString();
		payload.put("group_ext_id", group_ext_id);
		
		city=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.CITY).toString();
		payload.put("city", city);
		zip_code=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ZIP).toString();
		payload.put("zip_code", zip_code);
		address=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ADDRESS).toString();
		payload.put("address", address);
		state=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.STATE).toString();
		payload.put("state", state);
//		tracking_number=confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.PHONE_NUMBER).toString();
//		payload.put("tracking_number", tracking_number);
		Map<String, Object> confTNHierarchy = yamlReader.readCallflowInfo(Constants.GroupHierarchy.COMPANY);
		channel_id=confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.CHANNEL_ID).toString();
		payload.put("channel_id", channel_id);
		Map tn_obj= (Map) confTNHierarchy.get(TestDataYamlConstants.CallflowConstants.NUMBER);
		tracking_number=tn_obj.get("phone_number").toString();
		payload.put("tracking_number", tracking_number);
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(Constants.GroupHierarchy.COMPANY);
		campaign_id=confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString();
		payload.put("campaign_id", campaign_id);
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
		assign_to=confUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		payload.put("assign_to", assign_to);
	}
	
	public JSONObject createPayload(String field,String value) {
		
		JSONObject json = new JSONObject();
	
		for(String field1:fields) {
			
			if(field1.equals(field)){
				json.put(field1, value);
			}
			else {
				if(field1.equals("zip_code") || field1.equals("campaign_id")) {
					json.put(field1, Integer.parseInt(payload.get(field1).toString()));
				}
				else if (field1.equals("swap_channels") || field1.equals("is_outbound")){
					json.put(field1,Boolean.getBoolean(payload.get(field1).toString()));
				}
				else {
					json.put(field1, payload.get(field1));					
				}
			}
		}

//		
//		json.put("caller_name", caller_name);
//		json.put("custom_source_type_1", custom_source_type_1);
//		json.put("custom_source_type_2", custom_source_type_2);
//		json.put("custom_source_type_3", custom_source_type_3);
//		json.put("custom_source_type_4", custom_source_type_4);
//		json.put("custom_source_type_5", custom_source_type_5);
//		json.put("city", city);
//		json.put("address", address);
//		json.put("zip_code", Integer.parseInt(zip_code));
//		json.put("state", state);
//		json.put("channel_id", channel_id);
//		json.put("tracking_number", tracking_number);
//		json.put("campaign_id", Integer.parseInt(campaign_id));
//		json.put("assign_to", assign_to);
//		json.put("file", file);
//		json.put("call_date", call_date);
//
//		json.put("caller_id", caller_id);
//		json.put("group_id", org_unit_id);
//		json.put("ring_to", ring_to);
//		json.put("group_ext_id", group_ext_id);
//		json.put("disposition", disposition);
//		json.put("line_type", line_type);
//		json.put("swap_channels", Boolean.getBoolean(swap_channels));
//		json.put("is_outbound", Boolean.getBoolean(is_outbound));
//		json.put("company_name", company_name);
//		json.put("tag_name", tag_name);
		
		return json;
		
	}
	
	
	@Test(invocationCount=100)
	public void uploadCalls() throws ClientProtocolException, IOException, URISyntaxException, ParseException {
	

		test = extent.startTest("uploadCallWithValidCallDate", "Verify success message displayed if user pass valid Date/Time in request");
		test.assignCategory("CFA POST /Call Upload API");
		JSONObject json = new JSONObject();
		json=createPayload("","");
		
		CloseableHttpResponse response = HelperClass.make_post_request_call_upload("/v2/call/upload", access_token, json);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 ), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
	    
		while ((line = rd.readLine()) != null) {
			   // Convert response to JSON object
			   JSONParser parser = new JSONParser();
			   JSONObject jsonobj = (JSONObject) parser.parse(line); 
			   String success_message = jsonobj.get("result").toString();
		
			   test.log(LogStatus.INFO, "Verifying if success message is displayed");	   
			   String result = jsonobj.get("result").toString();
			   String exp_result="success";
			   
			   Assert.assertEquals(result, exp_result,"Result did not return success message");
			   Assert.assertNull(jsonobj.get("err"));
			   
		}
		
	}

	
}
