import org.accept.api.AcceptRunner;
import org.accept.api.HtmlReportOutputFile;
import org.accept.api.RootFolder;
import org.junit.runner.RunWith;

@RunWith(AcceptRunner.class)
//this test depends on where you run it (e.g. the working dir)
//unless obviously you specify the absolute path in @RootFolder but for heavens sake don't do it.
@RootFolder("acceptance")
@HtmlReportOutputFile("bin/acceptance.report.html")
public class AllAcceptanceValidation {}