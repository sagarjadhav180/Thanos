package com.convirza.tests.core.utils;

import com.convirza.core.utils.PostGresConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBIndustryUtils {
  private static PostGresConnection postgres;

  public static String getIndustryNameUsingID(String industryId) {
    String industryName = "";
    postgres = new PostGresConnection();
    Connection con = postgres.getConnection();
    ResultSet resultSet = postgres.getResultSet("select industry_name from industry where industry_id = '"+ industryId +"'");
    try {
      while(resultSet.next()) {
        industryName = resultSet.getString("industry_name");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return industryName;
  }
}
