package com.init.container.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Objects;
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
    private Class<?> sourceClass;
    private Map<Annotation, Set<AnnotatedElement>> annotationToAnnotatedElements;

    public AnnotatedClass(Class<?> sourceClass, Map<Annotation, Set<AnnotatedElement>> annotationToAnnotatedElements) {
        this.sourceClass = sourceClass;
        this.annotationToAnnotatedElements = annotationToAnnotatedElements;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public AnnotatedClass setSourceClass(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
        return this;
    }

    public Map<Annotation, Set<AnnotatedElement>> getAnnotationToAnnotatedElements() {
        return annotationToAnnotatedElements;
    }

    public AnnotatedClass setAnnotationToAnnotatedElements(Map<Annotation, Set<AnnotatedElement>> annotationToAnnotatedElements) {
        this.annotationToAnnotatedElements = annotationToAnnotatedElements;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedClass that = (AnnotatedClass) o;
        return Objects.equals(sourceClass, that.sourceClass) && Objects.equals(annotationToAnnotatedElements, that.annotationToAnnotatedElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, annotationToAnnotatedElements);
    }
}
