package com.convirza.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.DateUtils;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.base.TestDataPreparation;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBPhoneNumberUtils;
import com.convirza.tests.pojo.request.success.Ivrs;
import com.convirza.tests.pojo.request.success.NumberPool;
import com.convirza.tests.pojo.request.success.OverflowNumbers;
import com.convirza.tests.pojo.request.success.PhoneNumber;
import com.convirza.tests.pojo.request.success.PostCallflowRequest;
import com.convirza.tests.pojo.request.success.PostIvrRouteCallflowRequest;
import com.convirza.tests.pojo.request.success.PostPoolBasedCallflowRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import common.BaseClass;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TNUtil extends BaseClass {

	public static String className = "TNUtil";
	public static ArrayList<String> testData;
	public static ObjectMapper mapper = new ObjectMapper();
	public static Boolean isNumberFromAPI = false;
	static TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	@SuppressWarnings("unchecked")
	public static JSONArray createCallflow(String group, String callFlowType, int keypresses, String numbers, String action) {
		
		TestDataPreparation testDataPrep = new TestDataPreparation();
		isNumberFromAPI = false;
		String[] number = testDataPrep.getNumberForCallFlow(group);
		System.out.println("Number: " + number);
		System.out.println(Long.valueOf(number[0]));
		System.out.println(Long.valueOf(number[1]));
		if (isNumberFromAPI)
			testDataPrep.reserveNumberFromAPI(Long.valueOf(number[0]), Long.valueOf(number[1]));
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(callFlowType);

		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setPhone_number_id(Long.valueOf(number[0]));
		phoneNumber.setPhone_number(Long.valueOf(number[1]));
		
		JSONArray callflowReq = new JSONArray();
		switch(callFlowType) {
		case "simple" :
			JSONObject callflowReqObjSimple = new JSONObject();
			callflowReqObjSimple = simpleData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, numbers, action);
			callflowReq.add(callflowReqObjSimple);
			break;
		case "ivr" :
			JSONObject callflowReqObjIvr = new JSONObject();
			callflowReqObjIvr = ivrData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, keypresses, numbers, action);
			callflowReq.add(callflowReqObjIvr);				
			break;
		case "pool" :
			JSONObject callflowReqObjPool = new JSONObject();
			callflowReqObjPool = numberPoolData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, numbers, action);
			callflowReq.add(callflowReqObjPool);				
			break;
		case "percentage" :	
			JSONObject callflowReqObjPercent = new JSONObject();
			callflowReqObjPercent = percentData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, numbers);
			callflowReq.add(callflowReqObjPercent);		
			break;
		case "hangup" : 
			JSONObject callflowReqObjHangup = new JSONObject();
			callflowReqObjHangup = hangupData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, numbers);
			callflowReq.add(callflowReqObjHangup);		
			break;
		case "geo" : 
			JSONObject callflowReqObjGeo = new JSONObject();
			callflowReqObjGeo = geoData(confCampaignHierarchy, confGroupHierarchy, confCallflowHierarchy, phoneNumber, numbers);
			callflowReq.add(callflowReqObjGeo);		
			break;
		
		}
		return callflowReq;
	}
	
	
	public static JSONObject ivrData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber, int keypresses, String numbers, String action) {

		PostIvrRouteCallflowRequest postCallflowRequestIvr = mapper.convertValue(confCallflowHierarchy, PostIvrRouteCallflowRequest.class);
		
		List<Ivrs> ivrs = new ArrayList<Ivrs>();
		for(int i=1;i<=keypresses;i++) {
			Ivrs ivr =new Ivrs();
			ivr.setKey(i);
			ivr.setMessage("test_keypress-"+i);
			ivr.setMessage_enabled(true);
			ivr.setName("Keypress-"+i);
			ivr.setPlay_disclaimer("before");
			ivr.setRecord_enabled(true);
			ivr.setRoute_type("Simple");
			ivr.setTarget_did("8018786943");
			ivrs.add(ivr);
		}
		postCallflowRequestIvr.setIvrs(ivrs);
		postCallflowRequestIvr.setGroup_id(
			    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequestIvr.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequestIvr.setRecord_until(DateUtils.getFutureDate());
		postCallflowRequestIvr.setCampaign_id(
			    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
		postCallflowRequestIvr.setNumber(phoneNumber);
		if(action.startsWith("update")) {
			String[] params = action.split(",");
			postCallflowRequestIvr.setCall_flow_id(Integer.parseInt(params[1]));
			postCallflowRequestIvr.setCall_flow_name("Call flow updated");
		}
		
		Map<String, Object> mapIvr = mapper.convertValue(postCallflowRequestIvr, new TypeReference<Map<String, Object>>() {
			});
		if(action.equals("post"))
			mapIvr.remove("call_flow_id");
		else if(action.startsWith("update"))
			mapIvr.remove("number");			
		while (mapIvr.values().remove(null));	
		JSONObject callflowReqObjIvr = new JSONObject(mapIvr);
		return callflowReqObjIvr;
		
	}
	
	public static JSONObject simpleData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber, String numbers, String action) {
		
		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setRecord_until(DateUtils.getFutureDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
		postCallflowRequest.setNumber(phoneNumber);
		List<OverflowNumbers> overflowNumbers = overflowNumbersData(numbers);
		postCallflowRequest.setOverflowNumbers(overflowNumbers);
		if(action.startsWith("update")) {
			String[] params = action.split(",");
			postCallflowRequest.setCall_flow_id(Integer.parseInt(params[1]));
			postCallflowRequest.setCall_flow_name("Call flow updated");
		}
		
		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		if(action.startsWith("post"))
		   map.remove("call_flow_id");
		else if(action.startsWith("update"))
			  map.remove("number");		
					
		while (map.values().remove(null));	
		JSONObject callflowReqObj = new JSONObject(map);
		return callflowReqObj;
	}
	
	public static JSONObject numberPoolData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber,String numbers, String action) {

		PostPoolBasedCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostPoolBasedCallflowRequest.class);
	    postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setRecord_until(DateUtils.getFutureDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
		Long number = phoneNumber.getPhone_number();
		NumberPool numberPool = poolData(number);
		postCallflowRequest.setNumber_pool(numberPool);
		List<OverflowNumbers> overflowNumbers = overflowNumbersData(numbers);
		postCallflowRequest.setOverflowNumbers(overflowNumbers);
		if(action.startsWith("update")) {
			String[] params = action.split(",");
			postCallflowRequest.setCall_flow_id(Integer.parseInt(params[1]));
			postCallflowRequest.setCall_flow_name("Call flow updated");
		}
		
		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		if(action.equals("post"))
			  map.remove("call_flow_id");
		else if(action.equals("update"))
			  map.remove("numberPool");			
		while (map.values().remove(null));	
		JSONObject callflowReqObj = new JSONObject(map);
		return callflowReqObj;
	}
	
	public static JSONObject geoData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber, String numbers) {
		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
    	postCallflowRequest.setGroup_id(Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
    	postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
    	postCallflowRequest.setRecord_until(DateUtils.getFutureDate());
    	postCallflowRequest.setCampaign_id(Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
		postCallflowRequest.setNumber(phoneNumber);
		List<OverflowNumbers> overflowNumbers = overflowNumbersData(numbers);
		postCallflowRequest.setOverflowNumbers(overflowNumbers);
		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
		JSONObject callflowReqObj = new JSONObject(map);
		return callflowReqObj;
	}
	
    public static JSONObject percentData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber, String numbers) {
    	PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
    	postCallflowRequest.setGroup_id(Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
    	postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
    	postCallflowRequest.setRecord_until(DateUtils.getFutureDate());
    	postCallflowRequest.setCampaign_id(Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
        postCallflowRequest.setNumber(phoneNumber);
    	List<OverflowNumbers> overflowNumbers = overflowNumbersData(numbers);
		postCallflowRequest.setOverflowNumbers(overflowNumbers);
		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
		JSONObject callflowReqObj = new JSONObject(map);
		return callflowReqObj;
	}
	
	public void voicemailData() {
		
	}
	
	public void scheduleData() {
		
	}
	
	public void outboundData() {
		
	}
	
	public static JSONObject hangupData(Map<String, Object> confCampaignHierarchy, Map<String, Object> confGroupHierarchy, Map<String, Object> confCallflowHierarchy, PhoneNumber phoneNumber, String numbers) {
		PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
		postCallflowRequest.setGroup_id(
		    Integer.parseInt(confGroupHierarchy.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString()));
		postCallflowRequest.setUpdated_at(DateUtils.getTodayDate());
		postCallflowRequest.setCampaign_id(
		    Integer.parseInt(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString()));
        postCallflowRequest.setDefault_ringto(Constants.CallFlowCategory.HANGUP);
		postCallflowRequest.setNumber(phoneNumber);
		Map<String, Object> map = mapper.convertValue(postCallflowRequest, new TypeReference<Map<String, Object>>() {
		});
		while (map.values().remove(null));
		JSONObject callflowReqObj = new JSONObject(map);
		return callflowReqObj;
	}
	
	public static List<OverflowNumbers> overflowNumbersData(String numbers) {
		List<OverflowNumbers> overflowNumbers = new ArrayList<OverflowNumbers>();
		
		switch(numbers) {
		case "unique" :
			for(int i=1;i<=5;i++) {
				OverflowNumbers overflowNumber =new OverflowNumbers();
				Long number = Long.parseLong(RandomContentGenerator.createPhoneNumber());
				overflowNumber.setOverflowNumber(number);
				overflowNumber.setRings(i);
				overflowNumbers.add(overflowNumber);
			}
			break;
		case "duplicate":
			OverflowNumbers overflowNumber =new OverflowNumbers();
			for(int i=1;i<=5;i++) {
				Long number = Long.parseLong(RandomContentGenerator.createPhoneNumber());
				overflowNumber.setOverflowNumber(number);
				overflowNumber.setRings(i);
				overflowNumbers.add(overflowNumber);
			}
			break;
		}
		
		return overflowNumbers;
	}
	
	public void dniData() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray removeParams(JSONArray data, String[] fields) {
		
		JSONArray payload = new JSONArray();
		JSONObject jsonobj = (JSONObject) data.get(0);
		for(String field:fields) {
			jsonobj.remove(field);
		}
		payload.add(jsonobj);
		return payload;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray overRideTemplate(JSONArray data, HashMap<String,Object> fields) {
		
		JSONArray payload = new JSONArray();
		JSONObject jsonobj = (JSONObject) data.get(0);
		
		Set<String> keys = fields.keySet();
		Iterator<String> itr = keys.iterator();
		while(itr.hasNext()) {
			removeParams(data, new String[] {itr.next()});
		}
		jsonobj.putAll(fields);
		payload.add(jsonobj);
		return payload;
	}
	
	public static NumberPool poolData(Long number) {
		int npa = DBPhoneNumberUtils.getNPANXX(number, "npa");
		int nxx = DBPhoneNumberUtils.getNPANXX(number, "nxx");
		NumberPool numberPool = new NumberPool();
		numberPool.setNpa(npa);
		numberPool.setNxx(nxx);
		numberPool.setKeep_alive_minutes(60);
		numberPool.setNumber_quantity(2);
		numberPool.setPool_name("Automation Pool");
		return numberPool;
	}


	public static void updateTemplate(String group_id, String callFlowType, int keypresses, String numbers) {
		
	}
	
	public static JSONArray updateCallflow(Integer call_flow_id, Integer campaign_id, Integer group_id, String tn_type, String paramToBeUpdated, String updatedParam) {
		// TODO Auto-generated method stub
		JSONArray callflowReq = new JSONArray();
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowTemplete(tn_type);
		switch(tn_type) {
		case "simple" :
			PostCallflowRequest postCallflowRequestSimple = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
			postCallflowRequestSimple.setGroup_id(group_id);
			postCallflowRequestSimple.setCampaign_id(campaign_id);
			postCallflowRequestSimple.setCall_flow_id(call_flow_id);
			postCallflowRequestSimple.setCall_flow_name(paramToBeUpdated);
			
			Map<String, Object> mapSimpleRoute = mapper.convertValue(postCallflowRequestSimple, new TypeReference<Map<String, Object>>() {
			});
			while (mapSimpleRoute.values().remove(null));	
			JSONObject callflowReqObjSimple = new JSONObject(mapSimpleRoute);
			callflowReq.add(callflowReqObjSimple);
			break;
		case "ivr" :
			PostIvrRouteCallflowRequest postCallflowRequestIvr = mapper.convertValue(confCallflowHierarchy, PostIvrRouteCallflowRequest.class);
			postCallflowRequestIvr.setGroup_id(group_id);
			postCallflowRequestIvr.setCampaign_id(campaign_id);
			postCallflowRequestIvr.setCall_flow_id(call_flow_id);
			postCallflowRequestIvr.setCall_flow_name(paramToBeUpdated);
			
			Map<String, Object> mapIvr = mapper.convertValue(postCallflowRequestIvr, new TypeReference<Map<String, Object>>() {
			});
			while (mapIvr.values().remove(null));	
			JSONObject callflowReqObjIvr = new JSONObject(mapIvr);
			callflowReq.add(callflowReqObjIvr);
			break;
		case "pool" :
			PostPoolBasedCallflowRequest postCallflowRequestPool = mapper.convertValue(confCallflowHierarchy, PostPoolBasedCallflowRequest.class);
			postCallflowRequestPool.setGroup_id(group_id);
			postCallflowRequestPool.setCampaign_id(campaign_id);
			postCallflowRequestPool.setCall_flow_id(call_flow_id);
			postCallflowRequestPool.setCall_flow_name(paramToBeUpdated);
			
			Map<String, Object> mapPool = mapper.convertValue(postCallflowRequestPool, new TypeReference<Map<String, Object>>() {
			});
			while (mapPool.values().remove(null));	
			JSONObject callflowReqObPool = new JSONObject(mapPool);
			callflowReq.add(callflowReqObPool);
			break;
		
		}
		
		
		return callflowReq;
	}

	
}
