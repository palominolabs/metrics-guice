package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to metered methods.
 */
public class MeteredListener extends ClassHierarchyTraversingTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public MeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Metered annotation = method.getAnnotation(Metered.class);
        if (annotation != null) {
            final Meter meter = metricRegistry.meter(metricNamer.getNameForMetered(method, annotation));
            return new MeteredInterceptor(meter);
        }
        return null;
    }
}
