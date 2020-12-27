package com.convirza.tests.pojo.request.success;

import java.util.LinkedHashMap;
import java.util.Map;

import com.convirza.constants.TestDataYamlConstants;

public class PostCustomSourceRequest {
	private String custom_source_name;
	private Integer custom_source_id;
	private Integer org_unit_id;

	public String getCustom_source_name ()
	{
		return custom_source_name;
	}

	public void setCustom_source_name (String custom_source_name)
	{
		this.custom_source_name = custom_source_name;
	}
	
	public Integer getCustom_source_id ()
	{
		return custom_source_id;
	}

	public void setCustom_source_id (Integer custom_source_id)
	{
		this.custom_source_id = custom_source_id;
	}

	public Integer getOrg_unit_id ()
	{
		return org_unit_id;
	}

	public void setOrg_unit_id (Integer org_unit_id)
	{
		this.org_unit_id = org_unit_id;
	}

	public Map<String,Object> getMapObject() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put(TestDataYamlConstants.CustomSourceConstants.ORG_UNIT_ID, org_unit_id);
		map.put(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_ID, custom_source_id);
		map.put(TestDataYamlConstants.CustomSourceConstants.CUSTOM_SOURCE_NAME, custom_source_name);
		return map;
	}
	
	@Override
	public String toString()
	{
		return "ClassPojo [custom_source_name = "+custom_source_name+", org_unit_id = "+org_unit_id+"]";
	}
}
