package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Gauge;
import io.dropwizard.metrics5.MetricName;
import io.dropwizard.metrics5.MetricRegistry;
import com.google.inject.spi.InjectionListener;

import java.lang.reflect.Method;

/**
 * An injection listener which creates a gauge for the declaring class with the given name (or the method's name, if
 * none was provided) which returns the value returned by the annotated method.
 */
public class GaugeInjectionListener<I> implements InjectionListener<I> {
    private final MetricRegistry metricRegistry;
    private final MetricName metricName;
    private final Method method;

    public GaugeInjectionListener(MetricRegistry metricRegistry, MetricName metricName, Method method) {
        this.metricRegistry = metricRegistry;
        this.metricName = metricName;
        this.method = method;
    }

    @Override
    public void afterInjection(final I i) {
        metricRegistry.register(metricName, (Gauge<Object>) () -> {
            try {
                return method.invoke(i);
            } catch (Exception e) {
                return new RuntimeException(e);
            }
        });
    }
}
