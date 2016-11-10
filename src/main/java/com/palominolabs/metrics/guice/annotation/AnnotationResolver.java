package com.palominolabs.metrics.guice.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Finds annotations, if any, pertaining to a particular Method. Extension point for customizing annotation lookup.
 */
public interface AnnotationResolver {
    /**
     * @param annotationClass Metrics annotation to look for
     * @param method          method that the corresponding metric may be applied to
     * @param <T>             annotation type
     * @return a T instance, if found, else null
     */
    @Nullable
    <T extends Annotation> T findAnnotation(@Nonnull Class<T> annotationClass, @Nonnull Method method);
}
