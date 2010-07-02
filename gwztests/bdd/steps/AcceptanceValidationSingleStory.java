package bdd.steps;

import org.accept.api.AcceptRunner;
import org.accept.api.AcceptStoryRunner;
import org.accept.api.HtmlReportOutputFile;
import org.accept.api.RootFolder;
import org.junit.runner.RunWith;

@RunWith(AcceptStoryRunner.class)
//it is very unfortunate, but this test depends on where you run it (e.g. the work folder)
public class AcceptanceValidationSingleStory {}