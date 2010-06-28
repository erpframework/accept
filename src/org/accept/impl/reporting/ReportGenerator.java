package org.accept.impl.reporting;

import biz.source_code.miniTemplator.MiniTemplator;
import org.accept.domain.ValidationResult;
import org.accept.util.files.FileIO;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 28, 2010
 * Time: 5:36:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportGenerator {

    public String generate(List<ValidationResult> incomplete, List<ValidationResult> complete, String outputReportFile) {
        new FileIO().create(outputReportFile);
        URL resource = getClass().getClassLoader().getResource("report.template.html");
        MiniTemplator t;
        try {
            t = new MiniTemplator(resource.getFile());
            int idx = -1;
            t.setVariable("incompleteCount", incomplete.size() + "");
            for (ValidationResult result : incomplete) {
                idx ++;
                t.setVariable("story", result.getStory());
                t.setVariable("storyName", result.getStoryName());
                t.setVariable("idx", idx + "");
                t.addBlock("incompleteStory");
            }
            t.setVariable("completeCount", complete.size() + "");
            for (ValidationResult result : complete) {
                idx ++;
                t.setVariable("story", result.getStory());
                t.setVariable("storyName", result.getStoryName());
                t.setVariable("idx", idx + "");
                t.addBlock("completeStory");
            }

            t.generateOutput(outputReportFile);
            return t.generateOutput();            
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate report\n", e);
        }
    }
}
