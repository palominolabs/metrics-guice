package com.palominolabs.metrics.guice;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.Scopes;

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
     *
     * @return HealthCheckRegistry instance t6 bind
     */
    protected HealthCheckRegistry createHealthCheckRegistry() {
        return new HealthCheckRegistry();
    }

    /**
     * Override to provide a custom {@link MetricRegistry}
     *
     * @return MetricRegistry instance to bind
     */
    protected MetricRegistry createMetricRegistry() {
        return new MetricRegistry();
    }
}
