package com.convirza.cfa_testdata;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Modules {
	//groups
	public void uploadGroups(String group_name, String accessToken, String group_parent_id, String billing_idn) throws Exception;
	
	//upload campaigns
	public void uploadCampaigns(String campaign_name, String accessToken, String group_id, String campaign_owner_user_id) throws Exception;

	//upload Tracking numbers
	public void uploadTrackingNumbers(String level, String accessToken) throws Exception;
	
	//upload calls
	public void uploadCalls(String accessToken, String level) throws Exception;
	
	//upload web-hooks
	public void uploadWebHooks(String accessToken, String org_unit_id, String webhook_Name) throws Exception;
	
}
