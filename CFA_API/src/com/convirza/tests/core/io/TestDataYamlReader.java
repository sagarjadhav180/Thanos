package com.convirza.tests.core.io;

import java.util.Map;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;

public class TestDataYamlReader extends YamlReader {
	public Map<String, Object> yamlTestData;
	
	public Map<String, Object> readGroupInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confGroupObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.GROUP);
		Map<String, Object> confGroupHierarchy = (Map<String, Object>)confGroupObj.get(groupHierarchy);
		return confGroupHierarchy;
	}
	
	public Map<String, Object> readUserInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confUserObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.USER);
		Map<String, Object> confUserHierarchy = (Map<String, Object>)confUserObj.get(groupHierarchy);
		return confUserHierarchy;
	}
	
	public Map<String, Object> readCampaignInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCampaignObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CAMPAIGN);
		Map<String, Object> confCampaignHierarchy = (Map<String, Object>)confCampaignObj.get(groupHierarchy);
		return confCampaignHierarchy;
	}
	
	public Map<String, Object> setCampaignInfo(String groupHierarchy, Map<String, Object> campaignInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCampaignObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CAMPAIGN);
		confCampaignObj.put(groupHierarchy, campaignInfo);
		Map<String, Object> confCampaignHierarchy = (Map<String, Object>)confCampaignObj.get(groupHierarchy);
		return confCampaignHierarchy;
	}
	
	public Map<String, Object> setGroupFieldValue(String groupHierarchy, String fieldName, Integer value) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confGroupObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.GROUP);
		Map<String, Object> groupData = (Map<String, Object>)confGroupObj.get(groupHierarchy);
		groupData.put(fieldName, value);
		Map<String, Object> confGroupHierarchy = (Map<String, Object>)confGroupObj.get(groupHierarchy);
		return confGroupHierarchy;
	}
	
	public Map<String, Object> setGroupInfo(String groupHierarchy, Map<String, Object> groupInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confGroupObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.GROUP);
		confGroupObj.put(groupHierarchy, groupInfo);
		Map<String, Object> confGroupHierarchy = (Map<String, Object>)confGroupObj.get(groupHierarchy);
		return confGroupHierarchy;
	}
	
	public Map<String, Object> setUserInfo(String groupHierarchy, Map<String, Object> userInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confUserObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.USER);
		confUserObj.put(groupHierarchy, userInfo);
		Map<String, Object> confUserHierarchy = (Map<String, Object>)confUserObj.get(groupHierarchy);
		return confUserHierarchy;
	}
	
	public Map<String, Object> setCallflowInfo(String groupHierarchy, Map<String, Object> callflowInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCallflowObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CALLFLOW);
		confCallflowObj.put(groupHierarchy, callflowInfo);
		Map<String, Object> confCallflowHierarchy = (Map<String, Object>)confCallflowObj.get(groupHierarchy);
		return confCallflowHierarchy;
	}
	
	public Map<String, Object> setWebhookInfo(String groupHierarchy, Map<String, Object> callflowInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confWebhookObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.WEBHOOK);
		confWebhookObj.put(groupHierarchy, callflowInfo);
		Map<String, Object> confWebhookHierarchy = (Map<String, Object>)confWebhookObj.get(groupHierarchy);
		return confWebhookHierarchy;
	}
	
	public Map<String, Object> readCallflowInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCallflowObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CALLFLOW);
		Map<String, Object> confCallflowHierarchy = (Map<String, Object>)confCallflowObj.get(groupHierarchy);
		return confCallflowHierarchy;
	}
	
	public Map<String, Object> readCallflowTemplete(String callFlowType) {
		yamlTestData = readYaml(FileConstants.getCallFlowTempleteConfig());		
		Map<String, Object> confCallflowHierarchy = (Map<String, Object>)yamlTestData.get(callFlowType);
		return confCallflowHierarchy;
	}
	
	public Map<String, Object> readWebhookInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confWebhookwObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.WEBHOOK);
		Map<String, Object> confWebookHierarchy = (Map<String, Object>)confWebhookwObj.get(groupHierarchy);
		return confWebookHierarchy;
	}
	
	public Map<String, Object> readCustomSourceInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCustomSourcewObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CUSTOM_SOURCE);
		Map<String, Object> confCustomSourceHierarchy = (Map<String, Object>)confCustomSourcewObj.get(groupHierarchy);
		return confCustomSourceHierarchy;
	}
	
	public Map<String, Object> setCustomSourceInfo(String groupHierarchy, Map<String, Object> callflowInfo) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confCustomSourcewObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.CUSTOM_SOURCE);
		confCustomSourcewObj.put(groupHierarchy, callflowInfo);
		Map<String, Object> confCustomSourceHierarchy = (Map<String, Object>)confCustomSourcewObj.get(groupHierarchy);
		return confCustomSourceHierarchy;
	}
	
	public Map<String, Object> readBlacklistNumberInfo(String groupHierarchy) {
		yamlTestData = readYaml(FileConstants.getYamlTestDataConfig());		
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confBlacklistedNumberwObj = (Map<String, Object>)confConvirzaObj.get(TestDataYamlConstants.BLACKLISTED_NUMBER);
		Map<String, Object> confBlacklistedNumberHierarchy = (Map<String, Object>)confBlacklistedNumberwObj.get(groupHierarchy);
		return confBlacklistedNumberHierarchy;
	}
	
	public Map<String, Object> setBlacklistNumberInfo(String groupHierarchy, Map<String, Object> blacklistedNumberInfo) {
		Map<String, Object> confConvirzaObj = (Map<String, Object>) yamlTestData.get(TestDataYamlConstants.CONVIRZA);
		Map<String, Object> confBlacklistedNumberwObj = (Map<String, Object>) confConvirzaObj.get(TestDataYamlConstants.BLACKLISTED_NUMBER);
		confBlacklistedNumberwObj.put(groupHierarchy, blacklistedNumberInfo);
		Map<String, Object> confBlacklistedNumberHierarchy=(Map<String, Object>) confBlacklistedNumberwObj.get(groupHierarchy);
		
		return confBlacklistedNumberHierarchy;		
	}
}
