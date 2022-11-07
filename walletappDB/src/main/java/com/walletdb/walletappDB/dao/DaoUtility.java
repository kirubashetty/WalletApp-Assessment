package com.walletdb.walletappDB.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoUtility {

	public static Connection getConnectionToMySQL() {
		Connection connection = null;
		try {
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/fordtraining?" + "user=root&password=fordtraining");
			System.out.println("Connection to MYSQL successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

}
