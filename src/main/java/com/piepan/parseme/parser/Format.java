package com.piepan.parseme.parser;

import static com.piepan.parseme.util.Constants.EMPTY_STRING;

public enum Format {
    NUMERIC(EMPTY_STRING),
    ALPHANUMERIC(EMPTY_STRING),
    DATE_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    DATE_YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    DATE_YYYY_MM_DD("yyyy-MM-DD"),
    EMPTY(EMPTY_STRING);

    private final String text;

    Format(String text) {
        this.text  = text;
    }

    public String getText() {
        return this.text;
    }
}
