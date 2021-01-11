package com.palominolabs.metrics.guice.annotation;

import io.dropwizard.metrics5.annotation.Counted;
import io.dropwizard.metrics5.annotation.Metered;
import io.dropwizard.metrics5.annotation.Timed;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MethodAnnotationResolverTest {
    @Test
    void testMethodAnnotations() throws Exception {
        AnnotationResolver matcher = new MethodAnnotationResolver();
        Class<MethodAnnotatedClass> klass = MethodAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        assertNotNull(matcher.findAnnotation(Timed.class, publicMethod));
        assertNotNull(matcher.findAnnotation(Metered.class, protectedMethod));
        assertNotNull(matcher.findAnnotation(Counted.class, packagePrivateMethod));

        assertNull(matcher.findAnnotation(Timed.class, packagePrivateMethod));
        assertNull(matcher.findAnnotation(Counted.class, protectedMethod));
        assertNull(matcher.findAnnotation(Metered.class, publicMethod));
    }

    @SuppressWarnings("WeakerAccess")
    private static class MethodAnnotatedClass {
        @Timed
        public void publicMethod() {
        }

        @Metered
        protected void protectedMethod() {
        }

        @Counted
        void packagePrivateMethod() {
        }
    }
}
