package org.accept.impl.gwz;

import java.io.File;
import java.util.*;

import org.accept.domain.ValidationResult;
import org.accept.util.classpath.SimpleClasspathExplorer;
import org.accept.util.exception.StackTracePrinter;
import org.givwenzen.*;
import org.givwenzen.annotations.MarkedClass;

public class GWZRunner {
    private StepsExtractor stepsExtractor = new StepsExtractor();

    public void runContent(String content, File contentFile) {
        IDomainStepFinder finder = new MyDomainStepFinder();

        List<String> steps = new LinkedList<String>();
        stepsExtractor.extract(content, steps);

        final GivWenZenExecutor executor = new  GivWenZenExecutor(finder, new DomainStepFactory());

        int stepIndex = -1;
        for (String step : steps) {
            stepIndex++;
            try {
                executor.given(step);
            } catch (Exception e) {
                Throwable actual = theBottom(e, 2);
                throw new GWZException(stepIndex, step, e, actual, contentFile);
            }
        }
    }

	public ValidationResult run(String content, File contentFile) {
        try {
            this.runContent(content, contentFile);
        } catch (GWZException e) {
            //TODO: this printer swallows pieces of stack trace
            String trace = new StackTracePrinter().print(e.actual);
            return new ValidationResult(e.stepIndex, e.step, e.actual.getMessage(), trace, e.cause);
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
        Exception cause;
        Throwable actual;
        File contentFile;

        public GWZException(int stepIndex, String step, Exception cause, Throwable actual, File contentFile) {
            super("Failed to run step: " + step, cause);
            this.stepIndex = stepIndex;
            this.step = step;
            this.cause = cause;
            this.actual = actual;
            this.contentFile = contentFile;
            if (cause != actual) {
                setStackTrace(actual.getStackTrace());
            }
        }

        @Override
        public String toString() {
            String rootCause = (actual != cause)? actual.getMessage() : cause.getMessage();
            return "\nFailed to execute step!\n" +
                   "Step:  " + step + "\n" +
                   "File:  " + contentFile + "\n" +
                   "Line:  " + stepIndex + "\n" +
                   "Cause: " + rootCause + "\n";
        }
    }

    public class MyDomainStepFinder implements IDomainStepFinder {
        public Set<MarkedClass> findStepDefinitions() {
            final HashSet<MarkedClass> s = new HashSet<MarkedClass>();
            new SimpleClasspathExplorer().findClasses("bdd/steps", new SimpleClasspathExplorer.ClassHandler() {
                public void handle(Class cls) {
                    s.add(new MarkedClass(cls));
                }
            });
            return s;
        }
    }
}