package com.parseme.type;

public class StringParser extends Parser<String> {

    @Override
    public String write(String input) {
        return input == null ? "" : input;
    }

    @Override
    public String read(String input) {
        return input == null || input.isEmpty() ? null : input;
    }

}
