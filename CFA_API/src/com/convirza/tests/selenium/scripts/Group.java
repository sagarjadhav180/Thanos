package com.convirza.tests.selenium.scripts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.convirza.tests.selenium.pagefactory.GroupAndUserPage;
import com.convirza.tests.selenium.pagefactory.LeftPanelObject;
import com.convirza.tests.selenium.utils.JSExecuterHelper;
import com.convirza.tests.selenium.utils.WaitExecuter;

public class Group {
	private LeftPanelObject leftPanelObj;
	private GroupAndUserPage groupObj;
	WebDriverWait wait = null;	
	Actions action;
	private WebDriver driver;
	
	public Group(WebDriver driver) {
		this.driver = driver;
		loadPageObject();
		wait = new WebDriverWait(driver, 60);
		action = new Actions(driver);
	}
	
	public void loadPageObject() {
		groupObj = PageFactory.initElements(driver, GroupAndUserPage.class);
		leftPanelObj = PageFactory.initElements(driver, LeftPanelObject.class);
	}
	
	public void clickOnGroupLink(WebDriver driver) {	
		WaitExecuter.sleep(2000);		
		wait.until(ExpectedConditions.visibilityOf(leftPanelObj.groupAndUserLink));
		try {
			leftPanelObj.groupAndUserLink.click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void goToGroupSection(WebDriver driver) {
		WaitExecuter.sleep(2000);
		action.moveToElement(groupObj.groupSection).build().perform();
	}
	
	public void goToPageHeading() {
		WaitExecuter.sleep(2000);
		try {
			action.moveToElement(groupObj.pageHeading).build().perform();
		} catch (StaleElementReferenceException ex) {
			action.moveToElement(driver.findElement(By.xpath("//h1"))).build().perform();
		}
	}
	
//	public void deleteGroup(WebDriver driver) {
//		clickOnGroupLink(driver);
//		goToGroupSection(driver);
//
//		for (int i=0; i<groupObj.groupTableRow.size(); i++) {
//			WebElement tableRow = groupObj.groupTableRow.get(i);
//			if(tableRow.findElement(By.xpath("td[1]")).getText().equals("No Data Found"))
//				continue;
//			String groupName = tableRow.findElement(By.xpath("td[2]")).getText();
//			System.out.println(groupName);
//			if (groupName.contains("API Company Group")) {
//				WebElement groupselectButton = tableRow.findElement(By.xpath("td[9]/div/button[3]"));
//				JSExecuterHelper.clickOnElement(driver, groupselectButton);
//				WaitExecuter.sleep(3000);				
//				groupObj.deleteGroupPopup.sendKeys("yes");
//				JSExecuterHelper.clickOnElement(driver, groupObj.deleteGroupPopupOKButton);
//				WaitExecuter.sleep(2000);
//				Notification notification = new Notification(driver);
//				notification.closeNoticationIfExist();
//				
//
//			}
//		}
//		goToPageHeading();
//	}
	
	
	public void deleteGroup(WebDriver driver) {
		clickOnGroupLink(driver);
		goToGroupSection(driver);
		
		int rows = driver.findElements(By.xpath("//table[@id='table_sub_group']//tbody//tr")).size();
		
	    try {
	    	for(int i=0;i<rows;i++) {
				if(driver.findElement(By.xpath("//table[@id='table_sub_group']//tbody//tr//td[2]//span")).getText().equals("API Company Group") || driver.findElement(By.xpath("//table[@id='table_sub_group']//tbody//tr//td[2]//span")).getText().equals("PostGroupAPI")) {
					
					WebElement groupselectButton = driver.findElement(By.xpath("//table[@id='table_sub_group']//tbody//tr//td[2]//span//ancestor::tr//td[9]/div/button[3]"));
					JSExecuterHelper.clickOnElement(driver, groupselectButton);
					WaitExecuter.sleep(3000);				
					groupObj.deleteGroupPopup.sendKeys("yes");
					JSExecuterHelper.clickOnElement(driver, groupObj.deleteGroupPopupOKButton);
					WaitExecuter.sleep(2000);
					Notification notification = new Notification(driver);
					notification.closeNoticationIfExist();
		
			   }
			}
	    	
	    }catch(Exception e){
	    	
	    }finally {
	    	goToPageHeading();
	    }
			
	}
	
	
}
