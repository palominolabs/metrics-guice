package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.palominolabs.metrics.guice.matcher.AnnotationProvider;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to counted methods.
 */
public class CountedListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationProvider provider;

    public CountedListener(MetricRegistry metricRegistry, MetricNamer metricNamer, AnnotationProvider provider) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
        this.provider = provider;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Counted annotation = provider.getAnnotation(Counted.class, method);
        if (annotation != null) {
            final Counter counter = metricRegistry.counter(metricNamer.getNameForCounted(method, annotation));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }
}