package com.convirza.tests.selenium.utils;

public class WaitExecuter {

	public static void sleep(int milisec) {
		try {
			Thread.sleep(milisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
