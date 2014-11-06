package com.palominolabs.metrics.guice;

import com.codahale.metrics.Timer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * A method interceptor which times the execution of the annotated method.
 */
class TimedInterceptor implements MethodInterceptor {

    private final Timer timer;

    TimedInterceptor(Timer timer) {
        this.timer = timer;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Since these timers are always created via the default ctor (via MetricRegister#timer), they always use
        // nanoTime, so we can save an allocation here by not using Context.
        long start = System.nanoTime();
        try {
            return invocation.proceed();
        } finally {
            timer.update(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }
}
