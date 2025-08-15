package com.piepan.parseme.util;

public final class StringUtils {

    public static String leftPad(String str, int len) {
        if (str.length() < len ) {
          int dif = len - str.length();
          StringBuffer sb = new StringBuffer("");
          for (int i = 0; i < dif; i++) {
              sb.append(" ");
          }
          str = sb + str;
        }
        return str;
    }
}
