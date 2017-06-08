package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * Generates for the metrics corresponding to the various metric annotations.
 */
public interface MetricNamer {

    @Nonnull
    String getNameForCounted(@Nonnull Method method, @Nonnull Counted counted);

    @Nonnull
    String getNameForExceptionMetered(@Nonnull Method method, @Nonnull ExceptionMetered exceptionMetered);

    @Nonnull
    String getNameForGauge(@Nonnull Class<?> klass, @Nonnull Method method, @Nonnull Gauge gauge);

    @Nonnull
    String getNameForMetered(@Nonnull Method method, @Nonnull Metered metered);

    @Nonnull
    String getNameForTimed(@Nonnull Method method, @Nonnull Timed timed);
}
