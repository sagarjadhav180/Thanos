package com.convirza.tests.base;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.io.YamlWriter;
import com.convirza.tests.selenium.scripts.BrowserInitializer;
import com.convirza.tests.selenium.scripts.Campaign;
import com.convirza.tests.selenium.scripts.Group;
import com.convirza.tests.selenium.scripts.Login;
import com.convirza.tests.selenium.scripts.Webhook;

import common.BaseClass;
import common.HelperClass;

public class TestDataCleanup extends BaseClass {
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	YamlWriter yamlWriter = new YamlWriter();
	public static WebDriver driver;
	
	public void cleanup() {
		driver = BrowserInitializer.initialize("chrome");

		Login.login(driver);
		Group group = new Group(driver);
		group.deleteGroup(driver);
		
		Campaign campaign = new Campaign(driver);
		campaign.deleteCampaign();
		
		Webhook webhook = new Webhook(driver);
		webhook.deleteWebhook();
		
		JSONArray array=new JSONArray();
		JSONObject obj=new JSONObject();
		Map<String, Object> compConfBlacklistHierarchy = yamlReader.readBlacklistNumberInfo(Constants.GroupHierarchy.AGENCY);
		String blacklisted_number=compConfBlacklistHierarchy.get(TestDataYamlConstants.BlacklistNumberConstants.BLACKLISTED_NUMBER).toString();
		array.add(Long.parseLong(blacklisted_number));
		obj.put("number", array);
		
		try {
			HelperClass.make_delete_request("/v2/blacklistednumber", access_token, obj);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		driver.close();
	}	
}
