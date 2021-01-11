package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Gauge;
import io.dropwizard.metrics5.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.dropwizard.metrics5.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DeclaringClassMetricNamer.GAUGE_SUFFIX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("unchecked")
abstract class GaugeTestBase {
    private InstrumentedWithGauge instance;
    MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Injector injector =
                Guice.createInjector(MetricsInstrumentationModule.builder()
                        .withMetricRegistry(registry)
                        .withMetricNamer(getMetricNamer())
                        .build());
        this.instance = injector.getInstance(InstrumentedWithGauge.class);
    }

    abstract MetricNamer getMetricNamer();

    @Test
    void aGaugeAnnotatedMethod() {
        instance.doAThing();

        final Gauge<String> metric = registry.getGauges().get(name(InstrumentedWithGauge.class, "things"));

        assertThat("Guice creates a metric",
                metric,
                is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                metric.getValue(),
                is("poop"));
    }

    @Test
    void aGaugeAnnotatedMethodWithDefaultName() {
        instance.doAnotherThing();

        final Gauge<String> metric = registry.getGauges().get(name(InstrumentedWithGauge.class,
                "doAnotherThing", GAUGE_SUFFIX));

        assertThat("Guice creates a metric",
                metric,
                is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                metric.getValue(),
                is("anotherThing"));
    }

    @Test
    void aGaugeAnnotatedMethodWithAbsoluteName() {
        instance.doAThingWithAbsoluteName();

        final Gauge<String> metric = registry.getGauges().get(name("absoluteName"));

        assertThat("Guice creates a metric",
                metric,
                is(notNullValue()));

        assertThat("Guice creates a gauge with the given value",
                metric.getValue(),
                is("anotherThingWithAbsoluteName"));
    }

    @Test
    void aGaugeInSuperclass() {
        final Gauge<?> metric = registry.getGauges().get(name("gaugeParent"));

        assertNotNull(metric);
        assertEquals("gaugeParent", metric.getValue());
    }

    @Test
    void aPrivateGaugeInSuperclass() {
        final Gauge<?> metric = registry.getGauges().get(name("gaugeParentPrivate"));

        assertNotNull(metric);
        assertEquals("gaugeParentPrivate", metric.getValue());
    }

    @Test
    void aPrivateGauge() {
        final Gauge<?> metric = registry.getGauges().get(name(InstrumentedWithGauge.class, "gaugePrivate"));

        assertNotNull(metric);
        assertEquals("gaugePrivate", metric.getValue());
    }
}
