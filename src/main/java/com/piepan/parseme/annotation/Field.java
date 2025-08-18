package com.piepan.parseme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;
import com.piepan.parseme.parser.PaddingType;

import static com.piepan.parseme.util.Constants.SPACE;

@Target(ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Field {
    int length();
    int offset();
    FieldType type() default FieldType.STRING;
    Format format() default Format.EMPTY;
    PaddingType padding() default PaddingType.NONE;
    char paddingChar() default SPACE;
}
