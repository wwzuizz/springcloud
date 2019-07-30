package com.linwen.comm.validation.annotation;


import com.linwen.comm.validation.enums.ParameterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linwen on 18-12-22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,
        ElementType.PARAMETER})
public @interface ValidParameter {
    String name();

    String fieldName() default "";

    String description() default "";

    ValidInfo[] valids() default {};

    NotNull notNull() default @NotNull(isValid = false);

    ParameterType parameterType() default ParameterType.String;

    ParameterValueExistDB ValueExistDB() default @ParameterValueExistDB(isValid = false, ValidClass = {});

    ParameterValueNotExistDB ValueNotExistDB() default @ParameterValueNotExistDB(isValid = false, ValidClass = {});

    String[] validRanges() default "all";

    String[] showRanges() default "all";
}
