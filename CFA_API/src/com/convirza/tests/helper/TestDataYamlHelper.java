package com.convirza.tests.helper;

import java.util.Map;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;

public class TestDataYamlHelper {
	private static YamlWriter yamlWriter = new YamlWriter();
	private static TestDataYamlReader yamlReader = new TestDataYamlReader();
	
	/**
	 * @author ankur
	 * @param group : Group hierarchy where user details are added in yaml
	 * @param userData : contains map of id, email and phone number
	 */
	public static void writeUserData(String group, Map<String,String> userData) {
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(group);
		confUserHierarchy.put(TestDataYamlConstants.UserConstants.ID, userData.get(TestDataYamlConstants.UserConstants.ID));

		confUserHierarchy.put(TestDataYamlConstants.UserConstants.USER_EXT_ID, userData.get(TestDataYamlConstants.UserConstants.USER_EXT_ID));
		confUserHierarchy.put(TestDataYamlConstants.UserConstants.GROUP_ID, userData.get(TestDataYamlConstants.UserConstants.GROUP_ID));
		confUserHierarchy.put(TestDataYamlConstants.UserConstants.EMAIL, userData.get(TestDataYamlConstants.UserConstants.EMAIL));
		confUserHierarchy.put(TestDataYamlConstants.UserConstants.PHONE_NUMBER, userData.get(TestDataYamlConstants.UserConstants.PHONE_NUMBER));
		yamlWriter.write(yamlReader.yamlTestData, FileConstants.getYamlTestDataConfig());
	}
	
}
