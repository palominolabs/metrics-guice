package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to methods that should be instrumented for exceptions
 */
public class ExceptionMeteredListener extends ClassHierarchyTraversingTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public ExceptionMeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final ExceptionMetered annotation = method.getAnnotation(ExceptionMetered.class);
        if (annotation != null) {
            final Meter meter = metricRegistry.meter(metricNamer.getNameForExceptionMetered(method, annotation));
            return new ExceptionMeteredInterceptor(meter, annotation.cause());
        }
        return null;
    }
}
