package com.palominolabs.metrics.guice;

import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.Timer;
import io.dropwizard.metrics5.annotation.Timed;
import com.palominolabs.metrics.guice.annotation.AnnotationResolver;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A listener which adds method interceptors to timed methods.
 */
public class TimedListener extends DeclaredMethodsTypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;
    private final AnnotationResolver annotationResolver;

    public TimedListener(MetricRegistry metricRegistry, MetricNamer metricNamer,
            final AnnotationResolver annotationResolver) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
        this.annotationResolver = annotationResolver;
    }

    @Nullable
    @Override
    protected MethodInterceptor getInterceptor(Method method) {
        final Timed annotation = annotationResolver.findAnnotation(Timed.class, method);
        if (annotation != null) {
            final Timer timer = metricRegistry.timer(metricNamer.getNameForTimed(method, annotation));
            return new TimedInterceptor(timer);
        }
        return null;
    }
}
