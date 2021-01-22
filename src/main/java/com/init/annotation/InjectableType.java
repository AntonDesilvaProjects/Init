package com.init.annotation;

public enum InjectableType {
    /** Creates instance per container and applies it for every injection */
    SINGLETON,
    /** Creates instance per injection */
    INSTANCE
}
