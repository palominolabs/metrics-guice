package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MeteredTest {
    private InstrumentedWithMetered instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(new InstrumentationModule() {
            @Override
            protected MetricRegistry createMetricRegistry() {
                return registry;
            }
        });
        this.instance = injector.getInstance(InstrumentedWithMetered.class);
    }

    @Test
    public void aMeteredAnnotatedMethod() throws Exception {

        instance.doAThing();

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "things"));

        assertMetricIsSetup(metric);

        assertThat("Guice creates a meter which gets marked",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void aMeteredAnnotatedMethodWithDefaultScope() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "doAThingWithDefaultScope"));
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
    public void aMeteredAnnotatedMethodWithProtectedScope() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "doAThingWithProtectedScope"));

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
    public void aMeteredAnnotatedMethodWithName() throws Exception {

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
    public void aMeteredAnnotatedMethodWithAbsoluteName() {
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
