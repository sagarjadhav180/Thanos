package com.convirza.cfa_testdata;

import java.io.File;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.convirza.constants.Constants;
import com.convirza.constants.FileConstants;
import com.convirza.core.utils.PostGresConnection;
import com.convirza.tests.core.io.PropertiesReader;

public class TestDataUtil {

	private static PostGresConnection postgres;
	static Connection connection;
	
	public static int getInvocationCount(String entity) throws SQLException {
		int invocationCount= 0;

		Connection connection = getConnectionForTestDataDB();
		
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM userdetail_saveuserdata ORDER BY id DESC LIMIT 1");
		
		try {
			while(resultSet.next()) {
				invocationCount = resultSet.getInt(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return invocationCount;
	}

	
	public static String getCredentails(String entity) {
		String Credentails = "";

		Connection connection = getConnectionForTestDataDB();

		ResultSet resultSet = null;
		
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM userdetail_logindata ORDER BY id DESC LIMIT 1");
//			resultSet = postgres.getResultSet("SELECT * FROM contactmodule_userlogindata ORDER BY id DESC LIMIT 1");			
		}catch(Exception e) {
			e.printStackTrace();
		}

		try {
			while(resultSet.next()) {
				Credentails = resultSet.getString(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return Credentails;
	}
	
	
	public static List getComponentsToAdd(String entity) throws SQLException {
		List<String> componentsToAdd= new ArrayList<String>();

		Connection connection = getConnectionForTestDataDB();

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM userdetail_saveuserdata ORDER BY id DESC LIMIT 1");
		
		try {
			while(resultSet.next()) {
				Array entityToAdd = resultSet.getArray("components");
				componentsToAdd.add(entityToAdd.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return componentsToAdd;
	}

	
	public static List getComponentsToRemove(String entity) {
		List<String> componentsToRemove= new ArrayList<String>();

		Connection con = getConnectionForTestDataDB();

		ResultSet resultSet = postgres.getResultSet("");

		try {
			while(resultSet.next()) {
				Array entityToAdd = resultSet.getArray("entiry");
				componentsToRemove.add(entityToAdd.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			}
		return componentsToRemove;
	}
	
	public static Connection getConnectionForTestDataDB() {
		try {
			PropertiesReader dbConfig = new PropertiesReader();
			Properties dbConnection = dbConfig.readProperties(FileConstants.getPostgresConfigFile());
			
			String dbUrl = "jdbc:postgresql://test.cgr22uvzuj9v.us-east-2.rds.amazonaws.com:5432/test_database";
			String username = "postgres";
			String password = "Password12345";
					
			connection=DriverManager.getConnection(dbUrl,username,password);	
			
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
