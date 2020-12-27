package com.convirza.tests.pojo.response.success;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostGroupDatum {

	private String result;
	private String data;
	private Integer entry_count;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getEntry_count() {
		return entry_count;
	}

	public void setEntry_count(Integer entry_count) {
		this.entry_count = entry_count;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("result", result).append("data", data).append("entryCount", entry_count).append("additionalProperties", additionalProperties).toString();
	}

}
