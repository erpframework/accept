package org.accept.impl.reporting;

import biz.source_code.miniTemplator.MiniTemplator;
import org.accept.domain.ValidationResult;
import org.accept.util.files.FileIO;

import java.io.File;
import java.io.InputStream;
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
        InputStream resource = getClass().getClassLoader().getResourceAsStream("report.template.html");
        MiniTemplator t;
        try {
            MiniTemplator.TemplateSpecification spec = new MiniTemplator.TemplateSpecification();
            spec.templateText = new FileIO().read(resource);
            t = new MiniTemplator(spec);
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