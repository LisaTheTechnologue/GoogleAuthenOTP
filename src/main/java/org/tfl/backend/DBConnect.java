package org.tfl.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.tfl.constants.UserConstants;
import org.tfl.model.Users;

public class DBConnect {
	public Connection getConnection() throws SQLException {
		String URL = "jdbc:mysql://localhost:3306/cry301asm3";
		String username = "root";
		String password = "1234";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(URL, username, password);
	}

	
}
