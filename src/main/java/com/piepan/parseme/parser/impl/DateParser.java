package com.piepan.parseme.parser.impl;

import com.piepan.parseme.parser.Format;
import com.piepan.parseme.parser.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser extends Parser<LocalDate> {

    @Override
    public String write(Object input, Format format) {
        String output = "";
        if (input instanceof LocalDate) {
            output = format((LocalDate) input, format);
        }
        return output;
    }

    @Override
    public LocalDate read(String input, Format format) {

        if (input == null || input.isEmpty()) {
            return null;
        }

        return LocalDate.parse(input);
    }

    private String format(LocalDate input, Format format) {
        String pattern = "yyyy-MM-dd";

        if (Format.EMPTY != format) {
            pattern = format.getText();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return input.format(formatter);
    }

}
