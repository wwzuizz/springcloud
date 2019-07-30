package com.linwen.comm.validation.annotation;


import com.linwen.comm.validation.enums.Expression;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by linwen on 18-12-22.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ValidInfo {
    Expression name() default Expression.notNull;

    String message() default "";

    String targetValue() default "";

    String[] validRanges() default "all";

    String[] showRanges() default "all";
}
