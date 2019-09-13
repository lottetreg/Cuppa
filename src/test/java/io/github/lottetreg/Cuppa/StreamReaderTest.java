package io.github.lottetreg.Cuppa;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StreamReaderTest {
  private String data = "First Line\r\n" + "Second Line\r\n" + "\r\n" + "Last Line";

  @Test
  public void testReadLine() throws IOException {
    StreamReader reader =
        new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    assertEquals("First Line", reader.readLine());
    assertEquals("Second Line", reader.readLine());
    assertEquals("", reader.readLine());
    assertEquals("Last Line", reader.readLine());
  }

  @Test
  public void testReadLinesUntilEmptyLine() throws IOException {
    StreamReader reader =
        new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    assertEquals(Arrays.asList("First Line", "Second Line"), reader.readLinesUntilEmptyLine());
  }

  @Test
  public void testReadNCharsWithPartialLengthOfRemainingChars() throws IOException {
    StreamReader reader =
        new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    reader.readLinesUntilEmptyLine();

    assertEquals("Last", reader.readNChars(4));
  }

  @Test
  public void testReadNCharsWithWholeLengthOfRemainingChars() throws IOException {
    StreamReader reader =
        new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    reader.readLinesUntilEmptyLine();

    assertEquals("Last Line", reader.readNChars(9));
  }

  @Test
  public void testReadNCharsWith0Length() throws IOException {
    StreamReader reader =
        new StreamReader(new ByteArrayInputStream(this.data.getBytes()));

    reader.readLinesUntilEmptyLine();

    assertEquals("", reader.readNChars(0));
  }
}
