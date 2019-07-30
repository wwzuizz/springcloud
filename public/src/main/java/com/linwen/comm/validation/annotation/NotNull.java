package com.linwen.comm.validation.annotation;

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
public @interface NotNull {
    String message() default "";

    boolean isValid() default true;

    String[] validRanges() default "all";

    String[] showRanges() default "all";
}
