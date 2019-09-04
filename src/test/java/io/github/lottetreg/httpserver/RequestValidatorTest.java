package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.support.RequestBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RequestValidatorTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itCorrectlyParsesAValidRequest() throws IOException {
    String request = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Valid-Header: something")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    RequestParser requestValidator = new RequestParser(inputStream);

    assertEquals("GET", requestValidator.method);
    assertEquals("/", requestValidator.path);
    assertEquals("HTTP/1.0", requestValidator.version);
    assertEquals("something", requestValidator.headers.get("Valid-Header"));
  }

  @Test
  public void itDoesNotSetTheInvalidHeadersOnTheHeadersField() throws IOException {
    String request = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Invalid-Header: ")
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    RequestParser requestValidator = new RequestParser(inputStream);

    assertEquals(null, requestValidator.headers.get("Invalid-Header"));
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineIsEmpty() throws IOException {
    String request = "\r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(RequestParser.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: ");

    new RequestParser(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyOneElement() throws IOException {
    String request = "GET \r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(RequestParser.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET");

    new RequestParser(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyTwoElements() throws IOException {
    String request = "GET / \r\n\r\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(RequestParser.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET /");

    new RequestParser(inputStream);
  }
}
