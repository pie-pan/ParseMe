package com.piepan.parseme;

import com.piepan.parseme.annotation.Field;
import com.piepan.parseme.exceptions.ParseMeException;
import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ParseMeTest {

    @Test
    void stringToObject_shouldThrow_whenFieldHasNoAnnotation() throws Exception {
        assertThrows(ParseMeException.class, () -> {
            ParseMe.parse("text", NoAnnotation.class);
        });
    }

    @Test
    void stringToObject_shouldParse_whenFieldHasAnnotation() throws Exception {
        String input = "John2023-10-01N";

        WithAnnotation result = ParseMe.parse(input, WithAnnotation.class);

        assertEquals("John", result.name);
        assertEquals(LocalDate.of(2023, 10, 1), result.birthDate);
        assertFalse(result.isActive);
    }

    @Test
    void stringToObject_shouldParseCustomField_whenFieldHasCustomAnnotation() throws Exception {
        String input = "ProductA  000012023-10-01";

        WithAnnotationCustom result = ParseMe.parse(input, WithAnnotationCustom.class);

        assertEquals("ProductA  ", result.productName);
        assertEquals("00001", result.nestedField.code);
        assertEquals("2023-10-01", result.nestedField.startDate.toString());
    }

    @Test
    void objectToString_shouldThrow_whenFieldHasNoAnnotation() throws Exception {
        NoAnnotation noAnnotation = new NoAnnotation();
        noAnnotation.field = "text";

        assertThrows(ParseMeException.class, () -> ParseMe.parse(noAnnotation));
    }

    @Test
    void objectToString_shouldParse_whenFieldHasAnnotation() throws Exception {
        WithAnnotation withAnnotation = new WithAnnotation();
        withAnnotation.name = "John";
        withAnnotation.birthDate = LocalDate.of(2023, 10, 1);
        withAnnotation.isActive = false;

        String result = ParseMe.parse(withAnnotation);

        assertEquals("John2023-10-010", result);
    }

    static class NoAnnotation{
        String field;
    }

    static class WithAnnotation {
        @Field(length = 4, offset = 0)
        String name;
        @Field(length = 10, offset = 4, type = FieldType.DATE, format = Format.DATE_YYYY_MM_DD)
        LocalDate birthDate;
        @Field(length = 1, offset = 14, type = FieldType.BOOLEAN, format = Format.NUMERIC)
        Boolean isActive;
    }

    static class WithAnnotationCustom {
        @Field(length = 10, offset = 0)
        String productName;
        @Field(length = 15, offset = 10, type = FieldType.CUSTOM)
        NestedClass nestedField;
    }

    static class NestedClass {
        @Field(length = 5, offset = 0)
        String code;
        @Field(length = 10, offset = 5, type = FieldType.DATE, format = Format.DATE_YYYY_MM_DD)
        LocalDate startDate;
    }
}
