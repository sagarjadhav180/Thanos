package com.convirza.tests.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.convirza.tests.core.utils.DBCallFlowsUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.Assert;
import common.BaseClass;
import common.HelperClass;
import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataCreationContextWriter;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;
import com.convirza.tests.core.utils.DBCustomSourceUtils;
import com.convirza.tests.core.utils.DBPhoneNumberUtils;
import com.convirza.tests.core.utils.DBUserUtils;
import com.convirza.tests.helper.TestDataYamlHelper;
import com.convirza.tests.pojo.request.success.PhoneNumber;
import com.convirza.tests.pojo.request.success.PostCallflowRequest;
import com.convirza.tests.pojo.request.success.PostCampaignRequest;
import com.convirza.tests.pojo.request.success.PostCustomSourceRequest;
import com.convirza.tests.pojo.request.success.PostGroupRequest;
import com.convirza.tests.pojo.request.success.PostWebhookRequest;
import com.convirza.tests.pojo.request.success.ReserveNumberRequest;
import com.convirza.tests.pojo.response.success.PostGroupResponse;
import com.convirza.tests.selenium.scripts.BrowserInitializer;
import com.convirza.tests.selenium.scripts.Login;
import com.convirza.tests.selenium.scripts.User;
import com.convirza.tests.selenium.utils.WaitExecuter;

public class TestDataPreparation extends BaseClass {
	public static String className = "TestDataPreparation";
	public static ArrayList<String> testData;
	public ObjectMapper mapper = new ObjectMapper();
	public TestDataCreationContextWriter contextWriter = new TestDataCreationContextWriter();
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	YamlWriter yamlWriter = new YamlWriter();
  public Boolean isNumberFromAPI = false;
  
