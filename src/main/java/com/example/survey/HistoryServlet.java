package com.example.survey;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HistoryServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ConversionHistory> history = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT id, filename, formatting_option, timestamp FROM conversion_history ORDER BY timestamp DESC";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    history.add(new ConversionHistory(
                        rs.getLong("id"),
                        rs.getString("filename"),
                        rs.getString("formatting_option"),
                        rs.getTimestamp("timestamp")
                    ));
                }
            }
            req.setAttribute("history", history);
            req.getRequestDispatcher("/history.jsp").forward(req, resp);
            logger.info("Conversion history retrieved, size: {}", history.size());
        } catch (Exception e) {
            logger.error("Error retrieving history: {}", e.getMessage(), e);
            req.setAttribute("error", "Error retrieving history: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}