package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.convirza.core.utils.PostGresConnection;

public class DBBlacklistedNumbers {
	
	private static PostGresConnection postgres;
	
	public static Boolean getBlacklistedNumberDBEntry(String number) {
		String count = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		

			ResultSet resultSet = postgres.getResultSet("SELECT count(*) as COUNT FROM ce_blacklist WHERE number='"+number+"'");
			try {
				while(resultSet.next()) {
					count = resultSet.getString("count");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(Integer.parseInt(count)>0) {
				return true;
			}else {
				return false;
			}
			
	}
	
	
	public static List getBlacklistedNumbers(String org_unit_id) {
		List<Long> blacklistedNumbers=new ArrayList<Long>();
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = null;
		resultSet = postgres.getResultSet("SELECT * FROM ce_blacklist WHERE org_unit_id='"+org_unit_id+"'");
		
		try {
			while(resultSet.next()) {
//				int tag_id = resultSet.getInt("tag_id");
				String tag_ids = resultSet.getArray("number").toString();
				blacklistedNumbers.add(Long.parseLong(tag_ids));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return blacklistedNumbers;
	}

}
