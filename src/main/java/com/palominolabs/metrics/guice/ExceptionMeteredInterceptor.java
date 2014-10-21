package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A method interceptor which measures the rate at which the annotated method throws exceptions of a given type.
 */
class ExceptionMeteredInterceptor implements MethodInterceptor {

    private final Meter meter;
    private final Class<? extends Throwable> klass;

    ExceptionMeteredInterceptor(Meter meter, Class<? extends Throwable> klass) {
        this.meter = meter;
        this.klass = klass;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            if (klass.isAssignableFrom(t.getClass())) {
                meter.mark();
            }
            throw t;
        }
    }
}
