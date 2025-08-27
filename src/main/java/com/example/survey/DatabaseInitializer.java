package com.example.survey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DatabaseConfig.initializeDatabase();
            logger.info("Database initialization completed successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database: {}", e.getMessage(), e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No cleanup needed
    }
}