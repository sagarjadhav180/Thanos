package com.convirza.tests.helper;

import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.core.utils.DBCampaignUtils;
import com.convirza.tests.pojo.request.success.PostCallflowRequest;
import common.BaseClass;
import common.HelperClass;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class UpdateComponents extends BaseClass {
  private static TestDataYamlReader yamlReader = new TestDataYamlReader();
  public static ObjectMapper mapper = new ObjectMapper();

  public static String updateCampaign(String groupHierarchy) {
    Map<String, Object> confCampaignHierarchy = yamlReader.readCampaignInfo(groupHierarchy);
    JSONArray campaignReq = new JSONArray();
    JSONObject campaignReqObj = new JSONObject(confCampaignHierarchy);
    campaignReq.add(campaignReqObj);
    CloseableHttpResponse response;
    try {
      response = HelperClass.make_post_request("/v2/campaign", access_token, campaignReq);
      Assert.assertTrue(
        (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
        "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
          + response.getStatusLine().getReasonPhrase());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return DBCampaignUtils.getModifiedDateById(confCampaignHierarchy.get(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID).toString());
  }

  public static String updateCallflow(String groupHierarchy) {
    Map<String, Object> confCallflowHierarchy = yamlReader.readCallflowInfo(groupHierarchy);
    PostCallflowRequest postCallflowRequest = mapper.convertValue(confCallflowHierarchy, PostCallflowRequest.class);
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
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    String updateDate = DBCallFlowsUtils.getModifiedDateById(confCallflowHierarchy.get(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID).toString());
    return updateDate;
  }
}
