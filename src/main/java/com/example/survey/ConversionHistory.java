package com.example.survey;

import java.sql.Timestamp;

public class ConversionHistory {
    private long id;
    private String filename;
    private String formattingOption;
    private Timestamp timestamp;

    public ConversionHistory(long id, String filename, String formattingOption, Timestamp timestamp) {
        this.id = id;
        this.filename = filename;
        this.formattingOption = formattingOption;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getFormattingOption() {
        return formattingOption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}