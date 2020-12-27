package com.convirza.core.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.tests.core.io.PropertiesReader;

public class PostGresConnection implements DBConnection {
	Connection connection;
	
	public Connection getConnection() {
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
	
	public ResultSet getResultSet(String query) {
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			
		}
		return resultSet;
	}
	
	public int updateQuery(String query) {
		int result = 0;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(query);
		} catch (Exception e) {
			
		}
		return result;
	}
}	
