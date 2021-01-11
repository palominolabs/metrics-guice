package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.MetricName;
import io.dropwizard.metrics5.annotation.Counted;
import io.dropwizard.metrics5.annotation.ExceptionMetered;
import io.dropwizard.metrics5.annotation.Gauge;
import io.dropwizard.metrics5.annotation.Metered;
import io.dropwizard.metrics5.annotation.Timed;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

import static io.dropwizard.metrics5.MetricRegistry.name;

/**
 * Uses the name fields in the metric annotations, if present, or the method declaring class and method name.
 */
public class DeclaringClassMetricNamer implements MetricNamer {
    static final String COUNTER_SUFFIX = "counter";
    static final String COUNTER_SUFFIX_MONOTONIC = "current";
    static final String GAUGE_SUFFIX = "gauge";
    static final String METERED_SUFFIX = "meter";
    static final String TIMED_SUFFIX = "timer";

    @Nonnull
    @Override
    public MetricName getNameForCounted(@Nonnull Method method, @Nonnull Counted counted) {
        if (counted.absolute()) {
            return name(counted.name());
        }

        if (counted.name().isEmpty()) {
            if (counted.monotonic()) {
                return name(method.getDeclaringClass(), method.getName(), COUNTER_SUFFIX_MONOTONIC);
            } else {
                return name(method.getDeclaringClass(), method.getName(), COUNTER_SUFFIX);
            }
        }

        return name(method.getDeclaringClass(), counted.name());
    }

    @Nonnull
    @Override
    public MetricName getNameForExceptionMetered(@Nonnull Method method, @Nonnull ExceptionMetered exceptionMetered) {
        if (exceptionMetered.absolute()) {
            return name(exceptionMetered.name());
        }

        if (exceptionMetered.name().isEmpty()) {
            return
                    name(method.getDeclaringClass(), method.getName(), ExceptionMetered.DEFAULT_NAME_SUFFIX);
        }

        return name(method.getDeclaringClass(), exceptionMetered.name());
    }

    @Nonnull
    @Override
    public MetricName getNameForGauge(@Nonnull Class<?> instanceClass, @Nonnull Method method, @Nonnull Gauge gauge) {
        if (gauge.absolute()) {
            return name(gauge.name());
        }

        if (gauge.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), GAUGE_SUFFIX);
        }

        return name(method.getDeclaringClass(), gauge.name());
    }

    @Nonnull
    @Override
    public MetricName getNameForMetered(@Nonnull Method method, @Nonnull Metered metered) {
        if (metered.absolute()) {
            return name(metered.name());
        }

        if (metered.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), METERED_SUFFIX);
        }

        return name(method.getDeclaringClass(), metered.name());
    }

    @Nonnull
    @Override
    public MetricName getNameForTimed(@Nonnull Method method, @Nonnull Timed timed) {
        if (timed.absolute()) {
            return name(timed.name());
        }

        if (timed.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), TIMED_SUFFIX);
        }

        return name(method.getDeclaringClass(), timed.name());
    }
}
