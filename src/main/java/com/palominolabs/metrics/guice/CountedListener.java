package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import com.codahale.metrics.Counter;
import com.codahale.metrics.annotation.Counted;
import org.aopalliance.intercept.MethodInterceptor;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.annotation.Nullable;

/**
 * A listener which adds method interceptors to counted methods.
 */
public class CountedListener implements TypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    CountedListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Override
    public <T> void hear(TypeLiteral<T> literal,
        TypeEncounter<T> encounter) {
        Class<? super T> klass = literal.getRawType();

        do {
            for (Method method : klass.getDeclaredMethods()) {
                final MethodInterceptor interceptor = forMethod(metricRegistry, method);
                if (interceptor != null) {
                    encounter.bindInterceptor(Matchers.only(method), interceptor);
                }
            }
        } while ((klass = klass.getSuperclass()) != null);
    }

    @Nullable
    private MethodInterceptor forMethod(MetricRegistry metricRegistry, Method method) {
        final Counted annotation = method.getAnnotation(Counted.class);
        if (annotation != null) {
            final Counter counter = metricRegistry.counter(metricNamer.nameForCounted(method, annotation));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }
}