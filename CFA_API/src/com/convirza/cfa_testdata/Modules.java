package com.convirza.cfa_testdata;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Modules {
	//groups
	public void uploadGroups(String accessToken) throws Exception;
	
	//upload campaigns
	public void uploadCampaigns(String accessToken) throws Exception;;
	
	//upload Tracking numbers
	public void uploadTrackingNumbers(String accessToken) throws Exception;;
	
	//upload calls
	public void uploadCalls(String accessToken) throws Exception;;
	
	//upload tags
	public void tags(String accessToken) throws Exception;;
	
	//upload web-hooks
	public void uploadWebHooks(String accessToken) throws Exception;;
	
}
