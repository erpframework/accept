package org.accept.impl.gwz;

import org.accept.util.files.FileIO;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

public class StepsExtractor {

    public void extract(String content, List<Step> steps) {
        String[] strings = content.split("\n");
        int lineNo = 0;
        for (String string : strings) {
            lineNo++;
            String step = string.trim();
            if (    //ignore keywords
                    step.matches("(Given|When|Then)") ||
                    //ignore empty lines
                    step.length() == 0 ||
                    //ignore commented-out lines
                    step.startsWith("#")) {
                continue;
            }
            steps.add(new Step(step, lineNo));
        }
    }

    public static class Step {
        public String step;
        public int lineNo;

        public Step(String step, int lineNo) {
            this.step = step;
            this.lineNo = lineNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Step)) return false;

            Step other = (Step) o;

            if (lineNo != other.lineNo) return false;
            if (step != null ? !step.equals(other.step) : other.step != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
