package com.convirza.tests.base;

import java.util.Map;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants.CallflowConstants;
import com.convirza.constants.TestDataYamlConstants.CampaignConstants;
import com.convirza.constants.TestDataYamlConstants.GroupConstants;
import com.convirza.constants.TestDataYamlConstants.UserConstants;
import com.convirza.constants.TestDataYamlConstants.WebhookConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;

public class ResetTestData {
	static TestDataYamlReader yamlReader = new TestDataYamlReader();
	static YamlWriter yamlWriter = new YamlWriter();
	
	public static void resetIds(String group) {
		Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowInfo(group);
		confCallflowHierarchy.put(CallflowConstants.CALL_FLOW_ID, null);
		yamlReader.yamlTestData.putAll(yamlReader.setCallflowInfo(group, confCallflowHierarchy));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
		
		if (!group.equalsIgnoreCase(Constants.GroupHierarchy.AGENCY)) {
			Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);
			confGroupHierarchy.put(GroupConstants.GROUP_ID, null);
			yamlReader.yamlTestData.putAll(yamlReader.setGroupInfo(group, confGroupHierarchy));
			yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
			
			/*
			 * Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(group);
			 * confUserHierarchy.put(UserConstants.ID, null);
			 * yamlReader.yamlTestData.putAll(yamlReader.setUserInfo(group,
			 * confUserHierarchy)); yamlWriter.write(yamlReader.yamlTestData,
			 * FileConstants.getYamlTestDataConfig());
			 */
		}
		
		Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(group);
		confCampaignHierarchy.put(CampaignConstants.CAMPAIGN_ID, null);
		yamlReader.yamlTestData.putAll(yamlReader.setCampaignInfo(group, confCampaignHierarchy));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
		
		Map<String, Object> confWebhookHierarchy = yamlReader.readWebhookInfo(group);
		confWebhookHierarchy.put(WebhookConstants.WEBHOOK_ID, null);
		yamlReader.yamlTestData.putAll(yamlReader.setWebhookInfo(group, confWebhookHierarchy));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}
}
