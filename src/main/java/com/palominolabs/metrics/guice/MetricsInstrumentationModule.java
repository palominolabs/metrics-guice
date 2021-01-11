package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.annotation.Counted;
import io.dropwizard.metrics5.annotation.ExceptionMetered;
import io.dropwizard.metrics5.annotation.Gauge;
import io.dropwizard.metrics5.annotation.Metered;
import io.dropwizard.metrics5.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import javax.annotation.Nonnull;

/**
 * A Guice module which instruments methods annotated with the {@link Metered}, {@link Timed}, {@link Gauge}, {@link
 * Counted}, and {@link ExceptionMetered} annotations.
 *
 * @see Gauge
 * @see Metered
 * @see Timed
 * @see ExceptionMetered
 * @see Counted
 * @see MeteredInterceptor
 * @see TimedInterceptor
 * @see GaugeInjectionListener
 */
public class MetricsInstrumentationModule extends AbstractModule {
    private final MetricRegistry metricRegistry;
    private final Matcher<? super TypeLiteral<?>> matcher;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param metricRegistry     The registry to use when creating meters, etc. for annotated methods.
     * @param matcher            The matcher to determine which types to look for metrics in
     * @param metricNamer        The metric namer to use when creating names for metrics for annotated methods
     * @param annotationResolver The annotation provider
     */
    private MetricsInstrumentationModule(MetricRegistry metricRegistry, Matcher<? super TypeLiteral<?>> matcher,
            MetricNamer metricNamer, AnnotationResolver annotationResolver) {
        this.metricRegistry = metricRegistry;
        this.matcher = matcher;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Override
    protected void configure() {
        bindListener(matcher, new MeteredListener(metricRegistry, metricNamer, annotationResolver));
        bindListener(matcher, new TimedListener(metricRegistry, metricNamer, annotationResolver));
        bindListener(matcher, new GaugeListener(metricRegistry, metricNamer, annotationResolver));
        bindListener(matcher, new ExceptionMeteredListener(metricRegistry, metricNamer, annotationResolver));
        bindListener(matcher, new CountedListener(metricRegistry, metricNamer, annotationResolver));
    }

    public static class Builder {
        private MetricRegistry metricRegistry;
        private Matcher<? super TypeLiteral<?>> matcher = Matchers.any();
        private MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
        private AnnotationResolver annotationResolver = new MethodAnnotationResolver();

        /**
         * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
         * @return this
         */
        @Nonnull
        public Builder withMetricRegistry(@Nonnull MetricRegistry metricRegistry) {
            this.metricRegistry = metricRegistry;

            return this;
        }

        /**
         * @param matcher The matcher to determine which types to look for metrics in
         * @return this
         */
        @Nonnull
        public Builder withMatcher(@Nonnull Matcher<? super TypeLiteral<?>> matcher) {
            this.matcher = matcher;

            return this;
        }

        /**
         * @param metricNamer The metric namer to use when creating names for metrics for annotated methods
         * @return this
         */
        @Nonnull
        public Builder withMetricNamer(@Nonnull MetricNamer metricNamer) {
            this.metricNamer = metricNamer;

            return this;
        }

        /**
         * @param annotationResolver Annotation resolver to use
         * @return this
         */
        @Nonnull
        public Builder withAnnotationMatcher(@Nonnull AnnotationResolver annotationResolver) {
            this.annotationResolver = annotationResolver;

            return this;
        }

        @Nonnull
        public MetricsInstrumentationModule build() {
            return new MetricsInstrumentationModule(
                    Preconditions.checkNotNull(metricRegistry),
                    Preconditions.checkNotNull(matcher),
                    Preconditions.checkNotNull(metricNamer),
                    Preconditions.checkNotNull(annotationResolver)
            );
        }
    }
}
