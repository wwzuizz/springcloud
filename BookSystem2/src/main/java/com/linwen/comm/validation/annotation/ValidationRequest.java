package com.linwen.comm.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linwen on 18-12-22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidationRequest {
    String category() default "";

    String method() default "POST";

    String description() default "";

    String name() default "";

    String validRange();
}

