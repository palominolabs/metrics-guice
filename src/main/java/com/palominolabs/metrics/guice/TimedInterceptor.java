package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * A method interceptor which creates a timer for the declaring class with the given name (or the
 * method's name, if none was provided), and which times the execution of the annotated method.
 */
class TimedInterceptor implements MethodInterceptor {

    static final String TIMED_SUFFIX = "timer";

    static MethodInterceptor forMethod(MetricRegistry metricRegistry, Class<?> klass, Method method) {
        final Timed annotation = method.getAnnotation(Timed.class);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(determineName(annotation, klass, method));
            return new TimedInterceptor(timer);
        }
        return null;
    }

    private static String determineName(Timed annotation, Class<?> klass, Method method) {
        if (annotation.absolute()) {
            return annotation.name();
        }
        
        if (annotation.name().isEmpty()) {
            return MetricRegistry.name(klass, method.getName(), TIMED_SUFFIX);
        }

        return MetricRegistry.name(klass, annotation.name());
    }

    private final Timer timer;

    private TimedInterceptor(Timer timer) {
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
