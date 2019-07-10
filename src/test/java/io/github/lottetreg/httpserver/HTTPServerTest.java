package io.github.lottetreg.httpserver;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HTTPServerTest {
  @Test
  public void itPrintsHelloWorld() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    HTTPServer.print(new PrintStream(out));

    Assert.assertEquals("Hello world!\n", out.toString());
  }
}
