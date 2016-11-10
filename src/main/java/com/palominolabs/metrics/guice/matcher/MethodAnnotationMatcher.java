package com.palominolabs.metrics.guice.matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Looks for annotations on the method itself.
 */
public class MethodAnnotationMatcher implements AnnotationMatcher {

    @Override
    @Nullable
    public <T extends Annotation> T match(@Nonnull final Class<T> annotationClass, @Nonnull final Method method) {
        return method.getAnnotation(annotationClass);
    }
}
