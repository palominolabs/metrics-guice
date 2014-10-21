package com.palominolabs.metrics.guice;

import com.codahale.metrics.Counter;
import com.codahale.metrics.annotation.Counted;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

class CountedInterceptor implements MethodInterceptor {

    private final Counter counter;
    private final Counted annotation;

    CountedInterceptor(Counter counter, Counted annotation) {
        this.counter = counter;
        this.annotation = annotation;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        counter.inc();
        try {
            return invocation.proceed();
        } finally {
            if (annotation.monotonic()) {
                counter.dec();
            }
        }
    }
}
