package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import static io.dropwizard.metrics5.MetricRegistry.name;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GaugeInheritanceTest {

    @Test
    void testInheritance() {
        MetricRegistry registry = new MetricRegistry();
        final Injector injector = Guice
                .createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        injector.getInstance(Parent.class);
        injector.getInstance(Child1.class);
        injector.getInstance(Child2.class);

        // gauge in parent class is registered separately for each

        assertEquals(0,
                registry.getGauges().get(name(Parent.class, "aGauge", DeclaringClassMetricNamer.GAUGE_SUFFIX))
                        .getValue());
        assertEquals(1,
                registry.getGauges().get(name(Child1.class, "aGauge", DeclaringClassMetricNamer.GAUGE_SUFFIX))
                        .getValue());
        assertEquals(2,
                registry.getGauges().get(name(Child2.class, "aGauge", DeclaringClassMetricNamer.GAUGE_SUFFIX))
                        .getValue());
    }

    static class Parent {

        @io.dropwizard.metrics5.annotation.Gauge
        int aGauge() {
            return complexInternalCalculation();
        }

        int complexInternalCalculation() {
            return 0;
        }
    }

    static class Child1 extends Parent {
        @Override
        int complexInternalCalculation() {
            return 1;
        }
    }

    static class Child2 extends Parent {
        @Override
        int complexInternalCalculation() {
            return 2;
        }
    }
}
