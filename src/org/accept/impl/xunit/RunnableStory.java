package org.accept.impl.xunit;

import org.accept.impl.gwz.StepsExtractor;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: Szczepan Faber
 */
public class RunnableStory {
    private List<StepsExtractor.Step> steps = new LinkedList<StepsExtractor.Step>();

    public List<StepsExtractor.Step> getSteps() {
        return steps;
    }

    public RunnableStory steps(String stepsAsString) {
        new StepsExtractor().extract(stepsAsString, steps);
        return this;
    }

}
