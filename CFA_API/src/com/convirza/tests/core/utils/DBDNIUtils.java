package com.convirza.tests.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.convirza.core.utils.PostGresConnection;

public class DBDNIUtils {

	
private static PostGresConnection postgres;
		
	
	public static String getDNICode(String org_unit_id) {
		
		String dni_code = "";
		postgres = new PostGresConnection();
		postgres.getConnection();
		ResultSet result = postgres.getResultSet("SELECT dni_code FROM dni_org_unit WHERE org_unit_id="+org_unit_id+" ");
		try {
			while (result.next()) {
				dni_code = result.getString("dni_code");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dni_code;
	}
	
}
