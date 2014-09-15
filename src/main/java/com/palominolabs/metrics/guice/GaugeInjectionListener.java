package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.spi.InjectionListener;

/**
 * An injection listener which creates a gauge for the declaring class with the given name (or the
 * method's name, if none was provided) which returns the value returned by the annotated method.
 */
public class GaugeInjectionListener<I> implements InjectionListener<I> {
    private final MetricRegistry metricRegistry;
    private final String metricName;
    private final Method method;

    public GaugeInjectionListener(MetricRegistry metricRegistry, String metricName, Method method) {
        this.metricRegistry = metricRegistry;
        this.metricName = metricName;
        this.method = method;
    }

    @Override
    public void afterInjection(final I i) {
        metricRegistry.register(metricName, new Gauge<Object>() {
            @Override
            public Object getValue() {
                try {
                    return method.invoke(i);
                } catch (Exception e) {
                    return new RuntimeException(e);
                }
            }
        });
    }
}
