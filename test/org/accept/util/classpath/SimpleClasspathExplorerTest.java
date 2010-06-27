package org.accept.util.classpath;

import org.givwenzen.annotations.DomainSteps;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.accept.util.classpath.SimpleClasspathExplorer.*;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 23, 2010
 * Time: 8:07:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleClasspathExplorerTest {

    SimpleClasspathExplorer e = new SimpleClasspathExplorer();

    @Test
    public void shouldSearchClasspathForAnnotatedClasses() throws Exception {
        //given
        String pkg = "org/accept/util/classpath";

        //when
        final Set stepClasses = new HashSet();
        e.findClasses(pkg, new ClassHandler() {
            public void handle(Class cls) {
                if (cls.isAnnotationPresent(DomainSteps.class)) {
                    stepClasses.add(cls);
                }
            }
        });

        //then
        assertThat(stepClasses)
                .contains(ClassToFindByAnnotation.class)
                .excludes(ClassToFindInheritence.class);
    }

    @Test
    public void shouldSearchClasspathSubclasses() throws Exception {
        //given
        String pkg = "org/accept/util/classpath";

        //when
        final Set stepClasses = new HashSet();
        e.findClasses(pkg, new ClassHandler() {
            public void handle(Class cls) {
                if (Serializable.class.isAssignableFrom(cls)) {
                    stepClasses.add(cls);
                }
            }
        });

        //then
        assertThat(stepClasses)
                .contains(ClassToFindInheritence.class)
                .excludes(ClassToFindByAnnotation.class);
    }

    @Test
    public void shouldSearchRecursively() throws Exception {
        //given
        String pkg = "org/accept";

        //when
        final Set stepClasses = new HashSet();
        e.findClasses(pkg, new ClassHandler() {
            public void handle(Class cls) {
                if (cls.isAnnotationPresent(DomainSteps.class)) {
                    stepClasses.add(cls);
                }
            }
        });

        //then
        assertThat(stepClasses)
                .contains(ClassToFindByAnnotation.class)
                .excludes(ClassToFindInheritence.class);
    }

    @Test
    public void shouldSaySomethingDecentIfPackageNotFound() throws Exception {
        //given
        String pkg = "i/dont/exist";

        //when
        try {
            e.findClasses(pkg, null);
            //then
            fail();
        } catch (SimpleClasspathExplorer.UnableToFindPackageException e) {}
    }
}