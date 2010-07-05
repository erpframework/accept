package org.accept.api;

import org.accept.impl.gwz.GWZRunner;
import org.accept.impl.gwz.StepsExtractor;
import org.accept.impl.junit.AcceptFrameworkMethod;
import org.accept.impl.junit.JUnitSucks;
import org.accept.util.reflect.Whitebox;
import org.givwenzen.annotations.MarkedClass;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AcceptStepsRunner extends BlockJUnit4ClassRunner {

    private String storyFile;
    private RunnableStory story;
    private GWZRunner gwzRunner = new GWZRunner();

    public static class RunnableStory {
        private List<StepsExtractor.Step> steps = new LinkedList<StepsExtractor.Step>();

        public List<StepsExtractor.Step> getSteps() {
            return steps;
        }

        public RunnableStory steps(String stepsAsString) {
            new StepsExtractor().extract(stepsAsString, steps);
            return this;
        }
    };

    //TEST ONLY :)
    public AcceptStepsRunner(Class<?> klass) throws InitializationError {
        this(klass, " in test_addition.story", new RunnableStory().steps("Given\n" +
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

    public AcceptStepsRunner(Class<?> klass, String storyFile, RunnableStory story) throws InitializationError {
        super(klass);
        this.storyFile = storyFile;
        assert storyFile != null;
        this.story = story;
        assert story != null;
    }
//
//    @Override
//	protected List<FrameworkMethod> computeTestMethods() {
//        stepState = new Object[]{this} : stepState;
//        int adaptersLength = this.stepState.length + 1;
//        Object[] adapters = new Object[adaptersLength];
//        System.arraycopy(this.stepState, 0, adapters, 0, adaptersLength - 1);
//        adapters[adaptersLength - 1] = this;
//
//        Set<MarkedClass> classes = domainStepFinder.findStepDefinitions();
//        List<Object> stepDefinitions = factory.createStepDefinitions(classes, adapters);
//        stepDefinitions.add(0, this.stepState);
//        JUnitSucks sucker = new JUnitSucks();
//        return sucker.createMethodsForStory(story);
//	}

//    @Override
//    protected Statement methodInvoker(final FrameworkMethod method, Object test) {
//        Statement s = new Statement() {
//            @Override
//            public void evaluate() throws Throwable {
//                AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
//                gwzRunner.runStep(new File("foo"), m.step);
//            }
//        };
//        return s;
//    }

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
        Whitebox.setInternalState(d, "fDisplayName", m.step.step + "(" + storyFile + ")");
        return d;
    }
}