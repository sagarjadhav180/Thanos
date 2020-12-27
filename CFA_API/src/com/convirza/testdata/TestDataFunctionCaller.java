package com.convirza.testdata;

public class TestDataFunctionCaller {

	static Boolean create_group;	
	static Boolean create_campaign;
	static Boolean create_tn;
	static Boolean create_webhook;
	static Boolean create_tags;
	static Boolean upload_call_record;
	static Boolean add_premium_number;
	static Boolean add_reserve_number;
	static Boolean add_component;
	
	
	public void setCreationVars() {
		
		create_group  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		create_campaign  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		create_tn  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		create_webhook  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		create_tags  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		upload_call_record  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		add_premium_number  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		add_reserve_number  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));
		add_component  = Boolean.parseBoolean(TestDataDBUtil.getFeatureDetails(""));		
		
	}
	
	public void createGroups() {
		if(create_group=true) {
			TestData testData = new TestDataImplementation();
			testData.uploadGroups();
		}
	}
	
	public void createCampaigns() {
		if(create_campaign=true) {
			TestData testData = new TestDataImplementation();
			testData.uploadCampaigns();
		}
	}
	
	public void createTrackingNumber() {
		if(create_tn=true) {
			TestData testData = new TestDataImplementation();
			testData.uplaodTrackingNumbers();
		}
	}
	
	public void createTags() {
		if(create_tags=true) {
			TestData testData = new TestDataImplementation();
			testData.uploadTags();
		}
	}
	
	public void uploadCallRecord() {
		if(upload_call_record=true) {
			TestData testData = new TestDataImplementation();
			testData.uploadCalls();			
		}
	}
	
	public void addPremiumNumber() {
		if(add_premium_number=true) {
			TestData testData = new TestDataImplementation();
			testData.createPremiumNumber();			
		}
	}
	
	public void addReserveNumber() {
		if(add_reserve_number=true) {
			TestData testData = new TestDataImplementation();
			testData.createReseveNumbers();	
		}
	}
	
	public void addComponent() {
		if(add_component=true) {
			TestData testData = new TestDataImplementation();
			testData.addComponent();
		}
	}
	
	
	
}
