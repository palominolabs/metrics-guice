package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;

/**
 * A listener which adds method interceptors to metered methods.
 */
public class MeteredListener extends DeclaredMethodsTypeListener {
    @Inject
    private MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public MeteredListener(MetricNamer metricNamer, AnnotationResolver annotationResolver) {
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Metered annotation = annotationResolver.findAnnotation(Metered.class, method);
        if (annotation != null) {
            final Meter meter = metricRegistry.meter(metricNamer.getNameForMetered(method, annotation));
            return new MeteredInterceptor(meter);
        }
        return null;
    }
}
