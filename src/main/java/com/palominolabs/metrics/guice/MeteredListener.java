package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * A listener which adds method interceptors to metered methods.
 */
public class MeteredListener implements TypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public MeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Override
    public <T> void hear(TypeLiteral<T> literal,
        TypeEncounter<T> encounter) {
        Class<? super T> klass = literal.getRawType();

        do {
            for (Method method : klass.getDeclaredMethods()) {
                final MethodInterceptor interceptor = getInterceptor(metricRegistry, method);
                if (interceptor != null) {
                    encounter.bindInterceptor(Matchers.only(method), interceptor);
                }
            }
        } while ((klass = klass.getSuperclass()) != null);
    }

    @Nullable
    private MethodInterceptor getInterceptor(MetricRegistry metricRegistry, Method method) {
        final Metered annotation = method.getAnnotation(Metered.class);
        if (annotation != null) {
            final Meter meter = metricRegistry.meter(metricNamer.getNameForMetered(method, annotation));
            return new MeteredInterceptor(meter);
        }
        return null;
    }
}
