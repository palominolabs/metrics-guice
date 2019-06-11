package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener extends DeclaredMethodsTypeListener {
    @Inject
    private MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public TimedListener(MetricNamer metricNamer, final AnnotationResolver annotationResolver) {
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Timed annotation = annotationResolver.findAnnotation(Timed.class, method);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
