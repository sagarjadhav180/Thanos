package com.convirza.tests.pojo.request.success;

public class NumberPool {

	private String pool_name;
	private int keep_alive_minutes;
	private int number_quantity;
	private int npa;
	private int nxx;


	// Getter Methods 

	public String getPool_name() {
		return pool_name;
	}

	public int getKeep_alive_minutes() {
		return keep_alive_minutes;
	}

	public int getNumber_quantity() {
		return number_quantity;
	}

	public int getNpa() {
		return npa;
	}

	public int getNxx() {
		return nxx;
	}

	// Setter Methods 

	public void setPool_name(String pool_name) {
		this.pool_name = pool_name;
	}

	public void setKeep_alive_minutes(int keep_alive_minutes) {
		this.keep_alive_minutes = keep_alive_minutes;
	}

	public void setNumber_quantity(int number_quantity) {
		this.number_quantity = number_quantity;
	}

	public void setNpa(int npa) {
		this.npa = npa;
	}

	public void setNxx(int nxx) {
		this.nxx = nxx;
	}
	
	@Override
	public String toString() {
		return "NumberPool [pool_name=" + pool_name + ", keep_alive_minutes=" + keep_alive_minutes
				+ ", number_quantity=" + number_quantity + ", npa=" + npa + ", nxx=" + nxx + "]";
	}
	
}
