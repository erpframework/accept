package org.accept.impl.settings;

import org.accept.impl.settings.AcceptSettings;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
                "#commented out:\n" +
                "this is a line without '=' \n" +
                "path=lib/test/givwenzen-with-dependencies-1.0.jar\n" +
                "path=lib/test/junit-4.8.2.jar\n" +
                "java=java -Xmx256m", ";");

    @Test
    public void shouldParseJavaClassPath() throws Exception {
        //when
        String cp = settings.getJavaClassPath();

        //then
        assertThat(cp).endsWith("./bin;lib/test/givwenzen-with-dependencies-1.0.jar;lib/test/junit-4.8.2.jar");
    }

    @Test
    public void shouldParseJavaCommand() throws Exception {
        assertEquals("java -Xmx256m", settings.getJavaCommand());
    }
    
    @Test
    public void shouldBuildCommand() throws Exception {
        assertTrue(settings.buildCommand().startsWith("java -Xmx256m -cp "));
    }

    @Test
    public void shouldAllowComments() throws Exception {
        assertFalse(settings.buildCommand().contains("commented out"));
    }

    @Test
    public void shouldBuildClasspathCorrectly() throws Exception {
        String javaCp = System.getProperty("java.class.path");
        assertThat(new AcceptSettings("path=foo", ":").getJavaClassPath()).isEqualTo(javaCp + ":foo");
        assertThat(new AcceptSettings("path=foo\npath=bar", ":").getJavaClassPath()).isEqualTo(javaCp + ":foo:bar");
        assertThat(new AcceptSettings("").getJavaClassPath()).isEqualTo(javaCp);
    }
}
