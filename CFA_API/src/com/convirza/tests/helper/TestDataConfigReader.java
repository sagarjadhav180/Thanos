package com.convirza.tests.helper;

import com.convirza.tests.core.io.TestDataYamlReader;

import java.util.Map;

public class TestDataConfigReader {
  TestDataYamlReader yamlReader = new TestDataYamlReader();

  public Object readCampaignInfo(String groupHierarchy, String fieldName) {
    Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(groupHierarchy);
    return confCampaignHierarchy.get(fieldName);
  }
}
