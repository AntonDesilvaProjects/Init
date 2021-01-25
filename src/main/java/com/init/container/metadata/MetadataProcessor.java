package com.init.container.metadata;

import com.init.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetadataProcessor {
    private static final Set<Class<?>> FRAMEWORK_ANNOTATIONS = Set.of(
            Init.class, Injectable.class, InjectableApplication.class, Property.class, Configuration.class
    );
    private static final Set<Class<?>> CLASS_DEFINITION_ANNOTATIONS = Set.of(
            Injectable.class, Configuration.class
    );

    public void processMetadata(final String basePackagePath) {
        final List<AnnotatedClass> annotatedClasses = AnnotationUtils.scanForAnnotations(basePackagePath, FRAMEWORK_ANNOTATIONS);
        validateAnnotations(annotatedClasses);
        final Map<Class<?>, AnnotatedClass> classToAnnotatedMetaDataMap = annotatedClasses.stream().collect(Collectors.toMap(AnnotatedClass::getSourceClass, Function.identity()));
        buildClassDependencyGraph(classToAnnotatedMetaDataMap);
    }

    private void validateAnnotations(List<AnnotatedClass> annotatedClass) {
        // validate the annotated classes
        // injectable annotation can only be applied to method only in a configuration class
        // configuration/injectable can be applied to root level class or method - no fields
    }

    private Map<Class<?>, Object> buildInstanceCache(List<AnnotatedClass> annotatedClass) {
        // scan and build the properties first

        // build a map of class to dependencies


        // scan first for configuration files and build instances for those injectable methods
            // treat each injectable method as a constructor - any parameter to the method needs to be created first
        // then build instances for injectable classes

        return null;
    }

    private Object buildInstance(AnnotatedClass annotatedClass) {
        return null;
    }

    /*
     *  A 'class definition' is:
     *  - A class annotated with @Injectable at root class level
     *  - A class annotated with @Configuration at root class level. This is an edge case b/c configuration
     *    classes actually will not be injected anywhere.
     *  - A method annotated with @Injectable when invoked generates an instance. Can only be present in a
     *    class annotated with @Configuration
     *
     *  This method scans for class definitions and generates a map of class dependencies as follows:
     *
     *  class A => { class B, class C, class D}
     *  class B => { class C }
     *  class C => { }
     *  class D => { class B}
     *
     *  For injectable instances provided by methods, the combination of the method parameters
     *  and the enclosing classes dependencies as the dependencies for the returned instance.
     *
     * Collectors.groupingBy(annotatedClass -> annotatedClass.getAnnotationToAnnotatedElements()
                                    .keySet()
                                    .stream()
                                    .map(Annotation::getClass)
                                    .filter(CLASS_DEFINITION_ANNOTATIONS::contains)
                                    .findFirst()
                                    .orElse((Class) Annotation.class) // return default Annotation class; this will be ignored
     *
     * */
    private Map<AnnotatedClass, Set<AnnotatedClass>> buildClassDependencyGraph(final Map<Class<?>, AnnotatedClass> classToAnnotatedMetaDataMap) {
        // gather all root level annotated class definitions
        Map<Class<?>, List<AnnotatedClass>> rootLevelClassDefinitions = classToAnnotatedMetaDataMap.values().stream()
                .collect(Collectors.groupingBy(annotatedClass -> CLASS_DEFINITION_ANNOTATIONS.stream()
                                .filter(classDefAnnotation -> annotatedClass.getAnnotationClassToClassElements().containsKey(classDefAnnotation))
                                .findFirst()
                                .orElse(Annotation.class))
                );

        Map<Class<?>, Set<Dependency>> dependencyGraph = rootLevelClassDefinitions.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(AnnotatedClass::getSourceClass,
                        annotatedClass -> {
                            // extract dependencies for the class
                            // from the class's field, extract all dependency based annotation usages
                            Set<AnnotatedElement> fieldsToBeInitialized = Stream.of(Init.class, Property.class)
                                    .map(fieldAnnotation -> annotatedClass.getAnnotationClassToFieldElements().get(fieldAnnotation))
                                    .filter(Objects::nonNull)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toSet());
                            Set<Dependency> fieldDependencies = fieldsToBeInitialized.stream()
                                    .map(annotatedElement -> (Field) annotatedElement)
                                    .map(field -> {
                                        Set<Class<?>> fieldAnnotations = Arrays.stream(field.getDeclaredAnnotations()).map(Annotation::annotationType).collect(Collectors.toSet());
                                        Dependency.Builder builder = Dependency.Builder.aDependency();
                                        if (fieldAnnotations.contains(Init.class)) {
                                            final Class<?> dependencyClazz = field.getType();
                                            Init initAnnotation = field.getAnnotation(Init.class);
                                            if (dependencyClazz.isInterface()) {
                                                builder.withType(Dependency.Type.IMPLEMENTATION);
                                                builder.withImplementationName("".equals(initAnnotation.value()) ? null : initAnnotation.value());
                                            } else {
                                                builder.withType(Dependency.Type.INSTANCE);
                                            }
                                        } else if (fieldAnnotations.contains(Property.class)) {
                                            builder.withType(Dependency.Type.PROPERTY);
                                        } else {
                                            throw new IllegalArgumentException("Invalid annotation on class field");
                                        }
                                        return builder.build();
                                    })
                                    .collect(Collectors.toSet());

                            // next extract any additional dependencies based on no-arg ctor

                        return fieldDependencies;
                }));

        // for any beans defined as methods, combine the dependencies of the encompassing configuration
        // class along with the parameters for the method

        return null;
    }
}
