package org.accept.impl.junit;

import org.accept.api.AcceptStoryRunner;
import org.accept.impl.gwz.StepsExtractor;
import org.accept.util.files.FolderActions;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.accept.util.files.FolderActions.eachFile;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 24, 2010
 * Time: 10:30:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class JUnitSucks {

    public List<FrameworkMethod> createMethods(String folder) {
        final List<FrameworkMethod> testMethods = new LinkedList<FrameworkMethod>();
        eachFile(folder, ".*\\.story").act(new FolderActions.FileContentHandler() {
            public void handle(File file, String content) {
                testMethods.add(new AcceptFrameworkMethod(file, content));
            }
        });

        return testMethods;
    }

    public List<FrameworkMethod> createMethodsForStory(AcceptStoryRunner.RunnableStory story) {
        List<FrameworkMethod> testMethods = new LinkedList<FrameworkMethod>();
        for(StepsExtractor.Step step : story.getSteps()) {
            testMethods.add(new AcceptFrameworkMethod(step));
        }
        return testMethods;
    }
}