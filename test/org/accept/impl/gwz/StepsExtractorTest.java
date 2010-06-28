package org.accept.impl.gwz;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.accept.impl.gwz.StepsExtractor.*;

public class StepsExtractorTest {

    StepsExtractor e = new StepsExtractor();
    List steps = new LinkedList();

    @Test
    public void shouldReadSteps() throws Exception {
        //when
        e.extract("Given\n" +
                "  I enter 7\n" +
                "  I enter 10\n" +
                "When\n" +
                "  I add the numbers\n" +
                "Then\n" +
                "  The calculator shows 17", steps);

        //then
        assertThat(steps)
            .containsExactly(new Step("I enter 7", 2), new Step("I enter 10", 3), new Step("I add the numbers", 5), new Step("The calculator shows 17", 7));
    }

    @Test
    public void shouldTrimSteps() throws Exception {
        //when
        e.extract("   trim me   ", steps);

        //then
        assertThat(steps).containsExactly(new Step("trim me", 1));
    }

    @Test
    public void shouldIgnoreEmptyLines() throws Exception {
        //when
        e.extract("   \n\n" +
                "  life is good!\n", steps);

        //then
        assertThat(steps).containsExactly(new Step("life is good!", 3));
    }

    @Test
    public void shouldIgnoreCommentedOutLines() throws Exception {
        //when
        e.extract("# this is just a comment   \n" +
                "  Peace.\n", steps);

        //then
        assertThat(steps).containsExactly(new Step("Peace.", 2));
    }
}
