package org.accept.util.commandline;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 26, 2010
 * Time: 8:44:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineConfigTest {

    CommandLineConfig cfg;

    @Test
    public void shouldProvidePort() throws Exception {
        //when
        cfg = new CommandLineConfig("-port", "123");

        //then
        assertEquals(123, cfg.getPort(200));
    }
    
    @Test
    public void shouldProvideDefaultPort() throws Exception {
        //when
        cfg = new CommandLineConfig("blah", "blah");

        //then
        assertEquals(200, cfg.getPort(200));
    }
}
