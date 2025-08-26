package com.piepan.parseme;

import com.piepan.parseme.exceptions.ParseMeException;
import com.piepan.parseme.parser.PaddingType;
import com.piepan.parseme.parser.registry.ParserTypeRegistry;
import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;
import com.piepan.parseme.parser.Parser;
import com.piepan.parseme.util.StringUtils;

import java.lang.reflect.Field;

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
            Field [] fields = orderFieldsByOffset(clazz.getDeclaredFields());
            for (Field field : fields) {
                var value = readField(field, input);
                field.setAccessible(true);
                field.set(instance, value);
            }
        } catch (Exception e) {
            throw new ParseMeException("Failed to create an instance of " + clazz.getName(), e);
        }
        return instance;
    }

    public static Object readField(Field field, String input) {

        com.piepan.parseme.annotation.Field annotation = field.getAnnotation(com.piepan.parseme.annotation.Field.class);
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
            Field [] fields = orderFieldsByOffset(input.getClass().getDeclaredFields());

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

        com.piepan.parseme.annotation.Field annotation = field.getAnnotation(com.piepan.parseme.annotation.Field.class);
        int length = annotation.length();
        FieldType type = annotation.type();
        Format format = annotation.format();
        PaddingType paddingType = annotation.padding();
        char paddingChar = annotation.paddingChar();

        if (FieldType.CUSTOM.equals(type)) {
            return parse(field.get(o));
        } else {
            Parser<?> parser = ParserTypeRegistry.getParser(type);
            field.setAccessible(true);
            String value = parser.write(field.get(o), format);
            return StringUtils.padField(paddingType, value, length, paddingChar);
        }
    }

    private static Field[] orderFieldsByOffset(Field[] fields) {
        Field [] fieldsSorted = java.util.Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(com.piepan.parseme.annotation.Field.class))
                .sorted((f1, f2) -> {
                    int offset1 = f1.getAnnotation(com.piepan.parseme.annotation.Field.class).offset();
                    int offset2 = f2.getAnnotation(com.piepan.parseme.annotation.Field.class).offset();
                    return Integer.compare(offset1, offset2);
                })
                .toArray(Field[]::new);

        if (fieldsSorted.length != fields.length) {
            throw new ParseMeException("No fields annotated with @Field found");
        }
        return fieldsSorted;
    }

}
