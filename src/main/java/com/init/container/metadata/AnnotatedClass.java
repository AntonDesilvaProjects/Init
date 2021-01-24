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

    public AnnotatedClass(Class<?> sourceClass, Map<Class<?>, Set<AnnotatedElement>> annotationToAnnotatedElements) {
        this.sourceClass = sourceClass;
        this.annotationClassToAnnotatedElements = annotationToAnnotatedElements;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public Map<Class<?>, Set<AnnotatedElement>> getAnnotationClassToAnnotatedElements() {
        return annotationClassToAnnotatedElements;
    }
}
