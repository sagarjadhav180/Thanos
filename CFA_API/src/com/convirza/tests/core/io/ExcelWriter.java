package com.convirza.tests.core.io;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelWriter {

	public Workbook getWorkbook() throws IOException;
	public Sheet getSheet();
	public void write();
	public Sheet createSheet(String sheet);
	
}
