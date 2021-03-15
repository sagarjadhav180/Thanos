package com.convirza.cfa_testdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import common.BaseClass;
import common.HelperClass;

public class TestDataHelperClass extends BaseClass {

	ArrayList<String> test_data;	
	TestDataYamlReader yamlReader = new TestDataYamlReader();
	String access_token_company_admin;
	String access_token_location_admin;
	String access_token_agency_admin;
	
	int invocationCountForGroups;
	int invocationCountForCampaigns;
	int invocationCountForTrackingNumbers;
	int invocationCountForCalls;
	int invocationCountForWebhooks;
	int invocationCountForReserveNumbers;
	int invocationCountForPremiumNumbers;
	
	@BeforeClass
	public void generateOuthTokenForCompanyAndLocationUser() throws ClientProtocolException, URISyntaxException, IOException, ParseException, SQLException    {
		
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
	
	@Test
	public void groups() throws Exception {
		CFAModules cfaModules = new CFAModules();

		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agencyGroupId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Map<String, Object> compConfGroupHierarchyCompany = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String companyGroupId=compConfGroupHierarchyCompany.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		for(int i=1;i<=invocationCountForGroups;i++) {
			cfaModules.uploadGroups("CFA TEST GROUP COMPANY-"+RandomContentGenerator.getRandomString(3), access_token_agency_admin, agencyGroupId, agencyGroupId); //--company
			cfaModules.uploadGroups("CFA TEST GROUP LOCATION-"+RandomContentGenerator.getRandomString(3), access_token_company_admin, companyGroupId, agencyGroupId); //--location			
		}
	}
	
	
	@Test
	public void campaigns() throws Exception {
		CFAModules cfaModules = new CFAModules();
		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agencyGroupId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		Map<String, Object> compConfUserHierarchyAgency = yamlReader.readUserInfo(Constants.GroupHierarchy.AGENCY);
		String agencyUserId=compConfUserHierarchyAgency.get(TestDataYamlConstants.UserConstants.ID).toString();
		
		Map<String, Object> compConfGroupHierarchyCompany = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String companyGroupId=compConfGroupHierarchyCompany.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		Map<String, Object> compConfUserHierarchyCompany = yamlReader.readUserInfo(Constants.GroupHierarchy.COMPANY);
		String companyUserId=compConfUserHierarchyCompany.get(TestDataYamlConstants.UserConstants.ID).toString();

		Map<String, Object> compConfUserHierarchyLocation = yamlReader.readUserInfo(Constants.GroupHierarchy.LOCATION);
		String locationUserId=compConfUserHierarchyLocation.get(TestDataYamlConstants.UserConstants.ID).toString();
		
		Map<String, Object> compConfGroupHierarchyLocation = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String locationGroupId=compConfGroupHierarchyLocation.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		for(int i=1;i<=invocationCountForCampaigns;i++) {
			cfaModules.uploadCampaigns("CFA TEST CAMPAIGN AGENCY-"+RandomContentGenerator.getRandomString(3), access_token_agency_admin, agencyGroupId, agencyUserId); //--agency
			cfaModules.uploadCampaigns("CFA TEST CAMPAIGN COMPANY-"+RandomContentGenerator.getRandomString(3), access_token_company_admin, companyGroupId, companyUserId); //--company
			cfaModules.uploadCampaigns("CFA TEST CAMPAIGN LOCATION-"+RandomContentGenerator.getRandomString(3), access_token_location_admin, locationGroupId, locationUserId); //--location			
		}

	}

	
	@Test
	public void trackingNumbers() throws Exception {
		CFAModules cfaModules = new CFAModules();

		for(int i=1;i<=invocationCountForTrackingNumbers;i++) {
			cfaModules.uploadTrackingNumbers(Constants.GroupHierarchy.AGENCY, access_token_agency_admin); //--agency
			cfaModules.uploadTrackingNumbers(Constants.GroupHierarchy.COMPANY, access_token_company_admin); //--company
			cfaModules.uploadTrackingNumbers(Constants.GroupHierarchy.LOCATION, access_token_location_admin); //--location			
		}

	}
	
	
	@Test
	public void calls() throws Exception {
		CFAModules cfaModules = new CFAModules();

		for(int i=1;i<=invocationCountForCalls;i++) {
			cfaModules.uploadCalls(access_token_agency_admin); //--agency
			cfaModules.uploadCalls(access_token_company_admin); //--company
			cfaModules.uploadCalls(access_token_location_admin); //--location			
		}

	}
	
	
	@Test
	public void webhooks() throws Exception {
		CFAModules cfaModules = new CFAModules();

		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agencyGroupId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		Map<String, Object> compConfGroupHierarchyCompany = yamlReader.readGroupInfo(Constants.GroupHierarchy.COMPANY);
		String companyGroupId=compConfGroupHierarchyCompany.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		Map<String, Object> compConfGroupHierarchyLocation = yamlReader.readGroupInfo(Constants.GroupHierarchy.LOCATION);
		String locationGroupId=compConfGroupHierarchyLocation.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();

		for(int i=1;i<=invocationCountForWebhooks;i++) {
			cfaModules.uploadWebHooks(access_token_agency_admin, agencyGroupId, "CFA TEST WEBHOOK AGENCY-"+RandomContentGenerator.getRandomString(3)); //--agency
			cfaModules.uploadWebHooks(access_token_company_admin, companyGroupId, "CFA TEST WEBHOOK AGENCY-"+RandomContentGenerator.getRandomString(3)); //--company
			cfaModules.uploadWebHooks(access_token_location_admin, locationGroupId, "CFA TEST WEBHOOK AGENCY-"+RandomContentGenerator.getRandomString(3)); //--location			
		}

	}

	@Test
	public void components() {
		CFAComponent cfaComponent = new CFAComponent();
		cfaComponent.setUp();
		cfaComponent.componentAction();
	}
	
	
	@Test
	public void numbers() {
		
		CFANumber cfaNumber = new CFANumber();
		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String agencyGroupId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		
		for(int i=1;i<=invocationCountForReserveNumbers;i++) {
			cfaNumber.createReserveNumber();
		}
		for(int j=1;j<=invocationCountForPremiumNumbers;j++) {
			cfaNumber.createPremiumNumber(agencyGroupId);
		}
	}
	
	
	public void setInvoactionCounts() throws SQLException {
		invocationCountForGroups = TestDataUtil.getInvocationCount("groups");
		invocationCountForGroups = TestDataUtil.getInvocationCount("campaign");
		invocationCountForGroups = TestDataUtil.getInvocationCount("t_number");
		invocationCountForGroups = TestDataUtil.getInvocationCount("calls");
		invocationCountForWebhooks = TestDataUtil.getInvocationCount("Webhooks");
		invocationCountForReserveNumbers = TestDataUtil.getInvocationCount("r_number");
		invocationCountForPremiumNumbers = TestDataUtil.getInvocationCount("p_number");
	}
	
}
