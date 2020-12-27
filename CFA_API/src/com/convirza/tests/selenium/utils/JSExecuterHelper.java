package com.convirza.tests.selenium.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSExecuterHelper {
	private static JavascriptExecutor executor;
	
	public static void scrollInView(WebDriver driver, WebElement element) {
		executor = (JavascriptExecutor)driver; 
		executor.executeScript("arguments[0].scrollIntoView()", element);
	}
	
	public static void clickOnElement(WebDriver driver, WebElement element) {
		executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
	public static void HighlightElement(WebDriver driver, WebElement element) {
		executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
	}
	
	public static void addStyleToElement(WebDriver driver, WebElement element, Map<String,String> style) {
		executor = (JavascriptExecutor) driver;
		Set<Map.Entry<String, String>> mapEntry = style.entrySet();
		Iterator<Map.Entry<String, String>> itr = mapEntry.iterator();
		while(itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			executor.executeScript("arguments[0].setAttribute('style', '"+entry.getKey()+": "+entry.getValue()+";')", element);
		}
	}
}
