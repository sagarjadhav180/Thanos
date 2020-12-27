package com.convirza.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.convirza.tests.base.TestDataCleanup;


public class Pendo extends TestDataCleanup{

	public static void closePopUp(String xpath) {
		

		WebDriverWait wait=new WebDriverWait(driver, 1000);
		
		try {
			
			if((driver.findElements(By.xpath(xpath)).size())!=0) {
				WebElement pendo_close_button = driver.findElement(By.xpath(xpath));
				wait.until(ExpectedConditions.visibilityOf(pendo_close_button));
				pendo_close_button.click();				
			}
			
		}catch(Exception e){
			System.out.println("pendo pop is not present");
		}
		
	}
	
}
