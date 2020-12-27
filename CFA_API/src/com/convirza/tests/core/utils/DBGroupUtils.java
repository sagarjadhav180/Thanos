package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.convirza.core.utils.PostGresConnection;

public class DBGroupUtils {
	
	private static PostGresConnection postgres;
	
	public static String getOtherBillingGroupId(String groupId) {
		String numberDetails = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select org_unit_id from org_unit where org_unit_id = top_ou_id and org_unit_id != " + groupId + " limit 1;");
		try {
			while(resultSet.next()) {
				numberDetails = resultSet.getString("org_unit_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numberDetails;
	}
	
	public static String getGroupIdByStatus(String groupId, String status) {
		String numberDetails = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select org_unit_id from org_unit where org_unit_parent_id = '"+ groupId +"' and org_unit_status = '" + status + "' limit 1;");
		try {
			while(resultSet.next()) {
				numberDetails = resultSet.getString("org_unit_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numberDetails;
	}

	
	public static String getOrgUnitIdWithoutCustomSource(String groupId) {
		String org_unit_id = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("SELECT org_unit_id FROM org_unit WHERE org_unit_id NOT IN (SELECT org_unit_id FROM custom_source WHERE org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+groupId+"')) AND org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+groupId+"') LIMIT 1");
		try {
			while(resultSet.next()) {
				org_unit_id = resultSet.getString("org_unit_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return org_unit_id;
	}
	
	public static String getGroupFieldById(String groupId, String field) {
		String fieldValue = "";
		postgres = new PostGresConnection();
		postgres.getConnection();
		String query = "select " + field + " from org_unit_detail where org_unit_id = '"+ groupId + "'";
		ResultSet resultSet = postgres.getResultSet(query);
		try {
			while(resultSet.next()) {
				fieldValue = resultSet.getString(field);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fieldValue;
	}

}
