package com.palominolabs.metrics.guice.matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotation matcher interface, provides implementation matched annotations.
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public interface AnnotationMatcher {
    public <T extends Annotation> T match(Class<T> clazz, Method method);
}
