package com.convirza.tests.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import common.BaseClass;
import common.HelperClass;

public class CreateDNIClassOnCallFlow extends BaseClass {

	public CreateDNIClassOnCallFlow(String groupId, String callFlowId, String dniClass) throws ClientProtocolException, IOException, URISyntaxException, ParseException{
		JSONArray dni_array = new JSONArray();
		JSONObject json_obj = new JSONObject();
		json_obj.put("group_id", Integer.parseInt(groupId));

		json_obj.put("dni_code", "string");
		json_obj.put("custom_cookie", "string");
		json_obj.put("destination_url", "*.*");
		json_obj.put("dni_type", "url");
		json_obj.put("dni_element", "lmc_track");
		json_obj.put("call_flow_id", Integer.parseInt(callFlowId));
		json_obj.put("referrer", "*.google.*");
		json_obj.put("referrer_type", "organic");

		dni_array.add(json_obj);
		CloseableHttpResponse response = HelperClass.make_post_request("/v2/callflow/dni", access_token, dni_array);
		Assert.assertTrue(!(response.getStatusLine().getStatusCode() == 500 || response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 400), "Invalid status code is displayed. "+ "Returned Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			JSONParser parser = new JSONParser();
			JSONObject json_response =(JSONObject) parser.parse(line);
			System.out.println(json_response);
			String result_data = json_response.get("result").toString();
			Assert.assertEquals(result_data, "success", "API is returning error when valid access_token is passed.");
		}
	}
}