	public void createGroup(String groupHierarchy) throws Exception {
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(groupHierarchy);

		String groupExtId = RandomContentGenerator.getRandomString();
		String groupName = (String) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_NAME);
		Integer billingGroup = (Integer) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.BILLING_ID);
		Integer parentGroup = (Integer) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID);
		Integer topGroupId = (Integer) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.TOP_GROUP_ID);
		String address = (String) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ADDRESS);
		String city = (String) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.CITY);
		String state = (String) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.STATE);
		String zip = (String) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.ZIP);
		String phoneNumber = RandomContentGenerator.createPhoneNumber();
		Integer industryId = (Integer) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.INDUSTRY_ID);

		JSONArray jsonRequest = new JSONArray();

		JSONObject json = new JSONObject();
		json.put(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID, groupExtId);
		json.put(TestDataYamlConstants.GroupConstants.GROUP_NAME, groupName);
		json.put(TestDataYamlConstants.GroupConstants.BILLING_ID, billingGroup);
		json.put(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, parentGroup);
		json.put(TestDataYamlConstants.GroupConstants.TOP_GROUP_ID, topGroupId);
		json.put(TestDataYamlConstants.GroupConstants.ADDRESS, address);
		json.put(TestDataYamlConstants.GroupConstants.CITY, city);
		json.put(TestDataYamlConstants.GroupConstants.STATE, state);
		json.put(TestDataYamlConstants.GroupConstants.ZIP, zip);
		json.put(TestDataYamlConstants.GroupConstants.PHONE_NUMBER, phoneNumber);
		json.put(TestDataYamlConstants.GroupConstants.INDUSTRY_ID, industryId);
		jsonRequest.add(json);

		PostGroupRequest requestObj = mapper.readValue(json.toString(), PostGroupRequest.class);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/group", access_token, jsonRequest);
		Assert.assertTrue(
		    (response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502
		        || !(response.getStatusLine().getStatusCode() == 502) || response.getStatusLine().getStatusCode() == 401),
		    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
		        + response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			PostGroupResponse group = mapper.readValue(line, PostGroupResponse.class);
			String groupId = group.getData().get(0).getData();
//			contextWriter.setValue(className, "createGroup", groupId);
			requestObj.setgroup_id(Integer.parseInt(groupId));
			if (groupHierarchy.equals(Constants.GroupHierarchy.COMPANY)) {
				yamlReader.yamlTestData.putAll(yamlReader.setGroupFieldValue(Constants.GroupHierarchy.LOCATION,
				    TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, Integer.parseInt(groupId)));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
			}
			yamlReader.yamlTestData.putAll(yamlReader.setGroupInfo(groupHierarchy, requestObj.getMapObject()));
			yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
		}
	}

	public void createUser(WebDriver driver, String group) {
		Map<String, String> userDetails = User.createUser(driver, group);
		String id = DBUserUtils.getUserIdByEmail(userDetails.get(TestDataYamlConstants.UserConstants.EMAIL));
		String userExtId = RandomContentGenerator.getRandomString();
		DBUserUtils.updateExtIdUser(id, userExtId);
		userDetails.put(TestDataYamlConstants.UserConstants.ID, id);
		userDetails.put(TestDataYamlConstants.UserConstants.USER_EXT_ID, userExtId);
		TestDataYamlHelper.writeUserData(group, userDetails);
		WaitExecuter.sleep(5000);
	}

	public void createTestData() throws Exception {
			
	  ResetTestData.resetIds(Constants.GroupHierarchy.AGENCY);
	  ResetTestData.resetIds(Constants.GroupHierarchy.COMPANY);
	  ResetTestData.resetIds(Constants.GroupHierarchy.LOCATION);
	  
	  createGroup(Constants.GroupHierarchy.COMPANY);
	  createGroup(Constants.GroupHierarchy.LOCATION);
	  
	  WebDriver driver = BrowserInitializer.initialize("chrome");
	  Login.login(driver);
	  DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

	  createUser(driver, Constants.GroupHierarchy.COMPANY);
	  createUser(driver, Constants.GroupHierarchy.LOCATION);
	  createCampaign(Constants.GroupHierarchy.AGENCY);
	  createCampaign(Constants.GroupHierarchy.COMPANY);
	  createCampaign(Constants.GroupHierarchy.LOCATION);
	  
	  createCallflow(Constants.GroupHierarchy.AGENCY);
	  createCallflow(Constants.GroupHierarchy.COMPANY);
	  createCallflow(Constants.GroupHierarchy.LOCATION);
	  createWebhook(Constants.GroupHierarchy.AGENCY);
	  createWebhook(Constants.GroupHierarchy.COMPANY);
	  createWebhook(Constants.GroupHierarchy.LOCATION);
	  createCustomSource(Constants.GroupHierarchy.AGENCY);
	  createCustomSource(Constants.GroupHierarchy.COMPANY);
	  createCustomSource(Constants.GroupHierarchy.LOCATION);
	  createBlacklistedNumber(Constants.GroupHierarchy.AGENCY);
	  
	  driver.close();
		 
		setConfig();
	}
	
	public void setConfig() {
		Map<String,String> map = new HashMap<String, String>();
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
		String user = confUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		map.put("company_admin_email", user);
		confUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
		user = confUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		map.put("location_admin_email", user);
		HelperClass.write_config(map);
	}

	public void createCampaign(String group) throws Exception {
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);

		String campaignName = (String) confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME);
		String campaignExtId = RandomContentGenerator.createUUid();
		String campaignStatus = (String) confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_STATUS);
		String todayDate = DateUtils.getTodayDate();
		String futureDate = DateUtils.getFutureDate();

		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(group);
		Integer campaignUser = Integer.parseInt(confUserHierarchy.get(TestDataYamlConstants.UserConstants.ID).toString());
		ArrayList<Integer> campaignUsers = new ArrayList<Integer>();
		campaignUsers.add(campaignUser);

		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Integer campaignGroup = (Integer) confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID);

		JSONArray jsonRequest = new JSONArray();
		JSONObject json = new JSONObject();
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME, campaignName);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID, campaignExtId);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_CREATED, todayDate);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_STATUS, campaignStatus);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_MODIFIED, todayDate);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_START_DATE, todayDate);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_END_DATE, futureDate);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_OWNER_USER_ID, campaignUser);
		json.put(TestDataYamlConstants.CampaignConstants.GROUP_ID, campaignGroup);
		json.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_USERS, campaignUsers);
		jsonRequest.add(json);

		PostCampaignRequest requestObj = mapper.readValue(json.toString(), PostCampaignRequest.class);

		CloseableHttpResponse response = HelperClass.make_post_request("/v2/campaign", access_token, jsonRequest);
		Assert.assertTrue(
		    !(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401),
		    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
		        + response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONArray json_array_response = (JSONArray) parser.parse(line);
			JSONObject json_response = (JSONObject) json_array_response.get(0);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error while creating campaign.");
			int campaignId = Integer.parseInt(json_response.get("data").toString());
			requestObj.setCampaign_id(campaignId);
		}
		yamlReader.yamlTestData.putAll(yamlReader.setCampaignInfo(group, requestObj.getMapObject()));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}

	public void createCallflow(String group) {
		isNumberFromAPI = false;
		String[] number = getNumberForCallFlow(group);
		System.out.println("Number: " + number);
		System.out.println(Long.valueOf(number[0]));
		System.out.println(Long.valueOf(number[1]));
		if (isNumberFromAPI)
			reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowInfo(group);

		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setRecord_until(DateUtils.getFutureDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));

		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));

		postCallflowRequest.setNumber(phoneNumber);

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
		map.remove("isSimultaneous");
		JSONArray callflowReq = new JSONArray();
		JSONObject callflowReqObj = new JSONObject(map);
		callflowReq.add(callflowReqObj);

		CloseableHttpResponse response;

		try {
			response = HelperClass.make_post_request("/v2/callflow", access_token, callflowReq);
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) parser.parse(line);
				String result_data = jsonResponse.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow. API response: " + line);
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		yamlReader.yamlTestData.putAll(yamlReader.setCallflowInfo(group, postCallflowRequest.getMapObject()));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}

	public void createWebhook(String group) {
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confWebhookHierarchy = yamlReader.readWebhookInfo(group);

		PostWebhookRequest postWebhookRequest = mapper.convertValue(confWebhookHierarchy, PostWebhookRequest.class);
		postWebhookRequest.setOrg_unit_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));

		Map<String, Object> map = mapper.convertValue(postWebhookRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null))
			;
		JSONObject webhookReqObj = new JSONObject(map);

		CloseableHttpResponse response;

		try {
			response = HelperClass.make_post_request("/v2/webhook", access_token, webhookReqObj);
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) parser.parse(line);
				String result_data = jsonResponse.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow.");
				JSONObject dataResult = (JSONObject) jsonResponse.get("data");
				Integer webhookId = Integer.parseInt(dataResult.get("insertId").toString());
				postWebhookRequest.setWebhook_id(webhookId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		yamlReader.yamlTestData.putAll(yamlReader.setWebhookInfo(group, postWebhookRequest.getMapObject()));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}

	public void createCustomSource(String group) throws Exception {
		Map<String, Object> confCustomSourceHierarchy = yamlReader.readCustomSourceInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		PostCustomSourceRequest postCSRequest = mapper.convertValue(confCustomSourceHierarchy,
		    PostCustomSourceRequest.class);
		postCSRequest.setOrg_unit_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		Map<String, Object> map = mapper.convertValue(postCSRequest, new TypeReference<Map<String, Object>>() {
		});
		map.remove("mapObject");
		DBCustomSourceUtils.setCustomSourceStatus(postCSRequest.getCustom_source_name(), yamlReader.readGroupInfo("agency").get("group_id").toString(), "false");
		while (map.values().remove(null));
		JSONArray customSourceArrayObj = new JSONArray();
		JSONObject customSourceReqObj = new JSONObject(map);
		customSourceArrayObj.add(customSourceReqObj);
		CloseableHttpResponse response;
		try {
			response = HelperClass.make_post_request("/v2/customsource", access_token, customSourceArrayObj);
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) parser.parse(line);
				String result_data = jsonResponse.get("result").toString();
				Assert.assertEquals(result_data, "success", "API is returning error while creating customsource. API response: " + line);
				int customSourceId = DBCustomSourceUtils.getCustomSourceId(postCSRequest.getCustom_source_name(), String.valueOf(postCSRequest.getOrg_unit_id()));
				postCSRequest.setCustom_source_id(customSourceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		yamlReader.yamlTestData.putAll(yamlReader.setCustomSourceInfo(group, postCSRequest.getMapObject()));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());

	}

	public String[] getNumberForCallFlow(String group) {
		String[] numberDetails = null;
		if ((numberDetails = getReservedNumber()) != null) {
		} if ((numberDetails = DBPhoneNumberUtils.getNumberByStatus("unprovisioned")) != null) {
		} else if ((numberDetails = DBPhoneNumberUtils.getNumberByStatus("suspended")) != null) {
			DBPhoneNumberUtils.setNumberStausByNumber(numberDetails[1], "unprovisioned");
		} else {
			numberDetails = getNumberFromAPI();
			isNumberFromAPI = true;
		}
		return numberDetails;
	}

	public void reserveNumberFromAPI(Long numberId, Long number) {
		ReserveNumberRequest reserveNumberObj = new ReserveNumberRequest();
		reserveNumberObj.setNumber(number);
		reserveNumberObj.setNumber_id(numberId);
		reserveNumberObj.setSource(10001l);
		Map<String, Object> map = mapper.convertValue(reserveNumberObj, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
		JSONArray reserveNumberReq = new JSONArray();
		JSONObject reserveNumberJson = new JSONObject(map);
		reserveNumberReq.add(reserveNumberJson);
		CloseableHttpResponse response;

		try {
			response = HelperClass.make_post_request("/v2/number/reserve", access_token, reserveNumberReq);
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase()
			        + "Request: " + reserveNumberReq);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getReservedNumber() {
		CloseableHttpResponse response;
		List<String> numberDetails = new ArrayList<String>();
		try {
			response = HelperClass.make_get_request("/v2/number/reserved", access_token, new ArrayList<String>());
			Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject) parser.parse(line);
				JSONArray numberList = (JSONArray) result.get("data");
				if (numberList.size() > 0) {
					JSONObject firstRecord = (JSONObject) numberList.get(0);
					numberDetails.add(firstRecord.get("number_id").toString());
					numberDetails.add(firstRecord.get("number").toString());
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		String[] numDetails = Arrays.copyOf(numberDetails.toArray(), numberDetails.toArray().length, String[].class);
		return numDetails;
	}

	public String[] getNumberFromAPI() {
		CloseableHttpResponse response;
		String[] numberDetails = null;
		try {
			response = HelperClass.make_get_request("/v2/number/search", access_token, new ArrayList());
			Assert.assertTrue(
			    !(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject) parser.parse(line);
				JSONArray numberArray = (JSONArray)result.get("data");
				if (numberArray.size() > 0) {
					JSONObject numberObj = (JSONObject) numberArray.get(0);
					String numberId = (String) numberObj.get("number_id");
					String number = (String) numberObj.get("number");
					return new String[] { numberId, number };
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numberDetails;
	}
	
	public void createBlacklistedNumber(String group) throws UnsupportedOperationException, IOException {

		JSONObject json_obj = new JSONObject();
		Long number;
		number=Long.parseLong(RandomContentGenerator.createPhoneNumber());
		
		json_obj.put("number", number);
		CloseableHttpResponse response = null; 
		
		try {
			response = HelperClass.make_post_request("/v2/blacklistednumber", access_token, json_obj);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		
		Assert.assertTrue(
			    (response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 502
			        || !(response.getStatusLine().getStatusCode() == 502) || response.getStatusLine().getStatusCode() == 401),
			    "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
			        + response.getStatusLine().getReasonPhrase());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
			Map<String, Object> blacklistedNumbers =new HashMap<String, Object>();
			blacklistedNumbers.put("number", number);
			yamlReader.yamlTestData.putAll(yamlReader.setBlacklistNumberInfo(group, blacklistedNumbers));
			yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
			
	}
}
