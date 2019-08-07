package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class HTTPHeadersTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itIsInitializedWithAListOfStrings() {
    List<String> headersList = Arrays.asList(
            "Header 1: some value",
            "Header 2: some other value");

    HTTPHeaders headers = new HTTPHeaders(headersList);

    assertEquals("some value", headers.getHeader("Header 1"));
    assertEquals("some other value", headers.getHeader("Header 2"));
  }

  @Test
  public void itDoesNotThrowAnExceptionIfTheListOfHeadersIsEmpty() {
    List<String> headersList = Arrays.asList();

    new HTTPHeaders(headersList);
  }

  @Test
  public void getHeaderReturnsNullIfTheHeaderIsNotPresent() {
    List<String> headersList = Arrays.asList();

    HTTPHeaders headers = new HTTPHeaders(headersList);

    assertEquals(null, headers.getHeader("Header 1"));
  }

  @Test
  public void getHeaderOrDefaultReturnsTheHeaderIfPresent() {
    List<String> headersList = Arrays.asList("Header 1: some value");

    HTTPHeaders headers = new HTTPHeaders(headersList);

    assertEquals("some value", headers.getHeaderOrDefault("Header 1", ""));
  }

  @Test
  public void getHeaderOrDefaultReturnsDefaultIfTheHeaderIsNotPresent() {
    List<String> headersList = Arrays.asList();

    HTTPHeaders headers = new HTTPHeaders(headersList);

    assertEquals("default", headers.getHeaderOrDefault("Header 1", "default"));
  }

  @Test
  public void itThrowsAnExceptionIfAHeaderIsEmpty() {
    exceptionRule.expect(HTTPHeaders.IncorrectlyFormattedHeaderException.class);
    exceptionRule.expectMessage("Header is incorrectly formatted: ");

    new HTTPHeaders(Arrays.asList(""));
  }

  @Test
  public void itThrowsAnExceptionIfAHeaderIsFormattedIncorrectly() {
    exceptionRule.expect(HTTPHeaders.IncorrectlyFormattedHeaderException.class);
    exceptionRule.expectMessage("Header is incorrectly formatted: Header 1:some value");

    new HTTPHeaders(Arrays.asList("Header 1:some value"));
  }

  @Test
  public void itThrowsAnExceptionIfAHeaderIsMissingAName() {
    exceptionRule.expect(HTTPHeaders.IncorrectlyFormattedHeaderException.class);
    exceptionRule.expectMessage("Header is incorrectly formatted: : some value");

    new HTTPHeaders(Arrays.asList(": some value"));
  }

  @Test
  public void itThrowsAnExceptionIfAHeaderIsMissingAValue() {
    exceptionRule.expect(HTTPHeaders.IncorrectlyFormattedHeaderException.class);
    exceptionRule.expectMessage("Header is incorrectly formatted: Header 1: ");

    new HTTPHeaders(Arrays.asList("Header 1: "));
  }
}
