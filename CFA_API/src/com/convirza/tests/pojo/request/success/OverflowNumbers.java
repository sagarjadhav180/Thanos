package com.convirza.tests.pojo.request.success;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OverflowNumbers {
	
	private Long overflowNumber;
	private int rings;
	
	
	
	public Long getOverflowNumber() {
		return overflowNumber;
	}
	
	public void setOverflowNumber(Long overflowNumber) {
		this.overflowNumber = overflowNumber;
	}
	
	public int getRings() {
		return rings;
	}
	
	public void setRings(int rings) {
		this.rings = rings;
	}
	
	
	@Override
	public String toString() {
		return "OverflowNumbers [overflowNumber=" + overflowNumber + ", rings=" + rings + "]";
	}

}
