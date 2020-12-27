package com.convirza.tests.selenium.scripts;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.convirza.constants.FileConstants;

public class BrowserInitializer {
	
	public static WebDriver driver;
	
	public static WebDriver initialize(String browser) {
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", FileConstants.getChromeDriver());
			driver = new ChromeDriver();
		} else if (browser.equals("phantom")) {
		  DesiredCapabilities caps = new DesiredCapabilities();
		  caps.setJavascriptEnabled(true); 
		  caps.setCapability("takesScreenshot", false);
		  caps.setCapability(
		  PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
		  FileConstants.getPhantomDriver());
		  caps.setCapability("ignore-ssl-errors", true);
		  driver = new PhantomJSDriver(caps);
		}
		
		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		return driver;
	}	
}
