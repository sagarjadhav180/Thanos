package com.convirza.tests.pojo.response.success;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostGroupResponse {

	private String result;
	private Object err;
	private List<PostGroupDatum> data = null;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Object getErr() {
		return err;
	}

	public void setErr(Object err) {
		this.err = err;
	}

	public List<PostGroupDatum> getData() {
		return data;
	}

	public void setData(List<PostGroupDatum> data) {
		this.data = data;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("result", result).append("err", err).append("data", data).append("additionalProperties", additionalProperties).toString();
	}

}