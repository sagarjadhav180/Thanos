package com.convirza.tests.base;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.convirza.cfa_testdata.TestDataUtil;
import com.convirza.constants.Constants;
import com.convirza.constants.EnvironmentConstants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.PropertiesReader;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;
import com.convirza.tests.core.utils.DBUserUtils;
import common.HelperClass;

public class EnvionmentHelper {

	private String zip;

	public void updateEnvironmentConfigs() throws SQLException {
		updateConfigProperties();
		updatePostgresProperties();
	}
	
	public void updateConfigProperties() throws SQLException {
		
		String username = TestDataUtil.getCredentails("userID");
		String password = TestDataUtil.getCredentails("passID");
		int staging =  TestDataUtil.getInvocationCount("stage");
		
		String API_URL = "stag-"+staging+"-cfaapi-1.convirza.com";
		String APP_URL = "stag-"+staging+"-cmo-1.convirza.com";
		String ENV = "staging"+staging;
		
		Map<String,String> configDetails = new HashMap<String, String>();
		configDetails.put(EnvironmentConstants.ConfigConstants.URL, API_URL);
		configDetails.put(EnvironmentConstants.ConfigConstants.APP_URL, APP_URL);		
		configDetails.put(EnvironmentConstants.ConfigConstants.AGENCY_ADMIN_EMAIL, username);
		configDetails.put(EnvironmentConstants.ConfigConstants.AGENCY_ADMIN_PASSWORD, password);
		configDetails.put(EnvironmentConstants.ConfigConstants.ENV, ENV);		
		HelperClass.write_config(configDetails);
	}
		
	public void updatePostgresProperties() throws SQLException {
		PropertiesReader dbConfig = new PropertiesReader();
		Map<String,String> properties = new HashMap<String, String>();
	
		int staging =  TestDataUtil.getInvocationCount("stage");
		String CONNECTION_URL = "jdbc:postgresql://stag-"+staging+"-pg-1.convirza.com:5432";
		properties.put(EnvironmentConstants.PostgresConstants.CONNECTION_URL, CONNECTION_URL);
		dbConfig.writeProperties(FileConstants.getPostgresConfigFile(), properties);
	}

	public void updateTestDataWithConfig() {
		TestDataYamlReader yamlReader = new TestDataYamlReader();
		YamlWriter yamlWriter = new YamlWriter();
		String agency_admin_user = "";
		ArrayList<String> agency_admin = new ArrayList<>();
		agency_admin.add("agency_admin_email");
		try {
			agency_admin_user = (HelperClass.read_config(agency_admin)).get(0);
			Map<String,Object> userInfo = DBUserUtils.getAgencyAdminInfoByEmail(agency_admin_user);
			Integer userId = (Integer) userInfo.get(TestDataYamlConstants.UserConstants.ID);
			Integer groupId = (Integer) userInfo.get(TestDataYamlConstants.UserConstants.GROUP_ID);
			String firstName = (String) userInfo.get(TestDataYamlConstants.UserConstants.FIRST_NAME);
			String lastName = (String) userInfo.get(TestDataYamlConstants.UserConstants.LAST_NAME);
			String groupName = (String) userInfo.get(TestDataYamlConstants.GroupConstants.GROUP_NAME);
			String address = (String) userInfo.get(TestDataYamlConstants.GroupConstants.ADDRESS);
			String city = (String) userInfo.get(TestDataYamlConstants.GroupConstants.CITY);
			String state = (String) userInfo.get(TestDataYamlConstants.GroupConstants.STATE);
			String zip = (String) userInfo.get(TestDataYamlConstants.GroupConstants.ZIP);
			String phoneNumber = (String) userInfo.get(TestDataYamlConstants.GroupConstants.PHONE_NUMBER);
			Integer industryId = (Integer) userInfo.get(TestDataYamlConstants.GroupConstants.INDUSTRY_ID);

			String[] groupHeirarchy = new String[] {Constants.GroupHierarchy.AGENCY,Constants.GroupHierarchy.COMPANY,
																Constants.GroupHierarchy.LOCATION};
			for (String groupLevel : groupHeirarchy) {
				Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(groupLevel);
				Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(groupLevel);
				Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(groupLevel);
				Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowInfo(groupLevel);
				Map<String, Object> confWebhookHierarchy = yamlReader.readWebhookInfo(groupLevel);
				Map<String, Object> confCustomSourceHierarchy = yamlReader.readCustomSourceInfo(groupLevel);
				if (groupLevel.equalsIgnoreCase(Constants.GroupHierarchy.AGENCY)) {
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.GROUP_ID, groupId);
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, 0);
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.GROUP_NAME, (groupName == null ? "" : groupName));
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.ADDRESS, "Pune");
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.CITY, "Salt Lake City");
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.STATE, "UT");
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.ZIP, "10006");
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.PHONE_NUMBER, phoneNumber);
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.INDUSTRY_ID, industryId);
					confUserHierarchy.put(TestDataYamlConstants.UserConstants.ID, userId);
					confUserHierarchy.put(TestDataYamlConstants.UserConstants.FIRST_NAME, firstName);
					confUserHierarchy.put(TestDataYamlConstants.UserConstants.LAST_NAME, lastName);
					confUserHierarchy.put(TestDataYamlConstants.UserConstants.GROUP_ID, groupId);
					confUserHierarchy.put(TestDataYamlConstants.UserConstants.EMAIL, agency_admin_user);
					confCampaignHierarchy.put(TestDataYamlConstants.CampaignConstants.GROUP_ID,groupId);
					confCampaignHierarchy.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_OWNER_USER_ID,userId);
					List<Integer> users = new ArrayList<>();
					users.add(userId);
					confCampaignHierarchy.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_USERS,users);
					confCallflowHierarchy.put(TestDataYamlConstants.CallflowConstants.GROUP_ID, groupId);
					confWebhookHierarchy.put(TestDataYamlConstants.WebhookConstants.ORG_UNIT_ID, groupId);
					confCustomSourceHierarchy.put(TestDataYamlConstants.CustomSourceConstants.ORG_UNIT_ID, groupId);
				}
				if (groupLevel.equalsIgnoreCase(Constants.GroupHierarchy.COMPANY)) {
					confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, groupId);
				}
				confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.BILLING_ID, groupId);
				confGroupHierarchy.put(TestDataYamlConstants.GroupConstants.TOP_GROUP_ID, groupId);
				yamlReader.yamlTestData.putAll(yamlReader.setGroupInfo(groupLevel, confGroupHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
				yamlReader.yamlTestData.putAll(yamlReader.setUserInfo(groupLevel, confUserHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
				yamlReader.yamlTestData.putAll(yamlReader.setCampaignInfo(groupLevel, confCampaignHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
				yamlReader.yamlTestData.putAll(yamlReader.setCallflowInfo(groupLevel, confCallflowHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
				yamlReader.yamlTestData.putAll(yamlReader.setWebhookInfo(groupLevel, confWebhookHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
				yamlReader.yamlTestData.putAll(yamlReader.setCustomSourceInfo(groupLevel, confCustomSourceHierarchy));
				yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateConfigYaml(TestDataYamlReader yamlReader, YamlWriter yamlWriter, String groupLevel, Map<String,Object> config) {
		yamlReader.yamlTestData.putAll(yamlReader.setGroupInfo(groupLevel, config));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}
}
