package com.convirza.tests.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestNGMethod;

import com.convirza.constants.Directory;
import com.convirza.constants.TestCaseConstants;

public class TestCaseResultWriter implements ExcelWriter {
	FileOutputStream outputStream;
	Workbook workbook = null;
	String status = "";
	public TestCaseResultWriter(String status) {
		this.status = status;
	}
	
	public String getWorkbookPath() {
		if (status.equalsIgnoreCase(TestCaseConstants.Status.PASS))
			return Directory.ExcelResult.getPassedTestResultFile();
		else  
			return Directory.ExcelResult.getFailedTestResultFile();
	}
	
	public String getTodayDate() {
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return dateformat.format(date);
	}
	
	public CellStyle getHeaderStyle() {
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.index);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		return headerStyle;
	}
	
	@Override
	public Workbook getWorkbook() throws IOException {
		ZipSecureFile.setMinInflateRatio(0);
		String filePath = getWorkbookPath();
		
		//Create a object of File class to open xlsx file
        File file = new File(filePath);
        if (!file.exists()) {
            outputStream = new FileOutputStream(filePath,true);
	        //Find the file extension by spliting file name in substing and getting only extension name
	        String fileExtensionName = filePath.substring(filePath.indexOf("."));
	        
	        if(fileExtensionName.equals(".xlsx")){
	        	//If it is xlsx file then create object of XSSFWorkbook class
	        	workbook = new XSSFWorkbook();
	        }
	        else if(fileExtensionName.equals(".xls")){
	            //If it is xls file then create object of HSSFWorkbook class
	        	workbook = new HSSFWorkbook();
	        }
        } else {
        	outputStream = new FileOutputStream(filePath,true);

	        String fileExtensionName = filePath.substring(filePath.indexOf("."));
	        
	        if(fileExtensionName.equals(".xlsx")){
	        	//If it is xlsx file then create object of XSSFWorkbook class
	        	workbook = (XSSFWorkbook)WorkbookFactory.create(file);
	        } else if(fileExtensionName.equals(".xls")){
	            //If it is xls file then create object of HSSFWorkbook class
	        	workbook = (HSSFWorkbook)WorkbookFactory.create(file);
	        }

	        if (workbook.getNumberOfSheets() > 29) {
	        	workbook.removeSheetAt(0);
	        } 
        }
        return workbook;
	}

	@Override
	public Sheet getSheet() {
        //Read excel sheet by sheet name
		String sheetName = getTodayDate();
		Sheet sheet;
		if (workbook.getSheet(sheetName) != null) {
			return workbook.getSheet(sheetName);
		} else {
			sheet = createSheet(sheetName);
		}
        return sheet;
	}

	public void addTestCasesToReport(List<ITestNGMethod> testCases, Map<String,List<String>> methodAndRequestDetails) throws IOException {
		workbook = getWorkbook();
		Sheet sheet = getSheet();
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		
		int count = 0;
		Row header = sheet.createRow(count);
		Cell testCase = header.createCell(0);
		testCase.setCellValue("TestCase Name");
		testCase.setCellStyle(getHeaderStyle());

		Cell testStatus = header.createCell(1);
		testStatus.setCellValue("Status");
		testStatus.setCellStyle(getHeaderStyle());
		
		Cell url = header.createCell(2);
		url.setCellValue("Request URL");
		url.setCellStyle(getHeaderStyle());
		
		Cell params = header.createCell(3);
		params.setCellValue("Request Parameters");
		params.setCellStyle(getHeaderStyle());
		
		Row row;
		for (ITestNGMethod method : testCases) {
			row = sheet.createRow(++count);
			row.createCell(0).setCellValue(method.getMethodName());
			row.createCell(1).setCellValue(status.toUpperCase());
			row.createCell(2).setCellValue(methodAndRequestDetails.get(method.getMethodName()).get(0));
			row.createCell(3).setCellValue(methodAndRequestDetails.get(method.getMethodName()).get(1));
		}
		workbook.write(outputStream);
		outputStream.close();
		workbook.close();
	}
	
	public Sheet createSheet(String sheet) {
		Sheet newSheet = workbook.createSheet(sheet);
		workbook.setActiveSheet(workbook.getSheetIndex(newSheet));
		return newSheet;
	}
	
	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

}
