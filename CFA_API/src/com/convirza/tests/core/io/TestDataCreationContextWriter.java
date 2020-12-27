package com.convirza.tests.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.convirza.constants.FileConstants;

import common.HelperClass;

public class TestDataCreationContextWriter {

	public Workbook getWorkbook() throws IOException {		
		//Create a object of File class to open xlsx file
		FileInputStream inputStream = new FileInputStream(new File(FileConstants.getExcelTestDataFile()));
		Workbook workbook = WorkbookFactory.create(inputStream);
		inputStream.close();
		return workbook;
	}

	public void setValue(String className, String methodName, String value) throws IOException {
		Workbook workbook = getWorkbook();
		String sheetName = className;
		//Read sheet inside the workbook by its name
		Sheet sheet = workbook.getSheet(sheetName);
		//Find number of rows workbook excel file
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();

		//Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount+1; i++) {
			Row row = sheet.getRow(i);	  
			if (row != null) {
				if (row.getCell(0).getStringCellValue().equals(methodName)) {
					int lastCell = row.getLastCellNum();
					Cell cell = row.createCell(lastCell);
					cell.setCellValue("Test");
				} else
					continue;
			}
			//Create a loop to print cell values in a row
		}

		FileOutputStream outputStream = new FileOutputStream(new File(FileConstants.getExcelTestDataFile()));
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
