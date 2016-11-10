package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.palominolabs.metrics.guice.matcher.AnnotationMatcher;
import com.palominolabs.metrics.guice.matcher.AnnotationProvider;
import com.palominolabs.metrics.guice.matcher.MethodAnnotationMatcher;
import java.util.List;

/**
 * A Guice module which instruments methods annotated with the {@link Metered}, {@link Timed}, {@link Gauge}, {@link
 * Counted}, and {@link ExceptionMetered} annotations.
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
    private final AnnotationProvider annotationProvider;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
     * @param matcher The matcher to determine which types to look for metrics in
     * @param metricNamer The metric namer to use when creating names for metrics for annotated methods
     * @param annotationProvider The annotation provider
     */
    private MetricsInstrumentationModule(MetricRegistry metricRegistry, Matcher<? super TypeLiteral<?>> matcher,
        MetricNamer metricNamer, AnnotationProvider annotationProvider) {
        this.metricRegistry = metricRegistry;
        this.matcher = matcher;
        this.metricNamer = metricNamer;
        this.annotationProvider = annotationProvider;
    }

    @Override
    protected void configure() {
        bindListener(matcher, new MeteredListener(metricRegistry, metricNamer, annotationProvider));
        bindListener(matcher, new TimedListener(metricRegistry, metricNamer, annotationProvider));
        bindListener(matcher, new GaugeListener(metricRegistry, metricNamer, annotationProvider));
        bindListener(matcher, new ExceptionMeteredListener(metricRegistry, metricNamer, annotationProvider));
        bindListener(matcher, new CountedListener(metricRegistry, metricNamer, annotationProvider));
    }

    public static class Builder {
        private MetricRegistry metricRegistry;
        private Matcher<? super TypeLiteral<?>> matcher = Matchers.any();
        private MetricNamer metricNamer = new DefaultMetricNamer();
        private List<AnnotationMatcher> annotationMatchers = Lists.newArrayList();

        /**
         * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
         */
        public Builder withMetricRegistry(MetricRegistry metricRegistry) {
            this.metricRegistry = metricRegistry;

            return this;
        }

        /**
         * @param matcher The matcher to determine which types to look for metrics in
         */
        public Builder withMatcher(Matcher<? super TypeLiteral<?>> matcher) {
            this.matcher = matcher;

            return this;
        }

        /**
         * @param metricNamer The metric namer to use when creating names for metrics for annotated methods
         */
        public Builder withMetricNamer(MetricNamer metricNamer) {
            this.metricNamer = metricNamer;

            return this;
        }

        /**
         * @param matcher Annotation matcher to use
         */
        public Builder withAnnotationMatcher(AnnotationMatcher matcher) {
            annotationMatchers.add(matcher);

            return this;
        }

        public MetricsInstrumentationModule build() {
            if (annotationMatchers.isEmpty()) {
                annotationMatchers.add(new MethodAnnotationMatcher());
            }

            return new MetricsInstrumentationModule(
                Preconditions.checkNotNull(metricRegistry),
                Preconditions.checkNotNull(matcher),
                Preconditions.checkNotNull(metricNamer),
                new AnnotationProvider(annotationMatchers)
            );
        }

    }
}
