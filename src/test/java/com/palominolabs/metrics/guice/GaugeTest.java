package com.palominolabs.metrics.guice;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DefaultMetricNamer.GAUGE_SUFFIX;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class GaugeTest {
    private InstrumentedWithGauge instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(InstrumentedWithGauge.class);
    }

    @Test
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
    public void aGaugeAnnotatedMethodWithDefaultName() throws Exception {
        instance.doAnotherThing();

        final Gauge metric = registry.getGauges().get(name(InstrumentedWithGauge.class,
            "doAnotherThing", GAUGE_SUFFIX));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
            ((Gauge<String>) metric).getValue(),
            is("anotherThing"));
    }

    @Test
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

    @Test
    public void aGaugeWithoutNameInSuperclass() throws Exception {
        final Gauge<?> metric = registry.getGauges().get(name(InstrumentedWithGaugeParent.class,"justAGaugeFromParent", GAUGE_SUFFIX));

        assertNotNull(metric);
        assertEquals("justAGaugeFromParent", metric.getValue());
    }

    @Test
    public void aGaugeInSuperclass() throws Exception {
        final Gauge<?> metric = registry.getGauges().get(name("gaugeParent"));

        assertNotNull(metric);
        assertEquals("gaugeParent", metric.getValue());
    }

    @Test
    public void aPrivateGaugeInSuperclass() throws Exception {
        final Gauge<?> metric = registry.getGauges().get(name("gaugeParentPrivate"));

        assertNotNull(metric);
        assertEquals("gaugeParentPrivate", metric.getValue());
    }

    @Test
    public void aPrivateGauge() throws Exception {
        final Gauge<?> metric = registry.getGauges().get(name(InstrumentedWithGauge.class, "gaugePrivate"));

        assertNotNull(metric);
        assertEquals("gaugePrivate", metric.getValue());
    }
}
