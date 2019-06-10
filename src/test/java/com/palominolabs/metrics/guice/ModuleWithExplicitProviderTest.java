package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ModuleWithExplicitProviderTest {
    private InstrumentedWithCounter instance;
    private MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(
                MetricsInstrumentationModule.builder().withMetricRegistryProvider(() -> registry).build());
        this.instance = injector.getInstance(InstrumentedWithCounter.class);
    }


    @Test
    void aCounterAnnotatedMethod() {
        instance.doAThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class, "things"));

        assertThat("Guice creates a metric",
                metric,
                is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
                metric.getCount(),
                is((long) 1));
    }
}
