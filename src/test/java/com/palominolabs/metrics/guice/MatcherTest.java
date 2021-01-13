package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Meter;
import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.Timer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.dropwizard.metrics5.MetricRegistry.name;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class MatcherTest {
    private InstrumentedWithTimed timedInstance;
    private InstrumentedWithMetered meteredInstance;
    private MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Matcher<? super TypeLiteral<?>> matcher = new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(final TypeLiteral<?> typeLiteral) {
                return InstrumentedWithMetered.class.isAssignableFrom(typeLiteral.getRawType());
            }
        };
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).withMatcher(matcher).build());
        this.timedInstance = injector.getInstance(InstrumentedWithTimed.class);
        this.meteredInstance = injector.getInstance(InstrumentedWithMetered.class);
    }

    @Test
    void aTimedAnnotatedMethod() throws Exception {

        timedInstance.doAThing();

        final Timer metric = registry.getTimers().get(name(InstrumentedWithTimed.class,
            "things"));

        assertThat("Guice did not create a metric for timed",
            metric,
            is(nullValue()));
    }

    @Test
    void aMeteredAnnotatedMethod() {

        meteredInstance.doAThing();

        final Meter metric = registry.getMeters().get(name(InstrumentedWithMetered.class, "things"));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        assertThat("Guice creates a meter which gets marked",
            metric.getCount(),
            is(1L));
    }
}
