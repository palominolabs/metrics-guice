package com.palominolabs.metrics.guice.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Delegates to the provided list of resolvers, applying each resolver in turn.
 */
public class ListAnnotationResolver implements AnnotationResolver {
    private final List<AnnotationResolver> resolvers;

    public ListAnnotationResolver(List<AnnotationResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Nullable
    @Override
    public <T extends Annotation> T findAnnotation(@Nonnull Class<T> annotationClass, @Nonnull Method method) {
        for (AnnotationResolver resolver : resolvers) {
            T result = resolver.findAnnotation(annotationClass, method);
            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
