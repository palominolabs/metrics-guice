package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.junit.Assert.assertEquals;

public class GaugeInheritanceTest {

    @Test
    public void testInheritance() {
        MetricRegistry registry = new MetricRegistry();
        final Injector injector = Guice
                .createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        Parent parent = injector.getInstance(Parent.class);
        Child1 child1 = injector.getInstance(Child1.class);
        Child2 child2 = injector.getInstance(Child2.class);

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

        @com.codahale.metrics.annotation.Gauge
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
