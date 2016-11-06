package com.palominolabs.metrics.guice.matcher;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import org.junit.Test;

/**
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public class AnnotationProviderTest {

    @Test
    public void testMixedAnnotations() throws Exception {
        AnnotationProvider annotationProvider = new AnnotationProvider(
            Lists.newArrayList(
                new ClassAnnotationMatcher(),
                new MethodAnnotationMatcher()
            )
        );

        Class<MixedAnnotatedClass> klass = MixedAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        assertNotNull(annotationProvider.getAnnotation(Timed.class, publicMethod));
        assertNull(annotationProvider.getAnnotation(Metered.class, publicMethod));
        assertNull(annotationProvider.getAnnotation(Counted.class, publicMethod));

        assertNotNull(annotationProvider.getAnnotation(Timed.class, protectedMethod));
        assertNotNull(annotationProvider.getAnnotation(Metered.class, protectedMethod));
        assertNull(annotationProvider.getAnnotation(Counted.class, protectedMethod));

        assertNotNull(annotationProvider.getAnnotation(Timed.class, packagePrivateMethod));
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

        void packagePrivateMethod() {
        }
    }
}
