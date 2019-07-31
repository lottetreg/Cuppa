package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPResponseTest {
  @Test
  public void A200ResponseIsCorrectlyStringified() {
    HTTPResponse response = new HTTPResponse.Builder(200)
        .setProtocolVersion("1.1")
        .setHeaders(new HTTPHeaders(Arrays.asList("Header 1: some value")))
        .setBody("some body to love")
        .build();

    assertEquals("HTTP/1.1 200 OK\r\nHeader 1: some value\r\n\r\nsome body to love", response.toString());
  }

  @Test
  public void A301ResponseIsCorrectlyStringified() {
    HTTPResponse response = new HTTPResponse.Builder(301).build();

    assertEquals("HTTP/1.0 301 Moved Permanently\r\n\r\n", response.toString());
  }

  @Test
  public void A404ResponseIsCorrectlyStringified() {
    HTTPResponse response = new HTTPResponse.Builder(404).build();

    assertEquals("HTTP/1.0 404 Not Found\r\n\r\n", response.toString());
  }

  @Test
  public void A405ResponseIsCorrectlyStringified() {
    HTTPResponse response = new HTTPResponse.Builder(405).build();

    assertEquals("HTTP/1.0 405 Method Not Allowed\r\n\r\n", response.toString());
  }

  @Test
  public void A500ResponseIsCorrectlyStringified() {
    HTTPResponse response = new HTTPResponse.Builder(500).build();

    assertEquals("HTTP/1.0 500 Internal Server Error\r\n\r\n", response.toString());
  }
}
