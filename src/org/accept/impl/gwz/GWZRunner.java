package org.accept.impl.gwz;

import java.io.File;
import java.util.*;

import org.accept.domain.ValidationResult;
import org.accept.util.classpath.SimpleClasspathExplorer;
import org.givwenzen.*;
import org.givwenzen.annotations.MarkedClass;
import static org.accept.impl.gwz.StepsExtractor.*;

public class GWZRunner {
    private StepsExtractor stepsExtractor = new StepsExtractor();

    public void runExplosively(String content, File contentFile) {
        IDomainStepFinder finder = new MyDomainStepFinder();

        List<Step> steps = new LinkedList<Step>();
        stepsExtractor.extract(content, steps);

        final GivWenZenExecutor executor = new  GivWenZenExecutor(finder, new DomainStepFactory());

        for (Step step : steps) {
            try {
                executor.given(step.step);
            } catch (GivWenZenExecutionException e) {
                Throwable actual = extractActualException(e, 2);
                throw new GWZException(step.lineNo, step.step, actual, contentFile);
            } catch (Exception e) {
                throw new GWZException(step.lineNo, step.step, e, contentFile);
            }
        }
    }

	public ValidationResult run(String content, File contentFile) {
        String storyName = contentFile.getParentFile().getName() + "/" + contentFile.getName();
        try {
            this.runExplosively(content, contentFile);
        } catch (GWZException e) {
            return new ValidationResult()
                    .setStepIndex(e.stepIndex)
                    .setStep(e.step)
                    .setException(e.cause)
                    .setInfo(e.cause.getMessage())
                    .setStory(content)
                    .setStoryName(storyName);
        }
        return new ValidationResult()
                .setStory(content)
                .setStoryName(storyName);
	}

    //TODO: it would be nice if GWZ had simply getActualException() method
	private Throwable extractActualException(Throwable e, int max) {
		Throwable cause = e;
		int i = 0;
		while (cause.getCause() != null && ++i <= max) {
			cause = cause.getCause();
		}
		return cause;
	}

    private class GWZException extends RuntimeException {
        int stepIndex;
        String step;
        Throwable cause;
        File contentFile;

        public GWZException(int stepIndex, String step, Throwable cause, File contentFile) {
            super("Failed to validate story: " + contentFile, cause);
            this.stepIndex = stepIndex;
            this.step = step;
            this.cause = cause;
            this.contentFile = contentFile;
            setStackTrace(cause.getStackTrace());
        }

        @Override
        public String toString() {
            return "\nFailed to validate story!\n" +
                   "Story:   " + contentFile + "\n" +
                   "Step:    " + step + "\n" +
                   "Line no: " + stepIndex + "\n" +
                   "Cause:   " + cause.getMessage() + "\n";
        }
    }

    public class MyDomainStepFinder implements IDomainStepFinder {
        public Set<MarkedClass> findStepDefinitions() {
            final HashSet<MarkedClass> s = new HashSet<MarkedClass>();
            try {
                new SimpleClasspathExplorer().findClasses("bdd/steps", new SimpleClasspathExplorer.ClassHandler() {
                    public void handle(Class cls) {
                        s.add(new MarkedClass(cls));
                    }
                });
            }
            catch (SimpleClasspathExplorer.UnableToFindPackageException e) {
                //Ignore, let's make GWZ handle this problem        
            }
            return s;
        }
    }
}