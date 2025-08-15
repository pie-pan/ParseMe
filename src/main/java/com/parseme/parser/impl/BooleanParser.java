package com.parseme.parser.impl;

import com.parseme.parser.Format;
import com.parseme.parser.Parser;

public class BooleanParser extends Parser<Boolean> {

    @Override
    public String write(Object input, Format format) {
        String output = "";
        if (input instanceof Boolean) {
            output = format((Boolean) input, format);
        }
        return output;
    }

    @Override
    public Boolean read(String input, Format format) {
        if (input == null || input.isEmpty()){
            return null;
        }

        return Boolean.valueOf(Boolean.parseBoolean(input));
    }

    private String format(Boolean b, Format format) {
        switch (format) {
            case Format.NUMERIC:
                return b ? "1" : "0";
            case Format.ALPHANUMERIC:
                return Boolean.toString(b);
            default:
                return b ? "Y" : "N";
        }
    }

}
