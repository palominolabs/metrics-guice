package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener extends DeclaredMethodsTypeListener {
    private final Provider<MetricRegistry> metricRegistryProvider;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public TimedListener(Provider<MetricRegistry> metricRegistryProvider, MetricNamer metricNamer,
            final AnnotationResolver annotationResolver) {
        this.metricRegistryProvider = metricRegistryProvider;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Timed annotation = annotationResolver.findAnnotation(Timed.class, method);
        if (annotation != null) {
            final MetricRegistry metricRegistry = metricRegistryProvider.get();
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
