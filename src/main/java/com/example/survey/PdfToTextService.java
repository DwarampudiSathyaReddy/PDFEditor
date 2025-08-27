package com.example.survey;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;

public class PdfToTextService {
    private static final Logger logger = LoggerFactory.getLogger(PdfToTextService.class);

    public String convertPdfToText(File file, String formattingOption) throws IOException {
        logger.debug("Converting PDF to text with formattingOption={}", formattingOption);
        logger.info("Processing file: {}", file.getAbsolutePath());
        if (!file.exists() || !file.canRead()) {
            logger.error("PDF file does not exist or is not readable: {}", file.getAbsolutePath());
            throw new IOException("PDF file is not accessible: " + file.getAbsolutePath());
        }
        if (file.length() == 0) {
            logger.error("PDF file is empty: {}", file.getAbsolutePath());
            throw new IOException("PDF file is empty: " + file.getAbsolutePath());
        }
        try (PDDocument document = Loader.loadPDF(file)) {
            if (document.isEncrypted()) {
                logger.error("PDF file is encrypted: {}", file.getName());
                throw new IOException("Cannot process encrypted PDF: " + file.getName());
            }
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            if (text == null || text.trim().isEmpty()) {
                logger.warn("No text extracted from PDF file: {}", file.getName());
                return null;
            }
            String formattedText = applyFormatting(text, formattingOption);
            logger.debug("Text extracted successfully, length={}", formattedText.length());
            return formattedText;
        } catch (IOException e) {
            logger.error("Error extracting text from PDF file {}: {}", file.getAbsolutePath(), e.getMessage(), e);
            throw new IOException("Failed to convert PDF to text: " + file.getAbsolutePath(), e);
        }
    }

    public String getPdfPreview(Part filePart) throws IOException {
        logger.debug("Generating PDF preview");
        String tempDir = System.getProperty("java.io.tmpdir");
        Path tempFile = Paths.get(tempDir, "preview_" + System.currentTimeMillis() + ".pdf");
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Saved temporary file for preview: {}", tempFile);
        } catch (IOException e) {
            logger.error("Error saving temporary file for preview: {}", e.getMessage(), e);
            throw new IOException("Failed to save temporary file for preview: " + e.getMessage(), e);
        }

        try (PDDocument document = Loader.loadPDF(tempFile.toFile())) {
            if (document.isEncrypted()) {
                logger.error("PDF file is encrypted for preview: {}", tempFile.getFileName());
                throw new IOException("Cannot preview encrypted PDF");
            }
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 100);
            ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", imageBaos);
            String previewImage = Base64.getEncoder().encodeToString(imageBaos.toByteArray());
            logger.debug("Preview generated successfully, image size={}", previewImage.length());
            return previewImage;
        } catch (IOException e) {
            logger.error("Error generating preview for file {}: {}", tempFile.getFileName(), e.getMessage(), e);
            throw new IOException("Failed to generate preview: " + e.getMessage(), e);
        } finally {
            try {
                Files.deleteIfExists(tempFile);
                logger.debug("Deleted temporary preview file: {}", tempFile);
            } catch (IOException e) {
                logger.warn("Failed to delete temporary preview file {}: {}", tempFile, e.getMessage());
            }
        }
    }

    private String applyFormatting(String text, String formattingOption) {
        if (text == null) {
            logger.warn("Text is null, returning empty string");
            return "";
        }
        switch (formattingOption != null ? formattingOption : "none") {
            case "remove_line_breaks":
                return text.replaceAll("\\r?\\n+", " ");
            case "uppercase":
                return text.toUpperCase();
            case "lowercase":
                return text.toLowerCase();
            default:
                return text;
        }
    }
}