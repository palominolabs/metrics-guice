package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import com.palominolabs.metrics.guice.annotation.MethodAnnotationResolver;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;

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
    @Nullable
    private final Provider<MetricRegistry> metricRegistryProvider;
    private final Matcher<? super TypeLiteral<?>> matcher;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param metricRegistryProvider The registry provider to use when creating meters, etc. for annotated methods, or null if the module should request a provider from the injector instead.
     * @param matcher                The matcher to determine which types to look for metrics in
     * @param metricNamer            The metric namer to use when creating names for metrics for annotated methods
     * @param annotationResolver     The annotation provider
     */
    private MetricsInstrumentationModule(@Nullable Provider<MetricRegistry> metricRegistryProvider,
            Matcher<? super TypeLiteral<?>> matcher, MetricNamer metricNamer, AnnotationResolver annotationResolver) {
        this.metricRegistryProvider = metricRegistryProvider;
        this.matcher = matcher;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Override
    protected void configure() {
        Provider<MetricRegistry> provider;
        if (this.metricRegistryProvider == null) {
            provider = new MetricRegistryHolder();
            requestInjection(provider);
        } else {
            provider = this.metricRegistryProvider;
        }

        bindListener(matcher, new MeteredListener(provider, metricNamer, annotationResolver));
        bindListener(matcher, new TimedListener(provider, metricNamer, annotationResolver));
        bindListener(matcher, new GaugeListener(provider, metricNamer, annotationResolver));
        bindListener(matcher, new ExceptionMeteredListener(provider, metricNamer, annotationResolver));
        bindListener(matcher, new CountedListener(provider, metricNamer, annotationResolver));
    }

    public static class Builder {
        @Nullable
        private Provider<MetricRegistry> metricRegistryProvider = null;
        private Matcher<? super TypeLiteral<?>> matcher = Matchers.any();
        private MetricNamer metricNamer = new GaugeInstanceClassMetricNamer();
        private AnnotationResolver annotationResolver = new MethodAnnotationResolver();

        /**
         * @param metricRegistry The registry to use when creating meters, etc. for annotated methods.
         * @return this
         */
        @Nonnull
        public Builder withMetricRegistry(@Nonnull MetricRegistry metricRegistry) {
            this.metricRegistryProvider = () -> metricRegistry;

            return this;
        }

        /**
         * @param metricRegistryProvider The registry to use when creating meters, etc. for annotated methods.
         * @return this
         */
        @Nonnull
        public Builder withMetricRegistryProvider(@Nonnull Provider<MetricRegistry> metricRegistryProvider) {
            this.metricRegistryProvider = metricRegistryProvider;

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
                    metricRegistryProvider,
                    Preconditions.checkNotNull(matcher),
                    Preconditions.checkNotNull(metricNamer),
                    Preconditions.checkNotNull(annotationResolver));
        }
    }

    private static class MetricRegistryHolder implements Provider<MetricRegistry> {

        @Inject
        private volatile MetricRegistry registry;

        @Override
        public MetricRegistry get() {
            return registry;
        }
    }
}
