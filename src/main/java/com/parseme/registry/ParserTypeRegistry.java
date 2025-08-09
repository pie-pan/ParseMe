package com.parseme.registry;

import com.parseme.type.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ParserTypeRegistry {

    private static final Map<FieldType, Parser<?>> REGISTRY = new ConcurrentHashMap<>();

    static {
        REGISTRY.put(FieldType.BOOLEAN, new BooleanParser());
        REGISTRY.put(FieldType.STRING, new StringParser());
    }

    public static Parser<?> getParser(FieldType fieldType) {

        Parser<?> parser = REGISTRY.get(fieldType);

        if (parser == null) {
            throw new IllegalArgumentException("No parser registered for field type: " + fieldType);
        }

        return parser;
    }
}
