package org.accept.impl.gwz;

import java.io.File;
import java.util.*;

import org.accept.domain.ValidationResult;
import org.accept.util.classpath.SimpleClasspathExplorer;
import org.givwenzen.*;
import org.givwenzen.annotations.MarkedClass;

public class GWZRunner {
    private StepsExtractor stepsExtractor = new StepsExtractor();

    public void runExplosively(String content, File contentFile) {
        IDomainStepFinder finder = new MyDomainStepFinder();

        List<String> steps = new LinkedList<String>();
        stepsExtractor.extract(content, steps);

        final GivWenZenExecutor executor = new  GivWenZenExecutor(finder, new DomainStepFactory());

        int stepIndex = -1;
        for (String step : steps) {
            stepIndex++;
            try {
                executor.given(step);
            } catch (GivWenZenExecutionException e) {
                Throwable actual = theBottom(e, 2);
                throw new GWZException(stepIndex, step, actual, contentFile);
            } catch (Exception e) {
                throw new GWZException(stepIndex, step, e, contentFile);
            }
        }
    }

	public ValidationResult run(String content, File contentFile) {
        try {
            this.runExplosively(content, contentFile);
        } catch (GWZException e) {
            return new ValidationResult(e.stepIndex, e.step, e.cause.getMessage(), e.cause);
        }
		return new ValidationResult();
	}

	private Throwable theBottom(Throwable e, int max) {
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