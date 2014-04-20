package com.palominolabs.metrics.guice;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.Scopes;

/**
 * A Guice module which instruments methods annotated with the {@link com.codahale.metrics.annotation.Metered}, {@link
 * com.codahale.metrics.annotation.Timed}, {@link com.codahale.metrics.annotation.Gauge}, and {@link
 * com.codahale.metrics.annotation.ExceptionMetered} annotations.
 *
 * @see com.codahale.metrics.annotation.Gauge
 * @see com.codahale.metrics.annotation.Metered
 * @see com.codahale.metrics.annotation.Timed
 * @see com.codahale.metrics.annotation.ExceptionMetered
 * @see MeteredInterceptor
 * @see TimedInterceptor
 * @see GaugeInjectionListener
 */
public class InstrumentationModule extends BaseInstrumentationModule {

    @Override
    protected void configure() {
        final MetricRegistry metricRegistry = createMetricRegistry();
        bind(MetricRegistry.class).toInstance(metricRegistry);
        bind(HealthCheckRegistry.class).toInstance(createHealthCheckRegistry());
        bindJmxReporter();
        bindMetricsListeners(metricRegistry);
    }

    /**
     * Override to provide a custom binding for {@link JmxReporter}
     */
    protected void bindJmxReporter() {
        bind(JmxReporter.class).toProvider(JmxReporterProvider.class).in(Scopes.SINGLETON);
    }

    /**
     * Override to provide a custom {@link HealthCheckRegistry}
     */
    protected HealthCheckRegistry createHealthCheckRegistry() {
        return new HealthCheckRegistry();
    }

    /**
     * Override to provide a custom {@link MetricRegistry}
     */
    protected MetricRegistry createMetricRegistry() {
        return new MetricRegistry();
    }
}
