package com.convirza.tests.pojo.request.success;

import java.util.LinkedHashMap;
import java.util.Map;

import com.convirza.constants.TestDataYamlConstants;

public class PostCampaignRequest {
	private String campaign_name;
	private String campaign_start_date;
	private Integer[] campaign_users;
	private String campaign_created;
	private String campaign_status;
	private Integer group_id;
	private String campaign_end_date;
	private String campaign_ext_id;
	private Integer campaign_owner_user_id;
	private String campaign_modified;
	private Integer campaign_id;

	public String getCampaign_name ()
	{
		return campaign_name;
	}

	public void setCampaign_name (String campaign_name)
	{
		this.campaign_name = campaign_name;
	}

	public String getCampaign_start_date ()
	{
		return campaign_start_date;
	}

	public void setCampaign_start_date (String campaign_start_date)
	{
		this.campaign_start_date = campaign_start_date;
	}

	public Integer[] getCampaign_users ()
	{
		return campaign_users;
	}

	public void setCampaign_users (Integer[] campaign_users)
	{
		this.campaign_users = campaign_users;
	}

	public String getCampaign_created ()
	{
		return campaign_created;
	}

	public void setCampaign_created (String campaign_created)
	{
		this.campaign_created = campaign_created;
	}

	public String getCampaign_status ()
	{
		return campaign_status;
	}

	public void setCampaign_status (String campaign_status)
	{
		this.campaign_status = campaign_status;
	}

	public Integer getGroup_id ()
	{
		return group_id;
	}

	public void setGroup_id (Integer group_id)
	{
		this.group_id = group_id;
	}

	public String getCampaign_end_date ()
	{
		return campaign_end_date;
	}

	public void setCampaign_end_date (String campaign_end_date)
	{
		this.campaign_end_date = campaign_end_date;
	}

	public String getCampaign_ext_id ()
	{
		return campaign_ext_id;
	}

	public void setCampaign_ext_id (String campaign_ext_id)
	{
		this.campaign_ext_id = campaign_ext_id;
	}

	public Integer getCampaign_owner_user_id ()
	{
		return campaign_owner_user_id;
	}

	public void setCampaign_owner_user_id (Integer campaign_owner_user_id)
	{
		this.campaign_owner_user_id = campaign_owner_user_id;
	}

	public String getCampaign_modified ()
	{
		return campaign_modified;
	}

	public void setCampaign_modified (String campaign_modified)
	{
		this.campaign_modified = campaign_modified;
	}

	public Integer getCampaign_id ()
	{
		return campaign_id;
	}

	public void setCampaign_id (Integer campaign_id)
	{
		this.campaign_id = campaign_id;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [campaign_name = "+campaign_name+", campaign_start_date = "+campaign_start_date+", campaign_users = "+campaign_users+", campaign_created = "+campaign_created+", campaign_status = "+campaign_status+", group_id = "+group_id+", campaign_end_date = "+campaign_end_date+", campaign_ext_id = "+campaign_ext_id+", campaign_owner_user_id = "+campaign_owner_user_id+", campaign_modified = "+campaign_modified+", campaign_id = "+campaign_id+"]";
	}
	
	public Map<String, Object> getMapObject() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_ID, campaign_id);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_NAME, campaign_name);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_EXT_ID, campaign_ext_id);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_CREATED, campaign_created);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_STATUS, campaign_status);
		map.put(TestDataYamlConstants.CampaignConstants.GROUP_ID, group_id);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_MODIFIED, campaign_modified);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_START_DATE, campaign_start_date);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_END_DATE, campaign_end_date);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_OWNER_USER_ID, campaign_owner_user_id);
		map.put(TestDataYamlConstants.CampaignConstants.CAMPAIGN_USERS, campaign_users);
		return map;
	}
}