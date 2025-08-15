package com.parseme.annotation;

import java.lang.annotation.Retention;
import com.parseme.parser.FieldType;
import com.parseme.parser.Format;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Field {
    int length();
    int offset();
    FieldType type() default FieldType.STRING;
    Format format() default Format.EMPTY;
}
