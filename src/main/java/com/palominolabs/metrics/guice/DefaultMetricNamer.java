package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
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

    @Nonnull
    @Override
    public String nameForCounted(@Nonnull Method method, @Nonnull Counted counted) {
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
    public String nameForExceptionMetered(@Nonnull Method method, @Nonnull ExceptionMetered counted) {
        return null;  // TODO
    }

    @Nonnull
    @Override
    public String nameForGauge(@Nonnull Method method, @Nonnull Gauge counted) {
        return null;  // TODO
    }

    @Nonnull
    @Override
    public String nameForMetered(@Nonnull Method method, @Nonnull Metered counted) {
        return null;  // TODO
    }

    @Nonnull
    @Override
    public String nameForTimed(@Nonnull Method method, @Nonnull Timed counted) {
        return null;  // TODO
    }
}
