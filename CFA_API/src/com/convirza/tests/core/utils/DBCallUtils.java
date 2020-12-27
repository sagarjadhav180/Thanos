package com.convirza.tests.core.utils;

import com.convirza.core.utils.PostGresConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBCallUtils {
  private static PostGresConnection postgres;

  public static Map getModifiedDateById(String groupLevel) {
    Map<String, String> result = new HashMap<>();
    String modifiedDate = null;
    postgres = new PostGresConnection();
    postgres.getConnection();
    ResultSet resultSet = postgres.getResultSet("select call_id, cf.provisioned_route_id, ct_user.username as username from call join" +
      " ce_call_flows cf on call.provisioned_route_id = cf.provisioned_route_id " +
      "join org_unit on org_unit.org_unit_id = call.org_unit_id join ct_user on org_unit.org_unit_id = ct_user.ct_user_ou_id " +
      "where Lower(org_unit_name) LIKE '%"+ groupLevel +"%' and org_unit_status <> 'deleted' and ct_user.user_status = 'active' order by call_id desc limit 1");
    try {
      while(resultSet.next()) {
        String callId = resultSet.getString("call_id");
        result.put("call_id", callId);
        String userName = resultSet.getString("username");
        result.put("username", userName);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public static String getCallId(String group_id) {
	
	  String call_id= "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = postgres.getResultSet("SELECT * FROM call WHERE org_unit_id ='"+group_id+"' ORDER BY call_started DESC LIMIT 1");

		try {
			while(resultSet.next()) {
				call_id = resultSet.getString("call_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return call_id;
  }
  
  public static String getCallIdFromAnotherBilling(String group_id) {
		
	  String call_id= "";
		postgres = new PostGresConnection();
		Connection con = postgres.getConnection();

		ResultSet resultSet = postgres.getResultSet("SELECT * FROM call WHERE org_unit_id IN (SELECT org_unit_id FROM org_unit WHERE top_ou_id!='"+group_id+"') ORDER BY call_started DESC LIMIT 1");

		try {
			while(resultSet.next()) {
				call_id = resultSet.getString("call_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return call_id;
  }
}
