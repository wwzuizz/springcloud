package com.linwen.comm.validation.annotation;


import com.linwen.comm.validation.Validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by linwen on 18-12-23.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ParameterValueExistDB {
    Class<? extends Validator>[] ValidClass();

    boolean isValid() default true;

    String column() default "";

    String table() default "";

    String message() default "";

    String[] validRanges() default "all";

    String[] showRanges() default "all";
}
