package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static com.codahale.metrics.annotation.ExceptionMetered.DEFAULT_NAME_SUFFIX;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ExceptionMeteredTest {
    private InstrumentedWithExceptionMetered instance;
    private MetricRegistry registry;

    @Before
    public void setup() {
        this.registry = new MetricRegistry();
        final Injector injector = Guice.createInjector(new InstrumentationModule() {
            @Override
            protected MetricRegistry createMetricRegistry() {
                return registry;
            }
        });
        this.instance = injector.getInstance(InstrumentedWithExceptionMetered.class);
    }

    @Test
    public void anExceptionMeteredAnnotatedMethodWithPublicScope() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class, "exceptionCounter"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        try {
            instance.explodeWithPublicScope(true);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Swallow the expected exception
        }

        assertThat("Metric is marked",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithNoMetricName() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "explodeForUnnamedMetric", DEFAULT_NAME_SUFFIX));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        try {
            instance.explodeForUnnamedMetric();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Swallow the expected exception
        }

        assertThat("Metric is marked",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithName() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class, "n"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        try {
            instance.explodeForMetricWithName();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Swallow the expected exception
        }

        assertThat("Metric is marked",
                   metric.getCount(),
                   is(1L));
    }


    @Test
    public void anExceptionMeteredAnnotatedMethod_WithAbsoluteName() throws Exception {

        final Meter metric = registry.getMeters().get(name("absoluteName"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
            metric.getCount(),
            is(0L));

        try {
            instance.explodeForMetricWithAbsoluteName();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Swallow the expected exception
        }

        assertThat("Metric is marked",
            metric.getCount(),
            is(1L));
    }


    @Test
    public void anExceptionMeteredAnnotatedMethod_WithPublicScopeButNoExceptionThrown() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "exceptionCounter"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        instance.explodeWithPublicScope(false);

        assertThat("Metric should remain at zero if no exception is thrown",
                   metric.getCount(),
                   is(0L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithDefaultScope() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "explodeWithDefaultScope", DEFAULT_NAME_SUFFIX));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        try {
            instance.explodeWithDefaultScope();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }

        assertThat("Metric is marked",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithProtectedScope() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "explodeWithProtectedScope", DEFAULT_NAME_SUFFIX));

        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));

        try {
            instance.explodeWithProtectedScope();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }

        assertThat("Metric is marked",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithPublicScope_AndSpecificTypeOfException() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "failures"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));
        try {
            instance.errorProneMethod(new MyException());
            fail("Expected an exception to be thrown");
        } catch (MyException e) {
        }

        assertThat("Metric should be marked when the specified exception type is thrown",
                   metric.getCount(),
                   is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithPublicScope_AndSubclassesOfSpecifiedException() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "failures"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));
        try {
            instance.errorProneMethod(new MySpecialisedException());
            fail("Expected an exception to be thrown");
        } catch (MyException e) {
        }

        assertThat(
                "Metric should be marked when a subclass of the specified exception type is thrown",
                metric.getCount(),
                is(1L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithPublicScope_ButDifferentTypeOfException() throws Exception {

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "failures"));
        assertMetricIsSetup(metric);

        assertThat("Metric intialises to zero",
                   metric.getCount(),
                   is(0L));
        try {
            instance.errorProneMethod(new MyOtherException());
            fail("Expected an exception to be thrown");
        } catch (MyOtherException e) {
        }

        assertThat("Metric should not be marked if the exception is a different type",
                   metric.getCount(),
                   is(0L));
    }

    @Test
    public void anExceptionMeteredAnnotatedMethod_WithExtraOptions() throws Exception {

        try {
            instance.causeAnOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        final Meter metric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                          "things"));

        assertMetricIsSetup(metric);

        assertThat("Guice creates a meter which gets marked",
                   metric.getCount(),
                   is(1L));
    }


    @Test
    public void aMethodAnnotatedWithBothATimerAndAnExceptionCounter() throws Exception {

        final Timer timedMetric = registry.getTimers().get(name(InstrumentedWithExceptionMetered.class,
                                                               "timedAndException"));

        final Meter errorMetric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                               "timedAndException", DEFAULT_NAME_SUFFIX));

        assertThat("Guice creates a metric",
                   timedMetric,
                   is(notNullValue()));

        assertThat("Guice creates a timer",
                   timedMetric,
                   is(instanceOf(Timer.class)));

        assertThat("Guice creates a metric",
                   errorMetric,
                   is(notNullValue()));

        assertThat("Guice creates a meter",
                   errorMetric,
                   is(instanceOf(Meter.class)));

        // Counts should start at zero        
        assertThat("Timer Metric should be zero when initialised",
                   timedMetric.getCount(),
                   is(0L));


        assertThat("Error Metric should be zero when initialised",
                   errorMetric.getCount(),
                   is(0L));

        // Invoke, but don't throw an exception
        instance.timedAndException(null);

        assertThat("Expected the meter metric to be marked on invocation",
                   timedMetric.getCount(),
                   is(1L));

        assertThat("Expected the exception metric to be zero since no exceptions thrown",
                   errorMetric.getCount(),
                   is(0L));

        // Invoke and throw an exception
        try {
            instance.timedAndException(new RuntimeException());
            fail("Should have thrown an exception");
        } catch (Exception e) {}

        assertThat("Expected a count of 2, one for each invocation",
                   timedMetric.getCount(),
                   is(2L));

        assertThat("Expected exception count to be 1 as one (of two) invocations threw an exception",
                   errorMetric.getCount(),
                   is(1L));

    }

    @Test
    public void aMethodAnnotatedWithBothAMeteredAndAnExceptionCounter() throws Exception {

        final Metric meteredMetric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                                 "meteredAndException"));

        final Metric errorMetric = registry.getMeters().get(name(InstrumentedWithExceptionMetered.class,
                                                               "meteredAndException", DEFAULT_NAME_SUFFIX));

        assertThat("Guice creates a metric",
                   meteredMetric,
                   is(notNullValue()));

        assertThat("Guice creates a meter",
                   meteredMetric,
                   is(instanceOf(Meter.class)));

        assertThat("Guice creates a metric",
                   errorMetric,
                   is(notNullValue()));

        assertThat("Guice creates an exception meter",
                   errorMetric,
                   is(instanceOf(Meter.class)));

        // Counts should start at zero        
        assertThat("Meter Metric should be zero when initialised",
                   ((Meter) meteredMetric).getCount(),
                   is(0L));


        assertThat("Error Metric should be zero when initialised",
                   ((Meter) errorMetric).getCount(),
                   is(0L));

        // Invoke, but don't throw an exception
        instance.meteredAndException(null);

        assertThat("Expected the meter metric to be marked on invocation",
                   ((Meter) meteredMetric).getCount(),
                   is(1L));

        assertThat("Expected the exception metric to be zero since no exceptions thrown",
                   ((Meter) errorMetric).getCount(),
                   is(0L));

        // Invoke and throw an exception
        try {
            instance.meteredAndException(new RuntimeException());
            fail("Should have thrown an exception");
        } catch (Exception e) {}

        assertThat("Expected a count of 2, one for each invocation",
                   ((Meter) meteredMetric).getCount(),
                   is(2L));

        assertThat("Expected exception count to be 1 as one (of two) invocations threw an exception",
                   ((Meter) errorMetric).getCount(),
                   is(1L));

    }

    private void assertMetricIsSetup(final Meter metric) {
        assertThat("Guice creates a metric",
                   metric,
                   is(notNullValue()));
    }

    @SuppressWarnings("serial")
    private static class MyOtherException extends RuntimeException {
    }

    @SuppressWarnings("serial")
    private static class MySpecialisedException extends MyException {
    }
}
