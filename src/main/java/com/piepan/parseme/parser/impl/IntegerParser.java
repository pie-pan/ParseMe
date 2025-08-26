package com.piepan.parseme.parser.impl;

import com.piepan.parseme.parser.Parser;

import java.math.BigInteger;

import com.piepan.parseme.parser.Format;

public class IntegerParser  extends Parser<BigInteger> {

    @Override
    public String write(Object input, Format format) {
        String output = "";
        if (input instanceof BigInteger) {
            output = input.toString();
        }
        return output;
    }

    @Override
    public BigInteger read(String input, Format format) {
        BigInteger output = null;
        if (input != null && !input.isEmpty()) {
            output = new BigInteger(input);
        }
        return output;
    }    

}
