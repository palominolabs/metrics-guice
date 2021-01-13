package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.Meter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A method interceptor which creates a meter for the declaring class with the given name (or the method's name, if none
 * was provided), and which measures the rate at which the annotated method is invoked.
 */
class MeteredInterceptor implements MethodInterceptor {

    private final Meter meter;

    MeteredInterceptor(Meter meter) {
        this.meter = meter;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        meter.mark();
        return invocation.proceed();
    }
}
