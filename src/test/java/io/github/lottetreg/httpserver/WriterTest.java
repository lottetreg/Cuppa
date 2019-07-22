package io.github.lottetreg.httpserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

import java.io.*;

public class WriterTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itWritesToTheConnection() {
    class MockConnection extends BaseMockConnection{
      private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      @Override
      public OutputStream getOutputStream() {
        return this.outputStream;
      }
    }

    Connectionable connection = new MockConnection();

    new Writer().write(connection, "Some other string");

    assertEquals("Some other string", connection.getOutputStream().toString());
  }

  @Test
  public void itReThrowsAFailedToGetOutputStreamException() {
    class MockConnection extends BaseMockConnection {
      @Override
      public OutputStream getOutputStream() {
        throw new Connection.FailedToGetOutputStreamException(new Throwable());
      }
    }

    Connectionable connection = new MockConnection();

    exceptionRule.expect(Connection.FailedToGetOutputStreamException.class);

    new Writer().write(connection, "");
  }

  @Test
  public void itThrowsAnExceptionIfItFails() {
    class MockConnection extends BaseMockConnection {
      @Override
      public OutputStream getOutputStream() {
        throw new RuntimeException();
      }
    }

    Connectionable connection = new MockConnection();

    exceptionRule.expect(Writer.FailedToWriteToConnectionException.class);

    new Writer().write(connection, "");
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
