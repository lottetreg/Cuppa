package io.github.lottetreg.Cuppa;

import io.github.lottetreg.Cuppa.support.RequestStringBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPRequestTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itIsInitializedWithTheCorrectObjects() throws IOException {
    String requestString = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .addHeader("Content-Length: 17")
        .setBody("some body to love")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    HTTPRequest request = new HTTPRequest(parsedRequest);

    assertEquals("GET", request.getMethod());
    assertEquals("/", request.getPath());
    assertEquals("HTTP/1.0", request.getProtocolVersion());
    assertEquals("17", request.getHeader("Content-Length"));
    assertEquals("some body to love", request.getBody());
  }

  @Test
  public void itDoesNotSetTheBodyIfTheContentLengthIsMissing() throws IOException {
    String requestString = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .setBody("some body to love")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    HTTPRequest request = new HTTPRequest(parsedRequest);

    assertEquals("", request.getBody());
  }

  @Test
  public void itLogsAMessageAndDoesNotSetTheBodyIfTheContentLengthIsNotANumber() throws IOException {
    class MockOut implements Outable {
      private boolean receivedPrintln;
      private String message;

      @Override
      public void println(String message) {
        this.receivedPrintln = true;
        this.message = message;
      }
    }

    String requestString = new RequestStringBuilder()
        .setMethod("GET")
        .setPath("/")
        .addHeader("Content-Length: NaN")
        .setBody("some body to love")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(requestString.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);
    MockOut mockOut = new MockOut();

    HTTPRequest request = new HTTPRequest(parsedRequest, mockOut);

    assertTrue(mockOut.receivedPrintln);
    assertEquals("Invalid Content-Length: NaN", mockOut.message);
    assertEquals("", request.getBody());
  }

  @Test
  public void getHeaderReturnsNullIfTheHeaderDoesNotExist() throws IOException {
    HTTPRequest request = new RequestHelpers().buildHTTPRequest("GET", "/");

    assertEquals(null, request.getHeader("Header 1"));
  }

  @Test
  public void getContentLengthReturns0IfTheContentLengthDoesNotExist() throws IOException {
    HTTPRequest request = new RequestHelpers().buildHTTPRequest("GET", "/");

    assertEquals(0, request.getContentLength());
  }
}
