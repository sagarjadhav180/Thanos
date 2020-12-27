package com.convirza.tests.selenium.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage {

	@FindBy(how=How.XPATH, using="//input[@name='email']") 
	public WebElement email;
	
	@FindBy(how=How.XPATH, using="//input[@name='password']") 
	public WebElement password;

}
