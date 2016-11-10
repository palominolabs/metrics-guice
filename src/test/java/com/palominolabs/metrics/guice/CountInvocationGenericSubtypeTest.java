package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CountInvocationGenericSubtypeTest {

    private GenericThing<String> instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(StringThing.class);
    }

    @Test
    public void testCountsInvocationOfGenericOverride() {
        instance.doThing("foo");

        final Counter metric = registry.getCounters().get(name("stringThing"));

        assertNotNull(metric);

        assertEquals(1, metric.getCount());
    }
}
