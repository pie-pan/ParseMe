package com.piepan.parseme;

import com.piepan.parseme.exceptions.ParseMeException;
import com.piepan.parseme.parser.PaddingType;
import com.piepan.parseme.parser.registry.ParserTypeRegistry;
import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;
import com.piepan.parseme.parser.Parser;
import com.piepan.parseme.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ParseMe {

    /**
     * Return a java object from a positional string
     * @param input
     * @param clazz
     * @return
     * @param <T>
     */
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
            throw new ParseMeException("Failed to create an instance of " + clazz.getName(), e);
        }
        return instance;
    }

    public static Object readField(Field field, Class<?> clazz, String input) {
        validateAnnotation(field);

        com.piepan.parseme.annotation.Field annotation = field.getAnnotation(com.piepan.parseme.annotation.Field.class);
        assert annotation != null;
        int offset = annotation.offset();
        int length = annotation.length();
        FieldType type = annotation.type();
        Format format = annotation.format();

        if ( offset + length > input.length()) {
            throw new ParseMeException("Input string is too short for field " + field.getName() + ". Expected length: " + (offset + length) + ", but got: " + input.length());
        }

        String fieldValue = input.substring(offset, offset + length);

        field.setAccessible(true);

        if (FieldType.CUSTOM.equals(type)) {
            return parse(fieldValue, field.getType());
        } else {
            Parser<?> parser = ParserTypeRegistry.getParser(type);
            return parser.read(fieldValue, format);
        }
    }

    /**
     * return positional string from java Object with com.parseme.annotation.Field annotation
     * @param input
     * @return
     */
    public static String parse(Object input) {
        StringBuilder output = new StringBuilder();
        try {
            Field [] fields = input.getClass().getDeclaredFields();

            for (Field field : fields) {
                String value = writeField(field, input);
                output.append(value);
            }

        } catch (Exception e) {
            throw new ParseMeException("Failed to create an instance of " + input.getClass().getName(), e);
        }
        return output.toString();
    }

    public static String writeField(Field field, Object o) throws IllegalAccessException {
        validateAnnotation(field);

        com.piepan.parseme.annotation.Field annotation = field.getAnnotation(com.piepan.parseme.annotation.Field.class);
        assert annotation != null;
        int length = annotation.length();
        FieldType type = annotation.type();
        Format format = annotation.format();
        PaddingType paddingType = annotation.padding();
        char paddingChar = annotation.paddingChar();

        if (FieldType.CUSTOM.equals(type)) {
            return parse(o);
        } else {
            Parser<?> parser = ParserTypeRegistry.getParser(type);
            field.setAccessible(true);
            String value = parser.write(field.get(o), format);
            return StringUtils.padField(paddingType, value, length, paddingChar);
        }
    }

    private static void validateAnnotation(Field field) {
        if (!field.isAnnotationPresent(com.piepan.parseme.annotation.Field.class)) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not annotated with @Field");
        }
    }

}
