package com.convirza.tests.core.utils;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.convirza.core.utils.PostGresConnection;

public class DBTagUtils {

private static PostGresConnection postgres;
	
	public static List getTagIds(String tag_name,String org_unit_id) {
		List<Integer> tagids=new ArrayList<Integer>();
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = null;
		
		switch(tag_name) {
		case "sj123" : resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_name LIKE '"+tag_name+"%' and tag_active='t' AND org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+org_unit_id+"')");
		break;
		
		case "" : resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_active='t' AND org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+org_unit_id+"')");
		break;
		
		case "callback" : resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_name LIKE '"+tag_name+"' and tag_active='t' AND org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+org_unit_id+"')");
		break;
		}
		
		try {
			while(resultSet.next()) {
//				int tag_id = resultSet.getInt("tag_id");
				String tag_ids = resultSet.getArray("tag_id").toString();
				tagids.add(Integer.parseInt(tag_ids));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tagids;
	}
	
	
	public static int getTagId(String tag_name,String org_unit_id) {
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		int tag_id = 0;
		
		ResultSet resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_name LIKE 'sj123%' and tag_active='t' AND org_unit_id ='"+org_unit_id+"' LIMIT 1");

		try {
			while(resultSet.next()) {
				tag_id=resultSet.getInt("tag_id");
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag_id;
	}
	
	public static int getTagIdFromANotherBilling(String org_unit_id) {
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		int tag_id = 0;
		
		ResultSet resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_active='t' AND org_unit_id NOT IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id='"+org_unit_id+"') ORDER BY tag_created DESC LIMIT 1");

		try {
			while(resultSet.next()) {
				tag_id=resultSet.getInt("tag_id");
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag_id;
	}
	
	
	public static Boolean getTagStatus(int tag_id) {	
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		Boolean tag_status = null;
		
		ResultSet resultSet = postgres.getResultSet("SELECT * FROM tag WHERE tag_id='"+tag_id+"'");

		try {
			while(resultSet.next()) {
				tag_status=resultSet.getBoolean("tag_active");
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag_status;
	}
	
	
	public static List getTagIds(String call_id) {
		List<Integer> tagids=new ArrayList<Integer>();
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = null;
		resultSet = postgres.getResultSet("SELECT * FROM call_tag WHERE call_id IN ('"+call_id+"')");
		
		try {
			while(resultSet.next()) {
//				int tag_id = resultSet.getInt("tag_id");
				String tag_ids = resultSet.getArray("tag_id").toString();
				tagids.add(Integer.parseInt(tag_ids));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tagids;
	}
	
	public static List getCallIds(String group_id, String level) {
		List<Integer> call_ids=new ArrayList<Integer>();
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = null;
		switch(level) {	
		case "agency" : resultSet = postgres.getResultSet("SELECT DISTINCT(call_id) FROM call_tag WHERE call_id IN (SELECT call_id FROM call WHERE org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE billing_id='"+group_id+"')) ORDER BY call_id ASC");
		break;
		case "company" : resultSet = postgres.getResultSet("SELECT DISTINCT(call_id) FROM call_tag WHERE call_id IN (SELECT call_id FROM call WHERE org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE org_unit_parent_id='"+group_id+"' OR org_unit_id='"+group_id+"'))");
		break;
		case "location" : resultSet = postgres.getResultSet("SELECT DISTINCT(call_id) FROM call_tag WHERE call_id IN (SELECT call_id FROM call WHERE org_unit_id='"+group_id+"') ORDER BY call_id ASC");
		break;
		}
		
		
		
		try {
			while(resultSet.next()) {
//				int tag_id = resultSet.getInt("tag_id");
				String call_id = resultSet.getArray("call_id").toString();
				call_ids.add(Integer.parseInt(call_id));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return call_ids;
	}
}
