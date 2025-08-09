package com.parseme.type;

public abstract class Parser<T> {

    public abstract String write(T input);

    public abstract T read(String input);

}
