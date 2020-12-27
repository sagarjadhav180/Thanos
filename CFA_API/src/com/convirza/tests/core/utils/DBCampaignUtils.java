package com.convirza.tests.core.utils;

import com.convirza.core.utils.PostGresConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCampaignUtils {
  private static PostGresConnection postgres;

  public static String getModifiedDateById(String id) {
    String modifiedDate = null;
    postgres = new PostGresConnection();
    Connection con = postgres.getConnection();
    ResultSet resultSet = postgres.getResultSet("select campaign_modified from campaign where campaign_id = " + id);
    try {
      while(resultSet.next()) {
        modifiedDate = resultSet.getString("campaign_modified");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return modifiedDate;
  }
}
