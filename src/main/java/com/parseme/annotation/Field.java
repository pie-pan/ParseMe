package com.parseme.annotation;

import java.lang.annotation.Retention;
import com.parseme.type.FieldType;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Field {
    int length();
    int offset();
    FieldType type() default FieldType.STRING;
}
