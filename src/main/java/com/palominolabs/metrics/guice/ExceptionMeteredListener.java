package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to methods that should be instrumented for exceptions
 */
public class ExceptionMeteredListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public ExceptionMeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer,
            final AnnotationResolver annotationResolver) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final ExceptionMetered annotation = annotationResolver.findAnnotation(ExceptionMetered.class, method);
        if (annotation != null) {
            final Meter meter = metricRegistry.meter(metricNamer.getNameForExceptionMetered(method, annotation));
            return new ExceptionMeteredInterceptor(meter, annotation.cause());
        }
        return null;
    }
}
