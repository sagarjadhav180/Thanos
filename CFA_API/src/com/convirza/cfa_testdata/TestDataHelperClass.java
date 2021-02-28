package com.convirza.cfa_testdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import common.BaseClass;
import common.HelperClass;

public class TestDataHelperClass extends BaseClass {

	ArrayList<String> test_data;	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String access_token_agency_admin;
	
	String invocationCountForGroups;
	String invocationCountForCampaigns;
	String invocationCountForTrackingNumbers;
	String invocationCountForCalls;
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException    {
		
		Map<String, Object> compConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
		String username_compamy=compConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		Map<String, Object> locConfUserHierarchy = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
		String username_location=locConfUserHierarchy.get(TestDataYamlConstants.UserConstants.EMAIL).toString();
		
		String response = HelperClass.get_oauth_token(username_compamy, "lmc2demo");
		access_token_company_admin=response;
		
		String response1 = HelperClass.get_oauth_token(username_location, "lmc2demo");
		access_token_location_admin=response1;
		
		access_token_agency_admin = access_token;
		
		setInvoactionCounts();
	}
	
	@Test(invocationCount=100)
	public void groups() throws Exception {
		CFAModules cFAModules = new CFAModules();
	
		cFAModules.uploadGroups(access_token_company_admin); //--company
		cFAModules.uploadGroups(access_token_location_admin); //--location
	}
	
	
	@Test(invocationCount=100)
	public void campaigns() throws Exception {
		CFAModules cFAModules = new CFAModules();

		cFAModules.uploadCampaigns(access_token_agency_admin); //--agency
		cFAModules.uploadCampaigns(access_token_company_admin); //--company
		cFAModules.uploadCampaigns(access_token_location_admin); //--location
	}

	
	@Test(invocationCount=100)
	public void trackingNumbers() throws Exception {
		CFAModules cFAModules = new CFAModules();

		cFAModules.uploadTrackingNumbers(access_token_agency_admin); //--agency
		cFAModules.uploadTrackingNumbers(access_token_company_admin); //--company
		cFAModules.uploadTrackingNumbers(access_token_location_admin); //--location
	}
	
	
	@Test(invocationCount=100)
	public void calls() throws Exception {
		CFAModules cFAModules = new CFAModules();

		cFAModules.uploadCalls(access_token_agency_admin); //--agency
		cFAModules.uploadCalls(access_token_company_admin); //--company
		cFAModules.uploadCalls(access_token_location_admin); //--location
	}
	
	public void setInvoactionCounts() {
		invocationCountForGroups = TestDataUtil.getInvocationCount("groups");
		invocationCountForGroups = TestDataUtil.getInvocationCount("campaigns");
		invocationCountForGroups = TestDataUtil.getInvocationCount("trackingNUmbers");
		invocationCountForGroups = TestDataUtil.getInvocationCount("calls");
	}
	
}
