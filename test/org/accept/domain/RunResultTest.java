package org.accept.domain;

import static org.junit.Assert.*;

import org.junit.Test;


public class RunResultTest {

	@Test
	public void shouldBuildJSON() throws Exception {
//		assertEquals("{\"output\":\"foo\"}", new RunResult("foo").toJSON());
	}

	@Test
	public void shouldEscapeQuotes() throws Exception {
//		assertEquals("{\"output\":\"foo\\n \\\"bar\\\" \\n baz\"}", new RunResult("foo\n \"bar\" \n baz").toJSON());
	}
	
}
