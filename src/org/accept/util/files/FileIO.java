package org.accept.util.files;

import java.io.*;

public class FileIO {
    public final String TEMP_FILE_SUFFIX = "accept-tool";

    public String read(File file) {
		StringBuilder out = new StringBuilder();
        BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(file));
			String line = r.readLine();
			while (line != null) {
				out.append(line).append("\n");
				line = r.readLine();
			}
		} catch (Exception e) {
			throw new RuntimeException("Problems reading contents from file: " + file.getName(), e);
 		} finally {
            close(r);
        }
		return out.toString();
	}

    public void write(File file, String content) {
		PrintWriter p = null;
		try {
			p = new PrintWriter(new FileWriter(file));
			p.print(content);
			p.flush();
		} catch (Exception e) {
			throw new RuntimeException("Unable to write to this file: " + file, e);
		} finally {
            close(p);
        }
	}

    private void close(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (IOException e) {
            }
        }
    }

    private void close(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Creates temp file, writes data to it and returns the file.
     * File will be deleted on vm exit!
     */
    public File writeToTempFile(String content) {
		File file = createTempFile();
		write(file, content);
		return file;
	}

    /**
     * Creates temp file that will be deleted on vm exit!
     */
	public File createTempFile() {
		File file;
		try {
			file = File.createTempFile("tmp", TEMP_FILE_SUFFIX);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create temporary file", e);
		}
		file.deleteOnExit();
		return file;
	}

    public void create(String file) {
        File f = new File(file);
        if (f.exists()) {
            return;
        }

        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Unfortunately, I was unable to create this file: " + file, e);
        }
    }
}