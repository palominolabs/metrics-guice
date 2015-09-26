package com.palominolabs.metrics.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * A TypeListener which delegates to {@link DeclaredMethodsTypeListener#getInterceptor(Method)} for each method in the
 * class's declared methods.
 */
abstract class DeclaredMethodsTypeListener implements TypeListener {

    @Override
    public <T> void hear(TypeLiteral<T> literal, TypeEncounter<T> encounter) {
        Class<? super T> klass = literal.getRawType();

        for (Method method : klass.getDeclaredMethods()) {
            if (method.isSynthetic()) {
                continue;
            }

            final MethodInterceptor interceptor = getInterceptor(method);
            if (interceptor != null) {
                encounter.bindInterceptor(Matchers.only(method), interceptor);
            }
        }
    }

    /**
     * Called for every method on every class in the type hierarchy of the visited type
     *
     * @param method method to get interceptor for
     * @return null if no interceptor should be applied, else an interceptor
     */
    @Nullable
    protected abstract MethodInterceptor getInterceptor(Method method);
}
