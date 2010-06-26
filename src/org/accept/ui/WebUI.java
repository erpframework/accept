package org.accept.ui;

import java.io.File;
import java.util.logging.Logger;

import com.sabre.tinyweb.internal.nano.NanoWebEngine;
import org.accept.domain.ValidationResult;
import org.accept.impl.gwz.GiveWenZenAccept;

import com.sabre.tinyweb.Request;
import com.sabre.tinyweb.WebApplication;
import com.sabre.tinyweb.WebPage;
import org.accept.impl.settings.AcceptSettings;
import org.accept.util.commandline.CommandLineConfig;
import org.accept.util.exception.StackTracePrinter;

public class WebUI {

    AcceptSettings settings = new AcceptSettings();    

    GiveWenZenAccept accept = new GiveWenZenAccept();
    final static Logger log = Logger.getLogger(WebUI.class.toString());

    @WebPage
    public String files(Request request) throws Exception {
        String dir = request.getParameters().get("dir");
        //TODO: should decoding be a part of TinyWeb?
        dir = java.net.URLDecoder.decode(dir, "UTF-8");

        String out = accept.getFiles(dir);
        //TODO, veeeeeeeeeeery hacky!!!!
        if (!out.contains("<li")) {
            accept.createExampleStory(new File(dir));
            out = accept.getFiles(dir);
        }
        return out;
    }

    @WebPage
    public String settings(Request request) throws Exception {
        return settings.getRaw().getContent();
    }

    @WebPage
    public String validate(Request request) throws Exception {
        try {
            String content = request.getParameters().get("content");
            String guid = request.getParameters().get("guid");

            log.info("Using settings:\n" + settings + "\n");
            log.info("Received request to validate following:\n" + content + "\n");
            return accept.validate(content, settings, guid);
        } catch (Throwable e) {
            ValidationResult result = new ValidationResult();
            result.appendOutput(new StackTracePrinter().print(e));
            result.setMessage("Unable to validate the story. The cause is:\n" + e.getMessage());
            result.setStatus(ValidationResult.Status.red);
            return result.toJSON();
        }
    }

    @WebPage
    public String getProgress(Request request) throws Exception {
        String guid = request.getParameters().get("guid");
        return accept.getProgress(guid);
    }

    @WebPage
    public String saveSettings(Request request) throws Exception {
        String s = request.getParameters().get("settings");
        settings.getRaw().save(s);
        return "Settings SAVED.";
    }

    //For development you must change the working dir to Accept/html !!!
    public static void main(String[] args) {
        CommandLineConfig config = new CommandLineConfig(args);

        int port = config.getPort(80);
        // workDir/storiesDir not yet used...
        String workDir = config.getWorkDir(".");
        String storiesDir = config.getStoriesDir(".");

        WebApplication app = new WebApplication(new NanoWebEngine("."));
        app.addWebPage(new WebUI());
        app.start(port);
    }
}