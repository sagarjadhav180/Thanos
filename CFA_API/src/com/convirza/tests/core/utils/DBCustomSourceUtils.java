package com.convirza.tests.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.convirza.core.utils.PostGresConnection;

public class DBCustomSourceUtils {
	private static PostGresConnection postgres;
	
	public static int setCustomSourceStatus(String csName, String group, String status) {
		postgres = new PostGresConnection();
		postgres.getConnection();
		int result = postgres.updateQuery("update custom_source set custom_source_active = "+ "'" + status + "'" +" where custom_source_name = " + "'" + csName + "'" + " and org_unit_id in (select org_unit_id from org_unit where billing_id = '" + group + "')");
		return result;
	}
	
	
	public static int getCustomSourceId(String csName, String group) {
		int customSourceId = 0;
		postgres = new PostGresConnection();
		postgres.getConnection();
		ResultSet result = postgres.getResultSet("select custom_source_id from custom_source where custom_source_name = " + "'" + csName + "'" + " and org_unit_id = " + "'" + group + "'");
		try {
			while (result.next()) {
				customSourceId = result.getInt("custom_source_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customSourceId;
	}
	
	public static int getCustomSourceIdOfOtherBilling(String group) {
		int customSourceId = 0;
		postgres = new PostGresConnection();
		postgres.getConnection();
		ResultSet result = postgres.getResultSet("select custom_source_id from custom_source WHERE org_unit_id IN (select org_unit_id from org_unit where top_ou_id = "+group+") LIMIT 1");
		try {
			while (result.next()) {
				customSourceId = result.getInt("custom_source_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customSourceId;
	}
	
	public static int getDeletedCustomSourceId(String group) {
		int customSourceId = 0;
		postgres = new PostGresConnection();
		postgres.getConnection();
		ResultSet result = postgres.getResultSet("SELECT custom_source_id FROM custom_source WHERE org_unit_id="+group+" AND custom_source_active='f'");
		try {
			while (result.next()) {
				customSourceId = result.getInt("custom_source_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customSourceId;
	}
}
