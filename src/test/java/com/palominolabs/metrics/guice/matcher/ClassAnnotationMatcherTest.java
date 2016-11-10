package com.palominolabs.metrics.guice.matcher;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.lang.reflect.Method;
import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;

/**
 * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
 */
public class ClassAnnotationMatcherTest {

    @Test
    public void testTypeLevelAnnotations() throws Exception {
        AnnotationMatcher matcher = new ClassAnnotationMatcher();
        Class<TypeLevelAnnotatedClass> klass = TypeLevelAnnotatedClass.class;
        Method publicMethod = klass.getDeclaredMethod("publicMethod");
        Method protectedMethod = klass.getDeclaredMethod("protectedMethod");
        Method packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod");

        assertNotNull(matcher.match(Timed.class, publicMethod));
        assertNotNull(matcher.match(Metered.class, publicMethod));
        assertNotNull(matcher.match(Counted.class, publicMethod));

        assertNotNull(matcher.match(Timed.class, protectedMethod));
        assertNotNull(matcher.match(Metered.class, protectedMethod));
        assertNotNull(matcher.match(Counted.class, protectedMethod));

        assertNotNull(matcher.match(Timed.class, packagePrivateMethod));
        assertNotNull(matcher.match(Metered.class, packagePrivateMethod));
        assertNotNull(matcher.match(Counted.class, packagePrivateMethod));
    }

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
