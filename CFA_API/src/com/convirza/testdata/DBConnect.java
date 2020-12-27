package com.convirza.testdata;

import java.sql.Connection;
import java.sql.ResultSet;

public interface DBConnect {
	public Connection getConnection();
	public ResultSet getResultSet(String query);
}
