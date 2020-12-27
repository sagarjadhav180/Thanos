package com.convirza.tests.selenium.utils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class KeyEvent {

	public static void pressTab(WebElement element) {
		element.sendKeys(Keys.TAB);
	}
	
	public static void pressEnter(WebElement element) {
		element.sendKeys(Keys.ENTER);
	}
}
