package com.convirza.tests.selenium.pagefactory;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class WebhookPage {

	@FindBy(how=How.ID, using="_pendo-close-guide_")
	public List<WebElement> guideDiv;
	
	@FindBy(how=How.XPATH, using="//thead/parent::table")
	public WebElement webhookTable;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'modal-footer')]//button[contains(text(),'OK')]")
	public WebElement webhookModalDeleteOKButton;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'modal-footer')]//button[contains(text(),'Cancel')]")
	public WebElement webhookModalDeleteCancelButton;

	@FindBy(how=How.XPATH, using="//div[contains(@class,'ui-pnotify-closer')]")
	public WebElement notificationCloser;

	@FindBy(how=How.XPATH, using="//div[contains(@class,'alert')]")
	public List<WebElement> notificationAlert;
	
//	alert ui-pnotify-container alert-success ui-pnotify-shadow
}
