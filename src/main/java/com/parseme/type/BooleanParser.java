package com.parseme.type;

public class BooleanParser extends Parser<Boolean> {

    @Override
    public String write(Boolean input) {
        if (input == null) return "";
        return input.toString();
    }

    @Override
    public Boolean read(String input) {
        if (input == null || input.isEmpty()) return null;
        return Boolean.parseBoolean(input);
    }
}
