package com.palominolabs.metrics.guice.annotation;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassAnnotationResolverTest {

    @Test
    void testTypeLevelAnnotations() throws Exception {
        AnnotationResolver matcher = new ClassAnnotationResolver();
        Class<TypeLevelAnnotatedClass> klass = TypeLevelAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        assertNotNull(matcher.findAnnotation(Timed.class, publicMethod));
        assertNotNull(matcher.findAnnotation(Metered.class, publicMethod));
        assertNotNull(matcher.findAnnotation(Counted.class, publicMethod));

        assertNotNull(matcher.findAnnotation(Timed.class, protectedMethod));
        assertNotNull(matcher.findAnnotation(Metered.class, protectedMethod));
        assertNotNull(matcher.findAnnotation(Counted.class, protectedMethod));

        assertNotNull(matcher.findAnnotation(Timed.class, packagePrivateMethod));
        assertNotNull(matcher.findAnnotation(Metered.class, packagePrivateMethod));
        assertNotNull(matcher.findAnnotation(Counted.class, packagePrivateMethod));
    }

    @SuppressWarnings("WeakerAccess")
    @Timed
    @Metered
    @Counted
    private static class TypeLevelAnnotatedClass {
        public void publicMethod() {
        }

        protected void protectedMethod() {
        }

        void packagePrivateMethod() {
        }
    }
}
