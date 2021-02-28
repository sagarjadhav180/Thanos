package com.convirza.cfa_testdata;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.core.utils.PostGresConnection;
import com.convirza.tests.core.io.PropertiesReader;

public class TestDataUtil {

	private static PostGresConnection postgres;
	static Connection connection;
	
	public static String getInvocationCount(String entity) {
		String invocationCount= "";

		Connection con = getConnectionForTestDataDB();

		ResultSet resultSet = postgres.getResultSet("");

		try {
			while(resultSet.next()) {
				invocationCount = resultSet.getString("call_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return invocationCount;
	}


	public static Connection getConnectionForTestDataDB() {
		try {
			PropertiesReader dbConfig = new PropertiesReader();
			Properties dbConnection = dbConfig.readProperties(FileConstants.getPostgresConfigFile());
			
			connection = DriverManager.getConnection(dbConnection.getProperty(Constants.PostGresConfigConstants.CONNECTION_URL) + File.separator 
					+ dbConnection.getProperty(Constants.PostGresConfigConstants.DATABASE) 
					, dbConnection.getProperty(Constants.PostGresConfigConstants.USERNAME)
					, dbConnection.getProperty(Constants.PostGresConfigConstants.PASSWORD)); 
			
			Class.forName(dbConnection.getProperty(Constants.PostGresConfigConstants.DRIVER));
			
			System.out.println("Connected to PostgreSQL database!");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
}
