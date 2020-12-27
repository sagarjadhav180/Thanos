package com.convirza.tests.pojo.request.success;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.convirza.constants.TestDataYamlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostWebhookRequest {

	private String method;
	private Boolean include_Indicator_Scores;
	private Boolean include_DNI_Logs;
	private String webhook_Name;
	private String webhook_Endpoint;
	private String format;
	private String webhook_status;
	private Integer org_unit_id;
	private Integer webhook_id;
	private String description;

	public String getMethod ()
	{
		return method;
	}

	public void setMethod (String method)
	{
		this.method = method;
	}
	
	public Integer getWebhook_id ()
	{
		return webhook_id;
	}

	public void setWebhook_id (Integer webhook_id)
	{
		this.webhook_id = webhook_id;
	}

	public Boolean getInclude_Indicator_Scores ()
	{
		return include_Indicator_Scores;
	}

	public void setInclude_Indicator_Scores (Boolean include_Indicator_Scores)
	{
		this.include_Indicator_Scores = include_Indicator_Scores;
	}

	public Boolean getInclude_DNI_Logs ()
	{
		return include_DNI_Logs;
	}

	public void setInclude_DNI_Logs (Boolean include_DNI_Logs)
	{
		this.include_DNI_Logs = include_DNI_Logs;
	}

	public String getWebhook_Name ()
	{
		return webhook_Name;
	}

	public void setWebhook_Name (String webhook_Name)
	{
		this.webhook_Name = webhook_Name;
	}

	public String getWebhook_Endpoint ()
	{
		return webhook_Endpoint;
	}

	public void setWebhook_Endpoint (String webhook_Endpoint)
	{
		this.webhook_Endpoint = webhook_Endpoint;
	}

	public String getFormat ()
	{
		return format;
	}

	public void setFormat (String format)
	{
		this.format = format;
	}

	public String getWebhook_status ()
	{
		return webhook_status;
	}

	public void setWebhook_status (String webhook_status)
	{
		this.webhook_status = webhook_status;
	}

	public Integer getOrg_unit_id ()
	{
		return org_unit_id;
	}

	public void setOrg_unit_id (Integer org_unit_id)
	{
		this.org_unit_id = org_unit_id;
	}

	public String getDescription ()
	{
		return description;
	}

	public void setDescription (String description)
	{
		this.description = description;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [static_Parameter = + method = "+method+", include_Indicator_Scores = "+include_Indicator_Scores+", include_DNI_Logs = "+include_DNI_Logs+", webhook_Name = "+webhook_Name+", webhook_Endpoint = "+webhook_Endpoint+", format = "+format+", webhook_status = "+webhook_status+", org_unit_id = "+org_unit_id+", description = "+description+"]";
	}
	
	public Map<String, Object> getMapObject() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put(TestDataYamlConstants.WebhookConstants.WEBHOOK_ID, webhook_id);
		map.put(TestDataYamlConstants.WebhookConstants.WEBHOOK_STATUS, webhook_status);
		map.put(TestDataYamlConstants.WebhookConstants.ORG_UNIT_ID, org_unit_id);
		map.put(TestDataYamlConstants.WebhookConstants.WEBHOOK_NAME, webhook_Name);
		map.put(TestDataYamlConstants.WebhookConstants.DESCRIPTION, description);
		map.put(TestDataYamlConstants.WebhookConstants.WEBHOOK_ENDPOINT, webhook_Endpoint);
		map.put(TestDataYamlConstants.WebhookConstants.METHOD, method);
		map.put(TestDataYamlConstants.WebhookConstants.FORMAT, format);
		map.put(TestDataYamlConstants.WebhookConstants.INCLUDE_DNI_LOGS, include_DNI_Logs);
		map.put(TestDataYamlConstants.WebhookConstants.INCLUDE_INDICATOR_SCORES, include_Indicator_Scores);
		return map;
	}
}
