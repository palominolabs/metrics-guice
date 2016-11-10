package com.palominolabs.metrics.guice.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Looks for annotations on the enclosing class of the method.
 */
public class ClassAnnotationResolver implements AnnotationResolver {
    @Override
    @Nullable
    public <T extends Annotation> T findAnnotation(@Nonnull final Class<T> annotationClass,
            @Nonnull final Method method) {
        return method.getDeclaringClass().getAnnotation(annotationClass);
    }
}
