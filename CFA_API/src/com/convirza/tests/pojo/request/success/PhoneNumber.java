package com.convirza.tests.pojo.request.success;

public class PhoneNumber {
	private Long phone_number_id;
	private Long phone_number;

	public Long getPhone_number_id ()
	{
		return phone_number_id;
	}

	public void setPhone_number_id (Long phone_number_id)
	{
		this.phone_number_id = phone_number_id;
	}

	public Long getPhone_number ()
	{
		return phone_number;
	}

	public void setPhone_number (Long phone_number)
	{
		this.phone_number = phone_number;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [phone_number_id = "+phone_number_id+", phone_number = "+phone_number+"]";
	}
}
