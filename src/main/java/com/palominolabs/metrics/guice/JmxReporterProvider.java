package com.palominolabs.metrics.guice;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class JmxReporterProvider implements Provider<JmxReporter> {
    private final MetricRegistry metricRegistry;

    @Inject
    public JmxReporterProvider(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public JmxReporter get() {
        final JmxReporter reporter = JmxReporter.forRegistry(metricRegistry).build();
        reporter.start();
        return reporter;
    }
}
