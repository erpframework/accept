package org.accept.api;

import bdd.steps.CalculatorSteps;
import org.accept.impl.gwz.GWZRunner;
import org.accept.impl.gwz.StepsExtractor;
import org.accept.impl.xunit.AcceptFrameworkMethod;
import org.accept.impl.xunit.JUnitSucks;
import org.accept.impl.xunit.RunnableStory;
import org.accept.util.reflect.Whitebox;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

class AcceptStoryRunner extends BlockJUnit4ClassRunner {

    private String storyFile;
    private RunnableStory story;
    private GWZRunner gwzRunner = new GWZRunner();

    //TEST ONLY :)
    public AcceptStoryRunner(Class<?> klass) throws InitializationError {
        this(CalculatorSteps.class, " in test_addition.story", new RunnableStory().steps("Given\n" +
                "  calculator is turned on\n" +
                "  I enter 1\n" +
                "  I enter 100\n" +
                "When\n" +
                "  I add the numbers\n" +
                "Then\n" +
                "  The calculator shows 101"));
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        //do nothing, just prevent from validation errors
    }

    public AcceptStoryRunner(Class<?> klass, String storyFile, RunnableStory story) throws InitializationError {
        super(klass);
        this.storyFile = storyFile;
        assert storyFile != null;
        this.story = story;
        assert story != null;
    }

    @Override
	protected List<FrameworkMethod> computeTestMethods() {
        JUnitSucks sucker = new JUnitSucks();
        return sucker.createMethodsForStory(story);
	}        

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, Object test) {
        Statement s = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
                gwzRunner.runStep(new File(storyFile), m.step);
            }
        };
        return s;
    }

    @Override
    protected String getName() {
        return storyFile;
    }

    @Override
    protected String testName(FrameworkMethod method) {
        AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
        return m.step.step;
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        Description d = super.describeChild(method);
        AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
        Whitebox.setInternalState(d, "fDisplayName", m.step.step + " (" + storyFile + ".story:" + m.step.lineNo + ")");
        return d;
    }
}