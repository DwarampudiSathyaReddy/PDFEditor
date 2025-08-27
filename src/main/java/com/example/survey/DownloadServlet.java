package com.example.survey;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("filename");
        if (filename == null || filename.isEmpty()) {
            logger.warn("No filename provided for download");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Filename is required");
            return;
        }

        Path filePath = Paths.get(getServletContext().getRealPath("/downloads"), filename);
        if (!filePath.startsWith(getServletContext().getRealPath("/downloads"))) {
            logger.warn("Invalid file path: {}", filePath);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file path");
            return;
        }

        if (!Files.exists(filePath)) {
            logger.error("File not found for download: {}", filePath);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        logger.debug("Serving file for download: {}", filePath);
        resp.setContentType(getServletContext().getMimeType(filename));
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        Files.copy(filePath, resp.getOutputStream());
        logger.info("File downloaded successfully: {}", filename);
    }
}