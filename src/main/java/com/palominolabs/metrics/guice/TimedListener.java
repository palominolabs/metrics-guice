package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener implements TypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public TimedListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
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
        final Timed annotation = method.getAnnotation(Timed.class);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
