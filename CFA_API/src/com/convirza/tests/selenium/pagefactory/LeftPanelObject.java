package com.convirza.tests.selenium.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LeftPanelObject {

	@FindBy(how=How.XPATH, using="(//a[@href='#/set-group'])[2]")
	public WebElement groupAndUserLink;
	
	@FindBy(how=How.XPATH, using="(//a[@href='#/set-campaign'])[2]")
	public WebElement campaignAndCallflowLink;
	
	@FindBy(how=How.XPATH, using="(//span[text()='Settings'])[2]/parent::a")
	public WebElement settingsLink;
	
	@FindBy(how=How.XPATH, using="(//span[text()='Customization'])[2]/parent::a")
	public WebElement customizationLink;
	
	@FindBy(how=How.XPATH, using="(//span[text()='Webhook'])[2]/parent::a")
	public WebElement webhookLink;

	@FindBy(how=How.XPATH, using="//a[@id='leftmenu-trigger']")
	public WebElement toogleLeftButton;
}
