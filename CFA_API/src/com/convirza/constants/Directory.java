package com.convirza.constants;

import java.io.File;

import common.FileUtils;
import common.HelperClass;

public class Directory {
	public static String projectDir = System.getProperty("user.dir") + File.separator;
	
	public static class ExcelResult {
		public static String failedWorkbook = "failedTestResult.xlsx";
		public static String passedWorkbook = "passedTestResult.xlsx";
		public static String resultDir = "Result" + File.separator;
		public static String excelReportDir = resultDir + "Excel" + File.separator;
		
		public static String getFailedTestResultFile() {
			FileUtils.createDir(excelReportDir);
			return projectDir + excelReportDir + failedWorkbook;
		}
		
		public static String getPassedTestResultFile() {
			FileUtils.createDir(excelReportDir);
			return projectDir + excelReportDir + passedWorkbook;
		}
	}
	
	public static class HtmlResult {
		public static String htmlResult = "CFA_API_Result.html";
		public static String resultDir = "Result" + File.separator;
		public static String htmlReportDir = resultDir + "html" + File.separator;
		
		public static String gethtmlResultFile() {
			FileUtils.createDir(htmlReportDir);
			return projectDir + htmlReportDir + htmlResult;
		}
	}
	
	public static String getExcelTestDataDir() {
			String filePath = projectDir + "TestData" + File.separator;
			return filePath;
	}
	
	public static String confDir() {
		String confDir = Constants.PROJECT_DIR + Constants.RESOURCES + File.separator 
		+ Constants.CONF_DIR + File.separator; 
		return confDir;
	}
}
