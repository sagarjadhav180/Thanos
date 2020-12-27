package com.convirza.tests.selenium.scripts;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import com.convirza.tests.selenium.pagefactory.GroupAndUserPage;
import com.convirza.tests.selenium.utils.JSExecuterHelper;
import com.convirza.tests.selenium.utils.WaitExecuter;

public class Notification {
	private GroupAndUserPage groupObj;
	private Actions action;
	private WebDriver driver;
	
	public Notification(WebDriver driver) {
		groupObj = PageFactory.initElements(driver, GroupAndUserPage.class);
		action = new Actions(driver);
		this.driver = driver;
	}
	public void closeNoticationIfExist() {
		if (groupObj.notificationAlert.size() > 0) {
			action.moveToElement(groupObj.notificationAlert.get(0));
			Map<String,String> map = new HashMap<String, String>();
			map.put("visibility", "visible");
			JSExecuterHelper.addStyleToElement(driver, groupObj.notificationCloser, map);
			action.moveToElement(groupObj.notificationCloser);
			WaitExecuter.sleep(500);
			groupObj.notificationCloser.click();
		} else {
			System.err.println("No notication present");
		}
	}
}
