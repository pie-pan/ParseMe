package com.parseme.parser.impl;

import com.parseme.parser.Format;
import com.parseme.parser.Parser;

public class StringParser extends Parser<String> {

    @Override
    public String write(Object input, Format format) {
        return input instanceof String ? (String) input : "";
    }

    @Override
    public String read(String input, Format format) {
        return input == null || input.isEmpty() ? null : input;
    }

}
