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
  public void readReturnsAnHTTPRequest() {
    class MockConnection extends BaseMockConnection {
      @Override
      public InputStream getInputStream() {
        String request =
                "GET / HTTP/1.0\r\n" +
                "Content-Length: 17\r\n" +
                "\r\n" +
                "some body to love";

        return new ByteArrayInputStream(request.getBytes());
      }
    }

    Connectionable connection = new MockConnection();

    HTTPRequest request = new Reader().read(connection);

    assertEquals("some body to love", request.body);
  }

  @Test
  public void readReturnsAnHTTPRequestWithAnEmptyBody() {
    class MockConnection extends BaseMockConnection {
      @Override
      public InputStream getInputStream() {
        String request =
                        "GET / HTTP/1.0\r\n" +
                        "\r\n";

        return new ByteArrayInputStream(request.getBytes());
      }
    }

    Connectionable connection = new MockConnection();

    HTTPRequest request = new Reader().read(connection);

    assertEquals("", request.body);
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
