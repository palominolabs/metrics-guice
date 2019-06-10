package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.palominolabs.metrics.guice.DeclaringClassMetricNamer.COUNTER_SUFFIX;
import static com.palominolabs.metrics.guice.DeclaringClassMetricNamer.COUNTER_SUFFIX_MONOTONIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

class CountedTest {
    private InstrumentedWithCounter instance;
    private MetricRegistry registry;

    @BeforeEach
    void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(MetricsInstrumentationModule.builder().withMetricRegistry(registry).build());
        this.instance = injector.getInstance(InstrumentedWithCounter.class);
    }

    @Test
    void aCounterAnnotatedMethod() {
        instance.doAThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class, "things"));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
            metric.getCount(),
            is((long) 1));
    }

    @Test
    void aCounterAnnotatedMethodWithDefaultName() {
        instance.doAnotherThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class,
            "doAnotherThing", COUNTER_SUFFIX_MONOTONIC));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
            metric.getCount(),
            is((long) 1));
    }

    @Test
    void aCounterAnnotatedMethodWithDefaultNameAndMonotonicFalse() {
        instance.doYetAnotherThing();

        final Counter metric = registry.getCounters().get(name(InstrumentedWithCounter.class,
            "doYetAnotherThing", COUNTER_SUFFIX));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        // if things are working well then this should still be zero...
        assertThat("Guice creates a counter with the given value",
            metric.getCount(),
            is((long) 0));
    }

    @Test
    void aCounterAnnotatedMethodWithAbsoluteName() {
        instance.doAThingWithAbsoluteName();

        final Counter metric = registry.getCounters().get(name("absoluteName"));

        assertThat("Guice creates a metric",
            metric,
            is(notNullValue()));

        assertThat("Guice creates a counter with the given value",
            metric.getCount(),
            is((long) 1));
    }

    /**
     * Test to document the current (correct but regrettable) behavior.
     *
     * Crawling the injected class's supertype hierarchy doesn't really accomplish anything because AOPing supertype
     * methods doesn't work.
     *
     * In certain cases (e.g. a public type that inherits a public method from a non-public supertype), a synthetic
     * bridge method is generated in the subtype that invokes the supertype method, and this does copy the annotations
     * of the supertype method. However, we can't allow intercepting synthetic bridge methods in general: when a subtype
     * overrides a generic supertype's method with a more specifically typed method that would not override the
     * type-erased supertype method, a bridge method matching the supertype's erased signature is generated, but with
     * the subtype's method's annotation. It's not OK to intercept that synthetic method because that would lead to
     * double-counting, etc, since we also would intercept the regular non-synthetic method.
     *
     * Thus, we cannot intercept synthetic methods to maintain correctness, so we also lose out on one small way that we
     * could intercept annotated methods in superclasses.
     */
    @Test
    void aCounterForSuperclassMethod() {
        instance.counterParent();

        final Counter metric = registry.getCounters().get(name("counterParent"));

        // won't be created because we don't bother looking for supertype methods
        assertNull(metric);
    }
}
