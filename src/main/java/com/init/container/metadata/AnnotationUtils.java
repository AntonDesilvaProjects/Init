package com.init.container.metadata;

import com.init.annotation.Init;
import com.init.annotation.Injectable;
import com.init.annotation.InjectableApplication;
import com.init.annotation.Property;
import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import org.reflections8.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationUtils {
    /**
     *  Given a package prefix and set of annotations, finds any class in the package or any subpackage with
     *  at least one of the given annotations.
     *
     * @param basePackagePath base package to start the search from
     * @param annotationSet set of annotations to look for
     * @return List<AnnotationMetaData> list of classes which has at least one of the given annotations
     * */
    public static List<AnnotatedClass> scanForAnnotations(final String basePackagePath, Set<Class<?>> annotationSet) {
        // given a package name, find all classes within and scan for annotations
        // if any of the supplied annotations are found, then add it to the list
        return getClasses(basePackagePath)
                .stream()
                .map(clazz -> getAnnotationsForClass(clazz, annotationSet))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static Set<Class<?>> getClasses(final String packagePrefix) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader()))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packagePrefix))));
        return reflections.getSubTypesOf(Object.class);
    }

    public static Optional<AnnotatedClass> getAnnotationsForClass(Class<?> clazz, Set<Class<?>> annotations) {
        final Map<Class<?>, Set<AnnotatedElement>> annotationClassToAnnotatedElements = new HashMap<>();
        Stream.of(clazz.getDeclaredMethods(), clazz.getDeclaredFields(), clazz.getClasses(), new AnnotatedElement[] {clazz})
                .flatMap(Arrays::stream)
                .forEach(annotatedElement ->
                    Arrays.stream(annotatedElement.getDeclaredAnnotations()).forEach(annotation -> {
                        if (annotations.contains(annotation.getClass())) {
                            if (annotationClassToAnnotatedElements.containsKey(annotation.getClass())) {
                                annotationClassToAnnotatedElements.get(annotation.getClass()).add(annotatedElement);
                            } else {
                                annotationClassToAnnotatedElements.put(annotation.getClass(), new HashSet<>(Set.of(annotatedElement)));
                            }
                        }
                    })
                );
        return annotationClassToAnnotatedElements.isEmpty() ? Optional.empty() : Optional.of(new AnnotatedClass(clazz, annotationClassToAnnotatedElements));
    }


    public static void main2(String... args) {
//        Reflections reflections = new Reflections(
//                new ConfigurationBuilder().setUrls(
//                        ClasspathHelper.forPackage( "com.application" ) ).setScanners(
//                        new MethodAnnotationsScanner(), new FieldAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner()) );
//        Set<Field> methods = reflections.getFieldsAnnotatedWith( Injectable.class);
//        Set<Class<?>> types = reflections.getTypesAnnotatedWith( Injectable.class);
//        System.out.println(methods);


        List<ClassLoader> classLoaderList = List.of(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoaderList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.application"))));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        Class<?> clazz = classes.iterator().next();
        Method[] methods = clazz.getDeclaredMethods();
        Field[] fields = clazz.getDeclaredFields();
        Class[] clazzMethods = clazz.getClasses();
        Annotation[] declaredAnnotaions = clazz.getDeclaredAnnotations();

//        System.out.println(methods);
//        System.out.println(fields);
//        System.out.println(clazzMethods);

        Optional<AnnotatedClass> metadata = getAnnotationsForClass(clazz, Set.of(Init.class, Injectable.class, InjectableApplication.class, Property.class));
        System.out.println(metadata);
    }

    public static void main(String... args) {
        MetadataProcessor metadataProcessor = new MetadataProcessor();
        metadataProcessor.processMetadata("com.application");
    }
}
