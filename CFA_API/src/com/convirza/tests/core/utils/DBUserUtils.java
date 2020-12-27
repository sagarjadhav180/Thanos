package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.convirza.core.utils.PostGresConnection;
import com.convirza.core.utils.RandomContentGenerator;

public class DBUserUtils {
	private static PostGresConnection postgres;
	
	public static String getUserIdByEmail(String email) {
		int userId = 0;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select ct_user_id from ct_user where username = " + "'" + email + "'");
		try {
			while(resultSet.next()) {
				userId = resultSet.getInt("ct_user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(userId);
	}

	public static void updateExtIdUser(String id, String extId) {
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("update ct_user set user_ext_id = '" + extId + "' where ct_user_id = " + id);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static Map<String, Object> getAgencyAdminInfoByEmail(String email) {
		Map<String, Object> objects = new HashMap<>();
		int userId = 0;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select ct_user_id, user_ext_id, first_name, last_name, ct_user_ou_id," +
			" org_unit_name, address, city, state, zip, phone_number, industry_id" +
			" from ct_user inner join org_unit on ct_user.ct_user_ou_id = org_unit.org_unit_id" +
			" inner join org_unit_detail on org_unit.org_unit_id = org_unit_detail.org_unit_id " +
			"where username = " + "'" + email + "'");
		try {
			while(resultSet.next()) {
				objects.put("id", resultSet.getInt("ct_user_id"));
				objects.put("user_ext_id", resultSet.getString("user_ext_id"));
				objects.put("first_name", resultSet.getString("first_name"));
				objects.put("last_name", resultSet.getString("last_name"));
				objects.put("group_id", resultSet.getInt("ct_user_ou_id"));
				objects.put("group_name", resultSet.getString("org_unit_name"));
				objects.put("address", resultSet.getString("address"));
				objects.put("city", resultSet.getString("city"));
				objects.put("state", resultSet.getString("state"));
				objects.put("zip", resultSet.getString("zip"));
				objects.put("phone_number", resultSet.getString("phone_number"));
				objects.put("industry_id", resultSet.getInt("industry_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objects;
	}
}