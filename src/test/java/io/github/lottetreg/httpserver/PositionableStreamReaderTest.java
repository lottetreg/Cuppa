package io.github.lottetreg.httpserver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class PositionableStreamReaderTest {
  private String request =
          "GET / HTTP/1.0\r\n" +
          "Content-Length: 17\r\n" +
          "\r\n" +
          "some body to love";

  private PositionableInputStream inputStream = new PositionableInputStream(
          new ByteArrayInputStream(this.request.getBytes()));

  @Test
  public void testReadFirstLine() throws IOException {
    PositionableStreamReader reader = new PositionableStreamReader(this.inputStream);

    PositionableStreamReader.StringData initialLineData = reader.readFirstLine();

    assertEquals("GET / HTTP/1.0", initialLineData.value);
    assertEquals(16, initialLineData.endPosition);
  }

  @Test
  public void testReadToEmptyLine() throws IOException {
    PositionableStreamReader reader = new PositionableStreamReader(this.inputStream);

    PositionableStreamReader.ListData headersData = reader.readToEmptyLine(16);

    assertEquals(Arrays.asList("Content-Length: 17"), headersData.value);
    assertEquals(38, headersData.endPosition);
  }

  @Test
  public void testReadToEmptyLineWithMultipleLinesBeforeEmpty() throws IOException {
    String request =
            "GET / HTTP/1.0\r\n" +
            "Content-Length: 17\r\n" +
            "Some-Other-Header: Hey\r\n" +
            "\r\n" +
            "some body to love";

    PositionableInputStream inputStream = new PositionableInputStream(
            new ByteArrayInputStream(request.getBytes()));

    PositionableStreamReader reader = new PositionableStreamReader(inputStream);

    PositionableStreamReader.ListData headersData = reader.readToEmptyLine(16);

    assertEquals(Arrays.asList("Content-Length: 17", "Some-Other-Header: Hey"), headersData.value);
    assertEquals(62, headersData.endPosition);
  }

  @Test
  public void testReadNBytes() throws IOException {
    PositionableStreamReader reader = new PositionableStreamReader(this.inputStream);

    PositionableStreamReader.StringData bodyData = reader.readNBytes(38, 17);

    assertEquals("some body to love", bodyData.value);
    assertEquals(55, bodyData.endPosition);
  }
}
