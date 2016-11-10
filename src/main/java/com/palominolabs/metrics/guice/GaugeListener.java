package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Gauge;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;

/**
 * A listener which adds gauge injection listeners to classes with gauges.
 */
public class GaugeListener implements TypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public GaugeListener(MetricRegistry metricRegistry, MetricNamer metricNamer,
            final AnnotationResolver annotationResolver) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> literal, TypeEncounter<I> encounter) {
        Class<? super I> klass = literal.getRawType();

        do {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.isSynthetic()) {
                    continue;
                }

                final Gauge annotation = annotationResolver.findAnnotation(Gauge.class, method);
                if (annotation != null) {
                    if (method.getParameterTypes().length == 0) {
                        final String metricName = metricNamer.getNameForGauge(method, annotation);

                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }

                        encounter.register(new GaugeInjectionListener<I>(metricRegistry, metricName, method));
                    } else {
                        encounter.addError("Method %s is annotated with @Gauge but requires parameters.",
                                method);
                    }
                }
            }
        } while ((klass = klass.getSuperclass()) != null);
    }
}
