package org.accept.impl.settings;

import org.accept.impl.settings.AcceptSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 23, 2010
 * Time: 3:21:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcceptSettingsTest {

    AcceptSettings settings = new AcceptSettings("path=./bin\n" +
                "#this path is just an example:\n" +
                "this is a line without '=' \n" +
                "path=lib/test/givwenzen-with-dependencies-1.0.jar\n" +
                "path=lib/test/junit-4.8.2.jar\n" +
                "java=java");

    @Test
    public void shouldParseJavaClassPath() throws Exception {
        //when
        String cp = settings.getJavaClassPath();

        //then
        assertEquals("./bin;lib/test/givwenzen-with-dependencies-1.0.jar;lib/test/junit-4.8.2.jar;", cp);
    }

    @Test
    public void shouldParseJavaCommand() throws Exception {
        assertEquals("java", settings.getJavaCommand());
    }
    
    @Test
    public void shouldBuildCommand() throws Exception {
        assertTrue(settings.buildCommand().startsWith("java -cp ./bin;"));
    }

    @Test
    public void shouldAllowComments() throws Exception {
        assertTrue(settings.buildCommand().startsWith("java -cp ./bin;"));
    }
}
