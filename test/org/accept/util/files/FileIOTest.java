package org.accept.util.files;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 26, 2010
 * Time: 7:39:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileIOTest {

    FileIO io = new FileIO();

    @Test
    public void shouldCreateTempFile() throws Exception {
        //when
        File f = io.createTempFile();

        //then
        assertTrue(f.exists());
    }

    @Test
    public void shouldReadEmptyFile() throws Exception {
        //given
        File f = io.createTempFile();

        //when
        String content = io.read(f);

        //then
        assertEquals("", content);
    }

    @Test
    public void shouldCreateAndReadFile() throws Exception {
        //given
        File f = io.createTempFile();
        io.write(f, "hello \n world");

        //when
        String content = io.read(f);

        //then
        assertEquals("hello \n world\n", content);
    }

    @Test
    public void shouldCreateTempFileWithContent() throws Exception {
        //given
        File f = io.writeToTempFile("yo buddy\n");

        //when
        String content = io.read(f);

        //then
        assertEquals("yo buddy\n", content);
    }
}
