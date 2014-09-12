package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

/**
 * A listener which adds method interceptors to methods that should be instrumented for exceptions
 */
public class ExceptionMeteredListener implements TypeListener {
    private final MetricRegistry metricRegistry;

    ExceptionMeteredListener(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public <T> void hear(TypeLiteral<T> literal,
                         TypeEncounter<T> encounter) {
        final Class<?> klass = literal.getRawType();
        for (Method method : klass.getDeclaredMethods()) {
            final MethodInterceptor interceptor = ExceptionMeteredInterceptor.forMethod(
                    metricRegistry,
                    klass,
                    method);

            if (interceptor != null) {
                encounter.bindInterceptor(Matchers.only(method), interceptor);
            }
        }
    }
}
