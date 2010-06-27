package org.accept.impl.gwz;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

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
            .containsExactly("I enter 7", "I enter 10", "I add the numbers", "The calculator shows 17");
    }

    @Test
    public void shouldTrimSteps() throws Exception {
        //when
        e.extract("   trim me   ", steps);

        //then
        assertThat(steps).containsExactly("trim me");
    }

    @Test
    public void shouldIgnoreEmptyLines() throws Exception {
        //when
        e.extract("   \n" +
                "  life is good!\n", steps);

        //then
        assertThat(steps).containsExactly("life is good!");
    }

    @Test
    public void shouldIgnoreCommentedOutLines() throws Exception {
        //when
        e.extract("# this is just a comment   \n" +
                "  Peace.\n", steps);

        //then
        assertThat(steps).containsExactly("Peace.");
    }
}
