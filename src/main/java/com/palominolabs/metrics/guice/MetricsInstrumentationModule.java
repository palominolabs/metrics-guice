package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
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
public class MetricsInstrumentationModule extends AbstractModule 
{
    private MetricRegistry metricRegistry;
    private final Matcher<? super TypeLiteral<?>> matcher;

    /**
     * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
     */
    public MetricsInstrumentationModule(MetricRegistry metricRegistry) {
        this();
        setRegistry(metricRegistry);
    }
    
    protected MetricsInstrumentationModule()
    {
        this.matcher = Matchers.any();
    }
    
    protected void setRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }

    /**
     * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
     * @param matcher The matcher to determine which types to look for metrics in
     */
    public MetricsInstrumentationModule(MetricRegistry metricRegistry, Matcher<? super TypeLiteral<?>> matcher) {
        this.metricRegistry = metricRegistry;
        this.matcher = matcher;
    }

    @Override
    protected void configure() {
        bindListener(matcher, new MeteredListener(metricRegistry));
        bindListener(matcher, new TimedListener(metricRegistry));
        bindListener(matcher, new GaugeListener(metricRegistry));
        bindListener(matcher, new ExceptionMeteredListener(metricRegistry));
    }
}
