package com.convirza.tests.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.convirza.core.utils.PostGresConnection;

public class DBPhoneNumberUtils {

	private static PostGresConnection postgres;
	
	public static String[] getNumberByStatus(String status) {
		List<String> numberDetails = null;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		String query = "select phone_number.number_id, phone_number.number from phone_number inner join phone_detail on phone_number.number_id =  phone_detail.number_id left outer join ce_call_flows call_flow on phone_number.number_str = call_flow.dnis where number_status = '" + status + "' and phone_detail.provisioned_route_id is null and call_flow.id is null limit 1";
		ResultSet resultSet = postgres.getResultSet(query);
		try {
			while(resultSet != null && resultSet.next()) {
				numberDetails = new ArrayList<String>();
				numberDetails.add(resultSet.getString("number_id"));
				numberDetails.add(resultSet.getString("number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (numberDetails != null) {
			String[] dest = new String[numberDetails.toArray().length];
			System.arraycopy(numberDetails.toArray(), 0, dest, 0, numberDetails.toArray().length);
			return dest;
		} else 
			return null;
	}
	
	public static String getProvisionRouteIdByNumberId(String numberId) {
		String provisionedRouteId = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select provisioned_route_id from phone_detail where number_id = " + numberId);
		try {
			while(resultSet.next()) {
				provisionedRouteId = resultSet.getString("provisioned_route_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return provisionedRouteId;
	}
	
	public static String getProvisionRouteIdByName(String name) {
		String provisionedRouteId = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select provisioned_route_id from provisioned_route where provisioned_route_name = '"+ name +"' order by provisioned_route_id desc limit 1");
		try {
			while(resultSet.next()) {
				provisionedRouteId = resultSet.getString("provisioned_route_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return provisionedRouteId;
	}
	
	public static String[] getNumberByStatusAndGroup(String status, String groupId) {
		List<String> numberDetails = null;
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("select phone_number.number_id, number from phone_number join phone_detail on phone_number.number_id =  phone_detail.number_id where number_status = '" + status + "' and org_unit_id = '" + groupId + "' and provisioned_route_id is null limit 1");
		try {
			while(resultSet.next()) {
				numberDetails = new ArrayList<String>();
				numberDetails.add(resultSet.getString("number_id"));
				numberDetails.add(resultSet.getString("number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] dest = new String[numberDetails.toArray().length];
		System.arraycopy(numberDetails.toArray(), 0, dest, 0, numberDetails.toArray().length);
		return dest;
	}
	
	public static int setNumberStausByNumber(String number, String status) {
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		int result = postgres.updateQuery("update phone_number set number_status = "+ "'" + status + "'" +" where number = " + "'" + number + "'");
		return result;
	}
	
	public static String getPhoneNumberByVendor(String number_type) {
		String phone_number = "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		
		if(number_type.equals("sp")) {
			ResultSet resultSet = postgres.getResultSet("SELECT * FROM phone_number WHERE CAST(number AS character) NOT IN (SELECT number FROM ce_blacklist) AND number_id IN (SELECT number_id FROM phone_detail WHERE vendor_id='10001') LIMIT 1");
			try {
				while(resultSet.next()) {
					phone_number = resultSet.getString("number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(number_type.equals("ce")) {
			ResultSet resultSet = postgres.getResultSet("SELECT * FROM phone_number WHERE CAST(number AS character) NOT IN (SELECT number FROM ce_blacklist) AND number_id IN (SELECT number_id FROM phone_detail WHERE vendor_id!='10001') LIMIT 1");
			try {
				while(resultSet.next()) {
					phone_number = resultSet.getString("number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(number_type.equals("sip")) {
			ResultSet resultSet = postgres.getResultSet("SELECT * FROM phone_number WHERE CAST(number AS character) NOT IN (SELECT number FROM ce_blacklist) AND number_id IN (SELECT number_id FROM phone_detail WHERE vendor_id='10002') LIMIT 1");
			try {
				while(resultSet.next()) {
					phone_number = resultSet.getString("number");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("number to be blacklisted -"+phone_number);
		return phone_number;
	}
	
	public static int getNPANXX(Long number, String columnName) {
		int npa = 0;
		int nxx = 0;
		int val = 0;
		
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();
		ResultSet resultSet = postgres.getResultSet("SELECT * FROM phone_number WHERE number='"+number+"'");
		
		switch(columnName) {
		case "npa" :
			try {
				while(resultSet.next()) {
					npa = resultSet.getInt("npa");
					val = npa;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "nxx" :
			try {
				while(resultSet.next()) {
					nxx = resultSet.getInt("nxx");
					val = nxx;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		return val;
		
	}
}
