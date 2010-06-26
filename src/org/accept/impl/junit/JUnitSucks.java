package org.accept.impl.junit;

import org.accept.util.files.FolderActions;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
        org.accept.util.files.FolderActions.eachFile(folder, ".*\\.story").act(new FolderActions.FileContentHandler() {
            public void handle(File file, String content) {
                testMethods.add(new AcceptFrameworkMethod(file, content));
            }
        });

        return testMethods;
    }
}