package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itIsInitializedWithTheCorrectObjects() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders(Arrays.asList("Header 1: some value"));

    HTTPRequest request = new HTTPRequest(initialLine, headers);

    assertEquals("GET", request.getMethod());
    assertEquals("/", request.getURI());
    assertEquals("HTTP/1.0", request.getHTTPVersion());
    assertEquals("some value", request.getHeader("Header 1"));
  }

  @Test
  public void getHeaderReturnsNullIfTheHeaderDoesNotExist() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders();

    HTTPRequest request = new HTTPRequest(initialLine, headers);

    assertEquals(null, request.getHeader("Header 1"));
  }

  @Test
  public void getContentLengthReturnsTheContentLength() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders(Arrays.asList("Content-Length: 23"));

    HTTPRequest request = new HTTPRequest(initialLine, headers);

    assertEquals(23, request.getContentLength());
  }

  @Test
  public void getContentLengthReturns0IfTheContentLengthDoesNotExist() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders();

    HTTPRequest request = new HTTPRequest(initialLine, headers);

    assertEquals(0, request.getContentLength());
  }

  @Test
  public void getContentLengthThrowsExceptionIfContentLengthInvalid() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders(Arrays.asList("Content-Length: Not a Number"));

    exceptionRule.expect(HTTPRequest.InvalidContentLength.class);
    exceptionRule.expectMessage("Invalid content length header: Not a Number");

    new HTTPRequest(initialLine, headers).getContentLength();
  }

  @Test
  public void itHasABody() {
    HTTPInitialLine initialLine = new HTTPInitialLine("GET / HTTP/1.0");
    HTTPHeaders headers = new HTTPHeaders();
    String body = "some body";

    HTTPRequest request = new HTTPRequest(initialLine, headers, body);

    assertEquals("some body", request.getBody());
  }
}
