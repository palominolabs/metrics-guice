package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 *
 *
 */
public abstract class BaseInstrumentationModule extends AbstractModule {

    protected void bindMetricsListeners(final MetricRegistry metricRegistry) {
        bindListener(Matchers.any(), new MeteredListener(metricRegistry));
        bindListener(Matchers.any(), new TimedListener(metricRegistry));
        bindListener(Matchers.any(), new GaugeListener(metricRegistry));
        bindListener(Matchers.any(), new ExceptionMeteredListener(metricRegistry));
    }
}
