package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

import java.io.*;

public class ReaderTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itReadsFromTheConnection() {
    class MockConnection extends BaseMockConnection {
      @Override
      public InputStream getInputStream() {
        String input = "Some string";
        return new ByteArrayInputStream(input.getBytes());
      }
    }

    Connectionable connection = new MockConnection();

    assertEquals("Some string", new Reader().read(connection));
  }

  @Test
  public void itReThrowsAFailedToGetInputStreamException() {
    class MockConnection extends BaseMockConnection {
      public InputStream getInputStream() {
        throw new Connection.FailedToGetInputStreamException(new Throwable());
      }
    }

    Connectionable connection = new MockConnection();

    exceptionRule.expect(Connection.FailedToGetInputStreamException.class);

    new Reader().read(connection);
  }

  @Test
  public void itThrowsAnExceptionIfItFails() {
    class MockConnection extends BaseMockConnection {
      public InputStream getInputStream() {
        throw new RuntimeException();
      }
    }

    Connectionable connection = new MockConnection();

    exceptionRule.expect(Reader.FailedToReadFromConnectionException.class);

    new Reader().read(connection);
  }

  private class BaseMockConnection implements Connectionable {
    public OutputStream getOutputStream() {
      return new ByteArrayOutputStream();
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(new byte[] {});
    }
  }
}
