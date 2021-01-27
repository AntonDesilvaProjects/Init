package com.init.annotation;

import java.lang.annotation.*;

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
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Init {
    String value() default "";
}
