package com.convirza.tests.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.tests.base.TestDataPreparation;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;
import com.convirza.tests.core.utils.DBPhoneNumberUtils;
import com.convirza.tests.pojo.request.success.PhoneNumber;
import com.convirza.tests.pojo.request.success.PostCallflowRequest;
import com.convirza.tests.pojo.request.success.PostIvrRouteCallflowRequest;
import com.convirza.tests.pojo.request.success.PostPoolBasedCallflowRequest;

import common.BaseClass;
import common.HelperClass;

public class CreateCallFlow extends BaseClass {
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	YamlWriter yamlWriter = new YamlWriter();
	public ObjectMapper mapper = new ObjectMapper();
	
	public String createPoolBasedCallflow(String group) {
		String callFlowId = "";
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(Constants.CallFlowCategory.POOL);

		PostPoolBasedCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostPoolBasedCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
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
				callFlowId = DBPhoneNumberUtils.getProvisionRouteIdByName(postCallflowRequest.getCall_flow_name());
				return callFlowId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callFlowId;
	}
	
	public String createHangupCallflow(String group) {
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] number = testDataPreparation.getNumberForCallFlow(group);
		if (testDataPreparation.isNumberFromAPI)
			testDataPreparation.reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowInfo(group);

		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
    postCallflowRequest.setDefault_ringto(Constants.CallFlowCategory.HANGUP);
    
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));

		postCallflowRequest.setNumber(phoneNumber);

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
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
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow.");
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(postCallflowRequest.getCall_flow_id());
	}
	
	public String createGeoCallflow(String group) {
		boolean isReservedNumber = false;
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] number = testDataPreparation.getNumberForCallFlow(group);
		if (!isReservedNumber)
			testDataPreparation.reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(Constants.CallFlowCategory.GEO);

		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
    
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));

		postCallflowRequest.setNumber(phoneNumber);

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
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
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow.");
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(postCallflowRequest.getCall_flow_id());
	}
	
	public String createIvrCallflow(String group) {
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] number = testDataPreparation.getNumberForCallFlow(group);
		if (testDataPreparation.isNumberFromAPI)
			testDataPreparation.reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(Constants.CallFlowCategory.IVR);

		PostIvrRouteCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostIvrRouteCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
    
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));

		postCallflowRequest.setNumber(phoneNumber);

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
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
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow.");
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(postCallflowRequest.getCall_flow_id());
	}
	
	public String createPercentageCallflow(String group) {
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] number = testDataPreparation.getNumberForCallFlow(group);
		if (testDataPreparation.isNumberFromAPI)
			testDataPreparation.reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(Constants.CallFlowCategory.PERCENTAGE);

		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
    postCallflowRequest.setDefault_ringto(Constants.CallFlowCategory.PERCENTAGE);
    
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));

		postCallflowRequest.setNumber(phoneNumber);

		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
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
				Assert.assertEquals(result_data, "success", "API is returning error while creating callflow.");
				JSONArray dataResultArray = (JSONArray) jsonResponse.get("data");
//				JSONObject dataResult = (JSONObject) dataResultArray.get(0);
				postCallflowRequest.setCall_flow_id(Integer.parseInt(DBPhoneNumberUtils.getProvisionRouteIdByNumberId(phoneNumber.getPhone_number_id().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(postCallflowRequest.getCall_flow_id());
	}
}
