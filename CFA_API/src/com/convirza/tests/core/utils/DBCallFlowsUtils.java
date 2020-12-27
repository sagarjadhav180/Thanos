package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.convirza.core.utils.PostGresConnection;

public class DBCallFlowsUtils {

	private static PostGresConnection postgres;
	
	public static String getCallflowByStatusInGroup(String group, String status) throws SQLException {
		int userId = 0;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

			ResultSet resultSet = postgres.getResultSet("select * from ce_call_flows where ouid = '"+group+"' and status = '"+status+"'");

			try {
				while(resultSet.next()) {
					userId = resultSet.getInt("provisioned_route_id");
				}
			} catch (SQLException e) {
				userId = Integer.parseInt("no data found in db");
			}
		
		return String.valueOf(userId);	
		
	}
	
	public static String getOtherBillingCallFlowId(String groupId) {
		String provisioned_route_id = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = postgres.getResultSet("select provisioned_route_id from provisioned_route where provisioned_route_ou_id in (select org_unit_id from org_unit where top_ou_id != "+groupId+") AND is_dni_enabled='t' order by provisioned_route_id desc limit 1;");

		try {
			while(resultSet.next()) {
				provisioned_route_id = resultSet.getString("provisioned_route_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return provisioned_route_id;
	}
	
	public static String getOtherBillingCallFlowIdWithCustomSource(String groupId) {
		String provisioned_route_id = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("SELECT provisioned_route_id FROM callflow_custom_source WHERE provisioned_route_id IN (select provisioned_route_id from provisioned_route where provisioned_route_ou_id in (select org_unit_id from org_unit where top_ou_id != "+groupId+") order by provisioned_route_id desc) LIMIT 1");
		try {
			while(resultSet.next()) {
				provisioned_route_id = resultSet.getString("provisioned_route_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return provisioned_route_id;
	}
	
	public static String getCallId(String groupId) {
		String call_id = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("SELECT call_id FROM call WHERE org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id="+groupId+") LIMIT 1");
		try {
			while(resultSet.next()) {
				call_id = resultSet.getString("call_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return call_id;
	}
	

	public static String getModifiedDateById(String id) {
		String modifiedDate = null;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select provisioned_route_modified from provisioned_route where provisioned_route_id = " + id);
		try {
			while(resultSet.next()) {
				modifiedDate = resultSet.getString("provisioned_route_modified");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modifiedDate;
	}

	public static String getCreatedDateById(String id) {
		String createdDate = null;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select * from provisioned_route where provisioned_route_id = '"+id+"'");
		try {
			while(resultSet.next()) {
				createdDate = resultSet.getString("provisioned_route_created");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return createdDate;
	}
}
