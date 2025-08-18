package com.piepan.parseme.util;

import com.piepan.parseme.parser.PaddingType;

public final class StringUtils {

    public static String padField(PaddingType paddingType, String str, int length, char paddingChar) {
        switch (paddingType) {
            case LEFT:
                return leftPad(str, length, paddingChar);
            case RIGHT:
                return rightPad(str, length, paddingChar);
            case NONE:
                return str;
            default:
                throw new IllegalArgumentException("Unknown padding type: " + paddingType);
        }
    }

    public static String leftPad(String str, int len, char paddingChar) {
        if (str.length() < len ) {
          int dif = len - str.length();
            str = String.valueOf(paddingChar).repeat(dif) + str;
        }
        return str;
    }

    public static String rightPad(String str, int len, char paddingChar) {
        if (str.length() < len ) {
            int dif = len - str.length();
            str = str + String.valueOf(paddingChar).repeat(dif);
        }
        return str;
    }
}
