package com.example.survey;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/saveEdit")
public class SaveEditServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SaveEditServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("filename");
        String content = req.getParameter("content");
        String downloadFormat = req.getParameter("downloadFormat") != null ? req.getParameter("downloadFormat") : "txt";

        if (filename == null || content == null) {
            logger.warn("Filename or content missing for save edit");
            req.setAttribute("error", "Filename or content missing");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        String downloadDir = getServletContext().getRealPath("/downloads");
        Path downloadPath = Paths.get(downloadDir);
        Files.createDirectories(downloadPath);

        String sanitizedFilename = sanitizeFilename(filename, downloadFormat);
        Path filePath = downloadPath.resolve(sanitizedFilename);
        if (!filePath.startsWith(downloadPath)) {
            logger.warn("Invalid file path: {}", filePath);
            req.setAttribute("error", "Invalid file path");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        try {
            if ("word".equalsIgnoreCase(downloadFormat)) {
                try (XWPFDocument document = new XWPFDocument()) {
                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.setText(content);
                    Files.createDirectories(filePath.getParent());
                    try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                        document.write(out);
                    }
                }
            } else {
                Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
            }
            logger.info("File edited and saved: {}", sanitizedFilename);
            req.setAttribute("message", "File saved successfully");
            req.setAttribute("filename", sanitizedFilename);
            req.getRequestDispatcher("/result.jsp").forward(req, resp);
        } catch (IOException e) {
            logger.error("Error saving edited file: {}", e.getMessage(), e);
            req.setAttribute("error", "Error saving file: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    private String sanitizeFilename(String filename, String format) {
        if (filename == null || filename.isEmpty()) {
            logger.error("Filename is null, defaulting to default_file");
            return "default_file." + ("word".equalsIgnoreCase(format) ? "docx" : "txt");
        }
        String baseName = filename.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("\\.[^.]+$", "");
        return baseName + "." + ("word".equalsIgnoreCase(format) ? "docx" : "txt");
    }
}