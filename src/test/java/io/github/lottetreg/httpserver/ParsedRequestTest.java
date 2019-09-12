package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.support.RequestStringBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParsedRequestTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itCorrectlyParsesAValidRequest() throws IOException {
    String request = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Valid-Header: something")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    assertEquals("GET", parsedRequest.getMethod());
    assertEquals("/", parsedRequest.getPath());
    assertEquals("HTTP/1.0", parsedRequest.getVersion());
    assertEquals("something", parsedRequest.getHeaders().get("Valid-Header"));
  }

  @Test
  public void itSetsEmptyHeadersOnTheHeadersField() throws IOException {
    String request = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Empty-Header: ")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    assertEquals(" ", parsedRequest.getHeaders().get("Empty-Header"));
  }

  @Test
  public void itLogsAMessageAndDoesNotSetInvalidHeadersOnTheHeadersField() throws IOException {
    class MockOut implements Outable {
      private boolean receivedPrintln;
      private String message;

      @Override
      public void println(String message) {
        this.receivedPrintln = true;
        this.message = message;
      }
    }

    String request = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Invalid-Header")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
    MockOut mockOut = new MockOut();

    ParsedRequest parsedRequest = new ParsedRequest(inputStream, mockOut);

    assertTrue(mockOut.receivedPrintln);
    assertEquals("Invalid header: Invalid-Header", mockOut.message);
    assertEquals(null, parsedRequest.getHeaders().get("Invalid-Header"));
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineIsEmpty() throws IOException {
    String request = "\r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: ");

    new ParsedRequest(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyOneElement() throws IOException {
    String request = "GET \r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET");

    new ParsedRequest(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyTwoElements() throws IOException {
    String request = "GET / \r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET /");

    new ParsedRequest(inputStream);
  }
}
