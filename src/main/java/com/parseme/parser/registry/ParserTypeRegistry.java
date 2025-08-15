package com.parseme.parser.registry;

import com.parseme.parser.*;
import com.parseme.parser.impl.BooleanParser;
import com.parseme.parser.impl.DateParser;
import com.parseme.parser.impl.DateTimeParser;
import com.parseme.parser.impl.StringParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ParserTypeRegistry {

    private static final Map<FieldType, Parser<?>> REGISTRY = new ConcurrentHashMap<>();

    static {
        REGISTRY.put(FieldType.BOOLEAN, new BooleanParser());
        REGISTRY.put(FieldType.STRING, new StringParser());
        REGISTRY.put(FieldType.DATE, new DateParser());
        REGISTRY.put(FieldType.DATETIME, new DateTimeParser());
    }

    public static Parser<?> getParser(FieldType fieldType) {

        Parser<?> parser = REGISTRY.get(fieldType);

        if (parser == null) {
            throw new IllegalArgumentException("No parser registered for field type: " + fieldType);
        }

        return parser;
    }

}
