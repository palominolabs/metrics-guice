package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.MetricName;
import io.dropwizard.metrics5.annotation.Gauge;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

import static io.dropwizard.metrics5.MetricRegistry.name;

/**
 * For gauges (which can reside in superclasses of the type being instantiated), this will use the instantiated type for
 * the resulting metric name no matter what superclass declares the gauge method. This allows for a gauge located in a
 * superclass to be used by multiple inheritors without causing a duplicate metric name clash.
 *
 * For other metric types, which are not available on superclass methods, the declaring class (which would be the same
 * as the instantiated class) is used, as in {@link DeclaringClassMetricNamer}.
 */
public class GaugeInstanceClassMetricNamer extends DeclaringClassMetricNamer {
    @Nonnull
    @Override
    public MetricName getNameForGauge(@Nonnull Class<?> instanceClass, @Nonnull Method method, @Nonnull Gauge gauge) {
        if (gauge.absolute()) {
            return name(gauge.name());
        }

        if (gauge.name().isEmpty()) {
            return name(instanceClass, method.getName(), GAUGE_SUFFIX);
        }

        return name(instanceClass, gauge.name());
    }
}
