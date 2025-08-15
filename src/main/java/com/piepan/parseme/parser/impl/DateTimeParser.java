package com.piepan.parseme.parser.impl;

import com.piepan.parseme.parser.Format;
import com.piepan.parseme.parser.Parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser extends Parser<LocalDateTime> {

    @Override
    public String write(Object input, Format format) {
        String output = "";
        if (input instanceof LocalDateTime) {
            output = format((LocalDateTime) input, format);
        }

        return output;
    }

    @Override
    public LocalDateTime read(String input, Format format) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return LocalDateTime.parse(input);
    }

    private String format(LocalDateTime input, Format format) {
        String pattern = "yyyy-MM-dd HH:mm:sss";

        if (Format.EMPTY != format) {
            pattern = format.getText();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(input);
    }

}
