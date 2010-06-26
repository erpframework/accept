package org.accept.impl.gwz;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.logging.Logger;

import org.accept.api.Accept;
import org.accept.impl.settings.AcceptSettings;
import org.accept.util.files.FileIO;
import org.accept.util.files.FolderActions;

public class GiveWenZenAccept implements Accept {
	
	final static Logger log = Logger.getLogger(GiveWenZenAccept.class.toString());
    ProcessRunner runner = new ProcessRunner();

	public String validate(String content, String settings, String guid) {
        //TODO: those files are not deleted until the end of the program run
		File input = new FileIO().writeToTempFile(content);
		File output = new FileIO().createTempFile();

        org.accept.impl.settings.AcceptSettings s = new AcceptSettings(settings);
		String command = s.buildCommand() + " org.accept.impl.gwz.GivWenZenMain " + input + " " + output;

		log.info("About to run this command: \n" + command);

		String out = runner.run(command, output, guid);
		return out;
	}

    public String getProgress(String guid) {
        return runner.getOutput(guid);
    }

    public String getFiles(String dir) {
        //TODO: ugly code copy-pasted from web. I confess.
        //TODO It should be changed to only show .story files
        if (dir.charAt(dir.length() - 1) == '\\') {
            dir = dir.substring(0, dir.length() - 1) + "/";
        } else if (dir.charAt(dir.length() - 1) != '/') {
            dir += "/";
        }

        StringBuffer out = new StringBuffer();
        if (new File(dir).exists()) {
            String[] files = new File(dir).list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.charAt(0) != '.';
                }
            });
            Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
            out.append("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
            // All dirs
            for (String file : files) {
                if (new File(dir, file).isDirectory()) {
                    out.append("<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + dir + file + "/\">"
                            + file + "</a></li>");
                }
            }
            // All files
            for (String file : files) {
                if (!new File(dir, file).isDirectory()) {
                    int dotIndex = file.lastIndexOf('.');
                    String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
                    out.append("<li class=\"file ext_" + ext + "\"><a href=\"#\" rel=\"" + dir + file + "\">"
                            + file + "</a></li>");
                }
            }
            out.append("</ul>");
        }
        String s = out.toString();
        return s;
    }

    public void validate(String folder) {
        FolderActions.eachFile(folder, ".*story").act(new FolderActions.FileContentHandler() {
            public void handle(File file, String content) {
                new GWZRunner().runContent(content, file);
            }
        });
    }
}