package com.piepan.parseme.annotation;

import java.lang.annotation.Retention;
import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Field {
    int length();
    int offset();
    FieldType type() default FieldType.STRING;
    Format format() default Format.EMPTY;
}
