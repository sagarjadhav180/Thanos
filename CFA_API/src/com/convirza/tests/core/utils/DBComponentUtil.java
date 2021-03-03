package com.convirza.tests.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.convirza.core.utils.PostGresConnection;

public class DBComponentUtil {
	private static PostGresConnection postgres;

	public static void addComponent(Map<String,String> queries) {

		postgres = new PostGresConnection();
		postgres.getConnection();
		
		for( String query:queries.values()) {
			ResultSet resultSet = postgres.getResultSet(query);			
		}
		
	}

	public static void removeComponent(Map<String,String> queries) {

		postgres = new PostGresConnection();
		postgres.getConnection();
		
		for( String query:queries.values()) {
			ResultSet resultSet = postgres.getResultSet(query);			
		}

	}

}
