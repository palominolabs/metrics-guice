package com.palominolabs.metrics.guice;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class GaugeTest {
    private InstrumentedWithGauge instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(new InstrumentationModule(registry));
        this.instance = injector.getInstance(InstrumentedWithGauge.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void aGaugeAnnotatedMethod() throws Exception {
        instance.doAThing();

        final Gauge metric = registry.getGauges().get(name(InstrumentedWithGauge.class, "things"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                   ((Gauge<String>) metric).getValue(),
                   is("poop"));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void aGaugeAnnotatedMethodWithDefaultName() throws Exception {
        instance.doAnotherThing();

        final Gauge metric = registry.getGauges().get(name(InstrumentedWithGauge.class,
                                                                       "doAnotherThing"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                   ((Gauge<String>) metric).getValue(),
                   is("anotherThing"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void aGaugeAnnotatedMethodWithAbsoluteName() throws Exception {
        instance.doAThingWithAbsoluteName();

        final Gauge metric = registry.getGauges().get(name("absoluteName"));

        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                   ((Gauge<String>) metric).getValue(),
                   is("anotherThingWithAbsoluteName"));
    }

}
