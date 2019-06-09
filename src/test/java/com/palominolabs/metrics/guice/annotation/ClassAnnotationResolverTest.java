package com.palominolabs.metrics.guice.annotation;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;

public class ClassAnnotationResolverTest {

    @Test
    public void testTypeLevelAnnotations() throws Exception {
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

    @Timed
    @Metered
    @Counted
    @SuppressWarnings("unused")
    private static class TypeLevelAnnotatedClass {
        public void publicMethod() {
        }

        protected void protectedMethod() {
        }

        void packagePrivateMethod() {
        }
    }
}
