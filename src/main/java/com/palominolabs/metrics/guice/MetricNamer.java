package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public interface MetricNamer {

    @Nonnull
    String nameForCounted(@Nonnull Method method, @Nonnull Counted counted);

    @Nonnull
    String nameForExceptionMetered(@Nonnull Method method, @Nonnull ExceptionMetered counted);

    @Nonnull
    String nameForGauge(@Nonnull Method method, @Nonnull Gauge counted);

    @Nonnull
    String nameForMetered(@Nonnull Method method, @Nonnull Metered counted);

    @Nonnull
    String nameForTimed(@Nonnull Method method, @Nonnull Timed counted);
}
