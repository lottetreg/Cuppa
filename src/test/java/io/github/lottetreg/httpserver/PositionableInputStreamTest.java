package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PositionableInputStreamTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void getPositionReturnsThePositionWithinTheInputStream() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("something".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    assertEquals(0, positionableInputStream.getPosition());

    positionableInputStream.read();

    assertEquals(1, positionableInputStream.getPosition());
  }

  @Test
  public void itReadsFromTheSetPosition() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("something".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    positionableInputStream.setPosition(3);

    assertEquals("e", Character.toString((char)positionableInputStream.read()));
  }

  @Test
  public void setPositionSetsThePositionWithinTheInputStream() {
    InputStream inputStream = new ByteArrayInputStream("something".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    positionableInputStream.setPosition(2);

    assertEquals(2, positionableInputStream.getPosition());
  }

  @Test
  public void readLineReturnsEmptyStringIfTheStreamIsEmpty() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    assertEquals("", positionableInputStream.readLine());
  }

  @Test
  public void readLineWorksForLinesEndingWithCRLF() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("something\r\nsomething else".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    assertEquals("something", positionableInputStream.readLine());
    assertEquals("something else", positionableInputStream.readLine());
  }

  @Test
  public void readLineWorksForLinesEndingWithDoubleCRLF() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("something\r\n\r\n".getBytes());
    PositionableInputStream positionableInputStream = new PositionableInputStream(inputStream);

    assertEquals("something", positionableInputStream.readLine());
    assertEquals("", positionableInputStream.readLine());
  }

  @Test
  public void constructorThrowsExceptionIfItFails() {
    class MockInputStream extends InputStream {
      public int read() throws IOException {
        throw new IOException();
      }
    }

    exceptionRule.expect(PositionableInputStream.CreationFailedException.class);
    exceptionRule.expectMessage("Failed to create new PositionableInputStream");

    new PositionableInputStream(new MockInputStream());
  }
}
