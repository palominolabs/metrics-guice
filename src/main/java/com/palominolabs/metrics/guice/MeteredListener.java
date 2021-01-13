package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Meter;
import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.annotation.Metered;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to metered methods.
 */
public class MeteredListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public MeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer,
            AnnotationResolver annotationResolver) {
        this.metricRegistry = metricRegistry;
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
