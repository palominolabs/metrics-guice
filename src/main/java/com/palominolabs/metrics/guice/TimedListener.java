package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public TimedListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Timed annotation = method.getAnnotation(Timed.class);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
