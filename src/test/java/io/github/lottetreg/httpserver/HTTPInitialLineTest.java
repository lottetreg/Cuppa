package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class HTTPInitialLineTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itIsInitializedWithAString() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");

    assertEquals("GET", initialLine.method);
    assertEquals("/", initialLine.URI);
    assertEquals("HTTP/1.0", initialLine.HTTPVersion);
  }

  @Test
  public void itRaisesAnExceptionIfTheInitialLineIsEmpty() {
    exceptionRule.expect(HTTPInitialLine.IncorrectlyFormattedInitialLineException.class);
    exceptionRule.expectMessage("Initial line is incorrectly formatted: ");

    new HTTPInitialLine("");
  }

  @Test
  public void itRaisesAnExceptionIfTheInitialLineHasOnlyOnePart() {
    exceptionRule.expect(HTTPInitialLine.IncorrectlyFormattedInitialLineException.class);
    exceptionRule.expectMessage("Initial line is incorrectly formatted: GET");

    new HTTPInitialLine("GET");
  }

  @Test
  public void itRaisesAnExceptionIfTheInitialLineHasOnlyTwoParts() {
    exceptionRule.expect(HTTPInitialLine.IncorrectlyFormattedInitialLineException.class);
    exceptionRule.expectMessage("Initial line is incorrectly formatted: GET /");

    new HTTPInitialLine("GET /");
  }
}
