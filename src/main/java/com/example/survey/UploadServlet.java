package com.example.survey;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);
    private PdfToTextService pdfToTextService;

    @Override
    public void init() throws ServletException {
        pdfToTextService = new PdfToTextService();
        logger.info("UploadServlet initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Received GET request for /upload, redirecting to upload.jsp");
        req.getRequestDispatcher("/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Processing file upload");
        Part filePart = req.getPart("file");
        String formattingOption = req.getParameter("formattingOption") != null ? req.getParameter("formattingOption") : "none";
        String downloadFormat = req.getParameter("downloadFormat") != null ? req.getParameter("downloadFormat") : "txt";

        if (filePart == null || filePart.getSize() == 0) {
            logger.warn("No file uploaded");
            req.setAttribute("error", "Please select a file to upload");
            req.getRequestDispatcher("/upload.jsp").forward(req, resp);
            return;
        }

        try {
            String uploadDir = getServletContext().getRealPath("/uploads");
            String downloadDir = getServletContext().getRealPath("/downloads");
            Path uploadPath = Paths.get(uploadDir);
            Path downloadPath = Paths.get(downloadDir);
            logger.debug("Creating directories: uploadPath={}, downloadPath={}", uploadPath, downloadPath);
            Files.createDirectories(uploadPath);
            Files.createDirectories(downloadPath);

            String originalFilename = filePart.getSubmittedFileName();
            String sanitizedFilename = sanitizeFilename(originalFilename, "pdf");
            Path uploadedFile = uploadPath.resolve(sanitizedFilename);
            if (!uploadedFile.startsWith(uploadPath)) {
                logger.warn("Invalid file path: {}", uploadedFile);
                req.setAttribute("error", "Invalid file path");
                req.getRequestDispatcher("/upload.jsp").forward(req, resp);
                return;
            }

            logger.debug("Saving uploaded file to: {}", uploadedFile);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, uploadedFile, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Uploaded file saved successfully: {}", uploadedFile);
            } catch (IOException e) {
                logger.error("Error saving uploaded file to {}: {}", uploadedFile, e.getMessage(), e);
                throw new IOException("Failed to save uploaded file: " + e.getMessage(), e);
            }

            if (!Files.exists(uploadedFile) || !Files.isReadable(uploadedFile)) {
                logger.error("Saved file is not accessible: {}", uploadedFile);
                throw new IOException("Saved file is not accessible: " + uploadedFile);
            }
            logger.debug("Confirmed saved file exists and is readable: {}", uploadedFile);

            logger.debug("Converting PDF to text with formattingOption={}", formattingOption);
            String text;
            try {
                text = pdfToTextService.convertPdfToText(uploadedFile.toFile(), formattingOption);
                if (text == null || text.trim().isEmpty()) {
                    logger.error("Extracted text is null or empty for file: {}", originalFilename);
                    req.setAttribute("error", "Failed to extract text from PDF");
                    req.getRequestDispatcher("/upload.jsp").forward(req, resp);
                    return;
                }
                logger.info("PDF text extracted successfully, length={}", text.length());
            } catch (IOException e) {
                logger.error("Error converting PDF to text for file {}: {}", uploadedFile, e.getMessage(), e);
                throw new IOException("Failed to convert PDF to text: " + uploadedFile, e);
            } finally {
                try {
                    Files.deleteIfExists(uploadedFile);
                    logger.debug("Deleted temporary uploaded file: {}", uploadedFile);
                } catch (IOException e) {
                    logger.warn("Failed to delete temporary file {}: {}", uploadedFile, e.getMessage());
                }
            }

            String outputFilename = sanitizeFilename(originalFilename, downloadFormat);
            Path outputFile = downloadPath.resolve(outputFilename);
            if (!outputFile.startsWith(downloadPath)) {
                logger.warn("Invalid output file path: {}", outputFile);
                req.setAttribute("error", "Invalid output file path");
                req.getRequestDispatcher("/upload.jsp").forward(req, resp);
                return;
            }

            logger.debug("Saving converted file to: {}", outputFile);
            try {
                if ("word".equalsIgnoreCase(downloadFormat)) {
                    try (XWPFDocument document = new XWPFDocument()) {
                        XWPFParagraph paragraph = document.createParagraph();
                        XWPFRun run = paragraph.createRun();
                        run.setText(text);
                        Files.createDirectories(outputFile.getParent());
                        try (FileOutputStream out = new FileOutputStream(outputFile.toFile())) {
                            document.write(out);
                        }
                    }
                    logger.info("Word document saved: {}", outputFile);
                } else {
                    Files.write(outputFile, text.getBytes());
                    logger.info("Text file saved: {}", outputFile);
                }
            } catch (IOException e) {
                logger.error("Error saving converted file to {}: {}", outputFile, e.getMessage(), e);
                throw new IOException("Failed to save converted file: " + e.getMessage(), e);
            }

            try (Connection conn = DatabaseConfig.getConnection()) {
                logger.debug("Inserting into conversion_history: filename={}, formattingOption={}", outputFilename, formattingOption);
                String sql = "INSERT INTO conversion_history (filename, formatting_option, timestamp) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, outputFilename);
                    stmt.setString(2, formattingOption);
                    stmt.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                    stmt.executeUpdate();
                    logger.info("Conversion history logged for file: {}", outputFilename);
                }
            } catch (SQLException e) {
                logger.warn("Failed to log conversion history: {}", e.getMessage(), e);
            }

            req.setAttribute("filename", outputFilename);
            req.setAttribute("text", text);
            req.getRequestDispatcher("/result.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.error("Error processing file {}: {}", filePart.getSubmittedFileName(), e.getMessage(), e);
            req.setAttribute("error", "Error processing file: " + e.getMessage());
            req.getRequestDispatcher("/upload.jsp").forward(req, resp);
        }
    }

    private String sanitizeFilename(String filename, String format) {
        if (filename == null || filename.isEmpty()) {
            logger.error("Filename is null, defaulting to default_file");
            return "default_file." + ("word".equalsIgnoreCase(format) ? "docx" : format);
        }
        String baseName = filename.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("\\.[^.]+$", "");
        return baseName + "." + ("word".equalsIgnoreCase(format) ? "docx" : format);
    }
}