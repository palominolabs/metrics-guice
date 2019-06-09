package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to counted methods.
 */
public class CountedListener extends DeclaredMethodsTypeListener {
    private final Provider<MetricRegistry> metricRegistryProvider;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public CountedListener(Provider<MetricRegistry> metricRegistryProvider, MetricNamer metricNamer,
            AnnotationResolver annotationResolver) {
        this.metricRegistryProvider = metricRegistryProvider;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Counted annotation = annotationResolver.findAnnotation(Counted.class, method);
        if (annotation != null) {
            final MetricRegistry metricRegistry = metricRegistryProvider.get();
            final Counter counter = metricRegistry.counter(metricNamer.getNameForCounted(method, annotation));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }
}
