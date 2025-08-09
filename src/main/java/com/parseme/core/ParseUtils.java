package com.parseme.core;

import com.parseme.exceptions.ParseMeError;
import com.parseme.registry.ParserTypeRegistry;
import com.parseme.type.FieldType;
import com.parseme.type.Parser;

import java.lang.reflect.Field;

public class ParseUtils {

    public static <T> T parse(String input, Class<T> clazz) {
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            Field [] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                var value = readField(field, clazz, input);
                field.setAccessible(true);
                field.set(instance, value);
            }
        } catch (Exception e) {
            throw new ParseMeError("Failed to create an instance of " + clazz.getName(), e);
        }
        return instance;
    }

    public static Object readField(Field field, Class<?> clazz, String input) {
        if (!field.isAnnotationPresent(com.parseme.annotation.Field.class)) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not annotated with @Field");
        }

        com.parseme.annotation.Field annotation = field.getAnnotation(com.parseme.annotation.Field.class);
        assert annotation != null;
        int offset = annotation.offset();
        int length = annotation.length();
        FieldType type = annotation.type();
        String fieldValue = input.substring(offset, offset + length);

        field.setAccessible(true);

        if (FieldType.CUSTOM.equals(type)) {
            return parse(input, clazz);
        } else {
            Parser<?> parser = ParserTypeRegistry.getParser(type);
            return parser.read(fieldValue);
        }
    }
}
