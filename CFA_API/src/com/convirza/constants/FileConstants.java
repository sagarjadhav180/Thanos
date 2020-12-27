package com.convirza.constants;

import java.io.File;
import java.io.IOException;

import common.HelperClass;

public class FileConstants {

	public static class FileExtention {
		public static final String XLS = ".xls";
		public static final String JSON = ".json";
		public static final String YAML = ".yaml";
	}
	
	public static String getExcelTestDataFile() {
		String fileName = "";
		try {
			if (HelperClass.current_environment().contains("staging"))
				fileName = HelperClass.current_environment() + ".xls";
			else if (HelperClass.current_environment().equals("production"))
				fileName = "Production.xls";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Directory.getExcelTestDataDir() + fileName;
	}
	
	public static String getYamlTestDataConfig() {
		String filePath = "";
		try {
			filePath = Constants.PROJECT_DIR + Constants.RESOURCES + File.separator 
				+ Constants.CONF_DIR + File.separator 
				+ Constants.DYNAMIC_TD_FILE + FileConstants.FileExtention.YAML;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}	
	
	public static String getCallFlowTempleteConfig() {
		String filePath = Constants.PROJECT_DIR + Constants.RESOURCES + File.separator 
			+ Constants.CONF_DIR + File.separator 
			+ Constants.CALLFLOW_TEMPLETE;
		return filePath;
	}
	
	public static String getPostgresConfigFile() {
		String filePath = Directory.confDir() + Constants.POSTGRES_CONFIG;
		return filePath;
	}
	
	public static String getChromeDriver() {
		String filePath = Constants.PROJECT_DIR + Constants.DRIVER + File.separator 
			+ Constants.CHROME_DRIVER;
		return filePath;
	}
	
	public static String getPhantomDriver() {
		String filePath = Constants.PROJECT_DIR + Constants.DRIVER + File.separator 
			+ Constants.PHANTOM_DRIVER;
		return filePath;
	}
	
	
	public static String getMP3File(String size) {
		String filePath;
		if(size.equals("5mb")) {
			filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
					+ Constants.TEST_FILE_5MB_MP3;			
		}
		else if(size.equals("55mb")) {
			filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
					+ Constants.TEST_FILE_55MB_MP3;			
		}
		else
			filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_MP3;

		return filePath;
	}

	
	public static String getWAVFile(String size) {
		String filePath;
		
		if(size.equals("5mb")) {
			filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
					+ Constants.TEST_FILE_5MB_WAV;			
		}
		else
			filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_WAV;

		return filePath;
	}
	
	
	public static String getWVFile() {
		String filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_WV;
		return filePath;
	}
	
	public static String getBDXFile() {
		String filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_BDX;
		return filePath;
	}
	
	public static String getMP4File() {
		String filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_MP4;
		return filePath;
	}
	
	public static String getXLSFile() {
		String filePath = Constants.PROJECT_DIR + Constants.TEST_DATA_DIR + File.separator 
			+ Constants.TEST_FILE_XLS;
		return filePath;
	}
	
	public static String callUploadDateFormat() {
		String dateFormat = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'";
		return dateFormat;
	}
}
