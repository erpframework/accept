package org.accept.api;

import org.accept.domain.ValidationResult;
import org.accept.impl.gwz.GWZRunner;
import org.accept.impl.junit.AcceptFrameworkMethod;
import org.accept.impl.junit.JUnitSucks;
import org.accept.impl.reporting.ReportGenerator;
import org.accept.util.files.FolderActions;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.accept.util.files.FolderActions.eachFile;

public class AcceptFolderRunner extends Suite {
    private String folder;
    private final String reportFile;
    private List<ValidationResult> passed = new LinkedList<ValidationResult>();
    private List<ValidationResult> failed = new LinkedList<ValidationResult>();
    private ReportGenerator generator = new ReportGenerator();

    public AcceptFolderRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, getRunners(klass));
        folder = getFolder(klass);
        reportFile = getReportFile(klass);
	}

    private String getReportFile(Class<?> klass) {
        HtmlReportOutputFile file = klass.getAnnotation(HtmlReportOutputFile.class);
        if (file == null || file.value().length() == 0) {
            return "target/accept.report.html";
        }
        return file.value();
    }

    private static List<Runner> getRunners(final Class<?> klass) throws InitializationError {
        final LinkedList<Runner> runners = new LinkedList<Runner>();
        final String folder = getFolder(klass);
        eachFile(folder, ".*\\.story").act(new FolderActions.FileContentHandler() {
            public void handle(File file, String content) {
                AcceptStoryRunner.RunnableStory story = new AcceptStoryRunner.RunnableStory().steps(content);
                try {
                    //TODO this name can be done better (e.g. show in IDEA nicer)
                    String name = file.getPath().replaceFirst("\\.story$", "");
                    AcceptStoryRunner r = new AcceptStoryRunner(klass, name, story);
                    runners.add(r);
                } catch (InitializationError initializationError) {
                    throw new RuntimeException("Unable to instantianate runner", initializationError);
                }
            }
        });

        return runners;
    }

    private static String getFolder(Class<?> klass) {
        RootFolder folder = klass.getAnnotation(RootFolder.class);
        if (folder == null || folder.value() == null) {
            throw new RuntimeException("\nInvalid configuration of " + klass.getSimpleName() + ".\n" +
                    "Missing @RootFolder annotation\n" +
                    "Example of correct usage:\n" +
                    "  @RunWith(" + AcceptFolderRunner.class.getSimpleName() + ")\n" +
                    "  @RootFolder(\"path_to_folder_with_stories\")\n");
        }
        return folder.value();
    }

    @Override
    protected List<Runner> getChildren() {
        return super.getChildren();    //To change body of overridden methods use File | Settings | File Templates.
    }


}