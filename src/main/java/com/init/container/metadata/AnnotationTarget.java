package com.init.container.metadata;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

public class AnnotationTarget {
    private ElementType type;
    private Class clazz;
    private Method method;


    public static final class AnnotationTargetBuilder {
        private ElementType type;
        private Class clazz;
        private Method method;

        private AnnotationTargetBuilder() {
        }

        public static AnnotationTargetBuilder anAnnotationTarget() {
            return new AnnotationTargetBuilder();
        }

        public AnnotationTargetBuilder withType(ElementType type) {
            this.type = type;
            return this;
        }

        public AnnotationTargetBuilder withClazz(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public AnnotationTargetBuilder withMethod(Method method) {
            this.method = method;
            return this;
        }

        public AnnotationTarget build() {
            AnnotationTarget annotationTarget = new AnnotationTarget();
            annotationTarget.type = this.type;
            annotationTarget.method = this.method;
            annotationTarget.clazz = this.clazz;
            return annotationTarget;
        }
    }
}
