package com.init.container.metadata;

import com.init.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetadataProcessor {
    private static final Set<Class<?>> FRAMEWORK_ANNOTATIONS = Set.of(
            Init.class, Injectable.class, InjectableApplication.class, Property.class, Configuration.class
    );

    public void processMetadata(final String basePackagePath) {
        final List<AnnotatedClass> annotatedClasses = AnnotationUtils.scanForAnnotations(basePackagePath, FRAMEWORK_ANNOTATIONS);
        validateAnnotations(annotatedClasses);
    }

    private void validateAnnotations(List<AnnotatedClass> annotatedClass) {
        // validate the annotated classes
        // injectable annotation can only be applied to method only in a configuration class
    }

    private Map<Class<?>, Object> buildInstanceCache(List<AnnotatedClass> annotatedClass) {
        // scan first for configuration files and build instances for those injectable methods
            // treat each injectable method as a constructor - any parameter to the method needs to be created first
        // then build instances for injectable classes
        return null;
    }

    private Object buildInstance(AnnotatedClass annotatedClass) {
        return null;
    }
}
