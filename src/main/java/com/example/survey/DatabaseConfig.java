package com.example.survey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String URL = "jdbc:mysql://localhost:3306/survey?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Sathya@1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found: {}", e.getMessage(), e);
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        logger.debug("Database connection established");
        return conn;
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS conversion_history (\n" +
                         "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                         "    filename VARCHAR(255) NOT NULL,\n" +
                         "    formatting_option VARCHAR(50),\n" +
                         "    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                         ")";
            stmt.execute(sql);
            logger.info("Database initialized, conversion_history table created");
        } catch (SQLException e) {
            logger.error("Error initializing database: {}", e.getMessage(), e);
            throw e;
        }
    }
}