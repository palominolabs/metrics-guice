package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DefaultMetricNamer.TIMED_SUFFIX;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TimedTest {
    private InstrumentedWithTimed instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(InstrumentedWithTimed.class);
    }

    @Test
    public void aTimedAnnotatedMethod() throws Exception {

        instance.doAThing();

        final Timer metric = registry.getTimers().get(name(InstrumentedWithTimed.class,
            "things"));

        assertMetricSetup(metric);

        assertThat("Guice creates a timer which records invocation length",
            metric.getCount(),
            is(1L));

        assertThat("Guice creates a timer which records invocation duration without underestimating too much",
            metric.getSnapshot().getMax(),
            is(greaterThan(NANOSECONDS.convert(5, MILLISECONDS))));

        assertThat("Guice creates a timer which records invocation duration without overestimating too much",
            metric.getSnapshot().getMax(),
            is(lessThan(NANOSECONDS.convert(15, MILLISECONDS))));
    }

    @Test
    public void aTimedAnnotatedMethodWithDefaultScope() throws Exception {

        instance.doAThingWithDefaultScope();

        final Timer metric = registry.getTimers().get(name(InstrumentedWithTimed.class,
            "doAThingWithDefaultScope", TIMED_SUFFIX));

        assertMetricSetup(metric);
    }

    @Test
    public void aTimedAnnotatedMethodWithProtectedScope() throws Exception {

        instance.doAThingWithProtectedScope();

        final Timer metric = registry.getTimers().get(name(InstrumentedWithTimed.class,
            "doAThingWithProtectedScope", TIMED_SUFFIX));

        assertMetricSetup(metric);
    }

    @Test
    public void aTimedAnnotatedMethodWithAbsoluteName() throws Exception {

        instance.doAThingWithAbsoluteName();

        final Timer metric = registry.getTimers().get(name("absoluteName"));

        assertMetricSetup(metric);
    }

    private void assertMetricSetup(final Timer metric) {
        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));
    }
}
