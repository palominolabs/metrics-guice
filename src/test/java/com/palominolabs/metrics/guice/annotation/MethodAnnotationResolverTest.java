package com.palominolabs.metrics.guice.annotation;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import org.junit.Test;

public class MethodAnnotationResolverTest {
    @Test
    public void testMethodAnnotations() throws Exception {
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
