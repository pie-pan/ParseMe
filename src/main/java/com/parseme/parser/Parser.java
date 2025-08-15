package com.parseme.parser;

public abstract class Parser<T> {

    public abstract String write(Object input, Format format);

    public abstract T read(String input, Format format);

}
