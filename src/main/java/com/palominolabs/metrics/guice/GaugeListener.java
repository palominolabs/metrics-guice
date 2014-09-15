package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Gauge;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Method;

/**
 * A listener which adds gauge injection listeners to classes with gauges.
 */
class GaugeListener implements TypeListener {
    private final MetricRegistry metricRegistry;

    GaugeListener(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> literal, TypeEncounter<I> encounter) {
        Class<? super I> klass = literal.getRawType();
        for (final Method method : klass.getMethods()) {
            final Gauge annotation = method.getAnnotation(Gauge.class);
            if (annotation != null) {
                if (method.getParameterTypes().length == 0) {
                    final String metricName = determineName(annotation, klass, method);
                    encounter.register(new GaugeInjectionListener<I>(metricRegistry,
                        metricName,
                        method));
                } else {
                    encounter.addError("Method %s is annotated with @Gauge but requires parameters.",
                                       method);
                }
            }
        }
    }

    private static String determineName(Gauge annotation, Class<?> klass, Method method) {
        if (annotation.absolute()) {
            return annotation.name();
        }

        if (annotation.name().isEmpty()) {
            return MetricRegistry.name(klass, method.getName(), Gauge.class.getSimpleName());
        }

        return MetricRegistry.name(klass, annotation.name());
    }

}
