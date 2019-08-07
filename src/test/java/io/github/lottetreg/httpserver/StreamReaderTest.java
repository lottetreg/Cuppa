package io.github.lottetreg.httpserver;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StreamReaderTest {
  private String data =
          "GET / HTTP/1.0\r\n" +
          "Content-Length: 17\r\n" +
          "\r\n" +
          "some body to love";

  @Test
  public void testReadLine() throws IOException {
    StreamReader reader =
            new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    assertEquals("GET / HTTP/1.0", reader.readLine());
    assertEquals("Content-Length: 17", reader.readLine());
    assertEquals("", reader.readLine());
    assertEquals("some body to love", reader.readLine());
  }

  @Test
  public void testReadLinesUntilEmptyLine() throws IOException {
    StreamReader reader =
            new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    assertEquals(Arrays.asList("GET / HTTP/1.0", "Content-Length: 17"), reader.readLinesUntilEmptyLine());
  }

  @Test
  public void testReadNChars() throws IOException {
    StreamReader reader =
            new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    reader.readLinesUntilEmptyLine();

    assertEquals("some body to love", reader.readNChars(17));
  }
}
