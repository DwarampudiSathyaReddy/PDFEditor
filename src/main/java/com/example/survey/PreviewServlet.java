package com.example.survey;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/preview")
@MultipartConfig
public class PreviewServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PreviewServlet.class);
    private PdfToTextService pdfToTextService;

    @Override
    public void init() throws ServletException {
        pdfToTextService = new PdfToTextService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            logger.warn("No file provided for preview");
            req.setAttribute("error", "Please select a file for preview");
            req.getRequestDispatcher("/preview.jsp").forward(req, resp);
            return;
        }

        try {
            String previewImage = pdfToTextService.getPdfPreview(filePart);
            req.setAttribute("previewImage", previewImage);
            req.getRequestDispatcher("/preview.jsp").forward(req, resp);
            logger.info("Preview generated for file: {}", filePart.getSubmittedFileName());
        } catch (Exception e) {
            logger.error("Error generating preview: {}", e.getMessage(), e);
            req.setAttribute("error", "Error generating preview: " + e.getMessage());
            req.getRequestDispatcher("/preview.jsp").forward(req, resp);
        }
    }
}