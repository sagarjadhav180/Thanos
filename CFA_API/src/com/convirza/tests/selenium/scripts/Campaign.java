package com.convirza.tests.selenium.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import com.convirza.tests.selenium.pagefactory.CampaignPage;
import com.convirza.tests.selenium.pagefactory.LeftPanelObject;
import com.convirza.tests.selenium.utils.JSExecuterHelper;
import com.convirza.tests.selenium.utils.WaitExecuter;
import com.convirza.utils.Pendo;

public class Campaign {
	private LeftPanelObject leftPanel = null;
	private CampaignPage campaignPage = null;
	private WebDriver driver;
	private Actions action;
	
	public Campaign(WebDriver driver) {
		this.driver = driver;
		action = new Actions(driver);
		initializePageObject();
	}
	
	public void initializePageObject() {
		leftPanel = PageFactory.initElements(driver, LeftPanelObject.class);
		campaignPage = PageFactory.initElements(driver, CampaignPage.class);
	}
	
	public void goToCampaignPage() {
		WaitExecuter.sleep(2000);
		leftPanel.campaignAndCallflowLink.click();
	}
	
	public void removeGuideDivIfPresent() {
		if (campaignPage.guideDiv.size() > 0) {
			campaignPage.guideDiv.get(0).click();
		}
	}
	
	public void closeNoticationIfExist() {
		if (campaignPage.notificationAlert.size() > 0) {
			action.moveToElement(campaignPage.notificationAlert.get(0));
			Map<String,String> map = new HashMap<String, String>();
			map.put("visibility", "visible");
			JSExecuterHelper.addStyleToElement(driver, campaignPage.notificationCloser, map);
			action.moveToElement(campaignPage.notificationCloser);
			campaignPage.notificationCloser.click();
		} else {
			System.err.println("No notication present");
		}
	}
	
	public void goToPageHeading() {
		try {
			action.moveToElement(campaignPage.pageHeading).build().perform();
		} catch(Exception e) {
			// To Do
		}
	}

//	public void deleteCampaign() {
//		goToCampaignPage();
//		WaitExecuter.sleep(10000);
////		removeGuideDivIfPresent();
//		
//		WebElement campaignListTable = campaignPage.campaignTable;
//		WebElement campaignTbody = campaignListTable.findElement(By.tagName("tbody"));
//		List<WebElement> tableRows = campaignTbody.findElements(By.tagName("tr"));
//		for (int i=0; i<tableRows.size(); i++) {
//			WebElement row = tableRows.get(i);
//			if(row.findElement(By.xpath("td[1]")).getText().equals("No Data Found"))
//				continue;
//			String campaignName = row.findElement(By.xpath("td[3]")).getText();
//			if (campaignName.equalsIgnoreCase("Agency Campaign API")) {
//				WebElement deleteButton = row.findElement(By.xpath("//td[9]/span/button[contains(text(),'Archive')]"));
//				deleteButton.click();
//				WaitExecuter.sleep(2000);
//				campaignPage.campaignModalDeleteOKButton.click();
//				goToPageHeading();
//				WaitExecuter.sleep(2000);
//				closeNoticationIfExist();
//				
////				campaignListTable = campaignPage.campaignTable;
////				campaignTbody = campaignListTable.findElement(By.tagName("tbody"));
////				tableRows = campaignTbody.findElements(By.tagName("tr"));
//				if(i>0) {
//					i = -1;					
//				}
//
//			}
//		}
//	}
	
	public void deleteCampaign() {
		goToCampaignPage();
//		Actions action = new Actions(driver);
//		action.moveByOffset(100, 0).perform();
//		action.sendKeys(Keys.ESCAPE).perform();
//		Pendo.closePopUp("//button[@class='_pendo-close-guide']");
		WaitExecuter.sleep(5000);
//		removeGuideDivIfPresent();

		try {
			int rows = driver.findElements(By.xpath("//thead/parent::table//tbody//tr")).size();
			
			for(int i=0;i<rows;i++) {
				if(	driver.findElement(By.xpath("//thead/parent::table//tbody//tr//td[3]//span[1]")).getText().startsWith("Agency Campaign API")) {
					WebElement deleteButton = driver.findElement(By.xpath("//thead/parent::table//tbody//tr//td[3]//span[starts-with(text(),'Agency Campaign API')]//ancestor::tr//td[9]//button[text()='Archive']"));
					deleteButton.click();
					WaitExecuter.sleep(2000);
					campaignPage.campaignModalDeleteOKButton.click();
					goToPageHeading();
					WaitExecuter.sleep(2000);
					closeNoticationIfExist();
					WaitExecuter.sleep(5000);
		
			   }
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			goToPageHeading();
		}
	    
	}
	
}
