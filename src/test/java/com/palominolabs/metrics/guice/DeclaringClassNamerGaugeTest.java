package com.palominolabs.metrics.guice;

import com.codahale.metrics.Gauge;
import org.junit.jupiter.api.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DeclaringClassMetricNamer.GAUGE_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeclaringClassNamerGaugeTest extends GaugeTestBase {
    @Override
    MetricNamer getMetricNamer() {
        return new DeclaringClassMetricNamer();
    }

    @Test
    void aGaugeWithoutNameInSuperclass() {
        // named for the declaring class
        final Gauge<?> metric =
                registry.getGauges().get(name(InstrumentedWithGaugeParent.class, "justAGaugeFromParent",
                        GAUGE_SUFFIX));

        assertNotNull(metric);
        assertEquals("justAGaugeFromParent", metric.getValue());
    }
}
