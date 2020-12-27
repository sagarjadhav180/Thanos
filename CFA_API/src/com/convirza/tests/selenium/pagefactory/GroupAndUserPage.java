package com.convirza.tests.selenium.pagefactory;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class GroupAndUserPage {

	@FindBy(how=How.XPATH, using="//h1")
	public WebElement pageHeading;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_sub_group']")
	public WebElement groupSection;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_sub_group']")
	public WebElement groupTable;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_sub_group']//tbody/tr")
	public List<WebElement> groupTableRow;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_group_user']")
	public WebElement userSection;
	
	@FindBy(how=How.XPATH, using="//button[contains(text(),'Add User')]")
	public WebElement addUserButton;

	@FindBy(how=How.XPATH, using="//table[@id='table_group_user']//td//input")
	public List<WebElement> userInputTextFields;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_group_user']//td//select")
	public List<WebElement> userInputDropdowns;

	@FindBy(how=How.XPATH, using="(//table[@id='table_group_user']//td//form[@aria-hidden='false']//button)[1]")
	public WebElement submitButton;
	
	@FindBy(how=How.XPATH, using="//table[@id='table_group_user']/ancestor::div[@ng-show=\"isUserPanelOpen\"]/div[contains(@class,\"table-responsive\")]")
	public WebElement userScrollableDiv;
	
	@FindBy(how=How.XPATH, using="//input[contains(@class,'bootbox-input-text')]")
	public WebElement deleteGroupPopup;
	
	@FindBy(how=How.XPATH, using="//button[contains(text(),'OK')]")
	public WebElement deleteGroupPopupOKButton;
	
	@FindBy(how=How.XPATH, using="//div[contains(@class,'ui-pnotify-closer')]")
	public WebElement notificationCloser;

	@FindBy(how=How.XPATH, using="//div[contains(@class,'alert')]")
	public List<WebElement> notificationAlert;
}
