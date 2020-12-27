package com.convirza.tests.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.convirza.core.utils.PostGresConnection;

public class DBChannelUtils {
	  private static PostGresConnection postgres;

	  
	public static Map getAllValidChannelID() {

	    Map<String, String> result = new HashMap<String,String>(); 
	    String modifiedDate = null;
	    postgres = new PostGresConnection();
	    postgres.getConnection();
	    ResultSet resultSet = postgres.getResultSet("SELECT channel_id FROM channel");
	    try {
	      while(resultSet.next()) {
	        String channel_id = resultSet.getString("channel_id");
	        result.put(channel_id, channel_id);

	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

}
