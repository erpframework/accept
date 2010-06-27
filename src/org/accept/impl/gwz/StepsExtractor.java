package org.accept.impl.gwz;

import org.accept.util.files.FileIO;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

public class StepsExtractor {
    public void extract(File contentFile, List<String> steps) {
        this.extract(new FileIO().read(contentFile), steps);
    }

    public void extract(String content, List<String> steps) {
        StringTokenizer st = new StringTokenizer(content, "\n");
        while (st.hasMoreTokens()) {
            String step = st.nextToken().trim();
            if (    //ignore keywords
                    step.matches("(Given|When|Then)") ||
                    //ignore empty lines
                    step.length() == 0 ||
                    //ignore commented-out lines
                    step.startsWith("#")) {
                continue;
            }
            steps.add(step);
        }
    }
}
