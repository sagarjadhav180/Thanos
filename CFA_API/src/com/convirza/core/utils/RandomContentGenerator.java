package com.convirza.core.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class RandomContentGenerator {

	public static String createEmail() {
		Random r = new Random();
		String email = "convirza_user" + r.nextInt(999999)
			+ "@yopmail.com";
		return email;
	}
	
	public static String createPhoneNumber() {
		int num1, num2, num3;
		int set2, set3;
		Random r = new Random();
    num1 = r.nextInt(7) + 2; //add 1 so there is no 0 to begin  
    num2 = r.nextInt(8); //randomize to 8 because 0 counts as a number in the generator
    num3 = r.nextInt(8);
    set2 = r.nextInt(643) + 100;
    set3 = r.nextInt(8999) + 1000;
    return num1 + "" + num2 + "" + num3 + "" + set2 + "" + set3;
	}

	public static String createUUid() {
		UUID uniqueId = UUID.randomUUID();
		return uniqueId.toString();
	}

	public static String getRandomString() {
		return RandomStringUtils.randomAlphanumeric(20).toLowerCase();
	}
	
	public static String getRandomString(int digits) {
		return RandomStringUtils.randomAlphanumeric(digits).toLowerCase();
	}
}
