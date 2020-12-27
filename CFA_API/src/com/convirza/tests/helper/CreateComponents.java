package com.convirza.tests.helper;

import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBCallFlowsUtils;
import com.convirza.tests.pojo.request.success.PostCallflowRequest;
import com.convirza.tests.pojo.request.success.PostGroupRequest;
import com.convirza.tests.pojo.response.success.PostGroupResponse;
import common.BaseClass;
import common.HelperClass;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;

public class CreateComponents extends BaseClass {
  public static TestDataYamlReader yamlReader = new TestDataYamlReader();
  public static ObjectMapper mapper = new ObjectMapper();

  public static String createGroup(String groupHierarchy) {
    String groupId = null;
    Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(groupHierarchy);
    PostGroupRequest postGroupRequest = mapper.convertValue(confGroupHierarchy, PostGroupRequest.class);
    postGroupRequest.setgroup_id(null);
    postGroupRequest.setGroup_ext_id(null);
    Map<String, Object> map = mapper.convertValue(postGroupRequest, new TypeReference<Map<String, Object>>() {
    });
    while (map.values().remove(null));
    JSONArray groupRequest = new JSONArray();
    JSONObject groupReqObj = new JSONObject(map);
    groupRequest.add(groupReqObj);
    CloseableHttpResponse response;
    try {
      response = HelperClass.make_post_request("/v2/group", access_token, groupRequest);
      Assert.assertTrue(
        (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201),
        "Invalid status code is displayed. " + "Returned Status: " + response.getStatusLine().getStatusCode() + " "
          + response.getStatusLine().getReasonPhrase());

      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
        PostGroupResponse group = mapper.readValue(line, PostGroupResponse.class);
        groupId = group.getData().get(0).getData();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return groupId;
  }
}
