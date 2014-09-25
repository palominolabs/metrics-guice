package com.palominolabs.metrics.guice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;

class CountedInterceptor implements MethodInterceptor {
    static MethodInterceptor forMethod(MetricRegistry metricRegistry, Class<?> klass, Method method) {
        final Counted annotation = method.getAnnotation(Counted.class);
        if (annotation != null) {
            final Counter counter = metricRegistry.counter(determineName(annotation, klass, method));
            return new CountedInterceptor(counter, annotation);
        }
        return null;
    }

    private static String determineName(Counted annotation, Class<?> klass, Method method) {
        if (annotation.absolute()) {
            return annotation.name();
        }

        if (annotation.name().isEmpty()) {
        	if(annotation.monotonic())
        		return MetricRegistry.name(klass, method.getName(), Counted.class.getSimpleName() + "Monotonic");
        	else
        		return MetricRegistry.name(klass, method.getName(), Counted.class.getSimpleName());

        }

        return MetricRegistry.name(klass, annotation.name());
    }

    private final Counter counter;
	private final Counted annotation;

    private CountedInterceptor(Counter counter, Counted annotation) {
        this.counter = counter;
        this.annotation = annotation;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        counter.inc();
        try {
            return invocation.proceed();
        } finally {
        	if(annotation.monotonic())
        		counter.dec();
        }
    }
}
