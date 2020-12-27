package com.convirza.tests.selenium.scripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.convirza.constants.Constants;
import com.convirza.constants.EnvironmentConstants;
import com.convirza.tests.selenium.pagefactory.LoginPage;

import common.HelperClass;

public class Login {
	
	public static void login(WebDriver driver) {
    ArrayList<String> app_url = new ArrayList<String>();
    app_url.add(EnvironmentConstants.ConfigConstants.APP_URL);
    app_url.add(EnvironmentConstants.ConfigConstants.AGENCY_ADMIN_EMAIL);
    app_url.add(EnvironmentConstants.ConfigConstants.AGENCY_ADMIN_PASSWORD);
    ArrayList<String> config = null;
	  try {
			config = HelperClass.read_config(app_url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.get(Constants.HTTPS + config.get(0));				
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		login.email.sendKeys(config.get(1));
		login.password.sendKeys(config.get(2));
		login.password.submit();
	}
}