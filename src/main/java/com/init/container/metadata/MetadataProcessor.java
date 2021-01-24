package com.init.container.metadata;

import com.init.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // gather all root level annotated class definition
        Map<Class<?>, List<AnnotatedClass>> rootLevelClassDefinitions = classToAnnotatedMetaDataMap.values().stream()
                .collect(Collectors.groupingBy(annotatedClass -> {
                            // for each annotated class, find the class level annotations
                            Class<?> rootLevelClassAnnotation = Annotation.class;
                            for (Class<?> classDefinitionAnnotation: CLASS_DEFINITION_ANNOTATIONS) {
                                if (annotatedClass.getAnnotationClassToAnnotatedElements().containsKey(classDefinitionAnnotation)) {
                                    Set<AnnotatedElement> annotatedElements = annotatedClass.getAnnotationClassToAnnotatedElements().get(classDefinitionAnnotation);
                                    if (annotatedElements.stream().anyMatch(annotatedElement -> annotatedElement instanceof Type)) {
                                        return classDefinitionAnnotation;
                                    }
                                }
                            }


                            return rootLevelClassAnnotation;
                        })
                );

        // for any beans defined as methods, generate 'synthetic' annotated class definition
        Optional.ofNullable(rootLevelClassDefinitions.get(Configuration.class)).orElse(List.of()).stream()
                .filter(configurationClass -> {
                    //
                    return true;
                })
                .map(configurationClass -> {
            return new AnnotatedClass(null, null);
        }).collect(Collectors.toSet());

        return null;
    }
}
