package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * An abstract Guice module which provides a method to instrument methods annotated with the
 * {@link com.codahale.metrics.annotation.Metered}, {@link com.codahale.metrics.annotation.Timed},
 * {@link com.codahale.metrics.annotation.Gauge}, and {@link com.codahale.metrics.annotation.ExceptionMetered}
 * annotations.
 *
 * @see com.palominolabs.metrics.guice.InstrumentationModule
 */
public abstract class BaseInstrumentationModule extends AbstractModule {

    protected void bindMetricsListeners(final MetricRegistry metricRegistry) {
        bindListener(Matchers.any(), new MeteredListener(metricRegistry));
        bindListener(Matchers.any(), new TimedListener(metricRegistry));
        bindListener(Matchers.any(), new GaugeListener(metricRegistry));
        bindListener(Matchers.any(), new ExceptionMeteredListener(metricRegistry));
    }
}
