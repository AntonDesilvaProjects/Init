package com.init.container.metadata;

import com.init.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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
     *  and the enclosing class itself will be added as the dependencies for the returned instance.
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
    private Map<Class<?>, Set<Dependency>> buildClassDependencyGraph(final Map<Class<?>, AnnotatedClass> classToAnnotatedMetaDataMap) {
        // gather all root level annotated class definitions
        final Map<Class<?>, List<AnnotatedClass>> rootLevelClassDefinitions = classToAnnotatedMetaDataMap.values().stream()
                .collect(Collectors.groupingBy(annotatedClass -> CLASS_DEFINITION_ANNOTATIONS.stream()
                        .filter(classDefAnnotation -> annotatedClass.getAnnotationClassToClassElements().containsKey(classDefAnnotation))
                        .findFirst()
                        .orElse(Annotation.class))
                );

        final Map<Class<?>, Set<Dependency>> dependencyGraph = rootLevelClassDefinitions.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(AnnotatedClass::getSourceClass, this::extractDependenciesForClass));

        // for any beans defined as methods, combine the dependencies of the encompassing configuration
        // class along with the parameters for the method
        for (AnnotatedClass annotatedClass: Optional.ofNullable(rootLevelClassDefinitions.get(Configuration.class)).orElse(List.of())) {
            for (AnnotatedElement annotatedElement: Optional.ofNullable(annotatedClass.getAnnotationClassToMethodElements().get(Injectable.class)).orElse(Set.of())) {
                final Method method = (Method) annotatedElement;
                // get return types to get the class
                final Class<?> returnType = method.getReturnType();
                if (returnType.isPrimitive()) {
                    throw new IllegalArgumentException("Only object types are allowed for method based class definitions");
                }
                // get the parameters to get the dependencies
                final Set<Dependency> dependencies = Arrays.stream(method.getParameters())
                        .map(this::buildDependencyForParameter)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());

                dependencies.add(Dependency.Builder.aDependency()
                        .withClass(annotatedClass.getSourceClass())
                        .withType(Dependency.Type.INSTANCE)
                        .build());

                if (dependencyGraph.containsKey(returnType)) {
                    throw new IllegalArgumentException(String.format("Multiple instance definitions provided for '%s'", returnType));
                }
                dependencyGraph.put(returnType, dependencies);
            }
        }

        return dependencyGraph;
    }

    private Set<Dependency> extractDependenciesForClass(AnnotatedClass annotatedClass) {
        // extract dependencies for the myclass
        // from the class's field, extract all dependency based annotation usages
        final Set<AnnotatedElement> fieldsToBeInitialized = Stream.of(Init.class, Property.class)
                .map(fieldAnnotation -> annotatedClass.getAnnotationClassToFieldElements().get(fieldAnnotation))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        final Set<Dependency> fieldDependencies = fieldsToBeInitialized.stream()
                .map(annotatedElement -> (Field) annotatedElement)
                .map(this::buildDependencyForClassField)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        // next extract any additional dependencies based on no-arg ctor
        final Constructor<?>[] constructors = annotatedClass.getSourceClass().getDeclaredConstructors();
        // we will allow only two types of ctors
        // a. no arg ctor - in this case, all dependencies will be initialized via field annotations
        // b. one arg-based ctor - there can only be ctor with params. If both no-arg and arg-based ctor
        // exists throw error. If more than one arg-based ctor exists throw error.
        if (constructors.length > 1) {
            throw new IllegalArgumentException("");
        }
        // we need to check here for either scalar or object values
        // scalars will be treated as properties but must have accompanying @Property annotation to identify property path
        // object params will be assumed to @Init params(if not specified)
        final Set<Dependency> constructorParamDependencies = Arrays.stream(constructors[0].getParameters())
                .map(this::buildDependencyForParameter)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        return Stream.of(fieldDependencies, constructorParamDependencies).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Optional<Dependency> buildDependencyForClassField(Field field) {
        final Dependency.Builder dependencyBuilder = Dependency.Builder.aDependency().withClass(field.getType());
        if (field.isAnnotationPresent(Init.class)) {
            Init initAnnotation = field.getAnnotation(Init.class);
            if (field.getType().isInterface()) {
                dependencyBuilder.withType(Dependency.Type.IMPLEMENTATION);
            } else {
                dependencyBuilder.withType(Dependency.Type.INSTANCE);
            }
            dependencyBuilder.withImplementationName("".equals(initAnnotation.value()) ? null : initAnnotation.value());
        } else if (field.isAnnotationPresent(Property.class)) {
            dependencyBuilder.withType(Dependency.Type.PROPERTY);
            dependencyBuilder.withPropertyResolverString(field.getDeclaredAnnotation(Property.class).value());
        } else {
            return Optional.empty(); // an unknown annotation - ignore
        }
        return Optional.of(dependencyBuilder.build());
    }

    private Optional<Dependency> buildDependencyForParameter(Parameter parameter) {
        final Class<?> paramType = parameter.getType();
        final Dependency.Builder dependencyBuilder = Dependency.Builder.aDependency().withClass(paramType);
        if (isPrimitiveOrPrimitiveWrapperOrString(paramType)) {
            final Property property = Optional.ofNullable(parameter.getAnnotation(Property.class))
                    .orElseThrow(() -> new IllegalArgumentException("Need to specify property!"));
            dependencyBuilder.withType(Dependency.Type.PROPERTY);
            dependencyBuilder.withPropertyResolverString(property.value());
        } else {
            // if annotation is present, check for instance name specifier
            // unlike with fields, we don't require the @Init annotation on constructor
            // parameters
            final Init initAnnotation = parameter.getAnnotation(Init.class);
            if (initAnnotation != null) {
                dependencyBuilder.withImplementationName("".equals(initAnnotation.value()) ? null : initAnnotation.value());
            }
            if (paramType.isInterface()) {
                dependencyBuilder.withType(Dependency.Type.IMPLEMENTATION);
            } else {
                dependencyBuilder.withType(Dependency.Type.INSTANCE);
            }
        }
        return Optional.of(dependencyBuilder.build());
    }

    private static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
    }


}
