package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * A Guice module which instruments methods annotated with the {@link Metered}, {@link Timed}, {@link Gauge}, and {@link
 * ExceptionMetered} annotations.
 *
 * @see Gauge
 * @see Metered
 * @see Timed
 * @see ExceptionMetered
 * @see MeteredInterceptor
 * @see TimedInterceptor
 * @see GaugeInjectionListener
 */
public class InstrumentationModule extends AbstractModule {

    private final MetricRegistry metricRegistry;

    /**
     * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
     */
    public InstrumentationModule(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new MeteredListener(metricRegistry));
        bindListener(Matchers.any(), new TimedListener(metricRegistry));
        bindListener(Matchers.any(), new GaugeListener(metricRegistry));
        bindListener(Matchers.any(), new ExceptionMeteredListener(metricRegistry));
    }
}
