package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPResponseTest {
  @Test
  public void a200ResponseIsCorrectlyBuiltIntoABytes() {
    HTTPResponse response = new HTTPResponse.Builder(200)
        .setProtocolVersion("1.1")
        .setHeaders(new HTTPHeaders(Arrays.asList("Header 1: some value")))
        .setBody("some body to love")
        .build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.1 200 OK\r\nHeader 1: some value\r\n\r\nsome body to love", new String(bytes));
  }

  @Test
  public void a301ResponseIsCorrectlyBuiltIntoABytes() {
    HTTPResponse response = new HTTPResponse.Builder(301).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 301 Moved Permanently\r\n\r\n", new String(bytes));
  }

  @Test
  public void a404ResponseIsCorrectlyBuiltIntoABytes() {
    HTTPResponse response = new HTTPResponse.Builder(404).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 404 Not Found\r\n\r\n", new String(bytes));
  }

  @Test
  public void a405ResponseIsCorrectlyBuiltIntoABytes() {
    HTTPResponse response = new HTTPResponse.Builder(405).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 405 Method Not Allowed\r\n\r\n", new String(bytes));
  }

  @Test
  public void a500ResponseIsCorrectlyBuiltIntoABytes() {
    HTTPResponse response = new HTTPResponse.Builder(500).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 500 Internal Server Error\r\n\r\n", new String(bytes));
  }
}
