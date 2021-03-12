package com.convirza.cfa_testdata;

import com.convirza.tests.base.TestDataPreparation;
import com.convirza.tests.core.utils.DBNumberUtil;

public class CFANumber implements Number {

	@Override
	public void createReserveNumber() {
		// TODO Auto-generated method stub
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] numberDetails = testDataPreparation.getNumberFromAPI();
		testDataPreparation.reserveNumberFromAPI(Long.parseLong(numberDetails[0]), Long.parseLong(numberDetails[1]));
	}

	@Override
	public void createPremiumNumber(String orgUnitID) {
		// TODO Auto-generated method stub
		//reserving number
		TestDataPreparation testDataPreparation = new TestDataPreparation();
		String[] numberDetails = testDataPreparation.getNumberFromAPI();	
		testDataPreparation.reserveNumberFromAPI(Long.parseLong(numberDetails[0]), Long.parseLong(numberDetails[1]));
		
		//making number premium
		DBNumberUtil.makePremiumNumber(orgUnitID, numberDetails[0], numberDetails[1]);
	}

}
