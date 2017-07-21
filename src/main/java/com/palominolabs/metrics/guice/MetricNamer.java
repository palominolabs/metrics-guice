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

    /**
     * For AOP-wrapped method invocations (which is how all metrics other than Gauges have to be handled), there isn't a
     * way to handle annotated methods defined in superclasses since we can't AOP superclass methods. Gauges, however,
     * are invoked without requiring AOP, so gauges from superclasses are available.
     *
     * @param instanceClass the type being instantiated
     * @param method       the annotated method (which may belong to a supertype)
     * @param gauge        the annotation
     * @return a name
     */
    @Nonnull
    String getNameForGauge(@Nonnull Class<?> instanceClass, @Nonnull Method method, @Nonnull Gauge gauge);

    @Nonnull
    String getNameForMetered(@Nonnull Method method, @Nonnull Metered metered);

    @Nonnull
    String getNameForTimed(@Nonnull Method method, @Nonnull Timed timed);
}
