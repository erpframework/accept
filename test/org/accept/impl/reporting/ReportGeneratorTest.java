package org.accept.impl.reporting;

import org.accept.domain.ValidationResult;
import org.accept.util.files.FileIO;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 28, 2010
 * Time: 5:57:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportGeneratorTest {

    File temp = new FileIO().createTempFile();
    ReportGenerator g = new ReportGenerator();

    @Test
    public void shouldGenerateToPath() throws Exception {
        //given
        File deepTemp = new File(temp.getParentFile(), "foo/bar/baz/temp.txt");
        deepTemp.deleteOnExit();

        //when
        g.generate(asList(new ValidationResult().setStoryName("krk rocks")), (List) asList(), deepTemp.getPath());

        //then
        assertThat(new FileIO().read(deepTemp)).contains("krk rocks");
    }

    @Test
    public void shouldGenerateReport() throws Exception {
        //given
        List<ValidationResult> incomplete = asList(
                new ValidationResult().setStoryName("krk rocks").setStory("Krakow is a lovely place"),
                new ValidationResult().setStoryName("piwko rocks").setStory("dobre piwko nie jest zle"));

        List<ValidationResult> complete = asList(
                new ValidationResult().setStoryName("gimme Ferrari").setStory("Will you buy me a Ferrari?"),
                new ValidationResult().setStoryName("yeah, sure").setStory("Sure, mate!"));

        //when
        String out = g.generate(incomplete, complete, temp.getPath());

        //then
        assertThat(out)
                .contains("piwko rocks")
                .contains("dobre piwko nie jest zle")
                .contains("krk rocks")
                .contains("Krakow is a lovely place")
                .contains("gimme Ferrari")
                .contains("Will you buy me a Ferrari?")
                .contains("yeah, sure")
                .contains("Sure, mate!");
    }

    public static void main(String[] args) throws Exception {
        ReportGenerator g = new ReportGenerator();
        List<ValidationResult> incomplete = asList(
                new ValidationResult().setStoryName("Iteration 2/Addition.story").setStory("Given\n" +
                        "  calculator is turned on\n" +
                        "  I enter 7\n" +
                        "  I enter 10\n" +
                        "When\n" +
                        "  I add the numbers\n" +
                        "Then\n" +
                        "  The calculator shows 17"),
                new ValidationResult().setStoryName("Iteration 1/Subtraction.story").setStory("Given\n" +
                        "  calculator is turned on\n" +
                        "  I enter 15\n" +
                        "  I enter 10\n" +
                        "When\n" +
                        "  I subtract the numbers\n" +
                        "Then\n" +
                        "  The calculator shows 5"));

        List<ValidationResult> complete = asList(
                new ValidationResult().setStoryName("Iteration 1/beer.story").setStory("Given\n" +
                        "  I drink a beer\n" +
                        "When\n" +
                        "  I watch footbal\n" +
                        "Then\n" +
                        "  I am pretty happy"),
                new ValidationResult().setStoryName("Iteration 1/mockito.story").setStory("Given\n" +
                        "  I drink a mockito\n" +
                        "When\n" +
                        "  I am at the party\n" +
                        "Then\n" +
                        "  There's a big chance I'm having fun.")
                );
        
        g.generate(incomplete, complete, "generated.by.jUnit.for.manual.check.html");
    }
}
