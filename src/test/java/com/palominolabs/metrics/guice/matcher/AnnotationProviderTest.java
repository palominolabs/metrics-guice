package com.palominolabs.metrics.guice.matcher;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AnnotationProviderTest {

    @Test
    public void testMixedAnnotations() throws Exception {
        AnnotationProvider annotationProvider = new AnnotationProvider(
                Lists.newArrayList(
                        new MethodAnnotationMatcher(),
                        new ClassAnnotationMatcher()
                )
        );

        Class<MixedAnnotatedClass> klass = MixedAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        Timed classTimed = annotationProvider.getAnnotation(Timed.class, publicMethod);
        assertNotNull(classTimed);
        assertFalse(classTimed.absolute());
        assertNull(annotationProvider.getAnnotation(Metered.class, publicMethod));
        assertNull(annotationProvider.getAnnotation(Counted.class, publicMethod));

        assertNotNull(annotationProvider.getAnnotation(Timed.class, protectedMethod));
        assertNotNull(annotationProvider.getAnnotation(Metered.class, protectedMethod));
        assertNull(annotationProvider.getAnnotation(Counted.class, protectedMethod));

        Timed methodTimed = annotationProvider.getAnnotation(Timed.class, packagePrivateMethod);
        assertNotNull(methodTimed);
        assertTrue(methodTimed.absolute());
        assertNull(annotationProvider.getAnnotation(Metered.class, packagePrivateMethod));
        assertNull(annotationProvider.getAnnotation(Counted.class, packagePrivateMethod));
    }

    @Timed
    private static class MixedAnnotatedClass {
        public void publicMethod() {
        }

        @Metered
        protected void protectedMethod() {
        }

        @Timed(absolute = true)
        void packagePrivateMethod() {
        }
    }
}
