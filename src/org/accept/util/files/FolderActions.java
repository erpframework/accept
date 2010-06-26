package org.accept.util.files;

import java.io.File;
import java.io.FileFilter;


public class FolderActions {
	
	final FileFilter filenameFilter;
	final File rootDir;

	public FolderActions(final String rootDir, final String fileFilterRegex) {
		this(new File(rootDir), new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return file.getName().matches(fileFilterRegex);
			}
		});
	}
	
	public FolderActions(final File rootDir, final FileFilter filenameFilter) {
		this.rootDir = rootDir;
		assert rootDir.exists();
		this.filenameFilter = filenameFilter;
		
	}

	public void act(FileContentHandler fileContentHandler) {
		File[] files = rootDir.listFiles(filenameFilter);
		for(File f: files) {
			if (f.isDirectory()) {
				new FolderActions(f, filenameFilter).act(fileContentHandler);
			} else {
				fileContentHandler.handle(f, new FileIO().read(f));
			}
		}
	}

    public void act(FileHandler fileHandler) {
		File[] files = rootDir.listFiles(filenameFilter);
		for(File f: files) {
			if (f.isDirectory()) {
				new FolderActions(f, filenameFilter).act(fileHandler);
			} else {
				fileHandler.handle(f);
			}
		}
	}
	
	public static FolderActions eachFile(String rootDir, String fileFilterRegex) {
		return new FolderActions(rootDir, fileFilterRegex);
	}

    public static interface FileHandler {

        public void handle(File file);

    }

    public static interface FileContentHandler {

        public void handle(File file, String content);

    }
}
