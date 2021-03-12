package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.convirza.core.utils.PostGresConnection;

public class DBNumberUtil {

	private static PostGresConnection postgres;
	
	public static void makePremiumNumber(String orgUnitID, String numberID, String number) {
		String componentIdFor6thQuery = null;
		String componentIdFor7thQuery = null;
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
				
		String query1 = "Insert into component (component_name,component_desc) values ('"+number+" Reserved number','"+number+"')";
		System.out.println(query1);
		ResultSet resultSet1 = postgres.insertQuery(query1);

		//getting componentIdFor6thQuery
		ResultSet geteRsultSet1 = postgres.getResultSet("SELECT * FROM component WHERE component_name LIKE '"+number+" Reserved number'");
		try {
			while(geteRsultSet1.next()) {
				componentIdFor6thQuery = geteRsultSet1.getString("component_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String query2 = "Insert into component (component_name,component_desc) values ('"+number+" Reserved minute','"+number+"')";
		ResultSet resultSet2 = postgres.insertQuery(query2);
		
		//getting componentIdFor6thQuery
		ResultSet geteRsultSet2 = postgres.getResultSet("SELECT * FROM component WHERE component_name LIKE '"+number+" Reserved minute'");
		
		try {
			while(geteRsultSet2.next()) {
				componentIdFor7thQuery = geteRsultSet2.getString("component_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query3 = "Insert into org_component (org_unit_id,component_id,number_id,component_type) values ("+orgUnitID+", "+componentIdFor6thQuery+", "+componentIdFor7thQuery+",'number')";
		ResultSet resultSet3 = postgres.insertQuery(query3);
		
		String query4 = "Insert into org_component (org_unit_id,component_id,number_id,component_type) values ("+orgUnitID+", "+componentIdFor6thQuery+", "+componentIdFor7thQuery+",'minute')";
		ResultSet resultSet4 = postgres.insertQuery(query4);
	
	}
	
}
