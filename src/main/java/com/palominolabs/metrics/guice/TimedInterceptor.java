package com.palominolabs.metrics.guice;

import com.codahale.metrics.Timer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
        final Timer.Context ctx = timer.time();
        try {
            return invocation.proceed();
        } finally {
            ctx.stop();
        }
    }
}
