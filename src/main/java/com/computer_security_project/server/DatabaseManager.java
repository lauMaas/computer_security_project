package com.computer_security_project.server;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseManager {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection conn;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("db_config.properties")) {
            if (input == null) {
                throw new IOException(
                        "⚠️ db_config.properties file not found in classpath! Ensure it is inside src/main/resources/");
            }

            Properties props = new Properties();
            props.load(input);
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            System.out.println("✅ Database configuration loaded successfully!");
        } catch (IOException e) {
            System.err.println("⚠️ Error loading database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("🔍 Testing database connection...");
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("✅ Successfully connected to the database!");
            closeConnection();
        } else {
            System.err.println("❌ Failed to connect to the database.");
        }
    }
}