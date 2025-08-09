package com.parseme.type;

public enum FieldType {

    STRING("S"),
    INTEGER("I"),
    LONG("L"),
    DOUBLE("D"),
    BOOLEAN("B"),
    CUSTOM("C");


    private final String code;

    FieldType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
