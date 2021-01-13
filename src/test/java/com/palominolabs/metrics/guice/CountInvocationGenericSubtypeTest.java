package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Counter;
import io.dropwizard.metrics5.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.dropwizard.metrics5.MetricRegistry.name;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CountInvocationGenericSubtypeTest {

    private GenericThing<String> instance;
    private MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(StringThing.class);
    }

    @Test
    void testCountsInvocationOfGenericOverride() {
        instance.doThing("foo");

        final Counter metric = registry.getCounters().get(name("stringThing"));

        assertNotNull(metric);

        assertEquals(1, metric.getCount());
    }
}
