package com.example.survey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EditServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("filename");
        if (filename == null || filename.isEmpty()) {
            logger.warn("No filename provided for edit");
            req.setAttribute("error", "No file specified");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        Path downloadDir = Paths.get(getServletContext().getRealPath("/downloads"));
        Path filePath = downloadDir.resolve(filename);
        if (!filePath.startsWith(downloadDir)) {
            logger.warn("Invalid file path: {}", filePath);
            req.setAttribute("error", "Invalid file path");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        if (!Files.exists(filePath)) {
            logger.warn("File not found for edit: {}", filePath);
            req.setAttribute("error", "File not found: " + filename);
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            String content = new String(fileBytes, StandardCharsets.UTF_8);
            req.setAttribute("filename", filename);
            req.setAttribute("content", content);
            req.getRequestDispatcher("/edit.jsp").forward(req, resp);
            logger.info("File loaded for editing: {}", filename);
        } catch (IOException e) {
            logger.error("Error reading file for edit: {}", e.getMessage(), e);
            req.setAttribute("error", "Error reading file: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}