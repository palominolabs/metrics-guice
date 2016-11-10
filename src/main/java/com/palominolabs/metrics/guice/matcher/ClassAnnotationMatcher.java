package com.palominolabs.metrics.guice.matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Class level annotation matcher.
 *
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public class ClassAnnotationMatcher implements AnnotationMatcher {
    @Override
    public <T extends Annotation> T match(final Class<T> clazz, final Method method) {
        return method.getDeclaringClass().getAnnotation(clazz);
    }
}
