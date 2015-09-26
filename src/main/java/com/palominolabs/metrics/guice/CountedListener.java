package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to counted methods.
 */
public class CountedListener extends ClassHierarchyTraversingTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    CountedListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Counted annotation = method.getAnnotation(Counted.class);
        if (annotation != null) {
            final Counter counter = metricRegistry.counter(metricNamer.getNameForCounted(method, annotation));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }
}