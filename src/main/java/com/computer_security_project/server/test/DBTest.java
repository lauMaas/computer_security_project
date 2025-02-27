package com.computer_security_project.server.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/SecureChat";
        String user = "root";  
        String password = "password123";  // ✅ Provide the correct password here

        try {
            // ✅ Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Establish database connection
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("✅ Successfully connected to SecureChat database!");
                conn.close();
            } else {
                System.out.println("❌ Connection failed!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("⚠️ MySQL JDBC Driver not found! Add mysql-connector-java to classpath.");
        } catch (SQLException e) {
            System.err.println("⚠️ Connection failed: " + e.getMessage());
        }
    }
}