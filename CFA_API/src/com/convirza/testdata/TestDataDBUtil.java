package com.convirza.testdata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.convirza.testdata.PGConnection;

public class TestDataDBUtil {

	  private static PGConnection postgres;

	  
	  public static String getFeatureDetails(String column) {
			
		String var= "";
		postgres = new PGConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = postgres.getResultSet("");

		try {
			while(resultSet.next()) {
				  //use switch case to get required data
				var = resultSet.getString(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    	}
		
		return var;
	  }

	  
	  
}
