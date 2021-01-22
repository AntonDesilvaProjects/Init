package com.init.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to inject an initialized instance of the class or interface.
 *
 * @Init
 * MyClass myClass;
 *
 * @Init("specificInstanceName")
 * MyClass myClass;
 *
 * @Init
 * MyInterface interface;
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Init {
    String value() default "";
}
