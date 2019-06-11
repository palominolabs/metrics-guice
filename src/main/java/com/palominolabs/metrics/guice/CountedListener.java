package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;

/**
 * A listener which adds method interceptors to counted methods.
 */
public class CountedListener extends DeclaredMethodsTypeListener {
    @Inject
    private MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public CountedListener(MetricNamer metricNamer, AnnotationResolver annotationResolver) {
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Counted annotation = annotationResolver.findAnnotation(Counted.class, method);
        if (annotation != null) {
            final Counter counter = metricRegistry.counter(metricNamer.getNameForCounted(method, annotation));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }
}
