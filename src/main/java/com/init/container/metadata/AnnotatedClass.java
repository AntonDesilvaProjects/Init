package com.init.container.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public AnnotatedClass(Class<?> sourceClass, Map<Class<?>, Set<AnnotatedElement>> annotationToAnnotatedElements) {
        this.sourceClass = sourceClass;
        this.annotationClassToAnnotatedElements = annotationToAnnotatedElements.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().getClass(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToAnnotatedElements() {
        return annotationClassToAnnotatedElements;
    }


}
