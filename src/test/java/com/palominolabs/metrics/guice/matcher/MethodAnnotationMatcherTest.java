package com.palominolabs.metrics.guice.matcher;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import org.junit.Test;

/**
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public class MethodAnnotationMatcherTest {
    @Test
    public void testMethodAnnotations() throws Exception {
        AnnotationMatcher matcher = new MethodAnnotationMatcher();
        Class<MethodAnnotatedClass> klass = MethodAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        assertNotNull(matcher.match(Timed.class, publicMethod));
        assertNotNull(matcher.match(Metered.class, protectedMethod));
        assertNotNull(matcher.match(Counted.class, packagePrivateMethod));

        assertNull(matcher.match(Timed.class, packagePrivateMethod));
        assertNull(matcher.match(Counted.class, protectedMethod));
        assertNull(matcher.match(Metered.class, publicMethod));
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
