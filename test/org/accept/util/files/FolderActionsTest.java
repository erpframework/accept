package org.accept.util.files;

import static org.fest.assertions.Assertions.assertThat;
import static org.accept.util.files.FolderActions.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FolderActionsTest {

    FileIO io = new FileIO();
    File fileOne = io.writeToTempFile("1");
    File fileTwo = io.writeToTempFile("2");

    @Test
	public void shouldRecursivelyFindFilesWithContent() throws Exception {
		//given
        final Set data = new HashSet();

        //when
		FolderActions.eachFile(fileOne.getParent(), ".*" + io.TEMP_FILE_SUFFIX).act(new FileContentHandler() {
			public void handle(File file, String content) {
                data.add(content);
			}
		});

		//then
		assertThat(data).contains("1\n", "2\n");
	}

    @Test
	public void shouldRecursivelyFindFiles() throws Exception {
		//given
        final Set data = new HashSet();

        //when
		FolderActions.eachFile(fileOne.getParent(), ".*" + io.TEMP_FILE_SUFFIX).act(new FileHandler() {
			public void handle(File file) {
                data.add(file);
			}
		});

		//then
		assertThat(data).contains(fileOne, fileTwo);
	}
}
