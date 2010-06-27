package org.accept.util.classpath;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 27, 2010
 * Time: 11:04:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassNameBuilderTest {

    ClassNameBuilder b = new ClassNameBuilder();

    @Test
    public void shouldBuildClassName() throws Exception {
        assertEquals("org.foo.bar.Hello", b.build("c:\\some_project\\src\\org\\foo\\bar", "org/foo/bar", new File("c:\\some_project\\src\\org\\foo\\bar\\Hello.class")));
        assertEquals("org.foo.bar.Hello", b.build("c:\\some_project\\src\\org\\foo", "org/foo", new File("c:\\some_project\\src\\org\\foo\\bar\\Hello.class")));
        assertEquals("org.foo.bar.Hello", b.build("c:\\some_project\\src\\org\\foo", "org\\foo", new File("c:\\some_project\\src\\org\\foo\\bar\\Hello.class")));
        assertEquals("org.foo.bar.Hello", b.build("/some_project/src/org", "org", new File("/some_project/src/org/foo/bar/Hello.class")));
    }
}
