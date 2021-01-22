package com.init.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied on class definition, or method return type to describe a source
 * of injectable class instances. For class definitions, the instance name will be
 * camel-cased name of the class. For example:
 *
 * @Injectable
 * class TransactionManager {}
 *
 * The instance qualifier will be "transactionManager". For methods, the name of the method
 * will be used.
 *
 * Injectable instances can be classified as:
 *  a. Singleton - only a single instance will be created and injected into all injections. This
 *      is the default setting.
 *  b. Instance - a new instance will be created every time an injection is required.
 *
 * If applied on a class implementing a particular interface, then the @Init annotation can be used
 * to inject an instance of an implementor.
 *
 * For any injectable class, any constructor based dependencies will be automatically injected.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Injectable {
    String value() default "";
    InjectableType type() default InjectableType.SINGLETON;
}
