package com.convirza.tests.selenium.pagefactory;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

	public class CampaignPage {

	@FindBy(how=How.XPATH, using="//h1")
	public WebElement pageHeading;
	
	@FindBy(how=How.ID, using="_pendo-close-guide_")
	public List<WebElement> guideDiv;
	
	@FindBy(how=How.XPATH, using="//thead/parent::table")
	public WebElement campaignTable;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'modal-footer')]//button[contains(text(),'OK')]")
	public WebElement campaignModalDeleteOKButton;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'modal-footer')]//button[contains(text(),'Cancel')]")
	public WebElement campaignModalDeleteCancelButton;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'ui-pnotify-closer')]")
	public WebElement notificationCloser;

	@FindBy(how=How.XPATH, using="//div[contains(@class,'alert')]")
	public List<WebElement> notificationAlert;
}
