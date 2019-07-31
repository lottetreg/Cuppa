package io.github.lottetreg.httpserver;

import io.github.lottetreg.httpserver.support.RequestBuilder;
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
        String request = new RequestBuilder()
            .setMethod("POST")
            .setPath("/")
            .addHeader("Content-Length: 17")
            .setBody("some body to love")
            .build();

        return new ByteArrayInputStream(request.getBytes());
      }
    }

    Connectionable connection = new MockConnection();

    HTTPRequest request = new Reader().read(connection);

    assertEquals("some body to love", request.getBody());
  }

  @Test
  public void readReturnsAnHTTPRequestWithAnEmptyBody() {
    class MockConnection extends BaseMockConnection {
      @Override
      public InputStream getInputStream() {
        String request = new RequestBuilder()
            .setMethod("GET")
            .setPath("/")
            .build();

        return new ByteArrayInputStream(request.getBytes());
      }
    }

    Connectionable connection = new MockConnection();

    HTTPRequest request = new Reader().read(connection);

    assertEquals("", request.getBody());
  }

  @Test
  public void itReThrowsAFailedToGetInputStreamException() {
    class MockConnection extends BaseMockConnection {
      public InputStream getInputStream() {
        throw new Connection.FailedToGetInputStream(new Throwable());
      }
    }

    Connectionable connection = new MockConnection();

    exceptionRule.expect(Connection.FailedToGetInputStream.class);

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

    exceptionRule.expect(Reader.FailedToReadFromConnection.class);

    new Reader().read(connection);
  }

  private class BaseMockConnection implements Connectionable {
    public OutputStream getOutputStream() {
      return new ByteArrayOutputStream();
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(new byte[]{});
    }
  }
}
