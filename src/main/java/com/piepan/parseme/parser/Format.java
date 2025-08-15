package com.piepan.parseme.parser;

import static com.piepan.parseme.util.Constants.EMPTY_STRING;

public enum Format {
    NUMERIC(EMPTY_STRING),
    ALPHANUMERIC(EMPTY_STRING),
    DATE_YYYY_MM_DD_HH_MM_SS("YYYY-MM-DD HH-mm-ss"),
    DATE_YYYY_MM_DD_HH_MM("YYYY-MM-DD HH-mm"),
    DATE_YYYY_MM_DD("YYYY-MM-DD"),
    EMPTY(EMPTY_STRING);

    private String text;

    Format(String text) {
        this.text  = text;
    }

    public String getText() {
        return this.text;
    }
}
