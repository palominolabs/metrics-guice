package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MatcherTest {
    private InstrumentedWithTimed timedInstance;
    private InstrumentedWithMetered meteredInstance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Matcher<? super TypeLiteral<?>> matcher = new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(final TypeLiteral<?> typeLiteral) {
                return InstrumentedWithMetered.class.isAssignableFrom(typeLiteral.getRawType());
            }
        };
        final Injector injector = Guice.createInjector(new MetricsInstrumentationModule(registry, matcher));
        this.timedInstance = injector.getInstance(InstrumentedWithTimed.class);
        this.meteredInstance = injector.getInstance(InstrumentedWithMetered.class);
    }

    @Test
    public void aTimedAnnotatedMethod() throws Exception {

        timedInstance.doAThing();

        final Timer metric = registry.getTimers().get(name(InstrumentedWithTimed.class,
            "things"));

        assertThat("Guice did not create a metric for timed",
            metric,
            is(nullValue()));
    }

    @Test
    public void aMeteredAnnotatedMethod() throws Exception {

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
