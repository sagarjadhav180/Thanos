package com.convirza.tests.pojo.request.success;

public class ReserveNumberRequest {
	private Long number;
	private Long number_id;
	private Long source;

	public Long getNumber ()
	{
		return number;
	}

	public void setNumber (Long number)
	{
		this.number = number;
	}

	public Long getNumber_id ()
	{
		return number_id;
	}

	public void setNumber_id (Long number_id)
	{
		this.number_id = number_id;
	}

	public Long getSource ()
	{
		return source;
	}

	public void setSource (Long source)
	{
		this.source = source;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [number = "+number+", number_id = "+number_id+", source = "+source+"]";
	}
}
