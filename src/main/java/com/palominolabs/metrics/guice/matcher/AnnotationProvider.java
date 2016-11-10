package com.palominolabs.metrics.guice.matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Uses the configured matchers in order to find any annotations pertaining to this method.
 */
public class AnnotationProvider {
    private final List<AnnotationMatcher> matchers;

    public AnnotationProvider(List<AnnotationMatcher> matchers) {
        this.matchers = matchers;
    }

    @Nullable
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass, Method method) {
        for (AnnotationMatcher matcher : matchers) {
            T result = matcher.match(annotationClass, method);
            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
