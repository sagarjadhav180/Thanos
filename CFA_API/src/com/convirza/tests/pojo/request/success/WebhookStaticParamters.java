package com.convirza.tests.pojo.request.success;

public class WebhookStaticParamters {
	private String field_Name;
	private String field_Value;

	public String getField_Name ()
	{
		return field_Name;
	}

	public void setField_Name (String field_Name)
	{
		this.field_Name = field_Name;
	}

	public String getField_Value ()
	{
		return field_Value;
	}

	public void setField_Value (String field_Value)
	{
		this.field_Value = field_Value;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [field_Name = "+field_Name+", field_Value = "+field_Value+"]";
	}
}
