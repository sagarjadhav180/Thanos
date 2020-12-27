package com.convirza.tests.pojo.request.success;


import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.convirza.constants.TestDataYamlConstants;

public class PostGroupRequest {

	private Integer group_id;
	private String group_ext_id;
	private String group_name;
	private Integer group_parent_id;
	private Integer top_group_id;
	private Integer billing_id;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone_number;
	private Integer industry_id;

	public Integer getgroup_id() {
		return group_id;
	}

	public void setgroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	
	public String getGroup_ext_id() {
		return group_ext_id;
	}

	public void setGroup_ext_id(String groupExtId) {
		this.group_ext_id = groupExtId;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String groupName) {
		this.group_name = groupName;
	}

	public Integer getGroup_parent_id() {
		return group_parent_id;
	}

	public void setGroup_parent_id(Integer group_parent_id) {
		this.group_parent_id = group_parent_id;
	}

	public Integer getTop_group_id() {
		return top_group_id;
	}

	public void setTop_group_id(Integer top_group_id) {
		this.top_group_id = top_group_id;
	}

	public Integer getBilling_id() {
		return billing_id;
	}

	public void setBilling_id(Integer billing_id) {
		this.billing_id = billing_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phoneNumber) {
		this.phone_number = phoneNumber;
	}

	public Integer getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(Integer industryId) {
		this.industry_id = industryId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("group_ext_id", group_ext_id).append("group_name", group_name).append("group_parent_id", group_parent_id).append("top_group_id", top_group_id).append("billing_id", billing_id).append("address", address).append("city", city).append("state", state).append("zip", zip).append("phone_number", phone_number).append("industry_id", industry_id).toString();
	}
	
	public Map<String, Object> getMapObject() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put(TestDataYamlConstants.GroupConstants.GROUP_ID, group_id);
		map.put(TestDataYamlConstants.GroupConstants.GROUP_EXT_ID, group_ext_id);
		map.put(TestDataYamlConstants.GroupConstants.GROUP_NAME, group_name);
		map.put(TestDataYamlConstants.GroupConstants.GROUP_PARENT_ID, group_parent_id);
		map.put(TestDataYamlConstants.GroupConstants.TOP_GROUP_ID, top_group_id);
		map.put(TestDataYamlConstants.GroupConstants.BILLING_ID, billing_id);
		map.put(TestDataYamlConstants.GroupConstants.ADDRESS, address);
		map.put(TestDataYamlConstants.GroupConstants.CITY, city);
		map.put(TestDataYamlConstants.GroupConstants.STATE, state);
		map.put(TestDataYamlConstants.GroupConstants.ZIP, zip);
		map.put(TestDataYamlConstants.GroupConstants.PHONE_NUMBER, phone_number);
		map.put(TestDataYamlConstants.GroupConstants.INDUSTRY_ID, industry_id);	
		return map;
	}

}