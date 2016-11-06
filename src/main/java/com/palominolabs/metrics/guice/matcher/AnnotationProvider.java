package com.palominolabs.metrics.guice.matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Provider uses annotation matchers to provide annotations.
 *
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public class AnnotationProvider {
    private final List<AnnotationMatcher> matchers;

    public AnnotationProvider(List<AnnotationMatcher> matchers) {
        this.matchers = matchers;
    }

    public <T extends Annotation> T getAnnotation(Class<T> clazz, Method method) {
        for (AnnotationMatcher matcher : matchers) {
            T result = matcher.match(clazz, method);
            if (result != null) return result;
        }

        return null;
    }
}
