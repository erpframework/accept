package org.accept.api;

import org.accept.domain.ValidationResult;
import org.accept.impl.gwz.GWZRunner;
import org.accept.impl.junit.AcceptFrameworkMethod;
import org.accept.impl.junit.JUnitSucks;
import org.accept.impl.reporting.ReportGenerator;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class AcceptRunner extends BlockJUnit4ClassRunner {

    private String folder;
    //TODO: synchronize those lists?
    private List<ValidationResult> passed = new LinkedList<ValidationResult>();
    private List<ValidationResult> failed = new LinkedList<ValidationResult>();
    private ReportGenerator generator = new ReportGenerator();
    private String reportFile = "target/accept.report.html";

    public AcceptRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        generator.generate(failed, passed, reportFile);
    }

    private void validateRootFolder() {
        List<Annotation> anns = asList(this.getTestClass().getAnnotations());
        folder = null;
        for (Annotation ann : anns) {
            if (ann.annotationType() == RootFolder.class) {
                folder = ((RootFolder) ann).value();
            } else if (ann.annotationType() == HtmlReportOutputFile.class) {
                reportFile = ((HtmlReportOutputFile) ann).value();
            }
        }
        if (folder == null) {
            throw new RuntimeException("\nInvalid configuration of AcceptRunner.\n" +
                    "Missing @RootFolder annotation\n" +
                    "Example of correct usage:\n" +
                    "  @RunWith(AcceptRunner.class)\n" +
                    "  @RootFolder(\"path_to_folder_with_stories\")\n");
        }
    }

    @Override
	protected List<FrameworkMethod> computeTestMethods() {
        validateRootFolder();
        JUnitSucks sucker = new JUnitSucks();
        return sucker.createMethods(folder);
	}

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, Object test) {
        Statement s = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
                ValidationResult result = new GWZRunner().run(m.content, m.file);
                if (result.getException() != null) {
                    failed.add(result);
                    throw result.getException();
                } else {
                    passed.add(result);
                }
            }
        };
        return s;
    }

    @Override
    protected String testName(FrameworkMethod method) {
        AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
        return m.file.getParentFile().getName() + "/" + m.file.getName();
    }
}