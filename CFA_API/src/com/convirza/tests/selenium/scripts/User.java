package com.convirza.tests.selenium.scripts;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.RandomContentGenerator;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.selenium.pagefactory.GroupAndUserPage;
import com.convirza.tests.selenium.utils.JSExecuterHelper;
import com.convirza.tests.selenium.utils.KeyEvent;
import com.convirza.tests.selenium.utils.WaitExecuter;

public class User {
	private static TestDataYamlReader yamlReader;
	
	/**
	 * @author ankur
	 * @param driver : WebDriver instance after login
	 * @param group : Group hierarchy where user will be created
	 */
	
	public static Map<String,String> createUser(WebDriver driver, String group) {
		yamlReader = new TestDataYamlReader();
		
		Group groupObj = new Group(driver);
		groupObj.clickOnGroupLink(driver);
		groupObj.goToGroupSection(driver);
		
		GroupAndUserPage groupPageObj = PageFactory.initElements(driver, GroupAndUserPage.class);
		Map<String, Object> confGroupHierarchy = yamlReader.readGroupInfo(group);

		for (int i=0; i<groupPageObj.groupTableRow.size(); i++) {
			WebElement tableRow = groupPageObj.groupTableRow.get(i);
			WebElement tableData = tableRow.findElements(By.tagName("td")).get(0);
			if (tableData.getText().contains(confGroupHierarchy.get("group_id").toString())) {
				WebElement groupselectButton = tableRow.findElement(By.xpath("td[9]/div/button[1]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(groupselectButton).build().perform();
				JSExecuterHelper.clickOnElement(driver, groupselectButton);
				break;
			}else continue;
		}
		
		goToUserSection(driver, groupPageObj);
		
		WaitExecuter.sleep(5000);
		Map<String, Object> confUserHierarchy = yamlReader.readUserInfo(group);
		
		String email = RandomContentGenerator.createEmail();
		String phoneNumber = RandomContentGenerator.createPhoneNumber();
		groupPageObj.userInputTextFields.get(0).sendKeys(confUserHierarchy.get(TestDataYamlConstants.UserConstants.FIRST_NAME).toString());
		groupPageObj.userInputTextFields.get(1).sendKeys(confUserHierarchy.get(TestDataYamlConstants.UserConstants.LAST_NAME).toString());
		groupPageObj.userInputTextFields.get(2).sendKeys(email);
		groupPageObj.userInputTextFields.get(3).sendKeys(phoneNumber);
		
		new Select (groupPageObj.userInputDropdowns.get(0)).selectByVisibleText(confUserHierarchy.get(TestDataYamlConstants.UserConstants.ROLE).toString());
		new Select (groupPageObj.userInputDropdowns.get(1)).selectByVisibleText(confUserHierarchy.get(TestDataYamlConstants.UserConstants.STATUS).toString());
		
		KeyEvent.pressTab(groupPageObj.userInputDropdowns.get(1));
		KeyEvent.pressTab(groupPageObj.userInputDropdowns.get(1));
		JSExecuterHelper.clickOnElement(driver, groupPageObj.submitButton);
		
		Notification notification = new Notification(driver);
		notification.closeNoticationIfExist();
		
		groupObj.goToPageHeading();
		
		Map<String, String> map = new HashedMap<String, String>();
		map.put(TestDataYamlConstants.UserConstants.GROUP_ID, confGroupHierarchy.get("group_id").toString());
		map.put(TestDataYamlConstants.UserConstants.EMAIL, email);
		map.put(TestDataYamlConstants.UserConstants.PHONE_NUMBER, phoneNumber);
		
		return map;
	}
	
	public static void goToUserSection(WebDriver driver, GroupAndUserPage groupObj) {
		Actions action = new Actions(driver);
		action.moveToElement(groupObj.userSection).build().perform();
		WaitExecuter.sleep(4000);
		JSExecuterHelper.clickOnElement(driver, groupObj.addUserButton);
	}
}
