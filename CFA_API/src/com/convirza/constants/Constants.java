package com.convirza.constants;

import java.io.File;

public class Constants {
	public static final String PROJECT_DIR = System.getProperty("user.dir") + File.separator;
	public static final String TEST_DATA_DIR = "TestData";
	public static final String RESOURCES = "resources";
	public static final String CONF_DIR = "conf";
	public static final String DYNAMIC_TD_FILE = "DynamicTestData";
	public static final String CALLFLOW_TEMPLETE = "CallFlowTemplete.yaml";
	public static final String POSTGRES_CONFIG = "PostgresConfig.properties";
	public static final String DRIVER = "driver";
	public static final String CHROME_DRIVER = "chromedriver";
	public static final String PHANTOM_DRIVER = "phantomjs";
	public static final String HTTPS = "https://";
	public static final int GROUP_LEVEL = 3;
	public static final int MAX_LIMIT = 2000;
	public static final String TEST_FILE_MP3 = "testfile.mp3";
	public static final String TEST_FILE_5MB_MP3 = "testfile_5mb.mp3";
	public static final String TEST_FILE_55MB_MP3 = "testfile_55mb.mp3";
	public static final String TEST_FILE_WAV = "testfile.wav";
	public static final String TEST_FILE_5MB_WAV = "testfile_5mb.wav";
	public static final String TEST_FILE_WV = "testfile.wv";
	public static final String TEST_FILE_XLS = "staging.xls";
	public static final String TEST_FILE_MP4 = "testfile.mp4";
	public static final String TEST_FILE_BDX = "bdx_sample_file.mp3";
	
	public static class ComponentStatus {
		public static final String ACTIVE = "active";
		public static final String INACTIVE = "inactive";
		public static final String DELETED = "deleted";
		public static final String SUSPENDED = "suspended";
		public static final String PROVISIONED = "provisioned";
		public static final String UNPROVISIONED = "unprovisioned";
	}

	public static class GroupHierarchy {
		public static final String AGENCY = "agency";
		public static final String COMPANY = "company";
		public static final String LOCATION = "location";
	}
	
	public static class CallFlowCategory {
		public static final String POOL = "pool";
		public static final String GEO = "geo";
		public static final String IVR = "ivr";
		public static final String HANGUP = "hangup";
		public static final String PERCENTAGE = "percentage";
		public static final String SIMPLE = "simple";
	}
	
	public static class PostGresConfigConstants {
		public static final String CONNECTION_URL = "connection_url";
		public static final String DATABASE = "database";
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		public static final String DRIVER = "driver";
	}
}
