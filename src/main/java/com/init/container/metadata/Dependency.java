package com.init.container.metadata;

public class Dependency {
    enum Type {
        PROPERTY, // the receiver is dependent on a static property
        INSTANCE, // the receiver is dependent on instance of a class
        IMPLEMENTATION // the receiver is dependent on an implementation of an interface
    }
    private Type type;
    private Class<?> clazz;
    private String implementationName; // name of the specific implementation of interface

    public static final class Builder {
        private Type type;
        private Class<?> clazz;
        private String implementationName; // name of the specific implementation of interface

        private Builder() {
        }

        public static Builder aDependency() {
            return new Builder();
        }

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder withClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder withImplementationName(String implementationName) {
            this.implementationName = implementationName;
            return this;
        }

        public Dependency build() {
            Dependency dependency = new Dependency();
            dependency.implementationName = this.implementationName;
            dependency.type = this.type;
            dependency.clazz = this.clazz;
            return dependency;
        }
    }
}
