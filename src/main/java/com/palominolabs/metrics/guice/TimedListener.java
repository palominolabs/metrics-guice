package com.palominolabs.metrics.guice;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.palominolabs.metrics.guice.matcher.AnnotationProvider;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationProvider provider;

    public TimedListener(MetricRegistry metricRegistry, MetricNamer metricNamer, final AnnotationProvider provider) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
        this.provider = provider;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Timed annotation = provider.getAnnotation(Timed.class, method);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
