package com.convirza.tests.selenium.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import com.convirza.tests.selenium.pagefactory.WebhookPage;
import com.convirza.tests.selenium.pagefactory.LeftPanelObject;
import com.convirza.tests.selenium.utils.JSExecuterHelper;
import com.convirza.tests.selenium.utils.WaitExecuter;

public class Webhook {
	private LeftPanelObject leftPanel = null;
	private WebhookPage webhookPage = null;
	private WebDriver driver;
	private Actions action;
	
	public Webhook(WebDriver driver) {
		this.driver = driver;
		initializePageObject();
		action = new Actions(driver);
	}
	
	public void initializePageObject() {
		leftPanel = PageFactory.initElements(driver, LeftPanelObject.class);
		webhookPage = PageFactory.initElements(driver, WebhookPage.class);
	}
	
	public void goToWebhookPage() {
		leftPanel.toogleLeftButton.click();
		leftPanel.settingsLink.click();
		WaitExecuter.sleep(1000);
		leftPanel.customizationLink.click();
		WaitExecuter.sleep(1000);
		leftPanel.webhookLink.click();
	}
	
	public void closeNoticationIfExist() {
		if (webhookPage.notificationAlert.size() > 0) {
			action.moveToElement(webhookPage.notificationAlert.get(0));
			Map<String,String> map = new HashMap<String, String>();
			map.put("visibility", "visible");
			JSExecuterHelper.addStyleToElement(driver, webhookPage.notificationCloser, map);
			action.moveToElement(webhookPage.notificationCloser);
			WaitExecuter.sleep(1000);
			webhookPage.notificationCloser.click();
		} else {
			System.err.println("No notication present");
		}
	}
	
//	public void deleteWebhook() {
//		WaitExecuter.sleep(3000);
//		goToWebhookPage();		
//		WaitExecuter.sleep(2000);
//		WebElement webhookListTable = webhookPage.webhookTable;
//		WebElement webhookTbody = webhookListTable.findElement(By.tagName("tbody"));
//		List<WebElement> tableRows = webhookTbody.findElements(By.tagName("tr[position()>1]"));
//		System.out.println(webhookTbody.findElements(By.tagName("tr")));
//		for (int i=0; i<tableRows.size(); i++) {
//			WebElement row = tableRows.get(i);
//			System.out.println(row.findElement(By.xpath("td[1]")));
//			String webhookName = row.findElement(By.xpath("td[1]")).getText();
//			if (webhookName.equalsIgnoreCase("Agency Webhook API")) {
//				WebElement deleteButton = row.findElement(By.xpath("//td[4]/div/button[contains(text(),'Delete')]"));
//				deleteButton.click();
//				WaitExecuter.sleep(2000);
//				webhookPage.webhookModalDeleteOKButton.click();
//				WaitExecuter.sleep(2000);
//				closeNoticationIfExist();
//				webhookListTable = webhookPage.webhookTable;
//				webhookTbody = webhookListTable.findElement(By.tagName("tbody"));
//				tableRows = webhookTbody.findElements(By.tagName("tr"));
//				i = -1;
//			}
//		}
//	
//	}
	
	public void deleteWebhook() {
		WaitExecuter.sleep(3000);
		goToWebhookPage();		
		WaitExecuter.sleep(2000);
	
	    int rows = driver.findElements(By.xpath("//thead/parent::table//tbody//tr[position()>1]")).size();
		for(int j=0;j<rows;j++) {
			if(	driver.findElement(By.xpath("//thead/parent::table//tbody//tr[position()>1]//td[1]")).getText().equals("Agency Webhook API")) {
				WebElement deleteButton = driver.findElement(By.xpath("//thead/parent::table//tbody//tr[position()>1]//td[4]/div/button[contains(text(),'Delete')]"));
				deleteButton.click();
				WaitExecuter.sleep(2000);
				webhookPage.webhookModalDeleteOKButton.click();
				WaitExecuter.sleep(2000);
				closeNoticationIfExist();
			}
		}
		
	}
	
	
	
	
}