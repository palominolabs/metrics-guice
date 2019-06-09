package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DeclaringClassMetricNamer.METERED_SUFFIX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class MeteredTest {
    private InstrumentedWithMetered instance;
    private MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Injector injector =
                Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(InstrumentedWithMetered.class);
    }

    @Test
    void aMeteredAnnotatedMethod() {

        instance.doAThing();

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "things"));

        assertMetricIsSetup(metric);

        assertThat("Guice creates a meter which gets marked",
                metric.getCount(),
                is(1L));
    }

    @Test
    void aMeteredAnnotatedMethodWithDefaultScope() {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "doAThingWithDefaultScope",
                METERED_SUFFIX));
        assertMetricIsSetup(metric);

        assertThat("Metric initialises to zero",
                metric.getCount(),
                is(0L));

        instance.doAThingWithDefaultScope();

        assertThat("Metric is marked",
                metric.getCount(),
                is(1L));
    }

    @Test
    void aMeteredAnnotatedMethodWithProtectedScope() {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "doAThingWithProtectedScope",
                METERED_SUFFIX));

        assertMetricIsSetup(metric);

        assertThat("Metric initialises to zero",
                metric.getCount(),
                is(0L));

        instance.doAThingWithProtectedScope();

        assertThat("Metric is marked",
                metric.getCount(),
                is(1L));
    }

    @Test
    void aMeteredAnnotatedMethodWithName() {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "n"));

        assertMetricIsSetup(metric);

        assertThat("Metric initialises to zero",
                metric.getCount(),
                is(0L));

        instance.doAThingWithName();

        assertThat("Metric is marked",
                metric.getCount(),
                is(1L));
    }

    @Test
    void aMeteredAnnotatedMethodWithAbsoluteName() {
        final Meter metric = registry.getMeters().get(name("nameAbs"));

        assertMetricIsSetup(metric);

        assertThat("Metric initialises to zero",
                metric.getCount(),
                is(0L));

        instance.doAThingWithAbsoluteName();

        assertThat("Metric is marked",
                metric.getCount(),
                is(1L));
    }

    private void assertMetricIsSetup(final Meter metric) {
        assertThat("Guice creates a metric",
                metric,
                is(notNullValue()));
    }
}
