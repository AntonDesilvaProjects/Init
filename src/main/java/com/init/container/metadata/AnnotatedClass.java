package com.init.container.metadata;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Set;

/*
           {
               qualifiedName: com.init.container.MyClass,
               'init' : {
                   'target' : {
                       'type' : com.init.container.MyClass2,
                       'targetType' : 'Type'
                    }
                   com.init.container.MyClass3
               },
               'injectable' : {
                   'target' : {
                       'type' : com.init.container.MyClass2,
                       'targetType' : 'Type'
                    },
                    'target' : {
                       'method' : Method,
                       'targetType' : 'Method'
                    }
               }
           }
       */
public class AnnotatedClass {
    private final Class<?> sourceClass;
    private final Map<Class<?>, Set<AnnotatedElement>> annotationClassToAnnotatedElements;
    private final Map<Class<?>, Set<AnnotatedElement>> annotationClassToFieldElements;
    private final Map<Class<?>, Set<AnnotatedElement>> annotationClassToMethodElements;
    private final Map<Class<?>, Set<AnnotatedElement>> annotationClassToClassElements;

    public AnnotatedClass(Class<?> sourceClass, Map<Class<?>, Set<AnnotatedElement>> annotationClassToAnnotatedElements, Map<Class<?>, Set<AnnotatedElement>> annotationClassToFieldElements, Map<Class<?>, Set<AnnotatedElement>> annotationClassToMethodElements, Map<Class<?>, Set<AnnotatedElement>> annotationClassToClassElements) {
        this.sourceClass = sourceClass;
        this.annotationClassToAnnotatedElements = annotationClassToAnnotatedElements;
        this.annotationClassToFieldElements = annotationClassToFieldElements;
        this.annotationClassToMethodElements = annotationClassToMethodElements;
        this.annotationClassToClassElements = annotationClassToClassElements;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToAnnotatedElements() {
        return annotationClassToAnnotatedElements;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToFieldElements() {
        return annotationClassToFieldElements;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToMethodElements() {
        return annotationClassToMethodElements;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToClassElements() {
        return annotationClassToClassElements;
    }
}
