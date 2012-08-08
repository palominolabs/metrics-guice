package com.palominolabs.metrics.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.yammer.metrics.annotation.Gauge;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

import java.lang.reflect.Method;

/**
 * A listener which adds gauge injection listeners to classes with gauges.
 */
class GaugeListener implements TypeListener {
    private final MetricsRegistry metricsRegistry;

    GaugeListener(MetricsRegistry metricsRegistry) {
        this.metricsRegistry = metricsRegistry;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> literal, TypeEncounter<I> encounter) {
        Class<? super I> klass = literal.getRawType();
        for (final Method method : klass.getMethods()) {
            final Gauge annotation = method.getAnnotation(Gauge.class);
            if (annotation != null) {
                if (method.getParameterTypes().length == 0) {
                    final MetricName metricName = MetricName.forGaugeMethod(klass, method, annotation);
                    encounter.register(new GaugeInjectionListener<I>(metricsRegistry,
                                                                     metricName,
                                                                     method));
                } else {
                    encounter.addError("Method %s is annotated with @Gauge but requires parameters.",
                                       method);
                }
            }
        }
    }

}
