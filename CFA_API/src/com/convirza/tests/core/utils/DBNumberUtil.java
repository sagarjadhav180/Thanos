package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.convirza.core.utils.PostGresConnection;

public class DBNumberUtil {

	private static PostGresConnection postgres;
	
	public static void makePremiumNumber(String orgUnitID, String numberID) {
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		
		String query = "";
		ResultSet resultSet = postgres.getResultSet(query);
	
	}
	
}
