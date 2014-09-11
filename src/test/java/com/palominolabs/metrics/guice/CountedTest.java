package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CountedTest {
    private InstrumentedWithCounter instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(new MetricsInstrumentationModule(registry));
        this.instance = injector.getInstance(InstrumentedWithCounter.class);
    }

    @Test
    public void aCounterAnnotatedMethod() throws Exception {
        instance.doAThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class, "things"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
                   ((Counter) metric).getCount(),
                   is((long)1));
    }


    @Test
    public void aCounterAnnotatedMethodWithDefaultName() throws Exception {
        instance.doAnotherThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class,
                                                                       "doAnotherThing"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
                   ((Counter) metric).getCount(),
                   is((long)1));
    }

    @Test
    public void aCounterAnnotatedMethodWithAbsoluteName() throws Exception {
        instance.doAThingWithAbsoluteName();

        final Counter metric = registry.getCounters().get(name("absoluteName"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
                   ((Counter) metric).getCount(),
                   is((long)1));
    }

}
