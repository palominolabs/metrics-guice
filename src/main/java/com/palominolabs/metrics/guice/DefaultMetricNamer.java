package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Implements the default metric naming policy.
 */
public class DefaultMetricNamer implements MetricNamer {
    static final String COUNTER_SUFFIX = "counter";
    static final String COUNTER_SUFFIX_MONOTONIC = "current";
    static final String GAUGE_SUFFIX = "gauge";
    static final String METERED_SUFFIX = "meter";
    static final String TIMED_SUFFIX = "timer";

    @Nonnull
    @Override
    public String getNameForCounted(@Nonnull Method method, @Nonnull Counted counted) {
        if (counted.absolute()) {
            return counted.name();
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
    public String getNameForExceptionMetered(@Nonnull Method method, @Nonnull ExceptionMetered exceptionMetered) {
        if (exceptionMetered.absolute()) {
            return exceptionMetered.name();
        }

        if (exceptionMetered.name().isEmpty()) {
            return
                name(method.getDeclaringClass(), method.getName(), ExceptionMetered.DEFAULT_NAME_SUFFIX);
        }

        return name(method.getDeclaringClass(), exceptionMetered.name());
    }

    @Nonnull
    @Override
    public String getNameForGauge(@Nonnull Method method, @Nonnull Gauge gauge) {
        if (gauge.absolute()) {
            return gauge.name();
        }

        if (gauge.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), GAUGE_SUFFIX);
        }

        return name(method.getDeclaringClass(), gauge.name());
    }

    @Nonnull
    @Override
    public String getNameForMetered(@Nonnull Method method, @Nonnull Metered metered) {
        if (metered.absolute()) {
            return metered.name();
        }

        if (metered.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), METERED_SUFFIX);
        }

        return name(method.getDeclaringClass(), metered.name());
    }

    @Nonnull
    @Override
    public String getNameForTimed(@Nonnull Method method, @Nonnull Timed timed) {
        if (timed.absolute()) {
            return timed.name();
        }

        if (timed.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), TIMED_SUFFIX);
        }

        return name(method.getDeclaringClass(), timed.name());    }
}
