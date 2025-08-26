package com.piepan.parseme.parser.impl;

import java.math.BigDecimal;

import com.piepan.parseme.parser.Parser;

public class DecimalParser extends Parser<BigDecimal> {

    @Override
    public String write(Object input, com.piepan.parseme.parser.Format format) {
        String output = "";
        if (input instanceof BigDecimal) {
            output = input.toString();
        }
        return output;
    }

    @Override
    public BigDecimal read(String input, com.piepan.parseme.parser.Format format) {
        BigDecimal output = null;
        if (input != null && !input.isEmpty()) {
            output = new BigDecimal(input);
        }
        return output;
    }

}
